package com.stylefeng.guns.modular.system.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-01
 */
@TableName("t_vip_type")
public class VipType extends Model<VipType> {

    private static final long serialVersionUID = 1L;

    private Integer id;
    /**
     * 名字
     */
    private String name;
    /**
     * 原价
     */
    @TableField("origin_money")
    private Integer originMoney;
    /**
     * 实价
     */
    @TableField("real_money")
    private Integer realMoney;
    /**
     * 共送的砖量
     */
    @TableField("total_start")
    private Integer totalStart;
    /**
     * 每天送砖量
     */
    @TableField("day_start")
    private Integer dayStart;
    /**
     * 最高送
     */
    @TableField("top_start")
    private Integer topStart;
    @TableField("create_time")
    private Date createTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOriginMoney() {
        return originMoney;
    }

    public void setOriginMoney(Integer originMoney) {
        this.originMoney = originMoney;
    }

    public Integer getRealMoney() {
        return realMoney;
    }

    public void setRealMoney(Integer realMoney) {
        this.realMoney = realMoney;
    }

    public Integer getTotalStart() {
        return totalStart;
    }

    public void setTotalStart(Integer totalStart) {
        this.totalStart = totalStart;
    }

    public Integer getDayStart() {
        return dayStart;
    }

    public void setDayStart(Integer dayStart) {
        this.dayStart = dayStart;
    }

    public Integer getTopStart() {
        return topStart;
    }

    public void setTopStart(Integer topStart) {
        this.topStart = topStart;
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
        return "VipType{" +
        "id=" + id +
        ", name=" + name +
        ", originMoney=" + originMoney +
        ", realMoney=" + realMoney +
        ", totalStart=" + totalStart +
        ", dayStart=" + dayStart +
        ", topStart=" + topStart +
        ", createTime=" + createTime +
        "}";
    }
}
