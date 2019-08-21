/**
 * Copyright (C), 2019-2019, 贵州宏思锐达科技有限公司
 * FileName: AsyncTask
 * Author:   Arron-Wu
 * Date:     2019/3/3 22:34
 * Description: 异步执行的任务
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.stylefeng.guns.async;

import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.modular.message.service.IMessageService;
import com.stylefeng.guns.modular.ssoaccoun.service.ISsoAccountFlowService;
import com.stylefeng.guns.modular.system.dao.Dao;
import com.stylefeng.guns.modular.system.model.Message;
import com.stylefeng.guns.modular.system.model.SsoAccountFlow;
import com.stylefeng.guns.util.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈异步执行的任务〉
 *
 * @author Arron
 * @create 2019/3/3
 * @since 1.0.0
 */
@Component
public class AsyncTask {

    @Autowired
    private ISsoAccountFlowService accountFlowService;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private Dao dao;

    /**
     * 创建账户流水(批量)
     * @return
     */
    @Async
    public void creatAccountFlow() {
        List<Map<String, Object>> maps = dao.selectBySQL("SELECT a.sso_id,b.useable_balance FROM t_sso c LEFT JOIN t_sso_info a ON a.sso_id=c.sso_id LEFT JOIN t_sso_account b ON a.sso_id = b.sso_id where c.sex='1'");
        ArrayList<SsoAccountFlow> safs = new ArrayList<>();
        if (!Tool.listIsNull(maps)) {
            for (Map<String, Object> m : maps) {
                Double balance = (Double) m.get("useable_balance");
                Integer ssoId = (Integer) m.get("sso_id");
                SsoAccountFlow accountFlow = new SsoAccountFlow();
                accountFlow.setMoney(balance);
                accountFlow.setSsoId(ssoId);
                accountFlow.setBusinessType("1");
                accountFlow.setCreateTime(new DateTime());
                accountFlow.setBusinessName("结算");
                accountFlow.setComeFrom("0");
                safs.add(accountFlow);
            }
            dao.addSsoAccountFlows(safs);//动态sql之foreach实现批量插入
        }
    }
        /**
         * 创建消息（批量）
         * @return
         */
        @Async
        public void creatMessage (){
            List<Map<String, Object>> maps = dao.selectBySQL("SELECT a.sso_id FROM t_sso c LEFT JOIN t_sso_info a ON a.sso_id=c.sso_id LEFT JOIN t_sso_account b ON a.sso_id = b.sso_id where c.sex='1'");
            if (!Tool.listIsNull(maps)) {
                for (Map<String, Object> m : maps) {
                    Integer ssoId = (Integer) m.get("sso_id");
                    Message message = new Message();
                    message.setContent("您的提现申请已通过，官方已给您结算！");
                    message.setCreateTime(new DateTime());
                    message.setLook("0");
                    message.setSsoId(0);
                    message.setMessageSsoId(ssoId+"");
                    message.setType("0");
                    message.setOfficialMessageType("1");
                    messageService.insert(message);//优化用动态sql，，，因为项目小，这里就不做了
                }
            }
        }

}
