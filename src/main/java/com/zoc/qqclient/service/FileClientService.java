package com.zoc.qqclient.service;

import com.zoc.qqcommon.Message;
import com.zoc.qqcommon.MessageType;

import java.io.*;

public class FileClientService {
    public void sendFileToOne(String src, String dest, String userId, String getterId) {
        //读取src文件  -->  message
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_FILE_MES);
        message.setSender(userId);
        message.setGetter(getterId);
        message.setSrc(src);
        message.setDest(dest);
        //提示信息
        System.out.println("\n" + userId + " 给 " + getterId + " 发送文件: " + src
                + " 到对方的电脑的目录 " + dest);

        // 读取文件
        FileInputStream fileInputStream = null;
        byte[] bytes = new byte[(int) new File(src).length()];

        try {
            fileInputStream = new FileInputStream(src);
            // 将src文件读入到程序的字节数组
            fileInputStream.read(bytes);
            message.setFileBytes(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭文件输入流
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // 发送文件
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(userId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
