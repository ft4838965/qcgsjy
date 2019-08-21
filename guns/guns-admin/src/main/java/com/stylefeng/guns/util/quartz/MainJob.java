package com.stylefeng.guns.util.quartz;

import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MainJob implements Job {

	@Override
	public void execute(JobExecutionContext context) {
		System.err.println("[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[");
		System.err.println("[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[");
		System.err.println("[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[");
		System.err.println("[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[");
		System.err.println("[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[");
		System.err.println("[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[");
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		QuartzJobInfo info = (QuartzJobInfo) jobDataMap.get("params");
		byte type = info.getType();
		Map<String, Object> params = info.getParams();
		String phone = params.get("phone")+"";
		String ssoId = params.get("ssoId")+"";
		switch (type) {
		case JobTypeConsts.VIP_END_JOB:
			JobRuuner.vipEndRemand(phone,ssoId);
			break;
		case JobTypeConsts.VIP_START_ADD_JOB:
			JobRuuner.vipAddStart();
			break;
		default:
			break;
		}

	}

}
