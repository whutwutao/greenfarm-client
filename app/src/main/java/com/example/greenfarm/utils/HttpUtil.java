package com.example.greenfarm.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class HttpUtil {

    public static String networkProtocol = "http://";

    public static String serverIP = "192.168.43.192";

    public static String serverPort = ":8081";

    public static String getUrl(String action) {
        return networkProtocol + serverIP + serverPort + action;
    }

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public static void sendOkHttpRequest(String url, okhttp3.Callback callback) throws IOException {
        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(callback);
    }

    public static void postWithOkHttp(String url, String json, okhttp3.Callback callback) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
