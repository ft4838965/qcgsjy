package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 个人优势标签表
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-12
 */
@TableName("t_advantage")
public class Advantage extends Model<Advantage> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Integer id;
    /**
     * 名称
     */
    private String name;
    /**
     * 颜色
     */
    private String color;


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
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Advantage{" +
        "id=" + id +
        ", name=" + name +
        "}";
    }
}
