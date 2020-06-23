package site.teamo.biu.net.learning.netty.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/20
 */
public class ChatClient {
    private String username;

    private Selector selector;

    private SocketChannel socketChannel;

    private ChatClient(String username) {
        this.username = username;
    }

    private void init() throws IOException {
        selector = Selector.open();
    }

    public static ChatClient open(String username) throws IOException {
        ChatClient chatClient = new ChatClient(username);
        chatClient.init();
        return chatClient;
    }

    public void connect(String serverHost, int serverPort) throws IOException {
        socketChannel = SocketChannel.open(new InetSocketAddress(serverHost, serverPort));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        listener();
    }

    private void listener() {
        new Thread(() -> {
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
                        if (selectionKey.isReadable()) {
                            SocketChannel channel = (SocketChannel) selectionKey.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            int read = channel.read(buffer);
                            System.out.println(new String(buffer.array()));

                        }
                        System.out.println("移除事件");
                        iterator.remove();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendMsg(String msg) {
        msg = username + ":" + msg;
        try {
            socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ChatClient chatClient = ChatClient.open("郝大");
        chatClient.connect("127.0.0.1", 6666);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            chatClient.sendMsg(s);
        }
    }
}
