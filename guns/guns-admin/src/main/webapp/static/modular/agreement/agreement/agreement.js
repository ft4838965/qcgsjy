/**
 * 各种《xxx协议》管理初始化
 */
var Agreement = {
    id: "AgreementTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Agreement.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '协议类型', field: 'type', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Agreement.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Agreement.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加各种《xxx协议》
 */
Agreement.openAddAgreement = function () {
    var index = layer.open({
        type: 2,
        title: '添加各种《xxx协议》',
        area: ['100%', '100%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/agreement/agreement_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看各种《xxx协议》详情
 */
Agreement.openAgreementDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '各种《xxx协议》详情',
            area: ['100%', '100%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/agreement/agreement_update/' + Agreement.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除各种《xxx协议》
 */
Agreement.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/agreement/delete", function (data) {
            Feng.success("删除成功!");
            Agreement.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("agreementId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询各种《xxx协议》列表
 */
Agreement.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Agreement.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Agreement.initColumn();
    var table = new BSTable(Agreement.id, "/agreement/list", defaultColunms);
    table.setPaginationType("server");
    Agreement.table = table.init();
});
