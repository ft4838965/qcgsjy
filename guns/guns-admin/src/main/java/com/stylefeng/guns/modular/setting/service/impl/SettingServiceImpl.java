package com.stylefeng.guns.modular.setting.service.impl;

import com.stylefeng.guns.modular.system.model.Setting;
import com.stylefeng.guns.modular.system.dao.SettingMapper;
import com.stylefeng.guns.modular.setting.service.ISettingService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统设置表 服务实现类
 * </p>
 *
 * @author Arron
 * @since 2019-02-28
 */
@Service
public class SettingServiceImpl extends ServiceImpl<SettingMapper, Setting> implements ISettingService {

}
