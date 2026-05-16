package com.spint.app.screens._1home.commonUI

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.core.content.FileProvider
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.spint.app.utils.constants.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream


suspend fun shareImageFromUrl(context: Context, imageUrl: String, postTitle: String) {
    try {
        withContext(Dispatchers.IO) { // Run network operations in background
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .allowHardware(false)
                .build()

            val result = (context.imageLoader.execute(request) as SuccessResult).drawable
            val bitmap = (result as BitmapDrawable).bitmap

            // Save bitmap to cache
            val file = File(context.cacheDir, "shared_post.png")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }

            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, postTitle)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Share Post via"))
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun shareEventDeepLink(context: Context, postId: String) {
    val deepLink = "${Constants.APP_NAME}://event/$postId"
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, "Check out this event: $deepLink")
    }
    context.startActivity(Intent.createChooser(intent, "Share Event via"))
}
fun sharePingDeepLink(context: Context, deepLink: String) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, deepLink)
    }
    val shareIntent = Intent.createChooser(sendIntent, "Share Ping")
    context.startActivity(shareIntent)
}
fun shareProfileDeepLink(context: Context, deepLink: String) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, deepLink)
    }
    val shareIntent = Intent.createChooser(sendIntent, "Share Profile")
    context.startActivity(shareIntent)
}



fun sharePostUrl(context: Context, postUrl: String, postTitle: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, "$postTitle\n\n$postUrl")
    }
    context.startActivity(Intent.createChooser(intent, "Share Post via"))
}