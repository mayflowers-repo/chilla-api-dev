package com.mayflowertech.chilla.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import org.springframework.stereotype.Component;

import com.mayflowertech.chilla.config.custom.CustomException;

@Component
public class RSAEncryptionConfigUtil {

    private final PrivateKey privateKey;

    public RSAEncryptionConfigUtil() throws Exception {
        this.privateKey = loadPrivateKey("private_key.pem");
    }

    private PrivateKey loadPrivateKey(String fileName) throws Exception {
        // Load the file as a classpath resource
        InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (resourceStream == null) {
            throw new IllegalArgumentException("Private key file not found in classpath: " + fileName);
        }

        // Read the file content
        String keyContent = new BufferedReader(new InputStreamReader(resourceStream))
                .lines()
                .reduce("", (acc, line) -> acc + line)
                .replaceAll("(?m)-+BEGIN [^-]+-+|-+END [^-]+-+", "")
                .replaceAll("\\s", "");

        // Decode and convert to PrivateKey
        byte[] keyBytes = Base64.getDecoder().decode(keyContent);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }


    public String decrypt(String encryptedData) throws CustomException {
        try {
            // Validate encrypted data
            if (encryptedData == null || encryptedData.isEmpty()) {
                throw new CustomException("Encrypted data is null or empty");
            }

            // Decode the Base64 encrypted string
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);

            // Decrypt using RSA
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            // Convert decrypted bytes to String
            return new String(decryptedBytes);

        } catch (CustomException e) {
            throw e; // Re-throw custom exceptions for specific handling
        } catch (Throwable e) {
            // Wrap all other exceptions in CustomDecryptionException
            throw new CustomException("Decryption failed due to an unexpected error", e);
        }
    }
}
