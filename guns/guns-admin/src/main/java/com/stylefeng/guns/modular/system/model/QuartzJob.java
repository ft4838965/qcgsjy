package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 定时任务表
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-26
 */
@TableName("t_quartz_job")
public class QuartzJob extends Model<QuartzJob> {

    private static final long serialVersionUID = 1L;

    /**
     * 任务编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 任务名称
     */
    @TableField("job_name")
    private String jobName;
    /**
     * 执行时间
     */
    private Date fireDate;
    /**
     * 表达式
     */
    private String corn;
    /**
     * 任务类型
     */
    private String type;
    /**
     * 是否执行
     */
    @TableField("is_fire")
    private String isFire;
    /**
     * 参数
     */
    private String params;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Date getFireDate() {
        return fireDate;
    }

    public void setFireDate(Date fireDate) {
        this.fireDate = fireDate;
    }

    public String getCorn() {
        return corn;
    }

    public void setCorn(String corn) {
        this.corn = corn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsFire() {
        return isFire;
    }

    public void setIsFire(String isFire) {
        this.isFire = isFire;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "QuartzJob{" +
        "id=" + id +
        ", jobName=" + jobName +
        ", fireDate=" + fireDate +
        ", corn=" + corn +
        ", type=" + type +
        ", isFire=" + isFire +
        ", params=" + params +
        ", createTime=" + createTime +
        "}";
    }
}
