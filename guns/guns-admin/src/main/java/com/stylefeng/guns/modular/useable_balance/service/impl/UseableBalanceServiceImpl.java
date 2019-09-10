package com.stylefeng.guns.modular.useable_balance.service.impl;

import com.stylefeng.guns.modular.system.model.UseableBalance;
import com.stylefeng.guns.modular.system.dao.UseableBalanceMapper;
import com.stylefeng.guns.modular.useable_balance.service.IUseableBalanceService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户提现记录(用于下账) 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-08-22
 */
@Service
public class UseableBalanceServiceImpl extends ServiceImpl<UseableBalanceMapper, UseableBalance> implements IUseableBalanceService {

}
