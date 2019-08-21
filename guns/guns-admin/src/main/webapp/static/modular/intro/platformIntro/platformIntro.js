/**
 * 平台介绍管理初始化
 */
var PlatformIntro = {
    id: "PlatformIntroTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
PlatformIntro.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '平台介绍', field: 'intro', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
PlatformIntro.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        PlatformIntro.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加平台介绍
 */
PlatformIntro.openAddPlatformIntro = function () {
    var index = layer.open({
        type: 2,
        title: '添加平台介绍',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/platformIntro/platformIntro_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看平台介绍详情
 */
PlatformIntro.openPlatformIntroDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '平台介绍详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/platformIntro/platformIntro_update/' + PlatformIntro.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除平台介绍
 */
PlatformIntro.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/platformIntro/delete", function (data) {
            Feng.success("删除成功!");
            PlatformIntro.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("platformIntroId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询平台介绍列表
 */
PlatformIntro.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    PlatformIntro.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = PlatformIntro.initColumn();
    var table = new BSTable(PlatformIntro.id, "/platformIntro/list", defaultColunms);
    table.setPaginationType("client");
    PlatformIntro.table = table.init();
});
