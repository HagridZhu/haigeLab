package com.moyulab.cn.userservice.rpc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("excel-service")
public interface ExcelRpcService {

    @RequestMapping("excel/total")
    String excelTotal(@RequestParam("name") String name);

}
