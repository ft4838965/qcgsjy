/**
 * 全局配置管理初始化
 */
var Setting = {
    id: "SettingTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Setting.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            //{title: '', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '投诉电话', field: 'servicePhone', visible: true, align: 'center', valign: 'middle'},
            {title: '阿里云云存储access_key', field: 'aliOssAccessKey', visible: true, align: 'center', valign: 'middle'},
            {title: '阿里云云存储access_id', field: 'aliOssAccessId', visible: true, align: 'center', valign: 'middle'},
            {title: '阿里云实名认证接口appcode', field: 'aliSmAppcode', visible: true, align: 'center', valign: 'middle'},
            {title: '云片网(短信)appkey', field: 'ypAppkey', visible: true, align: 'center', valign: 'middle'},
            {title: '高德地图key', field: 'gdKey', visible: true, align: 'center', valign: 'middle'},
            {title: '阿里支付宝appid', field: 'aliPayAppId', visible: true, align: 'center', valign: 'middle'},
            {title: '阿里支付宝私匙', field: 'aliPayAppPrivateKey', visible: true, align: 'center', valign: 'middle'},
            {title: '阿里支付宝公匙', field: 'aliPayAppPublicKey', visible: true, align: 'center', valign: 'middle'},
            {title: '阿里支付宝回调地址', field: 'aliPayAppAliNotifyUrl', visible: true, align: 'center', valign: 'middle'},
            {title: '微信支付appid', field: 'wechatPayAppId', visible: true, align: 'center', valign: 'middle'},
            {title: '微信支付商户号', field: 'wechatPayMchId', visible: true, align: 'center', valign: 'middle'},
            {title: '微信支付回调地址', field: 'wechatPayNotifyUrl', visible: true, align: 'center', valign: 'middle'},
            {title: '微信支付商户key', field: 'wechatPayKey', visible: true, align: 'center', valign: 'middle'},
            {title: '微信服务号appid', field: 'wechatAppId', visible: false, align: 'center', valign: 'middle'},
            {title: '微信服务号appsecret', field: 'wechatAppSecret', visible: false, align: 'center', valign: 'middle'},
            {title: '安卓版本号', field: 'androidVersion', visible: false, align: 'center', valign: 'middle'},
            //{title: '安卓APK下载地址', field: 'androidApkUrl', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Setting.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Setting.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加全局配置
 */
Setting.openAddSetting = function () {
    var index = layer.open({
        type: 2,
        title: '添加全局配置',
        area: ['100%', '100%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/setting/setting_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看全局配置详情
 */
Setting.openSettingDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '全局配置详情',
            area: ['100%', '100%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/setting/setting_update/' + Setting.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除全局配置
 */
Setting.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/setting/delete", function (data) {
            Feng.success("删除成功!");
            Setting.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("settingId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询全局配置列表
 */
Setting.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Setting.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Setting.initColumn();
    var table = new BSTable(Setting.id, "/setting/list", defaultColunms);
    table.setPaginationType("client");
    Setting.table = table.init();
});
