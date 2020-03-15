package com.moyulab.cn.userservice.web;

import com.moyulab.cn.userservice.rpc.ExcelRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private ExcelRpcService excelRpcService;

    @GetMapping("export/excel")
    public Object exportExcel(String name){
        //调用excel模块接口
        return "user:" + excelRpcService.excelTotal(name);
    }
}
