/**
 * 邀请返现管理初始化
 */
var Invited = {
    id: "InvitedTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Invited.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            // {title: '主键', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '用户', field: 'ssoId', visible: true, align: 'center', valign: 'middle'},
            {title: '消费', field: 'ssoSpend', visible: true, align: 'center', valign: 'middle'},
            {title: '邀请用户', field: 'beSsoId', visible: true, align: 'center', valign: 'middle'},
            {title: '邀请用户性别', field: 'sex', visible: true, align: 'center', valign: 'middle'},
            {title: '邀请用户消费', field: 'beSsoSpend', visible: true, align: 'center', valign: 'middle'},
            {title: '返现状态', field: 'status', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Invited.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Invited.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加邀请返现
 */
Invited.openAddInvited = function () {
    var index = layer.open({
        type: 2,
        title: '添加邀请返现',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/invited/invited_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看邀请返现详情
 */
Invited.openInvitedDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '邀请返现详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/invited/invited_update/' + Invited.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除邀请返现
 */
Invited.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/invited/delete", function (data) {
            Feng.success("删除成功!");
            Invited.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("invitedId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询邀请返现列表
 */
Invited.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Invited.table.refresh({query: queryData});
};

/**
 * 单个返现
 */
cleanMoneyForOne = function () {
    if (Invited.check()) {
        parent.layer.confirm("确定返现吗？", {
            btn: ['确定', '取消']
        }, function (index) {
            parent.layer.close(index);
            var ajax = new $ax(Feng.ctxPath + "/invited/giveMoney", function (data) {
                Feng.success("返现成功!");
                Invited.table.refresh();
            }, function (data) {
                Feng.error("返现失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id",Invited.seItem.id);
            ajax.start();
        }, function (index) {
            parent.layer.close(index);
        });
    }
}

$(function () {
    var defaultColunms = Invited.initColumn();
    var table = new BSTable(Invited.id, "/invited/list", defaultColunms);
    table.setPaginationType("client");
    Invited.table = table.init();
});
