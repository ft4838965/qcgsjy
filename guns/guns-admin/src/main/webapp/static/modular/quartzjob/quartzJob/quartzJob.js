/**
 * 定时任务管理初始化
 */
var QuartzJob = {
    id: "QuartzJobTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
QuartzJob.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '任务编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '任务名称', field: 'jobName', visible: true, align: 'center', valign: 'middle'},
            {title: '执行时间', field: 'fireDate', visible: true, align: 'center', valign: 'middle'},
            {title: '表达式', field: 'corn', visible: true, align: 'center', valign: 'middle'},
            {title: '任务类型', field: 'type', visible: true, align: 'center', valign: 'middle'},
            {title: '是否执行', field: 'isFire', visible: true, align: 'center', valign: 'middle'},
            {title: '参数', field: 'params', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
QuartzJob.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        QuartzJob.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加定时任务
 */
QuartzJob.openAddQuartzJob = function () {
    var index = layer.open({
        type: 2,
        title: '添加定时任务',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/quartzJob/quartzJob_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看定时任务详情
 */
QuartzJob.openQuartzJobDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '定时任务详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/quartzJob/quartzJob_update/' + QuartzJob.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除定时任务
 */
QuartzJob.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/quartzJob/delete", function (data) {
            Feng.success("删除成功!");
            QuartzJob.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("quartzJobId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询定时任务列表
 */
QuartzJob.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    QuartzJob.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = QuartzJob.initColumn();
    var table = new BSTable(QuartzJob.id, "/quartzJob/list", defaultColunms);
    table.setPaginationType("client");
    QuartzJob.table = table.init();
});
