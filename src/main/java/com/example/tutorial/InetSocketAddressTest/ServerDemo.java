package com.example.tutorial.InetSocketAddressTest;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ServerDemo {

    public static void main(String[] args) throws IOException, InterruptedException {
        InetSocketAddress socketAddress = new InetSocketAddress(InetAddress.getLocalHost(), 5555);
        ServerSocketChannel server = ServerSocketChannel.open();
        server.socket().bind(socketAddress);
        SocketChannel channel = null;
        while ((channel = server.accept()) != null) {
            System.out.println(".");
            OutputStream out = channel.socket().getOutputStream();
            while (true) {
                System.out.print(",");
                out.write("hello world !!".getBytes());
                out.flush();
                Thread.sleep(1000);
            }
        }
    }
}
