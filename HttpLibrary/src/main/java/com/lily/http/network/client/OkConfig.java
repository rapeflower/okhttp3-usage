package com.lily.http.network.client;

/***********
 * @Author rape flower
 * @Date 2017-12-22 23:35
 * @Describe 配置文件
 */
public class OkConfig {

    private static boolean isDebug = false;

    private OkConfig() {

    }

    public static boolean isDebug() {
        return OkConfig.isDebug;
    }

    public static void setDebug(boolean debug) {
        OkConfig.isDebug = debug;
    }
}
