package site.teamo.biu.net.learning.netty.talk;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/20
 */
public class Client {

    public static void main(String[] args) throws IOException {
        new Client().start();
    }

    public void start() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        boolean localhost = socketChannel.connect(new InetSocketAddress("localhost", 6666));
        if (!localhost) {
            while (!socketChannel.finishConnect()) {
                System.out.println("尝试连接!");
            }
        }

        String test = "123456abc";
        ByteBuffer buffer = ByteBuffer.wrap(test.getBytes());
        socketChannel.write(buffer);

        socketChannel.socket().close();
        socketChannel.close();

    }
}
