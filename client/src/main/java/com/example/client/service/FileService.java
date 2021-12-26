package com.example.client.service;

import com.sun.deploy.net.HttpResponse;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.util.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

/**
 * 文件服务
 *
 * @author LIN
 */
public class FileService {
    Logger logger=Logger.getLogger("FileService");
    public static void main(String[] args) {
        FileService service=new FileService();
        String uuid = service.postFile("index.html");
        service.getFile("21212","");
    }

    /**
     * 上传文件
     * @param filePath 文件路径名
     * @return 文件uuid
     */
    public String postFile(@NotNull String filePath) {
        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String uuid = null;
        // 换行符
        final String newLine = "\r\n";
        final String boundaryPrefix = "--";
        // 定义数据分隔线
        String BOUNDARY = "========7d4a6d158c9";
        try {
            //打开连接
            URL url = new URL("http://localhost:8080/file");
            connection = (HttpURLConnection) url.openConnection();
            //设置连接请求方式
            connection.setRequestMethod("POST");
            // 设置超时时间
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(60000);
            //设置读写
            connection.setDoOutput(true);
            connection.setDoInput(true);
            // 设置请求头参数
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            connection.setRequestProperty("Charset", "UTF-8");
            os = connection.getOutputStream();

            // 上传文件
            if(uuid==null||uuid.length()==0){
                throw new FileNotFoundException("文件不存在");
            }
            File file = new File(filePath);
            System.out.println(file.getAbsolutePath());
            if(!file.isFile()){
                throw new FileNotFoundException("文件不存在");
            }

            StringBuilder sb = new StringBuilder();
            sb.append(boundaryPrefix);
            sb.append(BOUNDARY);
            sb.append(newLine);
            // 添加文件参数头
            sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName()
                    + "\"" + newLine);
            sb.append("Content-Type:application/octet-stream");
            // ，参数头设置完以后需要两个换行然后才是参数内容
            sb.append(newLine);
            sb.append(newLine);
            // 将参数头的数据写入到输出流中
            os.write(sb.toString().getBytes());


            //读取文件作为参数内容写入输出流
            DataInputStream in=new DataInputStream(new FileInputStream(file));
            byte[] buffer=new byte[1024];
            int bytes=0;
            while ((bytes = in.read(buffer)) != -1) {
                os.write(buffer, 0, bytes);
            }
            os.write(newLine.getBytes());
            in.close();

            //分割线结尾
            StringBuilder end_date=new StringBuilder();
            end_date.append(newLine).append(boundaryPrefix)
                    .append(BOUNDARY).append(boundaryPrefix)
                    .append(newLine);
            os.write(end_date.toString().getBytes());
            os.flush();
            os.close();

            //读取响应信息
            is=connection.getInputStream();
            br=new BufferedReader(new InputStreamReader(is));
            uuid=br.readLine();
            br.close();
            is.close();
            logger.info("文件uuid为："+uuid);
            logger.info("上传成功");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return uuid;
    }

    /**
     * 下载文件
     * @param uuid 文件uuid
     * @param filePath 文件保存路径
     */
    public void getFile(@NotNull String uuid, @Nullable String filePath) {
        HttpURLConnection connection=null;
        InputStream is=null;
        try {
            if(uuid==null||uuid.length()==0){
                throw new FileNotFoundException("uuid不能为空");
            }
            if(filePath==null){
                filePath="";
            }
            URL url=new URL("http://localhost:8080/file/"+uuid);
            connection= (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            // 设置超时时间
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(60000);
            //设置只读
            connection.setDoInput(true);

            int responseCode = connection.getResponseCode();
            if(responseCode==401){
                logger.warning("下载异常");
                return;
            }
            File dir=new File(filePath);
            if(!dir.isDirectory()){
                dir.mkdir();
            }
            String fileInfo = connection.getHeaderField("Content-Disposition");
            String fileName=fileInfo.substring(fileInfo.lastIndexOf("=")+1);
            File file=new File(dir,fileName);
            FileOutputStream out=new FileOutputStream(file);
            is=connection.getInputStream();
            byte[] buffer=new byte[1024];
            int bytes=0;
            while ((bytes=is.read(buffer))!=-1){
                out.write(buffer, 0, bytes);
            }
            is.close();
            out.close();
            logger.info("下载成功");
            logger.info("文件路径为："+file.getAbsolutePath());

        } catch (MalformedURLException e) {
            logger.warning(e.getMessage());
        } catch (IOException e) {
            logger.warning(e.getMessage());
        }

    }

    /**
     * 获取文件元数据
     * @param uuid 文件uuid
     * @return json格式文件元数据
     */
    public String getFileMeta(@NotNull String uuid) {
        HttpURLConnection connection=null;
        InputStream is=null;
        BufferedReader reader=null;
        String fileMetaData =null;
        try {
            if(uuid==null||uuid.length()==0){
                throw new FileNotFoundException("uuid不能为空");
            }
            URL url=new URL("http://localhost:8080/file/metaInfo/"+uuid);
            connection= (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            // 设置超时时间
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(60000);
            connection.setDoInput(true);

            is=connection.getInputStream();
            reader=new BufferedReader(new InputStreamReader(is));
            fileMetaData=reader.readLine();
            is.close();
            reader.close();
            if(fileMetaData==null){
                logger.info("找不到uuid为"+uuid+"的文件元数据");
                return fileMetaData;
            }
            logger.info("文件元数据为："+fileMetaData);
            return fileMetaData;

        } catch (MalformedURLException e) {
            logger.warning(e.getMessage());
        } catch (IOException e) {
            logger.warning(e.getMessage());
        }

        return "";
    }
}
