package com.lskj.gx.verbmk.test

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.lskj.gx.lib_common.config.AroutConfig
import com.lskj.gx.verbmk.R
import com.lskj.gx.verbmk.databinding.ActivityAdapterDbTestBinding
import com.lskj.gx.verbmk.entity.User

/**
 *   创建时间:  2020/11/16
 *   编写人: tzw
 *   功能描述:
 */
@Route(path = AroutConfig.A_APP_ADAPTER_DBDING_TEST)
class Adapte_dataBanding_test : AppCompatActivity() {
    //视图绑定
    private lateinit var bding: ActivityAdapterDbTestBinding
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bding = DataBindingUtil.setContentView(this, R.layout.activity_adapter_db_test)
        user = User()
        user.isEnable = false
        user.username = "tzw"
        user.userPwd = "pwd"
        user.userPicUrl =
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1605276319951&di=80b09dd5a484a869e57c206e6a4fde16&imgtype=0&src=http%3A%2F%2Fimg.article.pchome.net%2F00%2F34%2F09%2F06%2Fpic_lib%2Fs960x639%2Fjmfj061s960x639.jpg"

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
        val errorImg: Drawable = resources.getDrawable(R.drawable.default_pic_error_small)
        bding.errorDraw = errorImg
        val holder: Drawable = resources.getDrawable(R.drawable.default_pic_placeholder_small)
        bding.holder = holder
        bding.user = this.user
    }
}