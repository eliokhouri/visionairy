package edu.northeastern.visionairy

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import edu.northeastern.visionairy.ui.theme.NeonRed
import edu.northeastern.visionairy.ui.theme.VisionairyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Determine the camera type from the intent (default to back camera if not specified)
        val cameraType = intent.getStringExtra("camera") ?: "back"

        setContent {
            VisionairyTheme {
                CameraPreview(
                    cameraType = cameraType,
                    onExitClick = {
                        // Navigate back to HomeActivity
                        val intent = Intent(this, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun CameraPreview(cameraType: String, onExitClick: () -> Unit) {
    val context = LocalContext.current

    // State to track permission
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Permission launcher for requesting camera permission
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
    }

    // Check and request permission if not already granted
    LaunchedEffect(Unit) {
        if (!hasPermission) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Show camera preview if permission is granted
    if (hasPermission) {
        // Choose the camera based on the provided `cameraType`
        val cameraSelector = if (cameraType == "front") {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }

        Box(modifier = Modifier.fillMaxSize()) {
            // Camera preview
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = androidx.camera.core.Preview.Builder().build()
                        preview.surfaceProvider = previewView.surfaceProvider
                        cameraProvider.bindToLifecycle(
                            context as ComponentActivity,
                            cameraSelector,
                            preview
                        )
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                }
            )

            // Exit button at the top-right corner
            IconButton(
                onClick = onExitClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp) // Add padding for better placement
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
                    contentDescription = "Exit",
                    tint = NeonRed, // Set the color of the icon
                    modifier = Modifier.size(50.dp) // Set the size of the icon
                )
            }
        }
    }
}
