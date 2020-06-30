package site.teamo.site.biu.net.web.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/28
 */
@ApiModel(value = "创建代理映射对象",description = "创建代理映射对象")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProxyServerBO {

    /**
     * 代理服务主机名
     */
    @ApiModelProperty(value = "代理服务所在主机",name = "host",example = "localhost",required = false)
    private String host = "localhost";

    /**
     * 代理服务监听端口
     */
    @ApiModelProperty(value = "代理服务监听端口",name = "port",example = "80",required = true)
    @NotNull(message = "监听端口不能为空")
    private Integer port;

    /**
     * 映射的客户端id
     */
    @ApiModelProperty(value = "代理服务映射的客户端id",name = "clientId",example = "mypc",required = true)
    @NotBlank(message = "代理映射客户端不能为空")
    private String clientId;

    /**
     * 目标主机名
     */
    @ApiModelProperty(value = "目标服务的主机名/ip",name = "localhost",example = "127.0.0.1",required = false)
    private String targetHost = "localhost";

    /**
     * 目标端口
     */
    @ApiModelProperty(value = "目标服务端口号",name = "targetPort",example = "80",required = true)
    @NotNull(message = "目标端口号不能为空")
    private Integer targetPort;

}
