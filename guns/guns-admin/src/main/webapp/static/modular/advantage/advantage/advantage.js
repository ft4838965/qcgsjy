/**
 * 个人优势管理初始化
 */
var Advantage = {
    id: "AdvantageTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Advantage.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '名称', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '颜色', field: 'color', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Advantage.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Advantage.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加个人优势
 */
Advantage.openAddAdvantage = function () {
    var index = layer.open({
        type: 2,
        title: '添加个人优势',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/advantage/advantage_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看个人优势详情
 */
Advantage.openAdvantageDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '个人优势详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/advantage/advantage_update/' + Advantage.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除个人优势
 */
Advantage.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/advantage/delete", function (data) {
            Feng.success("删除成功!");
            Advantage.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("advantageId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询个人优势列表
 */
Advantage.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Advantage.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Advantage.initColumn();
    var table = new BSTable(Advantage.id, "/advantage/list", defaultColunms);
    table.setPaginationType("client");
    Advantage.table = table.init();
});
