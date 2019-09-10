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
 * 邀请关系表
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-21
 */
@TableName("t_invited")
@Getter
@Setter
@ToString
public class Invited extends Model<Invited> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 邀请合伙人id
     */
    @TableField("super_sso_id")
    private String superSsoId;
    /**
     * 用户id
     */
    @TableField("sso_id")
    private String ssoId;
    /**
     * 被邀请用户id
     */
    @TableField("be_sso_id")
    private String beSsoId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 被邀请用户id
     */
    @TableField("status")
    private String status;
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
