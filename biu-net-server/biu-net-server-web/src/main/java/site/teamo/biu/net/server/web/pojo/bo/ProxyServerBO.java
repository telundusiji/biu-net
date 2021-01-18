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
 * @create 2020/6/28
 */
@ApiModel(value = "代理服务端",description = "代理服务端")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProxyServerBO {
    /**
     * 代理服务监听端口
     */
    @ApiModelProperty(value = "代理服务监听端口",name = "port",example = "80",required = true)
    @NotNull(message = "监听端口不能为空")
    private Integer port;


}
