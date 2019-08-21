/**
 *
 */
window.wangEditor.fullscreen = {
    // editor create之后调用
    init: function (editorSelector) {
        $(editorSelector + " .w-e-toolbar").append('<div class="w-e-menu"><a class="_wangEditor_btn_fullscreen" href="###" onclick="window.wangEditor.fullscreen.toggleFullscreen(\'' + editorSelector + '\')">全屏</a></div>');
    },
    toggleFullscreen: function (editorSelector) {
        $(editorSelector).toggleClass('fullscreen-editor');
        if ($(editorSelector + ' ._wangEditor_btn_fullscreen').text() == '全屏') {
            if (editorSelector == '#userEdit') {
                $("#contentZindex").css({"z-index": "0", "position": "relative"});
            }
            if (editorSelector == '#editor') {
                $("#userZindex").css({"z-index": "0", "position": "relative"});
            }
            $(editorSelector + ' ._wangEditor_btn_fullscreen').text('退出全屏');
        } else {
            if (editorSelector == '#userEdit') {
                $("#contentZindex").removeAttr("style");
            }
            if (editorSelector == '#editor') {
                $("#userZindex").removeAttr("style");
            }
            $(editorSelector + ' ._wangEditor_btn_fullscreen').text('全屏');
        }
    }
};


/**
 *
 */
window.wangEditor.heyifanfullscreen = {
    // editor create之后调用
    init: function (editorSelector) {
        $(editorSelector + " .w-e-toolbar").append('<div class="w-e-menu"><a class="_heyifanwangEditor_btn_fullscreen" href="###" onclick="window.wangEditor.heyifanfullscreen.toggleFullscreen(\'' + editorSelector + '\')">全屏</a></div>');
    },
    toggleFullscreen: function (editorSelector) {
        $(editorSelector).toggleClass('heyifanfullscreen-editor');
        if ($(editorSelector + ' ._heyifanwangEditor_btn_fullscreen').text() == '全屏') {
            $(editorSelector + ' ._heyifanwangEditor_btn_fullscreen').text('退出全屏');
        } else {
            $(editorSelector + ' ._heyifanwangEditor_btn_fullscreen').text('全屏');
        }
    }
};
