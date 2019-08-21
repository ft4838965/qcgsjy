/**
 * Copyright (C), 2019-2019, 贵州宏思锐达科技有限公司
 * FileName: FileUploadUtil
 * Author:   Arron-Wu
 * Date:     2019/3/3 16:14
 * Description: 文件上传工具类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.stylefeng.guns.util;

import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.*;
import java.util.Iterator;
import java.util.UUID;

/**
 * 〈一句话功能简述〉<br>
 * 〈文件上传到本地服务器工具类〉
 *
 * @author Arron
 * @create 2019/3/3
 * @since 1.0.0
 */
public class FileUploadUtil {

    /**
     *
     * @param muiltRequest
     * @param path1 文件夹的相对路劲 如 "/static/img"  注:前加/ 后不加
     * @return
     * @throws IOException
     */
        public static String uploadFile(MultipartHttpServletRequest muiltRequest, String path1)  {
            // 获取遍历文件名
            Iterator iter=muiltRequest.getFileNames();
            String url=""; //返回的图片路径
            while (iter.hasNext()) {
                MultipartFile file=muiltRequest.getFile(iter.next().toString());
                System.out.println("-->>>"+file);
                if(!file.isEmpty()||file!=null) {     //获取原始文件名
                    try {
                        //获取跟目录
                        File path = null;

                        path = new File(ResourceUtils.getURL("classpath:").getPath());
                        if(!path.exists()) path = new File("");
                        //如果上传目录为static/api_img/，则可以如下获取：
                        File upload = new File(path.getAbsolutePath(),path1);
                        if(!upload.exists()) upload.mkdirs();
                        String filePath =upload.getAbsolutePath();

                        //处理文件名
                        String filename = file.getOriginalFilename();
                        InputStream is = file.getInputStream(); // 获取输入流,MultipartFile中可以直接得到文件的流
                        int pos = filename.lastIndexOf("."); // 取文件的格式
                        //唯一标识数字
                        UUID uuid = UUID.randomUUID();
                        String filenameurl = filePath + '\\' + uuid + filename.substring(pos);
                        // 获取输出流
                        OutputStream os = null;
                        os = new FileOutputStream(filenameurl);
                        // 创建一个缓冲区
                        byte[] buffer = new byte[1024];
                        // 判断输入流中的数据是否已经读完的标识
                        int len = 0;
                        // 循环将输入流读入到缓冲区当中，(len=is.read(buffer))>0就表示is里面还有数据
                        while ((len = is.read(buffer)) > 0) {
                            // 使用FileOutputStream输出流将缓冲区的数据写入到指定的目录当中
                            os.write(buffer, 0, len);
                        }
                        os.flush();
                        os.close();
                        is.close();

                        //返回资源路径
                        url = "http://"+Tool.getDomain()+path1+"/"+ uuid + filename.substring(pos);
                        return url;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return url;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return url;
                    }
                }
            }
           // url=url.substring(0, url.length()-1);
            return url;
        }

}
