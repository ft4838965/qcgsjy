/**
 * 合伙人管理初始化
 */
var SuperSso = {
    id: "SuperSsoTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
SuperSso.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '名称', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '手机号', field: 'phone', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
SuperSso.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        SuperSso.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加合伙人
 */
SuperSso.openAddSuperSso = function () {
    var index = layer.open({
        type: 2,
        title: '添加合伙人',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/superSso/superSso_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看合伙人详情
 */
SuperSso.openSuperSsoDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '合伙人详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/superSso/superSso_update/' + SuperSso.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除合伙人
 */
SuperSso.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/superSso/delete", function (data) {
            Feng.success("删除成功!");
            SuperSso.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("superSsoId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询合伙人列表
 */
SuperSso.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    SuperSso.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = SuperSso.initColumn();
    var table = new BSTable(SuperSso.id, "/superSso/list", defaultColunms);
    table.setPaginationType("client");
    SuperSso.table = table.init();
});
