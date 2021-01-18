package site.teamo.biu.net.server.web.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ProxyVO {
    /**
     * 目标主机名
     */
    private String targetHost;

    /**
     * 目标端口
     */
    private Integer targetPort;

    /**
     * 代理服务器
     */
    private ProxyServerVO proxyServer;

    /**
     * 客户端
     */
    private ClientVO client;
}
