package com.stylefeng.guns.util.quartz;



/**
 * 统一定时任务服务
 * @author nixianhua
 *
 */
public interface QuartzService {
	/**
	 * 添加定时任务
	 * @param info 任务信息
	 */
	void addJob(QuartzJobInfo info);

	/**
	 * 删除定时任务
	 * @param string
	 */
	void delJob(String string);

}
