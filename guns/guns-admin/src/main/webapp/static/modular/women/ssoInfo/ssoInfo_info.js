/**
 * 初始化女用户详情对话框
 */
var SsoInfoInfoDlg = {
    ssoInfoInfoData : {}
};

/**
 * 清除数据
 */
SsoInfoInfoDlg.clearData = function() {
    this.ssoInfoInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SsoInfoInfoDlg.set = function(key, val) {
    this.ssoInfoInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SsoInfoInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
SsoInfoInfoDlg.close = function() {
    parent.layer.close(window.parent.SsoInfo.layerIndex);
}

/**
 * 收集数据
 */
SsoInfoInfoDlg.collectData = function() {

    var arron = $('#tagIds').val();
    var newDemo = "";
    if (arron != null && arron.length > 0) {
        for (var z = 0; z < arron.length; z++) {
            newDemo += arron[z] + ",";
        }
        this.ssoInfoInfoData['tagIds'] = newDemo;
    } else {
        this.ssoInfoInfoData['tagIds'] = '';
    }

    this
    .set('id')
    .set('ssoId')
    .set('realName')
    .set('idCard')
    .set('money')
    .set('age')
    .set('tall')
    .set('weight')
    .set('advantege')
    .set('sort')
        .set('now_sheng')
        .set('now_shi')
        .set('now_qu')
        .set('avatar')
        .set('bigAvatar')
        .set('signature')
        .set('phone')
        .set('nickName')
        .set('startRemark')
        .set('checkBigAvatar')
        .set('origin_money');
}

/**
 * 提交添加
 */
SsoInfoInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/ssoInfo/add", function(data){
            Feng.success("添加成功!");
            window.parent.SsoInfo.table.refresh();
            SsoInfoInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + "输入有误" + "!");
    });
    ajax.set(this.ssoInfoInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
SsoInfoInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/ssoInfo/update", function(data){
        Feng.success("修改成功!");
        window.parent.SsoInfo.table.refresh();
        SsoInfoInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + "输入有误" + "!");
    });
    ajax.set(this.ssoInfoInfoData);
    ajax.start();
}

$(function() {
    // 初始化缩略图上传
    var avatarUp = new $WebUpload("avatar", "/tool/uploadFile");
    avatarUp.setUploadBarId("progressBar");
    avatarUp.init("/tool/uploadFile");
    // 初始化缩略图上传
    var avatar_bigUp = new $WebUpload("bigAvatar", "/tool/uploadFile");
    avatar_bigUp.setUploadBarId("progressBar");
    avatar_bigUp.init("/tool/uploadFile");

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

