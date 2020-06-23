package site.teamo.biu.net.common.bean;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.teamo.biu.net.common.constant.BiuNetConstant;

import java.util.UUID;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BiuNetMessage {

    private int head;

    private char[] clientKey;

    private char[] sessionId;

    private byte[] content;

    public static BiuNetMessage makeRegisterMessage(char[] key){
        return BiuNetMessage.builder()
                .head(BiuNetConstant.register_head)
                .clientKey(key)
                .sessionId(new char[36])
                .content(new byte[0])
                .build();
    }
}
