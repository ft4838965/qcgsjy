package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 系统设置表
 * </p>
 *
 * @author Arron
 * @since 2019-02-28
 */
@TableName("t_setting")
@Getter
@Setter
public class Setting extends Model<Setting> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 官方服务电话号码
     */
    @TableField("service_phone")
    private String servicePhone;
    /**
     * 阿里云云存储access_key
     */
    @TableField("ali_oss_access_key")
    private String aliOssAccessKey;
    /**
     * 阿里云云存储access_id
     */
    @TableField("ali_oss_access_id")
    private String aliOssAccessId;
    /**
     * 阿里云实名认证接口appcode
     */
    @TableField("ali_sm_appcode")
    private String aliSmAppcode;
    /**
     * 云片网(短信)appkey
     */
    @TableField("yp_appkey")
    private String ypAppkey;
    /**
     * 高德地图key
     */
    @TableField("gd_key")
    private String gdKey;
    /**
     * 阿里支付宝appid
     */
    @TableField("ali_pay_app_id")
    private String aliPayAppId;
    /**
     * 阿里支付宝私匙
     */
    @TableField("ali_pay_app_private_key")
    private String aliPayAppPrivateKey;
    /**
     * 阿里支付宝公匙
     */
    @TableField("ali_pay_app_public_key")
    private String aliPayAppPublicKey;
    /**
     * 阿里支付宝回调地址
     */
    @TableField("ali_pay_app_ali_notify_url")
    private String aliPayAppAliNotifyUrl;
    /**
     * 微信支付appid
     */
    @TableField("wechat_pay_app_id")
    private String wechatPayAppId;
    /**
     * 微信支付商户号
     */
    @TableField("wechat_pay_mch_id")
    private String wechatPayMchId;
    /**
     * 微信支付回调地址
     */
    @TableField("wechat_pay_notify_url")
    private String wechatPayNotifyUrl;
    /**
     * 微信支付商户key
     */
    @TableField("wechat_pay_key")
    private String wechatPayKey;
    /**
     * 微信订阅号appid
     */
    @TableField("wechat_app_id")
    private String wechatAppId;
    /**
     * 微信订阅号appsecret
     */
    @TableField("wechat_app_secret")
    private String wechatAppSecret;
    /**
     * 微信订阅号access_token(不用展示)
     */
    @TableField("wechat_access_token")
    private String wechatAccessToken;
    /**
     * 微信订阅号ticket(不用展示)
     */
    @TableField("wechat_ticket")
    private String wechatTicket;
    /**
     * 安卓版本号
     */
    @TableField("android_version")
    private String androidVersion;
    /**
     * 安卓APK下载地址
     */
    @TableField("android_apk_url")
    private String androidApkUrl;

    @TableField("money_give_man")
    private Integer moneyGiveMan;


    @TableField("money_give_women")
    private Integer moneyGiveWomen;

    @TableField("start_get_tel")
    private Integer startGetTel;

    @TableField("publish_switch")
    private String publishSwitch;

    @TableField("remind")
    private String remind;

    @TableField("tel")
    private String tel;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Setting{" +
        "id=" + id +
        ", servicePhone=" + servicePhone +
        ", aliOssAccessKey=" + aliOssAccessKey +
        ", aliOssAccessId=" + aliOssAccessId +
        ", aliSmAppcode=" + aliSmAppcode +
        ", ypAppkey=" + ypAppkey +
        ", gdKey=" + gdKey +
        ", aliPayAppId=" + aliPayAppId +
        ", aliPayAppPrivateKey=" + aliPayAppPrivateKey +
        ", aliPayAppPublicKey=" + aliPayAppPublicKey +
        ", aliPayAppAliNotifyUrl=" + aliPayAppAliNotifyUrl +
        ", wechatPayAppId=" + wechatPayAppId +
        ", wechatPayMchId=" + wechatPayMchId +
        ", wechatPayNotifyUrl=" + wechatPayNotifyUrl +
        ", wechatPayKey=" + wechatPayKey +
        ", wechatAppId=" + wechatAppId +
        ", wechatAppSecret=" + wechatAppSecret +
        ", wechatAccessToken=" + wechatAccessToken +
        ", wechatTicket=" + wechatTicket +
        ", androidVersion=" + androidVersion +
        ", androidApkUrl=" + androidApkUrl +
        "}";
    }
}
