package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 区域字典
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-02
 */
@TableName("sys_area")
public class Area extends Model<Area> {

    private static final long serialVersionUID = 1L;

    /**
     * 区域主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 区域名称
     */
    private String name;
    /**
     * 本表的父级主键(0为顶级)
     */
    private Integer pid;
    private String lat;
    private String lng;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Area{" +
        "id=" + id +
        ", name=" + name +
        ", pid=" + pid +
        ", lat=" + lat +
        ", lng=" + lng +
        "}";
    }
}
