package com.lskj.gx.lib_common.utils

import android.util.Base64
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 *   创建时间:  2021/1/5
 *   编写人: tzw
 *   功能描述:
 */
class EncryptionUtil {
    companion object {
        private const val SUFFIX = "PIEncrypt"
        private val KEY =
            byteArrayOf(84, 94, -60, 118, 67, -20, -55, -70)
        private val IV =
            byteArrayOf(79, 76, -120, -36, -61, -72, 91, 50)

        fun getKey(var0: String?): String {
            return piDecrypt("2H1T5whE5Ius4unsFyMQ2VEYiSEcJZs/33+iaZHSpv548yQUt+C7fv/cjM3Rsa18JqdxeHA1zMc=PIEncrypt")
        }

        fun encryStringParams(param: String?): String? {
            return aesEncrypt(param, getKey(null))
        }

        fun decryStringParams(param: String?): String? {
            return aesDecrypt(param, getKey(null))
        }

        fun EncryptionUtil() {}

        fun piEncrypt(var0: String?): String? {
            return if (var0 == null) {
                throw NullPointerException("要加密的字符串不能为 null。")
            } else {
                try {
                    val var1: SecretKey =
                        SecretKeyFactory.getInstance("DES").generateSecret(DESKeySpec(KEY))
                    val var2: Cipher = Cipher.getInstance("DES/CBC/PKCS5Padding")
                    var2.init(1, var1, IvParameterSpec(IV))
                    Base64.encodeToString(var2.doFinal(var0.toByteArray(charset("UTF8"))), 0)
                        .trim().toString() + "PIEncrypt"
                } catch (var3: Exception) {
                    throw var3
                }
            }
        }

        fun piDecrypt(var0: String?): String {
            var var0 = var0
            return if (var0 == null) {
                throw NullPointerException("要解密的字符串不能为 null。")
            } else {
                try {
                    val var1: SecretKey =
                        SecretKeyFactory.getInstance("DES").generateSecret(DESKeySpec(KEY))
                    val var2: Cipher = Cipher.getInstance("DES/CBC/PKCS5Padding")
                    var2.init(2, var1, IvParameterSpec(IV))
                    var0 = var0.substring(0, var0.length - "PIEncrypt".length)
                    String(var2.doFinal(Base64.decode(var0, 0)), Charset.forName("UTF8"))
                } catch (var3: Exception) {
                    throw var3
                }
            }
        }

        fun aesEncrypt(
            var0: String?,
            var1: String?
        ): String? {
            return if (var0 == null) {
                throw NullPointerException("要加密的字符串不能为 null。")
            } else {
                try {
                    val var2 =
                        SecretKeySpec(md5EncryptToBytes(var1), "AES")
                    val var3: Cipher = Cipher.getInstance("AES")
                    var3.init(1, var2)
                    ByteUtil.bytes2hex(
                        var3.doFinal(var0.toByteArray(charset("UTF-8"))),
                        false
                    )
                } catch (var4: Exception) {
                    throw var4
                }
            }
        }

        fun aesDecrypt(
            var0: String?,
            var1: String?
        ): String? {
            return if (var0 == null) {
                throw NullPointerException("要加密的字符串不能为 null。")
            } else {
                try {
                    val var2 =
                        SecretKeySpec(md5EncryptToBytes(var1), "AES")
                    val var3: Cipher = Cipher.getInstance("AES")
                    var3.init(2, var2)
                    String(var3.doFinal(ByteUtil.hex2Bytes(var0)))
                } catch (var4: Exception) {
                    throw var4
                }
            }
        }

        fun md5EncryptToString(var0: String?): String? {
            return ByteUtil.bytes2hex(md5EncryptToBytes(var0), false)
        }

        fun md5EncryptToBytes(var0: String?): ByteArray? {
            return if (var0 == null) {
                throw NullPointerException("要加密的字符串不能为 null。")
            } else {
                try {
                    val var1: MessageDigest = MessageDigest.getInstance("MD5")
                    var1.reset()
                    var1.update(var0.toByteArray(charset("UTF-8")))
                    var1.digest()
                } catch (var2: NoSuchAlgorithmException) {
                    throw var2
                } catch (var3: Exception) {
                    throw var3
                }
            }
        }
    }
}