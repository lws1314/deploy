package com.ftp.deploy.utils;

import com.ftp.deploy.utils.socket.SocketUtil;

import java.io.*;

/**
 * 从文件中读取执行日志
 *
 * @author ZERO
 */
public class ExecuteLog extends Thread {
    private File file;
    public ExecuteLog(File file) {
        this.file = file;
    }
    @Override
    public void run() {
        try {
            //获取文件大小
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            long skipBytes = raf.length();
            raf.close();
            raf = null;
            //使用行读取
            LineNumberReader reader = new LineNumberReader(new FileReader(file));
            //跳过一定内容
            reader.skip(skipBytes);
            String line;
            while (true) {
                try {
                    if ((line = reader.readLine()) != null) {
                        boolean bool = SocketUtil.sendMessage("<span style='color:black'>" + line + "</span>");
                        if (!bool) {
                            reader.close();
                            return;
                        }
                        continue;
                    }else{
                        if (SocketUtil.stop){
                            SocketUtil.stop = false;
                            reader.close();
                            return;
                        }
                    }
                    Thread.sleep(1000L);
                } catch (IOException | InterruptedException e) {
                    // 文件被清空的时候FileInputStream会被close
                    reader.close();
                    reader = null;
                    SocketUtil.stop = false;
                    return;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            SocketUtil.stop = false;
        }
    }
}
