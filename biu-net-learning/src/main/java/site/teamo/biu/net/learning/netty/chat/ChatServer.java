package site.teamo.biu.net.learning.netty.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/20
 */
public class ChatServer {

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private int port;

    private ChatServer(int port) {
        this.port = port;
    }

    private void init() throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public static ChatServer open(int port) throws IOException {
        ChatServer chatServer = new ChatServer(port);
        chatServer.init();
        return chatServer;
    }

    public void listen() {
        while (true) {
            try {
                int select = selector.select();
                System.out.println("发现事件个数：" + select);
                if (select <= 0) {
                    continue;
                }

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isAcceptable()) {
                        accept();
                    }
                    if (selectionKey.isReadable()) {
                        handle(selectionKey);
                    }
                    iterator.remove();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void accept() throws IOException {
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        System.out.println(socketChannel.getRemoteAddress() + "上线");
    }

    private void handle(SelectionKey selectionKey) {
        SocketChannel channel = null;
        try {
            channel = (SocketChannel) selectionKey.channel();
            ByteBuffer buffer = ByteBuffer.allocate(2);
            while (true) {
                buffer.clear();
                int read = channel.read(buffer);
                if (read == -1 || read==0) {
                    break;
                }
                Set<SocketChannel> clientChannels = getClientChannel();
                byte[] array = buffer.array();
                for (SocketChannel clientChannel : clientChannels) {
                    if (clientChannel.equals(channel)) {
                        continue;
                    }
                    clientChannel.write(ByteBuffer.wrap(array));

                }
            }

        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + "离线");
                selectionKey.channel();
                channel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }

    private Set<SocketChannel> getClientChannel() {
        Set<SelectionKey> keys = selector.keys();
        Set<SocketChannel> result = new HashSet<>();
        for (SelectionKey key : keys) {
            SelectableChannel channel = key.channel();
            if (channel instanceof SocketChannel) {
                result.add((SocketChannel) channel);
            }
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        ChatServer chatServer = ChatServer.open(6666);
        chatServer.listen();
    }

}
