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
 * 用户账户
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-02
 */
@TableName("t_sso_account")
public class SsoAccount extends Model<SsoAccount> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户ID
     */
    @TableField("sso_id")
    private Integer ssoId;
    /**
     * 可用金额
     */
    @TableField("useable_balance")
    private Double useableBalance;
    /**
     * 冻结金额
     */
    @TableField("frozen_balance")
    private Double frozenBalance;
    /**
     * 支付密码
     */
    @TableField("pay_password")
    private String payPassword;
    /**
     * 状态
     */
    private String status;
    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;


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

    public Double getUseableBalance() {
        return useableBalance;
    }

    public void setUseableBalance(Double useableBalance) {
        this.useableBalance = useableBalance;
    }

    public Double getFrozenBalance() {
        return frozenBalance;
    }

    public void setFrozenBalance(Double frozenBalance) {
        this.frozenBalance = frozenBalance;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SsoAccount{" +
        "id=" + id +
        ", ssoId=" + ssoId +
        ", useableBalance=" + useableBalance +
        ", frozenBalance=" + frozenBalance +
        ", payPassword=" + payPassword +
        ", status=" + status +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
