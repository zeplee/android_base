package com.aac.base.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptUtil {

    /**
     * @param source 被加密的字符串
     * @param key    加密key
     * @return 加密过后的字符串
     */
    public static String encrypt4AES(String source, String key) {
        try {
            byte[] iv = key.getBytes("utf-8");
            IvParameterSpec zeroIv = new IvParameterSpec(iv);
            SecretKeySpec key1 = new SecretKeySpec(iv, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key1, zeroIv);
            byte[] encryptedData = cipher.doFinal(source.getBytes("utf-8"));
            String encryptResultStr = parseByte2HexStr(encryptedData);
            return encryptResultStr; // 加密
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }


}
