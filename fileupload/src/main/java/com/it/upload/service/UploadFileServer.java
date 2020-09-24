package com.it.upload.service;

import org.springframework.web.multipart.MultipartFile;

/***
 * 上传文件服务
 */
public interface UploadFileServer {


    public void uploadFile(MultipartFile multipartFile);

}
