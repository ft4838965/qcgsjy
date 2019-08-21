/**
 * 初始化个人优势详情对话框
 */
var AdvantageInfoDlg = {
    advantageInfoData : {}
};

/**
 * 清除数据
 */
AdvantageInfoDlg.clearData = function() {
    this.advantageInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AdvantageInfoDlg.set = function(key, val) {
    this.advantageInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AdvantageInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
AdvantageInfoDlg.close = function() {
    parent.layer.close(window.parent.Advantage.layerIndex);
}

/**
 * 收集数据
 */
AdvantageInfoDlg.collectData = function() {
    this
    .set('id')
    .set('name')
        .set('color');
}

/**
 * 提交添加
 */
AdvantageInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/advantage/add", function(data){
        Feng.success("添加成功!");
        window.parent.Advantage.table.refresh();
        AdvantageInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.advantageInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
AdvantageInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/advantage/update", function(data){
        Feng.success("修改成功!");
        window.parent.Advantage.table.refresh();
        AdvantageInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.advantageInfoData);
    ajax.start();
}

$(function() {

});
