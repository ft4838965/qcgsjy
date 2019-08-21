/**
 * Copyright (C), 2019-2019, 贵州宏思锐达科技有限公司
 * FileName: MessageVo
 * Author:   Arron-Wu
 * Date:     2019/7/6 19:16
 * Description: 消息视图类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.stylefeng.guns.modular.system.model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.crypto.Data;

/**
 * 〈一句话功能简述〉<br> 
 * 〈消息视图类〉
 *
 * @author Arron
 * @create 2019/7/6
 * @since 1.0.0
 */
@Setter
@Getter
public class MessageVo {
    private int id;
    private String title;
    private String look;
    private String content;
    private Data time;
}