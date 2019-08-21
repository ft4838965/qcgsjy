/**
 * 初始化作品管理详情对话框
 */
var SsoWorkInfoDlg = {
    ssoWorkInfoData : {}
};

/**
 * 清除数据
 */
SsoWorkInfoDlg.clearData = function() {
    this.ssoWorkInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SsoWorkInfoDlg.set = function(key, val) {
    this.ssoWorkInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SsoWorkInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
SsoWorkInfoDlg.close = function() {
    parent.layer.close(window.parent.SsoWork.layerIndex);
}

/**
 * 收集数据
 */
SsoWorkInfoDlg.collectData = function() {
    this
    .set('id')
    .set('ssoId')
    .set('content')
    .set('thumb')
    .set('type')
    .set('check')
    .set('baseId')
    .set('likeCount')
    .set('commentCount')
    .set('createTime')
    .set('updateTime');
}

/**
 * 提交添加
 */
SsoWorkInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/ssoWork/add", function(data){
        Feng.success("添加成功!");
        window.parent.SsoWork.table.refresh();
        SsoWorkInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.ssoWorkInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
SsoWorkInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/ssoWork/update", function(data){
        Feng.success("修改成功!");
        window.parent.SsoWork.table.refresh();
        SsoWorkInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.ssoWorkInfoData);
    ajax.start();
}

/**
 * 提交审核通过
 */
SsoWorkInfoDlg.pass = function(id){
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/ssoWork/passOrNo?type=1&id="+id, function(data){
        Feng.success("审核成功!");
        window.parent.SsoWork.table.refresh();
        SsoWorkInfoDlg.close();
    },function(data){
        Feng.error("审核失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.ssoWorkInfoData);
    ajax.start();
}

/**
 * 提交审核拒绝
 */
SsoWorkInfoDlg.refuse = function(id){
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/ssoWork/passOrNo?type=2&id="+id, function(data){
        Feng.success("审核成功!");
        window.parent.SsoWork.table.refresh();
        SsoWorkInfoDlg.close();
    },function(data){
        Feng.error("审核失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.ssoWorkInfoData);
    ajax.start();
}

/**
 * 定义删除视频方法
 */

SsoWorkInfoDlg.deleteVideo=function(v){
    var ajax = new $ax(Feng.ctxPath + "/videoUtilOption/deleteVideoByfileUrl?fileUrl=" + v, function(data){
        Feng.success("删除成功! 请重新打开该页面查看效果!");

    },function(data){
        Feng.success("删除成功! 请重新打开该页面查看效果!");

    });
    ajax.set(this.worksInfoData);
    ajax.start();
};

/**
 * 定义审核方法
 */

SsoWorkInfoDlg.checkVideo = function (v) {
    var ajax = new $ax(Feng.ctxPath + "/ssoWork/checkVideo?objectName=" + v, function (data) {
        Feng.success("审核通过! 请重新打开该页面查看效果!");

    }, function (data) {
        Feng.success("审核通过! 请重新打开该页面查看效果!");
    });
    ajax.set(this.worksInfoData);
    ajax.start();
};

$(function() {

});
