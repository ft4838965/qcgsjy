/**
 * 初始化星评管理详情对话框
 */
var CommentInfoDlg = {
    commentInfoData : {}
};

/**
 * 清除数据
 */
CommentInfoDlg.clearData = function() {
    this.commentInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CommentInfoDlg.set = function(key, val) {
    this.commentInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CommentInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
CommentInfoDlg.close = function() {
    parent.layer.close(window.parent.Comment.layerIndex);
}

/**
 * 收集数据
 */
CommentInfoDlg.collectData = function() {
    this
    .set('id')
    .set('vipId')
    .set('girlId')
    .set('startScore')
    .set('createTime');
}

/**
 * 提交添加
 */
CommentInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/comment/add", function(data){
        Feng.success("添加成功!");
        window.parent.Comment.table.refresh();
        CommentInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.commentInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
CommentInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/comment/update", function(data){
        Feng.success("修改成功!");
        window.parent.Comment.table.refresh();
        CommentInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.commentInfoData);
    ajax.start();
}

$(function() {

});
