package com.dam1rka.SpringApp.service;

import com.dam1rka.SpringApp.dto.CardInfoDto;
import com.dam1rka.SpringApp.dto.GetCardDto;
import com.dam1rka.SpringApp.dto.OrderDto;
import com.google.gson.Gson;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.asn1.pkcs.RSAPublicKey;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.jcajce.provider.asymmetric.util.KeyUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class SecurityService {

    @Value("${jwt.public.key}")
    private String PUBLIC_KEY;
    @Value("${jwt.private.key}")
    private String PRIVATE_KEY;

    @Value("${wooppay.private.key}")
    private String WOOPPAY_PRIVATE_KEY;

    private Gson gson = new Gson();

    private String decode(String token) {
        PrivateKey privateKey = getPrivateKey(PRIVATE_KEY);
        byte[] decryptedBytes = decrypt(Base64.getDecoder().decode(token), privateKey);
        return new String(decryptedBytes);
    }

    private String decodeWooppay(String token) {
        PrivateKey privateKey = getPrivateKey(WOOPPAY_PRIVATE_KEY);
        byte[] decryptedBytes = decrypt(Base64.getDecoder().decode(token), privateKey);
        return new String(decryptedBytes);
    }

    private byte[] encode(String data) {
        PublicKey publicKey = getPublicKey(PUBLIC_KEY);
        byte[] encryptedBytes = encrypt(data.getBytes(), publicKey);
        return encryptedBytes;
    }

    private PublicKey getPublicKey(String publicKey) {
        try {
            RSAPublicKey rsaPublicKey = RSAPublicKey.getInstance(Base64.getDecoder().decode(publicKey));
            byte[] publicKeyInfoBytes = KeyUtil.getEncodedSubjectPublicKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, DERNull.INSTANCE), rsaPublicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyInfoBytes);
            PublicKey key = KeyFactory.getInstance("RSA").generatePublic(keySpec);
            return key;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    private static PrivateKey readPkcs8PrivateKey(byte[] pkcs8Bytes) throws GeneralSecurityException {
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA", "SunRsaSign");
//        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8Bytes);
//        try {
//            return keyFactory.generatePrivate(keySpec);
//        } catch (InvalidKeySpecException e) {
//            throw new IllegalArgumentException("Unexpected key format!", e);
//        }
//    }
//
//    private static byte[] join(byte[] byteArray1, byte[] byteArray2){
//        byte[] bytes = new byte[byteArray1.length + byteArray2.length];
//        System.arraycopy(byteArray1, 0, bytes, 0, byteArray1.length);
//        System.arraycopy(byteArray2, 0, bytes, byteArray1.length, byteArray2.length);
//        return bytes;
//    }
//
//    private static PrivateKey readPkcs1PrivateKey(byte[] pkcs1Bytes) throws GeneralSecurityException {
//        // We can't use Java internal APIs to parse ASN.1 structures, so we build a PKCS#8 key Java can understand
//        int pkcs1Length = pkcs1Bytes.length;
//        int totalLength = pkcs1Length + 22;
//        byte[] pkcs8Header = new byte[] {
//                0x30, (byte) 0x82, (byte) ((totalLength >> 8) & 0xff), (byte) (totalLength & 0xff), // Sequence + total length
//                0x2, 0x1, 0x0, // Integer (0)
//                0x30, 0xD, 0x6, 0x9, 0x2A, (byte) 0x86, 0x48, (byte) 0x86, (byte) 0xF7, 0xD, 0x1, 0x1, 0x1, 0x5, 0x0, // Sequence: 1.2.840.113549.1.1.1, NULL
//                0x4, (byte) 0x82, (byte) ((pkcs1Length >> 8) & 0xff), (byte) (pkcs1Length & 0xff) // Octet string + length
//        };
//        byte[] pkcs8bytes = join(pkcs8Header, pkcs1Bytes);
//        return readPkcs8PrivateKey(pkcs8bytes);
//    }
    private PrivateKey getPrivateKey(String privateKey) {
        try {
            RSAPrivateKey rsaPrivateKey = RSAPrivateKey.getInstance(Base64.getDecoder().decode(privateKey));
            byte[] privateKeyInfoBytes = KeyUtil.getEncodedPrivateKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, DERNull.INSTANCE), rsaPrivateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyInfoBytes);
            PrivateKey key = KeyFactory.getInstance("RSA").generatePrivate(keySpec);
            return key;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private byte[] encrypt(byte[] content, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private byte[] decrypt(byte[] content, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public OrderDto decodeOrder(String token) {
        String data = decode(token);
        OrderDto orderDto = gson.fromJson(data, OrderDto.class);
        return orderDto;
    }

    public GetCardDto decodeGetCard(String token) {
        String data = decode(token);
        GetCardDto getCardDto = gson.fromJson(data, GetCardDto.class);
        return getCardDto;
    }


    public CardInfoDto decodeCardInfo(String token) {
        String[] data = decodeWooppay(token).split("\\+");
        CardInfoDto cardInfoDto = new CardInfoDto();
        cardInfoDto.setPan(data[0]);
        cardInfoDto.setExp_month(data[1].substring(0, 2));
        cardInfoDto.setExp_year(data[1].substring(2));
        cardInfoDto.setCvc2(data[2]);
        return cardInfoDto;
    }

    public byte[] encodeCard(CardInfoDto cardInfoDto) {
        String data = gson.toJson(cardInfoDto, CardInfoDto.class);
        byte[] encrypted = encode(data);
        return encrypted;
    }
}
