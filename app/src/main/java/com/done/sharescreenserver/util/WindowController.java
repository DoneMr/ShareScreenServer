package com.done.sharescreenserver.util;

import android.hardware.input.InputManager;

import java.io.File;
import java.lang.reflect.Method;

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
 *
 * @author Done
 * @date 2018/1/3
 */

public class WindowController {

    private static final String TAG = "WindowController";

    private static final String DEV = "/dev/input/event3";

    private volatile static WindowController instance;

    private InputManager im;
    private Method injectInputEventMethod;
    private long downTime;

    public static WindowController getInstance() {
        if (instance == null) {
            synchronized (WindowController.class) {
                if (instance == null) {
                    instance = new WindowController();
                }
            }
        }
        return instance;
    }

    private WindowController() {
       /* Check access permission */
        try {
            File device = new File(DEV);
                /* Missing read/write permission, trying to chmod the file */
            Process su;
            su = Runtime.getRuntime().exec("/system/xbin/su");
            String cmd = "chmod 777 " + DEV + "\n"
                    + "exit\n";
            su.getOutputStream().write(cmd.getBytes());
            if ((su.waitFor() != 0) || !device.canRead()
                    || !device.canWrite()) {
                DoneLogger.e(TAG, "native open device error!");
            } else {
                DoneLogger.d(TAG, "change permission success!");
            }
        } catch (Exception e) {
            DoneLogger.e(TAG, "native open device cause exception: " + e.getLocalizedMessage());
        }
    }

    public void test() {
        DoneLogger.d(TAG, touch(0, 0, 100, 0));
        DoneLogger.d(TAG, touch(2, 0, 100, 40));
        DoneLogger.d(TAG, touch(2, 0, 100, 80));
        DoneLogger.d(TAG, touch(2, 0, 100, 160));
        DoneLogger.d(TAG, touch(2, 0, 100, 320));
        DoneLogger.d(TAG, touch(1, 0, 0, 0));
    }


    static {
        System.loadLibrary("native-lib");
    }

    public native String touch(int type, int fingerIndex, int x, int y);

    public native void closeTouch();
}
