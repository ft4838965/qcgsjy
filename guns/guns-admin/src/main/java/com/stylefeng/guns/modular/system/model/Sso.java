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
 * 用户表
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-01
 */
@TableName("t_sso")
@ToString
@Getter
@Setter
public class Sso extends Model<Sso> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户的ID
     */
    @TableField("sso_id")
    private Integer ssoId;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 令牌
     */
    private String token;
    /**
     * 昵称
     */
    @TableField("nick_name")
    private String nickName;
    /**
     * 性别
     */
    private String sex;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 封面图
     */
    @TableField("big_avatar")
    private String bigAvatar;
    /**
     * 封面图审核状态
     */
    @TableField("check_big_avatar")
    private String checkBigAvatar;
    /**
     * 个性签名
     */
    private String signature;
    /**
     * 用户状态
     */
    private String state;
    /**
     * 用户经度
     */
    private String lat;
    /**
     * 用户纬度
     */
    private String lng;
    /**
     * 见面砖石
     */
    private int meetingstart;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    @TableField("check_big_avatar_time")
    private Date checkBigAvatarTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
