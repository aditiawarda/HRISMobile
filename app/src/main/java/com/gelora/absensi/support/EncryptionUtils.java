package com.gelora.absensi.support;

import android.util.Base64;

public class EncryptionUtils {

    // Encryption key (example, replace with your own key)
    private static final String ENCRYPTION_KEY = "HRIS_g4p_P0nc0L@13740";

    // Simple XOR encryption
    public static String encrypt(String input) {
        byte[] inputBytes = input.getBytes();
        byte[] keyBytes = ENCRYPTION_KEY.getBytes();
        byte[] encryptedBytes = new byte[inputBytes.length];

        for (int i = 0; i < inputBytes.length; i++) {
            encryptedBytes[i] = (byte) (inputBytes[i] ^ keyBytes[i % keyBytes.length]);
        }

        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
    }

    // Simple XOR decryption
    public static String decrypt(String encryptedInput) {
        byte[] encryptedBytes = Base64.decode(encryptedInput, Base64.DEFAULT);
        byte[] keyBytes = ENCRYPTION_KEY.getBytes();
        byte[] decryptedBytes = new byte[encryptedBytes.length];

        for (int i = 0; i < encryptedBytes.length; i++) {
            decryptedBytes[i] = (byte) (encryptedBytes[i] ^ keyBytes[i % keyBytes.length]);
        }

        return new String(decryptedBytes);
    }

}
