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
 * 订单表
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-26
 */
@TableName("t_order")
public class Order extends Model<Order> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 登录账号
     */
    @TableField("sso_id")
    private Integer ssoId;
    /**
     * 订单号
     */
    @TableField("order_sn")
    private String orderSn;
    /**
     * 留言
     */
    private String leaveword;
    /**
     * 状态（0/待支付  1/已支付 ）
     */
    private String state;
    /**
     * 实付金额
     */
    @TableField("pay_money")
    private Integer payMoney;
    /**
     * 支付方式
     */
    @TableField("pay_channel")
    private String payChannel;
    /**
     * 支付时间
     */
    @TableField("pay_time")
    private Date payTime;
    /**
     * 来源拼串
     */
    @TableField("order_from")
    private String orderFrom;
    /**
     * 交易完成时间
     */
    @TableField("finished_time")
    private Date finishedTime;
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

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getLeaveword() {
        return leaveword;
    }

    public void setLeaveword(String leaveword) {
        this.leaveword = leaveword;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(Integer payMoney) {
        this.payMoney = payMoney;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getOrderFrom() {
        return orderFrom;
    }

    public void setOrderFrom(String orderFrom) {
        this.orderFrom = orderFrom;
    }

    public Date getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(Date finishedTime) {
        this.finishedTime = finishedTime;
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
        return "Order{" +
        "id=" + id +
        ", ssoId=" + ssoId +
        ", orderSn=" + orderSn +
        ", leaveword=" + leaveword +
        ", state=" + state +
        ", payMoney=" + payMoney +
        ", payChannel=" + payChannel +
        ", payTime=" + payTime +
        ", orderFrom=" + orderFrom +
        ", finishedTime=" + finishedTime +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
