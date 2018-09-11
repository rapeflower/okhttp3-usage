package com.lily.http.network.exception;

/***********
 * @Author rape flower
 * @Date 2017-03-15 14:40
 * @Describe 自定义的异常类
 */
public class OkHttpException extends Exception{
    private static final long serialVersionUID = 1L;

    //服务器返回的结果码
    private int eCode;
    //服务器返回的错误消息
    private Object eMsg;

    public OkHttpException(int eCode, Object eMsg) {
        this.eCode = eCode;
        this.eMsg = eMsg;
    }

    public int getECode() {
        return eCode;
    }

    public Object getEMsg() {
        return eMsg;
    }
}
