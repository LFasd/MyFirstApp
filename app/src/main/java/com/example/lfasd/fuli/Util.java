package com.example.lfasd.fuli;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by LFasd on 2017/6/3.
 */

public class Util {

    /**
     * 使用OkHttp进行网络请求
     *
     * @param url
     * @param callback
     */
    public static void requestURL(String url, Callback callback) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 解析返回的JSON
     *
     * @param response
     * @return
     */
    public static Return resolveJSON(Response response) {
        //解析数据
        Gson gson = new Gson();
        Return mReturn = null;

        try {
            String responseData = response.body().string();
            Log.d("test", responseData);

            mReturn = gson.fromJson(responseData, Return.class);

            gson.fromJson(responseData, new TypeToken<Return>() {}.getType());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return mReturn;
    }
}
