package com.moyulab.haige.haigelab.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moyulab.haige.haigelab.common.Result;
import com.moyulab.haige.haigelab.entity.RichWomen;
import com.moyulab.haige.haigelab.plusmapper.RichWomenMapper;
import com.moyulab.haige.haigelab.service.ExcelService;
import com.moyulab.haige.haigelab.util.UploadUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.io.InputStream;
import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private RichWomenMapper richWomenMapper;

    @Override
    public Result importExcel(InputStream is, String fileName) {
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
        try{
            Sheet sheet = wb.getSheetAt(0);
            List<Map<String, Object>> dataList = UploadUtil.readOneSheetForList(sheet, 1, 2, -1, 0, -1);
            List<RichWomen> richWomenList = new ArrayList<>();
            List<String> repeatList = new ArrayList<>();
            if (dataList != null && !dataList.isEmpty()) {
                dataList.stream().forEach(e -> {
                    RichWomen richWomen = new RichWomen();
                    richWomen.setName((String)e.get("name"));
                    richWomen.setAddress((String)e.get("address"));
                    richWomen.setSex((String)e.get("sex"));
                    richWomen.setLevel(Integer.valueOf((String)e.get("level")));
                    richWomen.setPhone((String)e.get("phone"));
                    richWomen.setTall((String)e.get("tall"));
                    richWomen.setWeight((String)e.get("weight"));
                    richWomen.setCreateDate(new Date());
                    String name = richWomen.getName();
                    if (name != null) {
                        QueryWrapper<RichWomen> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().eq(RichWomen::getName, name);
                        Integer integer = richWomenMapper.selectCount(queryWrapper);
                        if (integer > 0) {
                            repeatList.add(name);
                        }else{
                            richWomenMapper.insert(richWomen);
                            richWomenList.add(richWomen);
                        }
                    }
                });
            }

            if (!repeatList.isEmpty()) {
                return Result.error(String.join(",", repeatList) + ",名字已存在");
            }

            return Result.success(richWomenList);
        }catch (Exception e) {
            return Result.error("服务器错误：" + e.getMessage());
        }
    }

    public Workbook getWorkbook(String fileName, InputStream is) throws Exception {
        if (fileName.endsWith("xlsx")) {
            return new XSSFWorkbook(is);
        }
        return new HSSFWorkbook(is);
    }



}
