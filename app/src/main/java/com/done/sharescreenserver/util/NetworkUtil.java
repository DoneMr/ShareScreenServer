package com.done.sharescreenserver.util;


public class NetworkUtil {

    /**
     * 验证ip是否合法
     * @param text ip地址
     * @return 验证信息
     */
    public static boolean ipCheck(String text) {
        String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        return text.matches(regex);
    }
}
