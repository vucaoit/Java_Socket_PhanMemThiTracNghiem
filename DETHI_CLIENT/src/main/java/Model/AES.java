package Model;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
//4
import java.util.Base64;

public class AES {
    private SecretKey key;
    public AES(){
        //mỗi lần khởi động client thì Key sẽ được sinh ra
        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256); // độ dài key 256bit
            SecretKey secretKey = keyGen.generateKey();
            this.key = secretKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    public SecretKey getKey() {
        return key;
    }
    public String getKetString(){
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }// trả về chuỗi encode từ object SecretKey
    public String encrypt(String strToEncrypt) {// truyền dữ liệu vào để mã hóa
        //vì key là của client trên khỏi cần tham số key trong function
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//loại mã hóa AES, chế độ ECB, chuỗi đệm mật mã PKCS5Padding
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));// trả về string data mã hóa
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }
    public String decrypt(String strToDecrypt, SecretKey keyaes) {// truyền vào data đã đc mã hóa và khóa aes
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");//loại mã hóa AES, chế độ ECB, chuỗi đệm mật mã PKCS5Padding
            cipher.init(Cipher.DECRYPT_MODE, keyaes);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));// trả về chuỗi đã được giải mã
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }
}
