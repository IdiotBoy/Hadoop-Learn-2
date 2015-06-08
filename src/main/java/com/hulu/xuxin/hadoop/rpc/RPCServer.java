package com.hulu.xuxin.hadoop.rpc;

import java.io.IOException;

import org.apache.hadoop.HadoopIllegalArgumentException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.Server;

public class RPCServer {

    public static void main(String[] args) throws HadoopIllegalArgumentException, IOException {
        Server server = new RPC.Builder(new Configuration())
                                .setProtocol(ClientProtocol.class)
                                .setInstance(new ClientProtocolImpl())
                                .setBindAddress("localhost")
                                .setPort(5555)
                                .setNumHandlers(2)
                                .build();
        server.start();
    }

}
