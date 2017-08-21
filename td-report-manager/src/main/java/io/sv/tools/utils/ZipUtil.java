package io.sv.tools.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ZipUtil {

	// Staging and destination folders  
    private static String src = "C:/SRAVAN VEDALA/TESTING/TEST_DEVELOPMENT/ITAC_DATA/src/";
    private static String dest = "C:/SRAVAN VEDALA/TESTING/TEST_DEVELOPMENT/ITAC_DATA/dest/";
    
    private static final String WORK_DIR = "ZIPWorkFolder";
	
    private void createWorkDir(){
		File theDir = new File(WORK_DIR);

		  // if the directory does not exist, create it
		  if (!theDir.exists()) {
		    System.out.println("creating directory: " + WORK_DIR);
		    boolean result = theDir.mkdir();  

		     if(result) {    
		       System.out.println("DIR created");  
		     }
		  }
		  else
			  System.out.println("Found work directory. Directory creation avoided");
	}
	
    // Unzip -> Updates RTC value of gdf file -> Zip it back  
	public void updateSessionZip(String srcFile, String destFile) throws IOException{
		Path p = Paths.get(srcFile);		
		String srcFileName = p.getFileName().toString();
		p = Paths.get(destFile);
		String destFileName = p.getFileName().toString();		
		createWorkDir();
		
		// Unzip the zip
		UnZipUtil unzip = new UnZipUtil();
		unzip.recursiveUnzip(new File(srcFile), new File(WORK_DIR + "/" + srcFileName));
		unzip.removeAllZipFiles(new File(WORK_DIR));
	    System.out.println("Un zipping complete");
	    
	    // Update gdf file real time session clock
	    updateGDFFile(new File(WORK_DIR + "/"+ srcFileName));
	    
	    // Zip the session folder back 
	    ZipUtil_1.zipFile(WORK_DIR + "/"+ srcFileName, destFile, true);
	    // Clean up the work folder
	    deleteFolder(new File(WORK_DIR + "/"));
	}
	
	private void updateGDFFile(File stgLoc){		 
		File[] files = stgLoc.listFiles();
	    for (File file : files) {
	        if (file.isDirectory()) {
	            //System.out.println("Directory: " + file.getName());
	        	updateGDFFile(file); // Calls same method again.
	        } else {
	            //System.out.println("File: " + file.getName());
	            if (file.getName().contains("_gdf.xml")){
	            	updateRTC(file);
	            }
	        }
	    }
	}
	
	private void updateRTC(File file){
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();			
			if (file.exists()) {
				Document doc = db.parse(file);
				Element docEle = doc.getDocumentElement();

				// Print root element of the document
				System.out.println("Root element of the document: "
						+ docEle.getNodeName());

				NodeList dataEleList = docEle.getElementsByTagName("data");

				// Print total student elements in document
				System.out
				.println("Total data elements : " + dataEleList.getLength());


				for (int i = 0; i < dataEleList.getLength(); i++) {

					if (dataEleList.item(i).getAttributes().getNamedItem("name").getNodeValue().equals("InterrogationRealTimeClock")){
						String rtc = dataEleList.item(i).getAttributes().getNamedItem("value").getNodeValue();
						long rtcVal = Long.parseLong(rtc);
						rtc = String.valueOf(rtcVal +1);
						dataEleList.item(i).getAttributes().getNamedItem("value").setNodeValue(rtc);
						break;
					}
					
				} //for
				//createOutputFile(file.getAbsolutePath(), transformToString(doc));
				
				// write the content into xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(file);
		 
				// Output to console for testing
				// StreamResult result = new StreamResult(System.out);
		 
				transformer.transform(source, result);
				
			
			}
		}
	
		catch (Exception e) {
		System.out.println(e);}
	}
	
	private static String transformToString(Document document) {
	    try {
	        TransformerFactory transFactory = TransformerFactory.newInstance();
	        Transformer transformer = transFactory.newTransformer();
	        StringWriter buffer = new StringWriter();
	        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.transform(new DOMSource(document), new StreamResult(buffer));
	        return buffer.toString();
	    } catch (TransformerException e) {
	        throw new RuntimeException(e);
	    }
	}


	private static void createOutputFile(String filePath, String content) {
	    FileWriter writer = null;
	    try {
	        try {
	            writer = new FileWriter(filePath);
	            writer.write(content);
	        } finally {
	            if (writer != null) {
	                writer.flush();
	                writer.close(); 
	            }
	        }
	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    }
	}
	
	// Deletes all files and folders inside a specified folder.
	// Will not delete the folder it self.
	private static void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) { //some JVMs return null for empty dirs
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	                f.delete();
	            }	  
	            if(f.isDirectory()) // Finally delete the nested folders
	            	f.delete();
	        }
	    }
	    // folder.delete(); // If you want to delete the supplied folder as well.
	}
	
	// Iterate through all files and folders inside a specified folder.
	private static void showFiles(File folder) {
		File[] files = folder.listFiles();
	    for (File file : files) {
	        if (file.isDirectory()) {
	            System.out.println("Directory: " + file.getName());
	            showFiles(file); // Calls same method again.
	        } else {
	            System.out.println("File: " + file.getName());
	        }
	    }
	}
	
	public static void main(String[] args) throws IOException {
		// Sample client code
		 String  srcFile = src + "501411_3215-36_2013-07-09_13-18-55.zip";
		 String destFile = dest + "501411_3215-36_2013-07-09_13-18-55.zip";
		 
		 ZipUtil z = new ZipUtil();
		 z.updateSessionZip(srcFile, destFile);	 
	}

}
