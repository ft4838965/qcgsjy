/**
 * Copyright (C), 2019-2019, 贵州宏思锐达科技有限公司
 * FileName: TextMessage
 * Author:   Arron-Wu
 * Date:     2019/5/19 12:41
 * Description: 文本消息实体类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.stylefeng.guns.util.wechat_public;

import lombok.Getter;
import lombok.Setter;

/**
 * 〈一句话功能简述〉<br> 
 * 〈文本消息实体类〉
 *
 * @author Arron
 * @create 2019/5/19
 * @since 1.0.0
 */
@Getter
@Setter
public class TextMessage {
    private String ToUserName;

    private String FromUserName;

    private Long CreateTime;

    private String MsgType;

    private String Content;

    private String MsgId;
}