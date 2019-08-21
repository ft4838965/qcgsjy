/**
 * 初始化男用户详情对话框
 */
var SsoInfoDlg = {
    ssoInfoData : {}
};

/**
 * 清除数据
 */
SsoInfoDlg.clearData = function() {
    this.ssoInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SsoInfoDlg.set = function(key, val) {
    this.ssoInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SsoInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
SsoInfoDlg.close = function() {
    parent.layer.close(window.parent.Sso.layerIndex);
}

/**
 * 收集数据
 */
SsoInfoDlg.collectData = function(key) {
    this
    .set('id')
    .set('ssoId')
    .set('phone')
    .set('token')
    .set('nickName')
    .set('sex')
    .set('avatar')
    .set('bigAvatar')
    .set('signature')
    .set('state')
        .set('age')
        .set('birthday')
        .set('now_sheng')
        .set('now_shi')
        .set('now_qu')
        .set('vip')
        .set('start')
        .set('typeId');
}

/**
 * 提交添加
 */
SsoInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/sso/add", function(data){
            Feng.success("添加成功!");
            window.parent.Sso.table.refresh();
            SsoInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + "输入有误" + "!");
    });
    ajax.set(this.ssoInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
SsoInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/sso/update", function(data){
        Feng.success("修改成功!");
        window.parent.Sso.table.refresh();
        SsoInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.ssoInfoData);
    ajax.start();
}

$(function() {
    var thumbUp = new $WebUpload("avatar","/tool/uploadFile");
    thumbUp.setUploadBarId("progressBar");
    thumbUp.init("/tool/uploadFile");
});

function putArea(pid, pObject, id) {
    $(pObject).find('option').each(function (i, it) {
        if ($(it).val() == pid) $(it).attr('selected', 'selected');
    });
    $.ajaxSetup({async: false});
    $(pObject).nextAll('select').each(function (i, it) {
        $(it).val('0').change();
        $(it).children(':not(:eq(0))').remove();
    })
    if (pid != '0') {
        $.post('/sso/getAreaByPid', {pid: pid}, function (r) {
            $(r).each(function (i, it) {
                $(pObject).next('select:eq(0)').append('<option ' + (it.id == id ? 'selected="selected"' : '') + ' value="' + it.id + '">' + it.name + '</option>')
            });
        });
    }
}
