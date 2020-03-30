package com.oruit.share.util;

public class CurrentThreadUtil {
	
    private static ThreadLocal<CurrentThreadValue> threadLocal = new ThreadLocal<CurrentThreadValue>();
    
    public static CurrentThreadValue getThreadValue() {
        return (CurrentThreadValue) threadLocal.get();
    }
    public static void setThreadValue(CurrentThreadValue threadValue) {
        threadLocal.set(threadValue);
    }
    public static void clearLocal() {
        threadLocal.remove();
    }
}
