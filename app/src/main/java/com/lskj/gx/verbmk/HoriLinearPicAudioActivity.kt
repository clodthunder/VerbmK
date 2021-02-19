package com.lskj.gx.verbmk

import android.graphics.Color
import android.os.Bundle
import android.webkit.MimeTypeMap
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.lskj.gx.basic_entity.BaseImageEntity
import com.lskj.gx.hori_res.PicUploadDialog
import com.lskj.gx.hori_res.PreViewAudioDialog
import com.lskj.gx.hori_res.RecordAudioDialog
import com.lskj.gx.hori_res.ResSelectorDialog
import com.lskj.gx.lib_common.base.activity.BaseActivity
import com.lskj.gx.lib_common.config.AroutConfig
import com.lskj.gx.lib_common.utils.FileUtil
import com.lskj.gx.verbmk.databinding.ActivityHoriPicAudioBinding
import org.jetbrains.anko.toast
import java.io.File
import java.util.*

/**
 *   创建时间:  2021/2/14
 *   编写人: tzw
 *   功能描述: 测试横向linear
 *   1.图片拍摄 显示
 *   2.音频录制 预览
 */
@Route(path = AroutConfig.A_App_HORI_PIC_AUDIO)
class HoriLinearPicAudioActivity : BaseActivity() {

    private lateinit var bding: ActivityHoriPicAudioBinding
    private val images: ArrayList<BaseImageEntity> = arrayListOf()

    private var recordDialog: RecordAudioDialog? = null
    private var preViewAudioDialog: PreViewAudioDialog? = null
    private var resSelectorDialog: ResSelectorDialog? = null
    private var picUploadDialog: PicUploadDialog? = null

    override fun firstBdLayout() {
        bding = DataBindingUtil.setContentView(this, R.layout.activity_hori_pic_audio)
    }

    override fun afterBdInitParams() {

    }

    override fun afterPamsInitTbar() {

    }

    override fun afterTbarPreData(savedInstanceState: Bundle?) {

    }

    override fun afterPreData(savedInstanceState: Bundle?) {
        bding.ghllTest.setBackGround(Color.WHITE)
        bding.ghllTest.setImages(images)
        bding.btnAdd.setOnClickListener {
            openResSelector()
        }
        bding.btnAudio.setOnClickListener {
            startAudioRecord()
        }
        bding.ghllTest.setOnHorIvDelClickListener { position, entity, iv ->
            toast("删除 $position-> ${entity.toString()}")
            //自己调用删除
            bding.ghllTest.removeEntity(entity)
        }
        bding.ghllTest.setOnHorIvClickListener { position, entity, iv ->
            startPreViewAudio(entity.source as File, entity.duration)
        }
    }

    private fun openResSelector() {
        val args = Bundle()
        // 设置启用的按钮
        val enableBtns = arrayListOf<String>()
        enableBtns.add(ResSelectorDialog.BTN_CAMERA)
        enableBtns.add(ResSelectorDialog.BTN_ABLUM)
//        enableBtns.add(ResSelectorDialog.BTN_VIDEO)
//        enableBtns.add(ResSelectorDialog.BTN_VIDEO_ABLUM)
        enableBtns.add(ResSelectorDialog.BTN_AUDIO)
        enableBtns.add(ResSelectorDialog.BTN_AUDIO_ABLUM)
//        enableBtns.add(ResSelectorDialog.BTN_FILE)
        args.putStringArrayList(ResSelectorDialog.EXTRA_ENABLE_BTNS, enableBtns);

        //不为空更新参数
        resSelectorDialog?.let {
            resSelectorDialog?.updateArgs()
        } ?: let {
            resSelectorDialog = ResSelectorDialog.getInstance(args)
            resSelectorDialog?.setResOkListener(object : ResSelectorDialog.OnInvokeResOkListener {
                override fun onResOk(type: Int, resultFile: File) {
                    resSelectorDialog?.dismiss()
                    uploadPicToServer(resultFile)
                }
            })
        }
        resSelectorDialog?.show(this.supportFragmentManager, "resSelector_dialog")
    }

    //模拟上传文件
    //并更新进度
    private fun uploadPicToServer(cFile: File) {
        openPicUploadDialog(cFile)

    }

    /**
     * 文件上传
     */
    private fun openPicUploadDialog(file: File) {
        val args = Bundle()
        args.putString(PicUploadDialog.EXTRA_FILE_NAME, file.name)
        args.putString(
            PicUploadDialog.EXTRA_FILE_SIZE,
            FileUtil.formatFileSize(this, file.length())
        )
        args.putInt(PicUploadDialog.EXTRA_FILE_C_PROGRESS, 0)
        picUploadDialog?.let {
            picUploadDialog?.updateArgs(args)
        } ?: let {
            picUploadDialog = PicUploadDialog.getInstance(args)
            picUploadDialog?.setOnUploadCancleListener(object :
                PicUploadDialog.OnPicUploadCancleListener {
                override fun onUploadCancle() {
                    picUploadDialog?.dismiss()
                    toast("上传取消")
                    //模拟
                    if (file.exists()) {
                        file.delete()
                    }
                }
            })
        }
        picUploadDialog?.show(this.supportFragmentManager, "pic_upload_dialog")
    }

    /**
     * 启动音频录制
     */
    private fun startAudioRecord() {
        val args = Bundle()
        args.putBoolean(RecordAudioDialog.EXTR_AUTO_START, true)
        args.putString(RecordAudioDialog.EXTR_TITLE, "录制音频...")

        recordDialog?.let {
            recordDialog?.updateArgs(false)
        } ?: let {
            recordDialog = RecordAudioDialog.getInstance(args)
        }
        recordDialog?.show(this.supportFragmentManager, "record_dialog")
        recordDialog?.setRecordAudioBtnClick(object : RecordAudioDialog.OnRecordAudioBtnClick {
            override fun onCancleBtnClick() {
                toast("cancle click")
            }

            override fun onConfimBtnClick(audioFile: File, realMax: Int) {
//                startPreViewAudio(audioFile, realMax)
                addAudioToHori(audioFile, realMax)
            }

            override fun onSpectrumUpdate() {
                toast("onSpectrumUpdate")
            }

            override fun onStartBtnClick() {
                toast("开始录制")
            }
        })
    }

    private fun addAudioToHori(audioFile: File, realMax: Int) {
        val suffix = MimeTypeMap.getFileExtensionFromUrl(audioFile.absolutePath)
        val temp = BaseImageEntity(
            UUID.randomUUID().toString(),
            "", R.drawable.default_audio_pics,
            audioFile,
            suffix,
            realMax
        )
        images.add(temp)
        bding.ghllTest.addEntity(temp)
    }

    private fun startPreViewAudio(audioFile: File, realMax: Int) {
        val args = Bundle()
        args.putString(PreViewAudioDialog.EXTR_TITLE, "音频预览...")
        args.putInt(PreViewAudioDialog.EXTR_MAX_LONG, realMax)
        args.putBoolean(PreViewAudioDialog.EXTR_SHOW_RETRY_BTN, false)
        args.putBoolean(PreViewAudioDialog.EXTR_SHOW_UPLOAD_BTN, false)
        if (audioFile.exists()) {
            args.putBoolean(PreViewAudioDialog.EXTR_ISNET_SOURCE, false)
            args.putString(PreViewAudioDialog.EXTR_URL, audioFile.absolutePath)
        } else {
            args.putString(
                PreViewAudioDialog.EXTR_URL,
                "http://144.34.189.222:9090/audios/5rWL6K-V.mp3"
            )
            args.putBoolean(PreViewAudioDialog.EXTR_ISNET_SOURCE, true)
        }
        preViewAudioDialog?.let {
            preViewAudioDialog?.updateArguments(audioFile.absolutePath, realMax, false)
        } ?: let {
            preViewAudioDialog = PreViewAudioDialog.getInstance(args)
        }
        //初始化
        preViewAudioDialog?.setRecordAudioBtnClick(object :
            PreViewAudioDialog.OnPreViewAudioBtnClick {
            override fun onPreViewBtnClick() {
                toast("preview audio")
            }

            override fun onReRecordClick() {
                startAudioRecord()
            }

            override fun onUploadBtnClick() {
                toast("upload audio")
            }

            override fun onCancleBtnClick() {
                toast("cancle audio")
            }
        })
        preViewAudioDialog?.show(this.supportFragmentManager, "preview_audio")
    }
}