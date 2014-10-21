package com.burnerchat.enctypter;

import android.annotation.SuppressLint;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class BurnerEncrypter {
	private static final String RSA = "RSA";
	private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");

	private static BurnerEncrypter instance = new BurnerEncrypter();
	
	private BurnerEncrypter() {
	}
	
	public static BurnerEncrypter getInstance() {
		return BurnerEncrypter.instance;
	}
	
	public String encrypt(String s, KeyPair keys) {
		try {
			Cipher cipher = Cipher.getInstance(RSA);
		    cipher.init(Cipher.ENCRYPT_MODE, keys.getPublic());
		    byte[] encrypted = cipher.doFinal(encodeUTF8(s));
		    return decodeUTF8(encrypted);
		    
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}

	    return null;
	}
	
	@SuppressLint("TrulyRandom")
	public KeyPair generateKeys() {
		try {
			return KeyPairGenerator.getInstance(RSA).generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	

	private String decodeUTF8(byte[] bytes) {
	    return new String(bytes, UTF8_CHARSET);
	}

	private byte[] encodeUTF8(String string) {
	    return string.getBytes(UTF8_CHARSET);
	}
}
