package site.teamo.biu.net.server.web.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 爱做梦的锤子
 * @create 2021/3/4 15:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminLoginBO {
    private String username;
    private String password;
}
