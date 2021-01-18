package site.teamo.biu.net.server.web.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/13
 */
@ApiModel(value = "创建代理映射对象", description = "创建代理映射对象")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProxyBO {

    /**
     * 映射的客户端id
     */
    @ApiModelProperty(value = "代理服务端id", name = "proxyServerId", example = "xxxx", required = true)
    @NotBlank(message = "Proxy server id cannot be blank")
    private String proxyServerId;

    /**
     * 映射的客户端id
     */
    @ApiModelProperty(value = "代理服务映射的客户端id", name = "clientId", example = "xxxx", required = true)
    @NotBlank(message = "Client id cannot be blank")
    private String clientId;

    /**
     * 目标主机名
     */
    @ApiModelProperty(value = "目标服务的主机名/ip", name = "localhost", example = "127.0.0.1", required = false)
    private String targetHost = "localhost";

    /**
     * 目标端口
     */
    @ApiModelProperty(value = "目标服务端口号", name = "targetPort", example = "80", required = true)
    @NotNull(message = "Target port cannot be empty")
    private Integer targetPort;

}
