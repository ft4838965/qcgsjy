$(function () {
//弃用,改为H5页面接口使用模版输出,提升网页速度
//     $.post("/userApi/homepage",{uid:GetRequest().id},function (r) {
//         console.log(r)
//         if(r.success=='ok'){
//         }else{
//             alert(r.message);
//         }
//     })
});
function GetRequest() {
    var url = location.search; //获取url中"?"符后的字串
    var theRequest = new Object();
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for (var i = 0; i < strs.length; i++) {
            theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
        }
    }
    return theRequest;
}