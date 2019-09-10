/**
 * 用户申请提现管理初始化
 */
var UseableBalance = {
    id: "UseableBalanceTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
UseableBalance.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '单号', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '用户', field: 'ssoId', visible: true, align: 'center', valign: 'middle'},
            {title: '提现金额', field: 'useableBalance', visible: true, align: 'center', valign: 'middle'},
            {title: '状态', field: 'state', visible: true, align: 'center', valign: 'middle'},
            {title: '申请时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '下账时间', field: 'payTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
UseableBalance.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        UseableBalance.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加用户申请提现
 */
UseableBalance.openAddUseableBalance = function () {
    var index = layer.open({
        type: 2,
        title: '添加用户申请提现',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/useableBalance/useableBalance_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看用户申请提现详情
 */
UseableBalance.openUseableBalanceDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '用户申请提现详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/useableBalance/useableBalance_update/' + UseableBalance.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除用户申请提现
 */
UseableBalance.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/useableBalance/delete", function (data) {
            Feng.success("删除成功!");
            UseableBalance.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("useableBalanceId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询用户申请提现列表
 */
UseableBalance.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['state'] = $("#state").val();
    UseableBalance.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = UseableBalance.initColumn();
    var table = new BSTable(UseableBalance.id, "/useableBalance/list", defaultColunms);
    table.setPaginationType("server");
    UseableBalance.table = table.init();
});

/**
 * 结算
 */
UseableBalance.pay=function () {
    if (UseableBalance.check()) {
        parent.layer.confirm("确定下账吗？", {
            btn: ['确定', '取消']
        }, function (index) {
            parent.layer.close(index);
            var ajax = new $ax(Feng.ctxPath + "/useableBalance/pay", function (data) {
                if(data.success=="ok"){
                    Feng.success("下账成功!");
                    UseableBalance.table.refresh();
                }else Feng.error(data.message);
            }, function (data) {
                Feng.error("返现失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id",UseableBalance.seItem.id);
            ajax.start();
        }, function (index) {
            parent.layer.close(index);
        });
    }
}
