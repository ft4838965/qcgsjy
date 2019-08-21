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
 * 支付流水表
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-05
 */
@TableName("t_pay_bill")
public class PayBill extends Model<PayBill> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户ID
     */
    @TableField("sso_id")
    private Integer ssoId;
    /**
     * wechat:微信支付/ali:支付宝支付
     */
    @TableField("pay_type")
    private String payType;
    /**
     * 支付的表的订单号
     */
    @TableField("order_no")
    private String orderNo;
    /**
     * 微信/支付宝订单号
     */
    @TableField("pay_no")
    private String payNo;
    /**
     * 0:支付失败,1:支付成功/更新表的状态失败,2:支付成功/更新表的状态成功,3:已退款
     */
    @TableField("pay_state")
    private String payState;
    /**
     * 支付回调最后一次的参数
     */
    @TableField("pay_notify_params")
    private String payNotifyParams;
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

    public Integer getSsoId() {
        return ssoId;
    }

    public void setSsoId(Integer ssoId) {
        this.ssoId = ssoId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public String getPayNotifyParams() {
        return payNotifyParams;
    }

    public void setPayNotifyParams(String payNotifyParams) {
        this.payNotifyParams = payNotifyParams;
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
        return "PayBill{" +
        "id=" + id +
        ", ssoId=" + ssoId +
        ", payType=" + payType +
        ", orderNo=" + orderNo +
        ", payNo=" + payNo +
        ", payState=" + payState +
        ", payNotifyParams=" + payNotifyParams +
        ", createTime=" + createTime +
        "}";
    }
}
