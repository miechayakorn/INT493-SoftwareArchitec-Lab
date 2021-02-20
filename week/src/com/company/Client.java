package com.company;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Client {

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        SocketChannel clientCh = SocketChannel.open();
        clientCh.configureBlocking(false);
        clientCh.register(selector, SelectionKey.OP_CONNECT);

        clientCh.connect(new InetSocketAddress("127.0.0.1", 9000));
        while (true) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> it = keys.iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove();

                //check or do something with event
                if (key.isConnectable()) {
                    SocketChannel ch = (SocketChannel) key.channel();
                    if (!clientCh.finishConnect()) {
                        ch.close();
                        continue;
                    }
                    ch.configureBlocking(false);
                    ch.register(selector, SelectionKey.OP_READ);
                }
                if (key.isReadable()) {
                    SocketChannel ch = (SocketChannel) key.channel();
                    ByteBuffer buf = ByteBuffer.allocate(20);
                    ch.read(buf);
                    buf.flip();
                    System.out.println(new String(buf.array()));
                }
            }
        }
    }
}
