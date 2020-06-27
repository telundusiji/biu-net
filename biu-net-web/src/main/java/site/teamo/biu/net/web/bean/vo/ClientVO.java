package site.teamo.biu.net.web.bean.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientVO {

    private String id;

    private String name;

    private String password;

    private Date createTime;

    private Date updateTime;

}
