/*
 * This is base-lined Zipping utility. Use this to zip/compress a folder or file.
 * Use anyone of 'ZipUtil_1' or 'ZipUtil_2'. Both serves to 'zip' the folder/file content.
 * 
 */

package io.sv.tools.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil_1 {

	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	public static void main(String[] args) throws Exception {	  

		// Staging and destination folders  
		String stg = "C:/SRAVAN VEDALA/TESTING/TEST_DEVELOPMENT/ITAC_DATA/stg/";
		String dest = "C:/SRAVAN VEDALA/TESTING/TEST_DEVELOPMENT/ITAC_DATA/dest/";

		//Source Folder that contains the content that needs to be zipped.
		String fileOrFolderToZip = stg + "501411_3215-36_2013-07-09_13-18-55"; 
		// This must be the target zip file name that needs to be created.
		String zipFile = dest + "501411_3215-36_2013-07-09_13-18-55.zip";

		zipFile(fileOrFolderToZip,  zipFile, true);
	}

	public static void zipFile(String fileToZip, String zipFile, boolean excludeContainingFolder)
			throws IOException {        
		ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));    

		File srcFile = new File(fileToZip);
		if(excludeContainingFolder && srcFile.isDirectory()) {
			for(String fileName : srcFile.list()) {
				addToZip("", fileToZip + "/" + fileName, zipOut);
			}
		} else {
			addToZip("", fileToZip, zipOut);
		}

		zipOut.flush();
		zipOut.close();

		System.out.println("Successfully created " + zipFile);
	}

	private static void addToZip(String path, String srcFile, ZipOutputStream zipOut)
			throws IOException {        
		File file = new File(srcFile);
		String filePath = "".equals(path) ? file.getName() : path + "/" + file.getName();
		if (file.isDirectory()) {
			for (String fileName : file.list()) {             
				addToZip(filePath, srcFile + "/" + fileName, zipOut);
			}
		} else {
			zipOut.putNextEntry(new ZipEntry(filePath));
			FileInputStream in = new FileInputStream(srcFile);

			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			int len;
			while ((len = in.read(buffer)) != -1) {
				zipOut.write(buffer, 0, len);
			}

			in.close();
		}
	}
}
