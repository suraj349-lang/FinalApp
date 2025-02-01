package com.example.finalapp.screens.qrcode

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun QRCode(username:String) {
    Image(
        painter = rememberQrBitmapPainter("https://www.google.com"),
        contentDescription = "QR code for username",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.size(250.dp),
    )
//    Image(
//        painter = rememberQrBitmapPainterWithIcon("dskjgbdjkbgjkasdbgksb skjdbgkjadg assgbsjakdbgsadbgkgnsad gkjberjbgfsgkj  skjjgkja"),
//        contentDescription = "QR code for username",
//        contentScale = ContentScale.FillBounds,
//        modifier = Modifier.size(250.dp),
//    )

}


@Composable
fun rememberQrBitmapPainter(
    content: String,
    size: Dp = 150.dp,
    padding: Dp = 0.dp
): BitmapPainter {
    val density = LocalDensity.current
    val sizePx = with(density) { size.roundToPx() }
    val paddingPx = with(density) { padding.roundToPx() }

    var bitmap by remember(content) {
        mutableStateOf<Bitmap?>(null)
    }

    LaunchedEffect(content) {
        withContext(Dispatchers.IO) {
            try {
                val qrCodeWriter = QRCodeWriter()

                val encodeHints = mapOf<EncodeHintType, Any>(
                    EncodeHintType.MARGIN to paddingPx // Adjust padding for the QR code
                )

                val bitMatrix = qrCodeWriter.encode(
                    content,
                    BarcodeFormat.QR_CODE,
                    sizePx,
                    sizePx,
                    encodeHints
                )

                val newBitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
                for (x in 0 until sizePx) {
                    for (y in 0 until sizePx) {
                        val pixelColor = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
                        newBitmap.setPixel(x, y, pixelColor)
                    }
                }

                bitmap = newBitmap
            } catch (e: Exception) {
                e.printStackTrace() // Log the error
                bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888).apply {
                    eraseColor(Color.TRANSPARENT)
                }
            }
        }
    }

    return remember(bitmap) {
        val currentBitmap = bitmap ?: Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888).apply {
            eraseColor(Color.TRANSPARENT)
        }
        BitmapPainter(currentBitmap.asImageBitmap())
    }
}


@Composable
fun rememberQrBitmapPainterWithIcon(
    content: String,
    size: Dp = 150.dp,
    padding: Dp = 0.dp,
    overlay: (@Composable (Canvas) -> Unit)? = null // Optional overlay
): BitmapPainter {
    val density = LocalDensity.current
    val sizePx = with(density) { size.roundToPx() }
    val paddingPx = with(density) { padding.roundToPx() }

    var bitmap by remember(content) {
        mutableStateOf<Bitmap?>(null)
    }

    LaunchedEffect(content) {
        withContext(Dispatchers.IO) {
            try {
                val qrCodeWriter = QRCodeWriter()

                val encodeHints = mapOf<EncodeHintType, Any>(
                    EncodeHintType.MARGIN to paddingPx
                )

                val bitMatrix = qrCodeWriter.encode(
                    content,
                    BarcodeFormat.QR_CODE,
                    sizePx,
                    sizePx,
                    encodeHints
                )

                val qrBitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
                for (x in 0 until sizePx) {
                    for (y in 0 until sizePx) {
                        val pixelColor = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
                        qrBitmap.setPixel(x, y, pixelColor)
                    }
                }

                // Overlay text or image on the QR code
                val finalBitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(finalBitmap)
                canvas.drawBitmap(qrBitmap, 0f, 0f, null)

                // Add text or image overlay
                val paint = Paint().apply {
                    color = android.graphics.Color.RED
                    textSize = sizePx / 10f
                    textAlign = Paint.Align.CENTER
                    isAntiAlias = true
                }
                // Example: Draw text
                canvas.drawText("LOGO", sizePx / 2f, sizePx / 2f, paint)

                // Optionally, overlay an icon
                val iconSize = sizePx / 4
                val iconBitmap = Bitmap.createBitmap(iconSize, iconSize, Bitmap.Config.ARGB_8888)
                val iconCanvas = Canvas(iconBitmap)
                iconCanvas.drawColor(android.graphics.Color.BLUE) // Replace with actual icon logic
                canvas.drawBitmap(
                    iconBitmap,
                    (sizePx - iconSize) / 2f,
                    (sizePx - iconSize) / 2f,
                    null
                )

                bitmap = finalBitmap
            } catch (e: Exception) {
                e.printStackTrace()
                bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888).apply {
                    eraseColor(Color.TRANSPARENT)
                }
            }
        }
    }

    return remember(bitmap) {
        val currentBitmap = bitmap ?: Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888).apply {
            eraseColor(Color.TRANSPARENT)
        }
        BitmapPainter(currentBitmap.asImageBitmap())
    }
}
