package com.hulu.xuxin.hadoop.rpc;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.Server;

public class RPCDemo {
    private static final int port = 30006;
    private static String host = "localhost";

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
                Server server = new RPC.Builder(new Configuration())
                                       .setProtocol(ClientProtocol.class)
                                       .setInstance(new ClientProtocolImpl())
                                       .setBindAddress(host)
                                       .setPort(port)
                                       .setNumHandlers(2)
                                       .build();
                server.start();
                Thread.sleep(3000);
                server.stop();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class ClientThread extends Thread {
        @Override
        public void run() {
            try {
                ClientProtocol proxy = (ClientProtocol)RPC.getProxy(ClientProtocol.class,
                                                                    ClientProtocol.versionID,
                                                                    new InetSocketAddress(host, port),
                                                                    new Configuration());
                int result = proxy.add(5, 6);
                String echoResult = proxy.echo("result");
                System.out.println("result = " + result);
                System.out.println("echoResult = " + echoResult);
                RPC.stopProxy(proxy);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
