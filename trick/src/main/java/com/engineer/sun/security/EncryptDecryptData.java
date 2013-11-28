package com.engineer.sun.security;

public class EncryptDecryptData {
 
    public static void main(String[] args) throws NoSuchAlgorithmException,  
            InvalidKeyException, NoSuchPaddingException,  
            InvalidKeySpecException, IllegalBlockSizeException,  
            BadPaddingException {  
        // 1.1 >>> 首先要创建一个密匙  
        // DES算法要求有一个可信任的随机数源  
        SecureRandom sr = new SecureRandom();  
        // 为我们选择的DES算法生成一个KeyGenerator对象  
        KeyGenerator kg = KeyGenerator.getInstance("DES");  
        kg.init(sr);  
        // 生成密匙  
        SecretKey key = kg.generateKey();  
        // 获取密匙数据  
        //byte rawKeyData[] = key.getEncoded();  
        byte rawKeyData[] = "sucre".getBytes();  
        System.out.println("密匙===>" + rawKeyData);  
 
        String str = "hi.baidu.com/beijingalana"; // 待加密数据  
        // 2.1 >>> 调用加密方法  
        byte[] encryptedData = encrypt(rawKeyData, str);  
        // 3.1 >>> 调用解密方法  
        decrypt(rawKeyData, encryptedData);  
    }