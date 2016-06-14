package com.mogujie.tt.utils;




import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.RC4Engine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Base64;

import java.io.UnsupportedEncodingException;

/**
 * 网络请求报文体加密
 * Created by gy on 2016/6/8.
 */
public class EncryUtil {

    //分两步加密，先rc4加密，再base64加密
    public static String encrypt(String value, String key){
        try{
            RC4Engine engine = new RC4Engine();
            CipherParameters parameters = new KeyParameter(key.getBytes("gbk"));
            engine.init(false, parameters);
            int len = value.getBytes("gbk").length;
            byte[] bytes = new byte[len];
            for (int i = 0; i < len; i++) {
                bytes[i] = engine.returnByte(value.getBytes("gbk")[i]);
            }
            String base64Encrypt = new String(Base64.encode(bytes));
            return base64Encrypt;
        }catch(Exception e){
            throw new RuntimeException("加密失败");
        }

    }

    //解密
    public static String decrypt(String value, String key) {
        try {
            byte[] base64Decrypt = Base64.decode(value.getBytes("gbk"));
//        byte[] base64Decrypt = Base64.decodeBase64(aaaa.getBytes("gbk"));
            String dd = drc4(key, base64Decrypt);
            return dd;
        } catch (Exception e) {
            throw new RuntimeException("解密失败");
        }
    }

    private static String drc4(String key, byte[] bytes) throws UnsupportedEncodingException {
        RC4Engine engine = new RC4Engine();
        CipherParameters parameters = new KeyParameter(key.getBytes("utf-8"));
        engine.init(false, parameters);
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = engine.returnByte(bytes[i]);
        }
        return new String(bytes, "utf-8");
    }
}
