package com.example.mda.ui.kids

// UI screen component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.example.mda.R
import kotlinx.coroutines.delay

@Composable
fun KidsSplashScreen(
    onFinished: () -> Unit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.kids_mode))

    val progress by animateLottieCompositionAsState(
        composition,
        iterations = 2
    )

    LaunchedEffect(progress) {
        if (progress == 1f) {
            delay(300)
            onFinished()
        }
    }

    val textColor = MaterialTheme.colorScheme.onBackground

    Crossfade(targetState = progress < 1f, label = "fade_anim") { isPlaying ->
        if (isPlaying) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        modifier = Modifier.size(220.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "WELCOME TO KIDS MODE ",
                        color = textColor,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}