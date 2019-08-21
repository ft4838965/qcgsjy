/**
 * 初始化定时任务详情对话框
 */
var QuartzJobInfoDlg = {
    quartzJobInfoData : {}
};

/**
 * 清除数据
 */
QuartzJobInfoDlg.clearData = function() {
    this.quartzJobInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
QuartzJobInfoDlg.set = function(key, val) {
    this.quartzJobInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
QuartzJobInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
QuartzJobInfoDlg.close = function() {
    parent.layer.close(window.parent.QuartzJob.layerIndex);
}

/**
 * 收集数据
 */
QuartzJobInfoDlg.collectData = function() {
    this
    .set('id')
    .set('jobName')
    .set('fireDate')
    .set('corn')
    .set('type')
    .set('isFire')
    .set('params')
    .set('createTime');
}

/**
 * 提交添加
 */
QuartzJobInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/quartzJob/add", function(data){
        Feng.success("添加成功!");
        window.parent.QuartzJob.table.refresh();
        QuartzJobInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.quartzJobInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
QuartzJobInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/quartzJob/update", function(data){
        Feng.success("修改成功!");
        window.parent.QuartzJob.table.refresh();
        QuartzJobInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.quartzJobInfoData);
    ajax.start();
}

$(function() {

});
