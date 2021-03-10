package site.teamo.biu.net.common.util;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.nio.charset.Charset;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/21
 */
@Slf4j
public class RSAUtil {

    /**
     * 随机生成密钥对
     *
     * @throws NoSuchAlgorithmException
     */
    public static RSAKey generateRSAKey() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom(String.valueOf(System.nanoTime()).getBytes()));
        // 生成密钥对
        KeyPair keyPair = keyPairGen.generateKeyPair();
        return RSAKey.builder()
                .privateKey(new String(Base64.getEncoder().encode(keyPair.getPrivate().getEncoded())))
                .publicKey(new String(Base64.getEncoder().encode(keyPair.getPublic().getEncoded())))
                .build();
    }


    /**
     * 加密
     *
     * @param content   待加密内容
     * @param publicKey 公钥
     * @param charset   字符串字符集
     * @return 加密结果
     * @throws Exception
     */
    public static String encrypt(String content, String publicKey, Charset charset) throws Exception {
        byte[] contentBytes = content.getBytes(charset);
        return encrypt(contentBytes, publicKey);
    }

    public static String encrypt(byte[] content, String publicKeyStr) throws Exception {
        //base64编码的公钥
        byte[] publicKey = Base64.getDecoder().decode(publicKeyStr);
        RSAPublicKey rsaPublicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKey));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
        String outStr = Base64.getEncoder().encodeToString(cipher.doFinal(content));
        return outStr;
    }


    /**
     * 解密
     *
     * @param content    待解密内容
     * @param privateKey 私钥
     * @param charset    字符集
     * @return 解密结果
     * @throws Exception
     */
    public static String decrypt(String content, String privateKey, Charset charset) throws Exception {
        /**
         * 将待解密字符串转换为byte[]
         */
        byte[] contentByte = Base64.getDecoder().decode(content.getBytes(charset));
        return decrypt(contentByte, privateKey);
    }

    /**
     * 解密
     *
     * @param content       待解密内容
     * @param privateKeyStr 私钥
     * @return 解密结果
     * @throws Exception
     */
    public static String decrypt(byte[] content, String privateKeyStr) throws Exception {
        //base64编码的私钥
        byte[] privateKey = Base64.getDecoder().decode(privateKeyStr);
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKey));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
        String result = new String(cipher.doFinal(content));
        return result;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RSAKey {
        private String privateKey;
        private String publicKey;
    }

    public static void main(String[] args) throws Exception {
        RSAKey rsaKey = RSAUtil.generateRSAKey();
        System.out.println(rsaKey.publicKey);
        System.out.println();
        System.out.println(rsaKey.privateKey);
    }
}






