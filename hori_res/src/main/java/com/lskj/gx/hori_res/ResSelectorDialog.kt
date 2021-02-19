package com.lskj.gx.hori_res

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.hjq.permissions.XXPermissions.startPermissionActivity
import com.lskj.gx.lib_common.R
import com.lskj.gx.lib_common.dialog.BaseCenterDialogFragment
import com.lskj.gx.lib_common.utils.FileUtil
import org.jetbrains.anko.support.v4.toast
import java.io.File
import java.io.IOException

/**
 *   创建时间:  2021/2/14
 *   编写人: tzw
 *   功能描述: 这里处理
 *   1.相机拍摄、选择照片
 *   2.拍摄视频、选择视频
 */
class ResSelectorDialog : BaseCenterDialogFragment() {
    private var btnCamera: Button? = null
    private var btnAlbum: Button? = null
    private var btnVideo: Button? = null
    private var btnVideoAblum: Button? = null
    private var btnAudioAblum: Button? = null
    private var btnFile: Button? = null
    private var btnAudio: Button? = null
    private lateinit var resOkListener: OnInvokeResOkListener

    //临时文件 不用使用
    private lateinit var resultFile: File

    //照相机请求
    private val REQUEST_OPEN_CAMERA: Int = 101

    //调用系统相册
    private val REQUEST_OPEN_ALBUM: Int = 102

    //调用系统录制视频
    private val REQUEST_OPEN_VIDEO: Int = 103

    //调用系统选择视频
    private val REQUEST_OPEN_VIDEO_ALBUM: Int = 104

    //调用系统选择文件
    private val REQUEST_OPEN_FILE: Int = 105

    companion object {
        const val BTN_CAMERA: String = "btn_camera"
        const val BTN_ABLUM: String = "btn_ablum"
        const val BTN_VIDEO: String = "btn_video"
        const val BTN_VIDEO_ABLUM: String = "btn_video_ablum"
        const val BTN_AUDIO: String = "btn_audio"
        const val BTN_AUDIO_ABLUM: String = "btn_audio_ablum"
        const val BTN_FILE: String = "btn_file"

        //传入启用的按钮 默认全部禁用
        const val EXTRA_ENABLE_BTNS: String = "extra_btn_enables"
        private var instance: ResSelectorDialog? = null

        //const val EXTR_TITLE: String = "extra_title"
        //获取当前实例
        fun getInstance(
            bundle: Bundle
        ): ResSelectorDialog? {
            if (instance == null) {
                synchronized(ResSelectorDialog::class.java) {
                    if (instance == null) {
                        instance = ResSelectorDialog()
                        instance?.arguments = bundle
                    }
                }
            }
            return instance
        }
    }

    fun setResOkListener(listener: OnInvokeResOkListener) {
        this.resOkListener = listener
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(true)
            dialog.setCancelable(true)
        }
    }

    //更新bundle 参数信息
    fun updateArgs() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root: View = LayoutInflater.from(mContxt)
            .inflate(R.layout.dialog_take_pic_video, container, false)
        btnCamera = root.findViewById(R.id.btn_invoke_camera)
        btnAlbum = root.findViewById(R.id.btn_invoke_album)
        btnVideo = root.findViewById(R.id.btn_invoke_video)
        btnVideoAblum = root.findViewById(R.id.btn_invoke_v_album)
        btnAudio = root.findViewById(R.id.btn_invoke_audio)
        btnAudioAblum = root.findViewById(R.id.btn_invoke_a_audio)
        btnFile = root.findViewById(R.id.btn_invoke_f_album)

        btnCamera?.setOnClickListener {
            checkCameraPermission()
        }
        btnAlbum?.setOnClickListener {
            checkAlbumPermission()
        }
        btnVideo?.setOnClickListener {
            // TODO: 2021/2/19 实现视频录制
        }
        btnVideoAblum?.setOnClickListener {
            // TODO: 2021/2/19 实现视频选择
        }
        btnAudio?.setOnClickListener {

            //调用音频
        }
        btnAudioAblum?.setOnClickListener {

            //选择音频
        }
        root.findViewById<Button>(R.id.btn_invoke_close).setOnClickListener {
            dismiss()
        }
        btnFile?.setOnClickListener { }
        return root
    }

    /**
     * 调用拍照功能
     */
    fun checkCameraPermission() {
        //1.判断是否有权限
        XXPermissions.with(mContxt).permission(Manifest.permission.CAMERA)
            .permission(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                    if (all) {
                        invokeCamera()
                    }
                }

                override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                    super.onDenied(permissions, never)
                    if (never) {
                        toast("已拒绝相册相关权限权限")
                        // 如果是被永久拒绝就跳转到应用权限系统设置页面
                        startPermissionActivity(mContxt, permissions);
                    } else {
                        toast("获取相册相关权限失败")
                    }
                }
            })
    }

    /**
     * intent 调用系统相机
     */
    private fun invokeCamera() {
        val openCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //这句作用是如果没有相机则该应用不会闪退,要是不加这句则当系统没有相机应用的时候该应用会闪退
        if (openCameraIntent.resolveActivity(mContxt.packageManager) != null) {
            try {
                resultFile = FileUtil.createImageFile(mContxt, FileUtil.TYPE_PIC)
                if (resultFile != null) {
                    var mImageUri: Uri? = null
                    mImageUri = FileUtil.getFileUri(mContxt, resultFile)
                    openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
                    startActivityForResult(
                        openCameraIntent,
                        REQUEST_OPEN_CAMERA
                    )
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * intent 调用系统相册
     */
    private fun invokeAlbum() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*" //从所有图片中进行选择
        startActivityForResult(
            intent,
            REQUEST_OPEN_ALBUM
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //照相相册 删除空文件
        if (resultCode == Activity.RESULT_CANCELED
            && (requestCode == REQUEST_OPEN_CAMERA ||
                    requestCode == REQUEST_OPEN_ALBUM ||
                    requestCode == REQUEST_OPEN_VIDEO ||
                    requestCode == REQUEST_OPEN_VIDEO_ALBUM ||
                    requestCode == REQUEST_OPEN_FILE)
        ) {
            if (resultFile.exists()) {
                resultFile.delete()
            }
            return
        }
        //相机回调
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_OPEN_CAMERA) {
            //修正后的图片
            resultFile = FileUtil.rePairImgRotate(mContxt, resultFile, true)
            resOkListener.onResOk(REQUEST_OPEN_CAMERA, resultFile)
            return
        }
        //相册回调
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_OPEN_ALBUM) {
            data.let {
                val imgUri: Uri? = it?.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor: Cursor? = imgUri?.let { uri ->
                    mContxt.contentResolver
                        .query(uri, filePathColumn, null, null, null)
                }
                cursor?.moveToFirst()
                val columnIndex = cursor!!.getColumnIndex(filePathColumn[0])
                //从系统表中查询指定Uri对应的照片
                resultFile = File(cursor.getString(columnIndex))
                //从系统表中查询指定Uri对应的照片
                resultFile = FileUtil.rePairImgRotate(mContxt, resultFile, false)
                resOkListener.onResOk(REQUEST_OPEN_CAMERA, resultFile)
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    /*
        判断是否有权限读取 相册
     */
    private fun checkAlbumPermission() {
        XXPermissions.with(mContxt)
            .permission(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                    if (all) {
                        invokeAlbum()
                    }
                }

                override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                    super.onDenied(permissions, never)
                    if (never) {
                        toast("已拒绝拍照相关权限权限")
                        // 如果是被永久拒绝就跳转到应用权限系统设置页面
                        startPermissionActivity(mContxt, permissions);
                    } else {
                        toast("获取拍照相关权限失败")
                    }
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        enableBtns(args)
    }

    private fun enableBtns(args: Bundle?) {
        disableBtns()
        if (args != null) {
            val enableBtns = args.getStringArrayList(EXTRA_ENABLE_BTNS)
            if (enableBtns != null) {
                for (cur in enableBtns) {
                    if (BTN_CAMERA.equals(cur)) {
                        btnCamera?.visibility = View.VISIBLE
                        continue
                    } else if (BTN_ABLUM.equals(cur)) {
                        btnAlbum?.visibility = View.VISIBLE
                        continue
                    } else if (BTN_VIDEO.equals(cur)) {
                        btnVideo?.visibility = View.VISIBLE
                        continue
                    } else if (BTN_VIDEO_ABLUM.equals(cur)) {
                        btnVideoAblum?.visibility = View.VISIBLE
                        continue
                    } else if (BTN_AUDIO.equals(cur)) {
                        btnAudio?.visibility = View.VISIBLE
                        continue
                    } else if (BTN_AUDIO_ABLUM.equals(cur)) {
                        btnAudioAblum?.visibility = View.VISIBLE
                        continue
                    } else if (BTN_FILE.equals(cur)) {
                        btnFile?.visibility = View.VISIBLE
                        continue
                    }
                }
            }
        }
    }

    /**
     * 加载资源完成 回调
     */
    interface OnInvokeResOkListener {
        fun onResOk(type: Int, resultFile: File)
    }

    private fun disableBtns() {
        btnCamera?.visibility = View.GONE
        btnAlbum?.visibility = View.GONE
        btnVideo?.visibility = View.GONE
        btnVideoAblum?.visibility = View.GONE
        btnAudio?.visibility = View.GONE
        btnAudioAblum?.visibility = View.GONE
        btnFile?.visibility = View.GONE
    }

    fun dp2px(dpValue: Int): Int {
        val scale: Float = mContxt.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}