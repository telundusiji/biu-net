package site.teamo.biu.net.common.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProxyServerInfo {
    private String id;
    private String clientId;
    private String host;
    private int port;
    private String targetHost;
    private int targetPort;
}
