package com.stylefeng.guns.modular.account.service.impl;

import com.stylefeng.guns.modular.system.model.SsoAccount;
import com.stylefeng.guns.modular.system.dao.SsoAccountMapper;
import com.stylefeng.guns.modular.account.service.ISsoAccountService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户账户 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-02
 */
@Service
public class SsoAccountServiceImpl extends ServiceImpl<SsoAccountMapper, SsoAccount> implements ISsoAccountService {

}
