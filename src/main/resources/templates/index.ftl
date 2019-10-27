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
    <title>系统发布</title>
    <script type="text/javascript" src="/static/js/jquery-3.3.1.min.js"></script>
    <link rel="stylesheet" href="/static/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="/static/css/home.css" media="all">
    <link rel="stylesheet" href="/static/css/public.css" media="all">
    <script src="/static/layui/layui.js"></script>
    <script src="/static/js/public.js"></script>
    <style>
        html,
        body,
        #container {
            width: 100%;
            height: auto;
            overflow: auto;
            background-color: #f0f3fa;
        }

        #info_box {
            width: 98%;
            margin-left: 1%;
            margin-top: 1%;
        }

        .layui-upload-img {
            width: 100%;
            height: 100%;
            cursor: pointer;
        }

        .update {
            position: absolute;
            top: 33%;
            right: 20px;
        }
        .layui-layer-title{
            background-color:#3A6CAF !important;
            color: white!important;
        }
    </style>
</head>
<body>
<div class="layui-card layui-fluid" style="margin-bottom: 0;padding: 0!important;">
    <button class="layui-btn" onclick="releaseProgram()">发布程序</button>
</div>
<div id="container">
    <div id="info_box">
        <#if data??>
            <#list data! as item>
                <div class="layui-col-xs12 layui-tab-div">
                    <div class="layui-card">
                        <div class="layui-card-body">
                            <div class="layui-form-item"><label class="layui-form-label"><span>服务ID号：</span></label>
                                <div class="layui-input-block">${item.serverId!}</div>
                            </div>
                            <div class="layui-form-item"><label class="layui-form-label"><span>服务名称：</span></label>
                                <div class="layui-input-block">${item.serverName!}</div>
                            </div>
                            <div class="layui-form-item"><label class="layui-form-label"><span>项目路径：</span></label>
                                <div class="layui-input-block">${item.savePath!}</div>
                            </div>
                            <div class="layui-form-item"><label class="layui-form-label"><span>启动日志：</span></label>
                                <div class="layui-input-block">${item.runLogPath!}</div>
                            </div>
                            <div class="layui-form-item"><label class="layui-form-label"><span>运行参数：</span></label>
                                <div class="layui-input-block">${item.runParam!}
                                </div>
                            </div>
                            <button class="layui-btn update"
                                    data-serverId="${item.serverId!}"
                                    data-serverName="${item.serverName!}"
                                    data-savePath="${item.savePath!}"
                                    data-runParam="${item.runParam!?replace("\"","'")}" onclick="updateProgram(this)">更新程序
                            </button>
                        </div>
                    </div>
                </div>
            </#list>
        </#if>
    </div>
</div>
<script src="/static/js/index.js"></script>
</body>
</html>