package com.stylefeng.guns.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
public class OSSClientUtil {
    public Map<String,Object> updateHead(MultipartFile file) throws IOException{
        if (file == null || file.getSize() <= 0) {
            throw new RuntimeException("图片不能为空");
        }
        Map<String,Object>result=uploadImg2Oss(file);
        String name = result.get("name").toString();
        String imgUrl = getImgUrl(name);
        result.put("imgUrl",imgUrl);
//        return imgUrl;
        return result;
    }


    private String endpoint;
    // accessKey
    private String accessId;
    private String accesskey;
    //空间
    private String bucketName;
    //文件存储目录
    private String filedir;

    public OSSClientUtil(String endpoint, String accessId, String accesskey, String bucketName, String filedir) {
        this.endpoint = endpoint;
        this.accessId = accessId;
        this.accesskey = accesskey;
        this.bucketName = bucketName;
        this.filedir = filedir;
        ossClient=new OSSClient(endpoint,accessId,accesskey);
    }

    private OSSClient ossClient;

    public OSSClientUtil() {
        ossClient = new OSSClient(endpoint, accessId, accesskey);
    }

    /**
     * 初始化
     */
    public void init() {
        ossClient = new OSSClient(endpoint, accessId, accesskey);
    }

    /**
     * 销毁
     */
    public void destory() {
        ossClient.shutdown();
    }

    /**
     * 上传图片
     *
     * @param url
     */
    public void uploadImg2Oss(String url) {
        File fileOnServer = new File(url);
        FileInputStream fin;
        try {
            fin = new FileInputStream(fileOnServer);
            String[] split = url.split("/");
            this.uploadFile2OSS(fin, split[split.length - 1]);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("图片上传失败");
        }
    }


//    public String uploadImg2Oss(MultipartFile file) {
    public Map<String,Object> uploadImg2Oss(MultipartFile file) {
//        if (file.getSize() > 1024 * 1024) {
//            throw new RuntimeException("上传图片大小不能超过1M！");
//        }
        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        Random random = new Random();
        String name = random.nextInt(10000) + System.currentTimeMillis() + substring;
        Map<String,Object>result=new HashMap<>();
        try {
            InputStream inputStream = file.getInputStream();
            result=this.uploadFile2OSS(inputStream, name);
//            return name;
            result.put("name",name);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("图片上传失败");
        }
    }

    /**
     * 获得图片路径
     *
     * @param fileUrl
     * @return
     */
    public String getImgUrl(String fileUrl) {
        if (!StringUtils.isEmpty(fileUrl)) {
            String[] split = fileUrl.split("/");
            return this.getUrl(this.filedir + split[split.length - 1]);
        }
        return null;
    }

    /**
     * 上传到OSS服务器  如果同名文件会覆盖服务器上的
     *
     * @param instream 文件流
     * @param fileName 文件名称 包括后缀名
     * @return 出错返回"" ,唯一MD5数字签名
     */
    public Map<String,Object> uploadFile2OSS(InputStream instream, String fileName) {
        String ret = "";
        Map<String,Object>result=new HashMap<>();
        try {
            //创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            //上传文件
            //ObjectName为filedir + fileName,这个想办法传回去,让数据库记录起来,在删除记录的时候,还需要把ObjectName传给阿里云,删除服务器上资源
            PutObjectResult putResult = ossClient.putObject(bucketName,filedir + fileName,instream,objectMetadata);
//            PutObjectResult putResult = ossClient.putObject(new PutObjectRequest(bucketName, filedir + fileName, instream).<PutObjectRequest>withProgressListener(new PutObjectProgressListener()));
            ret = putResult.getETag();
            /*李俊添加,开始*/
            result.put("ret",ret);
            result.put("ALi_ObjectName",(filedir + fileName));
            /*李俊添加,结束*/
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        return ret;
        return result;
    }

    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     *
     * @param FilenameExtension 文件后缀
     * @return String
     */
    public static String getcontentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpeg";
        }
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx") ||
                FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase(".docx") ||
                FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        if (FilenameExtension.equalsIgnoreCase(".mp4")) {
            return "video/mp4";
        }
        return "image/jpeg";
    }

    /**
     * 获得url链接
     *
     * @param key
     * @return
     */
    public String getUrl(String key) {
        // 设置URL过期时间为10年  3600l* 1000*24*365*10
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }
}
