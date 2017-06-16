package com.example.lfasd.fuli;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Type;

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
     * 根据传进来的类分析Response中的Json数据并返回结果
     *
     * @param response
     * @param tClass
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T resolveJSON(Response response, Class<T> tClass) throws IOException {
        T result = null;

        String responseData = response.body().string();

        try {
            if (isJSON(responseData)) {
                Gson gson = new Gson();
                result = gson.fromJson(responseData, tClass);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 根据类型分析Response中的Json数据并返回结果
     *
     * @param response
     * @param type
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T resolveJSON(Response response, Type type) throws IOException {
        T result = null;

        String responseData = response.body().string();

        try {
            if (isJSON(responseData)) {
                Gson gson = new Gson();
                result = gson.fromJson(responseData, type);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 判断字符串是否是真正的Json数据
     *
     * @param json
     * @return
     * @throws JSONException
     */
    public static boolean isJSON(String json) throws JSONException {
        if (json.length() == 0) {
            return false;
        }

        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(json);
        if (!jsonElement.isJsonObject()) {
            throw new RuntimeException("The Response is not JSON");
        }

        return true;
    }
}
