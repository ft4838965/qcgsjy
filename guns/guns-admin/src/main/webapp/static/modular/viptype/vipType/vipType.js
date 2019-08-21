/**
 * 会员类型管理初始化
 */
var VipType = {
    id: "VipTypeTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
VipType.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '名字', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '原价', field: 'originMoney', visible: true, align: 'center', valign: 'middle'},
            {title: '实价', field: 'realMoney', visible: true, align: 'center', valign: 'middle'},
            {title: '共送的砖量', field: 'totalStart', visible: true, align: 'center', valign: 'middle'},
            {title: '每天送砖量', field: 'dayStart', visible: true, align: 'center', valign: 'middle'},
            {title: '最高送', field: 'topStart', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
VipType.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        VipType.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加会员类型
 */
VipType.openAddVipType = function () {
    var index = layer.open({
        type: 2,
        title: '添加会员类型',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/vipType/vipType_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看会员类型详情
 */
VipType.openVipTypeDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '会员类型详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/vipType/vipType_update/' + VipType.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除会员类型
 */
VipType.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/vipType/delete", function (data) {
            Feng.success("删除成功!");
            VipType.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("vipTypeId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询会员类型列表
 */
VipType.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    VipType.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = VipType.initColumn();
    var table = new BSTable(VipType.id, "/vipType/list", defaultColunms);
    table.setPaginationType("client");
    VipType.table = table.init();
});
