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
 * 媒体表
 * </p>
 *
 * @author Arron
 * @since 2019-02-28
 */
@TableName("t_media")
public class Media extends Model<Media> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 图片id
     */
    @TableField("image_id")
    private String imageId;
    /**
     * 视频id
     */
    @TableField("video_id")
    private String videoId;
    /**
     * 外键id
     */
    @TableField("base_id")
    private String baseId;
    /**
     * 图片名称
     */
    private String picturename;
    /**
     * 类型:  0/图片 1/视频
     */
    private Integer type;
    /**
     * 后缀
     */
    private String suffixname;
    /**
     * 上传文件的名称
     */
    @TableField("oss_object_name")
    private String ossObjectName;
    /**
     * 是否通过审核： 0/待审核  1/通过 2/未通过
     */
    private Integer check;
    /**
     *  创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 创建人
     */
    @TableField("create_by")
    private String createBy;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getBaseId() {
        return baseId;
    }

    public void setBaseId(String baseId) {
        this.baseId = baseId;
    }

    public String getPicturename() {
        return picturename;
    }

    public void setPicturename(String picturename) {
        this.picturename = picturename;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSuffixname() {
        return suffixname;
    }

    public void setSuffixname(String suffixname) {
        this.suffixname = suffixname;
    }

    public String getOssObjectName() {
        return ossObjectName;
    }

    public void setOssObjectName(String ossObjectName) {
        this.ossObjectName = ossObjectName;
    }

    public Integer getCheck() {
        return check;
    }

    public void setCheck(Integer check) {
        this.check = check;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Media{" +
        "id=" + id +
        ", imageId=" + imageId +
        ", videoId=" + videoId +
        ", baseId=" + baseId +
        ", picturename=" + picturename +
        ", type=" + type +
        ", suffixname=" + suffixname +
        ", ossObjectName=" + ossObjectName +
        ", check=" + check +
        ", createTime=" + createTime +
        ", createBy=" + createBy +
        "}";
    }
}
