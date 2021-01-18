package site.teamo.biu.net.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/12
 */
@SpringBootApplication(scanBasePackages = "site.teamo.biu.net")
@MapperScan(basePackages = {"site.teamo.biu.net.server.web.dao"})
public class BiuNetServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BiuNetServerApplication.class, args);
    }
}
