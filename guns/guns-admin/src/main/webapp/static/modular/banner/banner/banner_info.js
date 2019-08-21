/**
 * 初始化UI管理详情对话框
 */
var BannerInfoDlg = {
    bannerInfoData : {}
};

/**
 * 清除数据
 */
BannerInfoDlg.clearData = function() {
    this.bannerInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BannerInfoDlg.set = function(key, val) {
    this.bannerInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BannerInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
BannerInfoDlg.close = function() {
    parent.layer.close(window.parent.Banner.layerIndex);
}

/**
 * 收集数据
 */
BannerInfoDlg.collectData = function() {
    this
    .set('id')
    .set('name')
    .set('path')
    .set('type');
}

/**
 * 提交添加
 */
BannerInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/banner/add", function(data){
        Feng.success("添加成功!");
        window.parent.Banner.table.refresh();
        BannerInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bannerInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
BannerInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/banner/update", function(data){
        Feng.success("修改成功!");
        window.parent.Banner.table.refresh();
        BannerInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bannerInfoData);
    ajax.start();
}

$(function() {
    var thumbUp = new $WebUpload("path","/tool/uploadFile");
    thumbUp.setUploadBarId("progressBar");
    thumbUp.init("/tool/uploadFile");
});
