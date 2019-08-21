package com.stylefeng.guns.modular.muchPhoto;

import com.aliyun.vod.upload.impl.UploadImageImpl;
import com.aliyun.vod.upload.req.UploadImageRequest;
import com.aliyun.vod.upload.resp.UploadImageResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.modular.media.service.IMediaService;
import com.stylefeng.guns.modular.system.model.Media;
import com.stylefeng.guns.modular.system.model.Setting;
import com.stylefeng.guns.util.ResultMsg;
import com.stylefeng.guns.util.SettingConfiguration;
import com.stylefeng.guns.util.Tool;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Arron Cotter on 2019/2/25.
 */
@RequestMapping("muchPhoto")
@RestController
public class muchPhoto {
    @Autowired
    public SettingConfiguration settingConfiguration;


    /**
     * Spring配置restTemplate第三方接口  可以识别Gzip格式的Response 返回的数据 请不要修改
     *
     * @return Spring RestTemplate模板
     */
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory()); // 使用HttpClient，支持GZIP
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8)); // 支持中文编码
        return restTemplate;
    }

    @Autowired
    private IMediaService mediaService;

    @RequestMapping("upload")
    @ResponseBody
    public String upload(HttpServletRequest request, HttpServletResponse response, String goodsTypeId) {

        try {
            Map<String, MultipartFile> map = ((MultipartHttpServletRequest) request).getFileMap();
            MultipartFile multipartFile = null;
            for (Iterator<String> i = map.keySet().iterator(); i.hasNext(); ) {
                Object obj = i.next();
                multipartFile = (MultipartFile) map.get(obj);
            }

            Setting setting = settingConfiguration.getSetting();
            UploadImageRequest requestPhoto = new UploadImageRequest(setting.getAliOssAccessId(), setting.getAliOssAccessKey(), "default");

            if (multipartFile != null) {
                requestPhoto.setInputStream(multipartFile.getInputStream());
            }

            UploadImageImpl uploadImage = new UploadImageImpl();
            UploadImageResponse responsePhoto = uploadImage.upload(requestPhoto);

            Media media = new Media();
            media.setBaseId(goodsTypeId);
            media.setOssObjectName(responsePhoto.getImageURL());
            media.setImageId(responsePhoto.getImageId());
            media.setType(0);
            media.setCheck(0);
            media.setCreateTime(new DateTime());
            mediaService.insert(media);

        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
            return "图片上传失败!";
        }
        return "图片上传成功!";
    }


    @RequestMapping("deletephoto")
    @ResponseBody
    //删除到图片
    public String deletephoto(HttpServletRequest request, HttpServletResponse response) {
        try {
            Setting setting = settingConfiguration.getSetting();
            ResultMsg resultMsg = new ResultMsg();
            String imagesObjectName = (String) request.getParameter("key");//获取图片id
            EntityWrapper<Media> entityWrapper = new EntityWrapper<>();
            entityWrapper.where("oss_object_name={0}", imagesObjectName);
            List<Media> pictureList = mediaService.selectList(entityWrapper);
            String imagesId = pictureList.get(0).getImageId();
            Integer pid = pictureList.get(0).getId();
            Tool.deleteAliVideoImg(imagesId, setting.getAliOssAccessId(), setting.getAliOssAccessKey());
            mediaService.deleteById(pid);

        } catch (RestClientException e) {
            e.printStackTrace();
            return "删除图片失败!";
        }
        return "删除图片成功!";
    }


    @RequestMapping("wangUpload")
    @ResponseBody
    //wang富文本编辑器图片上传方法
    public JSONObject wangUpload(HttpServletRequest request) {
        JSONObject object = new JSONObject();

        try {
            Map<String, MultipartFile> map = ((MultipartHttpServletRequest) request).getFileMap();
            MultipartFile multipartFile = null;
            for (Iterator<String> i = map.keySet().iterator(); i.hasNext(); ) {
                Object obj = i.next();
                multipartFile = (MultipartFile) map.get(obj);
            }

            Setting setting = settingConfiguration.getSetting();
            UploadImageRequest requestPhoto = new UploadImageRequest(setting.getAliOssAccessId(), setting.getAliOssAccessKey(), "default");

            if (multipartFile != null) {
                requestPhoto.setInputStream(multipartFile.getInputStream());
            }

            UploadImageImpl uploadImage = new UploadImageImpl();
            UploadImageResponse responsePhoto = uploadImage.upload(requestPhoto);

            object.put("err", "0");
            object.put("url", responsePhoto.getImageURL());
        } catch (Exception e) {
            e.printStackTrace();
            return object;
        }
        return object;
    }



}
