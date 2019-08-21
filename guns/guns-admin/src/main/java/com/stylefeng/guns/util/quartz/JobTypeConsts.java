package com.stylefeng.guns.util.quartz;


/**
 * 定时任务类型
 * @author nixianhua
 *
 */
public interface JobTypeConsts {

    //会员到期短信提醒
	public static byte VIP_END_JOB  =  0;

	//会员每天增加砖石任务
	public static byte VIP_START_ADD_JOB  =  1;

}
