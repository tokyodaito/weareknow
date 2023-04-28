package com.bogsnebes.weareknow.accessibility.utils

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityService.ScreenshotResult
import android.accessibilityservice.AccessibilityService.TakeScreenshotCallback
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream


@RequiresApi(Build.VERSION_CODES.R)
class ScreenshotCallBack(
    private val context: Context,
    private val compressionQuality: Int,
    private val rect: Rect? = null
) : TakeScreenshotCallback {
    override fun onSuccess(p0: ScreenshotResult) {
        try {
            var bitmap = Bitmap.wrapHardwareBuffer(p0.hardwareBuffer, p0.colorSpace) ?: return
            rect?.let {
                bitmap = cropBitmap(bitmap, rect)
            }
            val folder = File(context.filesDir, "images")
            folder.mkdirs()

            val file = File(folder, SystemClock.elapsedRealtime().toString())

            //ToDo добавить в ДБ ссылку на файл
            FileOutputStream(file).use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, compressionQuality, it)
            }
            Log.e("ScreenshotCallBack", "Скриншот успешно сохранен")
        } catch (ex: Exception) {
            Log.e("ScreenshotCallBack", "Не возможно получить скриншот", ex)
        }
    }

    override fun onFailure(p0: Int) {
        Log.e("ScreenshotCallBack", "Не возможно получить скриншот")
    }

    private fun cropBitmap(bitmap: Bitmap, rect: Rect): Bitmap {
        if (rect.left < 0 || rect.top < 0 || rect.right > bitmap.width || rect.bottom > bitmap.height) {
            Log.e("ScreenshotCallBack", "Область обрезки выходит за пределы изображения")
            return bitmap
        }

        val croppedBitmap = Bitmap.createBitmap(rect.width(), rect.height(), bitmap.config)
        for (y in 0 until rect.height()) {
            for (x in 0 until rect.width()) {
                val pixel = bitmap.getPixel(rect.left + x, rect.top + y)
                croppedBitmap.setPixel(x, y, pixel)
            }
        }

        return croppedBitmap
    }

}

object AcsUtils {
    @SuppressLint("NewApi")
    fun takeScreenshot(
        service: AccessibilityService, context: Context,
        callBack: ScreenshotCallBack
    ) {
        service.takeScreenshot(0, context.mainExecutor, callBack)
    }
}