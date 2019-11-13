package com.moyulab.haige.haigelab.service;

import com.moyulab.haige.haigelab.common.Result;

import java.io.InputStream;

public interface ExcelService {

    Result importExcel(InputStream is, String fileName);

}
