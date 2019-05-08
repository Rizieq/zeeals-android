//package com.example.user.zeeals.security;
//
//import android.os.Build;
//import android.support.annotation.RequiresApi;
//import android.util.Base64;
//import android.util.Log;
//
//import java.security.InvalidAlgorithmParameterException;
//import java.security.InvalidKeyException;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.security.SecureRandom;
//import java.security.spec.KeySpec;
//import java.util.ArrayList;
//
//import javax.crypto.BadPaddingException;
//import javax.crypto.Cipher;
//import javax.crypto.IllegalBlockSizeException;
//import javax.crypto.NoSuchPaddingException;
//import javax.crypto.SecretKey;
//import javax.crypto.SecretKeyFactory;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.PBEKeySpec;
//import javax.crypto.spec.SecretKeySpec;
//
//import static com.facebook.AccessTokenManager.TAG;
//
//public class CryptoHelper{
//    protected static int _nKeySize=256;
//    protected static int[] valid_key_sizes = new int[]{128,192,256};
//
//    public CryptoHelper() {
//    }
//
//    public static String enc(String text){
//        String key = "nonce_value";
//        SecureRandom random = new SecureRandom();
//        byte salt[] = random.generateSeed(8);
//
//        String salted="";
//        String dx="";
//
//        int key_length = _nKeySize/8;
//        int block_length = 16;
//        int salted_length = key_length + block_length;
//
//        while(salted.length()<salted_length){
//            dx =md5(dx+key+salt.toString());
//            salted+=dx;
//        }
//        key = salted.substring(0,key_length);
//        String iv = salted.substring(key_length,(key_length+block_length));
//        return Base64.encodeToString(("Salted__"+salt.toString()+openssl_encrypt(text,key,iv)).getBytes(),Base64.DEFAULT);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public static String encrypt(String strToEncrypt)
//    {
//        String secret = "nonce_value";
//        SecureRandom random = new SecureRandom();
//        byte bytes[] = new byte[8];
//        random.nextBytes(bytes);
////        byte salt[] = random.ge;
//        Log.d(TAG, "encrypt: "+salt);
//        try
//        {
//            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
//            IvParameterSpec ivspec = new IvParameterSpec(iv);
//
//            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
//            KeySpec spec = new PBEKeySpec(secret.toCharArray(), salt, 65536, 256);
//            SecretKey tmp = factory.generateSecret(spec);
//            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
//
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
//            return java.util.Base64.getEncoder().encodeToString(("Salted__"+salt.toString()+cipher.doFinal(strToEncrypt.getBytes("UTF-8")).toString()).getBytes());
//        }
//        catch (Exception e)
//        {
//            System.out.println("Error while encrypting: " + e.toString());
//        }
//        return null;
//    }
//
//    public static String dec(String text){
//        String key = "nonce_value";
//        int key_length = _nKeySize/8;
//        int block_length = 16;
//        String data = Base64.decode(text,Base64.DEFAULT).toString();
//        String salt = data.substring(8,9);
//        String encypted = data.substring(9);
//
//        int rounds = 3;
//
//        String data00 = key+salt;
//        ArrayList<String> md5_hash = new ArrayList<>();
//        md5_hash.add(md5(data00));
//        String result = md5_hash.get(0);
//
//        for(int i = 1; i<rounds;i++){
//            md5_hash.add(md5(md5_hash.get(i-1)+data00));
//            result+=md5_hash.get(i);
//        }
//
//        key = result.substring(0,key_length);
//        String iv = result.substring(key_length,key_length+block_length);
//
//        return openssl_dec(encypted.getBytes(),key,iv);
//    }
//
//
//    public static final String md5(final String s) {
//        final String MD5 = "MD5";
//        try {
//            // Create MD5 Hash
//            MessageDigest digest = java.security.MessageDigest
//                    .getInstance(MD5);
//            digest.update(s.getBytes());
//            byte messageDigest[] = digest.digest();
//
//            // Create Hex String
//            StringBuilder hexString = new StringBuilder();
//            for (byte aMessageDigest : messageDigest) {
//                String h = Integer.toHexString(0xFF & aMessageDigest);
//                while (h.length() < 2)
//                    h = "0" + h;
//                hexString.append(h);
//            }
//            return hexString.toString();
//
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//
//    private static String openssl_encrypt(String data, String strKey, String strIv) {
//        try{
//            Cipher ciper = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            SecretKeySpec key = new SecretKeySpec(strKey.getBytes(), "AES");
//            IvParameterSpec iv = new IvParameterSpec(strIv.getBytes(), 0, ciper.getBlockSize());
//
//            // Encrypt
//            ciper.init(Cipher.ENCRYPT_MODE, key, iv);
//            byte[] encryptedCiperBytes = ciper.doFinal(data.getBytes());
//
//            String s = new String(encryptedCiperBytes);
//            return s;
//        }catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (InvalidAlgorithmParameterException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        }
//        return "";
//
//
//    }
//
//    private static String openssl_dec(byte[] text,String encKey,String iv){
//            String decrypted = null;
//            try
//            {
//                Cipher cipher = Cipher.getInstance ( "AES");
//                SecretKeySpec key = new SecretKeySpec ( encKey.getBytes ( "UTF-8" ), "AES" );
//                IvParameterSpec ivv = new IvParameterSpec(iv.getBytes());
//                cipher.init ( Cipher.DECRYPT_MODE, key,ivv);
//                decrypted = new String ( cipher.doFinal(text), "UTF-8" );
//            }
//            catch ( Exception e )
//            {
//                e.printStackTrace();
//            }
//
//            return decrypted;
//        }
//
//
//
//    public static AesCipher encrypt(String secretKey, String plainText) {
//        String initVector = null;
//        try {
//            // Check secret length
//            if (!isKeyLengthValid(secretKey)) {
//                throw new Exception("Secret key's length must be 128, 192 or 256 bits");
//            }
//
//            // Get random initialization vector
//            SecureRandom secureRandom = new SecureRandom();
//            byte[] initVectorBytes = new byte[INIT_VECTOR_LENGTH / 2];
//            secureRandom.nextBytes(initVectorBytes);
//            initVector = bytesToHex(initVectorBytes);
//            initVectorBytes = initVector.getBytes("UTF-8");
//
//            IvParameterSpec ivParameterSpec = new IvParameterSpec(initVectorBytes);
//            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");
//
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
//
//            // Encrypt input text
//            byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
//
//            ByteBuffer byteBuffer = ByteBuffer.allocate(initVectorBytes.length + encrypted.length);
//            byteBuffer.put(initVectorBytes);
//            byteBuffer.put(encrypted);
//
//            // Result is base64-encoded string: initVector + encrypted result
//            String result = Base64.encodeToString(byteBuffer.array(), Base64.DEFAULT);
//
//            // Return successful encoded object
//            return new AesCipher(initVector, result, null);
//        } catch (Throwable t) {
//            t.printStackTrace();
//            // Operation failed
//            return new AesCipher(initVector, null, t.getMessage());
//        }
//    }
//
//    /**
//     * Decrypt encoded text by AES-128-CBC algorithm
//     *
//     * @param secretKey  16/24/32 -characters secret password
//     * @param cipherText Encrypted text
//     * @return Self object instance with data or error message
//     */
//    public static String decr(String secretKey, String cipherText) {
//        try {
//            // Check secret length
//            if (!isKeyLengthValid(secretKey)) {
//                throw new Exception("Secret key's length must be 128, 192 or 256 bits");
//            }
//
//            // Get raw encoded data
//            byte[] encrypted = Base64.decode(cipherText, Base64.DEFAULT);
//
//            // Slice initialization vector
//            IvParameterSpec ivParameterSpec = new IvParameterSpec(encrypted, 0, 16);
//            // Set secret password
//            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");
//
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
//
//            // Trying to get decrypted text
//            String result = new String(cipher.doFinal(encrypted, 16, encrypted.length - 16));
//
//            // Return successful decoded object
//            return bytesToHex(ivParameterSpec.getIV()), result, null);
//        } catch (Throwable t) {
//            t.printStackTrace();
//            // Operation failed
//            return new AesCipher(null, null, t.getMessage());
//        }
//    }
//
//    /**
//     * Check that secret password length is valid
//     *
//     * @param key 16/24/32 -characters secret password
//     * @return TRUE if valid, FALSE otherwise
//     */
//    public static boolean isKeyLengthValid(String key) {
//        return key.length() == 16 || key.length() == 24 || key.length() == 32;
//    }
//
//    /**
//     * Convert Bytes to HEX
//     *
//     * @param bytes Bytes array
//     * @return String with bytes values
//     */
//    public static String bytesToHex(byte[] bytes) {
//        char[] hexChars = new char[bytes.length * 2];
//        for (int j = 0; j < bytes.length; j++) {
//            int v = bytes[j] & 0xFF;
//            hexChars[j * 2] = hexArray[v >>> 4];
//            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
//        }
//        return new String(hexChars);
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//}