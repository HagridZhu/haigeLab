package com.moyulab.haige.haigelab.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.moyulab.haige.haigelab.common.HaigeLabException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * 
 * 
 * <pre>
 * 。
 * </pre>
 * 
 * @author xfl  xiangfl@partner.midea.com.cn
 * @version 1.00.00
 * 
 *  <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 * </pre>
 */
public class UploadUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(UploadUtil.class);
	/**
	 * @describe 将Excel表单解析成List<List<String>>的形式放在内存中,
	 *  key为关键字组合，key1_key2，有相同的记录是，提示用户
	 *  value为String[][]的二维数据形式，如String[0][0]记录startRow行startCol列的数据
	 *  行和列的标识均从0开始，startRow=0,startCol=0,表示从excel的第1行第1列开始读取
	 *  
	 * @param sheet  读取的sheet
	 * @param startRow 从第几行开始读取数据，大于等于0的整数
	 * @param endRow 读取到第几行,-1，读到表单的行尾部;如果不等于-1，则为大于等于0的整数
	 * @param startCol 从第几列开始读
	 * @param endCol 读取到第几列,-1，读取表单的列尾部;如果不等于-1，则为大于等于0的正整数
	 * @return Map<String,String[][]>形式的数据
	 */
	public static List<List<String>> readOneSheetForList(Sheet  sheet,int startRow,int endRow,int startCol,int endCol){
		List<List<String>> result = new ArrayList<List<String>>();
		if(sheet==null) {return result;}
		
		if(startRow<0||startCol<0||(endRow!=-1&&endRow<0)||(endCol!=-1&&endCol<0)) {
			return result;
		}
		int sheetRow=sheet.getLastRowNum();//获得总行数
 
        endRow = (endRow==-1||endRow>sheetRow)?sheetRow:endRow;
        if(null == sheet.getRow(0)){
        	return result;
        }
        int sheetColumn=sheet.getRow(0).getPhysicalNumberOfCells()-1;//获取总列数 

        if(sheetColumn<endCol){throw new RuntimeException();}
        endCol = endCol==-1?sheetColumn:endCol;
        
        if(startRow>endRow || startCol>endCol) {
        	return result;//要读取的位置没有内容
        }
        
        
        for (int j=startRow;j<=endRow;j++) {
        	Row r = sheet.getRow(j);
        	if (r == null) {
        	    continue;
        	}
        	List<String> row = UploadUtil.readOneRowForList(sheet, j, startCol, endCol);
        	if(row!=null) {result.add(row);}
        }
		return result;
	}
	/**
	 * 读取excel中指定sheet的行，读取从startCol开始，endCol结束,
	 * list倒数第二列记录表格的行
	 * list倒数第一列为有无重复标记位，格式为[row1,row2,row3],如果没有重复则为空字符串"",
	 * 所有列均为空则返回 null;
	 * 
	 * @param sheet excel工作表
	 * @param row 要读取的excel行,从0开始索引
	 * @param startCol 读取的开始列，从0开始索引
	 * @param endCol 读取的结束列，从0开始索引
	 * @return List<String>,将读取内容以List形式返回,如果没有读取到则返回空
	 * */
	public static List<String> readOneRowForList(Sheet sheet,int row,int startCol,int endCol) {
		//下标+1为数组长度，加2列记录行数和预留一列检查唯一
		List<String> list = new ArrayList<String>();
		
	    int sheetColumn=sheet.getRow(0).getPhysicalNumberOfCells()-1;//获取总列数 

        if(sheetColumn<endCol){throw new HaigeLabException("");}
        endCol = endCol==-1?sheetColumn:endCol;
        
		boolean allNull = true;
		for(int i=startCol;i<=endCol;i++){
			// System.out.println("====校验值======"+sheet.getRow(row).getCell(i));
			String content = UploadUtil.getCellValue(sheet.getRow(row).getCell(i));
			/*if(i==0 && !content.equals("")) {
				content = content.toUpperCase();//身份证号转大写
			}*/
			if(!StringUtils.isEmpty(content)) {
				allNull = false;
			}else{
				content= "";
			}
			list.add(content);	
		}
		if(allNull) {
			return null;
		}
		list.add(""+row);
		list.add("");
		return list;
	}
	
	public static Map<String,Integer> readOneRowForMap(Sheet sheet,int hiderow,int startCol,int endCol) {
		Map<String,Integer> map  = new HashMap<String, Integer>();
		
	    int sheetColumn=sheet.getRow(0).getPhysicalNumberOfCells()-1;//获取总列数 

        if(sheetColumn<endCol){throw new HaigeLabException("");}
        endCol = endCol==-1?sheetColumn:endCol;
        
		boolean allNull = true;
		for(int i=startCol;i<=endCol;i++){
			// System.out.println("====校验值======"+sheet.getRow(row).getCell(i));
			String key = UploadUtil.getCellValue(sheet.getRow(hiderow).getCell(i));
			if(key.contains("|")){
				key = key.substring(0,key.indexOf("|"));
			}
			int rowNum = i;
			map.put(key, rowNum);
			if(!StringUtils.isEmpty(key)) {
				allNull = false;
			}
		}
		if(allNull) {
			return null;
		}
		return map;
	}
	
	public static  List<Map<String,Object>> readOneSheetForList(Sheet  sheet,int hideRow, int startRow,int endRow,int startCol,int endCol){
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		if(sheet==null) {return result;}
		
		if(startRow<0||startCol<0||(endRow!=-1&&endRow<0)||(endCol!=-1&&endCol<0)) {
			return result;
		}
		int sheetRow=sheet.getLastRowNum();//获得总行数
 
        endRow = (endRow==-1||endRow>sheetRow)?sheetRow:endRow;
        if(null == sheet.getRow(0)){
        	return result;
        }
        int sheetColumn=sheet.getRow(0).getPhysicalNumberOfCells()-1;//获取总列数 

        if(sheetColumn<endCol){throw new RuntimeException();}
        endCol = endCol==-1?sheetColumn:endCol;
        
        if(startRow>endRow || startCol>endCol) {
        	return result;//要读取的位置没有内容
        }
                
        for (int j=startRow;j<=endRow;j++) {
        	Row r = sheet.getRow(j);
        	if (r == null) {
        	    continue;
        	}
        	Map<String,Object> row = UploadUtil.readOneRowForMap(sheet, hideRow,j, startCol, endCol);
        	if(row!=null) {result.add(row);}
        }
		
		return result;
	}
	
	public static Map<String,Object> readOneRowForMap(Sheet sheet,int hideRow,int row,int startCol,int endCol){
		Map<String,Object> map = new HashMap<String, Object>();
		int sheetColumn=sheet.getRow(0).getPhysicalNumberOfCells()-1;//获取总列数 

        if(sheetColumn<endCol){throw new HaigeLabException("");}
        endCol = endCol==-1?sheetColumn:endCol;
        
		boolean allNull = true;
		for(int i=startCol;i<=endCol;i++){
			// System.out.println("====校验值======"+sheet.getRow(row).getCell(i));
			String key  = UploadUtil.getCellValue(sheet.getRow(hideRow).getCell(i));
			if(key.contains("|")){
				key = key.substring(0,key.indexOf("|"));
			}
			String content = UploadUtil.getCellValue(sheet.getRow(row).getCell(i));
			if(!StringUtils.isEmpty(content)) {
				allNull = false;
			}else{
				content= "";
			}
			map.put(key, content);
		}
		if(allNull) {
			return null;
		}
		map.put("rowNum", row);
		return map;
	}
	
	
 
	/**
	 * 获取单元格的值，不论单元格是什么形式的，一律返回String
	 * null或者"    "返回 ""
	 * " trim "返回 "trim"
	 * @author LiuQS
	 * */
	public static String getCellValue(Cell cell) {
		String cellValue = null;
		if(cell!=null){
			int cellType = cell.getCellType();
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			switch (cellType) {
			case Cell.CELL_TYPE_STRING: // 文本
				cellValue = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_NUMERIC: // 数字、日期
				if (DateUtil.isCellDateFormatted(cell)) {
					cellValue = fmt.format(cell.getDateCellValue()); // 日期型
				} else {					 
					 DecimalFormat df = new DecimalFormat("0");
					 cellValue = String.valueOf(df.format(cell.getNumericCellValue()));// 数字
 			}
				break;
			case Cell.CELL_TYPE_BOOLEAN: // 布尔型
				cellValue = String.valueOf(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_BLANK: // 空白
				cellValue = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_ERROR: // 错误
				cellValue = "";
				break;
			case Cell.CELL_TYPE_FORMULA: // 公式
				cellValue = "";
				break;
			default:
				// 存在不可识别的数据格式

			}
		}
		return cellValue;
	}
	
	/**
	 * 验证日期格式是否正确
	 * @param dateStr 字符串格式的日期
	 * @param pattern 字符串格式
	 * @return 如果能转为日期，则返回true,否则返回false
	 * @author LiuQS
	 * @createDate 2014-08-08
	 * */
	public static boolean isRigthDateStyle(String dateStr,String pattern) {
		if(StringUtils.isEmpty(dateStr)) {
			return false;
		}
		try {
			stringToDate(dateStr,pattern);
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	

	public static final Date stringToDate(String date, String format){
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		formatter.setLenient(false);
		Date realDate = null;
		try {
			realDate = formatter.parse(date);
		} catch (Exception e) {
			logger.error("stringToDate error", e);
		}
		return realDate;
	}
	
	public static void writeError(Sheet sheet,String info,int row,int col){
		try {
			   HSSFRow hSSFRow = (HSSFRow) sheet.createRow((short) row); 
			    HSSFCell cell = hSSFRow.createCell(col);
			    HSSFWorkbook workbook = new HSSFWorkbook();  
			    // 创建单元格的格式，如居中、左对齐等  
		        HSSFCellStyle cellStyle = workbook.createCellStyle();	        
		        HSSFFont font = workbook.createFont();				 
				font.setFontHeightInPoints((short) 16);//设置字体大小
				font.setColor(HSSFColor.YELLOW.index);  //填充色	 
				// 设置字体  
		        cellStyle.setFont(font);
		       // 为单元格设置格式  
	            cell.setCellStyle(cellStyle);  
	        	cell.setCellValue(info);  
	        	hSSFRow.createCell(col);
		} catch (Exception e) {
			logger.error("writeError error", e);
		}  
	}
	
	/**
     * 设置错误提示信息
     *  @param sheet 可编写excel表格
     *  @param info 要写入的信息
     *  @param row  写入的行
     *  @param col  写入的列
     *  @param writeType 写入类型 {0:替换原内容,1:追加当前信息}
     */
	public static void writeError(Sheet sheet,String info,int row,int col,int writeType) {
		try {
		    HSSFRow hSSFRow = (HSSFRow) sheet.createRow((short) row); 
		    HSSFCell cell = hSSFRow.createCell(col);
		    HSSFWorkbook workbook = new HSSFWorkbook();  
		    // 创建单元格的格式，如居中、左对齐等  
	        HSSFCellStyle cellStyle = workbook.createCellStyle();	        
	        HSSFFont font = workbook.createFont();				 
			font.setFontHeightInPoints((short) 16);//设置字体大小
			font.setColor(HSSFColor.YELLOW.index);  //填充色	 
			// 设置字体  
	        cellStyle.setFont(font);
	       // 为单元格设置格式  
            cell.setCellStyle(cellStyle);  
			//如果原来有错误提示，在后面追加新的错误信息
			String cellValue = UploadUtil.getCellValue(sheet.getRow(row).getCell(col) ); 
			if(!StringUtils.isEmpty(cellValue)){
				info = cellValue+";\r\n" + info;
			}
			cell.setCellValue(info);  
	 
		} catch (Exception e) {
			logger.error("writeError error", e);
		} 
 	}
	
	 /**
	  * 设置红色背景
	  * @author [ ]
	  * 日期   
	  * @param wb
	  * @param row
	  * @param cellNum
	  * @param value
	  */
   public static void setCellRedBkground(HSSFWorkbook wb, HSSFRow row, int cellNum,String value){
		    HSSFCell cell;
		 	cell = row.createCell(cellNum);
		 	HSSFCellStyle cellStyle = wb.createCellStyle();
		 	cellStyle.setFillBackgroundColor(HSSFColor.RED.index);
		 	cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		 	cellStyle.setFillForegroundColor(HSSFColor.RED.index);
		 	cell.setCellStyle(cellStyle);
		 	cell.setCellValue(value);
	 }
   
	 /**
	  * 设置红色背景
	  * @author [ ]
	  * 日期   
	  * @param row
	  * @param cellNum
	  * @param value
	  */
	 public static void setCellRedBkground(CellStyle cellStyle, Row row, int cellNum,String value){
			    Cell cell;
			 	cell = row.createCell((short) cellNum);
			 	cellStyle.setLocked(false);
			 	/*CellStyle cellStyle = wb.createCellStyle();
			 	cellStyle.setFillBackgroundColor(HSSFColor.RED.index);
			 	cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			 	cellStyle.setFillForegroundColor(HSSFColor.RED.index);*/
			 	cell.setCellStyle(cellStyle);
			 	cell.setCellValue(value);
	}
	 
	 /**
	  * 设置红色背景
	  * @author [ ]
	  * 日期   
	  * @param row
	  * @param cellNum
	  * @param value
	  */
	 public static void setCellRedBackground(CellStyle cellStyle,Row row, int cellNum,String value){
		Cell cell = row.createCell((short) cellNum);
		cellStyle.setFillBackgroundColor(HSSFColor.RED.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setFillForegroundColor(HSSFColor.RED.index);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(value);
	}
	 
	 /**
	  * 设置红色背景
	  * @author [ ]
	  * 日期   
	  * @param row
	  * @param cellNum
	  */
	 public static void setCellRedBackground(CellStyle cellStyle,Row row, int cellNum){
		Cell cell = row.getCell((short) cellNum);
		if(cell == null)cell = row.createCell((short) cellNum);
		cellStyle.setFillBackgroundColor(HSSFColor.RED.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setFillForegroundColor(HSSFColor.RED.index);
		cell.setCellStyle(cellStyle);
	}
	 
	 public static CellStyle getCellStyle(Workbook wb){
		 CellStyle cellStyle = wb.createCellStyle();
		 cellStyle.setFillBackgroundColor(HSSFColor.RED.index);
		 cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		 cellStyle.setFillForegroundColor(HSSFColor.RED.index);
		 return cellStyle;
	 }
   
   /**
    * 功能：创建带边框的CellStyle样式
    * @param     wb                HSSFWorkbook    
    * @param     backgroundColor    背景色    
    * @param     foregroundColor    前置色
    * @return    CellStyle
    */
   public static CellStyle createBorderCellStyle(HSSFWorkbook wb,short backgroundColor,short foregroundColor,short halign){
       CellStyle cs=wb.createCellStyle();
       cs.setAlignment(halign);
       cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
       cs.setFillBackgroundColor(backgroundColor);
       cs.setFillForegroundColor(foregroundColor);
       cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
       //cs.setFont(font);
       cs.setBorderLeft(CellStyle.BORDER_DASHED);
       cs.setBorderRight(CellStyle.BORDER_DASHED);
       cs.setBorderTop(CellStyle.BORDER_DASHED);
       cs.setBorderBottom(CellStyle.BORDER_DASHED);  
       return cs;
   }

   /**
	 * @Title isRightTemplet
	 * @Description 验证模板是否正确
	 * @param cellDataList
	 * @return
	 * */
	public static boolean isRightTemplet(List<List<String>> cellDataList,List<String>columnList) {
		if(cellDataList.size()!=1 || cellDataList.get(0).size()-2!=columnList.size()) {
			return false;
		}
		List<String> cellData = cellDataList.get(0);
		String title;
		String name;
		for(int i=0; i<cellData.size()-2; i++) {
			title = cellData.get(i);
			name = columnList.get(i);
			if(title==null || name==null || !name.equals(title)) {
				return false;
			}
		}
		return true;
	}
	public static List<List<String>> readOneSheetAsList(Sheet sheet,int startRow,int endRow,int startCol,int endCol){
		List<List<String>> result = new ArrayList<List<String>>();
		if(sheet==null) {return result;}
		
		if(startRow<0||startCol<0||(endRow!=-1&&endRow<0)||(endCol!=-1&&endCol<0)) {
			return result;
		}
		int sheetRow=sheet.getLastRowNum();//获得总行数
 
        endRow = (endRow==-1||endRow>sheetRow)?sheetRow:endRow;
        if(null == sheet.getRow(0)){
        	return result;
        }
        int sheetColumn=sheet.getRow(0).getPhysicalNumberOfCells()-1;//获取总列数 

        if(sheetColumn<endCol){throw new RuntimeException();}
        endCol = endCol==-1?sheetColumn:endCol;
        
        if(startRow>endRow || startCol>endCol) {
        	return result;//要读取的位置没有内容
        }
        
        
        for (int j=startRow;j<=endRow;j++) {
        	Row r = sheet.getRow(j);
        	if (r == null) {
        	    continue;
        	}
        	List<String> row = readOneRowASList(sheet, j, startCol, endCol);
        	if(row!=null) {result.add(row);}
        }
		return result;
	}
	/**
	 * 读取excel中指定sheet的行，读取从startCol开始，endCol结束,
	 * list倒数第二列记录表格的行
	 * list倒数第一列为有无重复标记位，格式为[row1,row2,row3],如果没有重复则为空字符串"",
	 * 所有列均为空则返回 null;
	 * 
	 * @param sheet excel工作表
	 * @param row 要读取的excel行,从0开始索引
	 * @param startCol 读取的开始列，从0开始索引
	 * @param endCol 读取的结束列，从0开始索引
	 * @return List<String>,将读取内容以List形式返回,如果没有读取到则返回空
	 * */
	public static List<String> readOneRowASList(Sheet sheet,int row,int startCol,int endCol) {
		//下标+1为数组长度，加2列记录行数和预留一列检查唯一
		List<String> list = new ArrayList<String>();
		
	    int sheetColumn=sheet.getRow(0).getPhysicalNumberOfCells()-1;//获取总列数 

        if(sheetColumn<endCol){throw new HaigeLabException("");}
        endCol = endCol==-1?sheetColumn:endCol;
        
		boolean allNull = true;
		for(int i=startCol;i<=endCol;i++){
			String content = getCellValue(sheet.getRow(row).getCell(i));
			if(!StringUtils.isEmpty(content)) {
				allNull = false;
			}else{
				content= "";
			}
			list.add(content);	
		}
		if(allNull) {
			return null;
		}
		return list;
	}
}
