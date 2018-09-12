package com.lily.http.network.listener;

/***********
 * @Author rape flower
 * @Date 2017-03-15 15:34
 * @Describe
 */
public class DataProcessor {

    public ProcessorListener listener = null;
    public Class<?> cls = null;
    public String source = null;

    public DataProcessor(ProcessorListener listener) {
        this.listener = listener;
    }

    public DataProcessor(ProcessorListener listener, Class<?> clazz) {
        this.listener = listener;
        this.cls = clazz;
    }

    public DataProcessor(ProcessorListener listener, String source) {
        this.listener = listener;
        this.source = source;
    }
}
