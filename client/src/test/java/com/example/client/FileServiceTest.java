package com.example.client;

import com.example.client.service.FileService;
import org.junit.Test;

/**
 * 文件服务单元测试
 */
public class FileServiceTest {
    FileService service = new FileService();

    @Test
    public void testUpload() {
        //正常情况,文件路径存在
                String uuid = service.postFile("F:\\git_code\\Http-file-upload\\index.html");

        //异常情况,文件路径不存在
//        String uuid = service.postFile("123");
//        String uuid = service.postFile("");
//        String uuid = service.postFile(null);
                System.out.println(uuid);
    }

    @Test
    public void testDownLoad() {
        //正常情况,uuid对应文件存在
          service.getFile("51441629-20c2-4fd4-8388-392777da91f5", "E:/file/download");
//        service.getFile("51441629-20c2-4fd4-8388-392777da91f5", "");
        //异常情况
        // uuid对应文件找不到
//        service.getFile("123","");
        // uuid为空串或null
//        service.getFile(null,"");
//        service.getFile("","");
    }

    @Test
    public void testGetFileMetaData() {
        //正常情况,uuid对应文件存在
        String fileMeta = service.getFileMeta("51441629-20c2-4fd4-8388-392777da91f5");

        //异常情况
        // uuid对应文件找不到
//                 String fileMeta = service.getFileMeta("1213");
        // uuid为空串或null
//                String fileMeta = service.getFileMeta("");
//                String fileMeta = service.getFileMeta(null);
                  System.out.println(fileMeta);
    }


}
