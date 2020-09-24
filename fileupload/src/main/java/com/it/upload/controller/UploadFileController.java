package com.it.upload.controller;

import com.it.upload.service.UploadFileServer;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/upload")
public class UploadFileController {


     @Autowired
    private UploadFileServer uploadFileServer;


     @PostMapping("/test")
     public String testUploadFile(@RequestParam("file") MultipartFile file){
         this.uploadFileServer.uploadFile(file);
           return "success";
     }

  @PostMapping("/bigfile")
  public String testUploadFile(HttpServletRequest httpRequest, @RequestParam("file") MultipartFile file  , @RequestParam("guid") String guid, @RequestParam("Integer") Integer chunk, @RequestParam("chunks")  Integer chunks){
    this.uploadFileServer.uploadFile(file);
    boolean isMultipart = ServletFileUpload.isMultipartContent(httpRequest);
    if (isMultipart) {

    }
    return "success";
  }


}
