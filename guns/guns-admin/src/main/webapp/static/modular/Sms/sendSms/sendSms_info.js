/**
 * 初始化短信记录详情对话框
 */
var SendSmsInfoDlg = {
    sendSmsInfoData : {}
};

/**
 * 清除数据
 */
SendSmsInfoDlg.clearData = function() {
    this.sendSmsInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SendSmsInfoDlg.set = function(key, val) {
    this.sendSmsInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SendSmsInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
SendSmsInfoDlg.close = function() {
    parent.layer.close(window.parent.SendSms.layerIndex);
}

/**
 * 收集数据
 */
SendSmsInfoDlg.collectData = function() {
    this
    .set('id')
    .set('type')
    .set('phone')
    .set('createTime');
}

/**
 * 提交添加
 */
SendSmsInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/sendSms/add", function(data){
        Feng.success("添加成功!");
        window.parent.SendSms.table.refresh();
        SendSmsInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.sendSmsInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
SendSmsInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/sendSms/update", function(data){
        Feng.success("修改成功!");
        window.parent.SendSms.table.refresh();
        SendSmsInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.sendSmsInfoData);
    ajax.start();
}

$(function() {

});
