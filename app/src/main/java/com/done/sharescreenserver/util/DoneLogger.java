package com.done.sharescreenserver.util;

import android.util.Log;

/**
 * 　　　　　　　　┏┓　　　┏┓+ +
 * 　　　　　　　┏┛┻━━━┛┻┓ + +
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃ ++ + + +
 * 　　　　　　 ████━████ ┃+
 * 　　　　　　　┃　　　　　　　┃ +
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　　┃ + +
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃ + + + +
 * 　　　　　　　　　┃　　　┃　　　　Code is far away from bug with the animal protecting
 * 　　　　　　　　　┃　　　┃ + 　　　　神兽保佑,代码无bug
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　+
 * 　　　　　　　　　┃　 　　┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　　　┣┓
 * 　　　　　　　　　┃ 　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛+ + + +
 * Created by Done on 2017/12/5.
 *
 * @author by Done
 */

public class DoneLogger {

    public static boolean DEBUG = true;

    public static void v(String tag, String message) {
        if (DEBUG) {
            Log.v(tag, processMessage(message));
        }
    }

    public static void d(String tag, String message) {
        if (DEBUG) {
            Log.d(tag, processMessage(message));
        }
    }

    public static void i(String tag, String message) {
        if (DEBUG) {
            Log.i(tag, processMessage(message));
        }
    }

    public static void w(String tag, String message) {
        if (DEBUG) {
            Log.w(tag, processMessage(message));
        }
    }

    public static void e(String tag, String message) {
        if (DEBUG) {
            Log.e(tag, processMessage(message));
        }
    }

    private static String processMessage(String message) {
        return "[ThreadName:" + Thread.currentThread().getName() +
                ",ThreadId:" + Thread.currentThread().getId() + "]☞" +
                message;
    }
}
