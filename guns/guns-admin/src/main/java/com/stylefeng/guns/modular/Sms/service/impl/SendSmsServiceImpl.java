package com.stylefeng.guns.modular.Sms.service.impl;

import com.stylefeng.guns.modular.system.model.SendSms;
import com.stylefeng.guns.modular.system.dao.SendSmsMapper;
import com.stylefeng.guns.modular.Sms.service.ISendSmsService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 发送短信记录表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-05
 */
@Service
public class SendSmsServiceImpl extends ServiceImpl<SendSmsMapper, SendSms> implements ISendSmsService {

}
