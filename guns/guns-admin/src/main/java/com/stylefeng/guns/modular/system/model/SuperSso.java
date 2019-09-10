package com.stylefeng.guns.modular.system.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 合伙人表
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-12
 */
@TableName("t_super_sso")
@Setter
@Getter
@ToString
public class SuperSso extends Model<SuperSso> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Integer id;
    /**
     * 名称
     */
    private String name;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 0:未审核,1:已审核
     */
    private String check;
    /**
     * 余额
     */
    private Double balance;
    /**
     * 邀请合伙人ID
     */
    private String pid;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
