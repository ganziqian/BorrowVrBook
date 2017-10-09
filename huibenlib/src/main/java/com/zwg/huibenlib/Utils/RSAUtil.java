package com.zwg.huibenlib.Utils;



import org.myapache.commons.codec.binary.Base64;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;


public class RSAUtil {
	private final String KEY_ALGORITHM = "RSA";
	private static final int KEY_SIZE = 1024;
	private static final String PUBLIC_KEY = "PUBLIC_KEY";
	private static final String PRIVATE_KEY = "PRIVATE_KEY";
	private static final RSAUtil rsaUtil = new RSAUtil();

	public static RSAUtil getInstance() {
		return rsaUtil;
	}

	private String getPublicKey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		byte[] publicKey = key.getEncoded();
		return encryptBASE64(key.getEncoded());
	}

	private String getPrivateKey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		byte[] privateKey = key.getEncoded();
		return encryptBASE64(key.getEncoded());
	}

	private byte[] decryptBASE64(String key) throws Exception {
		return Base64.decodeBase64(key);
	}

	private String encryptBASE64(byte[] key) throws Exception {
		return Base64.encodeBase64String(key);
	}

	/**
	 * 得到公钥
	 * 
	 * @param key
	 *            密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	private PublicKey getPublicKey(String key) throws Exception {
		byte[] keyBytes;
		keyBytes =Base64.decodeBase64(key);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	/**
	 * 得到私钥
	 * 
	 * @param key
	 *            密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	private PrivateKey getPrivateKey(String key) throws Exception {
		byte[] keyBytes;
		keyBytes =Base64.decodeBase64(key);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

	private byte[] encrypt(byte[] source, String publicKey) throws Exception {
		PublicKey key = getPublicKey(publicKey);
		Cipher cipher=Cipher.getInstance("RSA/ECB/PKCS1Padding");
		//Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] bt_encrypted = cipher.doFinal(source);
		return bt_encrypted;
	}

	private byte[] decrypt(byte[] mix, String privateKey) throws Exception {
		PrivateKey key = getPrivateKey(privateKey);
	//	Cipher cipher = Cipher.getInstance("RSA");
		Cipher cipher=Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] bt_original = cipher.doFinal(mix);
		return bt_original;
	}

	public Map<String, String> applyKeyPair() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(KEY_SIZE);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, Object> keyMap = new HashMap<String, Object>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		Map<String, String> keys = new HashMap<String, String>();
		keys.put(PUBLIC_KEY, getPublicKey(keyMap));
		keys.put(PRIVATE_KEY, getPrivateKey(keyMap));
		return keys;

	}

	/**
	 * 加密
	 * 
	 * @param source
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public String encrypt(String source, String publicKey) throws Exception {
		return Base64.encodeBase64String(encrypt(source.getBytes(), publicKey));

	}

	/**
	 * 解密
	 * 
	 * @param mix
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public String decrypt(String mix, String privateKey) throws Exception {
		return new String(decrypt(Base64.decodeBase64(mix), privateKey),"gbk");
	}

	public static void main(String[] args) throws Exception {
		Map<String, String> keys = RSAUtil.getInstance().applyKeyPair();
		String pub = keys.get(PUBLIC_KEY);
		String pri = keys.get(PRIVATE_KEY);
		String source = "442C05D98A42";
		System.err.println("原始内容：" + source);
		System.err.println("公钥：" + pub);
		String mix = RSAUtil.getInstance().encrypt(source, pub);
		System.err.println("加密后的内容：" + mix);
		System.err.println("私钥:" + pri);
		String de = RSAUtil.getInstance().decrypt(mix, pri);
		System.err.println("解密后的内容：" + de);
	

	}
}