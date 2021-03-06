package com.company;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {
    public static void main(String[] args) throws IOException, InterruptedException {
        Selector selector = Selector.open();

        ServerSocketChannel serverCh = ServerSocketChannel.open();
        serverCh.configureBlocking(false);
        serverCh.bind(new InetSocketAddress(9000));
        serverCh.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("listen for connection");
        while (true) {
            selector.select(); //watching events
            System.out.println("go some events");
            Set<SelectionKey> keys = selector.selectedKeys(); //list of events happen

            Iterator<SelectionKey> it = keys.iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove();

                if (key.isAcceptable()) {
                    ServerSocketChannel ch = (ServerSocketChannel) key.channel();
                    SocketChannel clientCh = ch.accept();
                    clientCh.configureBlocking(false);
                    clientCh.register(selector, SelectionKey.OP_WRITE);
                }
                if (key.isWritable()) {
                    //client ready for write
                    SocketChannel ch = (SocketChannel) key.channel();
                    ByteBuffer buf = ByteBuffer.allocate(20);
                    String msg = String.format("TIME:%d\n", System.currentTimeMillis());
                    buf.put(msg.getBytes());
                    buf.flip();
                    ch.write(buf);
                    Thread.sleep(1000);
                }
            }
        }
    }
}
