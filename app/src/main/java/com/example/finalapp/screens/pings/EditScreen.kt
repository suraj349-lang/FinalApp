package com.example.finalapp.screens.pings

import android.graphics.RenderEffect
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.navigation.SCREENS
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import android.graphics.Shader
import android.os.Build
import android.widget.Space
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.finalapp.utils.constants.Constants
import kotlin.math.roundToInt
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize


data class DraggableText(
    val id: Int,
    var text: String,
    var color: Color,
    var offsetX: Float,
    var offsetY: Float,
    var fontSize: TextUnit = 20.sp,
    var isBold: Boolean = false,
    var isItalic: Boolean = false,
    var scale: Float = 1f,
    var rotation: Float = 0f
)
enum class ImageFilter {
    NONE, GRAYSCALE, SEPIA, INVERT, BRIGHTNESS, CONTRAST
}


@Composable
fun EditScreen(navHostController: NavHostController,onCropFinished: (Rect) -> Unit = {}) {
    var isAddText by remember {
        mutableStateOf(false)
    }
    var showFilter by remember {
        mutableStateOf(true)
    }
    var crop by remember {
        mutableStateOf(false)
    }
    var showSlider by remember {
        mutableStateOf(false)
    }

    val context= LocalContext.current
    var imageBitmap = remember {
        BitmapFactory.decodeResource(context.resources, R.drawable.profile_image_3)
    }

    val bitmap = remember {
        BitmapFactory.decodeResource(context.resources, R.drawable.profile_image_3)
    }
    val imageSize = remember { IntSize(bitmap.width, bitmap.height) }

    var cropMode by remember { mutableStateOf(true) }
    var cropRect by remember {
        mutableStateOf(
            Rect(
                imageSize.width / 4,
                imageSize.height / 4,
                imageSize.width * 3 / 4,
                imageSize.height * 3 / 4
            )
        )
    }


    var selectedFilter by remember { mutableStateOf(ImageFilter.NONE) }
    val colorMatrix = when (selectedFilter) {
        ImageFilter.GRAYSCALE -> ColorMatrix().apply { setToSaturation(0f) }
        ImageFilter.SEPIA -> ColorMatrix().apply {
            setToScale(1f, 1f, 0.8f, 1f)
            val sepiaMatrix = floatArrayOf(
                0.393f, 0.769f, 0.189f, 0f, 0f,
                0.349f, 0.686f, 0.168f, 0f, 0f,
                0.272f, 0.534f, 0.131f, 0f, 0f,
                0f,     0f,     0f,    1f, 0f
            )
            set(ColorMatrix(sepiaMatrix))
        }
        ImageFilter.INVERT -> ColorMatrix(floatArrayOf(
            -1f, 0f, 0f, 0f, 255f,
            0f, -1f, 0f, 0f, 255f,
            0f, 0f, -1f, 0f, 255f,
            0f, 0f, 0f, 1f, 0f
        ))
        ImageFilter.BRIGHTNESS -> ColorMatrix().apply {
            setToScale(1.2f, 1.2f, 1.2f, 1f)
        }
        ImageFilter.CONTRAST -> ColorMatrix().apply {
            val contrast = 1.5f
            set(ColorMatrix(floatArrayOf(
                contrast, 0f, 0f, 0f, -128f * (contrast - 1),
                0f, contrast, 0f, 0f, -128f * (contrast - 1),
                0f, 0f, contrast, 0f, -128f * (contrast - 1),
                0f, 0f, 0f, 1f, 0f
            )))
        }
        ImageFilter.NONE -> ColorMatrix()
    }

    var brightness by remember { mutableStateOf(1f) } // 1f = normal
    var contrast by remember { mutableStateOf(1f) }   // 1f = normal
    var blurRadius by remember { mutableStateOf(0f) } // 0f = no blur

    val colorMatrix2 = createBrightnessContrastMatrix(brightness, contrast)
    val draggableTexts = remember { mutableStateListOf<DraggableText>() }
    var textColor by remember { mutableStateOf(Color.White) }
    var nextId by remember { mutableStateOf(0) }
    val blurEffect = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && blurRadius > 0f) {
        RenderEffect.createBlurEffect(blurRadius, blurRadius, Shader.TileMode.CLAMP)
    } else null
    val finalMatrix = multiplyColorMatrices(colorMatrix2, colorMatrix)
    val filters = listOf("Original", "Gray", "Sepia", "Invert")

    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var isDragging by remember { mutableStateOf(false) }


    Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .clip(shape = RoundedCornerShape(16.dp))
                    .background(color = Color.White)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cross),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(start = 6.dp, top = 6.dp)
                        .size(20.dp)
                        .align(Alignment.TopStart)
                        .shadow(elevation = 60.dp)
                        .zIndex(4f)
                )
                Image(
                    bitmap = imageBitmap.asImageBitmap(),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                        .graphicsLayer {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && blurRadius > 0f) {
                                renderEffect = blurEffect?.asComposeRenderEffect()
                            }
                        },
                    colorFilter = ColorFilter.colorMatrix(finalMatrix)
                )
                // Draw crop overlay
                if(crop) {
                    Canvas(
                        modifier = Modifier
                            .matchParentSize()
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragStart = {
                                        isDragging = cropRect.contains(it.x.toInt(), it.y.toInt())
                                        dragOffset = it
                                    },
                                    onDragEnd = {
                                        isDragging = false
                                    },
                                    onDrag = { change, dragAmount ->
                                        change.consume()
                                        if (isDragging) {
                                            cropRect = Rect(
                                                (cropRect.left + dragAmount.x).toInt().coerceIn(0, imageSize.width - cropRect.width()),
                                                (cropRect.top + dragAmount.y).toInt().coerceIn(0, imageSize.height - cropRect.height()),
                                                (cropRect.right + dragAmount.x).toInt().coerceIn(cropRect.width(), imageSize.width),
                                                (cropRect.bottom + dragAmount.y).toInt().coerceIn(cropRect.height(), imageSize.height)
                                            )
                                            onCropFinished(cropRect)
                                        }
                                    }
                                )
                            }
                    ) {
                        if (!cropMode) return@Canvas

                        drawRect(
                            color = Color.Black.copy(alpha = 0.5f),
                            size = size
                        )

                        // Clear the cropping area
                        drawRect(
                            color = Color.Transparent,
                            topLeft = Offset(cropRect.left.toFloat(), cropRect.top.toFloat()),
                            size = Size(cropRect.width().toFloat(), cropRect.height().toFloat()),
                            blendMode = BlendMode.Clear
                        )

                        // Draw border
                        drawRect(
                            color = Color.White,
                            topLeft = Offset(cropRect.left.toFloat(), cropRect.top.toFloat()),
                            size = Size(cropRect.width().toFloat(), cropRect.height().toFloat()),
                            style = Stroke(width = 4f)
                        )
                    }
                    // Buttons
                    Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                            Button(onClick = { cropMode = !cropMode }) {
                                Text(if (cropMode) "Cancel Crop" else "Crop")
                            }

                            if (cropMode) {
                                Button(onClick = {
                                    imageBitmap?.let { bmp ->
                                        val cropped = Bitmap.createBitmap(
                                            bmp,
                                            cropRect.left,
                                            cropRect.top,
                                            cropRect.width(),
                                            cropRect.height()
                                        )
                                        imageBitmap = cropped
                                        cropMode = false
                                    }
                                }) {
                                    Text("Apply Crop")
                                }
                            }
                        }
                    }
                }


/*
        Column(modifier = Modifier.width(60.dp).fillMaxHeight().align(Alignment.TopEnd), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {

            LazyRow(modifier = Modifier.padding(8.dp)) {
                items(filters) { label ->
                    val previewMatrix = when (label) {
                        "Gray" -> ColorMatrix().apply { setToSaturation(0f) }
                        "Sepia" -> ColorMatrix().apply {
                            set(
                                ColorMatrix(
                                    floatArrayOf(
                                        0.393f, 0.769f, 0.189f, 0f, 0f,
                                        0.349f, 0.686f, 0.168f, 0f, 0f,
                                        0.272f, 0.534f, 0.131f, 0f, 0f,
                                        0f,     0f,     0f,    1f, 0f
                                    )
                                )
                            )
                        }
                        "Invert" -> ColorMatrix().apply {
                            set(
                                ColorMatrix(
                                    floatArrayOf(
                                        -1f, 0f, 0f, 0f, 255f,
                                        0f, -1f, 0f, 0f, 255f,
                                        0f, 0f, -1f, 0f, 255f,
                                        0f, 0f, 0f, 1f, 0f
                                    )
                                )
                            )
                        }
                        else -> ColorMatrix()
                    }

                    Image(
                        painter = painterResource(id = R.drawable.profile_image_3),
                        contentDescription = label,
                        modifier = Modifier
                            .size(80.dp)
                            .padding(4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                // Update state based on selected filter
                                // or apply full-screen matrix if needed
                            },
                        colorFilter = ColorFilter.colorMatrix(previewMatrix)
                    )
                }
            }
        }

                 */

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(40.dp)
                        .align(Alignment.TopEnd),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = {
                            isAddText = true
                            draggableTexts.add(
                                DraggableText(
                                    id = nextId++,
                                    text = "New Text",
                                    color = textColor,
                                    offsetX = 0f,
                                    offsetY = 0f
                                )
                            )
                        },
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.text),
                            contentDescription = "",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    IconButton(
                        onClick = {
                            showFilter=!showFilter
                            isAddText=!isAddText
                        },
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.filter),
                            contentDescription = "",
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    IconButton(
                        onClick = { crop=!crop ;showFilter=false}) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_crop_24),
                            contentDescription = "",
                            modifier = Modifier.size(40.dp)
                        )
                    }

                }
                Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                    if (isAddText) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            val colors =
                                listOf(
                                    Color.White,
                                    Color.Red,
                                    Color.Green,
                                    Color.Blue,
                                    Color.Yellow,
                                    Color.Cyan
                                )
                            colors.forEach { color ->
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .border(
                                            width = if (textColor == color) 3.dp else 1.dp,
                                            color = if (textColor == color) Color.Black else Color.Gray,
                                            shape = CircleShape
                                        )
                                        .clickable { textColor = color }
                                )
                            }
                        }
                    }
                }
                if (isAddText && !showFilter) {
                    draggableTexts.forEach { item ->
                        var localOffsetX by remember { mutableStateOf(item.offsetX) }
                        var localOffsetY by remember { mutableStateOf(item.offsetY) }
                        var localText by remember { mutableStateOf(item.text) }

                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .offset {
                                    IntOffset(
                                        localOffsetX.roundToInt(),
                                        localOffsetY.roundToInt()
                                    )
                                }
                                .background(
                                    Color(0xFF121212).copy(alpha = 0.4f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(8.dp)
                                .pointerInput(Unit) {
                                    detectDragGestures { change, dragAmount ->
                                        change.consume()
                                        localOffsetX += dragAmount.x
                                        localOffsetY += dragAmount.y
                                        item.offsetX = localOffsetX
                                        item.offsetY = localOffsetY
                                    }
                                }
                        ) {
                            BasicTextField(
                                value = localText,
                                onValueChange = {
                                    localText = it
                                    item.text = it
                                },
                                textStyle = TextStyle(
                                    color = item.color,
                                    fontSize = 20.sp,
                                    fontFamily = Constants.FONT_MEDIUM
                                ),
                                modifier = Modifier.wrapContentSize()
                            )
                        }
                    }
                }
                Box(modifier = Modifier.align(Alignment.BottomStart)) {
                    Column() {
                        if(showSlider){
                            Column(modifier = Modifier) {
                                Text("Brightness")
                                Slider(value = brightness, onValueChange = { brightness = it }, valueRange = 0.5f..2f)

                                Text("Contrast")
                                Slider(value = contrast, onValueChange = { contrast = it }, valueRange = 0.5f..2f)

                                Text("Blur")
                                Slider(value = blurRadius, onValueChange = { blurRadius = it }, valueRange = 0f..25f)
                            }
                        }
                        if(showFilter && !isAddText) {
                            LazyRow {
                                items(ImageFilter.values()) { filter ->
                                    Column(
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .padding(end = 10.dp),
                                        verticalArrangement = Arrangement.Top,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Card(
                                            modifier = Modifier
                                                .size(60.dp)
                                                .clickable {
                                                    selectedFilter = filter;showSlider = true
                                                },
                                            shape = CircleShape,
                                            colors = CardDefaults.cardColors(containerColor = Color.White)
                                        ) {
                                            Image(
                                                painter = painterResource(id = R.drawable.profile_image_3),
                                                contentDescription = "",
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Crop
                                            )
                                        }
                                        Text(
                                            filter.name,
                                            fontSize = 8.sp,
                                            fontFamily = Constants.FONT_LIGHT,
                                            color = Color.Black
                                        )
                                    }

                                }
                            }
                        }
                    }
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp, top = 10.dp), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { navHostController.navigate(SCREENS.CREATE_PING.route) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray, contentColor = Color.White)
                ) {
                    Text(text = "Save")
                }
                Spacer(modifier = Modifier.width(20.dp))
                Button(
                    onClick = { navHostController.navigate(SCREENS.CREATE_PING.route) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D69C4), contentColor = Color.White)
                ) {
                    Text(text = "Next ->")
                }
            }

        }
    }
}

fun createBrightnessContrastMatrix(brightness: Float, contrast: Float): ColorMatrix {
    val b = brightness
    val c = contrast

    // First: brightness scale
    val brightnessMatrix = floatArrayOf(
        b, 0f, 0f, 0f, 0f,
        0f, b, 0f, 0f, 0f,
        0f, 0f, b, 0f, 0f,
        0f, 0f, 0f, 1f, 0f
    )

    // Second: contrast shift
    val translate = 128 * (1 - c)
    val contrastMatrix = floatArrayOf(
        c, 0f, 0f, 0f, translate,
        0f, c, 0f, 0f, translate,
        0f, 0f, c, 0f, translate,
        0f, 0f, 0f, 1f, 0f
    )

    // Multiply the two matrices manually
    val resultMatrix = FloatArray(20)
    for (row in 0 until 4) {
        for (col in 0 until 5) {
            var sum = 0f
            for (k in 0 until 4) {
                sum += contrastMatrix[row * 5 + k] * brightnessMatrix[k * 5 + col]
            }
            if (col == 4) sum += contrastMatrix[row * 5 + 4]
            resultMatrix[row * 5 + col] = sum
        }
    }
    resultMatrix[19] = 1f // last alpha channel
    return ColorMatrix(resultMatrix)
}

fun multiplyColorMatrices(a: ColorMatrix, b: ColorMatrix): ColorMatrix {
    val result = FloatArray(20)
    val aVals = a.values
    val bVals = b.values

    for (row in 0 until 4) {
        for (col in 0 until 5) {
            var sum = 0f
            for (k in 0 until 4) {
                sum += aVals[row * 5 + k] * bVals[k * 5 + col]
            }
            if (col == 4) sum += aVals[row * 5 + 4]
            result[row * 5 + col] = sum
        }
    }
    result[19] = 1f // alpha
    return ColorMatrix(result)
}
