package site.teamo.biu.net.common.message;

import io.netty.buffer.ByteBuf;
import site.teamo.biu.net.common.enums.BiuNetMessageHead;
import site.teamo.biu.net.common.exception.BadMessageException;
import site.teamo.biu.net.common.exception.ProtocolInconsistencyException;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/24
 */
public class PingRequest extends BiuNetMessage{
    public PingRequest() {
        super(BiuNetMessageHead.PING_REQUEST);
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public void setProtocol(Protocol protocol) throws ProtocolInconsistencyException {

    }

    @Override
    public void readFromByteBuf(ByteBuf byteBuf) throws BadMessageException {

    }

    @Override
    public void writeToByteBuf(ByteBuf byteBuf) {

    }

}
