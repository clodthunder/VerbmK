package com.lskj.gx.busi_account

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
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
    fun testCoroutin() {
        val deferred = (1..100).map { n ->
            GlobalScope.async {
                n
            }
        }
        println("javaClass = ${deferred}")
    }
}