package com.done.sharescreenserver;

import java.nio.ByteBuffer;

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
 * @date 2018/1/11
 */

public class TestJava {
    private static ByteBuffer byteBuffer;

    private static final int MAX_LENTH = 2 * 60 * 1024;

    public static void main(String[] args) {
        init();
        byte[] tempData1 = new byte[]{0x04, 0x01, 0x01, 0x02, 0x03};
        byte[] tempData2 = new byte[]{0x04, 0x02, 0x04, 0x05, 0x06};
        byte[] tempData3 = new byte[]{0x04, 0x03, 0x07, 0x08, 0x09};
        byte[] tempData4 = new byte[]{0x04, 0x04, 0x0A};
        byte[] tempData5 = new byte[]{0x01, 0x01, 0x01, 0x02, 0x03};
        receivePackage(tempData1);
        receivePackage(tempData2);
        receivePackage(tempData3);
        receivePackage(tempData4);
        String log = "";
        int dataLen = byteBuffer.position();
        byte[] packages = new byte[dataLen];
        byteBuffer.flip();
        byteBuffer.get(packages);
        for (byte aPackage : packages) {
            log += String.format("%02x,", aPackage);
        }
        System.out.println(log);
    }

    private static byte[] receivePackage(byte[] data) {
        int allCount = data[0];
        int curPackage = data[1];
        int realDataLen = data.length - 2;
        byte[] ret = null;

        String content = "";
        if (allCount == 1 && curPackage == 1) {
            ret = new byte[realDataLen];
            System.arraycopy(data, 2, ret, 0, realDataLen);
            content = "only one:";
            for (byte b : ret) {
                content = content + String.format("%02x,", b);
            }
            System.out.println(content);
            return ret;
        }
        if (allCount > 1) {
            System.out.println("allCount:" + allCount + ",curPackage:" + curPackage + ",put:" + data[2]);
            byteBuffer.put(data, 2, realDataLen);
        }
        return ret;
    }

    private static void init() {
        byteBuffer = ByteBuffer.allocateDirect(MAX_LENTH);
        byteBuffer.putInt(1);
        System.out.println(String.format("%02x,%02x,%02x,%02x",byteBuffer.get(1),byteBuffer.get(2),byteBuffer.get(3),byteBuffer.get(4)));
    }
}
