package com.stylefeng.guns.modular.works.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.modular.media.service.IMediaService;
import com.stylefeng.guns.modular.message.service.IMessageService;
import com.stylefeng.guns.modular.system.dao.Dao;
import com.stylefeng.guns.modular.system.model.Media;
import com.stylefeng.guns.modular.system.model.Message;
import com.stylefeng.guns.util.ResultMsg;
import com.stylefeng.guns.util.Tool;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.modular.system.model.SsoWork;
import com.stylefeng.guns.modular.works.service.ISsoWorkService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作品管理控制器
 *
 * @author fengshuonan
 * @Date 2019-03-04 14:33:20
 */
@Controller
@RequestMapping("/ssoWork")
public class
SsoWorkController extends BaseController {

    private String PREFIX = "/works/ssoWork/";

    @Autowired
    private ISsoWorkService ssoWorkService;

    @Autowired
    private IMediaService mediaService;

    @Autowired
    private IMessageService messageService;

    /**
     * 跳转到作品管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "ssoWork.html";
    }

    /**
     * 跳转到添加作品管理
     */
    @RequestMapping("/ssoWork_add")
    public String ssoWorkAdd() {
        return PREFIX + "ssoWork_add.html";
    }

    /**
     * 跳转到修改作品管理
     */
    @RequestMapping("/ssoWork_update/{ssoWorkId}")
    public String ssoWorkUpdate(@PathVariable Integer ssoWorkId, Model model) {
        SsoWork ssoWork = ssoWorkService.selectById(ssoWorkId);
        model.addAttribute("item",ssoWork);
        /**
         *获取当前的视频集合
         */
        EntityWrapper<Media> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("base_id", ssoWork.getBaseId());
        List<Media> pictures = mediaService.selectList(entityWrapper);
        List<Map<String, Object>> videoArray = new ArrayList<>();
        JSONObject mapJson = null;
        if (pictures.size() > 0) {
            for (int i = 0; i < pictures.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("key", (pictures.get(i).getOssObjectName()));
                // map.put("key", ("当前视频转码中请稍候!"));
                mapJson = new JSONObject(map);
                videoArray.add(mapJson);
                }

            }
          model.addAttribute("videoArray", videoArray);

            if("0".equals(ssoWork.getCheck())){
                ssoWork.setCheck("未审查");
            }else if ("1".equals(ssoWork.getCheck())){
                ssoWork.setCheck("已通过");
            }else if ("2".equals(ssoWork.getCheck())){
                ssoWork.setCheck("未通过");
            }

            if("1".equals(ssoWork.getType())){
                ssoWork.setType("图文");
            }else ssoWork.setType("视频");

        LogObjectHolder.me().set(ssoWork);
        return PREFIX + "ssoWork_edit.html";
    }

    @Autowired
    private Dao dao;

    /**
     * 审核
     *
     * @param type 通过还是拒绝
     * @return 审核结果
     */
    @RequestMapping(value = "/passOrNo")
    @ResponseBody
    public boolean checkVideo(String type,Integer id) {
        try {
//           String sql =  "update t_sso_work a set a.check = "+"'"+type+"'"+" where id =" + id;
//            dao.updateBySQL(sql);

            SsoWork ssoWork = ssoWorkService.selectById(id);
            String sql =  "UPDATE t_media a SET a.`check` = '"+type+"' WHERE base_id = " + ssoWork.getBaseId();
            dao.updateBySQL(sql);

          //创建消息告知用户
          Message message = new Message();
            if(type.equals("1")){
                message.setContent("您发布的内容已通过审核");
            }else message.setContent("您发布的内容未通过审核");;
            message.setCreateTime(new DateTime());
            message.setLook("0");
            message.setSsoId(0);
            message.setMessageSsoId(ssoWork.getSsoId());
            message.setType("0");
            message.setOfficialMessageType("4");
            messageService.insert(message);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @GetMapping(value = "/img")
    @ResponseBody
    public ResponseEntity<?> getUser(String baseId) {
        ResultMsg resultMsg = new ResultMsg();
        StringBuffer listObject = new StringBuffer();
        try {

            if (baseId == "") {

            } else {
                EntityWrapper<Media> entityWrapper = new EntityWrapper<>();
                entityWrapper.like("base_id", baseId);
                List<Media> picture = mediaService.selectList(entityWrapper);


                for (int i = 0; i < picture.size(); i++) {
                    listObject.append("," + picture.get(i).getOssObjectName());
                }

            }

            resultMsg = ResultMsg.success("查询成功", "查询成功", listObject);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ResultMsg>(ResultMsg.fail("系统错误", "系统错误", ""), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<ResultMsg>(resultMsg, HttpStatus.OK);

    }

    /**
     * 获取作品管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        EntityWrapper<SsoWork> wrapper = new EntityWrapper<>();
        if(!Tool.isNull(condition) && condition.equals("未审查的作品")){
            wrapper.eq("t_sso_work.check","0");
        }
        List<SsoWork> ssoWorkList = ssoWorkService.selectList(wrapper);
        for (SsoWork sw : ssoWorkList){
            if("0".equals(sw.getCheck())){
                sw.setCheck("未审查");
            }else if ("1".equals(sw.getCheck())){
                sw.setCheck("已通过");
            }else if ("2".equals(sw.getCheck())){
                sw.setCheck("未通过");
            }

            if("1".equals(sw.getType())){
                sw.setType("图文");
            }else sw.setType("视频");
        }
        return ssoWorkList;
    }

    /**
     * 新增作品管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(SsoWork ssoWork) {
        ssoWork.setCreateTime(new DateTime());
        ssoWorkService.insert(ssoWork);
        return SUCCESS_TIP;
    }

    /**
     * 删除作品管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer ssoWorkId) {
        ssoWorkService.deleteById(ssoWorkId);
        return SUCCESS_TIP;
    }

    /**
     * 修改作品管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(SsoWork ssoWork) {
        ssoWorkService.updateById(ssoWork);
        return SUCCESS_TIP;
    }

    /**
     * 作品管理详情
     */
    @RequestMapping(value = "/detail/{ssoWorkId}")
    @ResponseBody
    public Object detail(@PathVariable("ssoWorkId") Integer ssoWorkId) {
        return ssoWorkService.selectById(ssoWorkId);
    }
}
