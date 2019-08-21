/**
 * 初始化流水管理详情对话框
 */
var PayBillInfoDlg = {
    payBillInfoData : {}
};

/**
 * 清除数据
 */
PayBillInfoDlg.clearData = function() {
    this.payBillInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PayBillInfoDlg.set = function(key, val) {
    this.payBillInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PayBillInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
PayBillInfoDlg.close = function() {
    parent.layer.close(window.parent.PayBill.layerIndex);
}

/**
 * 收集数据
 */
PayBillInfoDlg.collectData = function() {
    this
    .set('id')
    .set('ssoId')
    .set('payType')
    .set('orderNo')
    .set('payNo')
    .set('payState')
    .set('payNotifyParams')
    .set('createTime');
}

/**
 * 提交添加
 */
PayBillInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/payBill/add", function(data){
        Feng.success("添加成功!");
        window.parent.PayBill.table.refresh();
        PayBillInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.payBillInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
PayBillInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/payBill/update", function(data){
        Feng.success("修改成功!");
        window.parent.PayBill.table.refresh();
        PayBillInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.payBillInfoData);
    ajax.start();
}

$(function() {

});
