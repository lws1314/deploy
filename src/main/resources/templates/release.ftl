<html>
<head>
    <meta charset="utf-8">
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <meta name="format-detection" content="telephone=no"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="white">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8"/>
    <link rel="shortcut icon" href="/static/img/logoioc.png" type="image/x-icon"/>
    <title>信息查看</title>
    <script type="text/javascript" src="/static/js/jquery-3.3.1.min.js"></script>
    <link rel="stylesheet" href="/static/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="/static/css/public.css" media="all">
    <script src="/static/layui/layui.js"></script>
    <script src="/static/js/public.js"></script>
    <style>
        html,
        body {
            width: 100%;
            height: auto;
            overflow: auto;
            background-color: white;
        }

        #container {
            width: 97%
        }

        #add_div {
            -moz-box-shadow: 0 0 1px #ccc;
            -webkit-box-shadow: 0 0 1px #ccc;
            box-shadow: 0 0 1px #ccc;
            text-align: center;
            position: fixed;
            height: 50px;
            width: 100%;
            background-color: white;
            bottom: 0;
        }

        .addButton {
            margin-top: 10px;
            background-color: #3A6CAF !important;
        }

        #file {
            position: absolute !important;
            opacity: 0 !important;
            z-index: 10;
            top: 0;
            cursor: pointer;
        }

        #file_str {
            z-index: 0;
        }

        .msg {
            font-size: 14px;
            margin: 5px 0 5px 8px;
            letter-spacing: 1px;
        }
    </style>
</head>
<body>

<div id="container" class="layui-card" style="margin-top: 10px;margin-right: 2%">
    <form class="layui-form" id="form" action="" onsubmit="false">
        <div class="layui-form-item">
            <label class="layui-form-label">服务ID号:</label>
            <div class="layui-input-block">
                <input type="text" name="serverId" lay-verify="required" autocomplete="off" placeholder="服务ID号(必填)"
                <#if data??>value="${data["serverId"]!}" readonly</#if> class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">服务名称:</label>
            <div class="layui-input-block">
                <input type="text" name="serverName" autocomplete="off" placeholder="服务名称(选填)"
                       <#if data??>value="${data["serverName"]!}" readonly</#if> class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">存储路径:</label>
            <div class="layui-input-block">
                <input type="text" name="savePath" autocomplete="off" placeholder="存储路径(必填)"
                       <#if data??>value="${data["savePath"]!}" readonly</#if> class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">运行参数:</label>
            <div class="layui-input-block">
                <input type="text" name="runParam" autocomplete="off" placeholder="运行参数(选填)"
                       <#if data??>value="${data["runParam"]!}" readonly</#if> class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">运行文件:</label>
            <div class="layui-input-block">
                <input type="text" id="file_str" placeholder="jar(必选)" autocomplete="off" lay-verify="required"
                       class="layui-input">
                <input type="file" id="file" accept=".jar" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">日志输出:</label>
            <div class="layui-input-block">
                <div class="scroll_bar" style="height: 180px;border: 1px #cccccc solid; overflow: auto" id="log"></div>
            </div>
        </div>
        <div class="layui-form-item layui-form-text" id="operate" style="display: none">
            <label class="layui-form-label">操作:</label>
            <div class="layui-input-block">
                <button class="layui-btn layui-btn-sm layui-btn-normal" style="display: none" lay-submit lay-filter="delFile" id="delFile">
                    删除执行文件
                </button>
                <button class="layui-btn layui-btn-sm layui-btn-normal" style="display: none" lay-submit lay-filter="viewLog" id="viewLog">
                    查看运行日志
                </button>
            </div>
        </div>

        <div id="add_div">
            <button class="layui-btn layui-btn-sm layui-btn-normal addButton" data-mark="<#if data??>2<#else >1</#if>" lay-submit lay-filter="addButton">
                <#if data??>更新<#else >发布</#if>
            </button>
        </div>
    </form>
</div>
<script src="/static/js/webSocket.js"></script>
<script>
    $(document).ready(function () {
        layui.use(['form', 'layedit'], function () {
            var form = layui.form
                    , layer = layui.layer
                    , layedit = layui.layedit;
            form.on('submit(addButton)', function (data) {
                var formData = new FormData();
                var serialize = $("#form").serializeArray();
                for (var key in serialize) {
                    var map = serialize[key];
                    formData.append(map.name, map.value);
                }
                //jar 文件
                var jar = $('#file')[0].files[0];
                formData.append("file", jar);

                var elem = data.elem;
                formData.append("status", elem.attributes[1].value);

                addParam("正在上传数据.......");
                loadUpdate('/deploy/releaseJson', formData, function (data) {
                    if (data.status === 1){
                        var viewLog =  $("#viewLog");
                        viewLog.show();
                        viewLog.attr("data-code", data.data);
                        $("#operate").show();
                    }else if (data.status === 6) {
                        var delFile =  $("#delFile");
                        delFile.show();
                        delFile.attr("data-code", data.data);
                        $("#operate").show();
                    }
                },$("#addButton"));
                return false;
            });

            form.on('submit(delFile)', function (data) {
                var code = $("#delFile").attr("data-code");
                var field = data.field;
                postAjax("/deploy/delFile", {code: code, path: field.savePath}, function (data) {
                    $("#operate").hide();
                    $("#delFile").hide();
                });
                return false;
            });
            //查看启动日志
            form.on('submit(viewLog)', function (data) {
                var code = $("#viewLog").attr("data-code");
                addParam("--------------------------------------------------------");
                addParam("开始读取【"+code+"】的启动日志内容.......");
                postAjaxSimple("/deploy/runLog", {path: code},function (data) {
                    addParam("请稍后正在读取【"+code+"】的启动日志内容.......");
                    $("#operate").hide();
                });
                return false;
            });

            $("#file").change(function () {
                var th = $(this);
                $("#file_str").val(th.val());
            })

        });
    });

    function addParam(msg) {
        var mainContainer = $('.scroll_bar');
        var offsetTop = mainContainer.offset().top;
        var scrollTop = mainContainer.scrollTop();
        $("#log").append($("<p class='msg'>" + msg + "</p>"));
        mainContainer.scrollTop(offsetTop + scrollTop);
    }

    msgCallback(function (message) {
        addParam(message)
    })

</script>

</body>
</html>