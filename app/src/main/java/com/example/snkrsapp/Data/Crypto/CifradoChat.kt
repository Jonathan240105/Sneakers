package com.example.snkrsapp.Data.Crypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.MGF1ParameterSpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.OAEPParameterSpec
import javax.crypto.spec.PSource
import javax.crypto.spec.SecretKeySpec

data class MensajeCifrado(
    val textoCifrado: String,
    val iv: String
)

object CifradoChat {
    private const val ANDROID_KEY_STORE = "AndroidKeyStore"
    private const val ALIAS_PREFIX = "chat_private_key_"
    private const val AES_TRANSFORMATION = "AES/GCM/NoPadding"
    private const val RSA_TRANSFORMATION = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding"
    private const val GCM_TAG_LENGTH = 128
    private val rsaOaepSpec = OAEPParameterSpec(
        "SHA-256",
        "MGF1",
        MGF1ParameterSpec.SHA1,
        PSource.PSpecified.DEFAULT
    )

    fun crearParClavesSiNoExiste(uid: String, deviceId: String) {
        val alias = aliasUsuario(uid, deviceId)
        val keyStore = cargarKeyStore()

        if (keyStore.containsAlias(alias)) return

        val keyPairGenerator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_RSA,
            ANDROID_KEY_STORE
        )

        val spec = KeyGenParameterSpec.Builder(
            alias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setKeySize(2048)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
            .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
            .build()

        keyPairGenerator.initialize(spec)
        keyPairGenerator.generateKeyPair()
    }

    fun obtenerClavePublicaBase64(uid: String, deviceId: String): String {
        val certificate = cargarKeyStore().getCertificate(aliasUsuario(uid, deviceId))
        val publicKey = certificate.publicKey
        return publicKey.encoded.toBase64()
    }

    fun generarClaveChat(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES)
        keyGenerator.init(256)
        return keyGenerator.generateKey()
    }

    fun cifrarMensaje(texto: String, claveChat: SecretKey): MensajeCifrado {
        val cipher = Cipher.getInstance(AES_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, claveChat)

        return MensajeCifrado(
            textoCifrado = cipher.doFinal(texto.toByteArray(Charsets.UTF_8)).toBase64(),
            iv = cipher.iv.toBase64()
        )
    }

    fun descifrarMensaje(mensaje: MensajeCifrado, claveChat: SecretKey): String {
        val cipher = Cipher.getInstance(AES_TRANSFORMATION)
        val iv = mensaje.iv.fromBase64()

        cipher.init(
            Cipher.DECRYPT_MODE,
            claveChat,
            GCMParameterSpec(GCM_TAG_LENGTH, iv)
        )

        return String(cipher.doFinal(mensaje.textoCifrado.fromBase64()), Charsets.UTF_8)
    }

    fun cifrarClaveChatParaUsuario(
        claveChat: SecretKey,
        clavePublicaUsuarioBase64: String
    ): String {
        val cipher = Cipher.getInstance(RSA_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, clavePublicaUsuarioBase64.toPublicKey(), rsaOaepSpec)
        return cipher.doFinal(claveChat.encoded).toBase64()
    }

    fun descifrarClaveChat(uid: String, deviceId: String, claveChatCifradaBase64: String): SecretKey {
        val cipher = Cipher.getInstance(RSA_TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, obtenerClavePrivada(uid, deviceId), rsaOaepSpec)

        val claveChatBytes = cipher.doFinal(claveChatCifradaBase64.fromBase64())
        return SecretKeySpec(claveChatBytes, KeyProperties.KEY_ALGORITHM_AES)
    }

    private fun obtenerClavePrivada(uid: String, deviceId: String): PrivateKey {
        return cargarKeyStore().getKey(aliasUsuario(uid, deviceId), null) as PrivateKey
    }

    private fun cargarKeyStore(): KeyStore {
        return KeyStore.getInstance(ANDROID_KEY_STORE).apply {
            load(null)
        }
    }

    private fun aliasUsuario(uid: String, deviceId: String): String = ALIAS_PREFIX + uid + "_" + deviceId

    private fun ByteArray.toBase64(): String =
        Base64.encodeToString(this, Base64.NO_WRAP)

    private fun String.fromBase64(): ByteArray =
        Base64.decode(this, Base64.NO_WRAP)

    private fun String.toPublicKey(): PublicKey {
        val keySpec = X509EncodedKeySpec(fromBase64())
        return KeyFactory.getInstance(KeyProperties.KEY_ALGORITHM_RSA).generatePublic(keySpec)
    }
}
