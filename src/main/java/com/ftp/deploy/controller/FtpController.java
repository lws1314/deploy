package com.ftp.deploy.controller;

import com.alibaba.fastjson.JSONObject;
import com.ftp.deploy.config.entity.Digit;
import com.ftp.deploy.config.entity.ResultInfo;
import com.ftp.deploy.config.exception.CustomException;
import com.ftp.deploy.utils.*;
import com.ftp.deploy.utils.socket.SocketUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * @author ZERO
 */
@Controller()
@RequestMapping("/deploy")
public class FtpController {
    /**
     * 项目保存路径
     */
    private static final String SAVE_PATH = "savePath";
    /**
     * windows 服务id
     */
    private static final String SERVER_ID = "serverId";
    /**
     * 服务名称
     */
    public static final String SERVER_NAME = "serverName";
    /**
     * 运行参数
     */
    public static final String RUN_PARAM = "runParam";

    private static final String STATUS = "status";

    private static final String BAT_START = "start.bat";

    private static final String BAT_STOP = "stop.bat";

    private static final String BAT_INSTALL = "install.bat";

    private static final String[] EX_NAME = {"install.bat", "restart.bat", "start.bat", "stop.bat", "uninstall.bat"};

    private static final String[] PROGRAM = {"/static/deploy/config.exe"};


    private static final String RUN_LOG = "console" + File.separator + "config.out.log";

    private static final String CODE_MARK = "code";
    /**
     * 表示程序执行成功
     */
    private static final int SUCCESS = 0;
    @Resource
    private HttpServletRequest request;


    @RequestMapping("/runLog")
    @ResponseBody
    public ResultInfo runLog(String path) {
        if (StringUtils.isEmpty(path)) {
            throw new CustomException(Digit.TWO.getInt(), "缺少参数");
        }

        File file = new File(path);
        if (file.exists()) {
            new ExecuteLog(file).start();
            return ResultInfo.success();
        } else {
            return ResultInfo.error("文件不存在,请重新尝试一下");
        }
    }

    @RequestMapping("/delFile")
    @ResponseBody
    public ResultInfo delFile(@RequestParam String path, @RequestParam String code) {
        if (StringUtils.isEmpty(path)) {
            return ResultInfo.error("缺少路径参数");
        }
        HttpSession session = request.getSession();
        String codeMark = Optional.ofNullable((String) session.getAttribute(CODE_MARK)).orElse("-1");
        if (codeMark.equals(code)) {
            session.removeAttribute(CODE_MARK);
            File file = new File(path);
            try {
                CommonUtils.delFile(file);
                return ResultInfo.success("删除成功");
            } catch (Exception e) {
                return ResultInfo.error("删除失败");
            }
        }
        return ResultInfo.error("标识错误禁止访问");
    }

    @PostMapping("releaseJson")
    @ResponseBody
    public ResultInfo releaseJson(MultipartFile file, @RequestParam Map<String, Object> param) {
        String savePath = (String) param.get(SAVE_PATH);
        String serverId = (String) param.get(SERVER_ID);
        try {
            if (!StringUtils.isEmpty(savePath) && !StringUtils.isEmpty(serverId)) {
                socketMsgSend("开始处理.....");
                String status = (String) param.get(STATUS);
                //验证jar是否存在
                File verify = new File(savePath, serverId + ".jar");
                boolean exists = verify.exists();
                boolean bool = true;
                if (exists) {
                    //备份旧项目程序
                    bool = backup(savePath, serverId);
                    if (bool){
                        socketMsgSend("开始停止服务........");
                        if (submit(savePath, BAT_STOP)) {
                            socketMsgSend("服务已停止........");
                            socketMsgSend("开始休眠1.5秒........");
                            Thread.sleep(1500);
                            socketMsgSend("开始更新jar........");
                        } else {
                            socketMsgSend("<span style='color:yellowy;'>服务停止失败！</span>");
                            socketMsgSend("尝试更新jar包");
                        }
                    }
                }else{
                    //不存在创建执行文件
                    //安装服务
                    socketMsgSend("开始创建服务所需文件......");
                    if (createExecuteFile(savePath, serverId, param)) {
                        socketMsgSend("服务所需文件创建成功");
                        socketMsgSend("开始安装服务......");
                        boolean az = submit(savePath, BAT_INSTALL);
                        if (az) {
                            socketMsgSend("<span style='color:green;'>服务安装完成</span>");
                        } else {
                            socketMsgSend("<span style='color:red;'>服务安装失败</span>");
                            HttpSession session = request.getSession();
                            String verifyCodeStr = CommonUtils.getVerifyCodeStr();
                            session.setAttribute(CODE_MARK, verifyCodeStr);
                            return result(Digit.SIX.getInt(), "服务安装失败", verifyCodeStr);
                        }
                    }
                }
                if (bool){
                    //保存执行jar
                    boolean jar = CommonUtils.uploadFile(file, savePath, serverId);
                    if (jar) {
                        socketMsgSend("jar包更新完成");
                        // 等待程序执行结束并输出状态
                        socketMsgSend("开始启动服务.......");
                        if (submit(savePath, BAT_START)) {
                            socketMsgSend("<span style='color:green;'>服务启动完成</span>");
                            //运行地址路径
                            String runLogPath = savePath + File.separator + RUN_LOG;
                            //运行完成后保存此次数据记录到磁盘以便下一次进行数据回显
                            if (!exists && "1".equals(status)) {
                                saveDisk(param, runLogPath);
                            }
                            return result(Digit.ONE.getInt(), "执行成功", runLogPath);
                        } else {
                            return result(Digit.TWO.getInt(), "执行失败", null);
                        }
                    } else {
                        socketMsgSend("jar包更新失败");
                    }
                }

            } else {
                return ResultInfo.error("缺少参数");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return result(Digit.TWO.getInt(), "执行BAT文件失败", null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result(Digit.TWO.getInt(), "处理失败", null);
    }

    private ResultInfo result(Integer status, String msg, String runLogPath) {
        return new ResultInfo(status, msg, runLogPath);
    }

    /**
     * 执行BAT 执行文件
     *
     * @param savePath bat 文件路径
     * @param batName  bat文件名称
     * @return 成功 true
     * @throws InterruptedException 线程 异常
     * @throws IOException          io异常
     */
    private boolean submit(String savePath, String batName) throws InterruptedException, IOException {
        //执行文件存放路径
        String bin = savePath + File.separator + "bin";
        //执行指令
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(bin + File.separator + batName);
        Process start = builder.start();
        new StreamPrint(start.getInputStream()).start();
        start.waitFor();
        start.destroy();
        builder = null;
        return start.exitValue() == SUCCESS;
    }

    /**
     * 创建执行文件
     *
     * @param savePath 文件保存路径
     * @param serverId 服务id
     * @param param    参数
     * @return
     */
    private boolean createExecuteFile(String savePath, String serverId, Map<String, Object> param) {
        //读取执行文件
        String configName = PROGRAM[0];
        InputStream configInput = this.getClass().getResourceAsStream(configName);
        boolean config = CommonUtils.uploadFile(configInput, savePath, configName.substring(configName.lastIndexOf("/") + 1));
        //生成xml 配置文件
        boolean xml = JDomXml.createXml(serverId, savePath, "config.xml", param);
        //生成配置文件
        boolean install = CommonUtils.outputFile(savePath, EX_NAME[0]);
        boolean restart = CommonUtils.outputFile(savePath, EX_NAME[1]);
        boolean start = CommonUtils.outputFile(savePath, EX_NAME[2]);
        boolean stop = CommonUtils.outputFile(savePath, EX_NAME[3]);
        boolean uninstall = CommonUtils.outputFile(savePath, EX_NAME[4]);
        //判断是否执行完成
        return config && install && restart && start && stop && uninstall && xml;
    }

    /**
     * 保存此次服务信息
     *
     * @param param
     * @param runLogPath
     */
    private static void saveDisk(Map<String, Object> param, String runLogPath) {
        param.put("runLogPath", runLogPath);
        String record = JSONObject.toJSONString(param);
        CommonUtils commonUtils = new CommonUtils();
        boolean b = CommonUtils.saveFile(record, new File(commonUtils.getJarPath(), CommonUtils.FILE_NAME_PROGRAM));
    }

    private void socketMsgSend(String message) {
        SocketUtil.sendMessage(message);
    }

    private boolean backup(String savePath,String fileName){
        socketMsgSend("开始备份旧程序...");
        try {
            String file = fileName + ".jar";
            InputStream is = new FileInputStream(new File(savePath, file));
            String yyyyMMddHHmmss = LocalDateTimeUtils.formatNow("yyyyMMddHHmmss");
            String path = savePath + File.separator + "back"+ File.separator +yyyyMMddHHmmss;
            socketMsgSend("旧程序备份路径》"+path);
            boolean b = CommonUtils.uploadFile(is, path, file);
            if (b){
                socketMsgSend("<span style='color:green;'>旧程序备份完成</span>");
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        socketMsgSend("<span style='color:red;'>旧程序备份失败</span>");
        socketMsgSend("<span style='color:red;'>此次更新程序结束</span>");
        return false;
    }

}
