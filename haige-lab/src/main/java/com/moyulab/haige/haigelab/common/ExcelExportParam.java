package com.moyulab.haige.haigelab.common;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * 
 * 
 * <pre>
 * Excel导出参数类。
 * </pre>
 * 
 * @author zhihai.zhu@partner.midea.com
 * @version 1.00.00 2018年04月01日
 * 
 *          <pre>
 * 修改记录
 *    修改后版本: 0.1    修改人：zhuzh2  修改日期:2018年04月01日
 * </pre>
 */
public class ExcelExportParam {
	private final static Integer TEMPLATE_CH = Integer.valueOf(1);
	
	private Workbook templateWb;//excel模板
	private Integer timeZone;//时区 默认东8区
	private String language;//语言 默认中文
	private String sheetName;//表格名
	private int sheetIndex;//表格下标
	private int startRow;//开始填充数据的行，从0开始算
	private boolean bySheetIndex;//是否通过表格下标获取表格，默认false,通过表格名
	private String[] headerNames;//显示的表头名
	private String[] headerKeys;//列的字段名
	private int areaCodeTemplate;//国家模板
	private List<Map<String,Object>> data;//写入的数据
	private String dateFormat;//日期格式
	private Integer hideRow;//隐藏行
	/**
	 * <pre>
	 * 初始化参数
	 * startRow: 1
	 * areaCodeTemplate: 1( Midea )
	 * </pre>
	 */
	public ExcelExportParam(){
		this.startRow = 1;
		this.areaCodeTemplate = TEMPLATE_CH;
		this.timeZone = 8;
		this.language = "zh-cn";
	}
	public Integer getHideRow() {
		return hideRow;
	}
	public void setHideRow(Integer hideRow) {
		this.hideRow = hideRow;
	}
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	public Integer getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(Integer timeZone) {
		this.timeZone = timeZone;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String[] getHeaderNames() {
		return headerNames;
	}
	public void setHeaderNames(String[] headerNames) {
		this.headerNames = headerNames;
	}
	public String[] getHeaderKeys() {
		return headerKeys;
	}
	public void setHeaderKeys(String[] headerKeys) {
		this.headerKeys = headerKeys;
	}
	public int getStartRow() {
		return startRow;
	}
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	public Workbook getTemplateWb() {
		return templateWb;
	}
	public void setTemplateWb(Workbook templateWb) {
		this.templateWb = templateWb;
	}
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public int getSheetIndex() {
		return sheetIndex;
	}
	public void setSheetIndex(int sheetIndex) {
		this.sheetIndex = sheetIndex;
	}
	public boolean isBySheetIndex() {
		return bySheetIndex;
	}
	public void setBySheetIndex(boolean bySheetIndex) {
		this.bySheetIndex = bySheetIndex;
	}
	public int getAreaCodeTemplate() {
		return areaCodeTemplate;
	}
	public void setAreaCodeTemplate(int areaCodeTemplate) {
		this.areaCodeTemplate = areaCodeTemplate;
	}
	public List<Map<String, Object>> getData() {
		return data;
	}
	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}
	
	
	
}
