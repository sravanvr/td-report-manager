/*
 * This is a base-lined un-zip utility.
 * Use this to un-zip a zipped file. * 
 */
package io.sv.tools.utils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;




public class UnZipUtil {
 private final static int BUFFER = 10241024;

 private static Logger log = Logger.getLogger(UnZipUtil.class);
 
 public static void main(String[] args) {
     UnZipUtil unzip = new UnZipUtil();

     String src = "C:/SRAVAN VEDALA/TESTING/TEST_DEVELOPMENT/ITAC_DATA/src/";
     String dest = "C:/SRAVAN VEDALA/TESTING/TEST_DEVELOPMENT/ITAC_DATA/dest/";
     
	 String  unZipFile = src + "501411_3215-36_2013-07-09_13-18-55.zip";
	 String unZipOutFolder = dest + "501411_3215-36_2013-07-09_13-18-55";
     
     unzip.recursiveUnzip(new File(unZipFile), new File(unZipOutFolder));
     unzip.removeAllZipFiles(new File(unZipOutFolder));
     System.out.println("Finished!!");
 }
 
 /*	Params:
  *  inFile: The full path of the zip file that needs to be unzipped
  *  outFolder: The 'folder path' where the zip file needs to be unzipped.         
  */
 public File recursiveUnzip(File inFile, File outFolder)
 {
	 DOMConfigurator.configure("config/log4j-config.xml");
	 
      try
      {
           this.createFolder(outFolder, true);
           BufferedOutputStream out = null;
           ZipInputStream  in = new ZipInputStream(new BufferedInputStream(new FileInputStream(inFile)));
           ZipEntry entry;
           while((entry = in.getNextEntry()) != null)
           {
                //System.out.println("Extracting: " + entry);
        	   log.debug("Extracting: " + entry);
                int count;
                byte data[] = new byte[BUFFER];
                
                // write the files to the disk
                File newFile = new File(outFolder.getPath() + "/" + entry.getName());
                Stack pathStack = new Stack();
                File newNevigate = newFile.getParentFile();
                while(newNevigate != null){
                    pathStack.push(newNevigate);
                    newNevigate = newNevigate.getParentFile();
                }
                //create all the path directories
                while(!pathStack.isEmpty()){
                    File createFile =(File) pathStack.pop();
                    this.createFolder(createFile, true);
                }
                if(!entry.isDirectory()){
                      out = new BufferedOutputStream(
                                new FileOutputStream(newFile),BUFFER);
                      while ((count = in.read(data,0,BUFFER)) != -1){
                           out.write(data,0,count);
                      }
                      this.cleanUp(out);
                      //recursively unzip files
                      if(entry.getName().toLowerCase().endsWith(".zip")){
                          String zipFilePath = outFolder.getPath() + "/" + entry.getName();
                          this.recursiveUnzip(new File(zipFilePath), new File(zipFilePath.substring(0, zipFilePath.length()-4)));
                      }
                }else{
                    this.createFolder(new File(outFolder.getPath() + "/" +entry.getName()), true);
                }
           }
           this.cleanUp(in);
           return outFolder;
      }catch(Exception e){
           e.printStackTrace();
           return inFile;
      }
 }
 
 public void removeAllZipFiles(File folder){
     String[] files = folder.list();
     for(String file: files){
         File item = new File(folder.getPath() + "/" + file);
         if(item.exists() && item.isDirectory()){
             this.removeAllZipFiles(item);
         }else if(item.exists() && item.getName().toLowerCase().endsWith(".zip")){
             item.delete();
             //System.out.println(item.getName() + " Removed!!");
             log.debug(item.getName() + " Removed!!");
         }
     }
 }
 
 
 private void createFolder(File folder, boolean isDriectory){
     if(isDriectory){
         folder.mkdir();
     }
 }
 
 private void cleanUp(InputStream in) throws Exception{
      in.close();
 }
 
 private void cleanUp(OutputStream out) throws Exception{
      out.flush();
      out.close();
 }

}