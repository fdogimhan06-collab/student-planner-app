package com.example.student_planner_app.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    var startAnimation by remember { mutableStateOf(false) }

    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1500),
        label = ""
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(3000)
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF4527A0), Color(0xFF7B52E0))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.alpha(alphaAnim.value)
        ) {
            Text("📚", fontSize = 80.sp)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Student Planner",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Organize • Learn • Succeed",
                fontSize = 15.sp,
                color = Color(0xFFD1C4E9)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(3) { index ->
                    var dotAnim by remember { mutableStateOf(false) }
                    val dotAlpha = animateFloatAsState(
                        targetValue = if (dotAnim) 1f else 0.3f,
                        animationSpec = tween(500),
                        label = ""
                    )
                    LaunchedEffect(Unit) {
                        delay(index * 200L)
                        while (true) {
                            dotAnim = true
                            delay(500)
                            dotAnim = false
                            delay(500)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .alpha(dotAlpha.value)
                            .background(Color.White, RoundedCornerShape(50))
                    )
                }
            }
        }
    }
}