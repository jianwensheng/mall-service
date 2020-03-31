package com.oruit.util.thread;

public class HttpThreadLocal {

    private static ThreadLocal<HttpThreadValue> threadLocal = new ThreadLocal<HttpThreadValue>();

    public static HttpThreadValue getThreadValue() {
        return (HttpThreadValue) threadLocal.get();
    }

    public static void setThreadValue(HttpThreadValue threadValue) {
        threadLocal.set(threadValue);
    }

    public static void clearLocal() {
        threadLocal.remove();
    }
}
