package site.teamo.biu.net.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/18
 */
@SpringBootApplication(scanBasePackages = "site.teamo.biu.net")
@EnableScheduling
public class BiuNetClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(BiuNetClientApplication.class, args);
    }
}
