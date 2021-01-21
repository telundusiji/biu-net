package site.teamo.biu.net.common.info;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import site.teamo.biu.net.common.annoation.Sensitive;

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
@Table(name = "client")
public class ClientInfo {
    @Id
    private String id;
    private String name;
    @Sensitive
    private String password;
    @Sensitive
    private String privateKey;
    private String publicKey;
}
