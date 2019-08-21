package com.stylefeng.guns.util;

import com.aliyun.vod.upload.impl.PutObjectProgressListener;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.modular.media.service.IMediaService;
import com.stylefeng.guns.modular.system.model.Media;
import com.stylefeng.guns.modular.system.model.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Heyifan Cotter on 2018/11/6.
 * 视频操作类
 */
@RequestMapping("videoUtilOption")
@RestController
public class videoUtilOption {
    @Autowired
    private IMediaService pictureService;


    @Autowired
    public SettingConfiguration settingConfiguration;


    public static DefaultAcsClient initVodClient() {
        //点播服务所在的Region，国内请填cn-shanghai，不要填写别的区域
        String regionId = "cn-shanghai";
        DefaultProfile profile = DefaultProfile.getProfile(regionId, "LTAI77NwWIXY2QJh", "JfzKZmj4YUqczqXqimsQNHrEN1vOkt");
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }


    /**
     * 上传视频方法
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("uploadVideo")
    ResultMsg uploadVideo(HttpServletRequest request, HttpServletResponse response, String goodsTypeId) {
        String videoId = "";
        try {
            Map<String, MultipartFile> map = ((MultipartHttpServletRequest) request).getFileMap();
            MultipartFile multipartFile = null;
            for (Iterator<String> i = map.keySet().iterator(); i.hasNext(); ) {
                Object obj = i.next();
                multipartFile = (MultipartFile) map.get(obj);
            }
            String originalFilename = multipartFile.getOriginalFilename();

            Setting setting = settingConfiguration.getSetting();
            UploadStreamRequest requestVideo = new UploadStreamRequest(setting.getAliOssAccessId(), setting.getAliOssAccessKey(), "springCloud", originalFilename, multipartFile.getInputStream());

            /* 是否使用默认水印(可选)，指定模板组ID时，根据模板组配置确定是否使用默认水印*/
            requestVideo.setShowWaterMark(true);
            //设置封面图片
            requestVideo.setCoverURL("");
            /* 模板组ID(可选) */
//            requestVideo.setTemplateGroupId("8c4792cbc8694e7084fd5330e56a33d");
            /* 视频标签,多个用逗号分隔(可选) */
            requestVideo.setTags("标签1,标签2");
            //request.setDescription("视频描述");
            requestVideo.setDescription("视频描述");
            requestVideo.setPrintProgress(true);
            /* 设置自定义上传进度回调 (必须继承 ProgressListener) */
            requestVideo.setProgressListener(new PutObjectProgressListener());

            /* 存储区域(可选) */
            //request.setStorageLocation("in-201703232118266-5sejdln9o.oss-cn-shanghai.aliyuncs.com");
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse responseVideo = uploader.uploadStream(requestVideo);
            System.out.print("RequestId=" + responseVideo.getRequestId() + "\n");  //请求视频点播服务的请求ID
            if (responseVideo.isSuccess()) {
                System.out.print("VideoId=" + responseVideo.getVideoId() + "\n");
                videoId = responseVideo.getVideoId();
                Media picture = new Media();
                picture.setBaseId(goodsTypeId);
                picture.setVideoId(videoId);
                picture.setCheck(0);
                picture.setType(1);
                pictureService.insert(picture);
            } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
                System.out.print("VideoId=" + responseVideo.getVideoId() + "\n");
                System.out.print("ErrorCode=" + responseVideo.getCode() + "\n");
                System.out.print("ErrorMessage=" + responseVideo.getMessage() + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMsg.fail("接口调用失败!", HttpStatus.BAD_REQUEST.toString(), "");
        }
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), videoId);
    }


    @RequestMapping("getVideo")
    @ResponseBody
    public static String getPlayInfo(String videoId) throws Exception {
        DefaultAcsClient client = initVodClient();
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        request.setVideoId(videoId);
        return client.getAcsResponse(request).getPlayInfoList().get(0).getPlayURL();
    }

    /**
     * 删除视频
     */
    @RequestMapping("deleteVideoByVideoId")
    public String deleteVideoByVideoId(String videoId) throws Exception {
        try {
            DefaultAcsClient client = initVodClient();
            DeleteVideoRequest request = new DeleteVideoRequest();
            //多个用逗号分隔，最多支持20个
            request.setVideoIds(videoId + ",");
            client.getAcsResponse(request);
        } catch (ClientException e) {
            return "删除失败!";
        }
        return "删除成功!";
    }

    /**
     * 删除视频根据fileUrl
     *
     * @param fileUrl
     * @return
     * @throws Exception
     */
    @RequestMapping("deleteVideoByfileUrl")
    public String deleteVideoByfileUrl(String fileUrl) throws Exception {
        try {
            DefaultAcsClient client = initVodClient();
            EntityWrapper<Media> pictureEntityWrapper = new EntityWrapper<>();
            pictureEntityWrapper.where("oss_object_name={0}", fileUrl);
            List<Media> pictureList = pictureService.selectList(pictureEntityWrapper);
            String videoId = pictureList.get(0).getVideoId();
            Integer id = pictureList.get(0).getId();
            DeleteVideoRequest request = new DeleteVideoRequest();
            //多个用逗号分隔，最多支持20个
            request.setVideoIds(videoId + ",");
            client.getAcsResponse(request);
            pictureService.deleteById(id);
        } catch (ClientException e) {
            e.printStackTrace();
            return "删除失败!";
        }
        return "删除成功!";
    }
}

