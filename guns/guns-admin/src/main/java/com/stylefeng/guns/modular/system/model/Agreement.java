package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 协议表
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-20
 */
@TableName("t_agreement")
public class Agreement extends Model<Agreement> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer id;
    /**
     * 类型
     */
    private String type;
    /**
     * 内容
     */
    private String content;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Agreement{" +
        "id=" + id +
        ", type=" + type +
        ", content=" + content +
        "}";
    }
}
