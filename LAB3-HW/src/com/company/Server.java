package com.company;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class Server {

    private static final ArrayList<SocketChannel> clients = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        Selector selector = Selector.open();

        ServerSocketChannel serverCh = ServerSocketChannel.open();
        serverCh.configureBlocking(false);

        // Run Server port 9000
        serverCh.bind(new InetSocketAddress(9000));
        serverCh.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println(" Server started :  connection");
        while (true) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> it = keys.iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                if (key.isAcceptable()) {
                    System.out.println("Got new client");
                    ServerSocketChannel ch = (ServerSocketChannel) key.channel();
                    SocketChannel clientCh = ch.accept();
                    clientCh.configureBlocking(false);
                    clientCh.register(selector, SelectionKey.OP_READ);
                    clients.add(clientCh);
                }

                if (key.isReadable()) {
                    SocketChannel ch = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(512);
                    ch.configureBlocking(false);
                    buffer.clear();
                    int n = ch.read(buffer);
                    if (n == -1) {
                        ch.close();
                        continue;
                    }
                    buffer.flip();
                    String message = new String(buffer.array());
                    System.out.println("Receive message from client : " + message);
                    for (SocketChannel client : clients) {
                        SocketChannel value = client;
                        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                        writeBuffer.put(("Message: " + message).getBytes());
                        writeBuffer.flip();
                        value.write(writeBuffer);
                    }
                }
                it.remove();
            }
        }

    }
}