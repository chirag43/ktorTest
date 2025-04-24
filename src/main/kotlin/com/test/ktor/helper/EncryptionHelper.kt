package com.test.ktor.helper

import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object EncryptionHelper {

    fun decrypt(encrypted: String, key: String): String {
        val secretKey = SecretKeySpec(key.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val decodedBytes = Base64.getDecoder().decode(encrypted)
        val decrypted = cipher.doFinal(decodedBytes)
        return String(decrypted)
    }

    fun encrypt(raw: String, key: String): String {
        val secretKey = SecretKeySpec(key.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encryptedBytes = cipher.doFinal(raw.toByteArray())
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }
}


fun main() {
}