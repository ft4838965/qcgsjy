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
 * 用户消息中心
 * </p>
 *
 * @author stylefeng
 * @since 2019-07-06
 */
@TableName("t_message")
public class Message extends Model<Message> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 发出这条消息的用户（0/官方  sso_id/具体用户）
     */
    @TableField("sso_id")
    private Integer ssoId;
    /**
     * 接收这条消息的用户（0/男用户 1/女用户 2/所有用户 sso_id/具体用户）
     */
    @TableField("message_sso_id")
    private String messageSsoId;
    /**
     * 消息类型： 0/会员到期  1/消费提醒 2/官方消息  (该字段弃用)
     */
    private String type;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 具体消息类型 ： 0/打赏 1/提现 (该字段弃用)
     */
    @TableField("official_message_type")
    private String officialMessageType;
    /**
     * 该条消息是否已被点击查看(0:否,1:是)
     */
    private String look;
    /**
     * 作品的id
     */
    @TableField("work_id")
    private Integer workId;
    /**
     * 系统消息已读的用户ID集合
     */
    @TableField("sso_ids_for_sys")
    private String ssoIdsForSys;
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

    public String getMessageSsoId() {
        return messageSsoId;
    }

    public void setMessageSsoId(String messageSsoId) {
        this.messageSsoId = messageSsoId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOfficialMessageType() {
        return officialMessageType;
    }

    public void setOfficialMessageType(String officialMessageType) {
        this.officialMessageType = officialMessageType;
    }

    public String getLook() {
        return look;
    }

    public void setLook(String look) {
        this.look = look;
    }

    public Integer getWorkId() {
        return workId;
    }

    public void setWorkId(Integer workId) {
        this.workId = workId;
    }

    public String getSsoIdsForSys() {
        return ssoIdsForSys;
    }

    public void setSsoIdsForSys(String ssoIdsForSys) {
        this.ssoIdsForSys = ssoIdsForSys;
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
        return "Message{" +
        "id=" + id +
        ", ssoId=" + ssoId +
        ", messageSsoId=" + messageSsoId +
        ", type=" + type +
        ", content=" + content +
        ", officialMessageType=" + officialMessageType +
        ", look=" + look +
        ", workId=" + workId +
        ", ssoIdsForSys=" + ssoIdsForSys +
        ", createTime=" + createTime +
        "}";
    }
}
