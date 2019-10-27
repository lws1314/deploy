package com.ftp.deploy.utils.socket;

import javax.websocket.Session;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ZERO
 */
public class RecordSingleCase implements Serializable {
    private RecordSingleCase() {}
    private static Map<String, Session> INSTANCE = new ConcurrentHashMap<String, Session>();
    public synchronized static Map<String, Session> getInstance() {
        return INSTANCE;
    }
}

