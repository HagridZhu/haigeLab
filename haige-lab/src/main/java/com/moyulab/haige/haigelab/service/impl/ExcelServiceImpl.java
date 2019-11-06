package com.moyulab.haige.haigelab.service.impl;

import com.moyulab.haige.haigelab.common.Result;
import com.moyulab.haige.haigelab.service.ExcelService;
import com.moyulab.haige.haigelab.util.UploadUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Override
    public Object importExcel(InputStream is, String fileName) {
        if (!StringUtils.hasText(fileName)) {
            return Result.error("文件名不能为空");
        }
        if (!fileName.endsWith("xls") && !fileName.endsWith("xlsx")) {
            return Result.error("文件格式不正确，仅支持xls/xlsx");
        }
        Workbook wb = null;
        try {
            wb = getWorkbook(fileName, is);
        }catch (Exception e){
            return Result.error("获取excel失败：" + e.getMessage());
        }
        Sheet sheet = wb.getSheetAt(0);
        List<Map<String, Object>> dataList = UploadUtil.readOneSheetForList(sheet, 0, 1, -1, 0, -1);
        return Result.success(dataList);
    }

    public Workbook getWorkbook(String fileName, InputStream is) throws Exception {
        if (fileName.endsWith("xlsx")) {
            return new XSSFWorkbook(is);
        }
        return new HSSFWorkbook(is);
    }



}
