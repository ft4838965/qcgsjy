/**
 * 初始化会员类型详情对话框
 */
var VipTypeInfoDlg = {
    vipTypeInfoData : {}
};

/**
 * 清除数据
 */
VipTypeInfoDlg.clearData = function() {
    this.vipTypeInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
VipTypeInfoDlg.set = function(key, val) {
    this.vipTypeInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
VipTypeInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
VipTypeInfoDlg.close = function() {
    parent.layer.close(window.parent.VipType.layerIndex);
}

/**
 * 收集数据
 */
VipTypeInfoDlg.collectData = function() {
    this
    .set('id')
    .set('name')
    .set('originMoney')
    .set('realMoney')
    .set('totalStart')
    .set('dayStart')
    .set('topStart');
}

/**
 * 提交添加
 */
VipTypeInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/vipType/add", function(data){
        Feng.success("添加成功!");
        window.parent.VipType.table.refresh();
        VipTypeInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.vipTypeInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
VipTypeInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/vipType/update", function(data){
        Feng.success("修改成功!");
        window.parent.VipType.table.refresh();
        VipTypeInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.vipTypeInfoData);
    ajax.start();
}

$(function() {

});
