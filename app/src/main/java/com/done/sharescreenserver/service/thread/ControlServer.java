package com.done.sharescreenserver.service.thread;

import android.text.TextUtils;

import com.done.sharescreenserver.Constant.Constants;
import com.done.sharescreenserver.service.model.RequestEntity;
import com.done.sharescreenserver.util.DoneLogger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


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

public class ControlServer extends Thread {

    private static final String TAG = "ControlServer";

    private boolean isRun = false;

    private ServerSocket serverSocket;

    private Map<String, TcpClientReceiver> tcpClientReceiverMap;

    private OnRequest onRequest;


    public ControlServer() {
        tcpClientReceiverMap = new ConcurrentHashMap<>();
    }

    public void enableServer(OnRequest onRequest) {
        if (!isRun) {
            this.onRequest = onRequest;
            this.start();
        }
    }

    public void closeServer() {
        isRun = false;
        for (TcpClientReceiver receiver : tcpClientReceiverMap.values()) {
            receiver.closeClient();
        }
        if (serverSocket != null) {
            try {
                serverSocket.close();
                serverSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void responseClient(String data) {
        for (TcpClientReceiver receiver : tcpClientReceiverMap.values()) {
            receiver.send(data);
        }
    }


    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

        initServer();

        while (isRun) {
            handleClient();
        }
    }

    private void handleClient() {
        try {
            Socket socket = serverSocket.accept();
            if (tcpClientReceiverMap.size() > 0) {
                socket.close();
                DoneLogger.d(TAG, "已经有一个客户端了,踢掉这个:" + socket.getInetAddress().getHostAddress());
            } else {
                ClientStatusImpl clientStatus = new ClientStatusImpl();
                TcpClientReceiver receiver = new TcpClientReceiver(socket, clientStatus);
                tcpClientReceiverMap.put(socket.getInetAddress().getHostAddress(), receiver);
                receiver.start();
                DoneLogger.d(TAG, "进来一个客户端:" + socket.getInetAddress().getHostAddress());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initServer() {
        try {
            serverSocket = new ServerSocket(Constants.CONTROL_SERVER_PORT);
            isRun = true;
            DoneLogger.d(TAG, "tcp 服务端创建成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ClientStatusImpl implements OnClientStatus {

        @Override
        public void onDisconnect(String address) {
            DoneLogger.d(TAG, address + " is disconnect");
            tcpClientReceiverMap.get(address).closeClient();
            tcpClientReceiverMap.remove(address);
            callbackError(-1, address);
        }

        @Override
        public void onError(String address) {
            DoneLogger.d(TAG, address + " is Error");
            tcpClientReceiverMap.get(address).closeClient();
            tcpClientReceiverMap.remove(address);
            callbackError(-1, address);
        }

        @Override
        public void onReceive(String address, RequestEntity requestEntity) {
            DoneLogger.d(TAG, address + " is receive:" + requestEntity.toString());
            callback(address, requestEntity);
        }

        private void callback(String address, RequestEntity requestEntity) {
            switch (requestEntity.method) {
                case Constants.RTDP_METHOD_SETUP:
                    callbackSETUP(requestEntity, address);
                    break;
                case Constants.RTDP_METHOD_PLAY:
                    callbackPLAY(requestEntity, address);
                    break;
                case Constants.RTDP_METHOD_TEARDOWN:
                    callbackTRAMDOWN(requestEntity, address);
                    break;
                case Constants.RTDP_METHOD_HEART:
                    callbackHEART(requestEntity, address);
                    break;
                case Constants.RTDP_METHOD_HOME:
                    callbackHOME(requestEntity, address);
                    break;
                case Constants.RTDP_METHOD_BACK:
                    callbackBACK(requestEntity, address);
                    break;
                case Constants.RTDP_METHOD_MENU:
                    callbackMENU(requestEntity, address);
                    break;
                case Constants.RTDP_METHOD_VOLUME:
                    callbackVOLUME(requestEntity, address);
                    break;
                case Constants.RTDP_METHOD_CLICK:
                    callbackCLICK(requestEntity, address);
                    break;
                case Constants.RTDP_METHOD_TOUCH:
                    callbackTOUCH(requestEntity, address);
                    break;
                default:
                    break;
            }
        }

        private void callbackTOUCH(RequestEntity requestEntity, String address) {
            if (onRequest != null) {
                onRequest.onTOUCH(requestEntity, address);
            }
        }

        private void callbackCLICK(RequestEntity requestEntity, String address) {
            if (onRequest != null) {
                onRequest.onCLICK(requestEntity, address);
            }
        }

        private void callbackHOME(RequestEntity requestEntity, String address) {
            if (onRequest != null) {
                onRequest.onHOME(requestEntity, address);
            }
        }

        private void callbackBACK(RequestEntity requestEntity, String address) {
            if (onRequest != null) {
                onRequest.onBACK(requestEntity, address);
            }
        }

        private void callbackMENU(RequestEntity requestEntity, String address) {
            if (onRequest != null) {
                onRequest.onMENU(requestEntity, address);
            }
        }

        private void callbackVOLUME(RequestEntity requestEntity, String address) {
            if (onRequest != null) {
                onRequest.onVOLUME(requestEntity, address);
            }
        }

        private void callbackSETUP(RequestEntity requestEntity, String address) {
            if (onRequest != null) {
                onRequest.onSETUP(requestEntity, address);
            }
        }

        private void callbackPLAY(RequestEntity requestEntity, String address) {
            if (onRequest != null) {
                onRequest.onPLAY(requestEntity, address);
            }
        }

        private void callbackTRAMDOWN(RequestEntity requestEntity, String address) {
            if (onRequest != null) {
                onRequest.onTEARDOWN(requestEntity, address);
            }
        }

        private void callbackHEART(RequestEntity requestEntity, String address) {
            if (onRequest != null) {
                onRequest.onHEART(requestEntity, address);
            }
        }

        private void callbackError(int code, String address) {
            if (onRequest != null) {
                onRequest.onError(code, address);
            }
        }
    }


    private class TcpClientReceiver extends Thread {

        private boolean isRun = false;

        private Socket client;
        private DataOutputStream out;
        private BufferedReader in;
        private OnClientStatus onClientStatus;
        private String address;

        public TcpClientReceiver(Socket socket, OnClientStatus onClientStatus) {
            client = socket;
            address = socket.getInetAddress().getHostAddress();
            this.onClientStatus = onClientStatus;
            try {
                out = new DataOutputStream(socket.getOutputStream());
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                isRun = true;
            } catch (IOException e) {
                DoneLogger.e(TAG, "获取接入服务端的tcp客户端IO流失败");
                e.printStackTrace();
            }
        }

        public void send(String data) {
            try {
                DoneLogger.d(TAG, "发送客户端数据:" + data);
                out.write(data.getBytes());
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void closeClient() {
            isRun = false;
            if (in != null) {
                try {
                    in.close();
                    in = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                    out = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (client != null) {
                try {
                    client.close();
                    client = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            String line;
            while (isRun) {
                try {
                    line = in.readLine();
                    if (line == null) {
                        DoneLogger.e(TAG, "客户端主动断开了");
                        throw new SocketException("client is disconnect");
                    }
                    if (!TextUtils.isEmpty(line)) {
                        RequestEntity requestEntity = parseRequest(line);
                        if (requestEntity != null) {
                            callbackReceive(requestEntity);
                        } else {
                            DoneLogger.e(TAG, "不识别客户端请求:" + line);
                        }
                    }
                } catch (SocketException e) {
                    callbackDisconn();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    callbackError();
                }
            }
        }

        private RequestEntity parseRequest(String line) {
            RequestEntity request = new RequestEntity();
            String[] contents = line.split(Constants.SPACE);
            if (contents.length == Constants.COUNT_REQUEST_PART) {
                request.method = contents[0];
                request.cseq = contents[1];
                request.length = contents[2];
                request.content = contents[3];
                return request;
            } else {
                return null;
            }
        }

        private void callbackDisconn() {
            if (onClientStatus != null) {
                onClientStatus.onDisconnect(address);
            }
        }

        private void callbackError() {
            if (onClientStatus != null) {
                onClientStatus.onError(address);
            }
        }

        private void callbackReceive(RequestEntity requestEntity) {
            if (onClientStatus != null) {
                onClientStatus.onReceive(address, requestEntity);
            }
        }

    }

    public interface OnClientStatus {
        void onDisconnect(String address);

        void onError(String address);

        void onReceive(String address, RequestEntity requestEntity);
    }
}
