package com.example.finalapp.screens.webrtc

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.constants.Constants
import org.webrtc.EglBase
import org.webrtc.SurfaceViewRenderer

@Composable
fun OmegleScreen() {
    val context = LocalContext.current
    val activity = context as Activity

    val cameraPermission = Manifest.permission.CAMERA
    val micPermission = Manifest.permission.RECORD_AUDIO

    var isConnected by remember { mutableStateOf(false) }
    var permissionsGranted by remember { mutableStateOf(false) }

    val eglBase = remember { EglBase.create() }

    // Keep the views across recompositions
    val localView = remember { SurfaceViewRenderer(context) }
    val remoteView = remember { SurfaceViewRenderer(context) }

    // Init + release localView safely
    DisposableEffect(localView) {
        localView.init(eglBase.eglBaseContext, null)
        localView.setMirror(true)

        onDispose {
            localView.release()
        }
    }

    // Init + release remoteView safely
    DisposableEffect(remoteView) {
        remoteView.init(eglBase.eglBaseContext, null)
        remoteView.setMirror(false)

        onDispose {
            remoteView.release()
        }
    }

    val webRTCManager = remember { WebRTCManager(context, localView, remoteView, eglBase) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[cameraPermission] == true && permissions[micPermission] == true
        permissionsGranted = granted

        if (granted) {
            webRTCManager.startLocalPreview()
        } else {
            Toast.makeText(context, "Camera and mic permissions are required.", Toast.LENGTH_LONG).show()
        }
    }

    // Ask for permissions on first load
    LaunchedEffect(Unit) {
        val hasCamera = ContextCompat.checkSelfPermission(context, cameraPermission) == PackageManager.PERMISSION_GRANTED
        val hasMic = ContextCompat.checkSelfPermission(context, micPermission) == PackageManager.PERMISSION_GRANTED
        if (hasCamera && hasMic) {
            permissionsGranted = true
            webRTCManager.startLocalPreview()
        } else {
            permissionLauncher.launch(arrayOf(cameraPermission, micPermission))
        }
    }

    // Handle connection lifecycle
    LaunchedEffect(permissionsGranted) {
        if (permissionsGranted) {
            webRTCManager.init()             // create PeerConnectionFactory
            webRTCManager.setupLocalVideo()  // ✅ prepare capturer + track + bind to localRenderer
            webRTCManager.startLocalPreview()// ✅ preview starts immediately
        }
    }
    LaunchedEffect(isConnected) {
        if (isConnected && permissionsGranted) {
            webRTCManager.setupSocket() // new method that only does setupSocket()
        } else {
            webRTCManager.release()
        }
    }



    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Random connect on mood",
                        fontFamily = Constants.FONT_MEDIUM,
                        fontSize = 24.sp,
                        color = Color.White
                    )
                },
                backgroundColor = Color.Black
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        if (permissionsGranted) {
                            isConnected = !isConnected
                        } else {
                            permissionLauncher.launch(arrayOf(cameraPermission, micPermission))
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = floatingActionBtnColor)
                ) {
                    Text(
                        if (isConnected) "Disconnect" else "Connect",
                        fontFamily = Constants.FONT_LIGHT,
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }
                Button(onClick = { /* TODO: Next random user */ }) {
                    Text("Next")
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            when {
                !permissionsGranted -> {
                    Text("Please grant camera and mic permissions to continue.", color = Color.White)
                }

                isConnected -> {
                    AndroidView(factory = {
                        (remoteView.parent as? ViewGroup)?.removeView(remoteView)
                        remoteView
                    }, modifier = Modifier.fillMaxSize())

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                            .size(120.dp, 160.dp)
                    ) {
                        AndroidView(factory = {
                            (localView.parent as? ViewGroup)?.removeView(localView)
                            localView
                        }, modifier = Modifier.fillMaxSize())
                    }
                }

                else -> {
                    // Show local preview even when not connected
                    Box(
                        modifier = Modifier
                            .size(240.dp, 320.dp)
                            .background(Color.DarkGray),
                        contentAlignment = Alignment.Center
                    ) {
                        AndroidView(factory = {
                            (localView.parent as? ViewGroup)?.removeView(localView)
                            localView
                        }, modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}
