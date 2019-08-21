package com.stylefeng.guns.modular.setting.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.util.FSS;
import com.stylefeng.guns.util.Tool;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.Setting;
import com.stylefeng.guns.modular.setting.service.ISettingService;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 全局配置控制器
 *
 * @author fengshuonan
 * @Date 2019-02-28 10:31:38
 */
@Controller
@RequestMapping("/setting")
public class SettingController extends BaseController {

    private String PREFIX = "/setting/setting/";

    @Autowired
    private ISettingService settingService;

    /**
     * 跳转到全局配置首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "setting.html";
    }

    @RequestMapping("/upAPK")
    @ResponseBody
    public String upAPK(@RequestParam("file") MultipartFile file) throws IOException {
        String url="未检测到任何上传文件";
        if(file!=null&&!file.isEmpty()){

            String filePath= Tool.getDomain().startsWith("localhost")||Tool.getDomain().startsWith("192.168.1.21")||Tool.getDomain().startsWith("192.168.1.28")? FSS.FILEPATHFILE_OFFLINE:FSS.FILEPATHFILE_ONLINE;
            File targetFile = new File(filePath);
            if(!targetFile.exists()){
                targetFile.mkdirs();
            }
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(filePath+file.getOriginalFilename());
                out.write(file.getBytes());
                out.flush();
                url="http://"+Tool.getDomain()+"/qcgu/"+file.getOriginalFilename();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
        return url;
    }

    /**
     * 跳转到添加全局配置
     */
    @RequestMapping("/setting_add")
    public String settingAdd() {
        return PREFIX + "setting_add.html";
    }

    /**
     * 跳转到修改全局配置
     */
    @RequestMapping("/setting_update/{settingId}")
    public String settingUpdate(@PathVariable Integer settingId, Model model) {
        Setting setting = settingService.selectById(settingId);
        model.addAttribute("item",setting);
        LogObjectHolder.me().set(setting);
        return PREFIX + "setting_edit.html";
    }

    /**
     * 获取全局配置列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return settingService.selectList(null);
    }

    /**
     * 新增全局配置
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Setting setting) {
        settingService.insert(setting);
        return SUCCESS_TIP;
    }

    /**
     * 删除全局配置
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer settingId) {
        settingService.deleteById(settingId);
        return SUCCESS_TIP;
    }

    /**
     * 修改全局配置
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Setting setting) {
        String x = setting.getPublishSwitch();
        settingService.updateById(setting);
        return SUCCESS_TIP;
    }

    /**
     * 全局配置详情
     */
    @RequestMapping(value = "/detail/{settingId}")
    @ResponseBody
    public Object detail(@PathVariable("settingId") Integer settingId) {
        return settingService.selectById(settingId);
    }
}
