package com.spint.app.screens.common

/*
@Composable
fun AutoImageCropper() {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val cropImageLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            imageUri = result.uriContent
        } else {
            Log.e("CropError", "Error: ${result.error?.message}")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        imageUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Cropped Image",
                modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }

        Button(
            onClick = {
                cropImageLauncher.launch(
                    CropImageContractOptions(
                        uri = null, // Let user pick an image
                        CropImageOptions(
                            guidelines = CropImageView.Guidelines.ON,
                            aspectRatioX = 1,
                            aspectRatioY = 1, // Square crop
                            fixAspectRatio = true
                        )
                    )
                )
            }
        ) {
            Text("Select & Crop Image")
        }
    }
}
*/