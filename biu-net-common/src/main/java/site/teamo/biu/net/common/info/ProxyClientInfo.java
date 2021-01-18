package site.teamo.biu.net.common.info;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class ProxyClientInfo {
        private String id;
        private String targetHost;
        private Integer targetPort;
        private Integer proxyServerPort;
        private String proxyCtxId;
}
