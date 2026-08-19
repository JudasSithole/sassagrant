package com.sassa.scamshield.ui.screens.onboarding

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.telecom.TelecomManager
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.sassa.scamshield.ui.theme.CardBackground
import com.sassa.scamshield.ui.theme.DarkCharcoal
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

data class PermissionItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val permission: String
)

@Composable
fun OnboardingFlow(
    onOnboardingComplete: () -> Unit
) {
    val scrollState = rememberScrollState()
    val (currentPage, setCurrentPage) = rememberSaveable { mutableStateOf(0) }
    val (hasRequestedPermissions, setHasRequestedPermissions) = rememberSaveable { mutableStateOf(false) }
    val (showPermissionScreen, setShowPermissionScreen) = remember { mutableStateOf(false) }

    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        setHasRequestedPermissions(true)
    }

    val allPermissions = listOf(
        Manifest.permission.READ_SMS,
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.CALL_PHONE
    )

    val permissionItems = listOf(
        PermissionItem(
            title = "SMS Messages",
            description = "Allows us to scan incoming messages for scam patterns",
            icon = androidx.compose.material.icons.Icons.Outlined.Email,
            permission = Manifest.permission.READ_SMS
        ),
        PermissionItem(
            title = "Phone Calls",
            description = "Identifies suspicious numbers before you answer",
            icon = androidx.compose.material.icons.Icons.Outlined.Call,
            permission = Manifest.permission.READ_CALL_LOG
        )
    )

    LaunchedEffect(hasRequestedPermissions) {
        if (hasRequestedPermissions) {
            onOnboardingComplete()
        }
    }

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
        if (showPermissionScreen) {
            PermissionScreen(
                permissionItems = permissionItems,
                allPermissions = allPermissions,
                onPermissionsRequested = { setHasRequestedPermissions(true) },
                onPermissionLauncher = { permissionLauncher.launch(allPermissions.toTypedArray()) },
                onSkip = { onOnboardingComplete() }
            )
        } else {
            OnboardingContent(
                currentPage = currentPage,
                setCurrentPage = setCurrentPage,
                onGetStartedClick = { setShowPermissionScreen(true) }
            )
        }
    }
}

@Composable
fun OnboardingContent(
    currentPage: Int,
    setCurrentPage: (Int) -> Unit,
    onGetStartedClick: () -> Unit
) {
    Spacer(modifier = Modifier.height(40.dp))
    OnboardingIndicator(
        total = onboardingPages.size,
        current = currentPage,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(32.dp))
    Column(
        modifier = Modifier.fillMaxWidth()
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
    Spacer(modifier = Modifier.height(48.dp))
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
            onClick = { onGetStartedClick() },
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

@Composable
fun PermissionScreen(
    permissionItems: List<PermissionItem>,
    allPermissions: List<String>,
    onPermissionsRequested: () -> Unit,
    onPermissionLauncher: (List<String>) -> Unit,
    onSkip: () -> Unit
) {
    val context = LocalContext.current
    val (grantedPermissions, setGrantedPermissions) = rememberSaveable { mutableStateOf<Set<String>>(emptySet()) }

    val checkPermission = { permission: String ->
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    LaunchedEffect(Unit) {
        val initiallyGranted = allPermissions.filter { checkPermission(it) }.toSet()
        setGrantedPermissions(initiallyGranted)
        if (initiallyGranted.size == allPermissions.size) {
            onPermissionsRequested()
        }
    }

    val animatedAlpha by animateFloatAsState(targetValue = if (grantedPermissions.isNotEmpty()) 1f else 0.5f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Permissions Required",
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "To protect you from scams, we need the following permissions:",
            color = TextSecondary,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        permissionItems.forEach { item ->
            PermissionCard(
                title = item.title,
                description = item.description,
                icon = item.icon,
                isGranted = grantedPermissions.contains(item.permission),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { onPermissionLauncher(allPermissions) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .alpha(animatedAlpha),
            enabled = grantedPermissions.size < allPermissions.size,
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
                    text = if (grantedPermissions.isNotEmpty()) "Grant Remaining" else "Grant All Permissions",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = { onSkip() }) {
            Text(
                text = "Skip for now",
                color = TextSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun PermissionCard(
    title: String,
    description: String,
    icon: ImageVector,
    isGranted: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBackground)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isGranted) GradientStart else TextSecondary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text(
                    text = description,
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
            Checkbox(
                checked = isGranted,
                onCheckedChange = {},
                modifier = Modifier.size(20.dp)
            )
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
