/**
 * 短信记录管理初始化
 */
var SendSms = {
    id: "SendSmsTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
SendSms.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '类型', field: 'type', visible: true, align: 'center', valign: 'middle'},
            {title: '手机号码', field: 'phone', visible: true, align: 'center', valign: 'middle'},
            {title: '发送日期', field: 'createTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
SendSms.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        SendSms.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加短信记录
 */
SendSms.openAddSendSms = function () {
    var index = layer.open({
        type: 2,
        title: '添加短信记录',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/sendSms/sendSms_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看短信记录详情
 */
SendSms.openSendSmsDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '短信记录详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/sendSms/sendSms_update/' + SendSms.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除短信记录
 */
SendSms.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/sendSms/delete", function (data) {
            Feng.success("删除成功!");
            SendSms.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("sendSmsId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询短信记录列表
 */
SendSms.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    SendSms.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = SendSms.initColumn();
    var table = new BSTable(SendSms.id, "/sendSms/list", defaultColunms);
    table.setPaginationType("client");
    SendSms.table = table.init();
});
