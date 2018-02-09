package com.done.sharescreenserver.util;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

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
 * Created by Done on 2017/11/20.
 *
 * @author by Done
 */

public class ToastUtils {

    private volatile static ToastUtils instance;

    private static Handler mainHandler;

    private static Toast toast;

    public static ToastUtils getInstance() {
        if (null == instance) {
            synchronized (ToastUtils.class) {
                if (null == instance) {
                    instance = new ToastUtils();
                }
            }
        }
        return instance;
    }

    private ToastUtils() {
        mainHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * showToast
     *
     * @param message  show message what you want
     * @param context  context
     * @param isCenter if true is show in center
     */
    public void showToast(String message, Context context, boolean isCenter) {
        mainHandler.post(() -> showToastShort(message, context, isCenter));
    }

    private void showToastShort(String message, Context context, boolean isCenter) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        if (isCenter) {
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        toast.show();
    }

}
