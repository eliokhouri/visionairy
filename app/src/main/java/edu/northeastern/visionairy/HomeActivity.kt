package edu.northeastern.visionairy

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import edu.northeastern.visionairy.ui.theme.VisionairyTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VisionairyTheme {
                HomeScreen(
                    onFrontCameraClick = {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("camera", "front")
                        startActivity(intent)
                    },
                    onBackCameraClick = {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("camera", "back")
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun HomeScreen(
    onFrontCameraClick: () -> Unit,
    onBackCameraClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // General padding around the screen
    ) {
        // Title positioned near the top of the screen
        Text(
            text = "Visionairy",
            fontSize = 32.sp, // Larger text
            fontWeight = FontWeight.Bold, // Bold text
            textAlign = TextAlign.Center, // Center align
            modifier = Modifier
                .align(Alignment.TopCenter) // Position title near the top center
                .padding(top = 150.dp) // Adjust padding as needed
        )

        // Buttons centered on the screen
        Column(
            modifier = Modifier
                .align(Alignment.Center) // Anchor the buttons to the center
                .padding(top = 1.dp), // Add extra padding if needed later
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = onFrontCameraClick) {
                Text(text = "Front Camera")
            }
            Spacer(modifier = Modifier.height(16.dp)) // Spacing between buttons
            Button(onClick = onBackCameraClick) {
                Text(text = "Back Camera")
            }
        }
    }
}

@Preview(showBackground = true, name = "Home Screen Preview")
@Composable
fun HomeScreenPreview() {
    VisionairyTheme {
        HomeScreen(
            onFrontCameraClick = {},
            onBackCameraClick = {}
        )
    }
}
