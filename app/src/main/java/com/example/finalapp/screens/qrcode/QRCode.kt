package com.example.finalapp.screens.qrcode

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.finalapp.R
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
//
//@Composable
//fun QRCode(userId:String) {
//    val baseUrl="http://www.spint.com/profile/"
//    Image(
//        painter = rememberQrBitmapPainter(baseUrl+userId),
//        contentDescription = "QR code for userID",
//        contentScale = ContentScale.FillBounds,
//        modifier = Modifier.size(250.dp).clip(shape = RoundedCornerShape(12.dp)),
//       // colorFilter = ColorFilter.tint(color = androidx.compose.ui.graphics.Color(0xFFDCE40A))
//    )
////    Image(
////        painter = rememberQrBitmapPainterWithIcon("dskjgbdjkbgjkasdbgksb skjdbgkjadg assgbsjakdbgsadbgkgnsad gkjberjbgfsgkj  skjjgkja"),
////        contentDescription = "QR code for username",
////        contentScale = ContentScale.FillBounds,
////        modifier = Modifier.size(250.dp),
////    )
//
//}
//
//
//@Composable
//fun rememberQrBitmapPainter(
//    content: String,
//    size: Dp = 150.dp,
//    padding: Dp = 0.dp
//): BitmapPainter {
//    val density = LocalDensity.current
//    val sizePx = with(density) { size.roundToPx() }
//    val paddingPx = with(density) { padding.roundToPx() }
//
//    var bitmap by remember(content) {
//        mutableStateOf<Bitmap?>(null)
//    }
//
//    LaunchedEffect(content) {
//        withContext(Dispatchers.IO) {
//            try {
//                val qrCodeWriter = QRCodeWriter()
//
//                val encodeHints = mapOf<EncodeHintType, Any>(
//                    EncodeHintType.MARGIN to paddingPx // Adjust padding for the QR code
//                )
//
//                val bitMatrix = qrCodeWriter.encode(
//                    content,
//                    BarcodeFormat.QR_CODE,
//                    sizePx,
//                    sizePx,
//                    encodeHints
//                )
//
//                val newBitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
//                for (x in 0 until sizePx) {
//                    for (y in 0 until sizePx) {
//                        val pixelColor = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
//                        newBitmap.setPixel(x, y, pixelColor)
//                    }
//                }
//
//                bitmap = newBitmap
//            } catch (e: Exception) {
//                e.printStackTrace() // Log the error
//                bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888).apply {
//                    eraseColor(Color.TRANSPARENT)
//                }
//            }
//        }
//    }
//
//    return remember(bitmap) {
//        val currentBitmap = bitmap ?: Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888).apply {
//            eraseColor(Color.TRANSPARENT)
//        }
//        BitmapPainter(currentBitmap.asImageBitmap())
//    }
//}
@Composable
fun rememberQrBitmapPainter(
    content: String,
    size: Dp = 150.dp,
    padding: Dp = 0.dp,
    qrColor: Color = Color(0xFF121212),
    backgroundColor: Color = Color(0XFFFFFFFF)
): BitmapPainter {
    val density = LocalDensity.current
    val sizePx = with(density) { size.roundToPx() }
    val paddingPx = with(density) { padding.roundToPx() }

    var bitmap by remember(content, qrColor, backgroundColor) {
        mutableStateOf<Bitmap?>(null)
    }

    LaunchedEffect(content, qrColor, backgroundColor) {
        withContext(Dispatchers.IO) {
            try {
                val qrCodeWriter = QRCodeWriter()

                val encodeHints = mapOf<EncodeHintType, Any>(
                    EncodeHintType.MARGIN to paddingPx,
                    EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.H
                )

                val bitMatrix = qrCodeWriter.encode(
                    content,
                    BarcodeFormat.QR_CODE,
                    sizePx,
                    sizePx,
                    encodeHints
                )

                val newBitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)

                val qrColorInt = qrColor.toArgb()
                val bgColorInt = backgroundColor.toArgb()

                for (x in 0 until sizePx) {
                    for (y in 0 until sizePx) {
                        val pixelColor = if (bitMatrix[x, y]) qrColorInt else bgColorInt
                        newBitmap.setPixel(x, y, pixelColor)
                    }
                }

                bitmap = newBitmap
            } catch (e: Exception) {
                e.printStackTrace()
                bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888).apply {
                    eraseColor(android.graphics.Color.TRANSPARENT)
                }
            }
        }
    }

    return remember(bitmap) {
        val currentBitmap = bitmap ?: Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888).apply {
            eraseColor(android.graphics.Color.TRANSPARENT)
        }
        BitmapPainter(currentBitmap.asImageBitmap())
    }
}

@Composable
fun QRCode(userIMAGE:String,userId: String) {
    val qrPainter = rememberQrBitmapPainter("https://spint.com/profile/$userId", qrColor = Color.Black, backgroundColor = Color(0xFFE9F0F7).copy(alpha = 0.8f) )//0xFFEEF707

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.background(color = Color.White, shape = RoundedCornerShape(12.dp))
            .size(250.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        Image(
            painter = qrPainter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize().padding(8.dp),
            contentScale = ContentScale.FillBounds,

        )

        // Center icon overlay
        AsyncImage(
            model = imagePrefix+userIMAGE, // your app icon
            contentDescription = "Center icon",
            contentScale=ContentScale.Crop,
            modifier = Modifier.clip(CircleShape)
                .size(48.dp).border(width = 1.dp, color = Color(0xFFEEF707), shape = CircleShape)

        )
    }
}

