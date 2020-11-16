package com.lskj.gx.verbmk

import androidx.lifecycle.MutableLiveData
import com.lskj.gx.verbmk.entity.UserLiveData
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Ignore
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testMult() {
        val userVms: MutableLiveData<List<UserLiveData>> =
            MutableLiveData<List<UserLiveData>>().also {
                loadUser()
            }
    }

    fun loadUser() {
        val userList: MutableList<UserLiveData> = mutableListOf()
        userList.add(UserLiveData("tzw", 12))
        userList.add(UserLiveData("wzy", 14))
    }

    //测试kotlin 数据的基本类型 学习标准库方法
    @Test
    @Ignore
    fun testJblx() {
        val i: Int = 1
        val b: Int? = i
        val c: Int? = i
        println(b == i)
        println(c == i)
        println(b === c)

        var a: Int = 12
        a.inc()
        a.dec()
    }

    @Test
    @Ignore
    fun testArr() {
        var arrint = arrayListOf<Int>();
        arrint[0] = 0;
        arrint[1] = 1;
//        println(arrint.asSequence())
//        var a: Int = 1
//        var b: Int = 2
//        var c: MutableList<Int> = Arrays.asList(a, b)
//        val greenString = arrint.joinToString()
//        println("greenStr=${greenString}")
    }

    @Test
    @Ignore
    fun testList() {
        val doubled = List(3) { it }  // 如果你想操作这个集合，应使用 MutableList
        println(doubled)
    }

    //测试数列
    @Test
    @Ignore
    fun testProgression() {
        var intPro: IntProgression = IntProgression.fromClosedRange(1, 100, 2)
        var intIterator = intPro.iterator()
        while (intIterator.hasNext()) {
            var str = intIterator.next()
            println(str)
        }

        var aa: kotlin.sequences.Sequence<kotlin.Int> = intPro.asSequence()
        var sIterator = aa.iterator()
        while (sIterator.hasNext()) {
            println("aa" + sIterator.next())
        }


    }
}