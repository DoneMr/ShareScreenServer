package com.done.sharescreenserver.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.done.sharescreenserver.MyApplication;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
 * Created by Done on 2017/11/29.
 *
 * @author by Done
 */

public class PhoneUtils {

    /**
     * all application use system service in this class static field
     */
    public static ConnectivityManager connectivityManager;
    public static WifiManager wifiManager;
    public static ActivityManager activityManager;
    public static TelephonyManager telephonyManager;
    public static AudioManager audioManager;

    /**
     * The method must invoke on application onCreate {@link MyApplication#onCreate()}
     *
     * @param context
     */
    public synchronized static void init(Context context) {
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    /**
     * The method set volume of type{@link AudioManager#STREAM_ALARM}
     *
     * @param type    volume type
     * @param isRaise if true volume is raise else low
     */
    public synchronized static boolean setVolume(int type, boolean isRaise) {
        try {
            if (isRaise) {
                audioManager.setStreamVolume(type,
                        audioManager.getStreamVolume(type) + 1,
                        AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_PLAY_SOUND);
            } else {
                audioManager.setStreamVolume(type,
                        (audioManager.getStreamVolume(type) - 1) < 0 ? 0 : (audioManager.getStreamVolume(type) - 1),
                        AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_PLAY_SOUND);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * The method set volume of type{@link AudioManager#STREAM_ALARM}
     *
     * @param type  volume type
     * @param isMax 0 volume is min 1 volume is max
     */
    public synchronized static boolean setVolume(int type, int isMax) {
        try {
            if (isMax > 0) {
                audioManager.setStreamVolume(type, audioManager.getStreamMaxVolume(type), AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_PLAY_SOUND);
            } else {
                audioManager.setStreamVolume(type, 0, AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_PLAY_SOUND);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 软关机，此方法需要具备系统权限
     *
     * @param context
     */
    public synchronized static void shutdown(Context context) {
        Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
        intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 设置wifi状态
     */
    public static void setWifiStatus(boolean enabled) {
        wifiManager.setWifiEnabled(enabled);
    }

    /**
     * 获取wifi状态
     */
    public static boolean getWifiStatus() {
        if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED
                || wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 通过反射实现开启或关闭移动数据 5.1以下
     *
     * @param enabled
     */
    public static void setMobileDataStatus(boolean enabled) {
        try {
            Class<?> conMgrClass = Class.forName(connectivityManager
                    .getClass().getName());
            // 得到ConnectivityManager类的成员变量mService（ConnectivityService类型）
            Field iConMgrField = conMgrClass.getDeclaredField("mService");
            iConMgrField.setAccessible(true);
            // mService成员初始化
            Object iConMgr = iConMgrField.get(connectivityManager);
            // 得到mService对应的Class对象
            Class<?> iConMgrClass = Class.forName(iConMgr.getClass().getName());
            /*
             * 得到mService的setMobileDataEnabled(该方法在android源码的ConnectivityService类中实现
			 * )， 该方法的参数为布尔型，所以第二个参数为Boolean.TYPE
			 */
            Method setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod(
                    "setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);
            /*
             * 调用ConnectivityManager的setMobileDataEnabled方法（方法是隐藏的），
			 * 实际上该方法的实现是在ConnectivityService(系统服务实现类)中的
			 */
            setMobileDataEnabledMethod.invoke(iConMgr, enabled);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
    }

    /**
     * 通过反射实现开启或关闭移动数据 5.1以上
     */
    public static void setMobileDataStatus5(boolean enabled) {
        try {
            Method setDataEnabled = telephonyManager.getClass().getDeclaredMethod("setDataEnabled", boolean.class);
            if (null != setDataEnabled) {
                setDataEnabled.invoke(telephonyManager, enabled);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取移动数据开关状态 5.1以上
     */
    public static boolean getMobileDataStatus5() {
        try {
            Method getDataEnabled = telephonyManager.getClass().getDeclaredMethod("getDataEnabled");
            if (null != getDataEnabled) {
                return (Boolean) getDataEnabled.invoke(telephonyManager);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 关闭热点
     */
    public static synchronized void closeWifiAp() {
        try {
            Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
            method.setAccessible(true);
            WifiConfiguration config = (WifiConfiguration) method.invoke(wifiManager);
            Method method2 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method2.invoke(wifiManager, config, false);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建热点
     *
     * @param mSSID    热点名称
     * @param mPasswd  热点密码
     * @param isDebug  是否打开wifi调试
     * @param isEnable 是否打开wifi热点
     */
    public static synchronized boolean enalbeWifiAp(String mSSID, String mPasswd, boolean isDebug, boolean isEnable) {
        Method method1 = null;
        try {
            method1 = wifiManager.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, boolean.class);
            WifiConfiguration netConfig = new WifiConfiguration();
            netConfig.SSID = mSSID;
            netConfig.preSharedKey = mPasswd;
            netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            if (isEnable) {
                return (boolean) method1.invoke(wifiManager, netConfig, true);
            } else {
                return (boolean) method1.invoke(wifiManager, netConfig, false);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 开关wifi调试
     *
     * @param flag true为开，默认端口为5555；false为关闭
     * @return 如果执行失败则返回false，成功返回true
     */
    private static boolean setWifiDebug(boolean flag) {
        int port = -1;
        if (flag) {
            port = 5555;
        }
        try {
            // 获取输出流
            OutputStream outputStream = Runtime.getRuntime().exec("su")
                    .getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(
                    outputStream);
            // 将命令写入
            dataOutputStream.writeBytes("stop adbd");
            // 提交命令
            dataOutputStream.flush();
            // 关闭流操作
            dataOutputStream.close();
            outputStream.close();

            // 获取输出流
            OutputStream outputStream2 = Runtime.getRuntime().exec("su")
                    .getOutputStream();
            DataOutputStream dataOutputStream2 = new DataOutputStream(
                    outputStream2);
            dataOutputStream2
                    .writeBytes("setprop service.adb.tcp.port " + port);
            dataOutputStream2.flush();
            // 关闭流操作
            dataOutputStream2.close();
            outputStream2.close();

            // 获取输出流
            OutputStream outputStream3 = Runtime.getRuntime().exec("su")
                    .getOutputStream();
            DataOutputStream dataOutputStream3 = new DataOutputStream(
                    outputStream3);
            dataOutputStream3.writeBytes("start adbd");
            dataOutputStream3.flush();
            // 关闭流操作
            dataOutputStream3.close();
            outputStream3.close();
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }
}
