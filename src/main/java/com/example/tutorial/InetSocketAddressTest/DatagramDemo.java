package com.example.tutorial.InetSocketAddressTest;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class DatagramDemo {
    private static final int port = 30006;

    public static void main(String[] args) throws Exception {
        Thread server = new ServerThread();
        server.start();
        Thread client = new ClientThread();
        client.start();
        Thread.sleep(3000);
    }

    static class ServerThread extends Thread {
        @Override
        public void run() {
            try {
                DatagramSocket socket = new DatagramSocket(null);// null is
                InetSocketAddress address = new InetSocketAddress(port);//server端bind到本地端口
                socket.bind(address);
                //read
                DatagramPacket rp = new DatagramPacket(new byte[1024], 1024);
                socket.receive(rp);
                System.out.println("server Receive :" + new String(rp.getData()));
                //reply
                InetAddress from = rp.getAddress();
                rp.setAddress(from);
                String message = "--server--" + System.currentTimeMillis();
                rp.setData(message.getBytes());
                socket.send(rp);
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class ClientThread extends Thread {
        @Override
        public void run() {
            try {
                //send
                DatagramSocket socket = new DatagramSocket();
                InetSocketAddress address = new InetSocketAddress(
                        InetAddress.getLocalHost(), port);
                DatagramPacket sp = new DatagramPacket(new byte[1024], 1024);
                String data = "--client--" + System.currentTimeMillis();
                sp.setData(data.getBytes());
                sp.setSocketAddress(address);
                socket.send(sp);
                //block for receiving.
                socket.receive(sp);
                System.out.println("client receive:" + new String(sp.getData()));
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}