package com.yang.base.util;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/***
 * @desc 加解密工具
 * @author yangguoq
 */

public class BaseCryptoHelper {

    private BaseCryptoHelper(){}

    private static class BaseCryptoHolder{
        private static final BaseCryptoHelper instance=new BaseCryptoHelper();
    }

    public static BaseCryptoHelper getInstance(){
        return BaseCryptoHelper.BaseCryptoHolder.instance;
    }

    /**
     * 偏移量AES 为16bytes.
     */
    private String iv="9876543210abcdef";

    /**
     * 密钥
     * //AES固定格式为128/192/256 bits.即：16/24/32bytes。
     */
    public  String password;


    /**
     * 初始化
     * @param password 密钥
     * @param iv 偏移；量
     */
    public void init(String password,String iv){
        this.password=password;
        this.iv=iv;
    }

    /**
     * 初始化
     * @param password 密钥
     */
    public void init(String password){
        this.password=password;
    }


    /**
     * 加密
     * @param  clearString
     * @return base64字符串
     */
    public String encrypt(String clearString) {
        try {
            return encrypt(clearString.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 加密
     * @param  clearByte
     * @return base64字符串
     */
    public synchronized String encrypt(byte[] clearByte) {
        try {
            if (password==null){
                throw new Exception( "AES password not init" );
            }else if (!(password.length()==16||password.length()==24||password.length()==32)){
                throw new Exception( "AES bad password" );
            }
            IvParameterSpec zeroIv = new IvParameterSpec(iv.getBytes());
            //两个参数，第一个为私钥字节数组， 第二个为加密方式 AES或者DES
            SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
            //实例化加密类，参数为加密方式，要写全
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); //PKCS5Padding比PKCS7Padding效率高，PKCS7Padding可支持IOS加解密
            //初始化，此方法可以采用三种方式，按加密算法要求来添加。（1）无第三个参数（2）第三个参数为SecureRandom random = new SecureRandom();中random对象，随机数。(AES不可采用这种方法)（3）采用此代码中的IVParameterSpec
            cipher.init(Cipher.ENCRYPT_MODE, key,zeroIv);
            //加密操作,返回加密后的字节数组，然后需要编码。主要编解码方式有Base64, HEX, UUE,7bit等等。此处看服务器需要什么编码方式
            byte[] encryptedData = cipher.doFinal(clearByte);
            return base64Encode(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 解密
     * @param encrypted
     * @return
     */
    public synchronized   String decrypt(String encrypted) {
        try {
            byte[] byteMi = base64Decode(encrypted);
            IvParameterSpec zeroIv = new IvParameterSpec(iv.getBytes());
            SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            //与加密时不同MODE:Cipher.DECRYPT_MODE
            cipher.init(Cipher.DECRYPT_MODE, key,zeroIv);
            byte[] decryptedData = cipher.doFinal(byteMi);
            return new String(decryptedData,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 将 Base64解密
     */
    public byte[] base64Decode(String data) {
        return Base64.decode(data, Base64.NO_WRAP);
    }

    /**
     * 将 Base64加密
     */
    public  String base64Encode(byte[] data) {
        return Base64.encodeToString(data, Base64.NO_WRAP);
    }

}
