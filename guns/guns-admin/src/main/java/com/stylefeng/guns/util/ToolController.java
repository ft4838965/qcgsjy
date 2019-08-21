package com.stylefeng.guns.util;

import com.aliyun.vod.upload.impl.UploadImageImpl;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadImageRequest;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadImageResponse;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.modular.media.service.IMediaService;
import com.stylefeng.guns.modular.setting.service.ISettingService;
import com.stylefeng.guns.modular.system.dao.Dao;
import com.stylefeng.guns.modular.system.model.Media;
import com.stylefeng.guns.modular.system.model.Setting;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
   阿里云上传图片工具类
 */
@Controller
@RequestMapping("/tool")
public class ToolController {
    @Autowired
    private ISettingService settingService;
    @Autowired
    private IMediaService pictureService;
    @Autowired
    private Dao dao;



    /**
     * 有做后台管理员登录限制
     */
    @RequestMapping("/uploadFile")
    @ResponseBody
    public Object uploadFile(@RequestParam("file") MultipartFile file, String objectType, String old_picture_id, String activity_id,String old_pid/*星厨报道表用到的*/,String reported_id/*星厨报道表id*/) {
        Map<String, Object> value = new HashMap<String, Object>();
        value.put("success", true);
        value.put("errorCode", 0);
        value.put("errorMsg", "");
        Setting setting = settingService.selectById(1);
        if (setting == null) {
            value.put("success", false);
            value.put("errorCode", 200);
            value.put("errorMsg", "系统设置'阿里云云存储'所有参数为空,请到'系统版块'→'系统设置'填写相关参数");
            return value;
        }
//        OSSClientUtl.getAliOssEndpoint(),setting.getAliOssAccessId(),setting.getAliOssAccessKey(),setting.getAliOssBucket(),setting.getAliOssFilePath());
        try {
            InputStream inputStream = file.getInputStream();
            if (!Tool.isNull(objectType) && "video".equals(objectType)) {
                UploadStreamRequest request = new UploadStreamRequest(setting.getAliOssAccessId(), setting.getAliOssAccessKey(), file.getOriginalFilename(), file.getOriginalFilename(), inputStream);
                UploadVideoImpl uploader = new UploadVideoImpl();
                UploadStreamResponse response = uploader.uploadStream(request);
                if ("Success".equals(response.getCode())) {
                    Media picture = new Media();
                    if (!Tool.isNull(old_picture_id)) {
                        picture = pictureService.selectOne(new EntityWrapper<Media>().eq("video_id", old_picture_id));
                        if (picture != null && !Tool.isNull(picture.getVideoId())) {
                            Tool.deleteAliVideo(
                                    picture.getVideoId(),
                                    setting.getAliOssAccessId(),
                                    setting.getAliOssAccessKey());
                        }
                        dao.deleteBySQL("delete from t_media where video_id='" + old_picture_id + "'");
                    }
                    if(!Tool.isNull(old_pid)){
                        picture=pictureService.selectById(old_pid);
                        if (picture != null && !Tool.isNull(picture.getVideoId())) {
                            Tool.deleteAliVideo(
                                    picture.getVideoId(),
                                    setting.getAliOssAccessId(),
                                    setting.getAliOssAccessKey());
                        }
                        pictureService.deleteById(Integer.valueOf(old_pid));
                    }
                    picture = new Media();
                    picture.setVideoId(response.getVideoId());
                    picture.setType(1);
                    picture.setOssObjectName("转码中...(可以先保存,转码结果稍后会自动保存到数据库中)");
                    pictureService.insert(picture);

                    value.put("pid", picture.getId());
                    value.put("object_name", picture.getVideoId());
                    value.put("data", "转码中...(可以先保存,转码结果稍后会自动保存到数据库中)");
                } else {
                    value.put("success", false);
                    value.put("errorCode", 200);
                    value.put("errorMsg", "上传失败" + response.getCode() + ":" + response.getMessage());
                }
            } else {
                UploadImageRequest request = new UploadImageRequest(setting.getAliOssAccessId(), setting.getAliOssAccessKey(), "default");
                if (inputStream != null) {
                    request.setInputStream(inputStream);
                }
                //新版上传图片
                UploadImageImpl uploadImage = new UploadImageImpl();
                UploadImageResponse response = uploadImage.upload(request);
                if ("Success".equals(response.getCode())) {
                    value.put("data", response.getImageURL());
                    value.put("object_name", response.getImageId());
                } else {
                    value.put("success", false);
                    value.put("errorCode", 200);
                    value.put("errorMsg", "上传失败" + response.getCode() + ":" + response.getMessage());
                }
            }
            inputStream.close();

            //旧版上传图片
//            Map<String,Object>result=ossClientUtil.updateHead(file);
//            String head = result.get("imgUrl").toString();
//            value.put("data", head);
//            value.put("object_name",result.get("ALi_ObjectName").toString());
        } catch (Exception e) {
            e.printStackTrace();
            value.put("success", false);
            value.put("errorCode", 200);
            value.put("errorMsg", "上传失败");
        }
        return value;
    }

    /**
     * 获取请求参数
     *
     * @param @param  request
     * @param @return
     * @param @throws Exception
     * @return Map<String                                                                                                                               String>
     * @throws
     * @Title: getCallbackParams
     * @Description: TODO
     */
    @RequestMapping("getCallbackParams")
    @ResponseBody
    public String getCallbackParams(HttpServletRequest request)
            throws Exception {
        Enumeration<String> paramNames = request.getParameterNames();
        boolean return_HaveParam = paramNames.hasMoreElements();
        Map<String, Object> map = new HashMap<>();
        if (return_HaveParam) {
            while (paramNames.hasMoreElements()) {
                String paramName = (String) paramNames.nextElement();
                String paramValue = request.getParameter(paramName);
                map.put(paramName, paramValue);
            }
        } else {
            InputStream inStream = request.getInputStream();
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            outSteam.close();
            inStream.close();
            String result = new String(outSteam.toByteArray(), "utf-8");
            Object obj = new JSONTokener(result).nextValue();
            if (obj instanceof JSONObject) {
                map = JSONObject.fromObject(result);
//                System.err.println(JSONObject.fromObject(result));
//                System.err.println("是JSONObject对象");
            }/*else if(obj instanceof JSONArray){
                System.err.println("是JSONArray对象");
            }*/ else {
//                System.err.println("啥对象都不是");
            }
        }

        System.err.println(map);
        if (!Tool.isNull(map.get("Status")) && "success".equals(map.get("Status"))) {
            if (!Tool.isNull(map.get("StreamInfos")) && new JSONTokener(map.get("StreamInfos").toString()).nextValue() instanceof JSONArray) {
                JSONArray StreamInfos = JSONArray.fromObject(map.get("StreamInfos"));
                for (Object streamInfo : StreamInfos) {
//                    if(new JSONTokener(streamInfo).nextValue() instanceof JSONObject){
                    Map<String, Object> streamInfoMap = JSONObject.fromObject(streamInfo);
                    if (!Tool.isNull(streamInfoMap.get("Format")) && "mp4".equals(streamInfoMap.get("Format"))) {

                        EntityWrapper<Media> worksEntityWrapper = new EntityWrapper<>();
                        List<Media> pictureList = pictureService.selectList(worksEntityWrapper.eq("video_id", map.get("VideoId").toString()));
                        if (pictureList.size() > 0) {

                            dao.updateBySQL("update t_media a set oss_object_name='" + streamInfoMap.get("FileUrl") + "',a.`check`=0 where video_id='" + map.get("VideoId") + "'");
//                            dao.updateBySQL("update "+ FSS.activity +" set video='"+streamInfoMap.get("FileUrl")+"' where video_object_name='"+map.get("VideoId")+"'");
                            break;
                        } else {
                            //视频回调比视频上传快
                            Media media = new Media();
                            String videoBaseId = RandomUtil.getRandom(22);
                            media.setCreateTime(new DateTime());
                            media.setOssObjectName(streamInfoMap.get("FileUrl").toString());
                            media.setVideoId(map.get("VideoId").toString());
                            media.setBaseId(videoBaseId);
                            String loginName = (String) SecurityUtils.getSubject().getPrincipal();
                            media.setCreateBy(loginName);
                            media.setCheck(0);
                            pictureService.insert(media);
                        }
                     }
//                    }
                }
            }
        }
        return "success";
    }
}
