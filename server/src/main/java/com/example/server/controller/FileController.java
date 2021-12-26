package com.example.server.controller;

import com.example.server.common.WebLog;
import com.example.server.entity.FileItem;
import com.example.server.service.FileItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 文件接口
 * @author LIN
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {
     @Autowired
     FileItemService fileItemService;

    @WebLog(description = "上传文件")
    @PostMapping("")
    public String postFile(HttpServletRequest request,@RequestParam("file") MultipartFile multipartFile) throws IOException {
        String uuid=fileItemService.uploadFile(multipartFile);
        return uuid;
    }

    @WebLog(description = "下载文件")
    @GetMapping("/{uuid}")
    public void getFile(@PathVariable(value = "uuid",required = false)String uuid,HttpServletResponse response) throws IOException {
         fileItemService.getFileByUUID(uuid,response);
    }

    @WebLog(description = "获取文件元数据")
    @GetMapping("/metaInfo/{uuid}")
    public FileItem getFileMeta(@PathVariable("uuid")String uuid){
        FileItem fileItem = fileItemService.getById(uuid);
        log.info("文件元数据是：{}",fileItem.toString());
        return fileItem;
    }


}
