package com.moyulab.cn.excelservice.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("excel")
public class ExcelController {

    @GetMapping("test")
    public Object test(){
        return "excel/test=" + new Random().nextInt(100);
    }
}
