package com.stylefeng.guns.util.quartz;

import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.modular.quartzjob.service.IQuartzJobService;
import com.stylefeng.guns.modular.system.model.QuartzJob;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;




@Service
public class QuartzServiceImpl implements QuartzService {


	@Autowired
	private SchedulerFactoryBean schedulerFactory;

	@Autowired
	private IQuartzJobService quartzJobService;

	@Override
	public void addJob(QuartzJobInfo info) {
		try {
			Scheduler sche = schedulerFactory.getScheduler();
			QuartzUtils.addJob(sche, info.getJobName(), MainJob.class, info, info.getCronj());
			//创建定时任务数据库记录
			QuartzJob quartzJob = new QuartzJob();
			quartzJob.setCorn(info.getCronj());
			quartzJob.setCreateTime(new DateTime());
			quartzJob.setFireDate(info.getFireDate());
			quartzJob.setIsFire("0");
			quartzJob.setType(JobTypeConsts.VIP_END_JOB+"");
			quartzJob.setJobName(info.getJobName());
			quartzJob.setParams(JSON.toJSONString(info.getParams()));
			quartzJobService.insert(quartzJob);
		} catch (Exception e) {
			e.printStackTrace();
	}
	}

	@Override
	public void delJob(String jobName) {
		try {
			Scheduler sche = schedulerFactory.getScheduler();
			QuartzUtils.removeJob(sche, jobName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
