function showAll(ele, boxH) {
    boxH = boxH || '212.5rem'
    $('.showAllBtn').bind('click',function(){
        $(this).toggleClass('active')
        if($(this).hasClass('active')){
            $(this).find('.w').html('收起全部')
            $(ele).css('height','auto')
        }else{
            $(this).find('.w').html('查看全部')
            $(ele).css('height',boxH)
        }
    })
}

function debounce(func, wait, immediate) {
    var timeout, args, context, timestamp, result;

    var later = function() {
        var last = new Date().getTime() - timestamp; // timestamp会实时更新
        if (last < wait && last >= 0) {
            timeout = setTimeout(later, wait - last);
        } else {
            timeout = null;
            if (!immediate) {
                result = func.apply(context, args);
                if (!timeout) context = args = null;
            }
        }
    };

    return function() {
        context = this;
        args = arguments;
        timestamp = new Date().getTime();
        var callNow = immediate && !timeout;

        if (!timeout) {
            timeout = setTimeout(later, wait);
        }
        if (callNow) {
            result = func.apply(context, args);
            context = args = null;
        }
        return result;
    };
}
/*
判断安卓还是苹果
return: a→安卓 i→苹果 o→其他
 */
function check_Android_IOS() {
    var u=navigator.userAgent;
    if(u.indexOf('Android') > -1 || u.indexOf('Adr') > -1|| u.indexOf('Linux') > -1){
        return "a";
    }else if(!!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/)){
        return "i"
    }else{
        return "o"
    }
}
// $(".idx-foot-select").click(function(){
//     $(this).find("i").toggleClass("up")
//     $(".idx-foot-items").slideToggle()
// })
// $('.idx-foot-items a').bind('click',function () {
//     var txt = $(this).html()
//     $('.txt').html(txt)
//     $('.linkBox i').toggleClass("up")
//     $(".idx-foot-items").slideToggle()
// })