package com.example.finalapp.screens.common

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint

fun applyFilter(bitmap: Bitmap, filter: FilterType): Bitmap? {
    return when (filter) {
        FilterType.Original -> bitmap
        FilterType.Grayscale -> applyGrayscale(bitmap)
        FilterType.Sepia -> applySepia(bitmap)
        FilterType.Warm -> adjustWarmth(bitmap, factor = 1.2f)
        FilterType.Cool -> adjustCoolness(bitmap, factor = 1.2f)
        FilterType.Vintage -> applyVintage(bitmap)
        FilterType.Bright -> adjustBrightness(bitmap, 30)
        FilterType.Dark -> adjustBrightness(bitmap, -30)
        FilterType.Contrast -> adjustContrast(bitmap, 1.5f)
        FilterType.Soft -> applySoftFocus(bitmap)
        FilterType.Sharpen -> applySharpen(bitmap)
        FilterType.Blur -> applyBlur(bitmap)
        FilterType.BW -> applyBw(bitmap)
        FilterType.Teal -> tint(bitmap, Color.CYAN)
        FilterType.Rose -> tint(bitmap, Color.MAGENTA)
        FilterType.Sunset -> applySunsetTone(bitmap)
        FilterType.Golden -> tint(bitmap, Color.rgb(255, 215, 0))
        FilterType.Deep -> adjustContrast(bitmap, 1.8f)
        FilterType.Film -> applyFilmLook(bitmap)
        FilterType.Neon -> applyNeonGlow(bitmap)

    }
}
fun applyGrayscale(src: Bitmap): Bitmap {
    val bmp = Bitmap.createBitmap(src.width, src.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)
    val paint = Paint()
    val cm = ColorMatrix()
    cm.setSaturation(0f)
    paint.colorFilter = ColorMatrixColorFilter(cm)
    canvas.drawBitmap(src, 0f, 0f, paint)
    return bmp
}

fun applySepia(src: Bitmap): Bitmap {
    val bmp = Bitmap.createBitmap(src.width, src.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)
    val paint = Paint()
    val cm = ColorMatrix()
    cm.setSaturation(0f)

    val sepiaMatrix = ColorMatrix()
    sepiaMatrix.setScale(1f, 1f, 0.8f, 1f)
    cm.postConcat(sepiaMatrix)

    paint.colorFilter = ColorMatrixColorFilter(cm)
    canvas.drawBitmap(src, 0f, 0f, paint)
    return bmp
}

fun adjustBrightness(src: Bitmap, value: Int): Bitmap? {
    val bmp = src.config?.let { Bitmap.createBitmap(src.width, src.height, it) }
    val canvas = bmp?.let { Canvas(it) }
    val paint = Paint()
    val cm = ColorMatrix()
    cm.set(
        floatArrayOf(
            1f, 0f, 0f, 0f, value.toFloat(),
            0f, 1f, 0f, 0f, value.toFloat(),
            0f, 0f, 1f, 0f, value.toFloat(),
            0f, 0f, 0f, 1f, 0f
        )
    )
    paint.colorFilter = ColorMatrixColorFilter(cm)
    if (canvas != null) {
        canvas.drawBitmap(src, 0f, 0f, paint)
    }
    return bmp
}

fun adjustContrast(src: Bitmap, contrast: Float): Bitmap? {
    val bmp = src.config?.let { Bitmap.createBitmap(src.width, src.height, it) }
    val canvas = bmp?.let { Canvas(it) }
    val paint = Paint()
    val scale = contrast
    val translate = (-0.5f * scale + 0.5f) * 255f
    val cm = ColorMatrix(
        floatArrayOf(
            scale, 0f, 0f, 0f, translate,
            0f, scale, 0f, 0f, translate,
            0f, 0f, scale, 0f, translate,
            0f, 0f, 0f, 1f, 0f
        )
    )
    paint.colorFilter = ColorMatrixColorFilter(cm)
    if (canvas != null) {
        canvas.drawBitmap(src, 0f, 0f, paint)
    }
    return bmp
}
fun adjustWarmth(bitmap: Bitmap, factor: Float): Bitmap = bitmap
fun adjustCoolness(bitmap: Bitmap, factor: Float): Bitmap = bitmap
fun applyVintage(bitmap: Bitmap): Bitmap = bitmap
fun applySoftFocus(bitmap: Bitmap): Bitmap = bitmap
fun applySharpen(bitmap: Bitmap): Bitmap = bitmap
fun applyBlur(bitmap: Bitmap): Bitmap = bitmap
fun applyBw(bitmap: Bitmap): Bitmap = bitmap
fun tint(bitmap: Bitmap, color: Int): Bitmap = bitmap
fun applySunsetTone(bitmap: Bitmap): Bitmap = bitmap
fun applyFilmLook(bitmap: Bitmap): Bitmap = bitmap
fun applyNeonGlow(bitmap: Bitmap): Bitmap = bitmap