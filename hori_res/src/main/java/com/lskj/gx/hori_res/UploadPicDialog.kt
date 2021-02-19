package com.lskj.gx.hori_res

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.lskj.gx.basic_widget.NumberProgressBar
import com.lskj.gx.lib_common.dialog.BaseCenterDialogFragment

/**
 *   创建时间:  2021/2/19
 *   编写人: tzw
 *   功能描述:图片文件上传进度提示 和音频 区分因为加压解压方式不一致
 */
class PicUploadDialog : BaseCenterDialogFragment() {
    var npbBar: NumberProgressBar? = null
    var tvFileName: TextView? = null
    var tvFileSize: TextView? = null
    var btnCancle: Button? = null
    var uploadListener: OnPicUploadCancleListener? = null

    companion object {
        private var instance: PicUploadDialog? = null

        const val EXTRA_FILE_NAME: String = "extra_file_name"
        const val EXTRA_FILE_SIZE: String = "extra_file_size"
        const val EXTRA_FILE_C_PROGRESS: String = "extra_c_progress"

        fun getInstance(
            bundle: Bundle
        ): PicUploadDialog? {
            if (instance == null) {
                synchronized(PicUploadDialog::class.java) {
                    if (instance == null) {
                        instance = PicUploadDialog()
                        instance?.arguments = bundle
                    }
                }
            }
            return instance
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = View.inflate(mContxt, R.layout.dialog_upload_pic, container)
        npbBar = view.findViewById<NumberProgressBar>(R.id.npb_upload)
        tvFileName = view.findViewById<TextView>(R.id.tv_file_name)
        tvFileSize = view.findViewById<TextView>(R.id.tv_file_size)
        btnCancle = view.findViewById<Button>(R.id.btn_cancle_upload)
        btnCancle?.setOnClickListener {
            uploadListener?.onUploadCancle()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reset()
        initData()
    }

    fun initData() {
        val args = arguments
        if (args != null) {
            val fileName = args.getString(EXTRA_FILE_NAME)
            if (!TextUtils.isEmpty(fileName)) {
                tvFileName?.let {
                    it.text = fileName
                }
            }
            val fileSize = args.getString(EXTRA_FILE_SIZE)
            if (!TextUtils.isEmpty(fileSize)) {
                tvFileSize?.let {
                    it.text = fileSize
                }
            }
            val cProgress = args.getInt(EXTRA_FILE_C_PROGRESS, 0)
            npbBar?.let {
                it.progress = cProgress
            }
        }
    }

    fun setOnUploadCancleListener(listener: OnPicUploadCancleListener) {
        this.uploadListener = listener

    }

    //更新
    fun updateArgs(args: Bundle) {
        arguments?.let {
            arguments = args
            reset()
            initData()
        }
    }

    fun uploadProgress(cProgress: Int) {
        arguments?.let {
            it.putInt(EXTRA_FILE_SIZE, cProgress)
            npbBar?.let { it.progress = cProgress }
        }
    }

    fun reset() {
        npbBar.let {
            it?.progress = 0
        }
        tvFileName.let {
            it?.text = ""
        }
        tvFileSize.let {
            it?.text = "0b"
        }
    }

    interface OnPicUploadCancleListener {
        fun onUploadCancle()
    }
}