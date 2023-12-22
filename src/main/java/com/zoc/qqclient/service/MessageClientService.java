package com.zoc.qqclient.service;

import com.zoc.qqcommon.Message;
import com.zoc.qqcommon.MessageType;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

public class MessageClientService {
    public void sendMessageToAll(String s, String userId) {
        // 构建message消息
        Message message = new Message();
        message.setSender(userId);
        message.setContent(s);
        // 忘记传递消息类型了
        message.setMesType(MessageType.MESSAGE_TO_ALL_MES);
        message.setSendTime(new Date().toString());

        // 这里其实就一样了，只需要将消息传递给服务端就行了
        try {
            ClientConnectServerThread clientConnectServerThread = ManageClientConnectServerThread.getClientConnectServerThread(userId);
            Socket socket = clientConnectServerThread.getSocket();
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // //编写一个方法，将私聊消息发送给服务器端
    public void sendMessageToOne(String content, String userId, String getterId) {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_COMM_MES);
        // 将userId告知客户端，这里的userId是从哪里传过来的呢？
        message.setSender(userId);
        // 想跟谁聊天？
        message.setGetter(getterId);
        // 聊点什么呢？
        message.setContent(content);
        // 发送时间设置到message对象
        message.setSendTime(new Date().toString());
        System.out.println(userId + " 对 " + getterId + " 说 " + content);

        // 发送到服务端
        try {
            // 这里的线程是哪里来的？这里的线程在用户登录的时候线程就一直存在，在ClientConnectServerThread.run()方法中。
            ClientConnectServerThread clientConnectServerThread = ManageClientConnectServerThread.getClientConnectServerThread(userId);
            Socket socket = clientConnectServerThread.getSocket();
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
