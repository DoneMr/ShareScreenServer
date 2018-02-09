package com.done.sharescreenserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.done.sharescreenserver.Constant.Constants;
import com.done.sharescreenserver.service.ScreenRecordService;
import com.done.sharescreenserver.service.screenrecorder.VideoEncodeConfig;
import com.done.sharescreenserver.service.thread.VideoEncoderUtil;
import com.done.sharescreenserver.util.DoneLogger;
import com.done.sharescreenserver.util.PhoneUtils;
import com.done.sharescreenserver.util.WindowController;
import com.done.sharescreenserver.service.screenrecorder.*;

import java.io.File;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private static final int SCREEN_REQUEST_CODE = 110;

    private Context mContext;

    private ServerReceiver serverReceiver;

    private MediaProjectionManager projectionManager;

    private VideoEncoderUtil videoEncoderUtil;

    private String clientAddress = "";

    private TextView tvInfo;

    MediaCodecInfo[] mediaCodecInfos;

    ScreenRecorder screenRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        projectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        initCodecInfos();
        initView();
        initServer();
        initBroadcast();
    }

    private void initCodecInfos() {
        Utils.findEncodersByTypeAsync(MediaFormat.MIMETYPE_VIDEO_AVC, infos -> {
            mediaCodecInfos = infos;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == SCREEN_REQUEST_CODE && resultCode == RESULT_OK) {
//            MediaProjection mediaProjection = projectionManager.getMediaProjection(resultCode, data);
//            stopScreenRecord();
//            tvInfo.setText("正在录屏:" + clientAddress);
//            videoEncoderUtil = new VideoEncoderUtil(mediaProjection, clientAddress);
//            videoEncoderUtil.start();
//        }
        if (requestCode == SCREEN_REQUEST_CODE && resultCode == RESULT_OK) {
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),
                    "ScreenCaptures");
            if (!dir.exists() && !dir.mkdirs()) {
                stopRecord();
                return;
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US);
            final File file = new File(dir, "Screen-" + format.format(new Date())
                    + ".mp4");
            VideoEncodeConfig video = createVideoConfig();
            MediaProjection mediaProjection = projectionManager.getMediaProjection(resultCode, data);
            screenRecorder = newRecorder(mediaProjection, video, file.getAbsolutePath());
            startRecord();
            tvInfo.setText("正在录屏:" + clientAddress);
        }
    }

    private void startRecord() {
        if (screenRecorder != null) {
            screenRecorder.start();
        }
    }

    private void stopRecord() {
        if (screenRecorder != null) {
            screenRecorder.quit();
            screenRecorder = null;
        }
    }

    private ScreenRecorder newRecorder(MediaProjection mediaProjection,
                                       VideoEncodeConfig video,
                                       String filePath) {
        ScreenRecorder recorder = new ScreenRecorder(video,
                1,
                mediaProjection,
                filePath,
                clientAddress);
        recorder.setCallback(new ScreenRecorder.Callback() {
            @Override
            public void onStop(Throwable error) {
                if (error != null) {
                    DoneLogger.e(TAG, error.getMessage());
                }
            }

            @Override
            public void onStart() {
                DoneLogger.d(TAG, "onStart");
            }

            @Override
            public void onRecording(long presentationTimeUs) {
                DoneLogger.d(TAG, "onRecording");
            }
        });
        return recorder;
    }

    private VideoEncodeConfig createVideoConfig() {
        int width = 720;// 720;
        int height = 1280;//1280;
        int bitrate = 800 * 1000;
        int framerate = 5;
        String codecName = findCodecName();
        String mimeType = MediaFormat.MIMETYPE_VIDEO_AVC;

        return new VideoEncodeConfig(width,
                height,
                bitrate,
                framerate,
                5,
                codecName,
                mimeType,
                null);
    }

    private String findCodecName() {
        for (int i = 0; i < mediaCodecInfos.length; i++) {
            if (mediaCodecInfos[i].getName().contains("qcom")) {
                return mediaCodecInfos[i].getName();
            }
        }
        return mediaCodecInfos[0].getName();
    }


    private void initBroadcast() {
        serverReceiver = new ServerReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.BROADCAST_ACTION_PLAY);
        intentFilter.addAction(Constants.BROADCAST_ACTION_TEARDOWN);
        intentFilter.addAction(Constants.BROADCAST_ACTION_ERROR);
        registerReceiver(serverReceiver, intentFilter);
    }


    private void initServer() {
        PhoneUtils.enalbeWifiAp(Constants.WIFI_NAME, Constants.WIFI_PASSWORD, false, true);
        Intent service = new Intent(mContext, ScreenRecordService.class);
        startService(service);
    }

    private void initView() {
        tvInfo = findViewById(R.id.tv_info);
        tvInfo.setText("停止录屏");
        tvInfo.setOnClickListener(this);
    }

    private void startScreenRecord() {
        if (!TextUtils.isEmpty(clientAddress)) {
            Intent captureIntent = projectionManager.createScreenCaptureIntent();
            startActivityForResult(captureIntent, SCREEN_REQUEST_CODE);
        }
    }

    private void stopScreenRecord() {
        if (videoEncoderUtil != null) {
            tvInfo.setText("停止录屏");
            videoEncoderUtil.stop();
            videoEncoderUtil = null;
        }
        if (screenRecorder != null) {
            screenRecorder.quit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent service = new Intent(mContext, ScreenRecordService.class);
        stopService(service);
        unregisterReceiver(serverReceiver);
        stopRecord();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_info:
                WindowController.getInstance().test();
                break;
            default:
                break;
        }
    }

    private class ServerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String address = intent.getStringExtra(Constants.BROADCAST_KEY_ADDRESS);

            switch (action) {
                case Constants.BROADCAST_ACTION_PLAY:
                    if (!TextUtils.isEmpty(address)) {
                        clientAddress = address;
                        startScreenRecord();
                    }
                    break;
                case Constants.BROADCAST_ACTION_TEARDOWN:
                    stopScreenRecord();
                    break;
                case Constants.BROADCAST_ACTION_ERROR:
                    stopScreenRecord();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        DoneLogger.d(TAG, "move Task To Back!");
        moveTaskToBack(true);
    }
}
