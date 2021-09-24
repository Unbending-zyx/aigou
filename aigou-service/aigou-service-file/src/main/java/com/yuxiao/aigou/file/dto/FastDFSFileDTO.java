package com.yuxiao.aigou.file.dto;

import java.io.Serializable;

/**
 * 封装文件上传信息
 *      时间
 *      上传者
 *      类型
 *      大小
 *      附加信息
 *      后缀
 *      文件内容
 *
 */
public class FastDFSFileDTO implements Serializable {
    //文件名
    private String fileName;
    //文件内容 字节数组
    private byte[] content;
    //文件扩展名
    private String ext;
    //文件的MD5摘要值
    private String md5;
    //文件创建者
    private String author;

    public FastDFSFileDTO() {
    }

    public FastDFSFileDTO(String fileName, byte[] content, String ext) {
        this.fileName = fileName;
        this.content = content;
        this.ext = ext;
    }

    public FastDFSFileDTO(String fileName, byte[] content, String ext, String md5, String author) {
        this.fileName = fileName;
        this.content = content;
        this.ext = ext;
        this.md5 = md5;
        this.author = author;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
