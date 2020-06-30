package site.teamo.biu.net.common.message;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import site.teamo.biu.net.common.enums.BiuNetMessageHead;
import site.teamo.biu.net.common.exception.BadMessageException;
import site.teamo.biu.net.common.exception.ProtocolInconsistencyException;
import site.teamo.biu.net.common.util.BiuNetMessageUtil;

import java.nio.charset.Charset;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/24
 */
public class ForwardRequest extends BiuNetMessage {

    private char[] targetHost = new char[36];
    private int targetPort = 0;
    private char[] sessionId = new char[36];

    public ForwardRequest() {
        super(BiuNetMessageHead.FORWARD_REQUEST);
    }

    @Override
    public String getProtocol() {
        return JSON.toJSONString(SimpleProtocol.builder()
                .targetHost(new String(targetHost).trim())
                .targetPort(targetPort)
                .sessionId(new String(sessionId).trim())
                .build());
    }

    @Override
    public void setProtocol(Protocol protocol) throws ProtocolInconsistencyException {
        if (protocol instanceof SimpleProtocol) {
            SimpleProtocol simpleProtocol = (SimpleProtocol) protocol;
            simpleProtocol.check();
            BiuNetMessageUtil.marge(targetHost, simpleProtocol.getTargetHost());
            targetPort = simpleProtocol.getTargetPort();
            BiuNetMessageUtil.marge(sessionId, simpleProtocol.getSessionId());
            return;
        }
        throw new ProtocolInconsistencyException("ForwardRequest.Protocol无法处理的类型:" + protocol);
    }

    @Override
    public void readFromByteBuf(ByteBuf byteBuf) throws BadMessageException {
        if(byteBuf.readableBytes()<76){
            throw new BadMessageException("与协议长度不一致期待："+76+"实际："+byteBuf.readableBytes());
        }
        CharSequence targetHost = byteBuf.readCharSequence(36, Charset.defaultCharset());
        targetPort = byteBuf.readInt();
        CharSequence sessionId = byteBuf.readCharSequence(36, Charset.defaultCharset());
        BiuNetMessageUtil.marge(this.targetHost,targetHost);
        BiuNetMessageUtil.marge(this.sessionId,sessionId);
    }

    @Override
    public void writeToByteBuf(ByteBuf byteBuf) {
        byteBuf.writeCharSequence(new String(targetHost),Charset.defaultCharset());
        byteBuf.writeInt(targetPort);
        byteBuf.writeCharSequence(new String(sessionId),Charset.defaultCharset());
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SimpleProtocol implements Protocol {

        private String targetHost;
        private Integer targetPort;
        private String sessionId;

        @Override
        public void check() throws ProtocolInconsistencyException {
            if (StringUtils.isBlank(targetHost)) {
                throw new ProtocolInconsistencyException("目标主机/IP为空");
            }
            if (targetPort == null || targetPort == 0) {
                throw new ProtocolInconsistencyException("目标端口为空");
            }
            if (StringUtils.isBlank(sessionId)) {
                throw new ProtocolInconsistencyException("会话id为空");
            }
            targetHost = targetHost.trim();
            sessionId = sessionId.trim();
            if (targetHost.length() > 36 || sessionId.length() > 36) {
                throw new ProtocolInconsistencyException("目标主机/IP或者会话ID长度超出36位");
            }
        }
    }
}
