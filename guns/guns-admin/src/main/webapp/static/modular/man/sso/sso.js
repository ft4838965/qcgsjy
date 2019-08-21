/**
 * 男用户管理初始化
 */
var Sso = {
    id: "SsoTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Sso.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            // {title: '用户编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
            //{title: '头像', field: 'avatar', visible: true, align: 'center', valign: 'middle'},
            {title: '用户ID', field: 'ssoId', visible: true, align: 'center', valign: 'middle'},
            {title: '手机号', field: 'phone', visible: true, align: 'center', valign: 'middle'},
            {title: '令牌', field: 'token', visible: true, align: 'center', valign: 'middle'},
            {title: '昵称', field: 'nickName', visible: true, align: 'center', valign: 'middle'},
            //{title: '性别', field: 'sex', visible: true, align: 'center', valign: 'middle'},
            //{title: '封面图', field: 'bigAvatar', visible: true, align: 'center', valign: 'middle'},
            //{title: '个性签名', field: 'signature', visible: true, align: 'center', valign: 'middle'},
            {title: '是否会员', field: 'is_vip', visible: true, align: 'center', valign: 'middle'},
            {title: '会员类型', field: 'vip_type', visible: true, align: 'center', valign: 'middle'},
            // {title: '用户状态', field: 'state', visible: true, align: 'center', valign: 'middle'},
            {title: '注册时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            //{title: '', field: 'updateTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Sso.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Sso.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加男用户
 */
Sso.openAddSso = function () {
    var index = layer.open({
        type: 2,
        title: '添加男用户',
        area: ['100%', '100%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/sso/sso_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看男用户详情
 */
Sso.openSsoDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '男用户详情',
            area: ['100%', '100%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/sso/sso_update/' + Sso.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除男用户
 */
Sso.delete = function () {
    if (this.check()) {
        parent.layer.confirm("确定删除吗？", {
            btn: ['确定', '取消']
        }, function (index) {
            parent.layer.close(index);
            var ajax = new $ax(Feng.ctxPath + "/sso/delete", function (data) {
                Feng.success("删除成功!");
                Sso.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("ssoId",Sso.seItem.id);
            ajax.start();
        }, function (index) {
            parent.layer.close(index);
        });
    }

};

/**
 * 查询男用户列表
 */
Sso.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['phone'] = $("#phone").val();
    Sso.table.refresh({query: queryData});
};

/**
 * 查询会员列表
 */
Sso.getVip = function(){
    var queryData = {};
    var gg =  $("#condition").val();
    $("#condition").val("查询所有会员用户");
    queryData['condition'] = $("#condition").val();
    $("#condition").val(gg);
    Sso.table.refresh({query: queryData});
}

/**
 * 查询会员列表
 */
Sso.getAll = function(){
    var queryData = {};
    queryData['condition'] = null;
    Sso.table.refresh({query: queryData});
}

$(function () {
    var defaultColunms = Sso.initColumn();
    var table = new BSTable(Sso.id, "/sso/list", defaultColunms);
    table.setPaginationType("client");
    Sso.table = table.init();
});
