package site.teamo.site.biu.net.web.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "proxy_server")
public class ProxyServer {
    /**
     * 代理服务id
     */
    @Id
    private String id;

    /**
     * 代理服务主机名
     */
    private String host;

    /**
     * 代理服务监听端口
     */
    private Integer port;

    /**
     * 映射的客户端id
     */
    @Column(name = "client_id")
    private String clientId;

    /**
     * 目标主机名
     */
    @Column(name = "target_host")
    private String targetHost;

    /**
     * 目标端口
     */
    @Column(name = "target_port")
    private Integer targetPort;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;
}