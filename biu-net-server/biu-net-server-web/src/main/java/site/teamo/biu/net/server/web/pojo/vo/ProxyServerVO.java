package site.teamo.biu.net.server.web.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/28
 */
@Data
@Builder
@Accessors(chain = true)
public class ProxyServerVO {

    /**
     * 代理服务id
     */
    private String id;


    /**
     * 代理服务监听端口
     */
    private Integer port;

}
