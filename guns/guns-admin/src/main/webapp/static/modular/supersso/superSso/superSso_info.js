/**
 * 初始化合伙人详情对话框
 */
var SuperSsoInfoDlg = {
    superSsoInfoData : {}
};

/**
 * 清除数据
 */
SuperSsoInfoDlg.clearData = function() {
    this.superSsoInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SuperSsoInfoDlg.set = function(key, val) {
    this.superSsoInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SuperSsoInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
SuperSsoInfoDlg.close = function() {
    parent.layer.close(window.parent.SuperSso.layerIndex);
}

/**
 * 收集数据
 */
SuperSsoInfoDlg.collectData = function() {
    this
    .set('name')
    .set('phone');
}

/**
 * 提交添加
 */
SuperSsoInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/superSso/add", function(data){
        Feng.success("添加成功!");
        window.parent.SuperSso.table.refresh();
        SuperSsoInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.superSsoInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
SuperSsoInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/superSso/update", function(data){
        Feng.success("修改成功!");
        window.parent.SuperSso.table.refresh();
        SuperSsoInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.superSsoInfoData);
    ajax.start();
}

$(function() {

});
