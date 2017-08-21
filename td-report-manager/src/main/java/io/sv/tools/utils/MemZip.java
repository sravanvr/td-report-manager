/*
 * This is a base-lined Zip And Un-Zip utility.
 * Works only for zip files without nested zips.
 * For nested zips use ZipUtil.java. 
 */

package io.sv.tools.utils;

import java.io.InputStream;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class MemZip {
	
	private final static int BUFFER = 10241024;

	// This function takes the "orig" zip file and copies the entire archive structure
	// into a new zip file specified in "tar"
	// Unzipping and Zipping happens inside memory
	// Implementation to update XMLs or any other files can be included inside this fucntion.
	// The limitation is this function DOES NOT support nested zip files.
	public void copyZip(String orig, String tar) throws IOException, SAXException, ParserConfigurationException{
		
		ZipFile         original = new ZipFile(orig);
		ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(tar));
		InputStream in = null;	
		
		Enumeration     entries = original.entries();
		byte[]          buffer = new byte[BUFFER]; //514
		
		while (entries.hasMoreElements()) {
		    
			ZipEntry    entry = (ZipEntry)entries.nextElement();
			
	    	ZipEntry newEntry = new ZipEntry(entry.getName());
	    	System.out.println("Zip entry name:" + entry.getName());
			
	    	 in = original.getInputStream(entry);
	    	 
	    	 if  (entry.getName().endsWith("_gdf.xml")) {
	    		 in = updateXML(in);
	    	 }
    	
	    	 if  (entry.getName().contains("RawDeviceData.xml")) {
	    		 in = updateRawXML(in);
	    	 }
	    	 
	        outputStream.putNextEntry(newEntry);
	        
	        while (0 < in.available()){
	            int read = in.read(buffer);
	            outputStream.write(buffer,0,read);
	        }
	   
	        in.close();
			   
		    outputStream.closeEntry();
		  
		} //while
		
		outputStream.close();	
	}

	@SuppressWarnings("deprecation")
	public static InputStream document2InputStream(Document document)    throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		@SuppressWarnings("deprecation")
		OutputFormat outputFormat = new OutputFormat(document);
		XMLSerializer serializer = new XMLSerializer(outputStream, outputFormat);
		serializer.serialize(document);
		return new ByteArrayInputStream(outputStream.toByteArray());
	}

	public static InputStream updateXML(InputStream is) throws SAXException, IOException,
														ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		dbf.setValidating(false);
		dbf.setIgnoringComments(false);
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setNamespaceAware(true);

		DocumentBuilder db = null;
		db = dbf.newDocumentBuilder();
		//db.setEntityResolver(new NullResolver());

		Document document = db.parse(is);

		Element docEle = document.getDocumentElement();

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
		
		return document2InputStream(document);
	}

	public static InputStream updateRawXML(InputStream is) throws SAXException, IOException,
															ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		dbf.setValidating(false);
		dbf.setIgnoringComments(false);
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setNamespaceAware(true);
		
		DocumentBuilder db = null;
		db = dbf.newDocumentBuilder();
		//db.setEntityResolver(new NullResolver());
		
		Document document = db.parse(is);
		
		Element docEle = document.getDocumentElement();
		
		// Print root element of the document
		System.out.println("Root element of the document: "
		+ docEle.getNodeName());
		
		NodeList dataEleList = docEle.getElementsByTagName("sessionTimestamp");
		
		// Print total student elements in document
		System.out
		.println("Total data elements : " + dataEleList.getLength());
		
		Node n = dataEleList.item(0);
		Element e = (Element) n;  
		
		System.out.println("RTC value is: "+ e.getElementsByTagName("realtimeClock").item(0).getNodeValue());
		String rtc = e.getElementsByTagName("realtimeClock").item(0).getTextContent();
		long rtcVal = Long.parseLong(rtc);
		rtc = String.valueOf(rtcVal +1);
		e.getElementsByTagName("realtimeClock").item(0).setTextContent(rtc);
		
		return document2InputStream(document);
}
	
	public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
		
		String src = "C:/SRAVAN VEDALA/TESTING/TEST_DEVELOPMENT/ITAC_DATA/src/";
		String dest = "C:/SRAVAN VEDALA/TESTING/TEST_DEVELOPMENT/ITAC_DATA/dest/";
		MemZip z = new MemZip();
		z.copyZip(src + "109089_3253-40_2013-05-15_11-15-58.zip", dest + "109089_3253-40_2013-05-15_11-15-58.zip");
	}

}
