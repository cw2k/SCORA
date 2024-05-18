import java.io.*;
//import org.apache.commons.io.IOUtils;



public class ScorchHTML {

	private String HTMLTemplate;
	
	public ScorchHTML() {
		try {
			LoadtemplateFile("ScorchHtmlTemplate.html");
		} catch (IOException e) {}
	}

	private void LoadtemplateFile(String FileName) throws IOException {
		
		InputStream in;
		in = getClass().getResourceAsStream( FileName );
		if (in == null)  {
			System.err.println("Can't load JAR-Resource: " + FileName );
		}
		
		HTMLTemplate = convertStreamToString(in);// IOUtils.toString(in);
	}

	public void Set(String Name, String Value) {
		
		HTMLTemplate = HTMLTemplate.replace("$" + Name + "$", Value);
		
	}
	public void SaveToFile(String FileName) throws IOException {
		
//		if (HTMLTemplate.isEmpty()) return;
		
		FileOutputStream  out = new FileOutputStream( FileName );
		out.write( HTMLTemplate.getBytes("UTF8") );
		out.close();
	}
	public String convertStreamToString(InputStream is) throws IOException {
		/*
		* To convert the InputStream to String we use the
		* Reader.read(char[] buffer) method. We iterate until the
		* Reader return -1 which means there's no more data to
		* read. We use the StringWriter class to produce the string.
		*/
		if (is != null) {
			Writer writer = new StringWriter();
		 
			char[] buffer = new char[1024];
			try {
				
				Reader reader = new BufferedReader(
				
				new InputStreamReader(is, "UTF-8"));
				
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
				
			} finally {	is.close();	}
			return writer.toString();
			} else {
				return "";
			}
		}
	}