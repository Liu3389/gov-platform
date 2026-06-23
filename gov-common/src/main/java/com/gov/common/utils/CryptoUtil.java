package com.gov.common.utils;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.symmetric.SM4;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 国密加密工具类
 * 提供 SM2（非对称加密）、SM4（对称加密）的便捷方法
 * 基于 BouncyCastle + Hutool 实现
 */
public class CryptoUtil {

    // ==================== SM4 对称加密 ====================

    /** 默认 SM4 密钥（生产环境必须通过配置注入） */
    private static final String DEFAULT_SM4_KEY = "0123456789ABCDEF0123456789ABCDEF";

    private static SM4 sm4;

    static {
        sm4 = SmUtil.sm4(hexToBytes(DEFAULT_SM4_KEY));
    }

    /**
     * SM4 加密（输出 Base64）
     *
     * @param plainText 明文
     * @return Base64 密文
     */
    public static String sm4Encrypt(String plainText) {
        if (plainText == null || plainText.isEmpty()) {
            return plainText;
        }
        byte[] encrypted = sm4.encrypt(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * SM4 解密（输入 Base64）
     *
     * @param cipherText Base64 密文
     * @return 明文
     */
    public static String sm4Decrypt(String cipherText) {
        if (cipherText == null || cipherText.isEmpty()) {
            return cipherText;
        }
        byte[] decoded = Base64.getDecoder().decode(cipherText);
        byte[] decrypted = sm4.decrypt(decoded);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    /**
     * 使用自定义密钥进行 SM4 加密
     */
    public static String sm4Encrypt(String plainText, String hexKey) {
        if (plainText == null || plainText.isEmpty()) {
            return plainText;
        }
        SM4 customSm4 = SmUtil.sm4(hexToBytes(hexKey));
        byte[] encrypted = customSm4.encrypt(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * 使用自定义密钥进行 SM4 解密
     */
    public static String sm4Decrypt(String cipherText, String hexKey) {
        if (cipherText == null || cipherText.isEmpty()) {
            return cipherText;
        }
        SM4 customSm4 = SmUtil.sm4(hexToBytes(hexKey));
        byte[] decoded = Base64.getDecoder().decode(cipherText);
        byte[] decrypted = customSm4.decrypt(decoded);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    // ==================== SM2 非对称加密 ====================

    /**
     * SM2 加密（使用公钥，输出 Base64）
     *
     * @param plainText 明文
     * @param publicKeyHex 十六进制公钥
     * @return Base64 密文
     */
    public static String sm2Encrypt(String plainText, String publicKeyHex) {
        if (plainText == null || plainText.isEmpty()) {
            return plainText;
        }
        SM2 sm2 = SmUtil.sm2(null, publicKeyHex);
        String encrypted = sm2.encryptBase64(plainText, KeyType.PublicKey);
        return encrypted;
    }

    /**
     * SM2 解密（使用私钥）
     *
     * @param cipherText Base64 密文
     * @param privateKeyHex 十六进制私钥
     * @return 明文
     */
    public static String sm2Decrypt(String cipherText, String privateKeyHex) {
        if (cipherText == null || cipherText.isEmpty()) {
            return cipherText;
        }
        SM2 sm2 = SmUtil.sm2(privateKeyHex, null);
        String decrypted = sm2.decryptStr(cipherText, KeyType.PrivateKey);
        return decrypted;
    }

    /**
     * SM2 签名
     *
     * @param data 待签名数据
     * @param privateKeyHex 十六进制私钥
     * @return Base64 签名
     */
    public static String sm2Sign(String data, String privateKeyHex) {
        SM2 sm2 = SmUtil.sm2(privateKeyHex, null);
        byte[] sign = sm2.sign(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(sign);
    }

    /**
     * SM2 验签
     *
     * @param data 原始数据
     * @param signBase64 Base64 签名
     * @param publicKeyHex 十六进制公钥
     * @return 验签结果
     */
    public static boolean sm2Verify(String data, String signBase64, String publicKeyHex) {
        SM2 sm2 = SmUtil.sm2(null, publicKeyHex);
        byte[] sign = Base64.getDecoder().decode(signBase64);
        return sm2.verify(data.getBytes(StandardCharsets.UTF_8), sign);
    }

    /**
     * 生成 SM2 密钥对
     *
     * @return [0]=私钥(Base64), [1]=公钥(Base64)
     */
    public static String[] generateSm2KeyPair() {
        SM2 sm2 = SmUtil.sm2();
        return new String[]{
            sm2.getPrivateKeyBase64(),
            sm2.getPublicKeyBase64()
        };
    }

    // ==================== 数据脱敏 ====================

    /**
     * 身份证号脱敏：320***********1234
     */
    public static String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() < 8) {
            return idCard;
        }
        return idCard.substring(0, 3) + "***********" + idCard.substring(idCard.length() - 4);
    }

    /**
     * 手机号脱敏：138****8000
     */
    public static String maskPhone(String phone) {
        if (phone == null || phone.length() < 7) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
    }

    /**
     * 姓名脱敏：张**
     */
    public static String maskName(String name) {
        if (name == null || name.length() <= 1) {
            return name;
        }
        return name.charAt(0) + name.substring(1).replaceAll(".", "*");
    }

    /**
     * 邮箱脱敏：a***@example.com
     */
    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        int atIndex = email.indexOf("@");
        String prefix = email.substring(0, atIndex);
        String suffix = email.substring(atIndex);
        if (prefix.length() <= 1) {
            return "*" + suffix;
        }
        return prefix.charAt(0) + "***" + suffix;
    }

    // ==================== 内部工具方法 ====================

    /**
     * 十六进制字符串转字节数组
     */
    private static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}
