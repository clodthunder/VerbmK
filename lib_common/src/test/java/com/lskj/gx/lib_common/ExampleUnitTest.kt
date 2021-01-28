package com.lskj.gx.lib_common

import com.lskj.gx.lib_common.utils.MD5Util
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testMd5() {
        var str = MD5Util.md5("123456")
        println("javaClass = ${str}")
    }
}