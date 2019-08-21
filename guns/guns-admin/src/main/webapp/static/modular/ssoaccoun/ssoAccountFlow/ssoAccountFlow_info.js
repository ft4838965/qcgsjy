/**
 * 初始化打赏提现详情对话框
 */
var SsoAccountFlowInfoDlg = {
    ssoAccountFlowInfoData : {}
};

/**
 * 清除数据
 */
SsoAccountFlowInfoDlg.clearData = function() {
    this.ssoAccountFlowInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SsoAccountFlowInfoDlg.set = function(key, val) {
    this.ssoAccountFlowInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SsoAccountFlowInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
SsoAccountFlowInfoDlg.close = function() {
    parent.layer.close(window.parent.SsoAccountFlow.layerIndex);
}

/**
 * 收集数据
 */
SsoAccountFlowInfoDlg.collectData = function() {
    this
    .set('id')
    .set('ssoId')
    .set('comeFrom')
    .set('money')
    .set('avilableBalance')
    .set('frozenBalance')
    .set('businessType')
    .set('businessName')
    .set('note')
    .set('createTime');
}

/**
 * 提交添加
 */
SsoAccountFlowInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/ssoAccountFlow/add", function(data){
        Feng.success("添加成功!");
        window.parent.SsoAccountFlow.table.refresh();
        SsoAccountFlowInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.ssoAccountFlowInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
SsoAccountFlowInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/ssoAccountFlow/update", function(data){
        Feng.success("修改成功!");
        window.parent.SsoAccountFlow.table.refresh();
        SsoAccountFlowInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.ssoAccountFlowInfoData);
    ajax.start();
}

$(function() {

});
