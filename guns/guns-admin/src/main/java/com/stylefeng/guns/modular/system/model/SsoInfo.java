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
 * 用户基本信息
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-01
 */
@TableName("t_sso_info")
public class SsoInfo extends Model<SsoInfo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 登录账号
     */
    @TableField("sso_id")
    private Integer ssoId;
    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;
    /**
     * 身份证
     */
    @TableField("id_card")
    private String idCard;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 身高
     */
    private Integer tall;
    /**
     * 体重
     */
    private Integer weight;
    /**
     * 个人优势
     */
    private String advantege;
    /**
     * 标签集合
     */
    @TableField("tag_ids")
    private String tagIds;
    /**
     * 居住地区域
     */
    @TableField("area_code")
    private Integer areaCode;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 排序
     */
    private Integer sort;

    /**
     * 星评
     */
    @TableField("start_remark")
    private Double startRemark;
    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;


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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getTall() {
        return tall;
    }

    public void setTall(Integer tall) {
        this.tall = tall;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getAdvantege() {
        return advantege;
    }

    public void setAdvantege(String advantege) {
        this.advantege = advantege;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    public Integer getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(Integer areaCode) {
        this.areaCode = areaCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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

    public Double getStartRemark() {
        return startRemark;
    }

    public void setStartRemark(Double startRemark) {
        this.startRemark = startRemark;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SsoInfo{" +
        "id=" + id +
        ", ssoId=" + ssoId +
        ", realName=" + realName +
        ", idCard=" + idCard +
        ", birthday=" + birthday +
        ", age=" + age +
        ", tall=" + tall +
        ", weight=" + weight +
        ", advantege=" + advantege +
        ", tagIds=" + tagIds +
        ", areaCode=" + areaCode +
        ", address=" + address +
        ", sort=" + sort +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
