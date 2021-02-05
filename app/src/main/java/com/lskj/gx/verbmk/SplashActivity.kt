package com.lskj.gx.verbmk

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.gyf.immersionbar.ktx.hideStatusBar
import com.gyf.immersionbar.ktx.immersionBar
import com.lskj.gx.lib_common.base.activity.BaseActivity
import com.lskj.gx.lib_common.config.AroutConfig
import com.lskj.gx.verbmk.databinding.ActivitySplashBinding
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 *   创建时间:  2020/11/16
 *   编写人: tzw
 *   功能描述:app 启动页面
 */
@Route(path = AroutConfig.A_APP_SPLASH)
class SplashActivity : BaseActivity() {
    private lateinit var disPosable: Disposable
    private lateinit var dbding: ActivitySplashBinding

    override fun firstBdLayout() {
        dbding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        immersionBar {
            hideStatusBar()
        }
    }

    override fun afterBdInitParams() {
    }

    override fun afterPamsInitTbar() {
    }

    override fun afterTbarPreData(savedInstanceState: Bundle?) {
    }

    override fun afterPreData(savedInstanceState: Bundle?) {
        disPosable = Observable.create { emitter: ObservableEmitter<Int?> ->
            emitter.onNext(
                0
            )
        }
            .delay(1500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { //跳转
                ARouter.getInstance().build(AroutConfig.A_APP_MAIN).navigation()
//                ARouter.getInstance().build(AroutConfig.A_ACCOUNT_LOGIN).navigation()
                finish()
            }
    }

    override fun onDestroy() {
        if (!disPosable.isDisposed()) {
            disPosable.dispose()
        }
        super.onDestroy()
    }
}