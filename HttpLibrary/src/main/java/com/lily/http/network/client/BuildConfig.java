package com.lily.http.network.client;

/***********
 * @Author rape flower
 * @Date 2017-12-22 23:35
 * @Describe 配置文件
 */
public class BuildConfig {

    public static final String APPLICATION_ID = "com.lily.http";
    public static final String BUILD_TYPE = "release";
    public static final String FLAVOR = "";
    public static final int VERSION_CODE = 1;
    public static final String VERSION_NAME = "1.0";
    private static boolean isDebug = false;

    private BuildConfig() {

    }

    public static boolean isDebug() {
        return BuildConfig.isDebug;
    }

    public static void setDebug(boolean debug) {
        BuildConfig.isDebug = debug;
    }
}
