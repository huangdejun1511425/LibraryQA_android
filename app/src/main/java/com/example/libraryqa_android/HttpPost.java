package com.example.libraryqa_android;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpPost {
    String gAsw;

    public void hPost(String ask, final Info info) {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        String requestBody = "{\"question\": \" " + ask + "\"}";
        System.out.println("33333" + ask);
        Request request = new Request.Builder()
                .url("http://39.108.80.74:8888?target=all")
                .post(RequestBody.create(mediaType, requestBody))
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                Info result = gson.fromJson(response.body().string(), Info.class);
                gAsw = result.graph_answer;
                System.out.println("123456789" + result.getSearch_answer().get(0).getQuestion());
                System.out.println("123456789" + result.getSearch_answer().get(1).getAnswer());
                System.out.println("123456789" + result.getSearch_answer().get(2).getQuestion());
                info.setGraph_answer(gAsw);
                info.setSearch_answer(result.getSearch_answer());
            }
        });
    }
}
