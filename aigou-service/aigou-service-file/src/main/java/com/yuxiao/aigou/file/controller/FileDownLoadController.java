package com.yuxiao.aigou.file.controller;

import com.yuxiao.aigou.file.util.FastDFSUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Date;

@RestController
@RequestMapping(value = "/file/download")
@CrossOrigin
public class FileDownLoadController {
    /**
     * 附件下载  浏览器访问  会弹出一个下载框  然后进行下载
     * @return
     */
    @GetMapping
    public void fileDownLoad(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("执行文件下载");
        //附件下载
        response.setHeader("Content-Disposition","attachment; filename=rBBRH2FP6MqAa9OTAAABNJUm7P0325.txt");
        //浏览器响应信息中的输出流
        ServletOutputStream os = response.getOutputStream();
        //服务器中要下载的文件的文件流
        InputStream downLoadFile = FastDFSUtil.downLoadFile("group1", "M00/00/00/rBBRH2FP6MqAa9OTAAABNJUm7P0325.txt");
        //将输入流复制到response中的输出流
        IOUtils.copy(downLoadFile, os);
        //关闭流操作
        IOUtils.closeQuietly(downLoadFile);
        IOUtils.closeQuietly(os);
    }
    @RequestMapping(value = "/download",method = RequestMethod.GET)
    public ResponseEntity fileDownLoad() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=rBBRH2FP6MqAa9OTAAABNJUm7P0325.txt");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Last-Modified", new Date().toString());

        InputStream downLoadFile = FastDFSUtil.downLoadFile("group1", "M00/00/00/rBBRH2FP6MqAa9OTAAABNJUm7P0325.txt");

        return ResponseEntity
                .ok()
                .headers(headers)
//                .contentLength(1024) //设置为文件的真实大小  否则下载会有问题  不设置也可以
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(downLoadFile));
    }
}
