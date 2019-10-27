/**
 * post封装
 * @param url 请求路径
 * @param parameter 请求参数
 * @param callback 请求成功回调函数
 * @param msg 请求过程中提示语
 * */
function postAjax(url, parameter, callback, msg) {
    var layerIndex = layer.msg(!msg ? '数据上传中...' : msg, {
        icon: 16,
        shade: 0.01,
        shadeClose: false,
        time: 0
    });
    $.ajax({
        url: url,
        type: "post",
        data: parameter,
        dataType: "json",
        traditional: true,//必须设成 true
        success: function (data) {
            if ((typeof callback === "function") && data.status === 1) {
                callback(data);     //调用传入的回调函数
            }else{

                layer.msg(data.msg, {
                    icon: data.status <= 2 ? data.status : 2,
                    shade: 0.01,
                    shadeClose: false,
                    time: 1500
                }, function () {
                    layer.close(layerIndex);
                    if (data.status !== 1) {
                        if (data.status === 3 || data.status === 5) {
                            top.location.href = "/";
                        }
                    }else{
                        try {
                            parent.refresh(parent.layer.getFrameIndex(window.name));
                        } catch (e2) {
                            try {
                                refresh(parent.layer.getFrameIndex(window.name));
                            } catch (e) {
                            }
                        }
                    }
                });
            }
        },
        error: function () {
            layer.msg("服务器连接失败!", {
                icon: 2,
                time: 2000
            });
        }
    })
}

function ajaxFileUpLoad(url, parameter, callback, msg) {
    var layerIndex = layer.msg(msg === undefined ? '数据上传中...' : msg, {
        icon: 16,
        shade: 0.01,
        shadeClose: false,
        time: 0
    });
    $.ajax({
        url: url,
        type: 'post',
        data: parameter,
        cache: false,
        processData: false,
        contentType: false,
        async: true,
        success: function (data) {
            if ((typeof callback === "function") && data.status === 1) {
                callback(data);     //调用传入的回调函数
            }else{
                layer.msg(data.msg, {
                    icon: data.status <= 2 ? data.status : 2,
                    shade: 0.01,
                    shadeClose: false,
                    time: 1500
                }, function () {
                    layer.close(layerIndex);
                    if (data.status !== 1) {
                        if (data.status === 3 || data.status === 5) {
                            top.location.href = "/";
                        }
                    }else{
                        try {
                            parent.refresh(parent.layer.getFrameIndex(window.name));
                        } catch (e2) {
                            try {
                                refresh(parent.layer.getFrameIndex(window.name));
                            } catch (e) {
                            }
                        }
                    }
                });
            }
        },
        error: function () {
            layer.msg("服务器连接失败!", {
                icon: 2,
                time: 2000
            });
        }
    });

}
function loadUpdate(url, parameter, callback, element) {
    element.attr('disabled',true);
    element.attr('disabled','disabled');
    element.prop('disabled','disabled');
    $.ajax({
        url: url,
        type: 'post',
        data: parameter,
        cache: false,
        processData: false,
        contentType: false,
        async: true,
        success: function (data) {
            element.attr('disabled',false);
            element.removeAttr("disabled");
            element.attr('disabled','');
            layer.msg(data.msg, {
                icon: data.status <= 2 ? data.status : 2,
                shade: 0.01,
                shadeClose: false,
                time: 1000
            }, function () {
                callback(data);
            });
        },
        error: function () {
            element.attr('disabled',false);
            element.removeAttr("disabled");
            element.attr('disabled','');
            layer.msg("服务器连接失败!", {
                icon: 2,
                time: 1500
            });
        }
    });
}

/**
 * post封装  simple
 * @param url 请求路径
 * @param parameter 请求参数
 * @param callback 请求成功回调函数
 * */
function postAjaxSimple(url, parameter, callback) {
    $.ajax({
        url: url,
        type: "post",
        data: parameter,
        dataType: "json",
        traditional: true,//必须设成 true
        success: function (data) {
            if ((typeof callback === "function") && data.status === 1) {
                callback(data);     //调用传入的回调函数
            }else{
                layer.msg(data.msg, {
                    icon: data.status <= 2 ? data.status : 2,
                    shade: 0.01,
                    shadeClose: false,
                    time: 1500
                }, function () {
                    if (data.status !== 1) {
                        if (data.status === 3 || data.status === 5) {
                            top.location.href = "/";
                        }
                    }
                });
            }
        },
        error: function () {
            layer.msg("服务器连接失败!", {
                icon: 2,
                time: 2000
            });
        }
    })
}


/**
 * 配合file 使用 加载显示图片
 * @param current  file 元素的 id 值
 * @param img 显示图片框的 id 值
 */
function loadImg(current, img) {
    var objUrl = getObjectURL(document.getElementById(current).files[0]);
    if (objUrl) {
        $("#" + img).attr("src", objUrl);
    }
}

function getObjectURL(file) {
    var url = null;
    if (window.createObjectURL !== undefined) { // basic
        url = window.createObjectURL(file);
    } else if (window.URL !== undefined) { // mozilla(firefox)
        url = window.URL.createObjectURL(file);
    } else if (window.webkitURL !== undefined) { // webkit or chrome
        url = window.webkitURL.createObjectURL(file);
    }
    return url;
}

// 禁止右键菜单
//document.oncontextmenu = function(){ return false; };
// 禁止文字选择
//document.onselectstart = function(){ return false; };
// 禁止复制
//document.oncopy = function(){ return false; };
// 禁止剪切
//document.oncut = function(){ return false; };
// 禁止粘贴
//document.onpaste = function(){ return false; };