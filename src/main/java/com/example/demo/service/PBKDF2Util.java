package com.example.demo.service;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PBKDF2Util {

	// PBKDF2のパラメータを設定
	private static final int SALT_LENGTH = 16; // ソルトの長さ
	private static final int ITERATIONS = 65536; // 反復回数
	private static final int KEY_LENGTH = 256; // キーの長さ（ビット単位）

	// パスワードハッシュ化メソッド
	public static String hashPassword(String password) {
		try {
			// ランダムなソルトを生成
			//SecureRandomは暗号化専用ランダムデータタイプです
			SecureRandom random = new SecureRandom();
			byte[] salt = new byte[SALT_LENGTH];
			random.nextBytes(salt);

			// キー仕様を設定し、PBKDF2でパスワードをハッシュ化
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			byte[] hash = factory.generateSecret(spec).getEncoded();

			// ソルトとハッシュ値をBase64文字列にエンコードして返す
			Base64.Encoder enc = Base64.getEncoder();
			return enc.encodeToString(salt) + ":" + enc.encodeToString(hash);
		} catch (Exception e) {
			System.out.println("Error while hashing password: " + e.getMessage());
			return null;
		}
	}

	// パスワード検証メソッド
	public static boolean verifyPassword(String password, String storedHash) {
		try {
			// 保存されたソルトとハッシュ値を分割
			String[] parts = storedHash.split(":");
			byte[] salt = Base64.getDecoder().decode(parts[0]);
			byte[] hash = Base64.getDecoder().decode(parts[1]);

			// キー仕様を設定し、入力パスワードをPBKDF2でハッシュ化
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			byte[] testHash = factory.generateSecret(spec).getEncoded();

			// 入力パスワードのハッシュ値と保存されたハッシュ値を比較
			return java.util.Arrays.equals(hash, testHash);
		} catch (Exception e) {
			System.out.println("Error while verifying password: " + e.getMessage());
			return false;
		}
	}
}
