package be.sec;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class SecureStorage {
    private static final String VAULT_FILE_PATH = "vault.dat";
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    /**
     * Save new entre
     * @param entries
     * @param password
     * @throws Exception
     */
    public static void save(List<Entry> entries, String password) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream;
        byte[] encrypted;

        // Transform data
        try {objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);}
        catch (IOException e) {throw new RuntimeException(e);}

        // Write object
        try {objectOutputStream.writeObject(entries);}
        catch (IOException e) {throw new RuntimeException(e);}

        // Close stream
        try {objectOutputStream.close();}
        catch (IOException e) {throw new RuntimeException(e);}

        // Encrypt vault
        try {encrypted = encrypt(byteArrayOutputStream.toByteArray(), password);}
        catch (Exception e) {throw new RuntimeException(e);}

        // Write new vault
        try {Files.write(Paths.get(VAULT_FILE_PATH), encrypted);}
        catch (IOException e) {throw new RuntimeException(e);}
    }

    /**
     * Load entries list
     * @param password
     * @return entries list
     */
    public static List<Entry> load(String password) {
        Path vault = Paths.get(VAULT_FILE_PATH);
        List<Entry> entries;
        byte[] decrypted, data;
        ObjectInputStream ois;

        // Get file storage
        // TODO What the logic??? Add error message?
        if (!Files.exists(vault)) return new ArrayList<>();

        // Read file storage
        try {data = Files.readAllBytes(vault);}
        catch (IOException e) { throw new RuntimeException(e); }

        // Decrypt file storage
        try {decrypted = decrypt(data, password);}
        catch (Exception e) {throw new RuntimeException(e);}

        // Create object stream
        try {ois = new ObjectInputStream(new ByteArrayInputStream(decrypted));}
        catch (IOException e) {throw new RuntimeException(e);}

        // Transform objects to entries
        try {entries = (List<Entry>) ois.readObject();}
        catch (IOException | ClassNotFoundException e) {throw new RuntimeException(e);}

        return entries;
    }

    /**
     * Encrypt data with password
     * @param data
     * @param password
     * @return encrypted bytes
     */
    private static byte[] encrypt(byte[] data, String password) {
        Cipher cipher;
        byte[] encryptedBytes;

        // Transform
        try {cipher = Cipher.getInstance(TRANSFORMATION);}
        catch (NoSuchAlgorithmException | NoSuchPaddingException e) {throw new RuntimeException(e);}

        // Encrypt
        try {cipher.init(Cipher.ENCRYPT_MODE, getKey(password));}
        catch (Exception e) {throw new RuntimeException(e);}

        // Transform to byte array
        try {encryptedBytes = cipher.doFinal(data);}
        catch (IllegalBlockSizeException | BadPaddingException e) {throw new RuntimeException(e);}

        return encryptedBytes;
    }

    /**
     * Decrypt data
     * @param data
     * @param password
     * @return decrypted bytes
     */
    private static byte[] decrypt(byte[] data, String password) {
        Cipher cipher;
        byte[] decryptedBytes;

        // Transform
        try { cipher = Cipher.getInstance(TRANSFORMATION);}
        catch (NoSuchAlgorithmException | NoSuchPaddingException e) {throw new RuntimeException(e);}

        // Cypher init
        try {cipher.init(Cipher.DECRYPT_MODE, getKey(password));}
        catch (Exception e) {throw new RuntimeException(e);}

        // Transform to byte array
        try { decryptedBytes = cipher.doFinal(data);}
        catch (IllegalBlockSizeException | BadPaddingException e) {throw new RuntimeException(e);}

        return decryptedBytes;
    }

    /**
     * Get key
     * @param password
     * @return secret key
     */
    private static SecretKey getKey(String password) {

        // TODO Simplified, better to use PBKDF2
        // TODO What does it mean 16??? Can i use higher???
        byte[] key = Arrays.copyOf(password.getBytes(), 16);
        return new SecretKeySpec(key, ALGORITHM);
    }
}
