/**
 * 星评管理管理初始化
 */
var Comment = {
    id: "CommentTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Comment.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '自增主键（评论ID）', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '会员ID', field: 'vipId', visible: true, align: 'center', valign: 'middle'},
            {title: '美女ID', field: 'girlId', visible: true, align: 'center', valign: 'middle'},
            {title: '星评分数', field: 'startScore', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Comment.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Comment.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加星评管理
 */
Comment.openAddComment = function () {
    var index = layer.open({
        type: 2,
        title: '添加星评管理',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/comment/comment_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看星评管理详情
 */
Comment.openCommentDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '星评管理详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/comment/comment_update/' + Comment.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除星评管理
 */
Comment.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/comment/delete", function (data) {
            Feng.success("删除成功!");
            Comment.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("commentId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询星评管理列表
 */
Comment.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Comment.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Comment.initColumn();
    var table = new BSTable(Comment.id, "/comment/list", defaultColunms);
    table.setPaginationType("client");
    Comment.table = table.init();
});
