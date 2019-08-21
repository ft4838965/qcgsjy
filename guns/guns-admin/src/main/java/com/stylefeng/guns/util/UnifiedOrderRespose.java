package com.stylefeng.guns.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class UnifiedOrderRespose {
	private String return_code;             //返回状态码
    private String return_msg;              //返回信息
    private String appid;                   //公众账号ID
    private String mch_id;                  //商户号
    private String device_info;             //设备号
    private String nonce_str;               //随机字符串
    private String sign;                    //签名
    private String result_code;             //业务结果
    private String err_code;                //错误代码
    private String err_code_des;            //错误代码描述
    private String trade_type;              //交易类型
    private String prepay_id;               //预支付交易会话标识
    private String code_url;                //二维码链接
    private String openid;                  //trade_type=JSAPI时（即公众号支付），此参数必传


}
