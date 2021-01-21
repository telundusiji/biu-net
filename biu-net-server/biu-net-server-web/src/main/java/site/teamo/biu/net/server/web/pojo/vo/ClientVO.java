package site.teamo.biu.net.server.web.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/24
 */
@Data
@Builder
@Accessors(chain = true)
public class ClientVO {

    private String id;

    private String name;

    private String publicKey;

}
