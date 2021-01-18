package site.teamo.biu.net.common.info;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@Table(name = "proxy")
public class ProxyInfo {
    @Id
    private String id;
    private String clientId;
    private String proxyServerId;
    private String targetHost;
    private Integer targetPort;
    private Integer enable;
}
