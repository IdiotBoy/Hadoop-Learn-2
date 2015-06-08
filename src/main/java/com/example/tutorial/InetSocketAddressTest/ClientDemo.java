package com.example.tutorial.InetSocketAddressTest;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientDemo {

    public static void main(String[] args) throws IOException, InterruptedException {
        InetSocketAddress socketAddress = new InetSocketAddress(InetAddress.getLocalHost(), 5555);
        SocketChannel channel = SocketChannel.open(socketAddress);
        channel.configureBlocking(false);
        
        ByteBuffer buffer = ByteBuffer.allocate(200);
        while (true) {
            int n = channel.read(buffer);
            System.out.println(n);
            System.out.println("recieve data: " + new String(buffer.array(), 0, n));
            Thread.sleep(1000);
            buffer.flip();
        }
    }

}
