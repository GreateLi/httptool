package com.http.tools.utils;

import android.util.Base64;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSAUtil {
	static String eee = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100b032dd48c07e41886a27b3c0a8c45265725829e3fc5c51f61b922697d8a2601c2dc6147f643e6f22301002d1f71e6ee26930c05a81702291219850b33b37cc4d544a1d8bb5e9ed855020e21048d61c63d91b45a79adc67c975afe850ef362fd5e2b3f8607f39860cec340a21aecfa5ba9a642065ded3680a1cfb901e69851ff74c415719c9eafa78d57c5ca0796dfbf3d1783d80b1211c79178b2086f05b2931c52022e697a11011ccb44ea796487d79569a0738f67db5b0328e0ddb65848367c568bc8b3f2cf33fd5e2522f1dadec6c8ec923913c5509bfe7c0d0bb8947e4d344f51c8c3d6827f78941d6b4d7299cb28b0234e6af81643bca8b522517a673e50203010001";
	/** 加密算法 */
	private static final String ALGORITHM = "RSA";
	/** 签名算法 */
	private static final String ALGORITHM_SIGN = "MD5withRSA";
	/** CIPHER算法名称 */
	/**
	 * <h1>常用填充模式有三种:</h1>
	 * <ul>
	 * <li>1.RSA/ECB/PKCS1Padding 最常用的模式</li>
	 * <br/>输入：必须比RSA钥模长(modulus)短至少11个字节,也就是RSA_size(rsa) – 11.如果输入的明文过长，必须先进行切割，然后再填充
	 * <br/>输出：和modulus一样长
	 * <br/>根据这个要求，对于512bit的密钥，block length = 512/8 – 11 = 53 字节
	 * <li>2.RSA/ECB/OAEPPadding</li>
	 * <br/>输入：RSA_size(rsa) – 41
	 * <br/>输出：和modulus一样长
	 * <li>3.RSA/ECB/NoPadding 不填充</li>
	 * <br/>输入：可以和RSA钥模长一样长，如果输入的明文过长，必须切割，然后填充
	 * <br/>输出：和modulus一样长
	 * </ul>
	 * 
	 */
	private static final String CIPHER_NAME = "RSA/ECB/PKCS1Padding";

	/** 密钥长度 **/
	private static final int KEY_SIZE = 2048;
	/** RSA解密长度 */
	private static final int decryptLen = KEY_SIZE / 8;
	/** RSA加密长度 */
	private static final int encryptLen = decryptLen - 11;

	private static String romString ="";

	private static String myKey="";

	public static String getMykey(String romString,String publicKey)
	{
		if(myKey.isEmpty())
		{
			RSAPublicKey rsaPublicKey = null;
			try {
				rsaPublicKey = loadPublicKeyByStr(publicKey);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				myKey = encryptByPublicKey(romString ,rsaPublicKey);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return myKey;
	}


	/**
	 * 从字符串中加载公钥
	 *
	 * @param publicKeyStr
	 *            公钥数据字符串
	 * @throws Exception
	 *             加载公钥时产生的异常
	 */
	public static RSAPublicKey loadPublicKeyByStr(String publicKeyStr)
			throws Exception {
		try {
			byte[] buffer = Base64.decode(publicKeyStr,Base64.DEFAULT);
			//String temp = StringUtils.bytes2Hex(buffer);
			//int len = temp.length();
			//boolean ret = temp.equals(eee);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("公钥非法");
		} catch (NullPointerException e) {
			throw new Exception("公钥数据为空");
		}
	}
	/**
	 * 公钥加密
	 *
	 * @param data
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static String encryptByPublicKey(String data, RSAPublicKey publicKey)
			throws Exception {
		//Cipher cipher = Cipher.getInstance("RSA");
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		// 模长
		int key_len = publicKey.getModulus().bitLength() / 8;
		// 加密数据长度 <= 模长-11
		String[] datas = splitString(data, key_len - 11);
		String mi = "";
		//如果明文长度大于模长-11则要分组加密
		for (String s : datas) {
			mi += bcd2Str(cipher.doFinal(s.getBytes()));
		}
		return mi;
	}
	/**
	 * 拆分字符串
	 */
	public static String[] splitString(String string, int len) {
		int x = string.length() / len;
		int y = string.length() % len;
		int z = 0;
		if (y != 0) {
			z = 1;
		}
		String[] strings = new String[x + z];
		String str = "";
		for (int i=0; i<x+z; i++) {
			if (i==x+z-1 && y!=0) {
				str = string.substring(i*len, i*len+y);
			}else{
				str = string.substring(i*len, i*len+len);
			}
			strings[i] = str;
		}
		return strings;
	}
	/**
	 * BCD转字符串
	 */
	public static String bcd2Str(byte[] bytes) {
		char temp[] = new char[bytes.length * 2], val;

		for (int i = 0; i < bytes.length; i++) {
			val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
			temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

			val = (char) (bytes[i] & 0x0f);
			temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
		}
		return new String(temp);
	}


	/**
	 * 生成密钥对
	 * 
	 * @param keySize
	 * @throws NoSuchAlgorithmException
	 */
	public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator kpGenerator = KeyPairGenerator.getInstance(ALGORITHM);
		SecureRandom secureRandom = new SecureRandom();
		kpGenerator.initialize(KEY_SIZE, secureRandom);
		KeyPair kp = kpGenerator.genKeyPair();

		return kp;
	}

	/**
	 * 用私钥签名
	 * 
	 * @param data   待签名字节数组
	 * @param priKey 私钥
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] sign(byte[] data, PrivateKey priKey) throws NoSuchAlgorithmException, InvalidKeySpecException,
			InvalidKeyException, SignatureException, UnsupportedEncodingException {
		Signature signature = Signature.getInstance(ALGORITHM_SIGN);
		signature.initSign(priKey);
		signature.update(data);

		byte[] signBytes = signature.sign();
		return signBytes;
	}

	/**
	 * 验证签名
	 * 
	 * @param content
	 * @param signBytes
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws InvalidKeyException
	 * @throws UnsupportedEncodingException
	 * @throws SignatureException
	 */
	public static boolean verify(byte[] data, byte[] signBytes, PublicKey pubKey) throws NoSuchAlgorithmException,
			InvalidKeySpecException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
		Signature signature = Signature.getInstance(ALGORITHM_SIGN);
		signature.initVerify(pubKey);
		signature.update(data);

		return signature.verify(signBytes);
	}

	/**
	 * 加密数据
	 * 
	 * @param data 待加密内容
	 * @param key  密钥
	 * @return
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static byte[] encrypt(byte[] data, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance(CIPHER_NAME);
		cipher.init(Cipher.ENCRYPT_MODE, key);

		String s = "";
		int dataLen = data.length;
		for (int i = 0; i <= dataLen / encryptLen; i++) {
			int pos = i * encryptLen;
			if (pos == dataLen) {
				break;
			}
			int length = encryptLen;
			if (pos + encryptLen > dataLen) {
				length = dataLen - pos;
			}
			byte[] t = subBytes(data, pos, length);
			byte[] bytes = cipher.doFinal(t);
			s += StringUtils.bytes2Hex(bytes);
		}

		return StringUtils.hex2Bytes(s);
	}

	/**
	 * 解密数据
	 * 
	 * @param data 待解密内容
	 * @param key  密钥
	 * @return
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static byte[] decrypt(byte[] data, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance(CIPHER_NAME);
		cipher.init(Cipher.DECRYPT_MODE, key);

		String s = "";
		int dataLen = data.length;
		for (int i = 0; i <= dataLen / decryptLen; i++) {
			int pos = i * decryptLen;
			if (pos == dataLen) {
				break;
			}
			int length = decryptLen;
			if (pos + decryptLen > dataLen) {
				length = dataLen - pos;
			}
			byte[] t = subBytes(data, pos, length);
			byte[] bytes = cipher.doFinal(t);
			s += StringUtils.bytes2Hex(bytes);
		}

		return StringUtils.hex2Bytes(s);
	}

	/**
	 * Encrypt file using 1024 RSA encryption
	 * 
	 * @param srcFileName  Source file name
	 * @param destFileName Destination file name
	 * @param key          The key. For encryption this is the Private Key and for
	 *                     decryption this is the public key
	 * @param cipherMode   Cipher Mode
	 * @throws Exception
	 */
	public static void encryptFile(String srcFileName, String destFileName, PublicKey key) throws Exception {
		encryptDecryptFile(srcFileName, destFileName, key, Cipher.ENCRYPT_MODE);
	}

	/**
	 * Decrypt file using 1024 RSA encryption
	 * 
	 * @param srcFileName  Source file name
	 * @param destFileName Destination file name
	 * @param key          The key. For encryption this is the Private Key and for
	 *                     decryption this is the public key
	 * @param cipherMode   Cipher Mode
	 * @throws Exception
	 */
	public static void decryptFile(String srcFileName, String destFileName, PrivateKey key) throws Exception {
		encryptDecryptFile(srcFileName, destFileName, key, Cipher.DECRYPT_MODE);
	}

	/**
	 * Encrypt and Decrypt files using 1024 RSA encryption
	 * 
	 * @param srcFileName  Source file name
	 * @param destFileName Destination file name
	 * @param key          The key. For encryption this is the Private Key and for
	 *                     decryption this is the public key
	 * @param cipherMode   Cipher Mode
	 * @throws Exception
	 */
	public static void encryptDecryptFile(String srcFileName, String destFileName, Key key, int cipherMode)
			throws Exception {
		OutputStream outputWriter = null;
		InputStream inputReader = null;
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_NAME);
			// String textLine = null;
			// RSA encryption data size limitations are slightly less than the
			// key modulus size,
			// depending on the actual padding scheme used (e.g. with 1024 bit
			// (128 byte) RSA key,
			// the size limit is 117 bytes for PKCS#1 v 1.5 padding.
			// (http://www.jensign.com/JavaScience/dotnet/RSAEncrypt/)
			byte[] buf = cipherMode == Cipher.ENCRYPT_MODE ? new byte[100] : new byte[128];
			int bufl;
			// init the Cipher object for Encryption...
			cipher.init(cipherMode, key);

			// start FileIO
			outputWriter = new FileOutputStream(destFileName);
			inputReader = new FileInputStream(srcFileName);
			while ((bufl = inputReader.read(buf)) != -1) {
				byte[] encText = null;
				if (cipherMode == Cipher.ENCRYPT_MODE) {
					encText = encrypt(copyBytes(buf, bufl), (PublicKey) key);
				} else {
					System.out.println("buf = " + new String(buf));
					encText = decrypt(copyBytes(buf, bufl), (PrivateKey) key);
				}
				outputWriter.write(encText);
				System.out.println("encText = " + new String(encText));
			}
			outputWriter.flush();

		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (outputWriter != null) {
					outputWriter.close();
				}
				if (inputReader != null) {
					inputReader.close();
				}
			} catch (Exception e) {
				// do nothing...
			} // end of inner try, catch (Exception)...
		}
	}

	public static byte[] copyBytes(byte[] arr, int length) {
		byte[] newArr = null;
		if (arr.length == length) {
			newArr = arr;
		} else {
			newArr = new byte[length];
			for (int i = 0; i < length; i++) {
				newArr[i] = (byte) arr[i];
			}
		}
		return newArr;
	}

	public static byte[] subBytes(byte[] data, int pos, int length) {
		byte[] newBytes = new byte[length];
		for (int i = 0; i < length; i++) {
			newBytes[i] = (byte) data[pos + i];
		}
		return newBytes;
	}

	public static void main(String[] args) {
		try {
			String charsetName = "utf8";

			String content = "192.168.123.123+" + System.currentTimeMillis() + "+123";
			
			content = "";
			for(int i = 0; i < 256; i++) {
				content += "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";
			}
			
			byte[] data = content.getBytes(charsetName);

			KeyPair kp = RSAUtil.generateKeyPair();
			PublicKey pubKey = kp.getPublic();
			PrivateKey priKey = kp.getPrivate();
			
			Long start = System.currentTimeMillis();
			for (int i = 0; i < 1000; i++) {
				byte[] signBytes = RSAUtil.sign(data, priKey);
				RSAUtil.verify(data, signBytes, pubKey);
			}
			System.out.println("签名耗费时间:" + (System.currentTimeMillis() - start));

			start = System.currentTimeMillis();
			byte[] t = RSAUtil.encrypt(data, priKey);
			System.out.println("加密耗时:" + (System.currentTimeMillis() - start));
			System.out.println("加密后数据=" + StringUtils.bytes2Hex(t));

			start = System.currentTimeMillis();
			t = RSAUtil.decrypt(t, pubKey);
			System.out.println("解密耗时:" + (System.currentTimeMillis() - start));
			System.out.println("解密后数据=" + new String(t, charsetName));
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
