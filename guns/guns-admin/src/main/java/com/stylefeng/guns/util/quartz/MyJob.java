package com.stylefeng.guns.util.quartz;

import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.modular.quartzjob.service.IQuartzJobService;
import com.stylefeng.guns.modular.system.model.QuartzJob;
import com.stylefeng.guns.util.Tool;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyJob implements Job {

	@Autowired
	private IQuartzJobService quartzJobService;

	@Autowired
	SchedulerFactoryBean factoryBean;

	@Autowired
	private QuartzService quartzService;

	@Override
	public void execute(JobExecutionContext context){
		System.err.println("**************************************************");
		System.err.println("**************************************************");
		System.err.println("**************************************************");
		System.err.println("**************************************************");
		System.err.println("**************************************************");
		System.err.println("**************************************************");
		System.err.println("**************************************************");
		//监测会员到期提醒
		List<QuartzJob> jobs = quartzJobService.selectList(null);
		if (!Tool.listIsNull(jobs)){
			for (QuartzJob job :jobs){
				long fireTime = job.getFireDate().getTime();
				Date now = new Date();
				long nowTime = now.getTime();

				if (fireTime<nowTime){
					QuartzJobInfo preHandleInfo = new QuartzJobInfo();
					HashMap<String,Object> hashMap = new HashMap<>();
					String map = job.getParams();
					Map params = JSON.parseObject(map);
					hashMap.put("phone", params.get("phone"));
					hashMap.put("ssoId",params.get("ssoId"));
					//job执行需要的参数
					preHandleInfo.setParams(hashMap);
					//job类型 用于标识是哪个任务
					preHandleInfo.setType(JobTypeConsts.VIP_END_JOB);
					//job名称
					preHandleInfo.setJobName(job.getJobName());
					//设置活动执行时间
					Long time = 1 * 1 * 10 * 1000L;
					Long end = now.getTime() + time;
					preHandleInfo.setFireDate(new Date(end));
					Scheduler sche = factoryBean.getScheduler();
					QuartzUtils.addJob(sche, preHandleInfo.getJobName(), MainJob.class, preHandleInfo, preHandleInfo.getCronj());
				}else {
					QuartzJobInfo preHandleInfo = new QuartzJobInfo();
					HashMap<String,Object> hashMap = new HashMap<>();
					String map = job.getParams();
					Map params = JSON.parseObject(map);
					hashMap.put("phone", params.get("phone"));
					hashMap.put("ssoId",params.get("ssoId"));
					//job执行需要的参数
					preHandleInfo.setParams(hashMap);
					//job类型 用于标识是哪个任务
					preHandleInfo.setType(JobTypeConsts.VIP_END_JOB);
					//job名称
					preHandleInfo.setJobName(job.getJobName());
					//设置活动执行时间
					preHandleInfo.setFireDate(job.getFireDate());
					Scheduler sche = factoryBean.getScheduler();
					QuartzUtils.addJob(sche, preHandleInfo.getJobName(), MainJob.class, preHandleInfo, preHandleInfo.getCronj());
				}
		    }
		}
	}

}
