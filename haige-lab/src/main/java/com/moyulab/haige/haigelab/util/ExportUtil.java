package com.moyulab.haige.haigelab.util;

import com.moyulab.haige.haigelab.common.ExcelExportParam;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ExportUtil {

    private final static String DEFAULT_DATE_FORMAT = "dd\\/MM\\/yyyy";
    private final static String PATH_DOWNLOAD_DEFAULT = "downloads"+File.separator+"template";
    private final static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 通过相关配置，写入数据到excel中
     * @param excelExportParam
     * @return
     */
    public static Workbook writeExcel(ExcelExportParam excelExportParam){
        List<Map<String,Object>> list = excelExportParam.getData();
        String sheetName = excelExportParam.getSheetName();
        int sheetIndex = excelExportParam.getSheetIndex();
        Workbook wb = excelExportParam.getTemplateWb();
        String[] headerNames = excelExportParam.getHeaderNames();
        String[] columnNames = excelExportParam.getHeaderKeys();
        int areaCodeTemplate = excelExportParam.getAreaCodeTemplate();
        int startRow = excelExportParam.getStartRow();
        boolean bySheetIndex = excelExportParam.isBySheetIndex();
        Sheet sheet = null;
        //获取表格
        sheet = bySheetIndex ? wb.getSheetAt(sheetIndex) : wb.getSheet(sheetName);
        if(sheet == null){ sheet = bySheetIndex ? wb.createSheet() : wb.createSheet(sheetName) ; }
        sheet.setDefaultColumnWidth(15);
        // 生成一个样式
        CellStyle style = wb.createCellStyle();
        String dateFormat = DEFAULT_DATE_FORMAT;
        // 如果有手动设置时间格式，则取手动设置的
        if( StringUtils.hasText(excelExportParam.getDateFormat()) ){dateFormat = excelExportParam.getDateFormat();}
        //获取第一行（表头）
        Row row = headerNames == null ? row = sheet.getRow(0) : sheet.createRow(0);
        //样式字体居中
        style.setAlignment(CellStyle.ALIGN_CENTER);
        //row.setHeight((short) 5);
        //获取表头第一行的所有单元格
        Cell cell=null;
        if(headerNames!=null){
            Font font = wb.createFont();
            CellStyle hstyle = wb.createCellStyle();
            font.setBold(true);
            hstyle.setFont(font);
            hstyle.setAlignment(CellStyle.ALIGN_CENTER);
            for(int i=0;i<headerNames.length;i++){
                cell=createCellAndSetValue(row,i,headerNames[i]);
                cell.setCellStyle(hstyle);
            }
        }
        //创建时间格式样式
        CellStyle cellStyle = wb.createCellStyle();
        DataFormat format=   wb.createDataFormat();
        short format2 = format.getFormat(dateFormat);
        cellStyle.setDataFormat(format2);
        //添加一些数据
        //向单元格里填充数据
        for (int i = 0; i < list.size(); i++) {
            row = sheet.getRow(i + startRow);
            if(row!=null){
                sheet.removeRow(row);
            }
            row = sheet.createRow(i + startRow);
            for(int j=0;j<columnNames.length;j++){
                String value="";
                cell = row.createCell(j);
                Object object=list.get(i).get(columnNames[j]);
                if(object != null){
                    if(object.getClass().equals(Timestamp.class) ||
                            object.getClass().equals(Date.class)){
//						value=new SimpleDateFormat("yyyy-MM-dd").format(object);
                        cell.setCellValue((Date)object);
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        try {
                            cell.setCellStyle(cellStyle);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else{
                        value = String.valueOf(object);
                        cell.setCellValue(value);
                        cell.setCellStyle(style);
                    }
                }else{
                    cell.setCellValue("");
                }
            }
        }
        return wb;
    }

    public static Cell createCellAndSetValue(Row row,int i,Object object){
        Cell cell;
        String value;
        if(object != null){
            value = String.valueOf(object);
        }else{
            value = "";
        }
        cell = row.createCell(i);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue(value);
        return cell;
    }

    public static void downExcel(String fileName, Workbook wb, HttpServletRequest request, HttpServletResponse response){
        OutputStream out=null;
        String userAgent = request.getHeader("User-Agent");
        try {
            if (userAgent.contains("MSIE")||userAgent.contains("Trident")) {
                //针对IE或者以IE为内核的浏览器：
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else {
                //非IE浏览器的处理：
                fileName = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", fileName));
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        try {
            String headStr = new StringBuilder()
                    .append("attachment;fileName=")
                    .append(new String(fileName.getBytes("UTF-8"),"ISO-8859-1"))
//					.append(URLEncoder.encode(fileName, "UTF-8"))
                    .toString();
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", headStr);
            out = response.getOutputStream();
            wb.write(out);
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 获取downloads/template下的Excel文件
     * @param fileName
     * @param loader
     * @return
     */
    public static  Workbook getTemplate(String fileName,ClassLoader loader){
        return getTemplate(PATH_DOWNLOAD_DEFAULT,fileName,loader);
    }

    /**
     * 获取指定文件夹下的excel文件
     * @param folder
     * @param fileName
     * @param loader
     * @return
     */
    public static  Workbook getTemplate(String folder,String fileName,ClassLoader loader){
        String path = (new StringBuilder()).append("META-INF").append(File.separator).append(folder).append(File.separator).append(fileName).toString();
        InputStream in = loader.getResourceAsStream(path);
        Workbook wb = null;
        try {
            if(fileName.endsWith(".xlsx")){
                wb = new XSSFWorkbook(in);
            }else if (fileName.endsWith(".xls")){
                wb = new HSSFWorkbook(in);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  wb;
    }



}
