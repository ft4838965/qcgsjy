package com.stylefeng.guns.modular.message.service.impl;

import com.stylefeng.guns.modular.system.model.Message;
import com.stylefeng.guns.modular.system.dao.MessageMapper;
import com.stylefeng.guns.modular.message.service.IMessageService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户消息中心 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-05
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

}
