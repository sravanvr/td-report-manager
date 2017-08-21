/**
 * 
 * This program creates a repository of cell styles
 * @author Sravan Vedala
 * @version 1.0
 * @since 2017-08-01
 * 
 */
package io.sv.tools.excelreportmanager;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CellStyles {
	
	Map cellStyleMap = new HashMap <String, CellStyle>();	
	Workbook workbook;

	public CellStyles(Workbook workbook){
		this.workbook = workbook;		
	}
	
	public  Map <String, CellStyle> createStyles(){
		
		short headerBGcolor = 0, headerTXTcolor = 0;
		
		if (workbook instanceof HSSFWorkbook){
			//135,206,235
			headerBGcolor = createHSSFCustomColor(HSSFColor.LIGHT_BLUE.index, (byte)135, (byte)206, (byte)235);
			headerTXTcolor = createHSSFCustomColor(HSSFColor.MAROON.index, (byte)122, (byte)62, (byte)72);
						
			// Create Header style
			CellStyle headerStyle;
			// step1 - Create Header Font
			Font headerFont = workbook.createFont();
			headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			headerFont.setColor(IndexedColors.MAROON.getIndex());
			headerFont.setFontHeightInPoints((short)20);		
			//step2 - border style
			headerStyle = createBorderStyle(); //Get the border style set in a seperate function
			//step3 - background color
			// In order to make back ground color work, both back ground and fore ground colors must be filled
			// Following three instructions should go together
			headerStyle.setFillBackgroundColor(headerBGcolor);
			headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			headerStyle.setFillForegroundColor(headerBGcolor);	
			headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
			headerStyle.setWrapText(true);
			// Finally add the Header font to the style
			headerStyle.setFont(headerFont);		
			// Then add this whole Header style package to a HashMap
			cellStyleMap.put("header", headerStyle);
			
			// Create Body style
			// Note: No font and no fill colors, this style has only borders
			CellStyle bodyStyle;
			bodyStyle = createBorderStyle(); //Get the border style set in a seperate function				
			bodyStyle.setAlignment(CellStyle.ALIGN_LEFT);			
			// Then add this whole style package to a HashMap
			cellStyleMap.put("allCells", bodyStyle);
			
			CellStyle failStyle;
			failStyle = createBorderStyle(); //Get the border style set in a seperate function			
			failStyle.setAlignment(CellStyle.ALIGN_LEFT);
			Font failFont = workbook.createFont();
			failFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			failFont.setColor(IndexedColors.RED.getIndex());
			failStyle.setFont(failFont);
			// Then add this whole style package to a HashMap
			cellStyleMap.put("failed", failStyle);
			
			failStyle = createBorderStyle(); //Get the border style set in a seperate function			
			failStyle.setAlignment(CellStyle.ALIGN_CENTER);
			failFont = workbook.createFont();
			failFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			failFont.setColor(IndexedColors.RED.getIndex());
			failStyle.setFont(failFont);
			// Then add this whole style package to a HashMap
			cellStyleMap.put("summFailed", failStyle);
			
			
			CellStyle headerLlinkStyle;
			// step1 - Create Header Font
			Font hlinkFont = workbook.createFont();
			hlinkFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			hlinkFont.setColor(IndexedColors.MAROON.getIndex());
			hlinkFont.setFontHeightInPoints((short)16);		
			hlinkFont.setUnderline(Font.U_SINGLE);			
			//step2 - border style
			headerLlinkStyle = createBorderStyle(); //Get the border style set in a seperate function
			//step3 - background color
			// In order to make back ground color work, both back ground and fore ground colors must be filled
			// Following three instructions should go together
			headerLlinkStyle.setFillBackgroundColor(headerBGcolor);
			headerLlinkStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			headerLlinkStyle.setFillForegroundColor(headerBGcolor);	
			headerLlinkStyle.setAlignment(CellStyle.ALIGN_CENTER);
			// Finally add the Header font to the style
			headerLlinkStyle.setFont(hlinkFont);		
			// Then add this whole Header style package to a HashMap
			cellStyleMap.put("headerLink", headerLlinkStyle);
						
			// Note: No font and no fill colors, this style has only borders
			CellStyle summBodyStyle;
			summBodyStyle = createBorderStyle(); //Get the border style set in a seperate function				
			summBodyStyle.setAlignment(CellStyle.ALIGN_CENTER);			
			// Then add this whole style package to a HashMap
			cellStyleMap.put("summCells", summBodyStyle);
						
			
		}
		/*
		 * Generic (i.e. SS = HSSF + XSSF) CellStyle does  not work for XSSF colors. XSSFCellStyle must be used.
		 * RGB color codes for XSSF colors can be found here. Get an RGB code you want and pass it to java.awt.Color() function
		 * http://www.rapidtables.com/web/color/RGB_Color.htm
		 */
		else{
			// Create Header style
			XSSFCellStyle headerStyle;
			// step1 - Create Header Font
			Font headerFont = workbook.createFont();
			headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			headerFont.setColor(IndexedColors.MAROON.getIndex());
			headerFont.setFontHeightInPoints((short)20);		
			//step2 - border style
			headerStyle = (XSSFCellStyle)createBorderStyle(); //Get the border style set in a seperate function
			//step3 - background color
			// In order to make back ground color work, both back ground and fore ground colors must be filled
			// Following three instructions should go together
			headerStyle.setFillBackgroundColor( new XSSFColor(new java.awt.Color(135,206,235)));
			headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			headerStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(135,206,235)));	
			headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
			headerStyle.setWrapText(true);
			// Finally add the Header font to the style
			headerStyle.setFont(headerFont);		
			// Then add this whole Header style package to a HashMap
			cellStyleMap.put("header", headerStyle);
			
			// Create Body style
			// Note: No font and no fill colors, this style has only borders
			CellStyle bodyStyle;
			bodyStyle = createBorderStyle(); //Get the border style set in a seperate function				
			bodyStyle.setAlignment(CellStyle.ALIGN_LEFT);
			// Then add this whole style package to a HashMap
			cellStyleMap.put("allCells", bodyStyle);
			
			CellStyle failStyle;
			failStyle = createBorderStyle();
			failStyle.setAlignment(CellStyle.ALIGN_LEFT);
			Font failFont = workbook.createFont();
			failFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			failFont.setColor(IndexedColors.RED.getIndex());
			failStyle.setFont(failFont);
			cellStyleMap.put("failed", failStyle);
						
			failStyle = createBorderStyle();
			failStyle.setAlignment(CellStyle.ALIGN_CENTER);
			failFont = workbook.createFont();
			failFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			failFont.setColor(IndexedColors.RED.getIndex());
			failStyle.setFont(failFont);
			cellStyleMap.put("summFailed", failStyle);
						 
			XSSFCellStyle headerLlinkStyle;
			// step1 - Create Header Font
			Font hlinkFont = workbook.createFont();
			hlinkFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			hlinkFont.setColor(IndexedColors.MAROON.getIndex());
			hlinkFont.setFontHeightInPoints((short)16);	
			hlinkFont.setUnderline(Font.U_SINGLE);			
			//step2 - border style
			headerLlinkStyle = (XSSFCellStyle)createBorderStyle(); //Get the border style set in a seperate function
			//step3 - background color
			// In order to make back ground color work, both back ground and fore ground colors must be filled
			// Following three instructions should go together
			headerLlinkStyle.setFillBackgroundColor( new XSSFColor(new java.awt.Color(135,206,235)));
			headerLlinkStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			headerLlinkStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(135,206,235)));	
			headerLlinkStyle.setAlignment(CellStyle.ALIGN_CENTER);
			// Finally add the Header font to the style
			headerLlinkStyle.setFont(hlinkFont);		
			// Then add this whole Header style package to a HashMap
			cellStyleMap.put("headerLink", headerLlinkStyle);
			
			// Note: No font and no fill colors, this style has only borders
			CellStyle summBodyStyle;
			summBodyStyle = createBorderStyle(); //Get the border style set in a seperate function				
			summBodyStyle.setAlignment(CellStyle.ALIGN_CENTER);
			// Then add this whole style package to a HashMap
			cellStyleMap.put("summCells", summBodyStyle);
						
		}
		// common style - no need to write seperate for HSSF and XSSF
		CellStyle hlink_style = workbook.createCellStyle();
		Font hlink_font = workbook.createFont();
		hlink_font.setUnderline(Font.U_SINGLE);
		hlink_font.setColor(IndexedColors.BLUE.getIndex());
		hlink_style.setFont(hlink_font);
		cellStyleMap.put("hyperlink", hlink_style );
		
		return cellStyleMap;
	}
	
	private CellStyle createBorderStyle(){
		CellStyle style = workbook.createCellStyle();
		style.setBorderBottom(CellStyle.BORDER_THICK);
		style.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THICK);
		style.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setBorderRight(CellStyle.BORDER_THICK);
		style.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setBorderTop(CellStyle.BORDER_THICK);
		style.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());	
		return style;
	}
	
	private short createHSSFCustomColor(short color, byte r, byte g, byte b){
		HSSFPalette palette = ((HSSFWorkbook) workbook).getCustomPalette();
		//replacing the standard red with freebsd.org red
	    palette.setColorAtIndex(color,
	            r, //(byte) 153,  //RGB red (0-255)
	            g, //(byte) 0,    //RGB green
	            b //(byte) 0     //RGB blue
	    );
	    //replacing lime with freebsd.org gold
	    palette.setColorAtIndex(color, r, g, b);
		return palette.getColor(color).getIndex();
	    
	}
}
