package com.test.ktor.helper

import kotlin.test.Test


class EncryptionHelperTest {

    @Test
    fun testEncryption() {
        val encrypted = EncryptionHelper.encrypt("password", "1234567890123456")
        println("Encrypted: $encrypted")

        val decrypted = EncryptionHelper.decrypt(encrypted, "1234567890123456")
        println("Decrypted: $decrypted")
    }
}