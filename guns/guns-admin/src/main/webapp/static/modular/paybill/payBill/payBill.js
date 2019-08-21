/**
 * 流水管理管理初始化
 */
var PayBill = {
    id: "PayBillTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
PayBill.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            // {title: '', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '用户ID', field: 'ssoId', visible: true, align: 'center', valign: 'middle'},
            {title: '支付方式', field: 'payType', visible: true, align: 'center', valign: 'middle'},
            {title: '支付的表的订单号', field: 'orderNo', visible: true, align: 'center', valign: 'middle'},
            {title: '微信/支付宝订单号', field: 'payNo', visible: true, align: 'center', valign: 'middle'},
            {title: '支付状态', field: 'payState', visible: true, align: 'center', valign: 'middle'},
            // {title: '支付回调最后一次的参数', field: 'payNotifyParams', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
PayBill.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        PayBill.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加流水管理
 */
PayBill.openAddPayBill = function () {
    var index = layer.open({
        type: 2,
        title: '添加流水管理',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/payBill/payBill_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看流水管理详情
 */
PayBill.openPayBillDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '流水管理详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/payBill/payBill_update/' + PayBill.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除流水管理
 */
PayBill.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/payBill/delete", function (data) {
            Feng.success("删除成功!");
            PayBill.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("payBillId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询流水管理列表
 */
PayBill.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    PayBill.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = PayBill.initColumn();
    var table = new BSTable(PayBill.id, "/payBill/list", defaultColunms);
    table.setPaginationType("client");
    PayBill.table = table.init();
});
