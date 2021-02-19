package com.lskj.gx.lib_common.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import com.lskj.gx.lib_common.config.AppContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 *   创建时间:  2021/2/14
 *   编写人: tzw
 *   功能描述: 文件大小格式化
 */
class FileUtil {
    companion object {
        const val TYPE_AUDIO = "audio"
        const val TYPE_PIC = "pic"

        fun createImageFile(context: Context, type: String): File {
            var houzhui: String? = null
            if (TYPE_AUDIO == type) {
                houzhui = ".mp3";
            } else if (TYPE_PIC == type) {
                houzhui = ".jpg"
            }
            val timeStamp = SimpleDateFormat("yyyy_MM_dd_HHmmss").format(Date())
            //本地文件路径
            val tempFile: File? =
                if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                    File(context.getExternalFilesDir(type), "$timeStamp$houzhui")
                } else {
                    File(
                        context.filesDir.toString() + File.separator + "pic",
                        "$timeStamp$houzhui"
                    )
                }
            if (tempFile?.exists()!!) {
                tempFile.delete()
            }
            //文件
            return File.createTempFile(
                timeStamp,
                "$houzhui",
                tempFile.parentFile?.absoluteFile
            )
        }

        fun getFileUri(context: Context, file: File): Uri {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                /*7.0以上要通过FileProvider将File转化为Uri*/
                FileProvider.getUriForFile(
                    context,
                    AppContext.instance.getContext().packageName
                        .toString() + ".fileprovider",
                    file
                )
            } else {
                /*7.0以下则直接使用Uri的fromFile方法将File转化为Uri*/
                Uri.fromFile(file)
            }
        }

        fun rePairImgRotate(context: Context, cFile: File, isCamera: Boolean): File {
            val newFile: File = FileUtil.createImageFile(context, TYPE_PIC)
            var bitMap: Bitmap = BitmapFactory.decodeFile(cFile.absolutePath)
            val angle = readPictureDegree(cFile)
            bitMap = rotaingImageView(angle, bitMap)
            val bos = FileOutputStream(newFile)
            bitMap.compress(Bitmap.CompressFormat.JPEG, 75, bos)
            bos.flush()
            bos.close()
            if (!bitMap.isRecycled) {
                bitMap.recycle()
            }
            //相机需要删除
            if (isCamera) {
                cFile.delete()
            }
            return newFile
        }

        fun rotaingImageView(angle: Int, bitmap: Bitmap): Bitmap {
            var returnBm: Bitmap? = null
            // 根据旋转角度，生成旋转矩阵
            // 根据旋转角度，生成旋转矩阵
            val matrix = Matrix()
            matrix.postRotate(angle.toFloat())
            try {
                // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
                returnBm =
                    Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            } catch (e: OutOfMemoryError) {
            }
            if (returnBm == null) {
                returnBm = bitmap
            }
            return returnBm
        }

        //读取照片旋转角度
        fun readPictureDegree(cFile: File): Int {
            var degree = 0
            try {
                val exifInterface: ExifInterface =
                    if (Build.VERSION.SDK_INT
                        >= Build.VERSION_CODES.Q
                    ) {
                        ExifInterface(cFile)
                    } else {
                        ExifInterface(cFile.absolutePath)
                    }
                val orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                    ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                    ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return degree
        }

        fun formatFileSize(context: Context?, roundedBytes: Long): String? {
            return formatFileSize(context, roundedBytes, false, Locale.US)
        }

        fun formatFileSize(context: Context?, roundedBytes: Long, locale: Locale): String? {
            return formatFileSize(context, roundedBytes, false, locale)
        }

        private fun formatFileSize(
            context: Context?,
            roundedBytes: Long,
            shorter: Boolean,
            locale: Locale
        ): String? {
            if (context == null) {
                return ""
            }
            var result = roundedBytes.toFloat()
            var suffix = "B"
            if (result > 900) {
                suffix = "KB"
                result /= 1024
            }
            if (result > 900) {
                suffix = "MB"
                result /= 1024
            }
            if (result > 900) {
                suffix = "GB"
                result /= 1024
            }
            if (result > 900) {
                suffix = "TB"
                result /= 1024
            }
            if (result > 900) {
                suffix = "PB"
                result /= 1024
            }
            val value: String
            if (result < 1) {
                value = java.lang.String.format(locale, "%.2f", result)
            } else if (result < 10) {
                if (shorter) {
                    value = java.lang.String.format(locale, "%.1f", result)
                } else {
                    value = java.lang.String.format(locale, "%.2f", result)
                }
            } else if (result < 100) {
                if (shorter) {
                    value = java.lang.String.format(locale, "%.0f", result)
                } else {
                    value = java.lang.String.format(locale, "%.2f", result)
                }
            } else {
                value = java.lang.String.format(locale, "%.0f", result)
            }
            return String.format("%s%s", value, suffix)
        }
    }
}