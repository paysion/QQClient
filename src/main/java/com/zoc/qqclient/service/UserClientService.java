package com.zoc.qqclient.service;

import com.zoc.qqcommon.Message;
import com.zoc.qqcommon.MessageType;
import com.zoc.qqcommon.User;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class UserClientService {
    // 因为我们可能在其他地方用使用user信息, 因此作出成员属性
    private User user = new User();
    // 因为Socket在其它地方也可能使用，因此作出属性
    private Socket socket;

    public boolean checkUser(String userId, String pwd) {
        boolean b = false;
        //创建User对象
        user.setUserId(userId);
        user.setPasswd(pwd);

        try {
            // 连接到服务端，发送u对象
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
            // 得到ObjectOutputStream对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);//发送User对象

            // 读取从服务器回复的 Message 对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message ms = (Message) ois.readObject();

            if (ms.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)) {// 登录OK

                // 创建一个和服务器端保持通信的线程-> 创建一个类 ClientConnectServerThread
                ClientConnectServerThread clientConnectServerThread =
                        new ClientConnectServerThread(socket);
                // 启动客户端的线程
                clientConnectServerThread.start();
                // 这里为了后面客户端的扩展，我们将线程放入到集合管理
                ManageClientConnectServerThread.addClientConnectServerThread(userId, clientConnectServerThread);
                b = true;
            } else {
                // 如果登录失败, 我们就不能启动和服务器通信的线程, 关闭 socket
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    public void onlineFriendList() {
    }

    public void logout() {
    }
}
