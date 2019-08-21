package com.stylefeng.guns.api;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.util.quartz.JobTypeConsts;
import com.stylefeng.guns.util.quartz.QuartzJobInfo;
import com.stylefeng.guns.util.quartz.QuartzService;
import com.stylefeng.guns.util.quartz.QuartzUtils;
import com.stylefeng.guns.core.cache.CacheKit;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.modular.Sms.service.ISendSmsService;
import com.stylefeng.guns.modular.account.service.ISsoAccountService;
import com.stylefeng.guns.modular.advantage.service.IAdvantageService;
import com.stylefeng.guns.modular.banner.service.IBannerService;
import com.stylefeng.guns.modular.intro.service.IPlatformIntroService;
import com.stylefeng.guns.modular.invited.service.IInvitedService;
import com.stylefeng.guns.modular.man.service.ISsoService;
import com.stylefeng.guns.modular.media.service.IMediaService;
import com.stylefeng.guns.modular.message.service.IMessageService;
import com.stylefeng.guns.modular.order.service.IOrderService;
import com.stylefeng.guns.modular.paybill.service.IPayBillService;
import com.stylefeng.guns.modular.quartzjob.service.IQuartzJobService;
import com.stylefeng.guns.modular.setting.service.ISettingService;
import com.stylefeng.guns.modular.ssoaccoun.service.ISsoAccountFlowService;
import com.stylefeng.guns.modular.startflow.service.IStartRecordService;
import com.stylefeng.guns.modular.startrmark.service.ICommentService;
import com.stylefeng.guns.modular.supersso.service.ISuperSsoService;
import com.stylefeng.guns.modular.system.dao.Dao;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.vip.service.IVipService;
import com.stylefeng.guns.modular.viptype.service.IVipTypeService;
import com.stylefeng.guns.modular.women.service.ISsoInfoService;
import com.stylefeng.guns.modular.works.service.ISsoWorkService;
import com.stylefeng.guns.util.*;
import com.stylefeng.guns.util.redis.RedisService;
import io.swagger.annotations.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jsoup.helper.DataUtil;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.UnaryOperator;
import java.util.logging.SimpleFormatter;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Arron Cotter on 2019/3/4.
 */

@Controller
@Api(tags = "前台统一接口")
@RequestMapping("ssoApi")
public class SsoApi {

    @Autowired
    private IMediaService mediaService;

    @Autowired
    private ISsoWorkService workService;

    @Autowired
    private ISsoService ssoService;

    @Autowired
    public SettingConfiguration settingConfiguration;

    @Autowired
    private IVipTypeService vipTypeService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IPayBillService payBillService;

    @Autowired
    private IVipService vipService;

    @Autowired
    private ISendSmsService sendSmsService;

    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Autowired
    private ISsoInfoService ssoInfoService;

    @Autowired
    private ISsoAccountService ssoAccountService;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private ISsoAccountFlowService ssoAccountFlowService;

    @Autowired
    private ISuperSsoService superSsoService;

    @Autowired
    private IBannerService bannerService;

    @Autowired
    private QuartzService quartzService;

    @Autowired
    private IQuartzJobService quartzJobService;

    @Autowired
    private SchedulerFactoryBean factoryBean;

    @Autowired
    private IStartRecordService startRecordService;

    @Autowired
    private IAdvantageService advantageService;

    @Autowired
    private IPlatformIntroService introService;

    @Autowired
    private ISettingService settingService;

    @Autowired
    private IInvitedService invitedService;

    @Autowired
    private ISsoAccountFlowService accountFlowService;

    @Autowired
    private Dao dao;

    //全局鉴权
    private Sso identify2(String token){
        Sso constant = CacheKit.get("CONSTANT", token);
        if (constant == null){
            //鉴权
            EntityWrapper<Sso> wrapper = new EntityWrapper<>();
            wrapper.eq("token", token);
            Sso sso = ssoService.selectOne(wrapper);
            if (sso == null){
                return null;
            }else {
                CacheKit.put("CONSTANT",token,sso);
                return  sso;
            }
        }else {
            return constant;
        }
    }

/*    //已测
    @ApiOperation(value = "根据前台上传视频回显url保存数据库", notes = " <img src='" + FSS.qcgs + "scsp.png'  />  根据前台上传视频回显url保存数据库")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "videoId", required = true, value = "前台上传视频回显的url"),
            @ApiImplicitParam(name = "imageUrl", required = true, value = "前台上传视频的封面图"),
            @ApiImplicitParam(name = "ssoId", required = true, value = "用户的ID"),
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
    })
    @ApiResponses(@ApiResponse(code = 200, message = ""))
    @RequestMapping(value = "saveVideoByObjectUrl", method = RequestMethod.POST)
    ResultMsg saveVideoByObjectUrl(String videoId, String imageUrl, String ssoId,String token) {
        Map<String, Object> arron = new HashMap<>();
        if (Tool.isNull(videoId)) return ResultMsg.fail("缺少参数", "videoId", null);
        if (Tool.isNull(imageUrl)) return ResultMsg.fail("缺少参数", "imageUrl", null);
        if (Tool.isNull(ssoId)) return ResultMsg.fail("缺少参数", "ssoId", null);
        if (Tool.isNull(token)) return ResultMsg.fail("缺少参数", "ssoId", null);
        Sso sso = identify2(token);
        if (sso ==null)return ResultMsg.success("token无效", null, null);
        Integer workId = 0;
        try {
            SsoWork ssoWork = new SsoWork();
            String imageBaseId = RandomUtil.getRandom(22);
            ssoWork.setThumb(imageUrl);//设置封面
            EntityWrapper<Media> pictureEntityWrapper = new EntityWrapper<>();
            pictureEntityWrapper.eq("video_id", videoId);
            List<Media> pictureList = mediaService.selectList(pictureEntityWrapper);
            if (pictureList.size() > 0) {
                ssoWork.setBaseId(pictureList.get(0).getBaseId());
            } else {
                ssoWork.setBaseId(imageBaseId);
                Media media = new Media();
                media.setBaseId(imageBaseId);
                media.setCreateTime(new DateTime());
                media.setType(1);
                media.setVideoId(videoId);
                boolean flag = mediaService.insert(media);
            }
            ssoWork.setType("2");
            ssoWork.setCheck("0");
            ssoWork.setCreateTime(new DateTime());
            ssoWork.setSsoId(ssoId);
            workService.insert(ssoWork);

            workId = ssoWork.getId();
            arron.put("workId", workId);
        } catch (Exception e) {
            return ResultMsg.fail("接口调用失败!", HttpStatus.BAD_GATEWAY.toString(), "上传视频保存到数据库失败!");
        }
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), arron);
    }*/

/*    @ApiOperation(value = "颜值认证上传封面图", notes = " <img src='" + FSS.qcgs + "scfm.png'  /> ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name="oldAvatorUrl",value = "修改前封面的url",required = false),
            @ApiImplicitParam(name="lat",value = "经度（用于个人详情页展示距离）",required = false),
            @ApiImplicitParam(name="lng",value = "纬度（用于个人详情页展示距离）",required = false),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n" +
            "      bigAvatarUrl:封面资源的路径url\n"  +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "bgImageUpload", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg bgImageUpload(HttpServletRequest request,String token,String oldAvatorUrl,String lat,String lng) {
        if (Tool.isNull(token))return ResultMsg.success("token为空", null, null);
        //鉴权
        Sso sso = identify2(token);
        if (sso ==null)return ResultMsg.success("token无效", null, null);

        Setting setting = settingConfiguration.getSetting();
        UploadImageRequest requestPhoto = new UploadImageRequest(setting.getAliOssAccessId(), setting.getAliOssAccessKey(), "default");
        Map<String, Object> arron = new HashMap<>();
        try {
            Map<String, MultipartFile> map = ((MultipartHttpServletRequest) request).getFileMap();
            MultipartFile multipartFile = null;
            for (Iterator<String> i = map.keySet().iterator(); i.hasNext(); ) {
                Object obj = i.next();
                multipartFile = (MultipartFile) map.get(obj);

                if (multipartFile != null) {
                    requestPhoto.setInputStream(multipartFile.getInputStream());
                }
                //上传图片
                UploadImageImpl uploadImage = new UploadImageImpl();
                UploadImageResponse responsePhoto = uploadImage.upload(requestPhoto);
                //将上传的图片路径写入数据库
                sso.setBigAvatar(responsePhoto.getImageURL());
                sso.setCheckBigAvatar("0");
                arron.put("bigAvatarUrl",responsePhoto.getImageURL());
            }
            ssoService.updateById(sso);

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    //更新位置
                    if (!Tool.isNull(lat) && !Tool.isNull(lng)){
                        sso.setLat(lat);
                        sso.setLng(lng);
                        CacheKit.remove("CONSTANT",token);
                        CacheKit.put("CONSTANT",token,sso);
                    }
                    if (!Tool.isNull(oldAvatorUrl)) {
                        //删除服务器上的图片
                        Tool.deleteAliVideoImgUrl(oldAvatorUrl, setting.getAliOssAccessId(), setting.getAliOssAccessKey());
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            return ResultMsg.fail("接口调用失败!", HttpStatus.BAD_REQUEST.toString(), e);
        }
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), "测试");
    }*/

    //已测
    @ApiOperation(value = "发布视频动态接口", notes = " <img src='" + FSS.qcgs + "scsp.png'  /> 发布成功后，跳转到当前用户的个人主页 ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "视频文件", required = false),
            @ApiImplicitParam(name = "imgUrl", required = true, value = "封面图url（调用统一单图片上传接口返回的url值）"),
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "content", value = "内容", required = true),
            @ApiImplicitParam(name="lat",value = "纬度（用于个人详情页展示距离）",required = false),
            @ApiImplicitParam(name="lng",value = "经度（用于个人详情页展示距离）",required = false)
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n" +
            "      workId : 作品ID\n"  +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "uploadVideoToLocal", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg uploadVideo(HttpServletRequest muiltRequest,String token,String imgUrl,String content,String lat,String lng) {
        if (Tool.isNull(token)) return ResultMsg.fail("缺少参数", "ssoId", null);
        if (Tool.isNull(imgUrl)) return ResultMsg.fail("缺少参数", "imgUrl", null);
        Map<String, Object> arron = new HashMap<>();
        Sso sso = identify2(token);
        if (sso ==null)return ResultMsg.success("token无效", null, null);
        Integer workId = 0;

        // 获取遍历文件名
        Iterator iter=((MultipartHttpServletRequest)muiltRequest).getFileNames();
        if (iter == null)return ResultMsg.success("文件为空！", HttpStatus.BAD_REQUEST.toString(), null);
        String url=""; //返回的图片路径
        while (iter.hasNext()) {
            MultipartFile file=((MultipartHttpServletRequest)muiltRequest).getFile(iter.next().toString());
            System.out.println("-->>>"+file);
            if(!file.isEmpty()||file!=null) {     //获取原始文件名
                try {
                    //获取跟目录
                    File path = null;

                    path = new File(ResourceUtils.getURL("classpath:").getPath());
                    if(!path.exists()) path = new File("");
                    //如果上传目录为static/api_img/，则可以如下获取：
                    File upload = new File(path.getAbsolutePath(),"/static/video");
                    if(!upload.exists()) upload.mkdirs();
                    String filePath =upload.getAbsolutePath();

                    //处理文件名
                    String filename = file.getOriginalFilename();
                    InputStream is = file.getInputStream(); // 获取输入流,MultipartFile中可以直接得到文件的流
                    //int pos = filename.lastIndexOf("."); // 取文件的格式
                    //唯一标识数字
                    UUID uuid = UUID.randomUUID();
                    String filenameurl = filePath + "/" + uuid +"_"+ filename;//.substring(pos);

                    //异步上传
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            // 获取输出流
                            OutputStream os = null;
                            try {
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

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                    String domain = Tool.getDomain();
                    if (domain.startsWith("localhost")||domain.startsWith("127.0.0.1")){
                        domain = "192.168.1.29:7777";
                    }
                    //返回资源路径
                    url = "http://"+domain+""+"/static/video/"+ uuid +"_"+ filename;//.substring(pos);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return ResultMsg.success("上传失败!", null, "");
                } catch (IOException e) {
                    e.printStackTrace();
                    return ResultMsg.success("上传失败!", null, "");
                }
            }
        }

        if (Tool.isNull(url))return ResultMsg.success("接口调用失败!", HttpStatus.BAD_REQUEST.toString(), "文件不能为空!");

        //关联
        String base_id = RandomUtil.getRandom(22);

        //创建媒体记录
        Media media = new Media();
        media.setType(1);
        media.setCheck(0);
        media.setCreateTime(new DateTime());
        media.setOssObjectName(url);
        media.setBaseId(base_id);
        mediaService.insert(media);

        //创建作品
        SsoWork ssoWork = new SsoWork();
        ssoWork.setThumb(imgUrl);//设置封面
        ssoWork.setType("2");
        ssoWork.setCheck("0");
        ssoWork.setCreateTime(new DateTime());
        ssoWork.setSsoId(sso.getSsoId()+"");
        ssoWork.setBaseId(base_id);
        ssoWork.setContent(content);
        workService.insert(ssoWork);

        if (!Tool.isNull(lat) && !Tool.isNull(lng)){
            sso.setLat(lat);
            sso.setLng(lng);
            ssoService.updateById(sso);
            CacheKit.remove("CONSTANT",token);
            CacheKit.put("CONSTANT",token,sso);
        }

        //返回动态ID给前端
        workId = ssoWork.getId();
        arron.put("workId", workId);

        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), arron);
    }

    @ApiOperation(value = "删除个人作品")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value = "登录后的令牌",required = true),
            @ApiImplicitParam(name="work_id",value = "要删除的作品",required = true),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{空,但是要注意看detail的返回内容}"))
    @RequestMapping(value="deleteWork",method = RequestMethod.POST)
    @ResponseBody
    ResultMsg deleteWork(String token,String work_id){
        try{
            if (Tool.isNull(token)) return  ResultMsg.fail("token为空!",null,null);
            if (Tool.isNull(work_id)) return  ResultMsg.fail("work_id为空!",null,null);
            Sso sso = identify2(token);
            if (sso == null) return ResultMsg.fail("非法token!",null,null);
            List<Map<String,Object>>works=dao.selectBySQL("select `work`.id,`work`.base_id,concat_ws(',',`work`.thumb,GROUP_CONCAT(media.oss_object_name)) files from t_sso_work `work` LEFT JOIN t_media media ON media.base_id=`work`.base_id where `work`.id="+work_id+" and `work`.sso_id='"+sso.getSsoId()+"'");
            if(Tool.listIsNull(works)||Tool.isNull(works.get(0).get("id")))return ResultMsg.fail("您要删除的作品不存在或者不属于您",null,null);
            dao.deleteBySQL("delete from t_media where base_id='"+works.get(0).get("base_id")+"'");
            dao.deleteBySQL("delete from t_sso_work where id="+works.get(0).get("id"));
            //获取跟目录
            File path = null;

            path = new File(ResourceUtils.getURL("classpath:").getPath());
            if(!path.exists()) path = new File("");
            //如果上传目录为static/api_img/，则可以如下获取：
            File videoFilePath = new File(path.getAbsolutePath(),"/static/video");
            if(!videoFilePath.exists()) videoFilePath.mkdirs();
            String videoPath =videoFilePath.getAbsolutePath();
            //如果上传目录为static/api_img/，则可以如下获取：
            File imgFilePath = new File(path.getAbsolutePath(),"/static/api_img");
            if(!imgFilePath.exists()) imgFilePath.mkdirs();
            String imgPath =imgFilePath.getAbsolutePath();

//            String filenameurl = filePath + "/" + uuid +"_"+ filename;//.substring(pos);
            new ArrayList<>(Arrays.asList(Tool.isNull(works.get(0).get("files"))?new String[]{}:works.get(0).get("files").toString().split(","))).parallelStream().forEach(fileUrl->{
                if(!Tool.isNull(fileUrl)){
                    if(fileUrl.indexOf("video")>-1){
                        File video=new File(videoPath+"/"+fileUrl.split("/")[fileUrl.split("/").length-1]);
                        if(video.exists())video.delete();
                    }
                    if(fileUrl.indexOf("api_img")>-1){
                        File img=new File(imgPath+"/"+fileUrl.split("/")[fileUrl.split("/").length-1]);
                        if(img.exists())img.delete();
                    }
                }
            });
            return ResultMsg.success("删除成功",null,null);
        }catch (Exception e){
            e.printStackTrace();
            return ResultMsg.fail("系统错误",null,null);
        }
    }


    //已测
/*    @ApiOperation(value = "根据前台传入的发布内容完善后台的作品接口", notes = " <img src='" + FSS.qcgs + "gxzp.png'  />")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workId", value = "作品ID", required = true),
            @ApiImplicitParam(name = "content", value = "内容", required = false),
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name="lat",value = "纬度（用于个人详情页展示距离）",required = false),
            @ApiImplicitParam(name="lng",value = "经度（用于个人详情页展示距离）",required = false)
    })
    @RequestMapping(value = "updateWorkById", method = RequestMethod.POST)
    ResultMsg updateWorkById(String workId, String token, String content,String lat,String lng) {
        if (Tool.isNull(token)) return ResultMsg.fail("缺少参数", "token", null);
        if (Tool.isNull(workId)) return ResultMsg.fail("缺少参数", "phone", null);
        Sso sso = identify2(token);
        if (sso ==null)return ResultMsg.success("token无效", null, null);
        //根据传入的作品id完善该作品
        Map<String, Object> tempMap = new HashMap<>();
        dao.updateBySQL("update t_sso_work set content="+"'"+content+"'"+" where id="+workId);
        tempMap.put("workId",workId);
        if (!Tool.isNull(lat) && !Tool.isNull(lng)){
            sso.setLat(lat);
            sso.setLng(lng);
            ssoService.updateById(sso);
            CacheKit.remove("CONSTANT",token);
            CacheKit.put("CONSTANT",token,sso);
        }
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), tempMap);
    }*/


    private ExecutorService executorService = Executors.newFixedThreadPool(50);
    @ApiOperation(value = "发布图文动态接口", notes = " <img src='" + FSS.qcgs + "scdt.png'  />")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "图片文件", required = false),
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "content", value = "内容", required = true),
            @ApiImplicitParam(name="lat",value = "纬度（用于个人详情页展示距离）",required = false),
            @ApiImplicitParam(name="lng",value = "经度（用于个人详情页展示距离）",required = false)
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n" +
            "      workId : 作品ID\n"  +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "worksImageUpload", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg upload(HttpServletRequest muiltRequest, String token,String content, String lat, String lng) {
        if (Tool.isNull(token)) return ResultMsg.success("token为空", null, null);
        //鉴权
        Sso sso = identify2(token);
        if (sso ==null)return ResultMsg.success("token无效", null, null);
        String baseId = RandomUtil.getRandom(22);
        Integer worksId = -1;
        Map<String, Object> arron = new HashMap<>();

        // 获取遍历文件名
        Iterator iter=((MultipartHttpServletRequest)muiltRequest).getFileNames();
        if (iter == null)return ResultMsg.success("文件为空！", HttpStatus.BAD_REQUEST.toString(), null);
        String url=""; //返回的图片路径
        String thumb = "";
        int i = 0;
        while (iter.hasNext()) {
            MultipartFile file=((MultipartHttpServletRequest)muiltRequest).getFile(iter.next().toString());
//            System.out.println("-->>>"+file);
            if(!file.isEmpty()||file!=null) {     //获取原始文件名
                try {
                    //获取跟目录
                    File path = null;
                    path = new File(ResourceUtils.getURL("classpath:").getPath());
                    if(!path.exists()) path = new File("");
                    //如果上传目录为static/api_img/，则可以如下获取：
                    File upload = new File(path.getAbsolutePath(),"/static/api_img");
                    if(!upload.exists()) upload.mkdirs();
                    String filePath =upload.getAbsolutePath();

                    //处理文件名
                    String filename = file.getOriginalFilename();
                    if (!filename.contains(".")){
                        filename = filename + ".jpg";
                    }
                    InputStream is = file.getInputStream(); // 获取输入流,MultipartFile中可以直接得到文件的流
                    int pos = filename.lastIndexOf("."); // 取文件的格式
                    //唯一标识数字
                    UUID uuid = UUID.randomUUID();
                    String filenameurl = filePath + "/" + uuid + filename.substring(pos);

                    //异步上传
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            // 获取输出流
                            OutputStream os = null;
                            try {
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

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    String domain = Tool.getDomain();
                    if (domain.startsWith("localhost")||domain.startsWith("127.0.0.1")){
                        domain = "192.168.1.29:7777";
                    }
                    //返回资源路径
                    url = "http://"+domain+""+"/static/api_img/"+ uuid + filename.substring(pos);
                    if (i==0)thumb = url;
                    //创建媒体记录
                    Media media = new Media();
                    media.setType(0);
                    media.setCheck(0);
                    media.setCreateTime(new DateTime());
                    media.setOssObjectName(url);
                    media.setBaseId(baseId);
                    mediaService.insert(media);
                    i++;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return ResultMsg.success("上传失败!", null, "");
                } catch (IOException e) {
                    e.printStackTrace();
                    return ResultMsg.success("上传失败!", null, "");
                }
            }
        }

        //构建作品对象写入数据库
        SsoWork ssoWork = new SsoWork();
        ssoWork.setCreateTime(new DateTime());
        ssoWork.setThumb(thumb);
        ssoWork.setBaseId(baseId);
        ssoWork.setType("1");
        ssoWork.setCheck("0");
        ssoWork.setSsoId(sso.getSsoId()+"");
        ssoWork.setContent(content);
        workService.insert(ssoWork);

        worksId = ssoWork.getId();
        arron.put("workId", worksId);

        if (!Tool.isNull(lat) && !Tool.isNull(lng)){
            sso.setLat(lat);
            sso.setLng(lng);
            ssoService.updateById(sso);
            CacheKit.remove("CONSTANT",token);
            CacheKit.put("CONSTANT",token,sso);
        }
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), arron);
    }

   /* //已测
    private ExecutorService executorService = Executors.newFixedThreadPool(50);
    @ApiOperation(value = "发布作品（单,多图片发布接口通用接口）", notes = " <img src='" + FSS.qcgs + "scdt.png'  /> 与发布视频一样，先调这个接口上传图片，我会返回作品的ID给你，再调另一个接口完善作品信息")
    @ApiImplicitParams({
                    @ApiImplicitParam(name = "token", value = "令牌", required = true),
                    @ApiImplicitParam(name = "files", value = "单个或多个图片（与星厨接口传参方式一样）", required = false),
                    @ApiImplicitParam(name="lat",value = "纬度（短的那个，用于个人详情页展示距离）",required = false),
                    @ApiImplicitParam(name="lng",value = "经度（长的那个，用于个人详情页展示距离）",required = false)
            })
    @RequestMapping(value = "worksImageUpload", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg upload(HttpServletRequest request, HttpServletResponse response,String token,String lat,String lng) {
        if (Tool.isNull(token)) return ResultMsg.success("token为空", null, null);
        //鉴权
        Sso sso = identify2(token);
        if (sso ==null)return ResultMsg.success("token无效", null, null);

        Setting setting = settingConfiguration.getSetting();
        UploadImageRequest requestPhoto = new UploadImageRequest(setting.getAliOssAccessId(), setting.getAliOssAccessKey(), "default");
        String baseId = RandomUtil.getRandom(22);
        Integer worksId = -1;
        Map<String, Object> arron = new HashMap<>();
        try {
            Map<String, MultipartFile> map = ((MultipartHttpServletRequest) request).getFileMap();
            MultipartFile multipartFile = null;
            for (Iterator<String> i = map.keySet().iterator(); i.hasNext(); ) {
                Object obj = i.next();
                multipartFile = (MultipartFile) map.get(obj);

                if (multipartFile != null) {
                    requestPhoto.setInputStream(multipartFile.getInputStream());
                }

                //上传图片
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        UploadImageImpl uploadImage = new UploadImageImpl();
                        UploadImageResponse responsePhoto = uploadImage.upload(requestPhoto);
                        //将上传的图片路径写入数据库
                        Media media = new Media();
                        media.setBaseId(baseId);
                        media.setOssObjectName(responsePhoto.getImageURL());
                        media.setImageId(responsePhoto.getImageId());
                        media.setCreateTime(new DateTime());
                        media.setType(0);
                        media.setCheck(0);
                        if (sso.getState().equals("封号")){
                            media.setCheck(2);
                        }
                        ShiroUser shiroUser = ShiroKit.getUser();
                        if (shiroUser != null) {
                            media.setCreateBy(shiroUser.getName());
                        }
                        mediaService.insert(media);
                    }
                });

            }
            //构建作品对象写入数据库
            EntityWrapper<Media> entityWrapper = new EntityWrapper<>();
            entityWrapper.eq("base_id", baseId);
            List<Media> list = mediaService.selectList(entityWrapper);

            SsoWork ssoWork = new SsoWork();
            ssoWork.setCreateTime(new DateTime());
//            if (list.size() > 0) {
//                ssoWork.setThumb(list.get(0).getOssObjectName());
//            } else {
//                //ssoWork.setThumb("http://java.xccb2018.com/image/cover/610F13C2B3BE416481015572B37898D8-6-2.png");//需要一张默认的图片
//            }
            ssoWork.setBaseId(baseId);
            ssoWork.setType("1");
            ssoWork.setCheck("0");
            ssoWork.setSsoId(sso.getSsoId()+"");
            workService.insert(ssoWork);

            worksId = ssoWork.getId();
            arron.put("workId", worksId);

            if (!Tool.isNull(lat) && !Tool.isNull(lng)){
                sso.setLat(lat);
                sso.setLng(lng);
                ssoService.updateById(sso);
                CacheKit.remove("CONSTANT",token);
                CacheKit.put("CONSTANT",token,sso);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResultMsg.fail("接口调用失败!", HttpStatus.BAD_REQUEST.toString(), e);
        }
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), arron);
    }*/


    //已测
    @ApiOperation(value = "点击立即充值（购买），进入订单页",notes = "<img src='"+FSS.qcgs+"dd.png' /></br>点击立即购买后，后台生成订单，计算金额，返回给前台会员类型名和需支付金额")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true,name="token",value = "用户token"),
            @ApiImplicitParam(required = true,name="vipTypeId",value = "会员类型ID（女性用户强制消费直接传 1  会员消费传对应的id）"),
    })
    @ApiResponses(@ApiResponse(code=200,message="data: {\n" +
            "&nbsp;"+"name: 类型名称(展示用),\n" +
            "&nbsp;"+"money: 需支付的金额(展示用) \n" +
            "&nbsp;"+"order_sn: 本系统生成订单号(支付请传这个) \n" +
            "}"))
    @RequestMapping(value = "vipSubmit",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg indentSubmit(String token,String vipTypeId){
        if(Tool.isNull(token))return ResultMsg.fail("缺少参数","token",null);
        if(Tool.isNull(vipTypeId))return ResultMsg.fail("缺少参数","vipTypeId",null);
        Sso sso = identify2(token);
        if (sso ==null)return ResultMsg.success("token无效", null, null);
        try{
            String indentNum = UuidUtil.get32UUID();
            VipType vipType = vipTypeService.selectById(vipTypeId);
            Order order = new Order();
            order.setState("0");
            order.setCreateTime(new DateTime());
            order.setOrderSn(indentNum);
            order.setSsoId(sso.getSsoId());
            order.setOrderFrom("buyVip:"+vipType.getId()+":"+ vipType.getTotalStart()+":"+vipType.getName());
            order.setPayMoney(vipType.getRealMoney());
            order.setPayTime(new DateTime());
            orderService.insert(order);

            Map<String,Object>result=new HashMap<>();
            result.put("name",vipType.getName());
            result.put("money",vipType.getRealMoney());
            result.put("order_sn",indentNum);
            return ResultMsg.success("生成订单成功，请支付",null,result);
        }catch (Exception e){
            e.printStackTrace();
            return ResultMsg.fail("系统错误",e.toString(),null);
        }
    }


    //已测    需求变化，弃用
/*    @ApiOperation(value = "砖石打赏接口",notes = "<img src='"+FSS.xgt+"indentSubmit.jpg' /></br>点击打赏后，传砖石数量和被打赏的用户ID,后台进行减扣")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true,name="token",value = "用户token"),
            @ApiImplicitParam(required = true,name="ssoId",value = "被打赏的用户ID"),
            @ApiImplicitParam(required = true,name="startCounnt",value = "砖石数量"),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "    }\n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "daShangSubmit",method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg daShangSubmit(String token,String ssoId,String startCounnt) {
        if (Tool.isNull(ssoId)) return ResultMsg.fail("缺少参数", "phone", null);
        if (Tool.isNull(token)) return ResultMsg.fail("缺少参数", "token", null);
        if (Tool.isNull(startCounnt)) return ResultMsg.fail("缺少参数", "money", null);
        Sso sso = identify2(token);
        if (sso == null) return ResultMsg.fail("非法token", token + "不存在", null);

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("someTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            Vip vip = vipService.selectOne(new EntityWrapper<Vip>().eq("sso_id", sso.getSsoId()));
            SsoAccount ssoAccount1 = ssoAccountService.selectOne(new EntityWrapper<SsoAccount>().eq("sso_id", sso.getSsoId()));
            if (vip == null) {
                return ResultMsg.success("接口调用成功!", "您不是会员", null);
            } else {
                Date now = new Date();
                if ((vip.getEndTime().getTime() < now.getTime())) {
                    vip.setStatus(2);
                    return ResultMsg.success("接口调用成功!", "您的会员已过期！", null);
                } else if ((ssoAccount1.getUseableBalance()+vip.getStart()) < Integer.parseInt(startCounnt)) {//会员砖石数量和账户余额总和
                    return ResultMsg.success("接口调用成功!", "您的砖石不足！", null);
                } else {
                    //会员减砖石
                    if (vip.getStart()>Integer.parseInt(startCounnt)){
                        vip.setStart(vip.getStart() - Integer.parseInt(startCounnt));
                        vipService.updateById(vip);
                    }else if (ssoAccount1.getUseableBalance()>Integer.parseInt(startCounnt)){
                       ssoAccount1.setUseableBalance(ssoAccount1.getUseableBalance() - Integer.parseInt(startCounnt));
                       ssoAccountService.updateById(ssoAccount1);
                    }else {
                        vip.setStart(Integer.parseInt(startCounnt) - vip.getStart());
                        ssoAccount1.setUseableBalance((double) (Integer.parseInt(startCounnt) - (Integer.parseInt(startCounnt) - vip.getStart())));
                        vipService.updateById(vip);
                        ssoAccountService.updateById(ssoAccount1);
                    }
                    //加砖石
                    SsoAccount ssoAccount = ssoAccountService.selectOne(new EntityWrapper<SsoAccount>().eq("sso_id", ssoId));
                    ssoAccount.setUseableBalance(Double.parseDouble(startCounnt)+ssoAccount.getUseableBalance());
                    ssoAccountService.updateById(ssoAccount);
                    //创建砖石消费流水
                    StartRecord startRecord = new StartRecord();
                    startRecord.setCreateTime(new DateTime());
                    startRecord.setGirlId(Integer.parseInt(ssoId));
                    startRecord.setSsoId(sso.getSsoId());
                    startRecord.setStartCount(Integer.parseInt(startCounnt));
                    startRecord.setType("0");
                    startRecordService.insert(startRecord);
                    //创建消息
                    Message message = new Message();
                    Sso girl = ssoService.selectOne(new EntityWrapper<Sso>().eq("sso_id", ssoId));
                    message.setContent("您打赏了"+girl.getNickName()+"用户"+startCounnt+"颗砖石！");
                    message.setCreateTime(new DateTime());
                    message.setLook("0");
                    message.setSsoId(0);
                    message.setMessageSsoId(sso.getSsoId()+"");
                    message.setType("1");
                    message.setOfficialMessageType("4");
                    messageService.insert(message);
                }
            }
            transactionManager.commit(status);
            return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), null);
        } catch (Exception e) {
            e.printStackTrace();
            transactionManager.rollback(status);
            return ResultMsg.success("接口调用失败!","服务器异常", null);
        }
    }*/

    //已测
    @ApiOperation(value = "选择支付方式,提交第三方支付",notes = "</br>根据要支付的订单号(order_sn),进行微信/支付宝统一下单,再根据选择的支付方式(pay_type:ali/wechat),返回APP(前端)所需的调起对应接口的字符串,微信是prepay_id(预支付交易会话标识),支付宝是orderString")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true,name="token",value = "用户token"),
            @ApiImplicitParam(name="pay_channel",value = "支付方式,只能根据支付类型选择:'wechat'(微信支付)/'alipay'(支付宝)"),
            @ApiImplicitParam(name="order_sn",value = "要支付订单号(不是支付宝或者微信的第三方订单号)")
    })
    @ApiResponses(@ApiResponse(code=200,message="data: {用于直接调起第三方支付的字符串}"))
    @RequestMapping(value = "vipPay",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg indentPay(String pay_channel,String order_sn,String token){
        Setting setting = settingConfiguration.getSetting();
        try{
            if(Tool.isNull(pay_channel))return ResultMsg.fail("缺少参数","pay_channel",null);
            if(Tool.isNull(order_sn))return ResultMsg.fail("缺少参数","order_sn",null);
            Sso sso = identify2(token);
            if (sso == null) return ResultMsg.fail("非法token", token + "不存在", null);
            if(pay_channel.trim().equals("wechat")||pay_channel.trim().equals("alipay")){
                Pattern p=Pattern.compile("^\\d+(\\.{0,1}\\d+){0,1}$");
                Order order = orderService.selectOne(new EntityWrapper<Order>().eq("order_sn", order_sn));
                if (order == null)return ResultMsg.fail("非法操作",order_sn+"号订单不存在",null);
                String body="黔城交友";
                //           boolean is_localhost=Tool.getDomain().startsWith("localhost")||Tool.getDomain().startsWith("192.168.1.21")||Tool.getDomain().startsWith("192.168.1.28");
                boolean is_localhost=true;
                //创建流水
                PayBill payBill = new PayBill();
                payBill.setCreateTime(new DateTime());
                payBill.setPayType(pay_channel);
                payBill.setOrderNo(order_sn);
                payBill.setSsoId(order.getSsoId());

                String result="";
                if(pay_channel.trim().equals("wechat")){
                    String nonce_str=UuidUtil.get32UUID();
//                    if(nonce_str!=null)return ResultMsg.fail("微信支付功能完成中,敬请期待","支付失败APP状态码-1,注意核对包签名",null);
                    SortedMap<String,Object> requestMap = new TreeMap<>();
                    requestMap.put("appid", setting.getWechatPayAppId());
                    if (order.getOrderFrom().equals("daShang")){
                        requestMap.put("attach", "daShang");
                    }else {
                        requestMap.put("attach", "qcgujy_vipbuy");
                    }
                    requestMap.put("mch_id",setting.getWechatPayMchId());
                    requestMap.put("nonce_str", nonce_str);
                    requestMap.put("body",body);
                    requestMap.put("out_trade_no",order_sn);
                    requestMap.put("total_fee",order.getPayMoney()*100);
                    //下面这句是当项目运行环境是本地时,所有支付金额为1分钱
                    if(is_localhost){requestMap.put("total_fee","1");}
                    requestMap.put("spbill_create_ip", "39.108.73.107"); //隨便設置一個
                    requestMap.put("notify_url", setting.getWechatPayNotifyUrl());
                    requestMap.put("trade_type","APP");
                    String orderInfo_toString = WXPayUtil.generateSignedXml(requestMap,setting.getWechatPayKey(),WXPayConstants.SignType.MD5);
                    Map<String,Object> orderResponse = WXPayUtil.httpOrder_Map(orderInfo_toString);// 调用统一下单接口,微信接口返回消息,单支付类型为NATIVE--扫码支付时,会带有二维码地址
//                    System.err.println("orderResponse\n"+orderResponse);
                    if(null!=orderResponse  && "SUCCESS".equals(orderResponse.get("return_code")) && "SUCCESS".equals(orderResponse.get("result_code"))){
                        SortedMap<String,Object> appMap = new TreeMap<String,Object>();
                        appMap.put("appid", setting.getWechatPayAppId());
                        appMap.put("partnerid",setting.getWechatPayMchId());
                        appMap.put("prepayid",orderResponse.get("prepay_id").toString());
                        appMap.put("noncestr",nonce_str);
                        appMap.put("timestamp",String.valueOf(System.currentTimeMillis()).substring(0,10));
                        appMap.put("package","Sign=WXPay");
                        appMap.put(WXPayConstants.FIELD_SIGN,WXPayUtil.generateSignature(appMap, setting.getWechatPayKey(), WXPayConstants.SignType.MD5));
                        // payNotifyService.insert(payNotify);
                        payBillService.insert(payBill);
//                        System.err.println("appMap\n"+appMap);
                        dao.updateBySQL("update t_order set pay_channel = '"+pay_channel+"' where order_sn ='"+order_sn+"'");
                        return ResultMsg.success("统一下单成功",null,appMap);
                    }else{
                        return ResultMsg.fail("微信支付统一下单失败",null,orderResponse);
                    }
                }
                if(pay_channel.trim().equals("alipay")){
                    result=Tool.aliPay(body,setting.getAliPayAppId(), setting.getAliPayAppPrivateKey(),setting.getAliPayAppPublicKey(),order_sn,/*当项目运行环境是本地时,所有支付金额为1分钱*/is_localhost?"0.01":order.getPayMoney().toString(),setting.getAliPayAppAliNotifyUrl(), order.getOrderFrom().equals("daShang")?"daShang":"qcgujy_vipbuy");
                    payBillService.insert(payBill);
                    return ResultMsg.success("成功下单",null,result);
                }
            }
            return ResultMsg.fail("支付方式选择错误","必须传'wechat'(微信支付)或者'ali'(支付宝支付)",null);
        }catch (NumberFormatException e){
            e.printStackTrace();
            return ResultMsg.fail("订单号:"+order_sn+"需付定金格式错误,请联系客服","本单需付定金数据库数据不为数字,有两种可能,1.直接操作数据库,2.通过接口传输修改数据(但后台接口做过了登录验证,也可能是修改了验证代码,将后台接口直接放行了)",null);
        }catch (Exception e){
            e.printStackTrace();
            return ResultMsg.fail("系统错误",e.toString(),null);
        }
    }
    /**
     * 支付宝支付回调地址
     * @return
     */
    @RequestMapping("alipayNotify")
    @ResponseBody
    public Object alipayNotify(HttpServletRequest request){
        Setting setting=settingConfiguration.getSetting();
        Map<String, String> params = AliSignUtils.convertRequestParamsToMap(request); // 将异步通知中收到的待验证所有参数都存放到map中
        try {
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
            //交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
            //支付的表名
            String goods_type = new String(request.getParameter("goods_type").getBytes("ISO-8859-1"),"UTF-8");
            // String goods_type= "qcgujy_vipbuy";
            // 调用SDK验证签名,签名验证是需要APP正式上线后才能成功的,这里先屏蔽掉
//            boolean signVerified = AlipaySignature.rsaCheckV1(params, setting.getAli_pay_app_public_key(), "UTF-8","RSA2");
            boolean signVerified = true;
//            boolean signVerified=true;
            PayBill payBill = payBillService.selectOne(new EntityWrapper<PayBill>().eq("order_no", out_trade_no));
            payBill.setPayNo(trade_no);
            payBill.setPayNotifyParams(JSONObject.fromObject(params).toString());

            if (signVerified) {
                // 按照支付结果异步通知中的描述，对支付结果中的业务内容进行1\2\3\4二次校验，校验成功后在response中返回success，校验失败返回failure
//                this.check(params);
                // 另起线程处理业务
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        System.err.println("------支付宝调请求结果------");
                        System.err.println(params);
//                        // 支付成功
                        if (trade_status.equals("TRADE_SUCCESS")|| trade_status.equals("TRADE_FINISHED")) {
                            // 处理支付成功逻辑
                            try {
                                //交易金额
                                BigDecimal earnest = new BigDecimal(new String(params.get("total_amount").getBytes("ISO-8859-1"),"UTF-8"));
                                Order order = orderService.selectOne(new EntityWrapper<Order>().eq("order_sn", out_trade_no));
                                switch (goods_type){
                                    case "qcgujy_vipbuy":
                                        if("0".equals(order.getState())) {//微信不停的回调,这里只在更新状态成功后执行逻辑
                                            //更新订单状态
                                            order.setState("1");
                                            order.setPayChannel("alipay");
                                            order.setFinishedTime(new Date());
                                            orderService.updateById(order);
                                            //更新流水状态
                                            payBill.setPayState("1");
                                            payBillService.updateById(payBill);
                                            //判断当前用户是否有vip账户
                                            Vip old_vip = vipService.selectOne(new EntityWrapper<Vip>().eq("sso_id", order.getSsoId()));
                                            String orderFrom = order.getOrderFrom();
                                            String[] split = orderFrom.split(":");
                                            if (old_vip == null){
                                                //创建Vip账户
                                                Vip vip = new Vip();
                                                vip.setSsoId(order.getSsoId());
                                                vip.setTypeId(Integer.parseInt(split[1]));
                                                vip.setStart(Integer.parseInt(split[2]));
                                                Calendar calendar = Calendar.getInstance();
                                                //上线后
                                                if ("1".equals(split[1])){
                                                    calendar.add(Calendar.DATE, 30);
                                                    vip.setValidDate(30);
                                                }else if("2".equals(split[1])){
                                                    calendar.add(Calendar.DATE, 90);
                                                    vip.setValidDate(90);
                                                }else {
                                                    calendar.add(Calendar.DATE, 180);
                                                    vip.setValidDate(180);
                                                }
                                                //测试用
/*                                                vip.setValidDate(30);
                                                Date now = new Date();
                                                Long time = 1 * 5 * 60 * 1000L;//五分钟
                                                //SimpleDateFormat sdf = new SimpleDateFormat("yyyy--MM--dd HH:mm:ss");
                                                Long end = now.getTime() + time;
                                                Date date = new Date(end);*/
                                                Date date = calendar.getTime();
                                                //DateTime date = DateTime.parse(calendar.getTime());
                                                vip.setEndTime(date);
                                                vip.setStatus(1);
                                                vipService.insert(vip);

                                                giveStart(order);
                                                giveMoney(order);

                                                //设置定时会员短信提醒
                                                setQuartzJob(vip.getValidDate(),order.getSsoId()+"");
                                            }else {
                                                if ("1".equals(old_vip.getStatus()) ){//有效 || "1".equals(old_vip.getStatus())
                                                    old_vip.setTypeId(4);//非标准方式
                                                    old_vip.setStart(Integer.parseInt(split[2])+old_vip.getStart());
                                                    Calendar calendar = Calendar.getInstance();
                                                    //上线后
                                                    if ("1".equals(split[1])){
                                                        calendar.add(Calendar.DATE, old_vip.getValidDate()+30);
                                                        old_vip.setValidDate(old_vip.getValidDate()+30);
                                                    }else if("2".equals(split[1])){
                                                        calendar.add(Calendar.DATE, old_vip.getValidDate()+90);
                                                        old_vip.setValidDate(old_vip.getValidDate()+90);
                                                    }else {
                                                        calendar.add(Calendar.DATE, old_vip.getValidDate()+180);
                                                        old_vip.setValidDate(old_vip.getValidDate()+180);
                                                    }
                                                    //测试用
/*                                                    old_vip.setValidDate(30+old_vip.getValidDate());
                                                    Date now = new Date();
                                                    Long time = 1 * 5 * 60 * 1000L;//五分钟
                                                    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy--MM--dd HH:mm:ss");
                                                    Long end = old_vip.getEndTime().getTime() + time;
                                                    Date date = new Date(end);*/
                                                    //Date date = calendar.getTime();
                                                    DateTime date = DateTime.parse(calendar.getTime());
                                                    old_vip.setEndTime(date);
                                                    vipService.updateById(old_vip);

                                                    giveStart(order);
                                                    giveMoney(order);

                                                    // 删除之前的定期提醒，设置新的定时VIP到期提醒
                                                    QuartzJobInfo preHandleInfo = new QuartzJobInfo();
                                                    HashMap<String,Object> hashMap = new HashMap<>();
                                                    Sso sso = ssoService.selectOne(new EntityWrapper<Sso>().eq("sso_id", order.getSsoId()));
                                                    hashMap.put("phone",sso.getPhone());
                                                    hashMap.put("ssoId",order.getSsoId());
                                                    //job执行需要的参数
                                                    preHandleInfo.setParams(hashMap);
                                                    //job类型 用于标识是哪个任务
                                                    preHandleInfo.setType(JobTypeConsts.VIP_END_JOB);
                                                    //job名称
                                                    preHandleInfo.setJobName(sso.getPhone());
                                                    //设置活动执行时间
                                                    Date now_ = new Date();
                                                    Long time_ = (old_vip.getValidDate()-7) * 24 * 60 * 60 * 1000L;//会员提前7天
                                                    Long remandTime = now_.getTime() + time_;

                                                    //删除数据库记录
                                                    quartzJobService.delete(new EntityWrapper<QuartzJob>().eq("job_name", sso.getPhone()));
                                                    //删除缓存
                                                    Scheduler sche = factoryBean.getScheduler();
                                                    QuartzUtils.removeJob(sche, sso.getPhone());

                                                    preHandleInfo.setFireDate(new Date(remandTime));
                                                    quartzService.addJob(preHandleInfo);
                                                }else {//无效
                                                    old_vip.setSsoId(order.getSsoId());
                                                    old_vip.setTypeId(Integer.parseInt(split[1]));
                                                    old_vip.setStart(Integer.parseInt(split[2]));
                                                    Calendar calendar = Calendar.getInstance();
                                                    //上线后
                                                    if ("1".equals(split[1])){
                                                        calendar.add(Calendar.DATE, 30);
                                                        old_vip.setValidDate(30);
                                                    }else if("2".equals(split[1])){
                                                        calendar.add(Calendar.DATE, 90);
                                                        old_vip.setValidDate(90);
                                                    }else {
                                                        calendar.add(Calendar.DATE, 180);
                                                        old_vip.setValidDate(180);
                                                    }
                                                    //测试用
/*                                                    old_vip.setValidDate(30);
                                                    Date now = new Date();
                                                    Long time = 1 * 5 * 60 * 1000L;//五分钟
                                                    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy--MM--dd HH:mm:ss");
                                                    Long end = now.getTime() + time;
                                                    Date date = new Date(end);*/
                                                    //Date date = calendar.getTime();
                                                    DateTime date = DateTime.parse(calendar.getTime());
                                                    old_vip.setEndTime(date);
                                                    old_vip.setStatus(1);
                                                    vipService.updateById(old_vip);

                                                    giveStart(order);
                                                    giveMoney(order);

                                                    //设置定时会员短信提醒
                                                    setQuartzJob(old_vip.getValidDate(),order.getSsoId()+"");
                                                }
                                            }
                                            //短信通知
                                            Sso sso = ssoService.selectOne(new EntityWrapper<Sso>().eq("sso_id", order.getSsoId()));
                                            String responseStr = "0".equals(sso.getSex())?new DuanXin_LiJun( setting.getYpAppkey(), "2925788").sendAllSms(sso.getPhone(),split[3],split[2])
                                                    :"1".equals(sso.getSex())?new DuanXin_LiJun( setting.getYpAppkey(), "3156526").sendAllSms(sso.getPhone(),split[3]):"";
                                            SendSms sendSms = new SendSms();
                                            sendSms.setCreateTime(new DateTime());
                                            sendSms.setPhone(sso.getPhone());
                                            sendSms.setType("1");
                                            sendSmsService.insert(sendSms);
                                            //创建消息
                                            Message message = new Message();
                                            message.setContent("尊敬的黔城用户，恭喜您购买我们的"+split[3]+"会员成功，您的"+split[2]+"颗砖石已到账，请在会员期限内使用，过期无效！");
                                            message.setMessageSsoId(order.getSsoId().toString());
                                            message.setSsoId(0);
                                            message.setType("2");
                                            message.setLook("0");
                                            message.setCreateTime(new DateTime());
                                            messageService.insert(message);
                                        }
                                        break;
                                    /*case "daShang" : //打赏业务流程
                                        String[] split = order.getOrderFrom().split(":");
                                        SsoAccountFlow ssoAccountFlow = ssoAccountFlowService.selectById(Integer.parseInt(split[1]));
                                        ssoAccountFlow.setNote("1");
                                        ssoAccountFlowService.updateById(ssoAccountFlow);
                                        //更新订单状态
                                        order.setState("1");
                                        order.setFinishedTime(new DateTime());
                                        orderService.updateById(order);
                                        //更新流水状态
                                        payBill.setPayState("1");
                                        payBillService.updateById(payBill);
                                        break;*/
                                    default:
                                        break;
                                }
                            } catch (Exception e) {
                                System.err.println("支付宝回调业务处理报错,params:" + params);
                                e.printStackTrace();
                            }
                        } else {
                            System.err.println("没有处理支付宝回调业务，支付宝交易状态：{},params:{}");
                            System.err.println(trade_status);
                            System.err.println(params);
                        }
                    }
                });
                // 如果签名验证正确，立即返回success，后续业务另起线程单独处理
                // 业务处理失败，可查看日志进行补偿，跟支付宝已经没多大关系。
                return "success";
            } else {
                System.err.println("支付宝回调签名认证失败，signVerified=false, paramsJson:{}"+params);
                return "failure";
            }
        } /*catch (AlipayApiException e) {
            System.err.println("支付宝回调签名认证失败,paramsJson:{},errorMsg:{}"+params);
            e.printStackTrace();
            return "failure";
        }*/catch ( UnsupportedEncodingException e){
            System.err.println("支付宝回调内容乱码:"+params);
            e.printStackTrace();
            return "failure";
        }catch ( Exception e){
            e.printStackTrace();
            return "failure";
        }
    }

    /**
     * 微信支付回调地址
     * @param request
     * @return
     */
    @RequestMapping(value="wechatpayNotify")
    @ResponseBody
    public Object wechatpayNotify(HttpServletRequest request){
        Setting setting=settingConfiguration.getSetting();
//        System.err.println("-----进入微信支付回调-----");
        try{
            Enumeration<String> requestParameters=request.getParameterNames();
            boolean return_HaveParam=requestParameters.hasMoreElements();
            Map<String, Object> map = new HashMap<>();
            if(return_HaveParam){
                while (requestParameters.hasMoreElements()) {
                    String string = requestParameters.nextElement();
                    map.put(string,request.getParameter(string));
                }
            }else{
                map=WXPayUtil.getCallbackParams(request);
            }
            PayBill payBill_ = new PayBill();
            //PayNotify payNotify_=new PayNotify();
            String goods_type_="";
            if ("SUCCESS".equals(map.get("return_code"))|| "SUCCESS".equals(map.get("result_code"))) {
                goods_type_=map.get("attach").toString();
                payBill_=payBillService.selectOne(new EntityWrapper<PayBill>().eq("order_no",map.get("out_trade_no")));
                payBill_.setPayNo(map.get("transaction_id").toString());
                payBill_.setPayNotifyParams(JSONObject.fromObject(map).toString());
            }
            Map<String,Object>map_=map;
            PayBill payBill = payBill_;
            String goods_type=goods_type_;
            System.err.println(map_);
            executorService.execute(new Runnable() {
                @Override
                public void run() {
//                    System.err.println("------微信回调请求结果------");
//                    System.err.println(map_);
//                        // 支付成功
                    if ("SUCCESS".equals(map_.get("return_code"))|| "SUCCESS".equals(map_.get("result_code"))) {
                        // 处理支付成功逻辑
                        try {
                            BigDecimal earnest = new BigDecimal(map_.get("total_fee").toString()).divide(new BigDecimal("100")).setScale(2,BigDecimal.ROUND_HALF_UP);
                            Order order = orderService.selectOne(new EntityWrapper<Order>().eq("order_sn",map_.get("out_trade_no")));
                            switch (goods_type){
                                case "qcgujy_vipbuy":
                                    if("0".equals(order.getState())) {//微信不停的回调,这里只在更新状态成功后执行逻辑
                                        //更新订单状态
                                        order.setState("1");
                                        order.setFinishedTime(new DateTime());
                                        order.setPayChannel("wechat");
                                        orderService.updateById(order);
                                        //更新流水状态
                                        payBill.setPayState("1");
                                        payBillService.updateById(payBill);
                                        //判断当前用户是否有vip账户
                                        Vip old_vip = vipService.selectOne(new EntityWrapper<Vip>().eq("sso_id", order.getSsoId()));
                                        String orderFrom = order.getOrderFrom();
                                        String[] split = orderFrom.split(":");
                                        if (old_vip == null){
                                            //创建Vip账户
                                            Vip vip = new Vip();
                                            vip.setSsoId(order.getSsoId());
                                            vip.setTypeId(Integer.parseInt(split[1]));
                                            vip.setStart(Integer.parseInt(split[2]));
                                            Calendar calendar = Calendar.getInstance();
                                            //上线后
                                            if ("1".equals(split[1])||"5".equals(split[1])){
                                                calendar.add(Calendar.DATE, 30);
                                                vip.setValidDate(30);
                                            }else if("2".equals(split[1])||"6".equals(split[1])){
                                                calendar.add(Calendar.DATE, 90);
                                                vip.setValidDate(90);
                                            }else if("3".equals(split[1])||"7".equals(split[1])){
                                                calendar.add(Calendar.DATE, 180);
                                                vip.setValidDate(90);
                                            }else {
                                                calendar.add(Calendar.DATE, 0);
                                                vip.setValidDate(0);
                                            }
                                            //测试用
/*                                          vip.setValidDate(30);
                                            Date now = new Date();
                                            Long time = 1 * 5 * 60 * 1000L;//五分钟
                                            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy--MM--dd HH:mm:ss");
                                            Long end = now.getTime() + time;
                                            Date date = new Date(end);*/
                                            //Date date = calendar.getTime();
                                            DateTime date = DateTime.parse(calendar.getTime());
                                            vip.setEndTime(date);
                                            vip.setStatus(1);
                                            vip.setCreateTime(new DateTime());
                                            vipService.insert(vip);

                                            giveStart(order);
                                            giveMoney(order);
                                            //设置定时会员短信提醒
                                            setQuartzJob(vip.getValidDate(),order.getSsoId()+"");
                                        }else {
                                            if (old_vip.getStatus()==1){
                                                //有效
                                                old_vip.setTypeId(4);//非标准方式
                                                old_vip.setStart(Integer.parseInt(split[2])+old_vip.getStart());
                                                Calendar calendar = Calendar.getInstance();
                                                //上线后
                                                if ("1".equals(split[1])||"5".equals(split[1])){
                                                    calendar.add(Calendar.DATE, old_vip.getValidDate()+30);
                                                    old_vip.setValidDate(old_vip.getValidDate()+30);
                                                }else if("2".equals(split[1])||"6".equals(split[1])){
                                                    calendar.add(Calendar.DATE, old_vip.getValidDate()+90);
                                                    old_vip.setValidDate(old_vip.getValidDate()+90);
                                                }else if("3".equals(split[1])||"7".equals(split[1])){
                                                    calendar.add(Calendar.DATE, old_vip.getValidDate()+180);
                                                    old_vip.setValidDate(old_vip.getValidDate()+180);
                                                }else {
                                                    calendar.add(Calendar.DATE, old_vip.getValidDate()+0);
                                                    old_vip.setValidDate(old_vip.getValidDate()+0);
                                                }
                                                //测试用
/*                                                old_vip.setValidDate(30+old_vip.getValidDate());
                                                Date now = new Date();
                                                Long time = 1 * 5 * 60 * 1000L;//五分钟
                                                //SimpleDateFormat sdf = new SimpleDateFormat("yyyy--MM--dd HH:mm:ss");
                                                Long end = old_vip.getEndTime().getTime() + time;
                                                Date date = new Date(end);
                                                */
                                                // Date date = calendar.getTime();
                                                DateTime date = DateTime.parse(calendar.getTime());
                                                old_vip.setEndTime(date);
                                                vipService.updateById(old_vip);

                                                giveStart(order);
                                                giveMoney(order);

                                                // 删除之前的定期提醒，设置新的定时VIP到期提醒
                                                QuartzJobInfo preHandleInfo = new QuartzJobInfo();
                                                HashMap<String,Object> hashMap = new HashMap<>();
                                                Sso sso = ssoService.selectOne(new EntityWrapper<Sso>().eq("sso_id", order.getSsoId()));
                                                hashMap.put("phone",sso.getPhone());
                                                hashMap.put("ssoId",order.getSsoId());
                                                //job执行需要的参数
                                                preHandleInfo.setParams(hashMap);
                                                //job类型 用于标识是哪个任务
                                                preHandleInfo.setType(JobTypeConsts.VIP_END_JOB);
                                                //job名称
                                                preHandleInfo.setJobName(sso.getPhone());
                                                //设置活动执行时间
                                                Date now_ = new Date();
                                                Long time_ = (old_vip.getValidDate()-7) * 24 * 60 * 60 * 1000L;//会员提前7天
                                                Long remandTime = now_.getTime() + time_;

                                                //删除数据库记录
                                                quartzJobService.delete(new EntityWrapper<QuartzJob>().eq("job_name", sso.getPhone()));
                                                //删除缓存
                                                Scheduler sche = factoryBean.getScheduler();
                                                QuartzUtils.removeJob(sche, sso.getPhone());

                                                preHandleInfo.setFireDate(new Date(remandTime));

                                                quartzService.addJob(preHandleInfo);

                                            }else {
                                                //无效
                                                Calendar calendar = Calendar.getInstance();
                                                int v = 0;
                                                int start = 0;
                                                //上线后
                                                if ("1".equals(split[1])){
                                                    calendar.add(Calendar.DATE, 30);
                                                    v=30;
                                                    start = 200;
                                                }else if("2".equals(split[1])){
                                                    calendar.add(Calendar.DATE, 90);
                                                    v=90;
                                                    start = 600;
                                                }else {
                                                    calendar.add(Calendar.DATE, 360);
                                                    v=360;
                                                    start=1200;
                                                }
                                                //测试用
                                               /* old_vip.setValidDate(30);
                                                Date now = new Date();
                                                Long time = 1 * 5 * 60 * 1000L;//五分钟
                                                //SimpleDateFormat sdf = new SimpleDateFormat("yyyy--MM--dd HH:mm:ss");
                                                Long end = now.getTime() + time;
                                                Date date = new Date(end);*/
                                                //Date date = calendar.getTime();
                                                DateTime date = DateTime.parse(calendar.getTime());
                                                dao.updateBySQL("UPDATE t_vip SET sso_id="+old_vip.getSsoId()+", " +
                                                        "type_id="+split[2]+", valid_date="+v+", `start`="+start+", " +
                                                        "`status`="+1+", create_time='"+new DateTime()+"', end_time='"+date+"' WHERE id="+old_vip.getId());

                                                giveStart(order);
                                                giveMoney(order);

                                                //设置定时会员短信提醒
                                                setQuartzJob(old_vip.getValidDate(),order.getSsoId()+"");
                                            }
                                        }

                                        //短信通知
                                        Sso sso = ssoService.selectOne(new EntityWrapper<Sso>().eq("sso_id", order.getSsoId()));
                                        String responseStr = "0".equals(sso.getSex())?new DuanXin_LiJun( setting.getYpAppkey(), "2925788").sendAllSms(sso.getPhone(),split[3],split[2])
                                                :"1".equals(sso.getSex())?new DuanXin_LiJun( setting.getYpAppkey(), "3156526").sendAllSms(sso.getPhone(),split[3]):"";
                                        SendSms sendSms = new SendSms();
                                        sendSms.setCreateTime(new DateTime());
                                        sendSms.setPhone(sso.getPhone());
                                        sendSms.setType("1");
                                        sendSmsService.insert(sendSms);
                                        //创建消息
                                        Message message = new Message();
//                                        if("0".equals(sso.getSex())){
//                                            message.setContent("尊敬的黔城用户，恭喜您购买我们的"+split[3]+"会员成功，您的"+split[2]+"颗砖石已到账，请在会员期限内使用，过期无效！");
//                                        }else {
//                                            message.setContent("尊敬的黔城用户，恭喜您购买我们的"+split[3]+"会员成功!");
//                                        }
                                        message.setContent("尊敬的黔城用户，恭喜您购买我们的"+split[3]+"会员成功!");
                                        message.setMessageSsoId(order.getSsoId().toString());
                                        message.setSsoId(0);
                                        message.setType("2");
                                        message.setLook("0");
                                        message.setCreateTime(new DateTime());
                                        messageService.insert(message);
                                    }
                                    break;
/*                                case "daShang" : //打赏业务流程
                                        String[] split = order.getOrderFrom().split(":");
                                        SsoAccountFlow ssoAccountFlow = ssoAccountFlowService.selectById(Integer.parseInt(split[1]));
                                        ssoAccountFlow.setNote("1");
                                        ssoAccountFlowService.updateById(ssoAccountFlow);
                                        //更新订单状态
                                        order.setState("1");
                                        order.setFinishedTime(new DateTime());
                                        orderService.updateById(order);
                                        //更新流水状态
                                        payBill.setPayState("1");
                                        payBillService.updateById(payBill);
                                    break;*/
                                default:
                                    break;
                            }
                        } catch (Exception e) {
                            System.err.println("微信回调业务处理报错,params:" + map_);
                            e.printStackTrace();
                        }
                    } else {
                        System.err.println("没有处理微信回调业务，支付宝交易状态：{},params:{}");
                        System.err.println(map_);
                    }
                }
            });
            return "<xml>" +
                    "<return_code><![CDATA[SUCCESS]]></return_code>" +
                    "<return_msg><![CDATA[OK]]></return_msg>" +
                    "</xml>";
        }catch (Exception e){
            e.printStackTrace();
            return "<xml>" +
                    "<return_code><![CDATA[SUCCESS]]></return_code>" +
                    "<return_msg><![CDATA[OK]]></return_msg>" +
                    "</xml>";
        }
    }

    @Async
    public void setQuartzJob(int validDate , String ssoId ){
        // 设置定时VIP到期提醒
        QuartzJobInfo preHandleInfo = new QuartzJobInfo();
        HashMap<String,Object> hashMap = new HashMap<>();
        Sso sso = ssoService.selectOne(new EntityWrapper<Sso>().eq("sso_id", ssoId));
        hashMap.put("phone",sso.getPhone());
        hashMap.put("ssoId",ssoId);
        //job执行需要的参数
        preHandleInfo.setParams(hashMap);
        //job类型 用于标识是哪个任务
        preHandleInfo.setType(JobTypeConsts.VIP_END_JOB);
        //job名称
        preHandleInfo.setJobName(sso.getPhone());
        //设置活动执行时间
        Date now_ = new Date();
        Long time_ = (validDate-7) * 24 * 60 * 60 * 1000L;//会员提前7天
        Long remandTime = now_.getTime() + time_;
        preHandleInfo.setFireDate(new Date(remandTime));
        quartzService.addJob(preHandleInfo);
    }

    @Async
    public void giveStart(Order order) {
        //男用户系统自动送n颗砖石
        List<Map<String, Object>> maps = dao.selectBySQL("SELECT\n" +
                "                b.id as vipId,\n" +
                "                a.sso_id AS ssoId,\n" +
                "                IF(NOW()<b.end_time,'1','0') as `status`\n" +
                "                FROM\n" +
                "                t_invited a\n" +
                "                LEFT JOIN t_vip b ON a.sso_id = b.sso_id\n" +
                "                LEFT JOIN t_sso c ON c.sso_id = a.sso_id \n" +
                "                WHERE\n" +
                "                c.sex = '0' \n" +
                "                AND a.status = '0' \n" +
                "                AND a.be_sso_id = " + order.getSsoId());
        if (!Tool.listIsNull(maps)) {
            String ssoId = maps.get(0).get("ssoId").toString();//邀请表的邀请人的ID
            Setting setting = settingService.selectById(1);
            Integer  money = setting.getMoneyGiveMan();//系统设置里邀请获得的钻石数量
            if (maps.get(0).containsKey("vipId")){
                String vipId = maps.get(0).get("vipId").toString();//邀请人在vip表的sso_id
                String status = maps.get(0).get("status").toString();
                if (Tool.isNull(vipId)) {
                    //创建Vip账户
                    Vip vip1 = new Vip();
                    vip1.setSsoId(Integer.parseInt(ssoId));
                    vip1.setTypeId(1);
                    vip1.setStart(money);
                    vip1.setValidDate(0);
                    vip1.setEndTime(new Date());
                    vip1.setCreateTime(new Date());
                    vip1.setStatus(2);
                    vipService.insert(vip1);
//                    Calendar calendar1 = Calendar.getInstance();
//                    calendar1.add(Calendar.DATE, 30);
//                    vip1.setValidDate(30);
//                    Date date1 = calendar1.getTime();
                    //设置定时会员短信提醒
//                    setQuartzJob(vip1.getValidDate(), ssoId);
                } else {
//                    if ("1".equals(status)) {
                        dao.updateBySQL("update t_vip set start = if(start is not null,start + "+money+","+money+") where id = " + vipId);
//                    } else {
//                        Calendar calendar1 = Calendar.getInstance();
//                        calendar1.add(Calendar.DATE, 30);
//                        DateTime date1 = DateTime.parse(calendar1.getTime());
//                        dao.updateBySQL("update t_vip set start = "+money+",valid_date = 30,end_time = " + date1 + " where id = " + vipId);
                        //设置定时会员短信提醒
//                        setQuartzJob(30, ssoId);
//                    }
                }
            }else {
                //创建Vip账户
                Vip vip1 = new Vip();
                vip1.setSsoId(Integer.parseInt(ssoId));
                vip1.setTypeId(1);
                vip1.setStart(money);
//                Calendar calendar1 = Calendar.getInstance();
//                calendar1.add(Calendar.DATE, 30);
//                vip1.setValidDate(30);
                vip1.setValidDate(0);
//                Date date1 = calendar1.getTime();
//                vip1.setEndTime(date1);
                vip1.setEndTime(new DateTime());
                vip1.setCreateTime(new DateTime());
                vip1.setStatus(2);
                vipService.insert(vip1);
                //设置定时会员短信提醒
//                setQuartzJob(vip1.getValidDate(), ssoId);
            }

            //创建消息
            Message message = new Message();
            //message.setContent("您邀请ID为" + order.getSsoId() + "的用户成功！我们送您"+money+"颗砖石，请查收！");
            message.setContent("您邀请ID为" + order.getSsoId() + "的用户成功");
            message.setCreateTime(new DateTime());
            message.setLook("0");
            message.setSsoId(0);
            message.setMessageSsoId(ssoId);
            message.setType("2");
            message.setOfficialMessageType("4");
            messageService.insert(message);
            //创建砖石消费流水
            StartRecord startRecord = new StartRecord();
            startRecord.setCreateTime(new DateTime());
            startRecord.setGirlId(Integer.parseInt(ssoId));
            startRecord.setSsoId(order.getSsoId());
            startRecord.setStartCount(money);
            startRecord.setType("2");
            startRecordService.insert(startRecord);

            //更新邀请状态
            dao.updateBySQL("update t_invited set status = '1' where be_sso_id = " + order.getSsoId());
        }
    }

    /**
     * 返现给女用户  数据库的金额是女的邀请女的返现金额
     * @param order
     */
    private void giveMoney(Order order){
        List<Map<String, Object>> maps = dao.selectBySQL("SELECT\n" +
                "                a.sso_id AS ssoId\n" +
                "                FROM\n" +
                "                t_invited a\n" +
                "                LEFT JOIN t_sso c ON c.sso_id = a.sso_id \n" +
                "                WHERE\n" +
                "                c.sex = '1' \n" +
                "                AND a.be_sso_id = " + order.getSsoId());
        if (Tool.listIsNull(maps))return;
        Invited invited = invitedService.selectOne(new EntityWrapper<Invited>().eq("sso_id", maps.get(0).get("ssoId")).eq("be_sso_id", order.getSsoId()));
        if (!Tool.listIsNull(maps) && invited.getStatus().equals("0")){
            //返现
            Setting setting = settingService.selectById(1);
            Integer  money = setting.getMoneyGiveWomen();
            Sso sso = ssoService.selectOne(new EntityWrapper<Sso>().eq("sso_id", order.getSsoId()));
            if ("0".equals(sso.getSex())){
                money = 66;
            }
            dao.updateBySQL("UPDATE t_sso_account a  SET a.useable_balance = a.useable_balance + "+money+"  WHERE a.sso_id ="+maps.get(0).get("ssoId"));
            //创建账户流水
            SsoAccountFlow ssoAccountFlow = new SsoAccountFlow();
            ssoAccountFlow.setMoney((double)money);
            ssoAccountFlow.setNote("邀请返现");
            ssoAccountFlow.setBusinessType("0");
            ssoAccountFlow.setBusinessName("返现");
            ssoAccountFlow.setComeFrom(order.getSsoId().toString());
            ssoAccountFlow.setSsoId(Integer.parseInt(maps.get(0).get("ssoId").toString()));
            ssoAccountFlow.setCreateTime(new DateTime());
            accountFlowService.insert(ssoAccountFlow);
            //创建消息
            Message message = new Message();
            message.setContent("您邀请ID为"+invited.getBeSsoId()+"的用户成功！返现￥"+money+"，请查收！");
            message.setCreateTime(new DateTime());
            message.setLook("0");
            message.setSsoId(0);
            message.setMessageSsoId(invited.getSsoId());
            message.setType("2");
            message.setOfficialMessageType("4");
            messageService.insert(message);
            invited.setStatus("1");
            invitedService.updateById(invited);
        }
    }


    //已测
    @ApiOperation(value = "获取手机验证码",notes = "<img src='"+FSS.qcgs+"dl.png'  />根据手机号生成验证码以短信形式发送,返回的内容里会有成功与否的消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="phone",value = "手机号",required = true),
            @ApiImplicitParam(name="invite",value = "邀请(非必传,分享出去的注册页面有这个字段才添加的)"),
            @ApiImplicitParam(name="phone_md5",value = "手机号验证字符串"),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{空,但是要注意看detail的返回内容}"))
    @RequestMapping(value="getVerificationCode",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg getVerificationCode(String phone,String phone_md5,String invite,HttpServletRequest request,HttpSession session){
        //17886262927
        if(Tool.isNull(phone))return ResultMsg.fail("请输入手机号",null,null);
//        if("17886262927".equals(phone))phone_md5=FSS.MD5_yan;

        if (Tool.isNull(session.getAttribute("sms_count"))) {
            if(Tool.isNull(phone_md5)||(!FSS.MD5_yan.equals(phone_md5)&&!FSS.MD5_yan_IOS.equals(phone_md5))){
                System.err.println("-------------------------------");
                System.err.println("The captcha interface is attacked");
                System.err.println(phone);
                System.err.println("-------------------------------");
                return ResultMsg.fail("版本过低,请更新",null,null);
            }
        }else{
            int sms_count=Integer.valueOf(session.getAttribute("sms_count").toString());
            if(sms_count>1)session.setAttribute("sms_count",sms_count-1);
            else return ResultMsg.fail("一小时内只能发送三次验证码信息,请关闭该页面,一小时后再来",null,null);
        }
        String randomCode= Tool.getRandomNum(6);
        try {
            if(!Tool.isNull(invite)&&ssoService.selectCount(new EntityWrapper<Sso>().eq("phone",phone))>0)return ResultMsg.fail("该手机已注册 您已是黔城故事用户","这个提示是针对分享出去的H5注册页面使用的,如果APP出现了这个提示,要么就是有懂行的人在测试我们的接口,要么就是APP传了这个参数",true);
            Setting setting=settingConfiguration.getSetting();
            String str=new DuanXin_LiJun(setting.getYpAppkey(),"2925756").codeSms(randomCode,phone);
            Map<String,Object>result= JSONObject.fromObject(str);
            if(!Tool.isNull(result.get("code"))){
                if(result.get("code").equals(0)){
                    CacheKit.put("CONSTANT",phone,randomCode);
                    System.out.println(phone+"验证码:\n"+randomCode);

                    SendSms sendSms = new SendSms();
                    sendSms.setType("0");
                    sendSms.setCreateTime(new DateTime());
                    sendSms.setPhone(phone);
                    sendSmsService.insert(sendSms);

                    return ResultMsg.success("发送成功!",result.toString(),null);
                }else{
                    return ResultMsg.fail("发送失败:"+result.get("msg"),""+result.get("detail"),null);
                }
            }else{
                return ResultMsg.fail("短信发送平台出现未知错误",result.toString(),null);
            }
//                                return ResultMsg.success("发送成功!",null,null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMsg.fail("系统错误",e.toString(),null);
        }
    }


    //已测
    @ApiOperation(value = "用户登录",notes = "<img src='"+FSS.qcgs+"dl.png' />根据用户手机号和验证码登录并且记录用户信息,然后将更新后的用户信息返回")
    @ApiImplicitParams({
            @ApiImplicitParam(name="phone",value = "手机号",required = true),
            @ApiImplicitParam(name="code",value = "验证码",required = true),
            @ApiImplicitParam(name="lat",value = "纬度（用于个人详情页展示距离）",required = false),
            @ApiImplicitParam(name="lng",value = "经度（用于个人详情页展示距离）",required = false),
            @ApiImplicitParam(name="name",value = "用户昵称(非必传,分享出去的注册页面有这个字段才添加的)",required = false),
            @ApiImplicitParam(name="uid",value = "分享用户的ID(非必传,分享出去的注册页面有这个字段才添加的)",required = false)
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n" +
            "      avatar:头像,\n"  +
            "      nickName:昵称,\n" +
            "      sex:性别;(0:男;1:女 没值显示选择性别的页面，有值不显示),\n" +
            "      ssoId:用户ID,\n" +
            "      token:登录状态的TOKEN,\n" +
            "      flag:用于判断女用户是否完善资料（1/要提示  2/不提示）,\n" +
            "      wori:颜值认证提示（1/要提示  2/不提示）,\n" +
            "      remind:有值(该值为剩余天数)就提醒，没值不提醒（用于首页VIP到期弹框提醒）,\n" +
            "    }\n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "ssoLogin",method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg ssoLogin(String phone,String code,String lat,String lng,String name,String uid) {
        if(Tool.isNull(phone))return ResultMsg.fail("请输入手机号",null,null);
        if(Tool.isNull(code))return ResultMsg.fail("请输入验证码",null,null);
        String cache_code = CacheKit.get("CONSTANT", phone);
        if(Tool.isNull(cache_code))return ResultMsg.fail("请先获取验证码",null,null);
        if(!cache_code.equals(code))return ResultMsg.fail("验证码错误",null,null);
        CacheKit.remove("CONSTANT",phone);
        //开启事务
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("ssoLogin");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        String token = "";
        Map<String,Object>ssoMap= new HashMap<>();
        //查询是否存在该用户
        Sso old_sso = ssoService.selectOne(new EntityWrapper<Sso>().eq("phone", phone));
        System.err.println(old_sso);
        if (old_sso != null){

            //是否被封号
            if (old_sso.getState().equals("封号")) return ResultMsg.fail("接口调用成功","您已经被封号",null);

            //查询出info对象
            SsoInfo ssoInfo = ssoInfoService.selectOne(new EntityWrapper<SsoInfo>().eq("sso_id", old_sso.getSsoId()));

            ssoMap.put("avatar",old_sso.getAvatar());
            ssoMap.put("nickName",old_sso.getNickName());
            ssoMap.put("sex",Tool.isNull(old_sso.getSex())?"":old_sso.getSex());
            ssoMap.put("ssoId",old_sso.getSsoId());
            if ( "0".equals(old_sso.getSex()) || (!Tool.isNull(old_sso.getBigAvatar()) && !old_sso.getAvatar().equals("http://www.qcy2019.com:7777/static/api_img/womenAvatar.jpg") && !Tool.isNull(ssoInfo.getAdvantege()))){
                ssoMap.put("flag","2");
            }else ssoMap.put("flag","1");
            Vip vip = vipService.selectOne(new EntityWrapper<Vip>().eq("sso_id", old_sso.getSsoId()));
            if (vip==null){
                ssoMap.put("wori","1");
                ssoMap.put("remind","");
            }else{
                int remind = Integer.parseInt(settingConfiguration.getSetting().getRemind());
                int validDate = vip.getValidDate();
                if (validDate <= remind && vip.getStatus()==1){
                    ssoMap.put("remind",vip.getValidDate());
                }else {
                    ssoMap.put("remind","");
                }
                ssoMap.put("wori","2");
            }
            //更新状态
            old_sso.setState("在线");
            if (!Tool.isNull(lat) && !Tool.isNull(lng)){
                old_sso.setLat(lat);
                old_sso.setLng(lng);
            } else {
                old_sso.setLat("106.42");
                old_sso.setLng("26.65");
            }
            //IOS这边TOKEN一变审核那边就不通畅
//            token = UuidUtil.get32UUID();
//            old_sso.setToken(token);
            token=old_sso.getToken();
            ssoService.updateById(old_sso);
            transactionManager.commit(status);
            ssoMap.put("token",token);
            CacheKit.put("CONSTANT",token,old_sso);
            return ResultMsg.success("登录成功",null,ssoMap);
        }else {
            try {
                int sso_id = Integer.parseInt(RandomUtil.getRandom(7));
                //封装账号信息
                Sso sso = new Sso();
                if (!Tool.isNull(name) && !Tool.isNull(uid)){
                    sso.setNickName(name);
                }else {sso.setNickName("黔城交友用户");}
                sso.setSsoId(sso_id);
                token = UuidUtil.get32UUID();
                sso.setToken(token);
                sso.setPhone(phone);
                sso.setState("在线");
                sso.setSignature("我在这里，你在哪");
                sso.setCheckBigAvatar("0");
                sso.setLat(lat);
                sso.setLng(lng);
                sso.setCreateTime(new DateTime());

                //封装基本信息
                SsoInfo ssoInfo = new SsoInfo();
                ssoInfo.setSsoId(sso_id);
                ssoInfo.setStartRemark(4.0);
                ssoInfo.setAge(25);
                ssoInfo.setTall(165);
                ssoInfo.setWeight(50);

                //封装账户信息
                SsoAccount ssoAccount = new SsoAccount();
                ssoAccount.setSsoId(sso_id);
                ssoAccount.setStatus("0");
                ssoAccount.setCreateTime(new DateTime());
                ssoAccount.setPayPassword(UuidUtil.get32UUID());
                ssoAccount.setFrozenBalance(0.00);
                ssoAccount.setUseableBalance(0.00);

                //创建邀请关系记录
                if (!Tool.isNull(uid)){
                    Invited invited = new Invited();
                    invited.setBeSsoId(sso_id+"");
                    invited.setSsoId(uid);
                    invited.setStatus("0");
                    invited.setCreateTime(new DateTime());
                    invitedService.insert(invited);
                }

                //插入数据库
                ssoService.insert(sso);
                ssoInfoService.insert(ssoInfo);
                ssoAccountService.insert(ssoAccount);
                transactionManager.commit(status);

                ssoMap.put("avatar",sso.getAvatar());
                ssoMap.put("nickName",sso.getNickName());
                ssoMap.put("sex","");
                ssoMap.put("ssoId",sso.getSsoId());
                ssoMap.put("token",sso.getToken());

                CacheKit.put("CONSTANT",token,sso);
                return ResultMsg.success("登录成功",HttpStatus.OK.toString(),ssoMap);
            } catch (Exception e) {
                e.printStackTrace();
                transactionManager.rollback(status);
                return  ResultMsg.fail("系统错误",e.toString(),null);
            }
        }

    }

    //已测
    @ApiOperation(value = "获取轮滑中的两个UI图",notes = "获取求聊页中的VIP限时抢购和黔城女生排行榜的两个UI图接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户的token", required = true)
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "      flag:标记是否可以点击（0/不能点击 1/可以点击）,\n" +
            "      vipUi:轮滑会员UI,\n" +
            "      sortUi:轮滑排行榜UI,\n" +
            "}\n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "getVipUiAndSortUi",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg getVipUiAndSortUi(String token) {
        if (Tool.isNull(token)) return  ResultMsg.fail("token为空!",null,null);
        Sso sso = identify2(token);
        if (sso == null) return ResultMsg.fail("非法token!",null,null);
        HashMap<String,Object> resultMap = new HashMap<>();
        //返回主页轮滑UI
        Object[] a = new Object[2];
        if ("0".equals(sso.getSex())){
           a[0]=1;
           a[1]=2;
        }else {
           a[0]=3;
           a[1]=4;
        }
        ArrayList<String> conditions = new ArrayList<>();
        conditions.add("id");
        List<Banner> banners = bannerService.selectList(new EntityWrapper<Banner>().in("id", a).orderAsc(conditions));
        if (banners.size()<2) return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), resultMap);
        resultMap.put("vipUi",banners.get(0).getPath());
        resultMap.put("sortUi",banners.get(1).getPath());
        resultMap.put("flag",1);
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), resultMap);
    }

    //已测
    @ApiOperation(value = "选择性别",notes = "<img src='"+FSS.qcgs+"xzxb.png' />")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户的token", required = true),
            @ApiImplicitParam(name = "sex", value = "性别（0/男 1/女）", required = true),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "      avatar:头像,\n"  +
            "      nickName:昵称,\n" +
            "      sex:性别;(0:男;1:女 没值显示选择性别的页面，有值不显示),\n" +
            "      ssoId:用户ID,\n" +
            "      token:登录状态的TOKEN,\n" +
            "      flag:用于判断女用户是否完善资料（1/要提示  2/不提示）,\n" +
            "      wori:颜值认证提示（1/要提示  2/不提示）,\n" +
            "}\n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "updateSex",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg updateSex(String token ,String sex) {
        if (Tool.isNull(token)) return  ResultMsg.fail("token为空!",null,null);
        if (Tool.isNull(sex)) return  ResultMsg.fail("sex为空!",null,null);
        Sso sso = identify2(token);
        if (sso == null) return ResultMsg.fail("非法token!",null,null);
        //dao.updateBySQL("update t_sso set sex = '"+sex+"' where sso_id = '"+sso.getSsoId()+"'");
        sso.setSex(sex);
        sso.setUpdateTime(new DateTime());
        String domain = Tool.getDomain();
        if (domain.startsWith("localhost")||domain.startsWith("127.0.0.1")){
            domain = "192.168.1.145:7777";
        }
        HashMap<String, Object> result = new HashMap<>();
        if ("0".equals(sex)){
            sso.setAvatar("http://"+domain+"/static/api_img/manAvatar.jpg");//设置默认头像
            result.put("avatar","http://"+domain+"/static/api_img/manAvatar.jpg");
        }else{
            sso.setAvatar("http://"+domain+"/static/api_img/womenAvatar.jpg");//设置默认头像
            result.put("avatar","http://"+domain+"/static/api_img/womenAvatar.jpg");
        }
        ssoService.updateById(sso);

        //临时增加的男性用户注册送1个月VIP和30钻
        if("0".equals(sex))dao.insertBySQL("insert into t_vip (sso_id,type_id,valid_date,`start`,`status`,create_time,end_time) value ("+sso.getSsoId()+",4,30,30,1,now(),DATE_ADD(now(),interval 30 day))");

        result.put("nickName",sso.getNickName());
        result.put("sex",sso.getSex());
        result.put("token",sso.getToken());
        result.put("ssoId",sso.getSsoId());
        if ("0".equals(sex)){
            result.put("flag","2");
            result.put("wori","2");
        }else{
            result.put("flag","1");
            result.put("wori","1");
        }
        CacheKit.remove("CONSTANT",token);
        //创建消息
//        Message message = new Message();
//        message.setContent("1".equals(sex)?"欢迎来到黔城故事,完成自拍认证可以开始枪聊赚钱啦！":"欢迎来到黔城故事,开始寻找你的女神吧!");
//        message.setCreateTime(new DateTime());
//        message.setLook("0");
//        message.setSsoId(0);
//        message.setMessageSsoId(String.valueOf(sso.getSsoId()));
//        message.setType("2");
//        message.setOfficialMessageType("4");
//        messageService.insert(message);
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), result);
    }

    //已测
    @ApiOperation(value = "获取所有会员类型信息",notes = "<img src='"+FSS.qcgs+"hylx.png' />点击主页会员UI后跳转到会员详情页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sex", value = "性别（0/男 1/女）", required = true)
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n" +
            " \"imgUrl\": 会员详情页的UI图url\"\",\n" +
            " \"vipTypeList\": \"会员类型集合\" \n" +
            "    {\n" +
            "        \"id\": \"\",\n" +
            "        \"name\": \n" +
            "         \"originMoney\": 原件 \n" +
            "         \"realMoney\": 现价\n" +
            "        \"mediaList\": \"图片或者视频集合\" \n" +
            "        \"totalStart\": \"送多少砖石\" \n" +
            "        },\n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "getVipInfo",method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg getVipInfo(String sex) {
        if (Tool.isNull(sex)) return  ResultMsg.fail("sex为空!",null,null);
        HashMap<String,Object> arron = new HashMap<>();
        Banner banner = bannerService.selectById(3);
        arron.put("imgUrl",banner.getPath());
        if ("0".equals(sex)){
            List<Map<String, Object>> maps = dao.selectBySQL("SELECT id,name,origin_money,real_money,total_start from t_vip_type where id < 4 ");
            arron.put("vipTypeList",maps);
        }else {
            List<Map<String, Object>> maps = dao.selectBySQL("SELECT id,name,origin_money,real_money,total_start from t_vip_type where id > 4 ");
            arron.put("vipTypeList",maps);
        }
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), arron);
    }

    //已测
    @ApiOperation(value = "黔城女生排行榜",notes = "<img src='"+FSS.qcgs+"phb.png' />点击主页排行榜UI后跳转到排行榜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "分页页码 不传默认为0,第一次进页面可不传,数据需要分页需要使用", required = false),
            @ApiImplicitParam(name = "pageSize", value = "分页页大小 不传默认为20,数据需要分页需要使用", required = false),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n" +
            " \"vipTypeList\": \"会员类型集合\" \n" +
            "    {\n" +
            "        \"sso_id\":用户id  用于点击头像跳转至个人主页 \n" +
            "        \"avator\":头像 \n" +
            "         \"nickName\": 昵称 \n" +
            "        \"sort\": \"人气值\" \n" +
            "        },\n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "getGirlSort",method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg getGirlSort(  @RequestParam(name = "pageSize", defaultValue = "20", required = false) Integer pageSize,
                                   @RequestParam(name = "pageNo", defaultValue = "0", required = false) Integer pageNo) {
        List<Map<String, Object>> arron = dao.selectBySQL("SELECT\n" +
                "\ta.sso_id,\n" +
                "\tavatar,\n" +
                "\tnick_name AS nickName,\n" +
                "\tsort \n" +
                "FROM\n" +
                "\tt_sso a,\n" +
                "\tt_sso_info b,\n" +
                "\tt_vip c\n" +
                "WHERE\n" +
                "\ta.sso_id = b.sso_id AND\n" +
                "\ta.sso_id = c.sso_id AND\n" +
                "\tNOW() < c.end_time\n" +
                "\tAND sex = '1' \n" +
                "\tAND sort IS NOT NULL \n" +
                "\tAND sort != '' \n" +
                "\tAND sort > 1 \n" +
                "ORDER BY\n" +
                "\tsort DESC \n" +
                "\tlimit " + pageNo * pageSize + "," + pageSize + "");
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), arron);
    }

    //已测
    @ApiOperation(value = "获取个人主页详情",notes = "<img src='"+FSS.qcgs+"grzy.png' />ssoId，如果是自己的传自己的,如果是看别人的传别人的，返回用户的详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value = "当前用户的token",required = true),
            @ApiImplicitParam(name="ssoId",value = "用户的ID（不管是看自己的还是看别人的都要传，总之看谁的就传谁的）",required = true),
            @ApiImplicitParam(name="lat",value = "纬度（短的那个）",required = true),
            @ApiImplicitParam(name="lng",value = "经度（长的那个）",required = true),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n" +
            "      avatar:头像,\n"  +
            "      nickName:昵称,\n" +
            "      sex:性别;(0:男;1:女),\n" +
            "      age:年龄,\n" +
            "      tall:身高 (单位已经给你拼接好了),\n" +
            "      weight:体重 (单位已经给你拼接好了),\n" +
            "      adress:地址,\n" +
            "      distance:距离,\n" +
            "      advantege:个人优势(拼串的形式  id:name:color ),\n" +
            "      startRemark:星评分数,\n" +
            "      phone:电话号码（0/没有获取过  返回电话号码说明获取过）,\n" +
            " \"tagList\": \"标签数组\" \n" +
            "    {\n" +
            "        \"小蛮腰 \n" +
            "        },\n" +
            "      haveStartRight:是否具有星评的权利（yes/有  no/没有）,\n" +
            "      meetingstart:见面需要的钻石\n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "getSsoInfo",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg getSsoInfo(String token,String ssoId,String lat,String lng) {
        if (Tool.isNull(token)) return ResultMsg.fail("token为空", null, null);
        if (Tool.isNull(lat)) return ResultMsg.fail("lat为空", null, null);
        if (Tool.isNull(lng)) return ResultMsg.fail("lng为空", null, null);
        Map<String,Object> arron = new HashMap<>();
        Sso sso = identify2(token);
        List<Map<String, Object>> infoList;
        if (sso ==null)return ResultMsg.success("token无效", null, null);
        try {
            infoList = dao.selectBySQL("SELECT\n" +
                    "\ta.avatar,\n" +
                    "\ta.phone,\n" +
                    "\ta.nick_name AS nickName,\n" +
                    "\ta.sex,\n" +
                    "\ta.lat,\n" +
                    "\ta.lng,\n" +
                    "\tb.age,\n" +
                    "\tCONCAT( b.tall, 'cm' ) tall,\n" +
                    "\tCONCAT( b.weight, 'kg' ) weight,\n" +
                    "\tb.advantege,\n" +
                    "\tb.start_remark AS startRemark,\n" +
                    "\tb.tag_ids AS tagIds,\n" +
                    "\ta.meetingstart,\n" +
                    "c.sso_id\n" +
                    "FROM\n" +
                    "\tt_sso a\n" +
                    "LEFT JOIN t_sso_info b ON a.sso_id = b.sso_id\n" +
                    "LEFT JOIN t_start_record c ON b.sso_id = c.girl_id\n" +
                    "WHERE\n" +
                    "\ta.sso_id = "+ "'" + ssoId +"'" );
            boolean flag = false;
            if (Tool.listIsNull(infoList))return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), infoList);
            if (!Tool.listIsNull(infoList)){
                for (Map<String, Object> m :infoList){
                    if (m.containsKey("sso_id")){
                        String wocao = m.get("sso_id")+"";
                        if (wocao.equals(sso.getSsoId()+"")){
                            flag = true;
                        }
                    }
                }
                if (flag ==true){
                    infoList.get(0).put("haveStartRight","yes");
                } else {
                    infoList.get(0).put("haveStartRight","no");
                }
                infoList.get(0).remove("sso_id");
                int distance = (int) LocationUtils.getDistance(Double.parseDouble(lat), Double.parseDouble(lng), Double.parseDouble(infoList.get(0).get("lat").toString()), Double.parseDouble(infoList.get(0).get("lng").toString()));
                if (distance<1000) infoList.get(0).put("distance",distance+"m");
                else {
                    double dis = distance/1000;
                    DecimalFormat df = new DecimalFormat("#.0");
                    infoList.get(0).put("distance",df.format(dis)+"km");
                }
                //infoList.get(0).put("distance",distance);
                //封装标签数组
                ArrayList<String> list = new ArrayList<>();
                if (infoList.get(0).containsKey("tagIds")){
                    String tagIds = infoList.get(0).get("tagIds").toString();
                    if (!Tool.isNull(tagIds)){
                        String[] split = tagIds.split(",");
                        for (String s : split){
                            String[] split1 = s.split(":");
                            list.add(split1[1]);
                        }
                    }
                    infoList.get(0).remove("tagIds");
                }

                List<Map<String, Object>> list1 = new ArrayList<>();
                if (infoList.get(0).containsKey("advantege")){
                    String tagIds = infoList.get(0).get("advantege").toString();
                    if (!Tool.isNull(tagIds)){
                        String[] split = tagIds.split(",");
                        for (String s : split){
                            String[] split1 = s.split(":");
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("id",split1[0]);
                            map.put("name",split1[1]);
                            map.put("color",split1[2]);
                            list1.add(map);
                        }
                    }
                    infoList.get(0).remove("advantege");
                }

                //判断是否获取过该用户的手机号
                Message message = messageService.selectOne(new EntityWrapper<Message>().eq("type", "1").eq("sso_id", ssoId).eq("message_sso_id", sso.getSsoId()));
                if (message == null){
                    infoList.get(0).put("phone","");
                }

                infoList.get(0).put("tagList",list);
                infoList.get(0).put("advantegeList",list1);
                infoList.get(0).put("adress","贵阳市");
                infoList.get(0).remove("lat");
                infoList.get(0).remove("lng");
                sso.setLng(lng);
                sso.setLat(lat);
                CacheKit.remove("CONSTANT",token);
                CacheKit.put("CONSTANT",token,sso);
                ssoService.updateById(sso);
            }
            return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), infoList.get(0));
        }catch (Exception e){
            e.printStackTrace();
            return ResultMsg.success("接口调用失败!", HttpStatus.BAD_REQUEST.toString(), new HashMap<>());
        }
    }

    //已测
    @ApiOperation(value = "获取用户动态",notes = "<img src='"+FSS.qcgs+"grzy.png' />ssoId，如果是自己的传自己的,如果是看别人的传别人的，返回用户的详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value = "用户的token",required = true),
            @ApiImplicitParam(name="ssoId",value = "用户的ID（获取别人的动态就传别人的ID，获取自己的动态就传自己的ID，查看所有人的动态时不用传）",required = true),
            @ApiImplicitParam(name = "pageNo", value = "分页页码 不传默认为0,第一次进页面可不传,数据需要分页需要使用", required = false),
            @ApiImplicitParam(name = "pageSize", value = "分页页大小 不传默认为10,数据需要分页需要使用", required = false),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n" +
            "    {\n" +
            "        \"id\": 作品ID\"\",\n" +
            "        \"content\": 内容\n" +
            "        \"thumb\": 封面\n" +
            "         \"type\": 1/图片  2/视频\n" +
            "         \"createTime\": 发布时间\n" +
            "        \"mediaList\": \"图片或者视频集合\" \n" +
            "  &nbsp;&nbsp;&nbsp;&nbsp;          {\n" +
//            "     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;           \"id\": \n" +
            "         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       \"oss_object_name\": 资源url\"\",\n" +
            "     &nbsp;&nbsp;&nbsp;&nbsp;       }\n" +
            "        },\n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "getDynamic",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg getdynamic(String token,
                                Integer ssoId,
                                @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                @RequestParam(name = "pageNo", defaultValue = "0", required = false) Integer pageNo) {
        if (Tool.isNull(token)) return ResultMsg.fail("token为空", null, null);
        if (Tool.isNull(ssoId)) return ResultMsg.fail("ssoId为空", null, null);
        List<Map<String, Object>> worksList = new ArrayList<>();
        List<Map<String, Object>> worksOrderByList = new ArrayList<>();
        Sso sso = identify2(token);
        if (sso ==null)return ResultMsg.success("token无效", null, null);
        if (sso.getSex().equals("1")){
            Vip vip = vipService.selectOne(new EntityWrapper<Vip>().eq("sso_id", ssoId));
            if (vip == null) return ResultMsg.success("该女性用户非会员!", HttpStatus.OK.toString(), worksList);
        }
        try {
            if (ssoId == 0){//所有用户动态

            }else {
                worksList =  dao.selectBySQL("SELECT\n" +
                        "\tid,\n" +
                        "\tcontent,\n" +
                        "\tthumb,\n" +
                        "\ttype,\n" +
                        "\t( SELECT GROUP_CONCAT( b.oss_object_name SEPARATOR ',' ) FROM t_media b WHERE b.base_id = a.base_id  ) AS pictures,\n" +//AND b.`check` = '1'
                        "\tIF\n" +
                        "\t(\n" +
                        "\tdate( a.create_time ) = curdate( ),\n" +
                        "\tDATE_FORMAT( a.create_time, '%H:%i' ),\n" +
                        "IF\n" +
                        "\t(\n" +
                        "\tto_days( now( ) ) - to_days( a.create_time ) = 1,\n" +
                        "\t'昨天',\n" +
                        "\t(\n" +
                        "IF\n" +
                        "\t(\n" +
                        "\tYEAR ( a.create_time ) = YEAR ( NOW( ) ),\n" +
                        "\tDATE_FORMAT( a.create_time, '%m-%d %H:%i' ),\n" +
                        "\tDATE_FORMAT( a.create_time, '%Y-%m-%d' ) \n" +
                        "\t) \n" +
                        "\t) \n" +
                        "\t) \n" +
                        "\t) time \n" +
                        "\tFROM t_sso_work a WHERE sso_id = "+ssoId +"\n" +
                        "\t order by create_time" );
                Iterator<Map<String, Object> > iterator = worksList.iterator();
                while(iterator.hasNext()) {
                    Map<String, Object> m = iterator.next();
                    ArrayList<String> mediaList = new ArrayList<>();
                    String pictures = m.get("pictures")+"";
                    if (!Tool.isNull(pictures)){
                        String[] split = pictures.split(",");
                        if (split.length>0){
                            Arrays.stream(split).forEach(s -> {
                                mediaList.add(s);
                            });
                        }
                    }
                    m.put("mediaList",mediaList);
                    m.remove("pictures");
                    if (Tool.listIsNull(mediaList)) iterator.remove();
                }
            }
            for (int i=worksList.size()-1;i>=0;i--){
                worksOrderByList.add(worksList.get(i));
            }
            return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), worksOrderByList);
        }catch (Exception e){
            e.printStackTrace();
            return ResultMsg.success("接口调用失败!", HttpStatus.BAD_REQUEST.toString(), worksOrderByList);
        }
    }

    //已测
    @ApiOperation(value = "抢聊主页，向下拉取更多封面图",notes = "<img src='"+FSS.qcgs+"casc.png' />")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value = "用户的token",required = true),
            @ApiImplicitParam(name = "pageNo", value = "分页页码 不传默认为0,第一次进页面可不传,数据需要分页需要使用", required = false),
            @ApiImplicitParam(name = "pageSize", value = "分页页大小 不传默认为10,数据需要分页需要使用", required = false),
            @ApiImplicitParam(name="lat",value = "纬度",required = true),
            @ApiImplicitParam(name="lng",value = "经度",required = true),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n" +
            "    {\n" +
            "        \"ssoId\": 用户的ID \n" +
            "        \"nick_name\":昵称 \n" +
            "        \"distance\":距离 \n" +
            "        \"bigAvator\": \"封面图\" \n" +
            "        },\n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "getGirlBigAvators",method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg getGirlBigAvators( @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                        @RequestParam(name = "pageNo", defaultValue = "0", required = false) Integer pageNo,String lat,String lng,String token) {
        if (Tool.isNull(token)) return ResultMsg.fail("token为空", null, null);
        if (Tool.isNull(lat)) return ResultMsg.fail("lat为空", null, null);
        if (Tool.isNull(lng)) return ResultMsg.fail("lng为空", null, null);
        Sso sso = identify2(token);
        if (sso ==null)return ResultMsg.success("token无效", null, null);
        List<Map<String, Object>> arron = dao.selectBySQL("SELECT\n" +
                "\ta.sso_id,\n" +
                "\tnick_name,\n" +
                "\tbig_avatar,\n" +
                "\tlat,\n" +
                "\tlng \n" +
                "FROM\n" +
                "\tt_sso a,\n" +
                "\tt_sso_info b,\n" +
                "\tt_vip c \n" +
                "WHERE\n" +
                "\ta.sso_id = b.sso_id \n" +
                "\tAND b.sso_id = c.sso_id \n" +
                "\tAND NOW() < c.end_time\n" +
                //"\tAND check_big_avatar = '1' \n" +
                "\tAND big_avatar IS NOT NULL \n" +
                "\tAND big_avatar != '' \n" +
                "\tAND sex = '1' \n" +
                "ORDER BY\n" +
                "\ta.create_time DESC "+ " limit " + pageNo * pageSize + "," + pageSize + "");
        int i=0;
        int index = -1;
        for (Map<String, Object> m : arron){
            Double lat1 =  Double.parseDouble(m.get("lat").toString());
            Double lng1 =  Double.parseDouble(m.get("lng").toString());
            int dis = (int) LocationUtils.getDistance(Double.parseDouble(lat), Double.parseDouble(lng), lat1, lng1);
            if (dis<1000) m.put("distance",dis+"m");
            else {
                double distance = dis/1000;
                DecimalFormat df = new DecimalFormat("#.0");
                m.put("distance",df.format(distance)+"km");
            }
            m.remove("lat");
            m.remove("lng");
            if (sso.getSex().equals("1") && m.get("sso_id").toString().equals(sso.getSsoId().toString())){
                 index = i;
            }
            i++;
        }
        if (index != -1){
            Map<String, Object> map = arron.get(index);
            //状态值
            SsoInfo info = ssoInfoService.selectOne(new EntityWrapper<SsoInfo>().eq("sso_id", sso.getSsoId()));
            if(Tool.isNull(info.getAdvantege()) || sso.getAvatar().equals("http://app.qcy2019.com:7777/static/api_img/womenAvatar.jpg")){
                map.put("state","未上线");
            }else {
                map.put("state","已上线");
            }
            arron.remove(index);
            arron.add(0,map);
        }else {
            if (pageNo==0 && sso.getSex().equals("1") && !Tool.isNull(sso.getBigAvatar())){
                Vip vip = vipService.selectOne(new EntityWrapper<Vip>().eq("sso_id", sso.getSsoId()));
                HashMap<String, Object> map = new HashMap<>();
                map.put("ssoId",sso.getSsoId());
                map.put("nick_name",sso.getNickName());
                map.put("distance","0m");
                map.put("bigAvator",sso.getBigAvatar());
                map.put("state","已上线");
                if (vip == null){
                    map.put("state","未上线");
                }else map.put("state","已下线");
                arron.add(0,map);
            }
        }
        sso.setLng(lng);
        sso.setLat(lat);
        CacheKit.remove("CONSTANT",token);
        CacheKit.put("CONSTANT",token,sso);
        ssoService.updateById(sso);
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), arron);
    }

    //已测
    @ApiOperation(value = "获取联系方式",notes = "ssoId，根据token判断是否有权利，根据ssoId查该用户的联系方式")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value = "当前用户的token",required = true),
            @ApiImplicitParam(name="ssoId",value = "想要获取的用户的ID",required = true),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n" +
            "      phone:手机号码(有权访问就返回电话号码，没有权利就返回不满足的条件),\n"  +
            "      start:见面需要的砖石数量\n"  +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "getPhone",method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg getPhone(String token,String ssoId) {
        if (Tool.isNull(token)) ResultMsg.fail("token为空", null, null);
        Sso sso = ssoService.selectOne(new EntityWrapper<Sso>().eq("token", token));
        if (sso == null) ResultMsg.fail("token无效", token, null);
        //开启事务
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("getPhone");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);

        Setting setting = settingService.selectById(1);
        Integer start = setting.getStartGetTel();

        Sso girl = ssoService.selectOne(new EntityWrapper<Sso>().eq("sso_id", ssoId));
        try {
            HashMap<String, Object> arron = new HashMap<>();
            Vip vip = vipService.selectOne(new EntityWrapper<Vip>().eq("sso_id", sso.getSsoId()));
            if (vip ==null){
                arron.put("phone","您还不是会员！");
            } else {
                Date now = new Date();
                if ((vip.getEndTime().getTime() <  now.getTime())){
                    vip.setStatus(2);
                    arron.put("phone","您的会员过期");
                }else if (vip.getStart() < start){//设置获取联系方式的星砖数量
                    arron.put("phone","您的星砖不够");
                }else {

                    arron.put("phone",girl.getPhone());
                    arron.put("start",girl.getMeetingstart());
/*                    SsoAccount ssoAccount1 = ssoAccountService.selectOne(new EntityWrapper<SsoAccount>().eq("sso_id", sso.getSsoId()));
                    //会员减砖石
                    if (vip.getStart()>500){*/
                    vip.setStart(vip.getStart() - start);
                    vipService.updateById(vip);
                    dao.updateBySQL("update t_sso_info set sort = sort + 20 where sso_id = "+girl.getSsoId());
     /*               }else if (ssoAccount1.getUseableBalance()>500){
                        ssoAccount1.setUseableBalance(ssoAccount1.getUseableBalance() - 500);
                        ssoAccountService.updateById(ssoAccount1);
                    }else {
                        vip.setStart(500 - vip.getStart());
                        ssoAccount1.setUseableBalance((double) (500 - (500 - vip.getStart())));
                        vipService.updateById(vip);
                        ssoAccountService.updateById(ssoAccount1);
                    }*/
                    //更新数据
//                    vip.setStart(vip.getStart()-500);
//                    vipService.updateById(vip);
                    //加砖石
//                    SsoAccount ssoAccount = ssoAccountService.selectOne(new EntityWrapper<SsoAccount>().eq("sso_id", ssoId));
//                    ssoAccount.setUseableBalance(500.0+ssoAccount.getUseableBalance());
//                    ssoAccountService.updateById(ssoAccount);
                    //创建砖石消费流水
                    StartRecord startRecord = new StartRecord();
                    startRecord.setCreateTime(new DateTime());
                    startRecord.setGirlId(Integer.parseInt(ssoId));
                    startRecord.setSsoId(sso.getSsoId());
                    startRecord.setStartCount(start);
                    startRecord.setType("1");
                    startRecordService.insert(startRecord);
                    //创建消息
                    Message message = new Message();
                    message.setContent("您刚才使用"+start+"砖石购买了用户名为："+girl.getNickName()+"的联系方式！");
                    message.setCreateTime(new DateTime());
                    message.setLook("0");
                    message.setSsoId(girl.getSsoId());
                    message.setMessageSsoId(sso.getSsoId()+"");
                    message.setType("1");
                    message.setOfficialMessageType("4");
                    messageService.insert(message);

                }
            }
            transactionManager.commit(status);
            return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), arron);
        }catch (Exception e) {
            e.printStackTrace();
            transactionManager.rollback(status);
            return  ResultMsg.fail("系统错误",e.toString(),null);
        }
    }

    //已测
    @ApiOperation(value = "星评或者点赞标签接口",notes = "这是一个混合接口，支持星评和点赞标签")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value = "当前用户的token",required = true),
            @ApiImplicitParam(name="ssoId",value = "被星评/点赞标签的用户的ID",required = true),
            @ApiImplicitParam(name="startRemark",value = "星评分数（注：只是星评的时候，传星评分数，否则不传）",required = false),
            @ApiImplicitParam(name="tagId",value = "标签ID（注：只是点赞标签的时候，传标签ID，否则不传）",required = false),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "giveStartOrTag",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg giveStartOrTag(String token,String ssoId,String startRemark,String tagId) {
        if (Tool.isNull(token)) return ResultMsg.fail("token为空", null, null);
        if (Tool.isNull(startRemark)) return ResultMsg.fail("参数为空", null, null);
        if (Tool.isNull(tagId)) return ResultMsg.fail("参数为空", null, null);
        Sso sso = ssoService.selectOne(new EntityWrapper<Sso>().eq("token", token));
        if (sso == null) return ResultMsg.fail("token无效", token, null);
        SsoInfo ssoInfo = ssoInfoService.selectOne(new EntityWrapper<SsoInfo>().eq("sso_id", sso.getSsoId()));
        StringBuffer sb = new StringBuffer();
        HashMap<String, Object> arron = new HashMap<>();
        if (Tool.isNull(startRemark)){//点赞标签
            String tagIds = ssoInfo.getTagIds();
            String[] tag = tagIds.split(",");
            for (int i=0;i<tag.length;i++){
                String[] split = tag[i].split(":");
                if(split[0].equals(tagId)){
                    int n = Integer.parseInt(split[2]) + 1;
                    split[2] = n+"";
                }
                sb.append(split[0]+":"+split[1]+":"+split[2]+",");
            }
            ssoInfo.setTagIds(sb+"");
            ssoInfoService.updateById(ssoInfo);

        }else {//星评

            //创建星评对象
            Comment comment = new Comment();
            comment.setCreateTime(new DateTime());
            comment.setGirlId(Integer.parseInt(ssoId));
            comment.setStartScore(Double.parseDouble(startRemark));
            comment.setVipId(sso.getSsoId());
            commentService.insert(comment);

            //更新星评分数
            List<Map<String, Object>> maps = dao.selectBySQL("select IF(CAST(avg(start_score*1.0) as DECIMAL(9,1)) is NULL,4,CAST(avg(start_score*1.0) as DECIMAL(9,1))) as score  from t_comment  where girl_id= " + "'" + ssoId + "'");
            String score = maps.get(0).get("score")+"";
            ssoInfo.setStartRemark(Double.parseDouble(score));
            ssoInfoService.updateById(ssoInfo);

        }
        //更新排序
        dao.updateBySQL("update t_sso_info set sort = sort + 5 where sso_id = "+ssoId);
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), arron);
    }

    //已测
    @ApiOperation(value = "我的封面主页",notes = "我的封面主页接口，我将返回封面url，审核状态，是否会员")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value = "当前用户的token",required = true)
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n" +
            "      url: 资源的路径url\n"  +
            "      status: 审核状态 0/审核中  1/审核通过  2/不通过\n"  +
            "      is_Vip: 是否会员  0/不是  1/是\n"  +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "bigAvatorIndex",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg bigAvatorIndex(String token) {
        if (Tool.isNull(token)) return ResultMsg.fail("token为空", null, null);
        Sso sso = ssoService.selectOne(new EntityWrapper<Sso>().eq("token", token));
        if (sso == null) return ResultMsg.fail("token无效", token, null);
        Map<String, Object> arron = new HashMap<>();
        arron = dao.selectBySQL("SELECT\n" +
                "\tIF(a.big_avatar is null,'',a.big_avatar) url,\n" +
                "\tIF(a.check_big_avatar is null,'',a.check_big_avatar) status,\n" +
                "  IF(b.id is null,'0','1') is_vip\n" +
                "FROM\n" +
                "\tt_sso a\n" +
                "\tLEFT JOIN t_vip b ON a.sso_id = b.sso_id \n" +
                "WHERE\n" +
                "\ta.sso_id ="+sso.getSsoId()).get(0);
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), arron);
    }

    //已测
    @ApiOperation(value = "上传单图片到服务器",notes = "<img src='"+FSS.qcgs+"xgxx.png' />（头像、封面、作品封面等单图片统一上传接口）")
    @ApiImplicitParams({
            @ApiImplicitParam(name="fiel",value = "图片文件",required = false),
            @ApiImplicitParam(name="token",value = "当前用户的token",required = true),
            @ApiImplicitParam(name="type",value = "上传类型  1/头像  2/个人封面  3/其他(只上传图片不更新数据库)",required = true),
            @ApiImplicitParam(name="oldAvatorUrl",value = "修改前头像/封面的url,系统默认头像可传，可不传，系统会做判断",required = false),
            @ApiImplicitParam(name="lat",value = "经度（用于个人详情页展示距离）",required = false),
            @ApiImplicitParam(name="lng",value = "纬度（用于个人详情页展示距离）",required = false),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n" +
            "      avator: 资源的路径url\n"  +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "uploadAvatorOrBigImg",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg uploadAvatorOrBigImg(HttpServletRequest request,String token,String oldAvatorUrl,String lat,String lng,String type) {
        if (Tool.isNull(token)) return ResultMsg.fail("token为空", null, null);
        Sso sso = identify2(token);
        if (sso == null) return ResultMsg.fail("token无效", token, null);
        HashMap<String, Object> arron = new HashMap<>();
        String url="未检测到任何上传文件";
        Map<String, MultipartFile> map = ((MultipartHttpServletRequest) request).getFileMap();
        if (map == null)return ResultMsg.fail("文件不能为空！", token, null);
        MultipartFile file = null;
        for (Iterator<String> i = map.keySet().iterator(); i.hasNext(); ) {
            Object obj = i.next();
            file = (MultipartFile) map.get(obj);
            if(file!=null&&!file.isEmpty()){
                //获取跟目录
                File path = null;
                try {
                    path = new File(ResourceUtils.getURL("classpath:").getPath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return ResultMsg.fail("文件未找到!", null, null);
                }
                if(!path.exists()) path = new File("");

                //如果上传目录为static/api_img/，则可以如下获取：
                File upload = new File(path.getAbsolutePath(),"static/api_img/");
                if(!upload.exists()) upload.mkdirs();
                String filePath =upload.getAbsolutePath();

                UUID uuid = UUID.randomUUID();
                //删除之前的
                if (!Tool.isNull(oldAvatorUrl)){
                    String[] split = oldAvatorUrl.split("/");
                    if (!split[split.length-1].equals("manAvatar.jpg") && !split[split.length-1].equals("womenAvatar.jpg")){
                        File oldFile = new File(filePath+"/"+split[split.length-1]);
                        if (oldFile.exists()){
                            oldFile.delete();
                        }
                    }
                }
                FileOutputStream out = null;
                File targetFile = new File(filePath);
                if(!targetFile.exists()){
                    targetFile.mkdirs();
                }
                MultipartFile file1 = (MultipartFile) map.get(obj);
                String originalFilename = file1.getOriginalFilename();
                if (!originalFilename.contains(".")){
                    originalFilename = originalFilename + ".jpg";
                }
                int pos = originalFilename.lastIndexOf("."); // 取文件的格式
                try {
                    out = new FileOutputStream(filePath+"/"+uuid+originalFilename.substring(pos));
                    out.write(file1.getBytes());
                    out.flush();
                    out.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String domain = Tool.getDomain();
                if (domain.startsWith("localhost")||domain.startsWith("127.0.0.1")){
                    domain = "192.168.1.108:7777";
                }
                url="http://"+domain+"/static/api_img/"+uuid+originalFilename.substring(pos);
                arron.put("avator",url);
            }
        }
        //更新位置
        if (!Tool.isNull(lat) && !Tool.isNull(lng)){
            sso.setLat(lat);
            sso.setLng(lng);
            CacheKit.remove("CONSTANT",token);
            CacheKit.put("CONSTANT",token,sso);
        }
        if ("1".equals(type)){
           sso.setAvatar(url);
        }else if ("2".equals(type)){
            sso.setCheckBigAvatar("0");
            sso.setBigAvatar(url);
        }
        if (!"3".equals(type)){
            ssoService.updateById(sso);
        }
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), arron);
    }

    @ApiOperation(value = "删除图片或者视频接口（本地）",notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name="urls",value = "删除多个时，资源地址用逗号隔开，只支持多图片删除和多视频删除，不支持urls中既包含图片地址又包含视频地址",required = true),
            @ApiImplicitParam(name="mediaType",value = "资源类型  1/图片   2/视频",required = true),
            @ApiImplicitParam(name="type",value = "操作类型  1/封面  ---暂时只有封面",required = true),
            @ApiImplicitParam(name="token",value = "当前登录用户token",required = true)
    })
    @RequestMapping(value = "deletePictureListByUrlOrObjectName",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg deletePictureByUrlOrObjectName(String urls,String token,String type,String mediaType){
        if(Tool.isNull(urls))return ResultMsg.fail("缺少参数","urls",null);
        if(Tool.isNull(token))return ResultMsg.fail("缺少参数","token",null);
        Sso sso = identify2(token);
        if(sso == null)return ResultMsg.fail("token无效！","token : "+token,null);
        try{
            //获取跟目录
            File path = null;
            path = new File(ResourceUtils.getURL("classpath:").getPath());
            if(!path.exists()) path = new File("");

            //如果上传目录为static/api_img/，则可以如下获取：
            File upload = null;
            if ("1".equals(mediaType)){
                upload = new File(path.getAbsolutePath(),"static/api_img/");
            }else {
                upload = new File(path.getAbsolutePath(),"static/video/");
            }

            if(!upload.exists()) upload.mkdirs();
            String filePath =upload.getAbsolutePath();

            String[] split = urls.split(",");
            for (String s:split) {
                if (!Tool.isNull(s)){
                    String[] split1 = s.split("/");
                    if (!split1[split1.length-1].equals("manAvatar.jpg") && !split1[split1.length-1].equals("womenAvatar.jpg")){
                        File oldFile = new File(filePath+"/"+split1[split1.length-1]);
                        if (oldFile.exists()){
                            oldFile.delete();
                        }
                    }
                }
            }

            //更新数据库
            if ("1".equals(type)){
                sso.setBigAvatar("");
                sso.setCheckBigAvatar("");
                ssoService.updateById(sso);
                CacheKit.remove("CONSTANT",token);
            }
            return ResultMsg.success("接口调用成功！",null,urls);
        }catch (Exception e){
            e.printStackTrace();
            return ResultMsg.fail("系统错误",e.toString(),null);
        }
    }


   /* @ApiOperation(value = "颜值认证上传封面图", notes = " <img src='" + FSS.qcgs + "scfm.png'  /> ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name="file",value = "封面图文件",required = true),
            @ApiImplicitParam(name="oldAvatorUrl",value = "修改前封面的url",required = false),
            @ApiImplicitParam(name="lat",value = "经度（用于个人详情页展示距离）",required = false),
            @ApiImplicitParam(name="lng",value = "纬度（用于个人详情页展示距离）",required = false),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n" +
            "      bigAvatarUrl:封面资源的路径url\n"  +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "bgImageUpload", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg bgImageUpload(HttpServletRequest request,String token,String oldAvatorUrl,String lat,String lng) {
        if (Tool.isNull(token))return ResultMsg.success("token为空", null, null);
        //鉴权
        Sso sso = identify2(token);
        if (sso ==null)return ResultMsg.success("token无效", null, null);

        Setting setting = settingConfiguration.getSetting();
        UploadImageRequest requestPhoto = new UploadImageRequest(setting.getAliOssAccessId(), setting.getAliOssAccessKey(), "default");
        Map<String, Object> arron = new HashMap<>();
        try {
            Map<String, MultipartFile> map = ((MultipartHttpServletRequest) request).getFileMap();
            MultipartFile multipartFile = null;
            for (Iterator<String> i = map.keySet().iterator(); i.hasNext(); ) {
                Object obj = i.next();
                multipartFile = (MultipartFile) map.get(obj);

                if (multipartFile != null) {
                    requestPhoto.setInputStream(multipartFile.getInputStream());
                }
                //上传图片
                UploadImageImpl uploadImage = new UploadImageImpl();
                UploadImageResponse responsePhoto = uploadImage.upload(requestPhoto);
                //将上传的图片路径写入数据库
               sso.setBigAvatar(responsePhoto.getImageURL());
               sso.setCheckBigAvatar("0");
               arron.put("bigAvatarUrl",responsePhoto.getImageURL());
            }
            ssoService.updateById(sso);

            executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        //更新位置
                        if (!Tool.isNull(lat) && !Tool.isNull(lng)){
                            sso.setLat(lat);
                            sso.setLng(lng);
                            CacheKit.remove("CONSTANT",token);
                            CacheKit.put("CONSTANT",token,sso);
                        }
                        if (!Tool.isNull(oldAvatorUrl)) {
                            //删除服务器上的图片
                            Tool.deleteAliVideoImgUrl(oldAvatorUrl, setting.getAliOssAccessId(), setting.getAliOssAccessKey());
                        }
                    }
            });

        } catch (Exception e) {
            e.printStackTrace();
            return ResultMsg.fail("接口调用失败!", HttpStatus.BAD_REQUEST.toString(), e);
        }
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), arron);
    }*/

/*    @ApiOperation(value = "删除图片接口",notes = "<img src='"+FSS.xgt+"deletePictureListByUrlOrObjectName1.jpg' /><img src='"+FSS.xgt+"deletePictureListByUrlOrObjectName2.jpg' /></br>该接口没有返回值目前用于发布/编辑活动时,点击删除图片urls,object_names哪个有传哪个")
    @ApiImplicitParams({
            @ApiImplicitParam(name="urls",value = "删除图片的超链接地址,可同时删除多张图片,地址之间逗号连接"),
            @ApiImplicitParam(name="token",value = "当前登录用户token",required = true)
    })
    @RequestMapping(value = "deletePictureListByUrlOrObjectName",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg deletePictureByUrlOrObjectName(String urls,String phone,String token){
        try{
            if(Tool.isNull(phone))return ResultMsg.fail("缺少参数","phone",null);
            if(Tool.isNull(token))return ResultMsg.fail("缺少参数","token",null);
            Sso sso = identify2(token);
            if (sso == null)return  ResultMsg.fail("token无效",null,null);
            Setting setting=settingConfiguration.getSetting();
            if(!Tool.isNull(urls)){
                Tool.deleteAliVideoImgUrl(urls,setting.getAliOssAccessId(),setting.getAliOssAccessKey());
            }
            return ResultMsg.success(null,null,null);
        }catch (Exception e){
            e.printStackTrace();
            return ResultMsg.fail("系统错误",e.toString(),null);
        }
    }*/

  /*  @ApiOperation(value = "删除个人作品",notes = "<img src='"+FSS.xgt+"deleteWorksInfoByUserOrManager.png' />" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value = "用户的token"),
            @ApiImplicitParam(required = true,name="workId",value = "作品ID")
    })
    @ApiResponses(@ApiResponse(code=200,message="data没东西,只有状态" ))
    @RequestMapping(value = "deleteWorkByID",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg deleteWorkByID(String token,String workId){
     if (Tool.isNull(token))return ResultMsg.fail("token为空",null,null);
        Sso sso = identify2(token);
        if (sso == null) return ResultMsg.fail("token无效",null,null);
        try{
            List<String>aLiPictureUrls=new ArrayList<>();
            Setting setting = settingConfiguration.getSetting();
            List<Map<String, Object>> maps = dao.selectBySQL("SELECT a.type,a.thumb,video_id,b.oss_object_name FROM t_sso_work a LEFT JOIN t_media b ON a.base_id = b.base_id WHERE a.id =" + workId);
            if(maps.size()>1){
                for (Map<String, Object> m : maps){
                    aLiPictureUrls.add(m.get("oss_object_name").toString());
                }
                Tool.deleteAliVideoImgUrl(StringUtils.join(aLiPictureUrls,","),setting.getAliOssAccessId(),setting.getAliOssAccessKey());
            }else {
                Tool.deleteAliVideo(maps.get(0).get("video_id").toString(),setting.getAliOssAccessId(),setting.getAliOssAccessKey());
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultMsg.fail("作品删除失败！","500",null);
        }

        return ResultMsg.success("接口调用成功",HttpStatus.OK.toString(),null);
}*/

    @ApiOperation(value = "删除个人作品(本地)",notes = "" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value = "用户的token"),
            @ApiImplicitParam(required = true,name="workId",value = "作品ID")
    })
    @ApiResponses(@ApiResponse(code=200,message="data没东西,只有状态" ))
    @RequestMapping(value = "deleteWorkByID",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg deleteWorkByID(String token,String workId){
        if (Tool.isNull(token))return ResultMsg.fail("token为空",null,null);
        Sso sso = identify2(token);
        if (sso == null) return ResultMsg.fail("token无效",null,null);
        try{
            ArrayList<String> list = new ArrayList<>();
            List<Map<String, Object>> maps = dao.selectBySQL("SELECT a.type,a.thumb,b.oss_object_name,b.id FROM t_sso_work a LEFT JOIN t_media b ON a.base_id = b.base_id WHERE a.id =" + workId);
            //获取跟目录
            File path = null;
            path = new File(ResourceUtils.getURL("classpath:").getPath());
            if(!path.exists()) path = new File("");
            File upload = null;
            upload = new File(path.getAbsolutePath(),"static/api_img/");
            if(!upload.exists()) upload.mkdirs();
            String filePath =upload.getAbsolutePath();
            if(maps.size()>1){
                for (Map<String, Object> m : maps){
                    //如果上传目录为static/api_img/，则可以如下获取：
                    String s = m.get("oss_object_name").toString();
                    if (!Tool.isNull(s)){
                        String[] split1 = s.split("/");
                        if (!split1[split1.length-1].equals("manAvatar.jpg") && !split1[split1.length-1].equals("womenAvatar.jpg")){
                            File oldFile = new File(filePath+"/"+split1[split1.length-1]);
                            if (oldFile.exists()){
                                oldFile.delete();
                            }
                        }
                    }
                    list.add(m.get("id").toString());
                }

            }else {
                //先删除封面
                File thumbFile = new File(filePath+"/"+maps.get(0).get("thumb"));
                if (thumbFile.exists()){
                    thumbFile.delete();
                }
                //删除视频
                upload = new File(path.getAbsolutePath(),"static/video/");
                if(!upload.exists()) upload.mkdirs();
                filePath =upload.getAbsolutePath();
                String s = maps.get(0).get("oss_object_name").toString();
                if (!Tool.isNull(s)){
                    String[] split1 = s.split("/");
                    if (!split1[split1.length-1].equals("manAvatar.jpg") && !split1[split1.length-1].equals("womenAvatar.jpg")){
                        File oldFile = new File(filePath+"/"+split1[split1.length-1]);
                        if (oldFile.exists()){
                            oldFile.delete();
                        }
                    }
                }
                list.add(maps.get(0).get("id").toString());
            }

            //删除作品
            workService.deleteById(workId);
            //删除图片
            mediaService.deleteBatchIds(list);

        }catch (Exception e){
            e.printStackTrace();
            return ResultMsg.fail("作品删除失败！","500",null);
        }

        return ResultMsg.success("接口调用成功",HttpStatus.OK.toString(),null);
    }


    @ApiOperation(value = "安卓核对版本接口",notes = "")
    @ApiResponses(@ApiResponse(code=200,message="data: {\n" +
            "&nbsp;"+"version:版本号字符串(请自行到'系统设置'里填写),\n" +
            "&nbsp;"+"download_url:下载地址(请自行到'系统设置'里上传),\n" +
            "}" ))
    @RequestMapping(value = "getAndroidVersionsAndDownloadUrl",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg getAndroidVersionsAndDownloadUrl(){
        try{
            Setting setting=settingConfiguration.getSetting();
            Map<String,Object>result=new HashMap<>();
            result.put("version",setting.getAndroidVersion());
            result.put("download_url",setting.getAndroidApkUrl());
            return ResultMsg.success("接口调用成功",HttpStatus.OK.toString(),result);
        }catch (Exception e){
            e.printStackTrace();
            return ResultMsg.fail("系统错误getAndroidVersionsAndDownloadUrl",e.toString(),null);
        }
    }



    //已测
    @ApiOperation(value = "保存修改后的个人信息",notes = "<img src='"+FSS.qcgs+"xgxx.png' />将页面上所有参数传给后台进行保存")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value = "当前用户的token",required = true),
            @ApiImplicitParam(name="avatar",value = "头像",required = true),
            @ApiImplicitParam(name="nickName",value = "昵称",required = true),
            @ApiImplicitParam(name="age",value = "年龄",required = true),
            @ApiImplicitParam(name="tall",value = "身高",required = true),
            @ApiImplicitParam(name="weight",value = "体重",required = true),
            @ApiImplicitParam(name="meetingstart",value = "见面砖石",required = true),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "      avatar:头像,\n"  +
            "      nickName:昵称,\n" +
            "      meetingstart:见面需要的砖石,\n" +
            "      sex:性别;(0:男;1:女 没值显示选择性别的页面，有值不显示),\n" +
            "      ssoId:用户ID,\n" +
            "      token:登录状态的TOKEN,\n" +
            "      flag:用于判断女用户是否完善资料（1/要提示  2/不提示）,\n" +
            "\n" +"}"+
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "savaInfo",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg savaInfo(String token,Sso sso,SsoInfo ssoInfo) {
        if (Tool.isNull(token)) return ResultMsg.fail("token为空", null, null);
        Sso sso1 = identify2(token);
        if (sso1 == null) return ResultMsg.fail("token无效", token, null);
        sso.setUpdateTime(new DateTime());
        try {
            dao.selectBySQL("UPDATE t_sso_info a,\n" +
                    "t_sso b \n" +
                    "SET avatar = '"+sso.getAvatar()+"',\n" +
                    "nick_name = '"+sso.getNickName()+"',\n" +
                    "meetingstart = '"+sso.getMeetingstart()+"',\n" +
                    "a.update_time = NOW(),\n" +
                    "age = '"+ssoInfo.getAge()+"',\n" +
                    "tall = '"+ssoInfo.getTall()+"',\n" +
                    "weight = '"+ssoInfo.getWeight()+"',\n" +
                    "b.update_time = NOW() \n" +
                    "WHERE\n" +
                    "\ta.sso_id = b.sso_id \n" +
                    "\tAND a.sso_id = "+sso1.getSsoId());
            CacheKit.remove("CONSTANT",token);
        }catch (Exception e){
            e.printStackTrace();
            return ResultMsg.success("接口调用失败!", "500", null);
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("avatar",sso.getAvatar());
        result.put("nickName",sso.getNickName());
        result.put("meetingstart",sso.getMeetingstart());
        result.put("sex",sso1.getSex());
        result.put("ssoId",sso1.getSsoId());
        result.put("token",token);
        result.put("sex",sso1.getSex());
        if (!Tool.isNull(sso1.getBigAvatar()) || "0".equals(sso1.getSex())){
            result.put("flag","2");
        }else result.put("flag","1");
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), result);
    }


    //已测
    @ApiOperation(value = "获取所有个人优势标签",notes = "<img src='"+FSS.qcgs+"grys.png' />")
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n" +
            " \"tagList\": \"妹子封面图集合\" \n" +
            "    {\n" +
            "        \"id\":编号 \n" +
            "         \"name\": 名字 \n" +
            "         \"color\": 颜色 \n" +
            "        },\n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "getTagList",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg getTagList() {
        List<Advantage> advantages = advantageService.selectList(null);
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), advantages);
    }

    //已测
    @ApiOperation(value = "保存个人优势标签",notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value = "当前用户的token",required = true),
            @ApiImplicitParam(name="advantege",value = "个人优势的拼串 （格式 ： id:name:color,id:name:color  同一个标签属性用冒号隔开，不同标签用逗号隔开）",required = true),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "        },\n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "saveTags",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg saveTags(String token , String advantege) {
        if (Tool.isNull(token))return ResultMsg.success("token为空!", null, null);
        if (Tool.isNull(advantege))return ResultMsg.success("advantege为空!", null, null);
        Sso sso = identify2(token);
        if (sso ==null)return ResultMsg.success("token无效", null, null);
        sso.setUpdateTime(new DateTime());
        if ("no".equals(advantege)){
            dao.updateBySQL("UPDATE t_sso_info SET advantege = '' where sso_id =" + sso.getSsoId());
        }else {
            dao.updateBySQL("UPDATE t_sso_info SET advantege = '"+advantege+"' where sso_id =" + sso.getSsoId());
        }
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), null);
    }

    //已测
    @ApiOperation(value = "我的VIP",notes = "<img src='"+FSS.qcgs+"wdhy.png' />")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value = "当前用户的token",required = true),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "        \"vipType\":会员类型名 \n" +
            "        \"validDate\":剩余天数 \n" +
            "        \"startCount\":剩余砖石数量 (后台已做性别处理，男用户才返回该字段，女用户不返回)\n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "getMyVip",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg getMyVip(String token) {
        if (Tool.isNull(token))return ResultMsg.success("token为空!", null, null);
        Sso sso = identify2(token);
        if (sso ==null)return ResultMsg.success("token无效", null, null);
        HashMap<String, Object> arron = new HashMap<>();
        List<Map<String, Object>> maps = dao.selectBySQL("SELECT start as startCount,\n" +
                "IF\n" +
                "\t( a.end_time > now( ), valid_date, \"0\" ) AS validDate, \n" +
                "IF\n" +
                "\t( a.end_time > now( ), CONCAT( c.NAME, '会员' ), \"暂未开通VIP\" ) AS vipType \n" +
                "FROM\n" +
                "\tt_vip a\n" +
                "\tLEFT JOIN t_sso b ON a.sso_id = b.sso_id\n" +
                "\tLEFT JOIN t_vip_type c ON a.type_id = c.id \n" +
                "WHERE\n" +
                "\ta.sso_id =" + sso.getSsoId());
        arron.put("vipType","暂未开通VIP");
        arron.put("validDate","0");
        if ("0".equals(sso.getSex())){//男的才返回砖石数量
            arron.put("startCount","0");
        }else {
            if (!Tool.listIsNull(maps) && maps.get(0).containsKey("startCount")){
               maps.get(0).remove("startCount");
            }
        }

        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), !Tool.listIsNull(maps)?maps.get(0):arron);
    }

    //已测
    @ApiOperation(value = "我的砖石--充值余额",notes = "<img src='"+FSS.qcgs+"czye.png' />")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value = "当前用户的token",required = true),
            /*@ApiImplicitParam(name="date",value = "哪个年月的记录（格式： 2019-03）不传默认为所有记录 ",required = false),*/
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n" +
            "\"startNum\": 砖石余额 \n" +
            "\"days\": 剩余多少天 \n" +
            " \"recordList\": \"消费记录\" \n" +
            "    {\n" +
            "        \"title\":标题 \n" +
            "         \"time\": 时间 \n" +
            "         \"start\": 砖石数量 \n" +
            "        },\n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "getMyStart",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg getMyStart(String token) {
        if (Tool.isNull(token))return ResultMsg.success("token为空!", null, null);
        Sso sso = identify2(token);
        if (sso ==null)return ResultMsg.success("token无效", null, null);
        HashMap<String, Object> arron = new HashMap<>();
        List<Map<String, Object>> maps = dao.selectBySQL("SELECT `start`,valid_date as days from t_vip WHERE sso_id =" + sso.getSsoId());
        if (Tool.listIsNull(maps)){
            arron.put("startNum","0");
            arron.put("days","0");
        }else {
            arron.put("startNum",maps.get(0).get("start"));
            arron.put("days",maps.get(0).get("days"));
        }

        List<Map<String, Object>> mapss = new ArrayList<>();

        //封裝消费记录
        List<Map<String, Object>> maps1 = dao.selectBySQL("SELECT\n" +
                "\tCONCAT( '获取', nick_name, '联系方式' ) AS title,\n" +
                "\tDATE_FORMAT( a.create_time, '%Y-%m-%d %H:%i' ) AS time,\n" +

//                "\t'-50' AS start \n" +
                "\tconcat('-',start_count) AS start \n" +

                "FROM\n" +
                "\tt_start_record a,\n" +
                "\tt_sso b \n" +
                "WHERE\n" +
                "\ta.girl_id = b.sso_id \n" +
                "\tAND type = '1' \n" +
                "\tAND a.sso_id ="+sso.getSsoId());
        mapss.addAll(maps1);


        //封装邀请记录
        if (!Tool.isNull(sso.getSex()) && sso.getSex().equals("0")){
            List<Map<String, Object>> maps2 = dao.selectBySQL("SELECT\n" +
                    "\tCONCAT('邀请',b.nick_name) as title,\n" +
                    "\tDATE_FORMAT( a.create_time, '%Y-%m-%d %H:%i' ) AS time,\n" +

//                    "  '+50' as start\t\n" +
                    "\tconcat('+',start_count) AS start \n" +

                    "FROM\n" +
                    "\tt_start_record a,\n" +
                    "\tt_sso b \n" +
                    "WHERE\n" +
                    "\ta.sso_id = b.sso_id \n" +
                    "\tAND type = '2' \n" +
                    "\tAND a.girl_id =" + sso.getSsoId());
            mapss.addAll(maps2);
//            if (!Tool.listIsNull(maps1)){
//                mapss.addAll(maps1.size()-1,maps2);
//                sort(mapss);
//            }
        }else {
            List<Map<String, Object>> maps2 = dao.selectBySQL("SELECT\n" +
                    "\tCONCAT('邀请',c.nick_name) as title,\n" +
                    "\tDATE_FORMAT( a.create_time, '%Y-%m-%d %H:%i' ) AS time,\n" +

//                    "  '+50' as start\t\n" +
                    "\tconcat('+',money) AS start \n" +

                    "FROM\n" +
                    "\tt_sso_account_flow a,\n" +
                    "\tt_sso c \n" +
                    "WHERE\n" +
                    "\ta.come_from = c.sso_id\n" +
                    "\tAND a.sso_id =" + sso.getSsoId());
            if (!Tool.listIsNull(maps1)){
                mapss.addAll(maps1.size()-1,maps2);
                sort(mapss);
            }
        }
        arron.put("recordList",mapss);
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), arron);
    }
    private void  sort(List<Map<String, Object>> maps){
        for (int i=0;i<=maps.size()-2;i++){
            for (int j=i+1;j<maps.size();j++){
                boolean b = TimeUtilFoMe.compareDate2(maps.get(i).get("time").toString(), maps.get(j).get("time").toString());
                if (b==false){
                    Map<String, Object> map1 = maps.get(i);
                    Map<String, Object> map2 = maps.get(j);
                    maps.remove(i);
                    maps.remove(j-1);
                    maps.add(i,map2);
                    maps.add(j,map1);
                }
            }
        }
    }


    //已测
    @ApiOperation(value = "我的砖石--砖石收益",notes = "<img src='"+FSS.qcgs+"zssy.png' />")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value = "当前用户的token",required = true),
            /*@ApiImplicitParam(name="date",value = "哪个年月的记录（格式： 2019-03）不传默认为所有记录 ",required = false),*/
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n" +
            "\"startNum\": 收益总额 \n" +
            " \"recordList\": \"收益记录\" \n" +
            "    {\n" +
            "        \"title\":标题 \n" +
            "         \"time\": 时间 \n" +
            "         \"start\": 砖石数量 \n" +
            "        },\n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "getStarts",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg getStarts(String token/*,String date*/) {
        if (Tool.isNull(token))return ResultMsg.success("token为空!", null, null);
        Sso sso = identify2(token);
        if (sso ==null)return ResultMsg.success("token无效", null, null);
        HashMap<String, Object> arron = new HashMap<>();
        List<Map<String, Object>> maps = null;
        if (sso.getSex().equals("0")){
            maps = dao.selectBySQL("SELECT SUM(start_count) as start from t_start_record where type = '2' and girl_id =" + sso.getSsoId());
            List<Map<String, Object>> maps1 = dao.selectBySQL("SELECT\n" +
                    "\tCONCAT('邀请',b.nick_name) as title,\n" +
                    "\tDATE_FORMAT( a.create_time, '%Y-%m-%d %H:%i' ) AS time,\n" +

//                    "  '+50' as start\t\n" +
                    "  concat('+',start_count) as start\t\n" +

                    "FROM\n" +
                    "\tt_start_record a,\n" +
                    "\tt_sso b \n" +
                    "WHERE\n" +
                    "\ta.sso_id = b.sso_id \n" +
                    "\tAND type = '2' \n" +
                    "\tAND a.girl_id =" + sso.getSsoId());
            arron.put("recordList",maps1);
        }else {
            maps = dao.selectBySQL("SELECT useable_balance as start from t_sso_account WHERE sso_id ="+sso.getSsoId());
            List<Map<String, Object>> maps1 = dao.selectBySQL("SELECT\n" +
                    "\tCONCAT('邀请',c.nick_name) as title,\n" +
                    "\tDATE_FORMAT( a.create_time, '%Y-%m-%d %H:%i' ) AS time,\n" +

//                    "\t'+50' as start\n" +
                    "  concat('+',money) as start\t\n" +

                    "FROM\n" +
                    "\tt_sso_account_flow a,\n" +
                    "\tt_sso c \n" +
                    "WHERE\n" +
                    "\ta.come_from = c.sso_id\n" +
                    "\tAND a.sso_id =" + sso.getSsoId());
            arron.put("recordList",maps1);
        }
        if (Tool.listIsNull(maps)){
            arron.put("startNum","0");
        }else {
            arron.put("startNum",maps.get(0).get("start"));
        }
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), arron);
    }

    //已测
   /* @ApiOperation(value = "消费提醒",notes = "<img src='"+FSS.xgt+"userLogin.jpg' />")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value = "当前用户的token",required = true),
            @ApiImplicitParam(name="date",value = "哪个年月的记录（格式： 2019-03）不传默认为所有记录 ",required = false),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n" +
            " \"expensesRecordList\": \"消费记录\" \n" +
            "    {\n" +
            "        \"title\":标题 \n" +
            "         \"time\": 时间 \n" +
            "         \"start\": 砖石数量 \n" +
            "        },\n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "getExpensesRecord",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg getExpensesRecord(String token,String date) {
        if (Tool.isNull(token))return ResultMsg.success("token为空!", null, null);
        Sso sso = identify2(token);
        if (sso ==null)return ResultMsg.success("token无效", null, null);
        HashMap<String, Object> arron = new HashMap<>();
        //封装消费记录
        List<Map<String, Object>> expensesRecordList = dao.selectBySQL("SELECT\n" +
                "IF\n" +
                "\t( a.type = '1', CONCAT( '获取黔城用户', b.nick_name, '的联系方式' ), CONCAT( '打赏黔城用户', b.nick_name ) ) AS title,\n" +
                "\ta.create_time as time,\n" +
                "\ta.start_count \n" +
                "FROM\n" +
                "\tt_start_record a\n" +
                "\tLEFT JOIN t_sso b ON a.girl_id = b.sso_id \n" +
                "WHERE\n" +
                "\t"+(Tool.isNull(date)?"":" a.create_time LIKE" + "'%" + date + "%'\n" ) +
                "\tAND a.sso_id =  \n" + sso.getSsoId()+"\n" +
                "ORDER BY\n" +
                "\ta.create_time DESC");
        arron.put("expensesRecordList",expensesRecordList);
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), arron);
    }*/


    //消息中心首页  已测
    @ApiOperation(value = "消息中心首页",notes = "<img src='"+FSS.qcgs+"xx.png' />消息中心首页")
    @ApiImplicitParams({@ApiImplicitParam(name="token",value = "当前用户的token",required = true)})
    @ApiResponses(@ApiResponse(code = 200, message = "data:[\n" +
            "\n" +
            "    {\n" +
            "         \"title\": 标题 \n" +
            "         \"time\": 时间 \n" +
            "         \"look\": 是否有红点 （1/有  0/没有） \n" +
            "         \"content\": 内容 \n" +
            "        },\n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "messageIndex",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg messageIndex(String token) {
        if (Tool.isNull(token))return ResultMsg.success("token为空!", null, null);
        Sso sso = identify2(token);
        if (sso ==null)return ResultMsg.success("token无效", null, null);
        ArrayList<Map<String, Object>> resultList = new ArrayList<>();
        //封装我的消息
        List<Map<String, Object>> myMessageList = dao.selectBySQL("SELECT\n" +
                "\t'我的消息' AS title,\n" +
                "\tIFNULL(content,'')content,\n" +
                "IF\n" +
                "\t( COUNT( CASE WHEN look = '0' THEN type END ) > 0, 1, 0 ) look,\n" +
                "IFNULL(IF\n" +
                "\t(\n" +
                "\tdate( a.create_time ) = curdate( ),\n" +
                "\tDATE_FORMAT( a.create_time, '%H:%i' ),\n" +
                "IF\n" +
                "\t(\n" +
                "\tto_days( now( ) ) - to_days( a.create_time ) = 1,\n" +
                "\t'昨天',\n" +
                "\t(\n" +
                "IF\n" +
                "\t(\n" +
                "\tYEAR ( a.create_time ) = YEAR ( NOW( ) ),\n" +
                "\tDATE_FORMAT( a.create_time, '%m-%d' ),\n" +
                "\tDATE_FORMAT( a.create_time, '%Y-%m-%d' ) \n" +
                "\t) \n" +
                "\t) \n" +
                "\t) \n" +
                "\t),'') time \n" +
                "FROM\n" +
                "\tt_message a \n" +
                "WHERE\n" +
                "\ta.message_sso_id =" + sso.getSsoId()+"\n" +
                "\tORDER BY create_time DESC \n");
        if (Tool.listIsNull(myMessageList)){
            HashMap<String, Object> map = new HashMap<>();
            map.put("title","我的消息");
            map.put("look","0");
            map.put("time","");
            map.put("content","");
            resultList.add(map);
        }else resultList.add(myMessageList.get(0));

        //封装系统消息(黔城团队)
        List<Map<String, Object>> sysMessageList = dao.selectBySQL("SELECT\n" +
                "\t'黔城团队' AS title,\n" +
                "\tIFNULL(content,'')content,\n" +
                "IF\n" +
                "\t(sso_ids_for_sys is not null,FIND_IN_SET('"+sso.getSsoId()+"',sso_ids_for_sys)<1,1) look,\n" +
                "IFNULL(IF\n" +
                "\t(\n" +
                "\tdate( a.create_time ) = curdate( ),\n" +
                "\tDATE_FORMAT( a.create_time, '%H:%i' ),\n" +
                "IF\n" +
                "\t(\n" +
                "\tto_days( now( ) ) - to_days( a.create_time ) = 1,\n" +
                "\t'昨天',\n" +
                "\t(\n" +
                "IF\n" +
                "\t(\n" +
                "\tYEAR ( a.create_time ) = YEAR ( NOW( ) ),\n" +
                "\tDATE_FORMAT( a.create_time, '%m-%d' ),\n" +
                "\tDATE_FORMAT( a.create_time, '%Y-%m-%d' ) \n" +
                "\t) \n" +
                "\t) \n" +
                "\t) \n" +
                "\t),'') time \n" +
                "FROM\n" +
                "\tt_message a \n" +
                "WHERE\n" +
                "\ta.message_sso_id IN ("+sso.getSex()+",2)\n" +
                "\tORDER BY create_time DESC");
        if (Tool.listIsNull(sysMessageList)){
            HashMap<String, Object> map = new HashMap<>();
            map.put("title","黔城团队");
            map.put("look","0");
            map.put("time","");
            map.put("content","");
            resultList.add(map);
        }else resultList.add(sysMessageList.get(0));
//        System.out.println(token);
//        System.out.println(JSONArray.fromObject(resultList));
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), resultList);
    }


    //已测
    @ApiOperation(value = "消息---我的消息",notes = "")
    @ApiImplicitParams({@ApiImplicitParam(name="token",value = "当前用户的token",required = true)})
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n"  +
            "    {\n" +
            "         \"time\": 时间 \n" +
            "         \"content\": 内容 \n" +
            "         \"id\": 编号 \n" +
            "         \"look\": 是否被查看过   0/没被查看  1/被查看 \n" +
            "        },\n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "getMyMessages",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg getMyMessages(String token) {
        if (Tool.isNull(token))return ResultMsg.success("token为空!", null, null);
        Sso sso = identify2(token);
        if (sso ==null)return ResultMsg.success("token无效", null, null);
        //封装我的消息集合
        List<Map<String, Object>> messageList = dao.selectBySQL("SELECT id,content,look," +
                "IF\n" +
                "\t(\n" +
                "\tdate( a.create_time ) = curdate( ),\n" +
                "\tDATE_FORMAT( a.create_time, '%H:%i' ),\n" +
                "IF\n" +
                "\t(\n" +
                "\tto_days( now( ) ) - to_days( a.create_time ) = 1,\n" +
                "\t'昨天',\n" +
                "\t(\n" +
                "IF\n" +
                "\t(\n" +
                "\tYEAR ( a.create_time ) = YEAR ( NOW( ) ),\n" +
                "\tDATE_FORMAT( a.create_time, '%m-%d' ),\n" +
                "\tDATE_FORMAT( a.create_time, '%Y-%m-%d' ) \n" +
                "\t) \n" +
                "\t) \n" +
                "\t) \n" +
                "\t) time \n" +
                "FROM t_message a where a.message_sso_id =" + sso.getSsoId()+
                "\t order by create_time\n");

        StringBuffer sb = new StringBuffer();
        messageList.stream().forEach(m -> {
            if ("0".equals(m.get("look").toString())){
                sb.append(m.get("id")+",");
            }
        });
        String temp = sb+"";
        if (!Tool.isNull(temp)){
            String ids = temp.substring(0,temp.length()-1);
            //将所有关于我的消息设置成已读
            dao.updateBySQL("UPDATE t_message SET look='1' WHERE id in ("+ids+")");
        }
        List<Map<String, Object>> messageList2 = new ArrayList<>();
        for (int i=messageList.size()-1;i>=0;i--){
            messageList2.add(messageList.get(i));
        }
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), messageList2);
    }

    //已测
    @ApiOperation(value = "消息---黔城团队",notes = "")
    @ApiImplicitParams({@ApiImplicitParam(name="token",value = "当前用户的token",required = true)})
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n" +
            "    {\n" +
            "         \"time\": 时间 \n" +
            "         \"content\": 内容 \n" +
            "         \"id\": 编号 \n" +
            "         \"look\": 是否被查看过   0/没被查看  1/被查看 \n" +
            "        },\n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "getOfficialMessages",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg getOfficialMessages(String token) {
        if (Tool.isNull(token))return ResultMsg.success("token为空!", null, null);
        Sso sso = identify2(token);
        if (sso ==null)return ResultMsg.success("token无效", null, null);
        List<Map<String, Object>> messageList = new ArrayList<>();
        //封装黔城团队消息集合
        messageList =  dao.selectBySQL("SELECT\n" +
                "\tid,\n" +
                "\tcontent,\n" +
                "\tIF(sso_ids_for_sys is not null,FIND_IN_SET('"+sso.getSsoId()+"',sso_ids_for_sys)>0,0) look,\n" +
                "\tIF\n" +
                "\t(\n" +
                "\tdate( a.create_time ) = curdate( ),\n" +
                "\tDATE_FORMAT( a.create_time, '%H:%i' ),\n" +
                "IF\n" +
                "\t(\n" +
                "\tto_days( now( ) ) - to_days( a.create_time ) = 1,\n" +
                "\t'昨天',\n" +
                "\t(\n" +
                "IF\n" +
                "\t(\n" +
                "\tYEAR ( a.create_time ) = YEAR ( NOW( ) ),\n" +
                "\tDATE_FORMAT( a.create_time, '%m-%d' ),\n" +
                "\tDATE_FORMAT( a.create_time, '%Y-%m-%d' ) \n" +
                "\t) \n" +
                "\t) \n" +
                "\t) \n" +
                "\t) time \n" +
                "FROM\n" +
                "\tt_message a \n" +
                "WHERE\n" +
                "\ta.message_sso_id IN ("+ sso.getSex() +",2)"+
                "\t order by create_time\n");

        StringBuffer sb = new StringBuffer();
        messageList.stream().forEach(m -> {
            if ("0".equals(m.get("look").toString())){
                sb.append(m.get("id")+",");
            }
        });
        String temp = sb+"";
        if (!Tool.isNull(temp)){
            String ids = temp.substring(0,temp.length()-1);
            //设置为已读
            synchronized (this){
                dao.updateBySQL("UPDATE t_message SET sso_ids_for_sys=if(sso_ids_for_sys IS NULL OR sso_ids_for_sys = '','" + sso.getSsoId() + "',CONCAT( sso_ids_for_sys, '," + sso.getSsoId() + "' )) WHERE id in ("+ ids +") and IF(sso_ids_for_sys is not null,FIND_IN_SET('"+sso.getSsoId()+"',sso_ids_for_sys),0)=0");
            }
        }
        List<Map<String, Object>> messageList2 = new ArrayList<>();
        for (int i=messageList.size()-1;i>0;i--){
            messageList2.add(messageList.get(i));
        }
//        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), messageList2);
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), messageList);
    }

    //已测
    @ApiOperation(value = "提现",notes = "<img src='"+FSS.qcgs+"zssy.png' />")
    @ApiImplicitParams({@ApiImplicitParam(name="token",value = "当前用户的token",required = true)})
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "withdraw",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg withdraw(String token) {
        if (Tool.isNull(token))return ResultMsg.success("token为空!", null, null);
        Sso sso = identify2(token);
        if (sso ==null)return ResultMsg.success("token无效", null, null);
        SsoAccount ssoAccount = ssoAccountService.selectOne(new EntityWrapper<SsoAccount>().eq("sso_id", sso.getSsoId()));
        if (ssoAccount!=null && ssoAccount.getUseableBalance() !=0.0 && ssoAccount.getUseableBalance() != 0){
            //创建消息
            Message message = new Message();
            message.setContent("您的提现申请已提交,总金额为 "+ssoAccount.getUseableBalance()+" ,三个工作日内管理员将与您联系！");
            message.setCreateTime(new DateTime());
            message.setLook("0");
            message.setSsoId(0);
            message.setMessageSsoId(sso.getSsoId()+"");
            message.setType("2");
            message.setOfficialMessageType("1");
            messageService.insert(message);

            //发短信
            try {
                Setting setting = settingConfiguration.getSetting();
                String responseStr = new DuanXin_LiJun( setting.getYpAppkey(), "3104484").sendAllSms(setting.getTel(),ssoAccount.getUseableBalance(),sso.getNickName(),sso.getPhone());
                System.out.println(responseStr);
                System.out.println(responseStr);
                System.out.println(responseStr);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //金额清零
            dao.updateBySQL("UPDATE t_sso_account a SET useable_balance=0.0,a.status='0' where sso_id =" + sso.getSsoId());
        }
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), null);
    }

    //已测
    @ApiOperation(value = "邀请好友--立即邀请",notes = "<img src='"+FSS.qcgs+"yqsy.png' />")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value = "当前用户的token",required = true),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n" +
            "\"title\":标题 \n" +
            "\"intro\": 内容 \n" +
            "\"icon\": 缩略图 \n" +
            "\"url\": 注册页面的url\n" +
            "\"}\n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "share",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg share(String token) {
        if (Tool.isNull(token))return ResultMsg.success("token为空!", null, null);
        Sso sso = identify2(token);
        if (sso ==null)return ResultMsg.success("token无效", null, null);
        HashMap<String, Object> arron = new HashMap<>();
        arron.put("title","《黔城故事》");
        arron.put("intro","黔城故事交友是一个在线交友的平台，这里聚集了很多的帅哥靓女，等你来撩哦!");
        arron.put("icon","app的图标");
        arron.put("url","http://"+Tool.getDomain()+"/ssoApi/login?uid="+sso.getSsoId());
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), arron);
    }

    /**
     * 所有分享出去后的H5页面,点击后跳转的注册页面
     * @return
     */
    @RequestMapping("login")
    public String login(Model model,HttpSession session){
        //给这个页面加一个发送短信次数
        if (Tool.isNull(session.getAttribute("sms_count")))session.setAttribute("sms_count",4);
        //微信从聊天记录(私聊,群聊)里分享链接到朋友圈没有缩略图简介标题解决方式--李俊
        Tool.putWX_config(model, settingService);
        return "/html5/login.html";
    }

    //已测
    @ApiOperation(value = "关于黔城",notes = "统一跳转到协议详情页")
    @RequestMapping(value = "aboutUs",method = RequestMethod.GET)
    public String aboutUs(Model model) {
        List<PlatformIntro> intros = introService.selectList(null);
        if (Tool.listIsNull(intros)) return "thisIsNotFound.html";
        HashMap<String, Object> map = new HashMap<>();
        if (!Tool.listIsNull(intros)){
            map.put("content",intros.get(0).getIntro());
            model.addAttribute("info",map);
        }
        model.addAttribute("title","关于黔城");
        return "/html5/agreementInfo.html";
    }

    //已测
    @ApiOperation(value = "退出登录",notes = "<img src='"+FSS.qcgs+"tcdl.png' />")
    @ApiImplicitParams({@ApiImplicitParam(name="token",value = "当前用户的token",required = true)})
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "loginOut",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg loginOut(String token) {
        if (Tool.isNull(token))return ResultMsg.success("token为空!", null, null);
        Sso sso = identify2(token);
        if (sso ==null)return ResultMsg.success("token无效", null, null);
        CacheKit.remove("CONSTANT",token);
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), null );
    }

    //已测
    @ApiOperation(value = "主页三个提示参数接口",notes = "/>")
    @ApiImplicitParams({@ApiImplicitParam(name="token",value = "当前用户的token",required = true)})
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "      flag:用于判断女用户是否完善资料（1/要提示  2/不提示）,\n" +
            "      wori:颜值认证提示（1/要提示  2/不提示）,\n" +
            "      remind:有值(该值为剩余天数)就提醒，没值不提醒（用于首页VIP到期弹框提醒）,\n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "getParams",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg getParams(String token) {
        if (Tool.isNull(token))return ResultMsg.success("token为空!", null, null);
        Sso sso = identify2(token);
        if (sso ==null)return ResultMsg.success("token无效", null, null);
        SsoInfo ssoInfo = ssoInfoService.selectOne(new EntityWrapper<SsoInfo>().eq("sso_id", sso.getSsoId()));
        HashMap<Object, Object> ssoMap = new HashMap<>();
        if ( "0".equals(sso.getSex()) || (!Tool.isNull(sso.getBigAvatar()) && !sso.getAvatar().equals("http://www.qcy2019.com:7777/static/api_img/womenAvatar.jpg") && !Tool.isNull(ssoInfo.getAdvantege()))){
            ssoMap.put("flag","2");
        }else ssoMap.put("flag","1");
        Vip vip = vipService.selectOne(new EntityWrapper<Vip>().eq("sso_id", sso.getSsoId()));
        if (vip==null){
            ssoMap.put("wori","1");
            ssoMap.put("remind","");
        }else{
            int remind = Integer.parseInt(settingConfiguration.getSetting().getRemind());
            int validDate = vip.getValidDate();
            if (validDate <= remind && vip.getStatus()==1){
                ssoMap.put("remind",vip.getValidDate());
            }else {
                ssoMap.put("remind","");
            }
            ssoMap.put("wori","2");
        }
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), ssoMap );
    }


    //已测
    @ApiOperation(value = "我的---判断是否有发布动态的权利 以及 获取官方电话 和 是否VIP（头像右下角的勾勾）",notes = "<img src='"+FSS.qcgs+"fbzp.png' />")
    @ApiImplicitParams({@ApiImplicitParam(name="token",value = "当前用户的token",required = true)})
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "  \"is_Vip\": \"是否Vip（1/是 2/不是）\",\n" +
            "  \"haveRight\": \"是否有权利（yes/有 no/没有）\",\n" +
            "  \"servicePhone\": \"官方电话\",\n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "publishRight",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg publishRight(String token) {
        if (Tool.isNull(token))return ResultMsg.success("token为空!", null, null);
        Sso sso = identify2(token);
        if (sso ==null)return ResultMsg.success("token无效", null, null);
        HashMap<String, Object> arron = new HashMap<>();
        Setting setting = settingConfiguration.getSetting();
        if ("0".equals(setting.getPublishSwitch())){
            arron.put("haveRight","no");
        }else {
            arron.put("haveRight","yes");
            if (sso.getState().equals("封号")){
                arron.put("haveRight","no");
            }
        }
        List<Map<String, Object>> maps = dao.selectBySQL("SELECT id FROM t_vip WHERE sso_id = " + sso.getSsoId() + " AND end_time > NOW( )");
        if (!Tool.listIsNull(maps)) arron.put("is_Vip","1");
        else arron.put("is_Vip","2");
        arron.put("servicePhone",setting.getServicePhone());
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), arron);
    }

    //已测
    @RequestMapping(value = "check",method = RequestMethod.POST)
    @ResponseBody
    public Object check(String token) {
        if (Tool.isNull(token))return ResultMsg.success("token无效", null, null);
        String[] split = token.split(":");
        if (split.length<1)return ResultMsg.success("token无效", null, null);
        SuperSso superSso = superSsoService.selectOne(new EntityWrapper<SuperSso>().eq("name", split[0]).eq("phone",split[1]));
         if (superSso == null)return ResultMsg.success("token无效", null, null);;
        HashMap<String, Object> arron = new HashMap<>();
        List<Map<String, Object>> bigAvatorList = dao.selectBySQL("SELECT big_avatar as imgUrl,phone FROM t_sso where check_big_avatar = '0' AND big_avatar IS NOT NULL and big_avatar != '' AND sex = '1' ORDER BY create_time ASC");
        arron.put("bigAvatorList",bigAvatorList);
        List<Map<String, Object>> mediaList = dao.selectBySQL("SELECT a.id,a.type,oss_object_name as url,thumb from  t_media a LEFT JOIN t_sso_work b ON a.base_id = b.base_id WHERE a.`check`='0' ");
        arron.put("mediaList",mediaList);
        return arron;
    }

    @RequestMapping(value = "getAllMedia",method = RequestMethod.POST)
    @ResponseBody
    public Object getAllMedia(String param,String token) {
        if (Tool.isNull(token))return ResultMsg.success("token无效", null, null);
        String[] split1 = token.split(":");
        if (split1.length<1)return ResultMsg.success("token无效", null, null);
        SuperSso superSso = superSsoService.selectOne(new EntityWrapper<SuperSso>().eq("name", split1[0]).eq("phone",split1[1]));
        if (superSso == null)return ResultMsg.success("token无效", null, null);
        if (Tool.isNull(param))return ResultMsg.success("param为空!", null, null);
        String[] split = param.split(":");
        List<Map<String, Object>> maps = null;
        if (split[0].equals("avator")){
            maps = dao.selectBySQL("SELECT c.type,c.oss_object_name as imgUrl,b.thumb from t_sso a,t_sso_work b,t_media c WHERE a.sso_id = b.sso_id and b.base_id = c.base_id and a.big_avatar = " + "'" + split[2]+":"+split[3] +":"+split[4] + "'");
        }else {
            maps = dao.selectBySQL("SELECT c.type,c.oss_object_name as imgUrl,b.thumb from t_sso a,t_sso_work b,t_media c WHERE a.sso_id = b.sso_id and b.base_id = c.base_id and a.sso_id = (SELECT DISTINCT sso_id from t_media a , t_sso_work b WHERE a.base_id = b.base_id and a.id = "+split[2]+")");
        }
        if (Tool.listIsNull(maps) && split[0].equals("avator")){
            HashMap<String, Object> arron = new HashMap<>();
            arron.put("type","0");
            arron.put("imgUrl",split[2]+":"+split[3]+":"+split[4]);
            arron.put("thumb","");
            maps.add(arron);
        }
        return maps;
    }

    @RequestMapping(value = "getTotalPage",method = RequestMethod.POST)
    @ResponseBody
    public Object getTotalPage(String token) {
        if (!"8450997833b6475a85e3049ad6c00f24".equals(token)){
            return "非法访问";
        }
        List<Map<String, Object>> maps = dao.selectBySQL("SELECT DISTINCT\n" +
                "\ta.id\n" +
                "FROM\n" +
                "\tt_invited a\n" +
                "\tLEFT JOIN t_vip c ON a.be_sso_id = c.sso_id\n" +
                "\tLEFT JOIN t_sso f ON a.sso_id = f.sso_id \n" +
                "WHERE\n" +
                "\tc.id IS NOT NULL \n" +
                "\tAND f.sex = '1' ");
        return maps.size();
    }

    @RequestMapping(value = "invitedList",method = RequestMethod.POST)
    @ResponseBody
    public Object invitedList(String token,int pageNo,String limit) {
        if (Tool.isNull(token))return ResultMsg.success("token无效", null, null);
        String[] split = token.split(":");
        if (split.length<1)return ResultMsg.success("token无效", null, null);
        SuperSso superSso = superSsoService.selectOne(new EntityWrapper<SuperSso>().eq("name", split[0]).eq("phone",split[1]));
        if (superSso == null)return ResultMsg.success("token无效", null, null);
        int index = (11*pageNo)-11;
        List<Map<String, Object>> maps = dao.selectBySQL("SELECT DISTINCT\n" +
                "\ta.id,\n" +
                "\tf.nick_name AS ssoId,\n" +
                "  ( SELECT SUM( pay_money ) FROM t_order WHERE sso_id = a.sso_id AND state = '1' ) AS ssoSpend,\n" +
                "\t( SELECT SUM( pay_money ) FROM t_order WHERE sso_id = a.be_sso_id AND state = '1' ) AS beSsoSpend,\n" +
                "IF\n" +
                "\t( d.sex = '0', '男', '女' ) AS `sex`,\n" +
                "IF\n" +
                "\t( a.`status` = 0, '未返现', '已返现' ) AS `status` \n" +
                "FROM\n" +
                "\tt_invited a\n" +
                "\tLEFT JOIN t_sso d ON a.be_sso_id = d.sso_id\n" +
                "\tLEFT JOIN t_vip c ON a.be_sso_id = c.sso_id\n" +
                "\tLEFT JOIN t_sso f ON a.sso_id = f.sso_id \n" +
                "WHERE\n" +
                "\tf.sex = '1' and c.id IS NOT NULL  \n" +
                "ORDER BY\n" +
                "\t`status` DESC \n" +
                "\tLIMIT "+index+",11");
        if (!Tool.listIsNull(maps)){
            maps = maps.stream().filter(m -> (m.containsKey("beSsoSpend"))).collect(Collectors.toList());
            maps.stream().forEach(m -> {
                if (!m.containsKey("ssoSpend")){
                    m.put("ssoSpend",'0');
                }
            });
        }
        return maps;
    }


    @RequestMapping(value = "/giveMoney")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Object cleanMoney(@RequestParam Integer id,String token) {
        if (Tool.isNull(token))return ResultMsg.success("token无效", null, null);
        String[] split = token.split(":");
        if (split.length<1)return ResultMsg.success("token无效", null, null);
        SuperSso superSso = superSsoService.selectOne(new EntityWrapper<SuperSso>().eq("name", split[0]).eq("phone",split[1]));
        if (superSso == null)return ResultMsg.success("token无效", null, null);
        Invited invited = invitedService.selectById(id);
//        Vip vip = vipService.selectOne(new EntityWrapper<Vip>().eq("sso_id", invited.getBeSsoId()));
        List<Order> orders = orderService.selectList(new EntityWrapper<Order>().eq("sso_id", invited.getBeSsoId()));
        Integer  money = settingConfiguration.getSetting().getMoneyGiveWomen();

        //开启事务
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("giveMoney");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            if (invited.getStatus().equals("0") && !Tool.listIsNull(orders)){
                //返现
                dao.updateBySQL("UPDATE t_sso_account a LEFT JOIN t_invited b ON a.sso_id = b.sso_id SET a.useable_balance = a.useable_balance +  "+money+"  WHERE b.id ="+id);
                //创建账户流水
                SsoAccountFlow ssoAccountFlow = new SsoAccountFlow();
                ssoAccountFlow.setMoney((double)money);
                ssoAccountFlow.setNote("邀请返现");
                ssoAccountFlow.setBusinessType("0");
                ssoAccountFlow.setBusinessName("返现");
                ssoAccountFlow.setComeFrom(invited.getBeSsoId());
                ssoAccountFlow.setSsoId(Integer.parseInt(invited.getSsoId()));
                ssoAccountFlow.setCreateTime(new DateTime());
                accountFlowService.insert(ssoAccountFlow);
                //创建消息
                Message message = new Message();
                message.setContent("您邀请ID为"+invited.getBeSsoId()+"的用户成功！返现"+money+"￥，请查收！");
                message.setCreateTime(new DateTime());
                message.setLook("0");
                message.setSsoId(0);
                message.setMessageSsoId(invited.getSsoId());
                message.setType("2");
                message.setOfficialMessageType("4");
                messageService.insert(message);
                invited.setStatus("1");
                invitedService.updateById(invited);
            }else if (invited.getStatus().equals("1")){
                //取消返现
                dao.updateBySQL("UPDATE t_sso_account a LEFT JOIN t_invited b ON a.sso_id = b.sso_id SET a.useable_balance = a.useable_balance -  "+money+"  WHERE b.id ="+id);
                //创建账户流水
                SsoAccountFlow ssoAccountFlow = new SsoAccountFlow();
                ssoAccountFlow.setMoney((double)money);
                ssoAccountFlow.setNote("取消返现");
                ssoAccountFlow.setBusinessType("2");
                ssoAccountFlow.setBusinessName("取消返现");
                ssoAccountFlow.setComeFrom(invited.getBeSsoId());
                ssoAccountFlow.setSsoId(Integer.parseInt(invited.getSsoId()));
                ssoAccountFlow.setCreateTime(new DateTime());
                accountFlowService.insert(ssoAccountFlow);
                //创建消息
                Message message = new Message();
                message.setContent("您邀请ID为"+invited.getBeSsoId()+"用户的邀请返现金额被管理员取消！");
                message.setCreateTime(new DateTime());
                message.setLook("0");
                message.setSsoId(0);
                message.setMessageSsoId(invited.getSsoId());
                message.setType("2");
                message.setOfficialMessageType("4");
                messageService.insert(message);
                //更新返现状态
                invited.setStatus("0");
                invitedService.updateById(invited);
            }
        }catch (Exception e){
            e.printStackTrace();
            transactionManager.rollback(status);
        }
        transactionManager.commit(status);
        return "200";
    }


    //已测
   /* @ApiOperation(value = "审查作品和封面是否通过",notes = "<img src='"+FSS.xgt+"userLogin.jpg' />")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value = "当前用户的token",required = true),
            @ApiImplicitParam(name="key",value = "作品或者封面图的标识",required = true),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))*/
    @RequestMapping(value = "checkPassOrNo",method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Object checkPassOrNo(String token,String key) {
        if (Tool.isNull(token))return ResultMsg.success("token无效", null, null);
        String[] split1 = token.split(":");
        if (split1.length<1)return ResultMsg.success("token无效", null, null);
        SuperSso superSso = superSsoService.selectOne(new EntityWrapper<SuperSso>().eq("name", split1[0]).eq("phone",split1[1]));
        if (superSso == null)return ResultMsg.success("token无效", null, null);
        if (Tool.isNull(key))return ResultMsg.success("key为空!", null, null);

        //开启事务
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("checkPassOrNo");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            List<Map<String, Object>> maps = new ArrayList<>();
            String[] split = key.split(":");
            if(!split[1].equals("3")){
                if (split[0].equals("media")){
                    dao.updateBySQL("update t_media a SET a.`check` = '"+ split[1] +"' WHERE id = '" + split[2]+"'");
                }else {
                    dao.updateBySQL("update t_sso a set check_big_avatar = '"+ split[1] +"' where big_avatar = '" + split[2]+":"+split[3]+":"+split[4]+"'");
                    maps =  dao.selectBySQL("select sso_id,phone,nick_name from t_sso where big_avatar ='" + split[2]+":"+split[3]+":"+split[4]+"'");
                    //发短信通知
                    if (!Tool.listIsNull(maps)){
                        String str=new DuanXin_LiJun(settingConfiguration.getSetting().getYpAppkey(),"3152696").sendAllSms(maps.get(0).get("phone").toString(),maps.get(0).get("nick_name").toString());
                    }
                    //创建消息
                    Message message = new Message();
                    message.setLook("0");
                    message.setOfficialMessageType("4");
                    message.setSsoId(0);
                    message.setMessageSsoId(maps.get(0).get("sso_id")+"");
                    message.setType("0");
                    message.setContent("恭喜！您的封面图已审核通过！");
                    message.setCreateTime(new DateTime());
                    messageService.insert(message);
                }
            }else {
                //封号逻辑
                if (split[0].equals("media")){
                    dao.updateBySQL("UPDATE t_media a LEFT JOIN t_sso_work b ON a.base_id = b.base_id LEFT JOIN t_sso c ON b.sso_id = c.sso_id SET c.state = '封号' WHERE a.id ="+split[2]);
                    //屏蔽所有作品的信息
                    List<Map<String, Object>> maps1 = dao.selectBySQL("SELECT\n" +
                            "\tbase_id \n" +
                            "FROM\n" +
                            "\tt_sso_work \n" +
                            "WHERE\n" +
                            "\tsso_id = ( SELECT DISTINCT sso_id FROM t_media a LEFT JOIN t_sso_work b ON a.base_id = b.base_id WHERE a.id = "+split[2]+")");
                    StringBuffer buffer = new StringBuffer("(");
                    for (Map<String, Object> m :maps1){
                        buffer.append(m.get("base_id")+",");
                    }
                    buffer.deleteCharAt(buffer.length()-1);
                    String str = buffer + ")";
                    dao.updateBySQL("UPDATE t_media \n" +
                            "SET t_media.`check` = '2' \n" +
                            "WHERE\n" +
                            "\tbase_id IN \n" +
                            "\n" +str+
                            "\t\n" +
                            "\t");
                }else {
                    dao.updateBySQL("update t_sso a set state = '封号' where big_avatar = '" + split[2]+":"+split[3]+"'");
                    dao.updateBySQL("UPDATE t_sso a LEFT JOIN t_sso_work b on a.sso_id = b.sso_id LEFT JOIN t_media c ON b.base_id = c.base_id set c.`check` = '2' WHERE a.big_avatar = '" + split[2]+":"+split[3]+"'");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            transactionManager.rollback(status);
            return  "500";
        }
        transactionManager.commit(status);
        return  "200";
    }

    //已测
/*    @ApiOperation(value = "获取帯头像的二维码",notes = "<img src='"+FSS.xgt+"userLogin.jpg' />")
    @ApiImplicitParams({@ApiImplicitParam(name="token",value = "当前用户的token",required = true)})
    @RequestMapping(value = "getAvatorCodeImg",method = RequestMethod.POST)
    @ResponseBody
    public void getAvatorCodeImg(HttpServletResponse response,String token) {
            Sso sso = identify2(token);
            String QRcode = "http://baidu.com";
            if(!Tool.isNull(sso.getAvatar())){
                PosterUtil.getLogoCodeImg(QRcode,sso.getAvatar(),response);
            }
    }*/


    @ApiOperation(value = "协议详情页",notes = "跳转协议详情H5")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true,name="id",value = "协议id （4/用户协议 6/隐私政策 7/提现规则 8/邀请好友协议 9/奖励规则 10/排行榜规则）")
    })
    @ApiResponses(@ApiResponse(code=200,message=""))
    @RequestMapping(value = "agreementDetail",method = RequestMethod.GET)
    public String agreement_detail(Integer id,Model model){
        List<Map<String, Object>> infos = dao.selectBySQL("select content from t_agreement where id=" + id);
        if (Tool.listIsNull(infos)) return "thisIsNotFound.html";
        Map<String, Object> info = infos.get(0);
        model.addAttribute("info",info);
        if ("4".equals(id+"")){
            model.addAttribute("title","用户协议");
        }else if ("6".equals(id+"")){
            model.addAttribute("title","隐私政策");
        }else if ("7".equals(id+"")){
            model.addAttribute("title","提现规则");
        }else if ("8".equals(id+"")){
            model.addAttribute("title","邀请好友协议");
        }else if ("9".equals(id+"")){
            model.addAttribute("title","奖励规则");
        }else if ("10".equals(id+"")){
            model.addAttribute("title","排行榜规则");
        }
        return "/html5/agreementInfo.html";
    }

    //已测
    @ApiOperation(value = "邀请好友首页",notes = "<img src='"+FSS.qcgs+"yqsy.png' />")
    @ApiImplicitParams({@ApiImplicitParam(name="token",value = "当前用户的token",required = true)})
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "  \"count\": \"邀请的人数\",\n" +
            "  \"startCount\": \"获得砖石数\",\n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "inviteFriend",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg inviteFriend(String token) {
        if (Tool.isNull(token))return ResultMsg.success("token为空!", null, null);
        Sso sso = identify2(token);
        if (sso ==null)return ResultMsg.success("token无效", null, null);
        HashMap<String, Object> arron = new HashMap<>();
        List<Map<String, Object>> maps =null;
        if (sso.getSex().equals("0")){
            maps = dao.selectBySQL("SELECT SUM(start_count) AS count FROM t_start_record WHERE type = '2' AND girl_id = " + sso.getSsoId());
        }else {
            maps =  dao.selectBySQL("SELECT useable_balance AS count FROM t_sso_account  WHERE sso_id ="+sso.getSsoId());
        }
        if (Tool.listIsNull(maps)){
            arron.put("startCount","0");
        }else {
            arron.put("startCount",maps.get(0).get("count"));
        }
        List<Map<String, Object>> mapss = dao.selectBySQL("SELECT COUNT(1) as count from t_invited a  WHERE  a.sso_id =" + sso.getSsoId());
        arron.put("count",mapss.get(0).get("count"));
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), arron);
    }


    @ApiOperation(value = "邀请记录",notes = "<img src='"+FSS.qcgs+"yqjl.png' />")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value = "当前用户的token",required = true),
            @ApiImplicitParam(name = "pageNo", value = "分页页码 不传默认为0,第一次进页面可不传,数据需要分页需要使用", required = false),
            @ApiImplicitParam(name = "pageSize", value = "分页页大小 不传默认为20,数据需要分页需要使用", required = false),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n" +
            "  \"startCount\": \"总获得多少砖石或钱\",\n" +
            " \"registeMap\": \"邀请注册\" \n" +
            " {\n" +
            "  \"count\": \"邀请注册总人数\",\n" +
            "  \"dateList\": \"用户集合\" \n" +
            "    [\n" +
            "    {\n" +
            "         \"avatar\": 头像 \n" +
            "         \"nickName\": 昵称 \n" +
            "         \"phone\": 手机号 \n" +
            "         \"time\": 时间 \n" +
            "     },\n" +
            "  ],\n" +
            "  },\n" +
            "  },\n" +
            " \"spendMap\": \"购买会员\" \n" +
            " {\n" +
            "  \"count\": \"成为会员的总人数\",\n" +
            "  \"spendDateList\": \"用户集合\" \n" +
            "    [\n" +
            "    {\n" +
            "         \"avatar\": 头像 \n" +
            "         \"nickName\": 昵称 \n" +
            "         \"phone\": 手机号 \n" +
            "         \"time\": 时间 \n" +
            "     },\n" +
            "  },\n" +
            "  ],\n" +
            "  },\n" +
            "  ],\n" +
            "  \"detail\": \"200\",\n" +
            "  \"message\": \"调用接口成功!\",\n" +
            "  \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "invitedRecord",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg invitedRecord(@RequestParam(name = "pageSize", defaultValue = "20", required = false) Integer pageSize,
                                   @RequestParam(name = "pageNo", defaultValue = "0", required = false) Integer pageNo,
                                   String token) {
        if (Tool.isNull(token))return ResultMsg.success("token为空!", null, null);
        Sso sso = identify2(token);
        if (sso ==null)return ResultMsg.success("token无效", null, null);
        HashMap<String, Object> arron = new HashMap<>();
        //封装总共获得多少砖石或者钱
        List<Map<String, Object>> maps =null;
        if (sso.getSex().equals("0")){
            maps = dao.selectBySQL("SELECT SUM(start_count) AS count FROM t_start_record WHERE type = '2' AND girl_id = " + sso.getSsoId());
        }else {
            maps =  dao.selectBySQL("SELECT SUM(money) AS count FROM t_sso_account_flow  WHERE business_type = '0' and sso_id ="+sso.getSsoId());
        }
        if (Tool.listIsNull(maps)){
            arron.put("startCount","0");
        }else {
            arron.put("startCount",maps.get(0).get("count"));
        }

        //封装registeMap
        HashMap<String, Object> registeMap = new HashMap<>();
        List<Map<String, Object>> registeList = dao.selectBySQL("SELECT avatar,nick_name as nickName,phone,DATE_FORMAT( a.create_time, '%Y-%m-%d' )as time from t_invited a,t_sso b WHERE a.be_sso_id = b.sso_id AND a.sso_id =" + sso.getSsoId()+" order by a.create_time desc"+ " limit " + pageNo * pageSize + "," + pageSize + "");
        List<Map<String, Object>> count = dao.selectBySQL("SELECT COUNT(1) as count from t_invited WHERE sso_id =" + sso.getSsoId());
        //dealPhone(registeList);//处理电话号码
        registeMap.put("count",count.get(0).get("count"));
        registeMap.put("dateList",registeList);
        arron.put("registeMap",registeMap);


        //封装spendMap
        HashMap<String, Object> spendMap = new HashMap<>();
        List<Map<String, Object>> spendList = dao.selectBySQL("SELECT avatar,nick_name as nickName,phone,DATE_FORMAT( a.create_time, '%Y-%m-%d' ) as time from t_invited a,t_sso b,t_vip c WHERE a.be_sso_id = b.sso_id AND b.sso_id = c.sso_id and a.sso_id ="+sso.getSsoId()+" order by a.create_time desc"+ " limit " + pageNo * pageSize + "," + pageSize + "");
        List<Map<String, Object>> count3 = dao.selectBySQL("SELECT COUNT(1) as count from t_invited a,t_vip c WHERE a.be_sso_id = c.sso_id and a.sso_id ="+sso.getSsoId());
        //dealPhone(spendList);//处理电话号码
        spendMap.put("count",count3.get(0).get("count"));
        spendMap.put("spendDateList",spendList);

        arron.put("spendMap",spendMap);
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), arron);
    }

    //处理电话号码
    public void dealPhone(List<Map<String, Object>> maps){
        if (!Tool.listIsNull(maps)) {
            for (Map<String, Object> m :maps){
                String phone = m.get("phone").toString();
                String phonePre = phone.substring(0, 3);
                String phoneLast = phone.substring(7, 11);
                String in = "****";
                String newPhone = phonePre+in+phoneLast;
                m.put("phone",newPhone);
            }
        }
    }

    public void deleteTime(List<Map<String, Object>> maps){
        if (!Tool.listIsNull(maps)) {
            for (Map<String, Object> m :maps){
                m.remove("time");
            }
        }
    }

//    @Autowired
//    RedisService redisTemplateService;
//    //已测
//    @ApiOperation(value = "后台统一测试接口",notes = "<img src='"+FSS.qcgs+"grys.png' />")
//    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
//            "\n" +
//            " \"url\": \"资源访问路径\" \n" +
//            "  ],\n" +
//            "  \"detail\": \"200\",\n" +
//            "  \"message\": \"调用接口成功!\",\n" +
//            "  \"success\": \"ok\"\n" +
//            "}"))
//    @RequestMapping(value = "test",method = RequestMethod.POST)
//    @ResponseBody
//    public ResultMsg test(HttpServletRequest request) {
//
//        HashMap<String, Object> map = new HashMap<>();
////        User user = new User();
////        user.setId(11);
////        user.setName("test");
////        user.setPassword("hello redis");
////        redisTemplateService.set("key1",user);
////        User us = redisTemplateService.get("key1",User.class);
////        map.put("name","test");
////        map.put("password","111");
////        //redisTemplateService.set("key1",map);
////        redisTemplateService.hmSet("info","name","哈哈哈");
////        redisTemplateService.hmSet("info","password","111");
////        String name = redisTemplateService.hmGet("info", "name")+"";
////        String password = redisTemplateService.hmGet("info", "password")+"";
////        System.out.println(name+":"+password);
//        return ResultMsg.success("调用成功！",HttpStatus.OK.toString(),name+":"+password);
//    }

}


