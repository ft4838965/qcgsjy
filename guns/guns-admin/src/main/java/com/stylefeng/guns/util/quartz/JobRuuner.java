package com.stylefeng.guns.util.quartz;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.modular.Sms.service.ISendSmsService;
import com.stylefeng.guns.modular.message.service.IMessageService;
import com.stylefeng.guns.modular.quartzjob.service.IQuartzJobService;
import com.stylefeng.guns.modular.system.dao.Dao;
import com.stylefeng.guns.modular.system.model.Message;
import com.stylefeng.guns.modular.system.model.QuartzJob;
import com.stylefeng.guns.modular.system.model.SendSms;
import com.stylefeng.guns.modular.system.model.Setting;
import com.stylefeng.guns.modular.vip.service.IVipService;
import com.stylefeng.guns.util.DuanXin_LiJun;
import com.stylefeng.guns.util.SettingConfiguration;
import com.stylefeng.guns.util.Tool;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;



@Service
public class JobRuuner {

	private static SettingConfiguration settingConfiguration;


	private static IMessageService messageService;

	private static IQuartzJobService quartzJobService;

	private static SchedulerFactoryBean factoryBean;

	private static ISendSmsService sendSmsService;

	private static IVipService vipService;

	private static Dao dao;

	@Autowired
	public void setSendSmsService(ISendSmsService sendSmsService) {
		JobRuuner.sendSmsService = sendSmsService;
	}

	@Autowired
	public void setSchedulerFactoryBean(SchedulerFactoryBean factoryBean) {
		JobRuuner.factoryBean = factoryBean;
	}

	@Autowired
	public void setQuartzJobService(IQuartzJobService quartzJobService) {
		JobRuuner.quartzJobService = quartzJobService;
	}

	@Autowired
	public void setSettingConfiguration(SettingConfiguration settingConfiguration) {
		JobRuuner.settingConfiguration = settingConfiguration;
	}

	@Autowired
	public void setQuartzJobService(IVipService vipService) {
		JobRuuner.vipService = vipService;
	}

	@Autowired
	public void setMessageService(IMessageService messageService) {
		JobRuuner.messageService = messageService;
	}

	@Autowired
	public void setMessageService(Dao dao) {
		JobRuuner.dao = dao;
	}

	public static void vipEndRemand(String phone,String ssoId){
		String randomCode= Tool.getRandomNum(6);
		System.err.println("--------------------------------------------------------------------");
		System.err.println("--------------------------------------------------------------------");
		System.err.println("--------------------------------------------------------------------");
		System.err.println("--------------------------------------------------------------------");
		System.err.println("--------------------------------------------------------------------");
		System.err.println("--------------------------------------------------------------------");
		System.err.println("--------------------------------------------------------------------");
		System.err.println("--------------------------------------------------------------------");
		System.err.println("--------------------------------------------------------------------");
		System.err.println("--------------------------------------------------------------------");
		Setting setting=settingConfiguration.getSetting();
		try {
			//要根据性别发不同的短信,所以这里要查询
			List<Map<String,Object>>ssos=dao.selectBySQL("select sso.sex,concat(IFNULL(vip.`start`,0),'钻石')`start`,IFNULL(concat(DATEDIFF(vip.end_time,'"+new SimpleDateFormat("yyyy-MM-dd").format(new DateTime()) +"'),'天'),'不久')days from t_sso sso LEFT JOIN t_vip vip ON vip.sso_id=sso.sso_id where sso.sso_id="+ssoId);
			// 发短信
			String responseStr = Tool.listIsNull(ssos)||Tool.isNull(ssos.get(0).get("sex"))||!new ArrayList<>(Arrays.asList("0","1")).contains(ssos.get(0).get("sex"))
			?"用户不存在，或者用户性别不对":"0".equals(ssos.get(0).get("sex"))//0男 1女
					?new DuanXin_LiJun( setting.getYpAppkey(), "2925796").sendAllSms(phone,ssos.get(0).get("days"),ssos.get(0).get("start")):new DuanXin_LiJun( setting.getYpAppkey(), "3156592").sendAllSms(phone,ssos.get(0).get("days"));
			//设置过期状态
			dao.selectBySQL("update t_vip set status=2 where sso_id = "+ssoId);

			//创建消息
			Message message = new Message();
			message.setLook("0");
			message.setOfficialMessageType("4");
			message.setSsoId(0);
			message.setMessageSsoId(ssoId);
			message.setType("0");
			message.setContent("尊敬的会员，您的会员期限即将到期，请尽快充值，谢谢！");
			message.setCreateTime(new Date());
			messageService.insert(message);

			//删除数据库记录
			quartzJobService.delete(new EntityWrapper<QuartzJob>().eq("job_name", phone));
			//删除缓存任务
			Scheduler sche = factoryBean.getScheduler();
			QuartzUtils.removeJob(sche, phone);

			//创建短信记录
			SendSms sendSms = new SendSms();
			sendSms.setPhone(phone);
			sendSms.setType("1");
			sendSms.setCreateTime(new DateTime());
			sendSmsService.insert(sendSms);

		} catch (IOException e) {
			e.printStackTrace();
			//删除缓存
			Scheduler sche = factoryBean.getScheduler();
			QuartzUtils.removeJob(sche, phone);
		}
	}

	public static void vipAddStart(){

	}

}
