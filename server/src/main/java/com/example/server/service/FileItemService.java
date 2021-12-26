package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.entity.FileItem;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author LIN
 */
public interface FileItemService extends IService<FileItem> {

    /**
     * 上传文件
     * @param multipartFile 上传文件
     * @return 文件uuid
     * @throws IOException
     */
    String uploadFile(MultipartFile multipartFile) throws IOException;

    /**
     * 下载文件
     * @param uuid 文件uuid
     * @param response 响应
     * @throws IOException
     */
    void getFileByUUID(String uuid, HttpServletResponse response) throws IOException;
}
