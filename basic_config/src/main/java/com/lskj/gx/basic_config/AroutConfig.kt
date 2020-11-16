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

        // 测试livedata com.lskj.gx.verbmk.test
        const val LIVE_DATA_TEST: String = "/app_live_data_test"

    }
}

