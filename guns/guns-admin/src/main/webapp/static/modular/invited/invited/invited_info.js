/**
 * 初始化邀请返现详情对话框
 */
var InvitedInfoDlg = {
    invitedInfoData : {}
};

/**
 * 清除数据
 */
InvitedInfoDlg.clearData = function() {
    this.invitedInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
InvitedInfoDlg.set = function(key, val) {
    this.invitedInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
InvitedInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
InvitedInfoDlg.close = function() {
    parent.layer.close(window.parent.Invited.layerIndex);
}

/**
 * 收集数据
 */
InvitedInfoDlg.collectData = function() {
    this
    .set('id')
    .set('ssoId')
    .set('beSsoId')
    .set('createTime');
}

/**
 * 提交添加
 */
InvitedInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/invited/add", function(data){
        Feng.success("添加成功!");
        window.parent.Invited.table.refresh();
        InvitedInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.invitedInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
InvitedInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/invited/update", function(data){
        Feng.success("修改成功!");
        window.parent.Invited.table.refresh();
        InvitedInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.invitedInfoData);
    ajax.start();
}

$(function() {

});
