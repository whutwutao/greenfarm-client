package com.example.greenfarm.utils;

public class WebSocketUtil {
    public static String netWorkProtocol = "ws://";

    public static String getWebSocketUrl(String paramName, String paramValue) {
        return netWorkProtocol + HttpUtil.serverIP + HttpUtil.serverPort + "/test" + "?" + paramName + "=" + paramValue;
    }
    
}

