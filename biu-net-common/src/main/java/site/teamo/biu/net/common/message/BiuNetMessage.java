package site.teamo.biu.net.common.message;

import io.netty.buffer.ByteBuf;
import lombok.Builder;
import lombok.Data;
import site.teamo.biu.net.common.enums.BiuNetMessageHead;
import site.teamo.biu.net.common.exception.BadMessageException;
import site.teamo.biu.net.common.exception.ProtocolInconsistencyException;

import java.nio.charset.Charset;

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
public abstract class BiuNetMessage {
    private int head;

    private byte[] content;

    public BiuNetMessage(BiuNetMessageHead head) {
        this.head = head.type;
    }

    public int getHead() {
        return head;
    }

    public BiuNetMessageHead getHeadEnum(){
        return BiuNetMessageHead.valueOfByType(head);
    }

    public abstract String getProtocol();

    public abstract void setProtocol(BiuNetMessage.Protocol protocol) throws ProtocolInconsistencyException;

    public abstract void readFromByteBuf(ByteBuf byteBuf) throws BadMessageException;

    public abstract void writeToByteBuf(ByteBuf byteBuf);

    public byte[] getContent() {
        return content == null ? new byte[0] : content;
    }

    public BiuNetMessage setContent(byte[] content) {
        this.content = content;
        return this;
    }

    @Override
    public String toString(){
        return toString(Charset.defaultCharset());
    }

    public String toString(Charset charset){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("head:");
        stringBuffer.append(head);
        stringBuffer.append("\nprotocol:");
        stringBuffer.append(getProtocol());
        stringBuffer.append("\ncontent:");
        stringBuffer.append(new String(content,Charset.defaultCharset()));
        return stringBuffer.toString();
    }

    public static interface Protocol {
        void check() throws ProtocolInconsistencyException;
    }
}
