package com.example.server.service.Impl;

import ch.qos.logback.core.util.DatePatternToRegexUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.entity.FileItem;
import com.example.server.mapper.FileItemMapper;
import com.example.server.service.FileItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.datetime.standard.DateTimeFormatterFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @author LIN
 */
@Service
@Slf4j
public class FileItemServiceImpl extends ServiceImpl<FileItemMapper, FileItem> implements FileItemService {
    @Value("${file.dir}")
    private  String fileDir;

    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        LocalDate date=LocalDate.now();
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyyMMdd");
        String formatDate=date.format(formatter);
        File dir=new File(fileDir,formatDate);
        if(!dir.isDirectory()){
            dir.mkdir();
        }
        String uuid=UUID.randomUUID().toString();
        String originalFileName=multipartFile.getOriginalFilename();
        String fileName=uuid+originalFileName;
        File file=new File(dir,fileName);
        multipartFile.transferTo(file);
        FileItem fileItem=new FileItem();

        fileItem.setId(uuid);
        fileItem.setName(originalFileName);
        fileItem.setSize(multipartFile.getSize());
        String type=originalFileName.substring(originalFileName.lastIndexOf("."));
        fileItem.setType(type);
        fileItem.setUrl(file.getPath());
        boolean save = this.save(fileItem);
        log.info("上传文件uuid是{}",uuid);
        return save?uuid:"上传失败";
    }

    @Override
    public void getFileByUUID(String uuid, HttpServletResponse response) throws IOException {
        log.info("获取文件的uuid是{}",uuid);
        FileItem fileItem = this.getById(uuid);
        if(fileItem==null){
            log.info("文件不存在");
            response.sendError(401,"下载失败");
            return;
        }
        String url = fileItem.getUrl();
        File file=new File(url);
        if(file.isFile()){
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition","attachment;filename="+file.getName());
            FileInputStream in=new FileInputStream(file);
            DataOutputStream out=new DataOutputStream(response.getOutputStream());
            byte[] buffer=new byte[1024];
            int bytes=0;
            while ((bytes=in.read(buffer))!=-1){
                out.write(buffer,0,bytes);
            }
            log.info("文件传输成功");
            in.close();
            out.close();
        }else {
            log.info("文件不存在");
            response.sendError(401,"下载失败");
        }

    }

}
