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
 * 作品管理表
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-04
 */
@TableName("t_sso_work")
public class SsoWork extends Model<SsoWork> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户的ID
     */
    @TableField("sso_id")
    private String ssoId;
    /**
     * 内容
     */
    private String content;
    /**
     * 作品封面图
     */
    private String thumb;
    /**
     * 类型（0：图文，1：视频）
     */
    private String type;
    /**
     * 审核状态（0：未审查，1：通过，2：拒绝）
     */
    private String check;
    /**
     * 图片表关联id

     */
    @TableField("base_id")
    private String baseId;
    /**
     * 点赞量
     */
    @TableField("like_count")
    private Integer likeCount;
    /**
     * 评论量
     */
    @TableField("comment_count")
    private Integer commentCount;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSsoId() {
        return ssoId;
    }

    public void setSsoId(String ssoId) {
        this.ssoId = ssoId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getBaseId() {
        return baseId;
    }

    public void setBaseId(String baseId) {
        this.baseId = baseId;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
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
        return "SsoWork{" +
        "id=" + id +
        ", ssoId=" + ssoId +
        ", content=" + content +
        ", thumb=" + thumb +
        ", type=" + type +
        ", check=" + check +
        ", baseId=" + baseId +
        ", likeCount=" + likeCount +
        ", commentCount=" + commentCount +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
