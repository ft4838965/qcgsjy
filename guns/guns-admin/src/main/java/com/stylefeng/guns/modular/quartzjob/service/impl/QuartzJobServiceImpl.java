package com.stylefeng.guns.modular.quartzjob.service.impl;

import com.stylefeng.guns.modular.system.model.QuartzJob;
import com.stylefeng.guns.modular.system.dao.QuartzJobMapper;
import com.stylefeng.guns.modular.quartzjob.service.IQuartzJobService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 定时任务表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-26
 */
@Service
public class QuartzJobServiceImpl extends ServiceImpl<QuartzJobMapper, QuartzJob> implements IQuartzJobService {

}
