package com.hulu.xuxin.hadoop.rpc;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

public class RPCClient {

    public static void main(String[] args) throws IOException {
        ClientProtocol proxy = (ClientProtocol)RPC.getProxy(ClientProtocol.class,
                                                            ClientProtocol.versionID,
                                                            new InetSocketAddress("localhost", 5555),
                                                            new Configuration());
        int result = proxy.add(5, 6);
        String echoResult = proxy.echo("result");
        System.out.println("result = " + result);
        System.out.println("echoResult = " + echoResult);
    }
}
