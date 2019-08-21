/**
 * 初始化全局配置详情对话框
 */
var SettingInfoDlg = {
    settingInfoData : {}
};

/**
 * 清除数据
 */
SettingInfoDlg.clearData = function() {
    this.settingInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SettingInfoDlg.set = function(key, val) {
    this.settingInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SettingInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
SettingInfoDlg.close = function() {
    parent.layer.close(window.parent.Setting.layerIndex);
}

/**
 * 收集数据
 */
SettingInfoDlg.collectData = function() {
    this
    .set('id')
    .set('servicePhone')
    .set('aliOssAccessKey')
    .set('aliOssAccessId')
    .set('aliSmAppcode')
    .set('ypAppkey')
    .set('gdKey')
    .set('aliPayAppId')
    .set('aliPayAppPrivateKey')
    .set('aliPayAppPublicKey')
    .set('aliPayAppAliNotifyUrl')
    .set('wechatPayAppId')
    .set('wechatPayMchId')
    .set('wechatPayNotifyUrl')
    .set('wechatPayKey')
    .set('wechatAppId')
    .set('wechatAppSecret')
    .set('wechatAccessToken')
    .set('wechatTicket')
    .set('androidVersion')
    .set('androidApkUrl')
        .set('moneyGiveMan')
        .set('moneyGiveWomen')
        .set('publishSwitch')
        .set('remind')
        .set('startGetTel')
        .set('tel');
}

/**
 * 提交添加
 */
SettingInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/setting/add", function(data){
        Feng.success("添加成功!");
        window.parent.Setting.table.refresh();
        SettingInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.settingInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
SettingInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/setting/update", function(data){
        Feng.success("修改成功!");
        window.parent.Setting.table.refresh();
        SettingInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.settingInfoData);
    ajax.start();
}

$(function() {

});

function addImg(obj) {
    var formData = new FormData();
    var uploadFile = $(obj).get(0).files[0];
    formData.append("file",uploadFile);
    console.log(uploadFile);
    if("undefined" != typeof(uploadFile) && uploadFile != null && uploadFile != ""){
        $.ajax({
            url:'/setting/upAPK',
            type:'POST',
            data:formData,
            async: false,
            cache: false,
            contentType: false, //不设置内容类型
            processData: false, //不处理数据
            success:function(data){
                $('#android_apk_url').empty().append('<a href="'+data+'">'+data+'</a>');
            },
            error:function(e){
                console.log(e);
                alert("上传失败！");
            }
        })
    }else {
        alert("选择的文件无效！请重新选择");
    }
    $(obj).replaceWith('<input type="file" style="display: none;" onchange="addImg($(this));" />');
}
