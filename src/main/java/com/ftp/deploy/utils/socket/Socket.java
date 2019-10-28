package com.ftp.deploy.utils.socket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * username：用户登录id
 * @author ZERO
 */
@Slf4j
@Component
@ServerEndpoint("/webSocket/{username}")
public class Socket {

    /**
     * 连接建立成功调用的方法
     * @param username
     * @param session
     * @throws IOException
     */
    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) throws IOException {
        //获取创建监听实例 的对象
        RecordSingleCase.getInstance().put(username, session);
        //创建队列
        session.getAsyncRemote().sendText("与服务的连接已建立");
        log.info("{}建立连接",username);
    }

    /**
     * 关闭链接
     * @throws IOException
     */
    @OnClose
    public void onClose(@PathParam("username") String username,Session session) throws IOException {
        RecordSingleCase.getInstance().remove(username);
        SocketUtil.stop = true;
        log.info("{}关闭连接",username);
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message
     * @throws IOException
     */
    @OnMessage
    public void onMessage(@PathParam("username") String username,String message) throws IOException {
    }

    /**
     * 发生错误时调用
     * */
    @OnError
    public void onError(@PathParam("username") String username,Session session, Throwable error) {
        RecordSingleCase.getInstance().remove(username);
        SocketUtil.stop = true;
        log.error(username + "---->{'status':'2','content':'链接异常中断'}",error);
    }

}
