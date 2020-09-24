package com.it.upload.service.impl;

import com.it.upload.service.UploadFileServer;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/***
 *
 */
@Service
public class UploadFileServerImpl implements UploadFileServer {


//  @Value()
  private String filePath;

    public void uploadFile(MultipartFile multipartFile) {

         System.out.println(multipartFile.getName()+" size: "+multipartFile.getSize());
    }


  public void bigFile(HttpServletRequest request, HttpServletResponse response, String guid, Integer chunk, MultipartFile file, Integer chunks){
    if(chunk == null || chunks == null){
      chunk = 0;
      chunks = 1;
    }
    try {

        // 临时目录用来存放所有分片文件
        String tempFileDir = filePath + guid;
        File parentFileDir = new File(tempFileDir);
        if (!parentFileDir.exists()) {
          parentFileDir.mkdirs();
        }
        // 分片处理时，前台会多次调用上传接口，每次都会上传文件的一部分到后台
        String tempFileName = guid + "_" + chunk + ".part";
        File tempPartFile = new File(parentFileDir, tempFileName);
        System.out.println("guid:" + guid);
        System.out.println("fileName:" + file.getOriginalFilename());
        System.out.println("tempFileName:" + tempFileName);
        System.out.println("chunk:" + chunk + ", chunks:" + chunks);
        System.out.println("---------------------------------");
//        FileUtils.copyInputStreamToFile(file.getInputStream(), tempPartFile);

    } catch (Exception e) {
      e.printStackTrace();
    }


  }


}
