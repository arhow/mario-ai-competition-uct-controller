package wang.agent.assistent;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream; 
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Properties;

 

/*

+--------------------------------------------------------------------- 

Config.init("php.ini"); 
Config instance = Config.getInstance(); 
instance.getConfig("PHP", "short_open_tag"); 

+--------------------------------------------------------------------- 

*/  

public class Config 
{ 
 
	private static String  filename = "mcconfig.ini";
	
	static int debuglog = 0;

	
	public static String getpath(String fileName)
	{
		return System.getProperty("user.dir") + "\\src\\wang\\agent\\assistent\\" + fileName;
	}
	
	public static String readINIFile(String key) 
	{ 
		String value = "";
		try 
		{
			Properties props = new Properties();
			props.load(new FileInputStream(getpath(filename)));
			value = props.getProperty(key);
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		} 
		
		return value;
	} 
	
	public static void writeINIFile(String key, String value) 
	{ 
		try 
		{
			Properties props = new Properties();
			props.load(new FileInputStream(getpath(filename)));
			props.setProperty(key, value);
			props.store(new FileOutputStream(getpath(filename)), "");
		} 
		catch (Exception ex) {ex.printStackTrace();} 
	}
	
	public static void writefile(String content, String filename) 
	{
		
		String path = System.getProperty("user.dir") + "\\src\\wang\\agent\\assistent\\" + filename;
		/*
		OutputStreamWriter osw;
		try 
		{
			osw = new OutputStreamWriter(new FileOutputStream(path));
			osw.write(content, 0, content.length());  
			osw.flush();  
		} catch (IOException e) 
		{
			e.printStackTrace();
		}  
		*/
		try 
		{
	        File file=new File(path);
	        if(!file.exists())
					file.createNewFile();
	        
	        FileOutputStream out;
			out = new FileOutputStream(file,true);
	        StringBuffer sb = new StringBuffer(content);
	        out.write(sb.toString().getBytes("utf-8"));
        
		} catch (IOException e) 
		{
				e.printStackTrace();
		}
  

	}
	
	

	
	 

}  

