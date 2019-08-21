package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 会员表
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-05
 */
@TableName("t_vip")
public class Vip extends Model<Vip> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户ID
     */
    @TableField("sso_id")
    private Integer ssoId;
    /**
     * 会员类型
     */
    @TableField("type_id")
    private Integer typeId;
    /**
     * 有效天数（每月都以30天为标准）
     */
    @TableField("valid_date")
    private Integer validDate;
    /**
     * 砖石数量
     */
    private Integer start;
    /**
     * 会员状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 到期时间
     */
    @TableField("end_time")
    private Date endTime;


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

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getValidDate() {
        return validDate;
    }

    public void setValidDate(Integer validDate) {
        this.validDate = validDate;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Vip{" +
        "id=" + id +
        ", ssoId=" + ssoId +
        ", typeId=" + typeId +
        ", validDate=" + validDate +
        ", start=" + start +
        ", status=" + status +
        ", createTime=" + createTime +
        ", endTime=" + endTime +
        "}";
    }
}
