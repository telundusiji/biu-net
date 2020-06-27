package site.teamo.biu.net.common.message;

import io.netty.buffer.ByteBuf;
import site.teamo.biu.net.common.enums.BiuNetMessageHead;
import site.teamo.biu.net.common.exception.BadMessageException;
import site.teamo.biu.net.common.exception.ProtocolInconsistencyException;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/24
 */
public class PingResponse extends BiuNetMessage{
    public PingResponse() {
        super(BiuNetMessageHead.PING_RESPONSE);
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public void setProtocol(Protocol protocol) throws ProtocolInconsistencyException {

    }

    @Override
    public void read(ByteBuf byteBuf) throws BadMessageException {

    }

    @Override
    public void write(ByteBuf byteBuf) {

    }

}
