/**
 * 初始化用户申请提现详情对话框
 */
var UseableBalanceInfoDlg = {
    useableBalanceInfoData : {}
};

/**
 * 清除数据
 */
UseableBalanceInfoDlg.clearData = function() {
    this.useableBalanceInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UseableBalanceInfoDlg.set = function(key, val) {
    this.useableBalanceInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UseableBalanceInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
UseableBalanceInfoDlg.close = function() {
    parent.layer.close(window.parent.UseableBalance.layerIndex);
}

/**
 * 收集数据
 */
UseableBalanceInfoDlg.collectData = function() {
    this
    .set('id')
    .set('ssoId')
    .set('useableBalance')
    .set('state')
    .set('createTime')
    .set('payTime');
}

/**
 * 提交添加
 */
UseableBalanceInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/useableBalance/add", function(data){
        Feng.success("添加成功!");
        window.parent.UseableBalance.table.refresh();
        UseableBalanceInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.useableBalanceInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
UseableBalanceInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/useableBalance/update", function(data){
        Feng.success("修改成功!");
        window.parent.UseableBalance.table.refresh();
        UseableBalanceInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.useableBalanceInfoData);
    ajax.start();
}

$(function() {

});
