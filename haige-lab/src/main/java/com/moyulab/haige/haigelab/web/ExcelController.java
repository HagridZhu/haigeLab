package com.moyulab.haige.haigelab.web;

import com.moyulab.haige.haigelab.common.BaseController;
import com.moyulab.haige.haigelab.common.ExcelExportParam;
import com.moyulab.haige.haigelab.common.Result;
import com.moyulab.haige.haigelab.entity.RichWomen;
import com.moyulab.haige.haigelab.plusmapper.RichWomenMapper;
import com.moyulab.haige.haigelab.service.ExcelService;
import com.moyulab.haige.haigelab.util.ExportUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URL;
import java.util.*;


@RestController
@RequestMapping("excel")
@Slf4j
public class ExcelController extends BaseController {

    @Autowired
    private ExcelService excelService;
    @Autowired
    private RichWomenMapper richWomenMapper;

    @PostMapping("import")
    public Result post(@RequestParam("file") MultipartFile multipartFile){
        InputStream inputStream = null;
        try{
            inputStream = multipartFile.getInputStream();
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
        return excelService.importExcel(inputStream, multipartFile.getOriginalFilename());
    }

    @GetMapping("download/template")
    public void dolownLoad(HttpServletRequest request,HttpServletResponse response){
        this.testDownFile(request, response, "template.xlsx");
    }

    @RequestMapping("list")
    public Object list(){
        URL resource = this.getClass().getClassLoader().getResource("");
        if (resource != null) {
            log.info(resource.getPath());
        }

        log.info("这是info{},{},{}","arg1","arg2","arg3");
        log.debug("这是debug{},{},{}","arg1","arg2","arg3");
        log.error("这是error{},{},{}","arg1","arg2","arg3");
        log.warn("这是warn{},{},{}","arg1","arg2","arg3");
        Map<String,Object> map = new HashMap<>();
        map.put("ADDRESS", "广州");
        List<RichWomen> richWomen = richWomenMapper.selectByMap(map);
        System.out.println(richWomen);
        try{
            map.get("aa").toString();
        }catch (Exception e){
//            e.printStackTrace();
            log.error("test error", e);
        }
        return Result.success(richWomenMapper.selectByMap(null));
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
