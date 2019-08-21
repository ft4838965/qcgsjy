package com.stylefeng.guns.util.quartz;

import com.stylefeng.guns.modular.system.dao.Dao;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class MyJob2 implements Job {

	@Autowired
	private Dao dao;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		dao.updateBySQL("UPDATE t_vip SET valid_date = valid_date-1 WHERE  now( ) < end_time and valid_date > 0");
	}


}
