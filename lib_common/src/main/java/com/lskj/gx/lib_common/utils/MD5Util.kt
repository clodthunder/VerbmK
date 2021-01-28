package com.lskj.gx.lib_common.utils

import android.util.Log
import java.security.MessageDigest


/**
 *   创建时间:  2021/1/6
 *   编写人: tzw
 *   功能描述:
 */
class MD5Util {
    companion object {
        private val TAG: String = MD5Util::class.java.simpleName
        fun md5(content: String): String? {
            // 用于加密的字符
            val md5String = charArrayOf(
                '0',
                '1',
                '2',
                '3',
                '4',
                '5',
                '6',
                '7',
                '8',
                '9',
                'A',
                'B',
                'C',
                'D',
                'E',
                'F'
            )
            return try {
                // 使用平台默认的字符集将md5String编码为byte序列,并将结果存储到一个新的byte数组中
                val byteInput = content.toByteArray()

                // 信息摘要是安全的单向哈希函数,它接收任意大小的数据,并输出固定长度的哈希值
                val mdInst: MessageDigest = MessageDigest.getInstance("MD5")

                // MessageDigest对象通过使用update方法处理数据,使用指定的byte数组更新摘要
                mdInst.update(byteInput)

                //摘要更新后通过调用digest() 执行哈希计算,获得密文
                val md: ByteArray = mdInst.digest()

                //把密文转换成16进制的字符串形式
                val j = md.size
                val str = CharArray(j * 2)
                var k = 0
                for (byte0 in md) {
                    str[k++] = md5String[(byte0).toInt() ushr 4 and 0xf]
                    str[k++] = md5String[(byte0).toInt() and 0xf]
                }
                // 返回加密后的字符串
                String(str)
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString())
                null
            }
        }
    }
}