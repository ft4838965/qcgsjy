/**
 * 作品管理管理初始化
 */
var SsoWork = {
    id: "SsoWorkTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
SsoWork.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '用户的ID', field: 'ssoId', visible: true, align: 'center', valign: 'middle'},
            {title: '内容', field: 'content', visible: true, align: 'center', valign: 'middle'},
            // {title: '作品封面图', field: 'thumb', visible: true, align: 'center', valign: 'middle'},
            {title: '类型', field: 'type', visible: true, align: 'center', valign: 'middle'},
            {title: '审核状态', field: 'check', visible: true, align: 'center', valign: 'middle'},
            // {title: '图片表关联id', field: 'baseId', visible: true, align: 'center', valign: 'middle'},
            // {title: '点赞量', field: 'likeCount', visible: true, align: 'center', valign: 'middle'},
            // {title: '评论量', field: 'commentCount', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            // {title: '更新时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
SsoWork.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        SsoWork.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加作品管理
 */
SsoWork.openAddSsoWork = function () {
    var index = layer.open({
        type: 2,
        title: '添加作品管理',
        area: ['100%', '100%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/ssoWork/ssoWork_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看作品管理详情
 */
SsoWork.openSsoWorkDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '作品管理详情',
            area: ['100%', '100%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/ssoWork/ssoWork_update/' + SsoWork.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除作品管理
 */
SsoWork.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/ssoWork/delete", function (data) {
            Feng.success("删除成功!");
            SsoWork.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("ssoWorkId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询作品管理列表
 */
SsoWork.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    SsoWork.table.refresh({query: queryData});
};

SsoWork.waite = function () {
    var queryData = {};
    $("#condition").val("未审查的作品");
    queryData['condition'] = $("#condition").val();
    $("#condition").val("");
    SsoWork.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = SsoWork.initColumn();
    var table = new BSTable(SsoWork.id, "/ssoWork/list", defaultColunms);
    table.setPaginationType("client");
    SsoWork.table = table.init();
});
