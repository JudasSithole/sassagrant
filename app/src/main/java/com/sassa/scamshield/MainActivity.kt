package com.sassa.scamshield

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.sassa.scamshield.ui.screens.onboarding.OnboardingFlow
import com.sassa.scamshield.ui.theme.ScamShieldTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScamShieldApp()
        }
    }
}

@Composable
fun ScamShieldApp() {
    ScamShieldTheme {
        var showOnboarding by rememberSaveable { mutableStateOf(true) }
        if (showOnboarding) {
            OnboardingFlow(
                onOnboardingComplete = { showOnboarding = false }
            )
        } else {
            com.sassa.scamshield.ui.AppScaffold()
        }
    }
}

@Preview
@Composable
fun ScamShieldAppPreview() {
    ScamShieldTheme {
        ScamShieldApp()
    }
}
