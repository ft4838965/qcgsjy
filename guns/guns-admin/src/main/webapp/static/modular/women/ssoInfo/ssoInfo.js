/**
 * 女用户管理初始化
 */
var SsoInfo = {
    id: "SsoInfoTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
SsoInfo.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},//truecheckbox: true,
            //{title: '', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '用户ID', field: 'ssoId', visible: true, align: 'center', valign: 'middle'},
            {title: '昵称', field: 'nickName', visible: true, align: 'center', valign: 'middle'},
            {title: '手机号', field: 'phone', visible: true, align: 'center', valign: 'middle'},
            //{title: '身份证', field: 'idCard', visible: true, align: 'center', valign: 'middle'},
            // {title: '生日', field: 'birthday', visible: true, align: 'center', valign: 'middle'},
            {title: '年龄', field: 'age', visible: true, align: 'center', valign: 'middle'},
            {title: '身高', field: 'tall', visible: true, align: 'center', valign: 'middle'},
            {title: '体重', field: 'weight', visible: true, align: 'center', valign: 'middle'},
            // {title: '个人优势', field: 'advantege', visible: true, align: 'center', valign: 'middle'},

            //{title: '标签集合', field: 'tagIds', visible: true, align: 'center', valign: 'middle'},
            //{title: '居住地区域', field: 'areaCode', visible: true, align: 'center', valign: 'middle'},
            //{title: '详细地址', field: 'address', visible: true, align: 'center', valign: 'middle'},
            //{title: '排序', field: 'sort', visible: true, align: 'center', valign: 'middle'},
            {title: '返现总额', field: 'total', visible: true, align: 'center', valign: 'middle'},
            {title: '封面审核', field: 'is_check', visible: true, align: 'center', valign: 'middle'},
            {title: '注册时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            //{title: '', field: 'updateTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
SsoInfo.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        SsoInfo.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加女用户
 */
SsoInfo.openAddSsoInfo = function () {
    var index = layer.open({
        type: 2,
        title: '添加女用户',
        area: ['100%', '100%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/ssoInfo/ssoInfo_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看女用户详情
 */
SsoInfo.openSsoInfoDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '女用户详情',
            area: ['100%', '100%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/ssoInfo/ssoInfo_update/' + SsoInfo.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除女用户
 */
SsoInfo.delete = function () {

    if (this.check()) {
        parent.layer.confirm("确定删除吗？", {
            btn: ['确定', '取消']
        }, function (index) {
            parent.layer.close(index);
            var ajax = new $ax(Feng.ctxPath + "/ssoInfo/delete", function (data) {
                Feng.success("删除成功!");
                SsoInfo.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("ssoInfoId",SsoInfo.seItem.id);
            ajax.start();
        }, function (index) {
            parent.layer.close(index);
        });
    }

};

/**
 * 查询女用户列表
 */
SsoInfo.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['phone'] = $("#phone").val();
    SsoInfo.table.refresh({query: queryData});
};
/**
 * 全部结算
 */
SsoInfo.cleanMoneyForAll = function () {
    parent.layer.confirm("确定全部结算吗？", {
        btn: ['确定', '取消']
    }, function (index) {
        parent.layer.close(index);
        var ajax = new $ax(Feng.ctxPath + "/ssoInfo/cleanMoney", function (data) {
            Feng.success("结算成功!");
            SsoInfo.table.refresh();
        }, function (data) {
            Feng.error("结算失败!" + data.responseJSON.message + "!");
        });
        ajax.set("ssoInfoId", 0);
        ajax.start();

    }, function (index) {
        parent.layer.close(index);
    });
}

/**
 * 单个结算
 */
SsoInfo.cleanMoneyForOne = function () {
    if (this.check()) {
        parent.layer.confirm("确定结算吗？", {
            btn: ['确定', '取消']
        }, function (index) {
            parent.layer.close(index);
            var ajax = new $ax(Feng.ctxPath + "/ssoInfo/cleanMoney", function (data) {
                Feng.success("结算成功!");
                SsoInfo.table.refresh();
            }, function (data) {
                Feng.error("结算失败!" + data.responseJSON.message + "!");
            });
            ajax.set("ssoInfoId",SsoInfo.seItem.id);
            ajax.start();
        }, function (index) {
            parent.layer.close(index);
        });
    }
}

/**
 * 导出会员资料
 */
SsoInfo.downloadFun = function () {
    window.location.href="/export/girls";
}

$(function () {
    var defaultColunms = SsoInfo.initColumn();
    var table = new BSTable(SsoInfo.id, "/ssoInfo/list", defaultColunms);
    table.setPaginationType("client");
    SsoInfo.table = table.init();
});
