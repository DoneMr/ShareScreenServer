package com.done.sharescreenserver.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.done.sharescreenserver.Constant.Constants;
import com.done.sharescreenserver.R;
import com.done.sharescreenserver.service.model.RequestEntity;
import com.done.sharescreenserver.service.model.ResponseEntity;
import com.done.sharescreenserver.service.thread.ControlServer;
import com.done.sharescreenserver.service.thread.OnRequest;
import com.done.sharescreenserver.util.PhoneUtils;
import com.done.sharescreenserver.util.ShellUtil;

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

public class ScreenRecordService extends Service {

    private NotificationManager notificationManager;

    private Context mContext;

    private ControlServer controlServer;
    private RequesterImpl requester;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        controlServer = new ControlServer();
        requester = new RequesterImpl();
        controlServer.enableServer(requester);
        updateNotification(Constants.SERVICE_NOTIFICATION);
    }

    private void updateNotification(String content) {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        /**
         * 构建通知对象
         * 1、设置通知的标题
         * 2、设置通知的内容
         * 3、设置通知的图标
         * 4、设置点击通知后的操作
         */
        Notification.Builder builer = new Notification.Builder(this);
        builer.setContentTitle("Screen Record");
        builer.setContentText(content);
        builer.setSmallIcon(R.mipmap.ic_launcher);
        builer.setOngoing(true);
        Notification notification = builer.build();
        startForeground(Constants.SERVICE_NOTIFICATION_ID, notification);
    }

    private void cancelNotification() {
        notificationManager.cancel(Constants.SERVICE_NOTIFICATION_ID);
    }

    private class RequesterImpl implements OnRequest {

        @Override
        public void onHOME(RequestEntity src, String address) {
            handleKey(src, Constants.ADB_HOME);
        }

        @Override
        public void onBACK(RequestEntity src, String address) {
            handleKey(src, Constants.ADB_BACK);
        }


        @Override
        public void onMENU(RequestEntity src, String address) {
            handleKey(src, Constants.ADB_MENU);
        }

        @Override
        public void onVOLUME(RequestEntity src, String address) {
            handleVolume(src);
        }

        @Override
        public void onCLICK(RequestEntity src, String address) {
            handleClick(src);
        }

        @Override
        public void onTOUCH(RequestEntity src, String address) {
            handleTouch(src);
        }


        @Override
        public void onSETUP(RequestEntity src, String address) {
            handleSETUP(src);
        }

        @Override
        public void onPLAY(RequestEntity src, String address) {
            handlePLAY(src, address);
        }


        @Override
        public void onTEARDOWN(RequestEntity src, String address) {
            handleTEARDOWN(src, address);
        }

        @Override
        public void onError(int code, String address) {
            handleError(address);
        }

        @Override
        public void onHEART(RequestEntity src, String address) {
            handleHEART(src, address);
        }


        private void handleTouch(RequestEntity src) {
            ResponseEntity responseEntity;
            String responseContent;
            responseContent = Constants.RESPONSE_STATUS.RESPONSE_METHOD_NOT_ALLOWED.getMessage();
            responseEntity = processResponse(Constants.RESPONSE_STATUS.RESPONSE_METHOD_NOT_ALLOWED.getStatus(), src.cseq, responseContent);
            String clickCoordinate = "";
            if (ShellUtil.hasRootPermission()) {
                ShellUtil.execCommand(Constants.ADB_CLICK + clickCoordinate, true);
                responseContent = Constants.RESPONSE_STATUS.RESPONSE_OK.getMessage();
                responseEntity = processResponse(Constants.RESPONSE_STATUS.RESPONSE_OK.getStatus(), src.cseq, responseContent);
            }
            controlServer.responseClient(responseEntity.toString());
        }

        private void handleClick(RequestEntity src) {
            ResponseEntity responseEntity;
            String responseContent;
            responseContent = Constants.RESPONSE_STATUS.RESPONSE_METHOD_NOT_ALLOWED.getMessage();
            responseEntity = processResponse(Constants.RESPONSE_STATUS.RESPONSE_METHOD_NOT_ALLOWED.getStatus(), src.cseq, responseContent);
            String touchCoordinate = "";
            if (ShellUtil.hasRootPermission()) {
                ShellUtil.execCommand(Constants.ADB_CLICK + touchCoordinate, true);
                responseContent = Constants.RESPONSE_STATUS.RESPONSE_OK.getMessage();
                responseEntity = processResponse(Constants.RESPONSE_STATUS.RESPONSE_OK.getStatus(), src.cseq, responseContent);
            }
            controlServer.responseClient(responseEntity.toString());
        }

        private void handleVolume(RequestEntity src) {
            ResponseEntity responseEntity;
            String responseContent;
            responseContent = Constants.RESPONSE_STATUS.RESPONSE_METHOD_NOT_ALLOWED.getMessage();
            responseEntity = processResponse(Constants.RESPONSE_STATUS.RESPONSE_METHOD_NOT_ALLOWED.getStatus(), src.cseq, responseContent);
            String[] volumes = src.content.split(Constants.RTDP_REQUEST_CONTENT_SPLIT);
            String volumeType = "";
            String volumeValue = "";
            if (volumes.length == 2) {
                for (String volume : volumes) {
                    if (volume.startsWith(Constants.RTDP_REQUEST_CONTENT_VOLUME_TYPE)) {
                        volumeType = volume.substring(5);
                    }
                    if (volume.startsWith(Constants.RTDP_REQUEST_CONTENT_VOLUME_SET)) {
                        volumeValue = volume.substring(4);
                    }
                }
                if (!TextUtils.isEmpty(volumeType) && !TextUtils.isEmpty(volumeValue)) {
                    int type = -100;
                    boolean bRet = false;
                    switch (volumeType) {
                        case Constants.RTDP_REQUEST_CONTENT_VOLUME_TYPE_ALARM:
                            type = AudioManager.STREAM_ALARM;
                            break;
                        case Constants.RTDP_REQUEST_CONTENT_VOLUME_TYPE_CALL:
                            type = AudioManager.STREAM_VOICE_CALL;
                            break;
                        case Constants.RTDP_REQUEST_CONTENT_VOLUME_TYPE_MUSIC:
                            type = AudioManager.STREAM_MUSIC;
                            break;
                        case Constants.RTDP_REQUEST_CONTENT_VOLUME_TYPE_NOTIFICATION:
                            type = AudioManager.STREAM_NOTIFICATION;
                            break;
                        case Constants.RTDP_REQUEST_CONTENT_VOLUME_TYPE_RING:
                            type = AudioManager.STREAM_RING;
                            break;
                        case Constants.RTDP_REQUEST_CONTENT_VOLUME_TYPE_SYSTEM:
                            type = AudioManager.STREAM_SYSTEM;
                            break;
                        default:
                            break;
                    }

                    if (type != -100) {
                        switch (volumeValue) {
                            case Constants.RTDP_REQUEST_CONTENT_VOLUME_SET_LOW:
                                bRet = PhoneUtils.setVolume(type, false);
                                break;
                            case Constants.RTDP_REQUEST_CONTENT_VOLUME_SET_UP:
                                bRet = PhoneUtils.setVolume(type, true);
                                break;
                            case Constants.RTDP_REQUEST_CONTENT_VOLUME_SET_MAX:
                                bRet = PhoneUtils.setVolume(type, 1);
                                break;
                            case Constants.RTDP_REQUEST_CONTENT_VOLUME_SET_MIN:
                                bRet = PhoneUtils.setVolume(type, 0);
                                break;
                            default:
                                break;
                        }
                    }
                    if (bRet) {
                        responseContent = Constants.RESPONSE_STATUS.RESPONSE_OK.getMessage();
                        responseEntity = processResponse(Constants.RESPONSE_STATUS.RESPONSE_OK.getStatus(), src.cseq, responseContent);
                    }
                }
            }
            controlServer.responseClient(responseEntity.toString());
        }

        private void handleKey(RequestEntity src, String adbCommand) {
            ResponseEntity responseEntity;
            String responseContent;
            responseContent = Constants.RESPONSE_STATUS.RESPONSE_METHOD_NOT_ALLOWED.getMessage();
            responseEntity = processResponse(Constants.RESPONSE_STATUS.RESPONSE_METHOD_NOT_ALLOWED.getStatus(), src.cseq, responseContent);
            if (ShellUtil.hasRootPermission()) {
                ShellUtil.execCommand(adbCommand, true);
                responseContent = Constants.RESPONSE_STATUS.RESPONSE_OK.getMessage();
                responseEntity = processResponse(Constants.RESPONSE_STATUS.RESPONSE_OK.getStatus(), src.cseq, responseContent);
            }
            controlServer.responseClient(responseEntity.toString());
        }

        private void handleError(String address) {
            sendBroadcast(Constants.BROADCAST_TYPE_ERROR, address);
        }


        private void handleHEART(RequestEntity src, String address) {
            String content = src.content;
            ResponseEntity responseEntity = processResponse(Constants.RESPONSE_STATUS.RESPONSE_NOT_FOUND.getStatus(),
                    src.cseq,
                    Constants.RESPONSE_STATUS.RESPONSE_NOT_FOUND.getMessage());
            String responseContent = Constants.RESPONSE_STATUS.RESPONSE_NOT_FOUND.getMessage();
            if (Constants.HEART_REQUEST_KEY_WORD.equalsIgnoreCase(content)) {
                responseContent = Constants.RESPONSE_STATUS.RESPONSE_OK.getMessage();
                responseEntity = processResponse(Constants.RESPONSE_STATUS.RESPONSE_OK.getStatus(), src.cseq, responseContent);
            }
            controlServer.responseClient(responseEntity.toString());
        }


        private void handleTEARDOWN(RequestEntity src, String address) {
            String content = src.content;
            ResponseEntity responseEntity = processResponse(Constants.RESPONSE_STATUS.RESPONSE_NOT_FOUND.getStatus(),
                    src.cseq,
                    Constants.RESPONSE_STATUS.RESPONSE_NOT_FOUND.getMessage());
            String responseContent = Constants.RESPONSE_STATUS.RESPONSE_NOT_FOUND.getMessage();
            if (content.equalsIgnoreCase(Constants.TEARDOWN_REQUEST_KEY_WORD)) {
                responseContent = Constants.RESPONSE_STATUS.RESPONSE_OK.getMessage();
                responseEntity = processResponse(Constants.RESPONSE_STATUS.RESPONSE_OK.getStatus(), src.cseq, responseContent);
                sendBroadcast(Constants.BROADCAST_TYPE_TEARDOWN, address);
            }
            controlServer.responseClient(responseEntity.toString());
        }

        private void handlePLAY(RequestEntity src, String address) {
            sendBroadcast(Constants.BROADCAST_TYPE_PLAY, address);
            ResponseEntity responseEntity;
            String responseContent;
            responseContent = Constants.RESPONSE_STATUS.RESPONSE_OK.getMessage();
            responseEntity = processResponse(Constants.RESPONSE_STATUS.RESPONSE_OK.getStatus(), src.cseq, responseContent);
            controlServer.responseClient(responseEntity.toString());
        }

        private void handleSETUP(RequestEntity src) {
            String content = src.content;
            ResponseEntity responseEntity = processResponse(Constants.RESPONSE_STATUS.RESPONSE_NOT_FOUND.getStatus(),
                    src.cseq,
                    Constants.RESPONSE_STATUS.RESPONSE_NOT_FOUND.getMessage());
            String responseContent = Constants.RESPONSE_STATUS.RESPONSE_NOT_FOUND.getMessage();
            if (content.contains(Constants.SETUP_REQUEST_KEY_WORD)) {
                int clientVideoPort = Integer.parseInt(content.substring(content.indexOf("=") + 1));
                if (clientVideoPort > 0) {
                    Constants.VIDEO_CLIENT_PORT = clientVideoPort;
                    responseContent = "client-port=" + clientVideoPort + ";" +
                            "server-port=" + Constants.VIDEO_SERVER_PORT;
                    responseEntity = processResponse(Constants.RESPONSE_STATUS.RESPONSE_OK.getStatus(), src.cseq, responseContent);
                }
            }
            controlServer.responseClient(responseEntity.toString());
        }

        private ResponseEntity processResponse(int status, String cseq, String content) {
            ResponseEntity response = new ResponseEntity();
            response.status = String.valueOf(status);
            response.cseq = cseq;
            response.content = content;
            response.length = String.valueOf(content.length());
            return response;
        }

        private void sendBroadcast(int type, String address) {
            Intent broadcast = new Intent();
            broadcast.putExtra(Constants.BROADCAST_KEY_ADDRESS, address);
            switch (type) {
                case Constants.BROADCAST_TYPE_PLAY:
                    broadcast.setAction(Constants.BROADCAST_ACTION_PLAY);
                    break;
                case Constants.BROADCAST_TYPE_TEARDOWN:
                    broadcast.setAction(Constants.BROADCAST_ACTION_TEARDOWN);
                    break;
                case Constants.BROADCAST_TYPE_ERROR:
                    broadcast.setAction(Constants.BROADCAST_ACTION_ERROR);
                    break;
                default:
                    break;
            }
            mContext.sendBroadcast(broadcast);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelNotification();
        controlServer.closeServer();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
