package com.sassa.scamshield.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sassa.scamshield.ui.components.PermissionCard
import com.sassa.scamshield.ui.components.ProtectionStatusCard
import com.sassa.scamshield.ui.theme.DarkCharcoal
import com.sassa.scamshield.ui.theme.CardBackground
import com.sassa.scamshield.ui.theme.CardBackgroundSecondary
import com.sassa.scamshield.ui.theme.Purple500
import com.sassa.scamshield.ui.theme.TextPrimary
import com.sassa.scamshield.ui.theme.TextSecondary
import com.sassa.scamshield.ui.viewmodel.SettingsUiState

@Composable
fun SettingsScreen(
    viewModel: com.sassa.scamshield.ui.viewmodel.SettingsViewModel,
    onNavigateToEducation: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "Settings",
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )
        Text(
            text = "Manage your protection and privacy preferences.",
            color = TextSecondary,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Protection Settings",
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        SettingToggleCard(
            title = "Message Protection",
            description = "Scan incoming SMS messages for scam indicators",
            isEnabled = uiState.messageProtectionEnabled,
            onToggle = { viewModel.updateMessageProtection(it) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        SettingToggleCard(
            title = "Call Protection",
            description = "Identify suspicious callers before answering",
            isEnabled = uiState.callProtectionEnabled,
            onToggle = { viewModel.updateCallProtection(it) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        SettingToggleCard(
            title = "Automatic Scam Alerts",
            description = "Show alerts for detected threats",
            isEnabled = uiState.autoAlertsEnabled,
            onToggle = { viewModel.updateAutoAlerts(it) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        SettingToggleCard(
            title = "High-Risk Call Blocking",
            description = "Automatically block high-risk incoming calls",
            isEnabled = uiState.blockHighRiskCalls,
            onToggle = { viewModel.updateBlockHighRiskCalls(it) }
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Privacy",
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        SettingToggleCard(
            title = "Local Message Analysis",
            description = "Analyze messages on your device without sending data",
            isEnabled = uiState.localAnalysisEnabled,
            onToggle = { }
        )
        Spacer(modifier = Modifier.height(8.dp))
        SettingToggleCard(
            title = "Data Sharing",
            description = "Share anonymized scam reports with community",
            isEnabled = uiState.dataSharingEnabled,
            onToggle = { }
        )
        Spacer(modifier = Modifier.height(8.dp))
        SettingCard(
            title = "Report History",
            description = "View your submitted scam reports",
            onClick = { }
        )
        Spacer(modifier = Modifier.height(8.dp))
        SettingCard(
            title = "Privacy Policy",
            description = "Read how we protect your data",
            onClick = { }
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Permissions",
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        PermissionCard(
            permissionName = "SMS Permission",
            permissionDescription = "Read incoming SMS to analyze for scams",
            isGranted = false,
            onRequestClick = { },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        PermissionCard(
            permissionName = "Call Screening",
            permissionDescription = "Screen and identify suspicious incoming calls",
            isGranted = false,
            onRequestClick = { },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        PermissionCard(
            permissionName = "Notification Permission",
            permissionDescription = "Show security alerts and notifications",
            isGranted = false,
            onRequestClick = { },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))

        TextButton(
            onClick = onNavigateToEducation,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Scam Education Center",
                color = Purple500,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun SettingToggleCard(
    title: String,
    description: String,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(text = description, color = TextSecondary, fontSize = 12.sp)
            }
            Switch(
                checked = isEnabled,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    uncheckedThumbColor = Color.White,
                    checkedTrackColor = Purple500,
                    uncheckedTrackColor = TextSecondary
                )
            )
        }
    }
}

@Composable
fun SettingCard(
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBackground)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Column {
            Text(text = title, color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Text(text = description, color = TextSecondary, fontSize = 12.sp)
        }
    }
}
