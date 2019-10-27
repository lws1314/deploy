package com.ftp.deploy.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.ftp.deploy.utils.socket.SocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 打印 cmd 控制台输出信息
 *
 * @author ZERO
 */
@Slf4j
public class StreamPrint extends Thread {
    private InputStream inputStream;

    public StreamPrint(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        if (inputStream != null) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
                String str = null;
                try {
                    while ((str = br.readLine()) != null) {
                        if ("".equals(str)) continue;
                        log.info(str);
                        SocketUtil.sendMessage(str);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
