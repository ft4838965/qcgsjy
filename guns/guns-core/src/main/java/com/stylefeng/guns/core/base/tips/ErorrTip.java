/**
 * Copyright (C), 2019-2019, 贵州宏思锐达科技有限公司
 * FileName: ErorrTip
 * Author:   Arron-Wu
 * Date:     2019/3/2 11:01
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.stylefeng.guns.core.base.tips;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Arron
 * @create 2019/3/2
 * @since 1.0.0
 */
public class ErorrTip extends Tip {
    public ErorrTip(){
        super.code = 400;
        super.message = "输入有误";
    }
}