package site.teamo.site.biu.net.web.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private Integer online;

    private Date createTime;

    private Date updateTime;

    private Date loginTime;

}
