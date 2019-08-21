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
 * 用户支付流水
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-05
 */
@TableName("t_sso_account_flow")
public class SsoAccountFlow extends Model<SsoAccountFlow> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户ID
     */
    @TableField("sso_id")
    private Integer ssoId;
    /**
     * 消费来源--0/官方--1/用户
     */
    @TableField("come_from")
    private String comeFrom;
    /**
     * 金额
     */
    private Double money;
    /**
     * 剩余可用金额
     */
    @TableField("avilable_balance")
    private Integer avilableBalance;
    /**
     * 剩余冻结金额
     */
    @TableField("frozen_balance")
    private Integer frozenBalance;
    /**
     * 业务类型--0/打赏--1/提现
     */
    @TableField("business_type")
    private String businessType;
    /**
     * 业务名
     */
    @TableField("business_name")
    private String businessName;
    /**
     * 备注
     */
    private String note;
    @TableField("create_time")
    private Date createTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSsoId() {
        return ssoId;
    }

    public void setSsoId(Integer ssoId) {
        this.ssoId = ssoId;
    }

    public String getComeFrom() {
        return comeFrom;
    }

    public void setComeFrom(String comeFrom) {
        this.comeFrom = comeFrom;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Integer getAvilableBalance() {
        return avilableBalance;
    }

    public void setAvilableBalance(Integer avilableBalance) {
        this.avilableBalance = avilableBalance;
    }

    public Integer getFrozenBalance() {
        return frozenBalance;
    }

    public void setFrozenBalance(Integer frozenBalance) {
        this.frozenBalance = frozenBalance;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
        return "SsoAccountFlow{" +
        "id=" + id +
        ", ssoId=" + ssoId +
        ", comeFrom=" + comeFrom +
        ", money=" + money +
        ", avilableBalance=" + avilableBalance +
        ", frozenBalance=" + frozenBalance +
        ", businessType=" + businessType +
        ", businessName=" + businessName +
        ", note=" + note +
        ", createTime=" + createTime +
        "}";
    }
}
