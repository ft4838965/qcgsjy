/**
 * 初始化砖石流水详情对话框
 */
var StartRecordInfoDlg = {
    startRecordInfoData : {}
};

/**
 * 清除数据
 */
StartRecordInfoDlg.clearData = function() {
    this.startRecordInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
StartRecordInfoDlg.set = function(key, val) {
    this.startRecordInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
StartRecordInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
StartRecordInfoDlg.close = function() {
    parent.layer.close(window.parent.StartRecord.layerIndex);
}

/**
 * 收集数据
 */
StartRecordInfoDlg.collectData = function() {
    this
    .set('id')
    .set('ssoId')
    .set('girlId')
    .set('startCount');
}

/**
 * 提交添加
 */
StartRecordInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/startRecord/add", function(data){
        Feng.success("添加成功!");
        window.parent.StartRecord.table.refresh();
        StartRecordInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.startRecordInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
StartRecordInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/startRecord/update", function(data){
        Feng.success("修改成功!");
        window.parent.StartRecord.table.refresh();
        StartRecordInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.startRecordInfoData);
    ajax.start();
}

$(function() {

});
