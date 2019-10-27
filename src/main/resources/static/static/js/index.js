$(document).ready(function () {
    layui.config({
        base: '/static/layui/' //静态资源所在路径
    }).extend({
        index: 'lay/modules' //主入口模块
    }).use(['layer'], function () {
        var layer = layui.layer;
    });
});

//发布程序
function releaseProgram() {
    layer.open({
        type: 2,
        title: '发布程序',
        shade: 0.3,
        shadeClose: false,
        area: ['70%', '80%'],
        content: "/releaseProgram.html"
    });
}

function updateProgram(th) {
    var data = $(th);
    var serverId = data.attr("data-serverId");
    var serverName = data.attr("data-serverName");
    var savePath = encodeURI(data.attr("data-savePath"));
    var runParam = encodeURI(data.attr("data-runParam"));
    layer.open({
        type: 2,
        title: '更新程序',
        shade: 0.3,
        shadeClose: false,
        area: ['70%', '80%'],
        content: "/updateProgram.html?serverId=" + serverId + "&serverName=" +serverName+ "&savePath=" +savePath+ "&runParam="+runParam
    });
}