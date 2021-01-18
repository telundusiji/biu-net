package site.teamo.biu.net.common.message;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/24
 */

/**
 * 抽象协议消息
 * 包含三部分：
 * 协议头——用于确定消息类别
 * 协议体——根据不同消息类型定义
 * 消息体——具体转发的数据
 */
@Data
@Accessors(chain = true)
public class BiuNetMessage<T> {
    private MessageType type;
    private T content;
}
