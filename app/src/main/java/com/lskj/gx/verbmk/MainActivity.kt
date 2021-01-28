package com.lskj.gx.verbmk

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.lskj.gx.basi_base.activity.BaseActivity
import com.lskj.gx.lib_common.config.AroutConfig
import com.lskj.gx.verbmk.databinding.ActivityMainBinding
import com.lskj.gx.verbmk.entity.User

@Route(path = AroutConfig.A_APP_MAIN)
class
MainActivity : BaseActivity() {
    //视图绑定
    private lateinit var bding: ActivityMainBinding
    private lateinit var user: User

    override fun firstBdLayout() {
        bding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun afterBdInitParams() {

    }

    override fun afterPamsInitTbar() {

    }

    override fun afterTbarPreData(savedInstanceState: Bundle?) {
        user = User()
        user.isEnable = false
        user.username = "tzw"
        user.userPwd = "pwd"
        user.userPicUrl =
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1605276319951&di=80b09dd5a484a869e57c206e6a4fde16&imgtype=0&src=http%3A%2F%2Fimg.article.pchome.net%2F00%2F34%2F09%2F06%2Fpic_lib%2Fs960x639%2Fjmfj061s960x639.jpg"
        bding.user = this.user
    }

    override fun afterPreData(savedInstanceState: Bundle?) {
        bding.btnAddStack.setOnClickListener {
            user.isEnable = true
            Toast.makeText(this, "" + user.toString(), Toast.LENGTH_LONG).show()
        }
        bding.btnRemoveStack.setOnClickListener {
            user.isEnable = false
            Toast.makeText(this, "" + user.toString(), Toast.LENGTH_LONG).show()
        }
        bding.etUserName.addTextChangedListener {
            if (!it.isNullOrEmpty()) {
                user.username = it.toString()
                Toast.makeText(this, "" + user.toString(), Toast.LENGTH_LONG).show()
            }
        }
        bding.btnChangePic.setOnClickListener {
            user.userPicUrl =
                "https://timgsa.baidu.com/timg?iasssmage&quality=80&size=b9999_10000&sec=1605276319950&di=6f1a12cecbdfcb39b038128decb61569&imgtype=0&src=http%3A%2F%2Fattach.bbs.miui.com%2Fforum%2F201303%2F16%2F173710lvx470i4348z6i6z123.jpg"
        }
        bding.btnInvokeLogin.setOnClickListener {
            ARouter.getInstance().build(AroutConfig.A_ACCOUNT_LOGIN).navigation()
        }
        bding.btnInvokeLiveDataTest.setOnClickListener {
            ARouter.getInstance().build(AroutConfig.A_APP_LIVE_DATA_TEST).navigation()
        }
        val errorImg: Drawable = resources.getDrawable(R.drawable.default_pic_error_small)
        bding.errorDraw = errorImg
        val holder: Drawable = resources.getDrawable(R.drawable.default_pic_placeholder_small)
        bding.holder = holder
    }
}