package com.zoc.qqclient.service;

import com.zoc.qqcommon.Message;
import com.zoc.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientConnectServerThread extends Thread{
    private Socket socket;
    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("客户端线程，等待从读取从服务器端发送的消息");
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                // 如果服务器没有发送Message对象,线程会阻塞在这里
                Message message = (Message) ois.readObject();
                // 注意，后面我们需要去使用message
                // 判断这个message类型，然后做相应的业务处理
                // 如果是读取到的是 服务端返回的在线用户列表
                if (message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)) {
                    //取出在线列表信息，并显示
                    String[] onlineUsers = message.getContent().split(" ");
                    System.out.println("\n=======当前在线用户列表========");
                    for (int i = 0; i < onlineUsers.length; i++) {
                        System.out.println("用户: " + onlineUsers[i]);
                    }
                } else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)) { //普通的聊天消息

                } else {
                    System.out.println("是其他类型的message, 暂时不处理....");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 为了更方便拿去socket
    public Socket getSocket() {
        return socket;
    }
}
