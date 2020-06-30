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
public class LoginRequest extends BiuNetMessage {

    private char[] name = new char[36];
    private char[] password = new char[36];

    public LoginRequest() {
        super(BiuNetMessageHead.LOGIN_REQUEST);
    }

    @Override
    public String getProtocol() {
        return JSON.toJSONString(SimpleProtocol.builder()
                .name(new String(name).trim())
                .password(new String(password).trim())
                .build());
    }

    @Override
    public void setProtocol(BiuNetMessage.Protocol protocol) throws ProtocolInconsistencyException {
        if (protocol instanceof LoginRequest.SimpleProtocol) {
            SimpleProtocol simpleProtocol = (SimpleProtocol) protocol;
            simpleProtocol.check();
            BiuNetMessageUtil.marge(name, simpleProtocol.getName());
            BiuNetMessageUtil.marge(password, simpleProtocol.getPassword());
            return;
        }
        throw new ProtocolInconsistencyException("LoginRequest.Protocol无法处理的类型:" + protocol);

    }

    @Override
    public void readFromByteBuf(ByteBuf byteBuf) throws BadMessageException {
        if(byteBuf.readableBytes()<72){
            throw new BadMessageException("与协议长度不一致期待："+72+"实际："+byteBuf.readableBytes());
        }
        CharSequence name = byteBuf.readCharSequence(36, Charset.defaultCharset());
        CharSequence password = byteBuf.readCharSequence(36, Charset.defaultCharset());
        BiuNetMessageUtil.marge(this.name,name);
        BiuNetMessageUtil.marge(this.password,password);

    }

    @Override
    public void writeToByteBuf(ByteBuf byteBuf) {
        byteBuf.writeCharSequence(new String(name),Charset.defaultCharset());
        byteBuf.writeCharSequence(new String(password),Charset.defaultCharset());
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SimpleProtocol implements BiuNetMessage.Protocol {
        private String name;
        private String password;

        @Override
        public void check() throws ProtocolInconsistencyException {
            if (StringUtils.isBlank(name) || StringUtils.isBlank(password)) {
                throw new ProtocolInconsistencyException("名称或密码为空");
            }
            name = name.trim();
            password = password.trim();
            if (name.length() > 36 || password.length() > 36) {
                throw new ProtocolInconsistencyException("名称或密码长度超出36位限制");
            }

        }
    }
}
