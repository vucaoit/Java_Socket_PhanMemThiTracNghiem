package Model;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
//4
import java.util.Base64;

public class AES {
    public AES(){
    }
    public SecretKey StringtoSecretKey(String key){//chuyển chuỗi khóa về dạng object SecretKey(khóa bí mật của AES)
        byte[] decodedKey = Base64.getDecoder().decode(key);
// rebuild key using SecretKeySpec
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        return originalKey;// trả về đối tượng là SecretKey
    }
    public String encrypt(String strToEncrypt,SecretKey key1) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key1);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            //System.out.println(e.toString());
        }
        return null;
    }
    public String decrypt(String strToDecrypt, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            //System.out.println(e.toString());
        }
        return null;
    }
}
