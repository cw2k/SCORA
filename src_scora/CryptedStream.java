import java.io.*;

public class CryptedStream {

//	public File CurrentFile; 
	private RandomAccessFile in; 

	private boolean isScorchFile;
	
	public CryptedStream(RandomAccessFile  file, boolean isScorchFile) throws IOException {
		
		in = file;
		
//		this.CurrentFile = file;
		this.isScorchFile = isScorchFile;
		
		if (isScorchFile)
			in.skipBytes(0x0D);
		else
			in.skipBytes(0x17);

		int CryptSeed = in.readInt();
		LogOutput(String.format("CryptSeed: %08X", CryptSeed));
		decryptSetWeed(CryptSeed);

		if (isScorchFile)
			in.skipBytes(0x11);
		else
			in.skipBytes(0x23);
		
		
	}

	private  void decryptSetWeed(int CryptSeed ) {
		PRNG_Mode = isScorchFile ? 3 : 2;
		Key = CryptSeed;
		// Round = 0;
		EKey1 = CryptSeed & 0xFFFFFFF8 ^ 523293307; // 0x1F30D27B
		EKey2 = CryptSeed & 0xFFFFFFF8 ^ 978092210; // 0x3A4C80B2
		EKeyMode = (CryptSeed & 7) << 4;

	}


	private int PRNG_Mode;
	private int Key;
	// private int Round;
	private int EKey1;
	private int EKey2;
	private int EKeyMode;

	public   int PRNG() {
		int EKeyMode_tmp;
		int EKey1_tmp;
		int PostXor[] = { 27811, 12742, 37606, 5005 };

		// Round++;
		if (PRNG_Mode == 0) { // MSVCR90.rand
			Key = Key * 214013 + 2531011;
			return (Key >> 16) & 0x7FFF;
		}
		if (PRNG_Mode == 1) {
			Key = Key * 1103515245 + 12345; // 0x3039
			return (Key >> 16) & 0x7FFF;
		}

		if (PRNG_Mode == 3)
			EKeyMode = (EKeyMode * 47485) & 0xFFFF; // 0xB97D
		else
			EKeyMode = (EKeyMode * 13821) & 0x7FFF; // 0x35FD

		EKeyMode_tmp = (EKeyMode >> 8) & 3;
		EKey1_tmp = 0;
		if (EKeyMode_tmp != 2) {
			EKey1 = EKey1 * 214013 + 2531011;
			EKey1_tmp = EKey1;
		}
		if (EKeyMode_tmp != 1) {
			EKey2 = EKey2 * 1103515245 + 12345;
			EKey1_tmp ^= EKey2;
		}
		return (PostXor[EKeyMode_tmp] ^ (EKey1_tmp >> 16) & 0x7FFF);
	}

	private  void LogOutput(String Logtext) {
//		System.out.println(Logtext);
	}
	

	public byte readByte() throws IOException {
		byte Char;
		Char = in.readByte();
		
		Char ^= (byte) PRNG();

	return Char;
	}

}
