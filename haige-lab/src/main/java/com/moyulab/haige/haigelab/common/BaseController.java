package com.moyulab.haige.haigelab.common;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class BaseController {

    protected void testDownFile(HttpServletRequest request, HttpServletResponse response, String fileName){
        OutputStream out=null;
        String userAgent = request.getHeader("User-Agent");
        try {
            if (userAgent.contains("MSIE")||userAgent.contains("Trident")) {
                //针对IE或者以IE为内核的浏览器：
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else {
                //非IE浏览器的处理：
                fileName = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", fileName));
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        try {
            String headStr = new StringBuilder()
                    .append("attachment;fileName=")
                    .append(new String(fileName.getBytes("UTF-8"),"ISO-8859-1"))
//					.append(URLEncoder.encode(fileName, "UTF-8"))
                    .toString();
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", headStr);
            out = response.getOutputStream();
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("META-INF/downloads/" + fileName);
            byte[] buf = new byte[1024];
            int len = 0;

            while((len = is.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.flush();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void downloadFile(HttpServletResponse response, String filePath) {
        String[] arr = filePath.split("/");
        this.downloadFile(response, filePath, arr[arr.length - 1]);
    }

    protected void downloadFile(HttpServletResponse response, String filePath,
                                String downloadName) {
        ClassLoader loader = this.getClass().getClassLoader();
        String path = "META-INF/downloads/" + filePath;
        InputStream in = loader.getResourceAsStream(path);
        this.downloadFile(response, downloadName, in);
    }

    protected void downloadFile(HttpServletResponse response,
                                String downloadName, InputStream in) {
        try {
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName="
                    + URLEncoder.encode(downloadName, "UTF-8"));
        } catch (UnsupportedEncodingException var15) {
            throw new HaigeLabException("下载文件名编码时出现错误.", var15);
        }

        try {
            byte[] buf = new byte[1024];
            ServletOutputStream out = response.getOutputStream();
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            out.flush();
        } catch (IOException var16) {
            throw new HaigeLabException("read temp error....", var16);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
