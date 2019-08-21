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
 * 用户评论表
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-09
 */
@TableName("t_comment")
public class Comment extends Model<Comment> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键（评论ID）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 会员ID
     */
    @TableField("vip_id")
    private Integer vipId;
    /**
     * 美女ID
     */
    @TableField("girl_id")
    private Integer girlId;
    /**
     * 星评分数
     */
    @TableField("start_score")
    private Double startScore;
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

    public Integer getVipId() {
        return vipId;
    }

    public void setVipId(Integer vipId) {
        this.vipId = vipId;
    }

    public Integer getGirlId() {
        return girlId;
    }

    public void setGirlId(Integer girlId) {
        this.girlId = girlId;
    }

    public Double getStartScore() {
        return startScore;
    }

    public void setStartScore(Double startScore) {
        this.startScore = startScore;
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
        return "Comment{" +
        "id=" + id +
        ", vipId=" + vipId +
        ", girlId=" + girlId +
        ", startScore=" + startScore +
        ", createTime=" + createTime +
        "}";
    }
}
