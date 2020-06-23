package site.teamo.biu.net.learning.netty.http;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/15
 */
public class MainApplication {
    public static void main(String[] args) throws InterruptedException {
//        new MainServer(8080).start();
//        new Thread(()-> {
//            try {
//                new MainServer(8080).start();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start();
        new ForwardServer(8081).bind();
    }
}
