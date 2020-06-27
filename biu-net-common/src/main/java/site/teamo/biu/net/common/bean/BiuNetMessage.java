//package site.teamo.biu.net.common.bean;
//
//import com.alibaba.fastjson.JSON;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import site.teamo.biu.net.common.constant.BiuNetConstant;
//
///**
// * @author 爱做梦的锤子
// * @create 2020/6/22
// */
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class BiuNetMessage {
//
//    private int head;
//
//    private char[] sessionId;
//
//    private byte[] content;
//
//    public static BiuNetMessage makeRegisterMessage(ClientPassport clientPassport){
//        return BiuNetMessage.builder()
//                .head(BiuNetConstant.login_head)
//                .sessionId(new char[36])
//                .content(JSON.toJSONBytes(clientPassport))
//                .build();
//    }
//}
