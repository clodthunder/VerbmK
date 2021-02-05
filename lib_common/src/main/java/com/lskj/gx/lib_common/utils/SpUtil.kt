package com.lskj.gx.lib_common.utils
import android.content.SharedPreferences
import android.preference.PreferenceManager.getDefaultSharedPreferences
import com.lskj.gx.lib_common.config.AppContext
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 *   创建时间:  2021/1/5
 *   编写人: tzw
 *   功能描述: 使用委托的方式实现
 *
 *  // 获取值
 *       val host = SharedPreferencesUtils.HOST ?: ""
 *       val port = SharedPreferencesUtils.PORT ?: 0
 *  // 设置值
 *       SharedPreferencesUtils.HOST = "我是设置的新值"
 *       SharedPreferencesUtils.PORT = 8080
 */
class SpUtil {
    object SharedPreferencesUtils {
        var preferences: SharedPreferences =
            getDefaultSharedPreferences(AppContext.instance.getContext())

        //存储token
        var TOKEN by SharedPreferenceDelegates.string("")
        var DEFAULT_BASE_URL by SharedPreferenceDelegates.string("")
        var DEFAULT_BASE_PORT by SharedPreferenceDelegates.int(0)

    }


    /**
     * 定义类型 属性委托类
     */
    private object SharedPreferenceDelegates {

        /**
         * 定义委托获取和设置对应类型的方法
         * 委托的原理,大家可以看我前面的文章
         */
        fun int(defaultValue: Int = 0) = object : ReadWriteProperty<SharedPreferencesUtils, Int> {

            override fun getValue(thisRef: SharedPreferencesUtils, property: KProperty<*>): Int {
                /**
                 * 当获取值的时候,调用此方法
                 * key 值是对应变量的昵称
                 */
                return thisRef.preferences.getInt(property.name, defaultValue)
            }

            override fun setValue(
                thisRef: SharedPreferencesUtils,
                property: KProperty<*>,
                value: Int
            ) {
                /**
                 * 当设置值的时候,调用此方法
                 * key 值是对应变量的昵称
                 */
                thisRef.preferences.edit().putInt(property.name, value).apply()
            }
        }

        fun long(defaultValue: Long = 0L) =
            object : ReadWriteProperty<SharedPreferencesUtils, Long> {

                override fun getValue(
                    thisRef: SharedPreferencesUtils,
                    property: KProperty<*>
                ): Long {
                    return thisRef.preferences.getLong(property.name, defaultValue)
                }

                override fun setValue(
                    thisRef: SharedPreferencesUtils,
                    property: KProperty<*>,
                    value: Long
                ) {
                    thisRef.preferences.edit().putLong(property.name, value).apply()
                }
            }

        fun boolean(defaultValue: Boolean = false) =
            object : ReadWriteProperty<SharedPreferencesUtils, Boolean> {
                override fun getValue(
                    thisRef: SharedPreferencesUtils,
                    property: KProperty<*>
                ): Boolean {
                    return thisRef.preferences.getBoolean(property.name, defaultValue)
                }

                override fun setValue(
                    thisRef: SharedPreferencesUtils,
                    property: KProperty<*>,
                    value: Boolean
                ) {
                    thisRef.preferences.edit().putBoolean(property.name, value).apply()
                }
            }

        fun float(defaultValue: Float = 0.0f) =
            object : ReadWriteProperty<SharedPreferencesUtils, Float> {
                override fun getValue(
                    thisRef: SharedPreferencesUtils,
                    property: KProperty<*>
                ): Float {
                    return thisRef.preferences.getFloat(property.name, defaultValue)
                }

                override fun setValue(
                    thisRef: SharedPreferencesUtils,
                    property: KProperty<*>,
                    value: Float
                ) {
                    thisRef.preferences.edit().putFloat(property.name, value).apply()
                }
            }

        fun string(defaultValue: String) =
            object : ReadWriteProperty<SharedPreferencesUtils, String> {
                override fun getValue(
                    thisRef: SharedPreferencesUtils,
                    property: KProperty<*>
                ): String {
                    return thisRef.preferences.getString(property.name, defaultValue) ?: ""
                }

                override fun setValue(
                    thisRef: SharedPreferencesUtils,
                    property: KProperty<*>,
                    value: String
                ) {
                    thisRef.preferences.edit().putString(property.name, value).apply()
                }
            }
    }
}