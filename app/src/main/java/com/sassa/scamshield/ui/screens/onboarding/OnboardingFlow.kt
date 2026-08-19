package com.sassa.scamshield.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sassa.scamshield.ui.theme.CardBackground
import com.sassa.scamshield.ui.theme.GradientEnd
import com.sassa.scamshield.ui.theme.GradientStart
import com.sassa.scamshield.ui.theme.Purple500
import com.sassa.scamshield.ui.theme.TextPrimary
import com.sassa.scamshield.ui.theme.TextSecondary

data class OnboardingPage(
    val title: String,
    val description: String
)

val onboardingPages = listOf(
    OnboardingPage(
        title = "Protect Your Grant",
        description = "ScamShield helps you identify suspicious SASSA messages and phone calls."
    ),
    OnboardingPage(
        title = "Detect Suspicious Messages",
        description = "We analyze common scam indicators and warn you before you act."
    ),
    OnboardingPage(
        title = "Identify Suspicious Calls",
        description = "Know when a phone number has been reported as suspicious."
    ),
    OnboardingPage(
        title = "Stay Protected",
        description = "Your privacy matters. We prioritize on-device protection."
    )
)

@Composable
fun OnboardingFlow(
    onOnboardingComplete: () -> Unit
) {
    val scrollState = rememberScrollState()
    val (currentPage, setCurrentPage) = remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0A0A0A),
                        Color(0xFF121212)
                    )
                )
            )
            .verticalScroll(scrollState)
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        OnboardingIndicator(
            total = onboardingPages.size,
            current = currentPage,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Column(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = onboardingPages[currentPage].title,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = onboardingPages[currentPage].description,
                color = TextSecondary,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (currentPage < onboardingPages.lastIndex) {
            Button(
                onClick = { setCurrentPage(currentPage + 1) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(GradientStart, GradientEnd)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Continue",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = { setCurrentPage(onboardingPages.lastIndex) }) {
                Text(
                    text = "Skip",
                    color = TextSecondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        } else {
            Button(
                onClick = { onOnboardingComplete() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(GradientStart, GradientEnd)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Get Started",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun OnboardingIndicator(
    total: Int,
    current: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(total) { index ->
            val width = if (index == current) 24f else 8f
            val color = if (index == current) Purple500 else TextSecondary.copy(alpha = 0.3f)
            Box(
                modifier = Modifier
                    .width(width.dp)
                    .height(4.dp)
                    .background(color, RoundedCornerShape(2.dp))
            )
        }
    }
}
