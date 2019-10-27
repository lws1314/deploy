package com.ftp.deploy.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 公共的工具类
 *
 * @author ZERO
 */
public class CommonUtils {

    public static final String FILE_NAME_PROGRAM = "program.txt";

    /**
     * @param file           上传的文件
     * @param filePath       上传文件的路径
     * @param customFileName 自定义文件名称
     * @return
     */
    public static boolean uploadFile(MultipartFile file, String filePath, String customFileName) {
        if (file != null && !file.isEmpty()) {
            String fileName = customFileName + ".jar";
            File newFile = new File(filePath);
            boolean mkdirs = true;
            if (!newFile.exists()) {
                mkdirs = newFile.mkdirs();
            }
            if (mkdirs) {
                File uploadFile = new File(newFile, fileName);
                try {
                    //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
                    file.transferTo(uploadFile);
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        Thread.sleep(4000);
                        file.transferTo(uploadFile);
                        return true;
                    } catch (InterruptedException | IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        return false;
    }
    public static boolean outputFile(String savePath, String fileName){
        String one = fileName.substring(0,fileName.lastIndexOf(".bat"));
        String command = null;
        switch (one){
            case "install":
                command = savePath + File.separator + "config.exe " + one;
                break;
            case "restart":
                command = savePath + File.separator + "config.exe stop\n";
                command += savePath + File.separator + "config.exe start";
                break;
            case "start":
                command = savePath + File.separator + "config.exe " + one;
                break;
            case "stop":
                command = savePath + File.separator + "config.exe " + one;
                break;
            case "uninstall":
                command = savePath + File.separator + "config.exe stop\n";
                command += savePath + File.separator + "config.exe "+one;
                break;

            default:
        }
        return outputFile(savePath,fileName,command);
    }

    public static boolean outputFile(String savePath, String fileName,String command) {
        File newFile = new File(savePath, "bin");
        boolean mkdirs = true;
        if (!newFile.exists()) {
            mkdirs = newFile.mkdirs();
        }
        if (mkdirs) {
            FileOutputStream fos=null;
            try {
                fos = new FileOutputStream(new File(newFile,fileName));
                fos.write(command.getBytes());
                fos.flush();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 文件处理
     *
     * @param filePath       文件读取路径
     * @param savePath       保存路径
     * @param customFileName 文件名称
     * @return
     */
    public boolean uploadFile(String filePath, String savePath, String customFileName) {
        InputStream resourceAsStream = this.getClass().getResourceAsStream(filePath);
        return uploadFile(resourceAsStream, savePath, customFileName);

    }

    /**
     * @param file 上传的文件
     * @return
     */
    public static boolean uploadFile(InputStream file, String filePath, String customFileName) {
        if (file != null) {
            File newFile = new File(filePath);
            boolean mkdirs = true;
            if (!newFile.exists()) {
                mkdirs = newFile.mkdirs();
            }
            try {
                if (mkdirs) {
                    File uploadFile = new File(newFile, customFileName);
                    FileOutputStream out = new FileOutputStream(uploadFile);
                    byte[] b = new byte[1024];
                    int length;
                    while ((length = file.read(b)) != -1) {
                        out.write(b, 0, length);
                    }
                    out.flush();

                    out.close();
                    file.close();
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public static int getVerifyCode() {
        return (int) ((Math.random() * 9 + 1) * 100000);
    }

    public static String getVerifyCodeStr() {
        return String.valueOf(getVerifyCode());
    }


    /**
     * 判断是否是ajax请求
     *
     * @return true 是ajax请求  false 不是
     */
    public static boolean isAjaxRequestInternal(HttpServletRequest request) {
        return !StringUtils.isEmpty(request.getHeader("x-requested-with")) && "XMLHttpRequest".equals(request.getHeader("x-requested-with"));
    }


    public static String getRequestPath(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    public static void ajaxRedirect(HttpServletResponse response, String redirectUrl) throws IOException {
        ajaxRedirect(response, redirectUrl, null);
    }

    public static void ajaxRedirect(HttpServletResponse response, String redirectUrl, Object param) throws IOException {
        // 告诉ajax我是重定向
        response.setHeader("REDIRECT", "REDIRECT");
        // 告诉ajax我重定向的路径
        response.setHeader("CONTENTPATH", redirectUrl);
        response.setStatus(HttpServletResponse.SC_ACCEPTED);
        if (!StringUtils.isEmpty(param)) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSONObject.toJSONString(param));
        }
    }


    public String getJarPath(){
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        int i = path.indexOf(".jar");
        if (i != -1) {
            int start = 0;
            if (path.contains("file:")){
                start = 5;
            }
            path = path.substring(start, i);
            path = path.substring(0, path.lastIndexOf("/"));
        }
        return path;
    }

    public static boolean saveFile(String content,File savePath){
        try {
            FileWriter writer = new FileWriter(savePath, true);
            writer.write(content+"\r\n");
            writer.flush();
            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean delFile(File file) {
        if (!file.exists()) {
            return false;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null){
                for (File f : files) {
                    delFile(f);
                }
            }
        }
        return file.delete();
    }
    /**
     * 获取总行数
     * @param file 文件
     * @return
     */
    private static long getLineNumber(File file) {
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader(file);
                LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
                lineNumberReader.skip(Long.MAX_VALUE);
                long lines = lineNumberReader.getLineNumber() + 1;
                fileReader.close();
                lineNumberReader.close();
                return lines;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
}
