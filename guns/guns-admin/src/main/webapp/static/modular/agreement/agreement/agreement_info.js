/**
 * 初始化各种《xxx协议》详情对话框
 */
var AgreementInfoDlg = {
    agreementInfoData : {}
};

/**
 * 清除数据
 */
AgreementInfoDlg.clearData = function() {
    this.agreementInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AgreementInfoDlg.set = function(key, val) {
    this.agreementInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AgreementInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
AgreementInfoDlg.close = function() {
    parent.layer.close(window.parent.Agreement.layerIndex);
}

/**
 * 收集数据
 */
AgreementInfoDlg.collectData = function() {
    this.agreementInfoData['content']=AgreementInfoDlg.heyifanEdit.txt.html();
    this
    .set('id').set('type');
}

/**
 * 提交添加
 */
AgreementInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();
    if($.trim(this.agreementInfoData.type).length<1){
        Feng.error("请选择协议类型");
        return false;
    }
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/agreement/add", function(data){
        if(data.code!=200){
            Feng.error("修改失败!" + data.message + "!");
        }else{
            Feng.success("添加成功!");
            window.parent.Agreement.table.refresh();
            AgreementInfoDlg.close();
        }
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.agreementInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
AgreementInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();
    if($.trim(this.agreementInfoData.type).length<1){
        Feng.error("请选择协议类型");
        return false;
    }
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/agreement/update", function(data){
        if(data.code!=200){
            Feng.error("修改失败!" + data.message + "!");
        }else{
            Feng.success("修改成功!");
            window.parent.Agreement.table.refresh();
            AgreementInfoDlg.close();
        }
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.agreementInfoData);
    ajax.start();
}

//自动替换转义
function escape2Html(str) {
    var arrEntities={'lt':'<','gt':'>','nbsp':' ','amp':'&','quot':'"'};
    return str.replace(/&(lt|gt|nbsp|amp|quot);/ig,function(all,t){return arrEntities[t];});
}


