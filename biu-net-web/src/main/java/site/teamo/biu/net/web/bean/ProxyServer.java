package site.teamo.biu.net.web.bean;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

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

    /**
     * 获取代理服务id
     *
     * @return id - 代理服务id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置代理服务id
     *
     * @param id 代理服务id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取代理服务主机名
     *
     * @return host - 代理服务主机名
     */
    public String getHost() {
        return host;
    }

    /**
     * 设置代理服务主机名
     *
     * @param host 代理服务主机名
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * 获取代理服务监听端口
     *
     * @return port - 代理服务监听端口
     */
    public Integer getPort() {
        return port;
    }

    /**
     * 设置代理服务监听端口
     *
     * @param port 代理服务监听端口
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * 获取映射的客户端id
     *
     * @return client_id - 映射的客户端id
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * 设置映射的客户端id
     *
     * @param clientId 映射的客户端id
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * 获取目标主机名
     *
     * @return target_host - 目标主机名
     */
    public String getTargetHost() {
        return targetHost;
    }

    /**
     * 设置目标主机名
     *
     * @param targetHost 目标主机名
     */
    public void setTargetHost(String targetHost) {
        this.targetHost = targetHost;
    }

    /**
     * 获取目标端口
     *
     * @return target_port - 目标端口
     */
    public Integer getTargetPort() {
        return targetPort;
    }

    /**
     * 设置目标端口
     *
     * @param targetPort 目标端口
     */
    public void setTargetPort(Integer targetPort) {
        this.targetPort = targetPort;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改时间
     *
     * @return update_time - 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}