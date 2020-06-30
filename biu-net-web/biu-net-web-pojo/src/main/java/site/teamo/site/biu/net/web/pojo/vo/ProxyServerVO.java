package site.teamo.site.biu.net.web.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProxyServerVO {

    /**
     * 代理服务id
     */
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
    private String clientId;

    /**
     * 映射的客户端名字
     */
    private String clientName;

    /**
     * 目标主机名
     */
    private String targetHost;

    /**
     * 目标端口
     */
    private Integer targetPort;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}
