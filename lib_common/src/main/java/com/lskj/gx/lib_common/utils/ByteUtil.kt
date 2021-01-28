package com.lskj.gx.lib_common.utils

/**
 *   创建时间:  2021/1/5
 *   编写人: tzw
 *   功能描述:
 */
class ByteUtil {
    companion object {
        fun bytes2hex(var0: ByteArray?, var1: Boolean): String? {
            return if (var0 == null) {
                throw NullPointerException("要转换的 byte 数组不能为 null。")
            } else {
                val var2 = StringBuilder(var0.size)
                val var6: ByteArray = var0
                val var5 = var0.size
                for (var4 in 0 until var5) {
                    val var3 = var6[var4]
                    var2.append(String.format("%02x", var3))
                }
                if (var1) var2.toString().toUpperCase() else var2.toString()
            }
        }

        fun hex2Bytes(var0: String?): ByteArray? {
            var var0 = var0
            return if (var0 == null) {
                throw NullPointerException("要转换的十六进制字符串不能为 null。")
            } else {
                var0 = var0.trim { it <= ' ' }
                val var1 = var0.length
                if (var1 == 0) {
                    null
                } else if (var1 % 2 != 0) {
                    throw java.lang.Exception()
                } else {
                    val var2 = ByteArray(var1 / 2)
                    try {
                        var var3 = 0
                        while (var3 < var1) {
                            var2[var3 / 2] = var0.substring(var3, var3 + 2).toInt(16).toByte()
                            var3 += 2
                        }
                        var2
                    } catch (var4: Exception) {
                        throw var4
                    }
                }
            }
        }
    }
}