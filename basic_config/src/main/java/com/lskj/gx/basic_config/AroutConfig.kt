package com.lskj.gx.basic_config

/**
 *   创建时间:  2020/11/11
 *   编写人: tzw
 *   功能描述:
 */
class AroutConfig {
    companion object {
        val instance: AroutConfig by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AroutConfig()
        }

        //app start
        const val A_APP_SPLASH: String = "/app/splash_activity"
        const val A_APP_MAIN: String = "/app/main_activity"

        // 测试livedata com.lskj.gx.verbmk.test
        const val A_APP_LIVE_DATA_TEST: String = "/app/live_data_test"
        const val A_APP_ADAPTER_DBDING_TEST: String = "/app/adapte_databanding_test"

        //app end

        //account module  start
        const val A_ACCOUNT_LOGIN: String = "/account/login"
        //account module end


    }
}
