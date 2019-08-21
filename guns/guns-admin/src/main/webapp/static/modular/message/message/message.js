/**
 * 消息管理管理初始化
 */
var Message = {
    id: "MessageTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Message.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
            //{title: '发出这条消息的用户（0/官方  sso_id/具体用户）', field: 'ssoId', visible: true, align: 'center', valign: 'middle'},
            {title: '谁能查看', field: 'messageSsoId', visible: true, align: 'center', valign: 'middle'},
            //{title: '消息类型： 0/官方消息  1/用户消息', field: 'type', visible: true, align: 'center', valign: 'middle'},
            {title: '消息内容', field: 'content', visible: true, align: 'center', valign: 'middle'},
            //{title: '具体消息类型 ： 0/打赏 1/提现  2/评论  3/点赞 4/通知', field: 'officialMessageType', visible: true, align: 'center', valign: 'middle'},
            //{title: '该条消息是否已被点击查看(0:否,1:是)', field: 'look', visible: true, align: 'center', valign: 'middle'},
            //{title: '作品的id', field: 'workId', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Message.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Message.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加消息管理
 */
Message.openAddMessage = function () {
    var index = layer.open({
        type: 2,
        title: '添加消息管理',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/message/message_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看消息管理详情
 */
Message.openMessageDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '消息管理详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/message/message_update/' + Message.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除消息管理
 */
Message.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/message/delete", function (data) {
            Feng.success("删除成功!");
            Message.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("messageId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询消息管理列表
 */
Message.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Message.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Message.initColumn();
    var table = new BSTable(Message.id, "/message/list", defaultColunms);
    table.setPaginationType("client");
    Message.table = table.init();
});
