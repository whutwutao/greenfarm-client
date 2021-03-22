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

    public static final String serverIP = "http://192.168.1.102";

    public static final String serverPort = ":8081";

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public static void sendOkHttpRequest(String url, okhttp3.Callback callback) {
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
