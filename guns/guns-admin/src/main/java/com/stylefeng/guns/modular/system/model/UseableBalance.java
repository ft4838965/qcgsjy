package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 用户提现记录(用于下账)
 * </p>
 *
 * @author stylefeng
 * @since 2019-08-22
 */
@TableName("lijun_useable_balance")
@Getter
@Setter
@ToString
public class UseableBalance extends Model<UseableBalance> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户的sso_id
     */
    @TableField("sso_id")
    private String ssoId;
    /**
     * 合伙人id
     */
    @TableField("super_sso_id")
    private String superSsoId;
    /**
     * 提现手机号
     */
    @TableField("phone")
    private String phone;
    /**
     * 提现金额
     */
    @TableField("useable_balance")
    private Double useableBalance;
    /**
     * 0,未下账,1,已下账
     */
    private String state;
    @TableField("create_time")
    private Date createTime;
    /**
     * 下账时间
     */
    @TableField("pay_time")
    private Date payTime;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
