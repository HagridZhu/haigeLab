package com.moyulab.haige.haigelab.service;

import java.io.InputStream;

public interface ExcelService {

    Object importExcel(InputStream is, String fileName);

}
