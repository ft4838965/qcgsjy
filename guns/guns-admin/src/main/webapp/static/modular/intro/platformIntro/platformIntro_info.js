/**
 * 初始化平台介绍详情对话框
 */
var PlatformIntroInfoDlg = {
    platformIntroInfoData : {}
};

/**
 * 清除数据
 */
PlatformIntroInfoDlg.clearData = function() {
    this.platformIntroInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PlatformIntroInfoDlg.set = function(key, val) {
    this.platformIntroInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PlatformIntroInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
PlatformIntroInfoDlg.close = function() {
    parent.layer.close(window.parent.PlatformIntro.layerIndex);
}

/**
 * 收集数据
 */
PlatformIntroInfoDlg.collectData = function() {
    this.platformIntroInfoData['intro']=PlatformIntroInfoDlg.heyifanEdit.txt.html();
    this
    .set('id');
}

/**
 * 提交添加
 */
PlatformIntroInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/platformIntro/add", function(data){
        Feng.success("添加成功!");
        window.parent.PlatformIntro.table.refresh();
        PlatformIntroInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.platformIntroInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
PlatformIntroInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/platformIntro/update", function(data){
        Feng.success("修改成功!");
        window.parent.PlatformIntro.table.refresh();
        PlatformIntroInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.platformIntroInfoData);
    ajax.start();
}

//自动替换转义
function escape2Html(str) {
    var arrEntities={'lt':'<','gt':'>','nbsp':' ','amp':'&','quot':'"'};
    return str.replace(/&(lt|gt|nbsp|amp|quot);/ig,function(all,t){return arrEntities[t];});
}

$(function() {

    var userEdit = window.wangEditor;
    var heyifanEditor = new userEdit('#editor');
    heyifanEditor.customConfig.uploadImgServer = '/muchPhoto/wangUpload'  ; // 上传图片到服务器
    heyifanEditor.customConfig.uploadImgTimeout = 300000;
    heyifanEditor.customConfig.uploadImgHooks = {
        before: function (xhr, editor, files) {
        },
        success: function (xhr, editor, result) {
        },
        fail: function (xhr, editor, result) {
            alert("上传图片失败!")
        },
        error: function (xhr, editor) {
        },
        timeout: function (xhr, editor) {
        },
        customInsert: function (insertImg, result, editor) {
            var url = result.url
            insertImg(url)
        }
    };
    heyifanEditor.create();
    userEdit.fullscreen.init('#editor');
    //var s = escape2Html($('#editor').next().html());
    heyifanEditor.txt.html($('#editor').next().html());
    PlatformIntroInfoDlg.heyifanEdit = heyifanEditor;
});


