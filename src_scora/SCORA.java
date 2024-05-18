import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Array;
//import java.nio.file.Path;
//import java.nio.file.Paths;
import java.util.Arrays;
import java.util.zip.Inflater;
import java.util.zip.InflaterOutputStream;

import javax.imageio.ImageIO;


public class SCORA {

	private static final String SCORA_Version = "1.2c";

	public static  interface Exit {
		final static int ALL_OKAY = 0;
		final static int NOARGS = 1;
		final static int INVALID_OPTION = 2;
		final static int ERR_OPENFILE = 3;
		final static int ERR_READWRITE_FILE = 4;
	}
	public int ExitCode;

	private Throwable myErrFileFormat = new Throwable("InvalidFileFormat");
//	private Throwable myErrCommandline = new Throwable("InvalidCommandline");

	
	private static final String MAGIC_SCORCH = "CCSCORCH";
	private static final String MAGIC_SIBELIUS = "SIBELIUS";

	private String magic2;
	private boolean isScorchFile;

	private static String in_FileName;
	private File inFile;
	private RandomAccessFile din;
	private CryptedStream CryptedIn;

	// private DataInputStream din;

	private static String out_FileName;
	private File outFile;
//	private BufferedOutputStream out;
	private DataOutputStream dout;

	// private boolean DoDecompress = false;
	private boolean option_Decompress	= true;
	private boolean option_Unlock     	= true;

	private boolean option_CreateHTML 	= false;
	private boolean option_OutputChunksAsFile = false;
	private boolean option_NoImages 		= false;
	private boolean option_Verbose 		= false;
	
	
	private static int LogIndentLevel;
	private final static int LogIndentSpace = 4;
	
	private boolean isEncrypted;
	private boolean isWrittenSomethingUsefullToOutputFile;
	private boolean DeleteOutputFileBecauseITS_A_PDF_Wrapper;
	private int ChunkType; //sub-chunk type
	public interface CHUNK {
		final static int sib0 = 0;
		final static int sib = 1;
		final static int lib = 2;
		final static int sib3 = 3;
		final static int Idea_Dir = 4;
		final static int Idea_Text = 5;
		final static int Idea_sib = 6;
		final static int Idea_png = 7;
		final static int SCORCH_PDF_DIR = 8;
		final static int SCORCH_PDF_DRM = 9; //CHUNK_SCORCH_PDF_DRM
		final static int SCORCH_PDF_DATA = 10; // CHUNK_SCORCH_PDF_DATA
		final static int Versions_Dir = 11;
		final static int Version_Dir = 12;
		final static int Version_Descr = 13;
	}


	long BenchMarkStart;
	private void b() {
		BenchMarkStart = System.nanoTime();
	}

	private void e() {
		System.out.println("SCORA.main() tooked: " + (System.nanoTime() - BenchMarkStart) / 100000   );
	}
	

	public static SCORA mySCORA;
	
	public static void main(String[] args) {
		
		String JREver = System.getProperty("java.version");
	//	JREver = "1.5.0_31";	
		if ( JREver.compareTo("1.6" ) < 0) {
			LogOutputErr(	"JAVA Version WARNING: You currently have JAVA " + JREver + " installed.\n" + 
								"SCORA was written and tested for at least JAVA 1.6\n" + 
								"Please update ya Java incase you get errors like 'NoClassDefFound' or 'ClassNotFoundException' !\n\n");
		}
		
		mySCORA = new SCORA();
		try {
			
			try {
				ProcessCommandline(args);
			} catch (Exception e) {}
	
			try {
				
				openInputFile();
				openOutputFile();
	
			} catch (IOException e1) {
				LogOutputErr(e1.toString() + " - Current dir: \n "
						+ System.getProperty("user.dir"));
	
				Terminate(Exit.ERR_OPENFILE);
			}
	
			
			
			
			
			mySCORA.run();
		} catch (Exception e) {}
		
		System.exit (mySCORA.ExitCode);
	}
	public static void Terminate(int ExitCode) throws Exception {

		
		if ((mySCORA.isWrittenSomethingUsefullToOutputFile == false) ||
		    (mySCORA.DeleteOutputFileBecauseITS_A_PDF_Wrapper == true) ){
			
			try {
				mySCORA.dout.close();
				mySCORA.outFile.delete();
			} catch (Exception e){}

		} else {
			
			LogOutput( "Done! ");
			LogOutputVerbose( "OutputFile: " + out_FileName );

			if (mySCORA.option_CreateHTML) createHtmlFile();

		}
		
		mySCORA.ExitCode = ExitCode;
		throw new Exception("ExitRequest") ;

		
	}


	public static String FileNameOnly(String Filename) {
		String[] tmpArr = Filename.split("\\\\");
		int LastIndex = Array.getLength(tmpArr) - 1; 
		return tmpArr[LastIndex];
	}

	
	public static void createHtmlFile() {
		ScorchHTML Html = new ScorchHTML();
		Html.Set("TITLE", "Song");
		Html.Set("COMPOSER", "someone");
		Html.Set("HEIGHT", "800");
		Html.Set("WIDTH", "600");
		
		Html.Set("FILENAME", FileNameOnly(out_FileName));
		
		String HtmlFileName;
		
		HtmlFileName= in_FileName + ".html";
		try {
			
			Html.SaveToFile( HtmlFileName );
			LogOutput( "Html file: " + HtmlFileName );

		} catch (IOException e) {
			LogOutputErr("Writing HtmlFile failed! /n" + e.toString());
		}
		
	}
	
	public static void ProcessCommandline (String[] args) throws Exception {

		ShowTitle();

		if (args.length == 0) {
			showHowToUse();
			Terminate(Exit.NOARGS);
		}


		for (String arg : args) {

			if (arg.startsWith("-")) {

				String option = arg.substring(1);
				if (option.equalsIgnoreCase("noDecompress"))
					mySCORA.option_Decompress = false;

				else if  (option.equalsIgnoreCase("HTML")) {
//					LogOutputErr( "Sorry Option '" + arg + "' not implemented yet." );
					mySCORA.option_CreateHTML = true;
				}
				else if  (option.equalsIgnoreCase("NoImages")) {
					mySCORA.option_NoImages = true;
				}
				else if  (option.equalsIgnoreCase("chunks")) {
					mySCORA.option_OutputChunksAsFile = true;
				}
				else if  (option.equalsIgnoreCase("verbose")) {
					mySCORA.option_Verbose = true;
				}
				else if  (option.equalsIgnoreCase("noUnlock")) {
					mySCORA.option_Unlock = false;
				}

				else {
					LogOutputErr( "invalid option: " + arg );
					Terminate(Exit.INVALID_OPTION);
				}

			} else {
				if (in_FileName == null ) {
					in_FileName  =  arg ;
					out_FileName = in_FileName + ".sib";
				
				} else if (0 == in_FileName.compareToIgnoreCase(  arg  ) )
					 LogOutputErr( "'"+ arg +"' as outputname ignored. Overwriting inputfile is not supported." );
				 
				else
					out_FileName = arg ;
			}
//			if (in_FileName == null ) {
//				in_FileName  = MakePath( arg );
//				out_FileName = MakePath( in_FileName.toString() + ".sib");
//				
//			} else if (0 == in_FileName.compareTo( MakePath( arg ) ) )
//				LogOutputErr( "'"+ arg +"' as outputname ignored. Overwriting inputfile is not supported." );
//			
//			else
//				out_FileName = MakePath( arg );
//		}
		}
		if (in_FileName == null ) {
			LogOutputErr( "You need to specify an file for input" );
			Terminate(Exit.INVALID_OPTION);
		}
	}

//	public static Path MakePath(String pathString, String... more) {
//		return( Paths.get( pathString, more ).normalize() );
//	}

	public static void ShowTitle() {
		String appTitle = "Scorch-Away Version " + SCORA_Version;
		LogOutput(appTitle);
		LogOutput(StringGenNFill(appTitle.length(), '='));
		LogOutput("");
	}

	public static void showHowToUse() {
		LogOutput("Converts a Avid Scorch (*.sco) music score file into Avid Sibelius file (*.sib).");

		LogOutput("\n So you can edit open & print it in Avid Sibelius without any restriction ! \n"
				+ " ... or edit the scorch restriction (at the end of the file) with a (Hex-)Editor \n"
				+ " and  open it in the scorch webplugin via the generate *.htm.");

		LogOutput("\n Note: You can also open a *.sib to remove its encryption and compression.\n"
				+ "      Now it's open to analyze/customization!\n"
				+ "      ... and you gain a better compression rate when packing it with\n"
				+ "      Rar or 7-zip/LZMA.");

		LogOutput("\n\nusage: Scorch.jar <*.sco|*.sib file>  [<OutputfileName>]\n");
		LogOutput(  "\noptions:" +
				    "\n -noDecompress   Don't decompress the decrypted data" +
				    "\n -NoUnlock       Keep Scorch options as they are." +
				    "\n -chunks         Redirects decompressed chunk data into new files" +
				    "\n                 (useful for *.idea or *.lib and *.sco with embedded PDF)" +
				    "\n -NoImages       Skip dumping images from extracted data" +
				    "\n -verbose        Shows additional information( & save raw image data)" +

				    
				    "\n -HTML           Create Html to run the file in Scorch"
				  );

	}

	public static void openInputFile() throws IOException {
		if ((in_FileName == null) || (in_FileName.toString() == ""))
			throw new IllegalArgumentException();
		
		mySCORA.inFile = new File( in_FileName.toString() );
		mySCORA.din = new RandomAccessFile(mySCORA.inFile, "r");
		// din = new DataInputStream(in);

		// Test File Exists?
		if (mySCORA.din.length() == 0)
			throw new IOException();

	}

	public static void openOutputFile() throws IOException {
		if ((out_FileName == null) || (out_FileName.toString() == ""))
			throw new IllegalArgumentException();

		mySCORA.outFile = new File(out_FileName.toString());
		mySCORA.dout = new DataOutputStream(
			   new BufferedOutputStream( new FileOutputStream(mySCORA.outFile)));

	}

	public static void LogOutput(String Logtext) {
		System.out.println( StringGenNFill( LogIndentLevel * LogIndentSpace , ' ' ) +  Logtext);
	}
	
	public static void LogOutputVerbose(String Logtext) {
		if (mySCORA.option_Verbose) {
			LogOutput(Logtext);
		}
	}

	public static void LogOutputErr(String Logtext) {
		System.err.println(Logtext);
	}

	public static String StringGenNFill(int len, char fill) {
		if (len < 0)
			return null;

		char[] cs = new char[len];
		Arrays.fill(cs, fill);
		return new String(cs);
	}
	

	
	
	
	
	public SCORA() {}
	
	public void run() {
		try {
			//		b();
	
	
			try {
	
				readData();
				closeFile();
	
			} catch (IOException e) {
	
				if ( (e.getCause() != null)  && (e.getCause().equals(myErrFileFormat)) ){
					
					LogOutputErr( myErrFileFormat.getMessage() + ": " + e.getMessage() ); 
				
				} else {
				
					e.printStackTrace();
				
				}
				Terminate(Exit.ERR_READWRITE_FILE);
			}
	
			Terminate(Exit.ALL_OKAY);

		} catch (Exception e_Gen) {
	//		e_Gen.printStackTrace();

		}
	}

		
	public void TransferUncompressedData(int inDataEndOffset) throws IOException {
	
		// Copy uncompressed Data
		int In_Pos = (int) din.getFilePointer();
		if ( inDataEndOffset > In_Pos ) {
			int BytesToCopy = inDataEndOffset - In_Pos;
			LogOutputVerbose(String.format("Copying Uncompressed Data @ %08X Length: %08X to OutputFile",In_Pos ,BytesToCopy ));
			
			byte[] buff = new byte[BytesToCopy];
			din.read(buff);
			dout.write(buff);
		}
		else if ( inDataEndOffset == In_Pos ); //Do Nothing here 
		else;

	}
	
	public void readData() throws IOException {
		OpenDir(0);

		TransferUncompressedData((int) din.length());
	}

	public void OpenDir(int startOffset) throws IOException {

		
		din.seek(startOffset);

		int items;
		items = openChunk();
		
		RandomAccessFile dirReader;
		RandomAccessFile dirWriter;
		
		if (items >= 1 ) {
			dirReader = new RandomAccessFile (inFile, "r");
			dirReader.seek(din.getFilePointer());

			dirWriter = new RandomAccessFile (outFile, "rw");
			dirWriter.seek(dout.size());
			
			// seek to end of dir
			for (int i = 0; i < items; i++) {
				dout.writeInt(int32());
				dout.writeInt(int32());
			}
		
			for (int i = 0; i < items; i++) {
				
				ChunkType = dirReader.readInt();

				int Offset = dirReader.readInt();
				if (Offset < 0x1b) break;

				TransferUncompressedData(Offset);
				
				int NewOffset = (int) dout.size();
				
				LogOutput(String.format("#%X Type: %X [" + GetTypeExt() + "]  @ %08X -OutputFile-> %08X",i , ChunkType, Offset, NewOffset));

				dout.flush(); //That's really important since dout is buffered
				dirWriter.writeInt(ChunkType);
				dirWriter.writeInt(NewOffset);

				LogIndentLevel++;
				OpenDir( Offset );
				LogIndentLevel--;
				
			}

		}

	}

	public int openChunk() throws IOException {

		isScorchFile = TestHeader();
		LogOutput("");
		LogOutput(String.format("Magic: %s", magic2));
		dout.write(0xf);
		dout.writeBytes(MAGIC_SIBELIUS + "\0");

		int Version = int32();
		LogOutput(String.format("Version: %08X", Version));
		showVersionInfos(Version);
		dout.writeInt(Version);

		if (Version > 6) {
			int Timestamp = int32();
			LogOutputVerbose(String.format("Timestamp: %08X", Timestamp));
			dout.writeInt(Timestamp);
		}

		if (Version < 5)
			throw new IOException(
					"Support for Sibelius fileformat below Version 2 not implemented yet!", myErrFileFormat);

		int Mode = int32();
		LogOutputVerbose(String.format("Mode: %08X", Mode));

		Boolean isCompressed = (Mode & 8) != 0;
		LogOutputVerbose(" -> isCompressed: " + isCompressed);
		if (option_Decompress)
			Mode = Mode & ~8;

		isEncrypted = (Mode & 2) != 0;
		LogOutputVerbose(" -> isEncrypted: " + isEncrypted);
		Mode = Mode & ~2;

		dout.writeInt(Mode);

		if (isCompressed) {
			int Compressinfo = int32();
			LogOutputVerbose(String.format("Compressinfo: %08X", Compressinfo));
			if (option_Decompress == false )	dout.writeInt(Compressinfo);

		}

		if (isEncrypted) {
			CryptedIn = new CryptedStream(din, isScorchFile) ;
		}

		int Items = 0;

		if (Version > 0x210000) {
			Items = int32();
			LogOutputVerbose(String.format("Items: %08d", Items));
			dout.writeInt(Items);
		}

		
     // OutputBuffer[]
		ByteArrayOutputStream OutputBuffer = new ByteArrayOutputStream() ;
		ByteArrayOutputStream DecompBuff = new ByteArrayOutputStream() ;
		if (isCompressed) {
			decryptNDecompress( OutputBuffer, DecompBuff );
		}
		
		if (DecompBuff.size() > 0 ) {
			// Dump embedded PDF's from *.sco (ScorchPDFWrapper)
			if ( ChunkType == CHUNK.SCORCH_PDF_DATA) {
				// Make FileName & Create File / ChunkOut
				String TmpFileName = String.format( in_FileName +
									 				"." +  GetTypeExt() 
									 				);
				OutputStream ChunkOut =  new FileOutputStream( TmpFileName );
				ChunkDumpData(DecompBuff.toByteArray(), ChunkOut);
				DeleteOutputFileBecauseITS_A_PDF_Wrapper = true;
				
			} else if ( (ChunkType == CHUNK.sib) || 
					    (ChunkType == CHUNK.sib0) ) {
	
				if (option_Unlock) {
					
					if (option_Decompress == false) 
						LogOutputErr("Warning: Remove scorch restrictions will not work because the option 'noDecompress' was set!" );
					
					RestrictionsRemove(OutputBuffer);
				}
			}
	
			
			
			// write data to File
			if (DeleteOutputFileBecauseITS_A_PDF_Wrapper == false) 
				OutputBuffer.writeTo(dout);
			
			if (option_NoImages == false)
				PicturesFind(OutputBuffer.toByteArray());
			
			
	}	
		
		
		return (Items);
	}

	public String GetTypeExt() {
		
		String retval;
		
		switch (ChunkType) {
		//0
		case CHUNK.sib0:
			retval = "sib0";
			break;

		//1
		case CHUNK.sib:
			retval = "sib";
			break;

		//2
		case CHUNK.lib:
			retval = "lib";
			break;
		//3
		case CHUNK.sib3:
			retval = "3sib";
			break;

			
		//4
		case CHUNK.Idea_Dir:
			retval = "Idea_Dir";
			break;
			//5		
			case CHUNK.Idea_Text:
				retval = "Idea_Text";
				break;
			//6
			case CHUNK.Idea_sib:
				retval = "Idea_sib";
				break;
		    //7 png
			case CHUNK.Idea_png:
				retval = "Idea.png";
				break;

			
		//8
		case CHUNK.SCORCH_PDF_DIR:
			retval = "Pdf_Dir";
			break;
			//9 RestrictionFile in *.sco
			case CHUNK.SCORCH_PDF_DRM:
				retval = "txt";
				break;
			//10 Embedded File in *.sco
			case CHUNK.SCORCH_PDF_DATA:
				retval = "pdf";
				break;
			
		//11
		case CHUNK.Versions_Dir:
			retval = "Versions_Dir";
			break;
			
			//12
			case CHUNK.Version_Dir:
				retval = "Version_Dir";
				break;
			//13
			case CHUNK.Version_Descr:
				retval = "Version_Descr";
				break;
			
		default:
			retval = "T" + ChunkType;

		}

		return( retval );
	}

	public void showVersionInfos(int Version) {

		int VersionMaj;
		int VersionMajRaw = Version >> 16;
		switch (VersionMajRaw) {
		case 0x0039:
			VersionMaj = 7;
			break;

		case 0x0036:
			VersionMaj = 6;
			break;

		case 0x002D:
			VersionMaj = 5;
			break;

		case 0x0028:
			VersionMaj = 0;
			break;

		case 0x001C:
			Version = 0x1B0015;

		case 0x001B:
			VersionMaj = 4;
			break;

		case 0x000A:
			VersionMaj = 3;
			break;

		case 0x0008:
			VersionMaj = 2;
			break;

		case 0x0002:
			VersionMaj = 1;
			break;

		default:
			VersionMaj = -1;

		}

		LogOutput(" -> Sibelius " + VersionMaj);
	}



	public void DumpData(ByteArrayOutputStream OutputBuffer) {
		byte[] SearchBuff = OutputBuffer.toByteArray();
		
	}

	
	public void RestrictionsRemove(ByteArrayOutputStream OutputBuffer) {
		
		byte[] SearchBuff = OutputBuffer.toByteArray();

		try {
			RestrictionsRemoveText(SearchBuff , "ViewAllPages");
			RestrictionsRemoveText(SearchBuff , "PlayOptionType");
			RestrictionsRemoveText(SearchBuff , "PrintMode");
			RestrictionsRemoveText(SearchBuff , "ViewMode");
			RestrictionsRemoveText(SearchBuff , "ViewPages");
			RestrictionsRemoveText(SearchBuff , "SecurityScript");
	
			/*To unsafe to apply
			final int GlobalSnap_POS = 0x6A;
			byte GlobalSnap = SearchBuff[GlobalSnap_POS];
			if ( GlobalSnap == 0 ) {
				SearchBuff[GlobalSnap_POS] = 1;
			} else if ( GlobalSnap == 1 ) {
			} else {
				LogOutputErr("ScorchRemoveRestrictions enable Save&Print for sib-files fail. \n" + 
							 "Expected values for 'GlobalSnap' are only 00 and 01. Possible the offset is wrong/changed due to untested scorch versions. \n" +
							 "Please send me an email about that error and include the file.");
			}
	*/
			OutputBuffer.reset();
			OutputBuffer.write(SearchBuff);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int bytes2Int(byte[] b, int offset) {
		return ( 
				( (b[offset    ] & 0xff) << 030 ) |
				( (b[offset + 1] & 0xff) << 020 ) |
				( (b[offset + 2] & 0xff) << 010 ) |
			    ( (b[offset + 3] & 0xff)        ) );
	}
	private String PictureSaveRAW_MakeFileName(int Offset, int ResX, int ResY) {
		return String.format("Pic_%08X_32BPP_ARGB_%d x %d ", 
				Offset, ResX, ResY);
	}
	
	public void PictureSaveRAW( byte[] PicData_RGBA, int size,
									  int ResX, int ResY, int FileOffset_JustForInfo) throws IOException {
		
		String PicFileName = PictureSaveRAW_MakeFileName(FileOffset_JustForInfo, ResX, ResY);
		LogOutput(String.format("Raw picturedata %s", PicFileName));
		
		
		DataOutputStream RawPicFile = new DataOutputStream( new FileOutputStream( PicFileName + ".raw") );
		RawPicFile.write(PicData_RGBA, 0, size);
		RawPicFile.close();

		
	}
	

	private String PictureSavePNG_MakeFileName(int Offset, int ResX, int ResY) {
		return String.format("Pic_%08X_%d x %d ", 
				Offset, ResX, ResY);
	}

	public void PictureSavePNG( byte[] PicData, int size,
			  int ResX, int ResY, int FileOffset_JustForInfo) throws IOException {

			int pixels = ResX * ResY;

		 // the Raster object don't works with byte[] so create a int[] array
			int[] PicData_ARGB_int = new int[pixels];
		
		 // Prepare alpha-channel check (it checks if there are any different values in the alpha-Channel)
			boolean IsAlphaUsed = false;
			int i = 0;
			int A_Init = PicData[i] << 030; //shift over 3 byte's

		 //	Copy ( A,R,G,B,...)ByteArray to ->  ( ARGB,...)IntArray
		 // Flip image horizontally (By copying/fill the Y-lines in reverse order)
			for (int pos_line  = pixels - ResX;  pos_line >= 0 ; pos_line-= ResX) {

				for (int x = 0; x < ResX; x++) {
					
					int ARGB = bytes2Int(PicData,i); i+=4;

					PicData_ARGB_int[ pos_line + x ] = ARGB;
					
					int A = (ARGB & 0xff000000) ;
					if (A != A_Init) IsAlphaUsed = true;

				}
			}

			BufferedImage imgOut = new BufferedImage( ResX, ResY, IsAlphaUsed ? BufferedImage.TYPE_INT_ARGB: 
																			    BufferedImage.TYPE_INT_RGB );
			imgOut.getRaster().setDataElements(0, 0, imgOut.getWidth(), imgOut.getHeight(), PicData_ARGB_int);

			String sImgFilename = PictureSavePNG_MakeFileName(FileOffset_JustForInfo, ResX, ResY);
		    try {

		      ImageIO.write( imgOut, "png", new File( sImgFilename + ".png") );
		    } catch( Exception ex ) {
		      LogOutputErr ( "\nError: Image storing to '" + sImgFilename + "' failed: " + ex.getMessage() );
		    }
		
}

	
//  Heuristically search for images - since there is no easy 'normal' way for that
//  the standard way would required to read (read over) all data with fixed sizes + caring about file version differences
//  
//	CheckType 0x5c
//  Read "Bits"
//		ReadINT32 Size
//	    ReadINT32 PicData (Size* 4)
//
// "GraphicDataList"
//
//	ReadINT32 "Width"
//	ReadINT32 "Checksum0"
//	ReadINT32 "Checksum1"
//	ReadINT32 "Checksum2"
//	ReadINT32 "Checksum3"
//  if Ver > 80000 (Sib v2)
//	ReadFloat "XPixelsPerMM"  (0x4071EB85 -> 3.780000)
//	ReadFloat "YPixelsPerMM"  (0x4071EB85 -> 3.780000)
//	Read7bitPacked SizeAddData 
	
	
	public void PicturesFind(byte[] Buffer) throws IOException {

		
		ByteArrayInputStream ISInputBuffer = new ByteArrayInputStream(Buffer);
        DataInputStream in = new DataInputStream(ISInputBuffer);

		
		final byte IMG_CHUNKID = 0x5c;
		final int ImgMinSize = 0x8;

		int offset = 0;

		// Scan buffer for 'IMG_CHUNKID' byte that marks the start of a picture
		int tmpByte;
		while (-1 != (tmpByte = in.read() )) {
			offset++;
			if (tmpByte == IMG_CHUNKID)  {
				
				// Get the picture size (Number of pixels)
				in.mark(-1);
				
					int pixel = in.readInt();
					
				in.reset();
				//(pixel == 0x01000000);
				// one pixel is 4 Byte[A,R,B,G] ('pixel << 2' is same as 'pixel * 4')	
				int size = pixel << 2;
				
				int ImgMaxSize = in.available();
				boolean isRangeOk = (size >= ImgMinSize ) && (size <= ImgMaxSize);
				
				if (isRangeOk ) {
					
					//Read ahead width
					in.mark(-1);

						in.skipBytes(size + 4); // '+ 4' because of 'pixel = in.readInt();'
						int Width = in.readInt();

					in.reset();

					isRangeOk = (Width >= 1 ) && (Width < pixel); // Note: ...(Width <= pixel) will allow pic's with 1 pixel height

					if ( isRangeOk && ( (pixel % Width) == 0 ) ) {
						
						//pixel = in.readInt();
						in.skipBytes(4);  offset += 4;
						
						byte[] outputBuffer = new byte[size];
						in.read(outputBuffer, 0, size);

						//int Width = in.readInt();
						in.skipBytes(4);  offset += 4;

						
						int Height = pixel / Width;
						
						if (option_Verbose) 
							PictureSaveRAW(outputBuffer, size, Width, Height, offset);
						
						PictureSavePNG(outputBuffer, size, Width, Height, offset);
						
						int Checksum0 = in.readInt();
						int Checksum1 = in.readInt();
						int Checksum2 = in.readInt();
						int Checksum3 = in.readInt();
						LogOutputVerbose(String.format("Checksum: %08X, %08X, %08X, %08X", Checksum0, Checksum1, Checksum2, Checksum3 ));
						
						float XPixelsPerMM = in.readFloat() ;
						float YPixelsPerMM = in.readFloat();
						LogOutputVerbose("XPixelsPerMM: " + XPixelsPerMM);
						LogOutputVerbose("YPixelsPerMM: " + YPixelsPerMM);
						
						int AdditionalDataSize = GetInt32Packed( in );
						
						LogOutputVerbose(String.format("AdditionalDataSize: %08X", AdditionalDataSize));
						
						//TODO: show additionalData
						in.skipBytes(AdditionalDataSize);  offset += size + AdditionalDataSize;
					}
				}
			} 
			
			
		}
	} 
//	public void PicturesFind_arr(ByteArrayOutputStream BSOutputBuffer) throws IOException {
//
////		DataInput outputBuffer = new DataInput ( new ByteArrayInputStream ( BSOutputBuffer.toByteArray() ));
//		byte[] outputBuffer = BSOutputBuffer.toByteArray();
//		
//		int start;
//		final byte IMG_CHUNKID = 0x5c;
//		final int ImgMinSize = 0x8;
//
//		// Scan buffer for 'IMG_CHUNKID' byte that marks the start of a picture
//		for (int i = 0; i < outputBuffer.length; i++) {
//			
//			if (outputBuffer[i] == IMG_CHUNKID)  {
//				start = i + 1 ; // Sizeof(IMG_CHUNKID) = 1
//				
//			 // Get the picture size (Number of pixels)
//				int pixel = bytes2Int(outputBuffer, start);
//				//(pixel == 0x01000000);
//			 // one pixel is 4 Byte[A,R,B,G] ('pixel << 2' is same as 'pixel * 4')	
//				int size = pixel << 2;
//				
//				int ImgMaxSize = outputBuffer.length - start;
//				boolean isRangeOk = (size >= ImgMinSize ) && (size <= ImgMaxSize);
//				
//				if (isRangeOk ) {
//					start += 4; // Sizeof(ImgMaxSize) = 1
//					
//					int Width = bytes2Int(outputBuffer, start + size);
//					isRangeOk = (Width >= 1 ) && (Width <= pixel);
//					
//					if ( isRangeOk && ( (pixel % Width) == 0 ) ) {
//
//						int Height = pixel / Width;
//						
//						PictureSaveRAW(outputBuffer, start, size, Width, Height);
//						PictureSavePNG(outputBuffer, start, size, Width, Height);
//
//						i += size + 4;
//						
//						int Checksum0 = bytes2Int(outputBuffer, i +   04);
//						int Checksum1 = bytes2Int(outputBuffer, i +  010);						
//						int Checksum2 = bytes2Int(outputBuffer, i +  014);						
//						int Checksum3 = bytes2Int(outputBuffer, i +  020);
//						LogOutputVerbose(String.format("Checksum: %08X, %08X, %08X, %08X", Checksum0, Checksum1, Checksum2, Checksum3 ));
//						
//						float XPixelsPerMM = (float)bytes2Int(outputBuffer, i + 024);
//						float YPixelsPerMM = (float)bytes2Int(outputBuffer, i + 030);
//						LogOutputVerbose("XPixelsPerMM: " + XPixelsPerMM);
//						LogOutputVerbose("YPixelsPerMM: " + YPixelsPerMM);
//						
//						int [] AdditionalDataOffset = {i + 030};
//						int AdditionalDataSize = GetInt32Packed (outputBuffer, AdditionalDataOffset);
//						LogOutputVerbose(String.format("AdditionalDataSize: %08X", AdditionalDataSize));
//					}
//				}
//			} 
//		
//		
//		}
//	
//	} 

	private int GetInt32Packed(InputStream in) throws IOException {
		in.mark(4);
		
		byte[] buff = new byte[4];
		
			in.read(buff, 0, 4);
			
		in.reset();

		int [] IO_Offset = {0};
		int RetValue = GetInt32Packed (buff, IO_Offset);
		in.skip(IO_Offset[0]);
		return RetValue; 
	}
	private int GetInt32Packed(byte[] buff , int[] IOVar_offset) {
	
		int offs;
		int inByte;
 		int bitpos = 0;
		
		int value = 0;
	 // Loop as long as bit is '1'	
		for ( offs= IOVar_offset[0]; (offs < buff.length)  && 
				                   ( (inByte = buff[offs++] & 1)==1 ) ; 
			  bitpos+=7) {
		 // cut off last bit	
			inByte = ((inByte & 0xff) >> 1);
			
		 // shift & store in value	
			value |= inByte >> bitpos;
				
		}
		
		IOVar_offset[0] = offs;
		return value;
	}

	
	private void RestrictionsRemoveText(byte[] SearchBuff , String SearchStr) throws IOException {
//		String ReplaceStr
//		byte[] ReplaceBuff = ReplaceStr.getBytes("UTF-16BE");
		byte[] MatchBuff = SearchStr.getBytes("UTF-16BE");

		int FoundIndex = -1;
		boolean MatchFound = false;
		
		for (int i = 0; (i < SearchBuff.length - 1) /*&& (!MatchFound)*/; i++) {
			
			for (int j = 0; (j < MatchBuff.length - 2) && (MatchFound = (SearchBuff[i] == MatchBuff[j]) );	j++) {
				
				if ( MatchFound)  {
					if (j == 0) FoundIndex = i;
					i++;
				} 
			}

			//if (MatchFound) break;
			//Unlocking_Scorch 
			if (MatchFound) {
				LogOutputVerbose(String.format("Removing '%s' at 0x%08X", SearchStr, FoundIndex));
				SearchBuff[FoundIndex + 1] = 'X';

			}

		
		}
		
//		//Unlocking_Scorch 
//		if (MatchFound) {
//			LogOutputVerbose(String.format("Removing '%s' at 0x%08X", SearchStr, FoundIndex));
//			SearchBuff[FoundIndex + 1] = 'X';
//
//		}

	} 
	public void decryptNDecompress( ByteArrayOutputStream OutputBuffer,
									ByteArrayOutputStream DecompBuff   
									) throws IOException {

	  // Decompr <- DecompBuff[] <- inf
		Inflater inf = new Inflater();
		InflaterOutputStream Decompr;

//		OutputBuffer = new ByteArrayOutputStream();
//		DecompBuff   = new ByteArrayOutputStream();
		Decompr = new InflaterOutputStream(DecompBuff, inf);

		
		int curpos = (int) din.getFilePointer();
		LogOutputVerbose(String.format("Data Starts at: %08X", curpos));

		// Speed up read hack BEGIN
		// Copy 'din' to 'din_Fast'
		BufferedInputStream din_Fast = new BufferedInputStream( new FileInputStream(inFile) );
							din_Fast.skip(curpos); //Seek to curpos
		
		try {
			byte Char;
			while (inf.finished() == false) {

//				Char = din.readByte();

				Char = (byte) din_Fast.read();
				curpos++;
				
				if (isEncrypted) 
					Char ^= CryptedIn.PRNG();


				// SCORA.LogOutput(String.format("Data flow: %08X", in.getFilePointer() ));
				
 			 // Store uncompressed data in OutputBuffer if option 'write decompressed data' is disabled 
				if (option_Decompress == false)
					OutputBuffer.write(Char);
				
			 // Note: decompression is always done to find out, when data chunk is finished
				Decompr.write(Char);

			}
			
			din_Fast.close();
			DecompBuff.close();
			
			din.seek(curpos);
			// Speed up read hack END
			
			LogOutputVerbose(String.format("Data end at: %08X", curpos ));
			

		 // Copy uncompressed data to OutputBuffer
			if (option_Decompress) 
				DecompBuff.writeTo(OutputBuffer);

		 // OutputBuffer holds now decrypted (& decompressed) data
			
			if (option_OutputChunksAsFile) {
 			 // Create File / ChunkOut
				OutputStream ChunkOut =  new FileOutputStream( ChunkDumpMakeFilename( din.getFilePointer() ) );
				ChunkDumpData(DecompBuff.toByteArray(), ChunkOut);
				
			} 

		} catch (EOFException e) {
		}
		Decompr.finish();

		
		isWrittenSomethingUsefullToOutputFile = true;

		// while (true) {
		// byte ExtraChar= in.readByte();
		// SCORA.LogOutput(String.format("ExtraChar: %02X", ExtraChar));
		// }

		
		
		
	}


	public static byte[]  DataInputStreamToBytes( DataInputStream myStream ) throws IOException {
		byte[] tmp = new byte[myStream.available()];
		myStream.readFully(tmp);
		return(tmp);
	}

	private String ChunkDumpMakeFilename(long l) {
		return String.format(
		//in_FileName.getFileName().toString() + "_" +
		"File_" +
		"0x%08X_" + ChunkType + "." +  GetTypeExt() , l 
		);

		
		 
	}

	private void ChunkDumpData(byte[] data_in, OutputStream ChunkOut ) throws IOException {

		
		DataInputStream DecompBuff_as_DIStream = new DataInputStream( new ByteArrayInputStream( data_in ));

		//Plan A: Cut off leading DataSize
		int DataSize;
		DataSize = DecompBuff_as_DIStream.readInt();
		if (DataSize == DecompBuff_as_DIStream.available()) {
			LogOutputVerbose(String.format("ChunkDump: Data_Header__DataSize: %08X", DataSize));


		} else if (DataSize == 0) {
			LogOutputVerbose(String.format("ChunkDump: Cutting off : %08X at the start", DataSize));
			
		} else {

			DecompBuff_as_DIStream.reset();
			
			//Plan B: Cut off Type & leading DataSize
			int TypeSub;
			TypeSub = DecompBuff_as_DIStream.readUnsignedByte();

			//Cut off leading DataSize (Same as: Plan A...)
			DataSize = DecompBuff_as_DIStream.readInt();
			if (DataSize == DecompBuff_as_DIStream.available()) {
				LogOutputVerbose(String.format("ChunkDump: Data_Header__SubType : %02X '%c'", TypeSub, TypeSub));
				LogOutputVerbose(String.format("ChunkDump: Data_Header__DataSize: %08X", DataSize));

			} else {

				DecompBuff_as_DIStream.reset();
			}
		}

		// And BOM-marker for big-endian Unicode File
		if ( ChunkType == CHUNK.SCORCH_PDF_DRM) {
			ChunkOut.write( 0xfe);
			ChunkOut.write( 0xff);
		}
		
		ChunkOut.write( DataInputStreamToBytes(DecompBuff_as_DIStream) );
		ChunkOut.close();
	}

	
	public boolean TestHeader() throws IOException {

		int magic1;
		magic1 = int8();
		if (magic1 != 0x0f)
			throw new IOException("File must start with 0x0f", myErrFileFormat );

		boolean isScorchFile;
		magic2 = FixedString(9);
		
		if (magic2.startsWith(MAGIC_SCORCH))
			isScorchFile = true;
		
		else if (magic2.startsWith(MAGIC_SIBELIUS))
			isScorchFile = false;
		
		else
			throw new IOException(
					"FileMagic must start with 'SIBELIUS' or 'CCSCORCH'", myErrFileFormat);

		// Skip 00 at the end of SIBELIUS
		// in.skip(1);

		return isScorchFile;

	}

	public int int8() throws IOException {
		int tmp = din.read();
		if ( tmp < 0)
			throw new EOFException();
		return tmp;
	}

	public int int32() throws IOException {

		// byte[] myInt32= new byte[4];
		// if (in.read(myInt32) != 4)
		// throw new
		// IOException("Can't read all requested bytes - EOF reached?");;

		// ByteBuffer bb = ByteBuffer.wrap(myInt32);

		return din.readInt();
	}

	public String FixedString(int len) throws IOException {

		byte[] myString = new byte[len];
		if (din.read(myString) != len)
			throw new IOException(
					"Can't read all requested bytes - EOF reached?");
		;
		return new String(myString);
	}

	public void closeFile() throws IOException {
		din.close();
		dout.close();
	}

}