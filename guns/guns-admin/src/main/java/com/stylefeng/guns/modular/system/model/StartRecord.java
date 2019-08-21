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
 * 会员砖石消费记录表
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-06
 */
@TableName("t_start_record")
public class StartRecord extends Model<StartRecord> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String type;
    /**
     * 用户ID
     */
    @TableField("sso_id")
    private Integer ssoId;
    /**
     * 美女ID
     */
    @TableField("girl_id")
    private Integer girlId;
    /**
     * 消费砖石数量
     */
    @TableField("start_count")
    private Integer startCount;
    @TableField("create_time")
    private Date createTime;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
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

    public Integer getGirlId() {
        return girlId;
    }

    public void setGirlId(Integer girlId) {
        this.girlId = girlId;
    }

    public Integer getStartCount() {
        return startCount;
    }

    public void setStartCount(Integer startCount) {
        this.startCount = startCount;
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
        return "StartRecord{" +
        "id=" + id +
        ", ssoId=" + ssoId +
        ", girlId=" + girlId +
        ", startCount=" + startCount +
        ", createTime=" + createTime +
        "}";
    }
}
