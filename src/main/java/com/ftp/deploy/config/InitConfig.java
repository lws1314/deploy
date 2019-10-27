package com.ftp.deploy.config;

import java.io.*;

import com.ftp.deploy.utils.CommonUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;

/**
 * 初始化配置
 *
 * @author ZERO
 */
@Component
public class InitConfig implements ApplicationRunner {

    private static final String MARK = "file:";

    @Override
    public void run(ApplicationArguments args) {
        createPidFile();
        System.out.println("program Startup Completed");
    }

    /**
     * 获取当前程序pid
     */
    private void createPidFile() {
        // get name representing the running Java virtual machine.
        String name = ManagementFactory.getRuntimeMXBean().getName();
        // get pid
        String pid = name.split("@")[0];
        try {
            CommonUtils commonUtils = new CommonUtils();
            OutputStream os = new FileOutputStream(new File(commonUtils.getJarPath(), "pid.txt"));
            os.write(pid.getBytes());
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
