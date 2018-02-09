#include <jni.h>
#include <string>
#include <android/log.h>
#include <string.h>
#include <errno.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <linux/input.h>
#include <sys/time.h>
#include <sys/types.h>
#include <unistd.h>

static const char *TAG = "DONE-TOUCH";
#define LOG_TAG "ZJX"
#define ABS_MT_SLOT 0x2f
#define ABS_MT_TOUCH_MAJOR 0x30
/* WARNING: DO NOT EDIT, AUTO-GENERATED CODE - SEE TOP FOR INSTRUCTIONS */
#define ABS_MT_TOUCH_MINOR 0x31
#define ABS_MT_WIDTH_MAJOR 0x32
#define ABS_MT_WIDTH_MINOR 0x33
#define ABS_MT_ORIENTATION 0x34
/* WARNING: DO NOT EDIT, AUTO-GENERATED CODE - SEE TOP FOR INSTRUCTIONS */
#define ABS_MT_POSITION_X 0x35
#define ABS_MT_POSITION_Y 0x36
#define ABS_MT_TOOL_TYPE 0x37
#define ABS_MT_BLOB_ID 0x38
/* WARNING: DO NOT EDIT, AUTO-GENERATED CODE - SEE TOP FOR INSTRUCTIONS */
#define ABS_MT_TRACKING_ID 0x39
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

int finger_count = 0;
int fd_touch = 0;
int finger_id = 520;

void sendEvent(__u16 type, __u16 code, int value) {
    struct input_event event;
    event.type = type;
    event.code = code;
    event.value = value;
    //    gettimeofday(&event.time,0);
    write(fd_touch, &event, sizeof(event));
}

void touchMove(int finger_index, int x, int y) {
    sendEvent(EV_ABS, ABS_MT_SLOT, finger_index);
    sendEvent(EV_ABS, ABS_MT_POSITION_X, x);
    sendEvent(EV_ABS, ABS_MT_POSITION_Y, y);
    sendEvent(EV_SYN, SYN_REPORT, 0);
}

void touchDown(int finger_index, int x, int y) {
    finger_count++;
    sendEvent(EV_ABS, ABS_MT_SLOT, finger_index);
    //需要指定ABS_MT_TRACKING_ID，否则sendevent( EV_ABS, ABS_MT_TRACKING_ID, -1);不会生效
    sendEvent(EV_ABS, ABS_MT_TRACKING_ID, finger_id++);
    sendEvent(EV_ABS, ABS_MT_POSITION_X, x);
    sendEvent(EV_ABS, ABS_MT_POSITION_Y, y);
    if (finger_count == 1) {
        sendEvent(EV_KEY, BTN_TOUCH, 1);
    }
    sendEvent(EV_SYN, SYN_REPORT, 0);
}

void touchUp(int finger_index) {
    finger_count--;
    sendEvent(EV_ABS, ABS_MT_SLOT, finger_index);
    sendEvent(EV_ABS, ABS_MT_TRACKING_ID, -1);
    if (finger_count == 0) {
        sendEvent(EV_KEY, BTN_TOUCH, 0);
    }
    sendEvent(EV_SYN, SYN_REPORT, 0);
}

extern "C"
JNIEXPORT jstring


JNICALL
Java_com_done_sharescreenserver_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_done_sharescreenserver_util_WindowController_touch(JNIEnv *env, jobject instance,
                                                            jint type, jint fingerIndex, jint x,
                                                            jint y) {
    char *returnValue = (char *) "open failed!";
    if (fd_touch <= 0) {
        fd_touch = open("/dev/input/event3", O_RDWR);
    }
    LOGD("fd_touch:%d", fd_touch);
    if (fd_touch <= 0) {
        return env->NewStringUTF(returnValue);
    }
    returnValue = (char *) "open success!";
    struct timeval cur;
    gettimeofday(&cur, NULL);
    switch (type) {
        case 0:
            returnValue = (char *) "touch Down!";
            touchDown(fingerIndex, x, y);
            break;
        case 1:
            returnValue = (char *) "touch Up!";
            touchUp(fingerIndex);
            break;
        case 2:
            returnValue = (char *) "touch Move";
            touchMove(fingerIndex, x, y);
            break;
        default:
            returnValue = (char *) "nothing!";
            break;
    }
    return env->NewStringUTF(returnValue);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_done_sharescreenserver_util_WindowController_closeTouch(JNIEnv *env, jobject instance) {
    LOGD("close touch fd:%d", fd_touch);
    if (fd_touch > 0) {
        close(fd_touch);
    }
}