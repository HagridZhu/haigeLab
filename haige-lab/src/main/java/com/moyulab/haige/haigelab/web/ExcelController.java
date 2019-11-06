package com.moyulab.haige.haigelab.web;

import com.moyulab.haige.haigelab.common.ExcelExportParam;
import com.moyulab.haige.haigelab.common.Result;
import com.moyulab.haige.haigelab.service.ExcelService;
import com.moyulab.haige.haigelab.util.ExportUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.*;


@RestController
@RequestMapping("excel")
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @PostMapping("import")
    public Object post(@RequestParam("file") MultipartFile multipartFile){
        InputStream inputStream = null;
        try{
            inputStream = multipartFile.getInputStream();
        }catch (Exception e){
            return Result.error(e.getMessage());
        }

        return excelService.importExcel(inputStream, multipartFile.getOriginalFilename());
    }

    @GetMapping("excel")
    public void downFile(HttpServletRequest request, HttpServletResponse response){
        ExcelExportParam param = new ExcelExportParam();
        param.setHeaderKeys(new String[]{"name","age","date"});
        param.setHeaderNames(new String[]{"姓名","年龄","日期"});
        param.setBySheetIndex(true);
        List<Map<String,Object>> dataList = new ArrayList<>();
        for (int i = 0 ; i < 10; i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("name", "小明");
            map.put("age", 10);
            map.put("date", new Date());
            dataList.add(map);
        }
        param.setData(dataList);
        XSSFWorkbook wb = new XSSFWorkbook();
        wb.createSheet();
        param.setTemplateWb(wb);
        ExportUtil.downExcel("test.xlsx",ExportUtil.writeExcel(param), request, response);
    }



}
