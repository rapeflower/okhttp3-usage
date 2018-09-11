package com.lily.http.network.listener;

/***********
 * @Author rape flower
 * @Date 2017-03-15 15:34
 * @Describe
 */
public class DisposeDataHandle {

    public DisposeDataListener listener = null;
    public Class<?> cls = null;
    public String source = null;

    public DisposeDataHandle(DisposeDataListener listener) {
        this.listener = listener;
    }

    public DisposeDataHandle(DisposeDataListener listener, Class<?> clazz) {
        this.listener = listener;
        this.cls = clazz;
    }

    public DisposeDataHandle(DisposeDataListener listener, String source) {
        this.listener = listener;
        this.source = source;
    }
}
