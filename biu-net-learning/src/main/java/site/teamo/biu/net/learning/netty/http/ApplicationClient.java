package site.teamo.biu.net.learning.netty.http;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/15
 */
public class ApplicationClient {
    public static void main(String[] args) throws InterruptedException {
        new Client("client1").connect("localhost",8080);
    }
}
