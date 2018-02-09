package com.done.sharescreenserver.Constant;

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
 * Created by Done on 2017/12/8.
 *
 * @author by Done
 */

public class Constants {

    public enum RESPONSE_STATUS {

        /**
         * response ok take effective data to client
         */
        RESPONSE_OK(200, "OK"),

        /**
         * response 400 to client, server can not understand the intent for request please read RTDP protocol
         */
        RESPONSE_BAD_REQUEST(400, "Bad Request"),

        /**
         * response 404 to client, server not support the method, please read RTDP protocol
         */
        RESPONSE_NOT_FOUND(404, "Not Found The Method"),

        /**
         * response 405 to client, client has no permission to use the method
         */
        RESPONSE_METHOD_NOT_ALLOWED(405, "Method Not Allowed");

        int status;
        String message;

        RESPONSE_STATUS(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }

    /**
     * The request content key words
     */
    public static final String SETUP_REQUEST_KEY_WORD = "client-port=";
    public static final String PLAY_REQUEST_KEY_WORD = "play phone window";
    public static final String TEARDOWN_REQUEST_KEY_WORD = "shutdown";
    public static final String HEART_REQUEST_KEY_WORD = "heart";

    public static final String SPACE = " ";
    public static final String CRLF = "\r\n";
    public static final int COUNT_REQUEST_PART = 4;

    public static final String RTDP_METHOD_SETUP = "SETUP";
    public static final String RTDP_METHOD_PLAY = "PLAY";
    public static final String RTDP_METHOD_TEARDOWN = "TEARDOWN";
    public static final String RTDP_METHOD_HEART = "HEART";
    public static final String RTDP_METHOD_HOME = "HOME";
    public static final String RTDP_METHOD_BACK = "BACK";
    public static final String RTDP_METHOD_MENU = "MENU";
    public static final String RTDP_METHOD_VOLUME = "VOLUME";
    public static final String RTDP_METHOD_CLICK = "CLICK";
    public static final String RTDP_METHOD_TOUCH = "TOUCH";

    public static final String RTDP_REQUEST_CONTENT_SPLIT = ";";
    public static final String RTDP_REQUEST_CONTENT_VOLUME_SET = "set=";
    public static final String RTDP_REQUEST_CONTENT_VOLUME_TYPE = "type=";
    public static final String RTDP_REQUEST_CONTENT_VOLUME_TYPE_CALL = "call";
    public static final String RTDP_REQUEST_CONTENT_VOLUME_TYPE_SYSTEM = "system";
    public static final String RTDP_REQUEST_CONTENT_VOLUME_TYPE_RING = "ring";
    public static final String RTDP_REQUEST_CONTENT_VOLUME_TYPE_MUSIC = "music";
    public static final String RTDP_REQUEST_CONTENT_VOLUME_TYPE_ALARM = "alarm";
    public static final String RTDP_REQUEST_CONTENT_VOLUME_TYPE_NOTIFICATION = "notification";
    public static final String RTDP_REQUEST_CONTENT_VOLUME_SET_UP = "up";
    public static final String RTDP_REQUEST_CONTENT_VOLUME_SET_LOW = "low";
    public static final String RTDP_REQUEST_CONTENT_VOLUME_SET_MAX = "max";
    public static final String RTDP_REQUEST_CONTENT_VOLUME_SET_MIN = "min";


    public static final String WIFI_NAME = "Phone Live";
    public static final String WIFI_PASSWORD = "11111111";
    public static final String SERVICE_NOTIFICATION = "Screen Service is running";

    public static final int SERVICE_NOTIFICATION_ID = 1;

    public static final int VIDEO_SERVER_PORT = 8198;
    public static final int CONTROL_SERVER_PORT = 8199;
    public static int VIDEO_CLIENT_PORT = 8085;

    /**
     * Broadcast types and action
     */
    public static final int BROADCAST_TYPE_PLAY = 1;
    public static final int BROADCAST_TYPE_TEARDOWN = 2;
    public static final int BROADCAST_TYPE_ERROR = 3;

    public static final String BROADCAST_ACTION_PLAY = "com.done.live.PLAY";
    public static final String BROADCAST_ACTION_TEARDOWN = "com.done.live.TEARDOWN";
    public static final String BROADCAST_ACTION_ERROR = "com.done.live.ERROR";

    public static final String BROADCAST_KEY_ADDRESS = "ADDRESS";

    /**
     * mock key event
     */
    public static final String ADB_HOME = "input keyevent 3";
    public static final String ADB_BACK = "input keyevent 4";
    public static final String ADB_MENU = "input keyevent 1";
    public static final String ADB_CLICK = "tap ";
    public static final String ADB_TOUCH = "swipe ";
}
