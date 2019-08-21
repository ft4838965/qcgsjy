/**
 * 打赏提现管理初始化
 */
var SsoAccountFlow = {
    id: "SsoAccountFlowTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
SsoAccountFlow.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '用户ID', field: 'ssoId', visible: true, align: 'center', valign: 'middle'},
            {title: '消费来源', field: 'comeFrom', visible: true, align: 'center', valign: 'middle'},
            {title: '金额', field: 'money', visible: true, align: 'center', valign: 'middle'},
            // {title: '剩余可用金额', field: 'avilableBalance', visible: true, align: 'center', valign: 'middle'},
            // {title: '剩余冻结金额', field: 'frozenBalance', visible: true, align: 'center', valign: 'middle'},
            // {title: '业务类型--0/打赏--1/提现', field: 'businessType', visible: true, align: 'center', valign: 'middle'},
            {title: '业务名', field: 'businessName', visible: true, align: 'center', valign: 'middle'},
            // {title: '备注', field: 'note', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
SsoAccountFlow.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        SsoAccountFlow.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加打赏提现
 */
SsoAccountFlow.openAddSsoAccountFlow = function () {
    var index = layer.open({
        type: 2,
        title: '添加打赏提现',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/ssoAccountFlow/ssoAccountFlow_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看打赏提现详情
 */
SsoAccountFlow.openSsoAccountFlowDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '打赏提现详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/ssoAccountFlow/ssoAccountFlow_update/' + SsoAccountFlow.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除打赏提现
 */
SsoAccountFlow.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/ssoAccountFlow/delete", function (data) {
            Feng.success("删除成功!");
            SsoAccountFlow.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("ssoAccountFlowId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询打赏提现列表
 */
SsoAccountFlow.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    SsoAccountFlow.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = SsoAccountFlow.initColumn();
    var table = new BSTable(SsoAccountFlow.id, "/ssoAccountFlow/list", defaultColunms);
    table.setPaginationType("client");
    SsoAccountFlow.table = table.init();
});
