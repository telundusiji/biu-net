package site.teamo.biu.net.common.message;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import site.teamo.biu.net.common.enums.BiuNetMessageHead;
import site.teamo.biu.net.common.enums.YesNo;
import site.teamo.biu.net.common.exception.BadMessageException;
import site.teamo.biu.net.common.exception.ProtocolInconsistencyException;
import site.teamo.biu.net.common.util.BiuNetMessageUtil;

import java.nio.charset.Charset;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/24
 */
public class LoginResponse extends BiuNetMessage{
    private char[] name = new char[36];
    private int isSuccess = YesNo.YES.type;

    public LoginResponse() {
        super(BiuNetMessageHead.LOGIN_RESPONSE);
    }

    @Override
    public String getProtocol() {
        return JSON.toJSONString(
                SimpleProtocol.builder()
                        .name(new String(name).trim())
                        .isSuccess(isSuccess)
                        .build()
        );
    }

    @Override
    public void setProtocol(Protocol protocol) throws ProtocolInconsistencyException {
        if(protocol instanceof SimpleProtocol){
            SimpleProtocol simpleProtocol = (SimpleProtocol)protocol;
            simpleProtocol.check();
            char[] tempName = simpleProtocol.name.toCharArray();
            for (int i = 0; i < tempName.length; i++) {
                name[i] = tempName[i];
            }
            isSuccess = simpleProtocol.isSuccess;
            return;
        }
        throw new ProtocolInconsistencyException("LoginResponse.Protocol无法处理的类型:" + protocol);
    }

    @Override
    public void readFromByteBuf(ByteBuf byteBuf) throws BadMessageException {
        if(byteBuf.readableBytes()<(36+4)){
            throw new BadMessageException("与协议长度不一致期待："+72+"实际："+byteBuf.readableBytes());
        }
        CharSequence name = byteBuf.readCharSequence(36, Charset.defaultCharset());
        int isSuccess = byteBuf.readInt();
        BiuNetMessageUtil.marge(this.name,name);
        this.isSuccess = isSuccess;

    }

    @Override
    public void writeToByteBuf(ByteBuf byteBuf) {
        byteBuf.writeCharSequence(new String(name),Charset.defaultCharset());
        byteBuf.writeInt(isSuccess);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SimpleProtocol implements BiuNetMessage.Protocol{

        private String name;
        private Integer isSuccess;

        @Override
        public void check() throws ProtocolInconsistencyException {
            if(StringUtils.isBlank(name)||isSuccess==null){
                throw new ProtocolInconsistencyException("名称和结果信息为空");
            }
            name = name.trim();
            if(name.length()>36){
                throw new ProtocolInconsistencyException("名字长度超出36位");
            }
        }

        public static SimpleProtocol parse(String protocol){
            return JSON.parseObject(protocol,SimpleProtocol.class);
        }
    }
}
