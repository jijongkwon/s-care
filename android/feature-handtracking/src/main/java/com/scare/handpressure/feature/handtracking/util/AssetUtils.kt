package com.scare.handpressure.feature.handtracking.util

import android.content.Context
import java.io.File
import java.io.FileOutputStream

object AssetUtils {
    fun copyAssetToCache(context: Context, assetName: String): String {
        val cacheFile = File(context.cacheDir, assetName)
        if (!cacheFile.exists()) {
            context.assets.open(assetName).use { input ->
                FileOutputStream(cacheFile).use { output ->
                    input.copyTo(output)
                }
            }
        }
        return cacheFile.absolutePath
    }
}