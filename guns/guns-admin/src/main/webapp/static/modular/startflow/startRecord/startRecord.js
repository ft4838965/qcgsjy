/**
 * 砖石流水管理初始化
 */
var StartRecord = {
    id: "StartRecordTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
StartRecord.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '用户ID', field: 'ssoId', visible: true, align: 'center', valign: 'middle'},
            {title: '美女ID', field: 'girlId', visible: true, align: 'center', valign: 'middle'},
            {title: '消费砖石数量', field: 'startCount', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
StartRecord.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        StartRecord.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加砖石流水
 */
StartRecord.openAddStartRecord = function () {
    var index = layer.open({
        type: 2,
        title: '添加砖石流水',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/startRecord/startRecord_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看砖石流水详情
 */
StartRecord.openStartRecordDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '砖石流水详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/startRecord/startRecord_update/' + StartRecord.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除砖石流水
 */
StartRecord.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/startRecord/delete", function (data) {
            Feng.success("删除成功!");
            StartRecord.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("startRecordId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询砖石流水列表
 */
StartRecord.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    StartRecord.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = StartRecord.initColumn();
    var table = new BSTable(StartRecord.id, "/startRecord/list", defaultColunms);
    table.setPaginationType("client");
    StartRecord.table = table.init();
});
