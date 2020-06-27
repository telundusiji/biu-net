package site.teamo.biu.net.web.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/24
 */
@ApiModel(value = "创建客户端用户对象",description = "创建客户端信息封装")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientBO {

    @ApiModelProperty(value = "客户端名称",name = "clientName",example = "pc-xxx",required = true)
    @NotBlank(message = "客户端名称不能为空")
    private String clientName;

    @ApiModelProperty(value = "客户端连接密钥",name = "password",example = "15a3303e-3f4d-4cac-9bc2-9757f093d759",required = true)
    @Pattern(regexp = "[0-9a-zA-Z\\-]{6,64}",message = "密码6-64位，由数字、字母和’-‘组成")
    private String password;
}
