package site.teamo.biu.net.learning.netty.chat;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/20
 */
public class ChatTest {
    public static void main(String[] args) throws IOException {
        ChatClient chatClient = ChatClient.open("锤子");
        chatClient.connect("127.0.0.1",6666);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String s = scanner.nextLine();
            chatClient.sendMsg(s);
            System.out.println("发送一条数据");
        }
    }
}
