package com.ftp.deploy.utils.socket;

import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import javax.websocket.Session;
import java.io.IOException;

/**
 * @author LiuJiaPeng
 * @version 1.0
 * @date 2019-04-12 10:04
 */
@Slf4j
public class SocketUtil {
    /**
     * 发送消息给个人
     * @param message 消息
     * @return 成功 true
     */
    public static boolean sendMessage(String message){
       return sendMessage("admin",message);
    }

    /**
     * 发送消息给个人
     * @param to  key
     * @param message 消息
     * @return 成功 true
     */
    public static boolean sendMessage(@NotNull String to, String message) {
        try {
            Session session = RecordSingleCase.getInstance().get(to);
            if (session!=null && session.isOpen()){
                session.getBasicRemote().sendText(message);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 发送消息给全部人员
     * @param message 消息
     */
    public static void sendMessageAll(String message){
        for (Session session : RecordSingleCase.getInstance().values()) {
            session.getAsyncRemote().sendText(message);
        }
    }


}
