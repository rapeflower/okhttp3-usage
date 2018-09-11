package com.lily.http.network.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.lily.http.network.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import ikidou.reflect.TypeBuilder;

/***********
 * @Author rape flower
 * @Date 2017-11-30 11:33
 * @Describe JSON数据帮助类
 */
public class JsonHelper {

    private static final String TAG = JsonHelper.class.getSimpleName();
    private static final String KEY_RESULT = "result";
    private static final String KEY_DATA = "data";
    private static final String RESPONSE_RESULT_CODE = "resultCode";
    private static final String RESPONSE_MESSAGE = "message";
    private static final String RESPONSE_RESULT = KEY_RESULT;

    /**
     * 获取JSONObject
     *
     * @param jsonStr json数据字符串
     * @return
     */
    public static JSONObject getJSONObject(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 对象转JSON
     *
     * @param object
     * @return
     */
    public static String toJSON(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    /**
     * JSON转对象
     *
     * @param json json数据字符串
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T fromJSONObject(String json, Class<T> cls) {
        Gson gson = new Gson();
        Type type = TypeBuilder.newInstance(cls).build();
        return gson.fromJson(json, type);
    }

    /**
     * JSON转对象集合
     *
     * @param json json数据字符串
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> fromJSONArray(String json, Class<T> cls) {
        Gson gson = new Gson();
        Type type = TypeBuilder.newInstance(List.class)
                .addTypeParam(cls)
                .build();
        return gson.fromJson(json, type);
    }

    /**
     * 解析JSON
     *
     * @param jsonStr
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> Response parseJSON(String jsonStr, Class<T> cls) {
        JSONObject jsonObject = getJSONObject(jsonStr);
        if (jsonObject == null) {
            return null;
        }

        Response response = new Response();
        try {
            if (jsonObject.has(RESPONSE_RESULT_CODE)) {
                Object code = jsonObject.get(RESPONSE_RESULT_CODE);
                if (code instanceof Integer) {
                    response.resultCode = (int) code;
                } else if (code instanceof String) {
                    response.resultCode = Integer.parseInt((String) code);
                }
            }
            if (jsonObject.has(RESPONSE_MESSAGE)) {
                response.message = jsonObject.getString(RESPONSE_MESSAGE);
            }
            if (jsonObject.has(RESPONSE_RESULT)) {
                Object value = jsonObject.get(RESPONSE_RESULT);
                if (cls == String.class) {
                    if (value instanceof String) {
                        response.data = value;
                    }
                } else {
                    if (value instanceof JSONObject) {
                        JSONObject result = (JSONObject) value;
                        response.data = fromJSONObject(result.toString(), cls);
                    } else if (value instanceof JSONArray) {
                        JSONArray result = (JSONArray) value;
                        response.data = fromJSONArray(result.toString(), cls);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

}
