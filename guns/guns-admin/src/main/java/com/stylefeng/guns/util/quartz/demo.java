package com.stylefeng.guns.util.quartz;


/**
 * Created by Heyifan Cotter on 2019/4/18.
 */
public class demo {
    public static void main(String[] args) {
        // 设置定时VIP到期提醒
/*        QuartzJobInfo preHandleInfo = new QuartzJobInfo();
        HashMap<String,Object> hashMap = new HashMap<>();
        Sso sso = ssoService.selectOne(new EntityWrapper<Sso>().eq("sso_id", ssoId));
        hashMap.put("phone",sso.getPhone());
        hashMap.put("ssoId",ssoId);
        //job执行需要的参数
        preHandleInfo.setParams(hashMap);
        //job类型 用于标识是哪个任务
        preHandleInfo.setType(JobTypeConsts.VIP_END_JOB);
        //job名称
        preHandleInfo.setJobName(sso.getPhone());
        //设置活动执行时间
        Date now_ = new Date();
        Long time_ = (validDate-7) * 24 * 60 * 60 * 1000L;//会员提前7天
        Long remandTime = now_.getTime() + time_;
        preHandleInfo.setFireDate(new Date(remandTime));
        quartzService.addJob(preHandleInfo);*/
    }
}
