package com.lily.http.network.request;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/***********
 * @Author rape flower
 * @Date 2017-03-14 15:09
 * @Describe 请求参数
 */
public class RequestParams {

    public ConcurrentHashMap<String, String> urlParams = new ConcurrentHashMap<String, String>();
    public ConcurrentHashMap<String, Object> fileParams = new ConcurrentHashMap<String, Object>();
    public Object jsonParam = null;
    private boolean isJsonParam = false;//表示当前传入请求参数：false: 不是Json格式，true: 是Json格式

    /**
     * Constructs a new empty instance
     */
    public RequestParams() {
        this((Map<String, String>) null);
    }

    /**
     * Constructs a new RequestParams instance containing the key/value
     * string params from the specified map.
     *
     * @param source the source key/value string map to add.
     */
    public RequestParams(Map<String, String> source) {
        if (source != null) {
            for (Map.Entry<String, String> entry : source.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Constructs a new RequestParams instance and populate it with a single
     * initial key/value string param.
     *
     * @param key
     * @param value
     */
    public RequestParams(final String key, final String value) {
        this(new HashMap<String, String>() {
            {
                put(key, value);
            }
        });
    }

    /**
     * Adds key/value string pair to the request.
     *
     * @param key  the key name for the new param.
     * @param value  the value string for new param.
     */
    public void put(String key, String value) {
        if (key != null && value != null) {
            urlParams.put(key, value);
        }
    }

    /**
     * Adds key/value string with Object pair to the request.
     *
     * @param key  the key name for the new param.
     * @param object  the value object for new param.
     * @throws FileNotFoundException when the file not find throw a exception.
     */
    public void put(String key, Object object) throws FileNotFoundException{
        if (key != null) {
            fileParams.put(key, object);
        }
    }

    /**
     * Adds object pair to the request.
     *
     * @param object
     */
    public void put(Object object) {
        this.jsonParam = object;
    }

    /**
     * Has RequestParams
     *
     * @return
     */
    public boolean hasParams() {
        if (urlParams.size() > 0 || fileParams.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * Get method
     *
     * @return
     */
    public boolean isJsonParam() {
        return isJsonParam;
    }

    /**
     * Set method
     *
     * @param isJsonParam
     */
    public void setIsJsonParam(boolean isJsonParam) {
        this.isJsonParam = isJsonParam;
    }
}