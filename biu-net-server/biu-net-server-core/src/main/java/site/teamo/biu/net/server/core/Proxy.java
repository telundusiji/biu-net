package site.teamo.biu.net.server.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import site.teamo.biu.net.common.info.ProxyInfo;
import site.teamo.biu.net.common.enums.YesNo;
import site.teamo.biu.net.common.util.BiuNetBeanUtil;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/13
 */
@Data
@Accessors(chain = true)
public class Proxy {
    private Info info;
    private MockClient mockClient;
    private ProxyServer proxyServer;


    public void start() {
        info.enable = YesNo.YES.type;
        proxyServer.start();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Accessors(chain = true)
    @Table(name = "proxy")
    public static class Info {
        @Id
        private String id;
        private String clientId;
        private String proxyServerId;
        private String targetHost;
        private Integer targetPort;
        private Integer enable;

        public static Info create(ProxyInfo info) {
            Info result = new Info();
            BiuNetBeanUtil.copyBean(info, result);
            return result;
        }
    }
}
