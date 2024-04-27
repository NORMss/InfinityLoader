package com.norm.myinfinityloader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.norm.myinfinityloader.ui.theme.MyInfinityLoaderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyInfinityLoaderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        InfinityLoader(
                            modifier = Modifier
                                .width(256.dp)
                                .height(200.dp),
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color.Blue, Color.Cyan)
                            ),
                            glow = Glow(
                                xShifting = 2.dp,
                                yShifting = 2.dp,
                            ),
                            duration = 6_000,
                            placeholderColor = Color.Red.copy(alpha = 0.25f)
                        )
                    }
                }
            }
        }
    }
}