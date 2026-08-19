package com.sassa.scamshield.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import com.sassa.scamshield.ui.components.GradientSecurityCard
import com.sassa.scamshield.ui.components.QuickActionButton
import com.sassa.scamshield.ui.components.ThreatCard
import com.sassa.scamshield.ui.components.ActivityCard
import com.sassa.scamshield.ui.theme.CardBackground
import com.sassa.scamshield.ui.theme.DarkCharcoal
import com.sassa.scamshield.ui.theme.TextPrimary
import com.sassa.scamshield.ui.theme.TextSecondary
import com.sassa.scamshield.ui.viewmodel.HomeUiState
import com.sassa.scamshield.util.RiskLevel

@Composable
fun HomeScreen(
    viewModel: com.sassa.scamshield.ui.viewmodel.HomeViewModel,
    onNavigateToScanMessage: () -> Unit,
    onNavigateToNumberLookup: () -> Unit,
    onNavigateToReportScam: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
            .verticalScroll(scrollState)
            .padding(bottom = 80.dp)
    ) {
        HomeTopBar()
        Spacer(modifier = Modifier.height(24.dp))
        GradientSecurityCard(
            title = "YOU'RE PROTECTED",
            subtitle = "Security Status",
            score = uiState.stats.protectionConfidence,
            scoreLabel = "Protection Confidence",
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                ThreatCard(
                    title = "Threats Blocked",
                    value = uiState.stats.threatsBlocked.toString(),
                    subtitle = "total threats blocked"
                )
                Spacer(modifier = Modifier.width(8.dp))
                ThreatCard(
                    title = "Suspicious Messages",
                    value = uiState.stats.suspiciousMessages.toString(),
                    subtitle = "flagged messages"
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                ThreatCard(
                    title = "Suspicious Calls",
                    value = uiState.stats.suspiciousCalls.toString(),
                    subtitle = "flagged calls"
                )
                Spacer(modifier = Modifier.width(8.dp))
                ThreatCard(
                    title = "Confidence",
                    value = "${uiState.stats.protectionConfidence}%",
                    subtitle = "protection confidence"
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Quick Actions",
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            QuickActionButton(
                text = "Scan Message",
                icon = Icons.Outlined.Email,
                onClick = onNavigateToScanMessage
            )
            Spacer(modifier = Modifier.height(12.dp))
            QuickActionButton(
                text = "Check Number",
                icon = Icons.Outlined.Phone,
                onClick = onNavigateToNumberLookup
            )
            Spacer(modifier = Modifier.height(12.dp))
            QuickActionButton(
                text = "Report Scam",
                icon = Icons.Outlined.Description,
                onClick = onNavigateToReportScam
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Recent Activity",
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        if (uiState.recentEvents.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(CardBackground),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No recent activity", color = TextSecondary, fontSize = 14.sp)
            }
        } else {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                uiState.recentEvents.take(5).forEachIndexed { index, event ->
                    val riskColor = event.riskLevel.colorValue()
                    ActivityCard(
                        iconTint = riskColor,
                        iconImageVector = getEventIcon(event.eventType),
                        title = event.title,
                        description = event.description,
                        timestamp = formatTimestamp(event.timestamp.toEpochMilli()),
                        riskLevelText = event.riskLevel.label
                    )
                    if (index < 4) Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun HomeTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = "Good morning", color = TextSecondary, fontSize = 14.sp)
            Text(text = "You're protected", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notifications",
                tint = TextPrimary,
                modifier = Modifier.size(24.dp)
            )
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(CardBackground)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Profile",
                    tint = Color(0xFF8B5CF6),
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun getEventIcon(eventType: com.sassa.scamshield.domain.model.EventType): androidx.compose.ui.graphics.vector.ImageVector {
    return when (eventType) {
        com.sassa.scamshield.domain.model.EventType.MESSAGE_DETECTED,
        com.sassa.scamshield.domain.model.EventType.MESSAGE_FLAGGED -> Icons.Outlined.Email
        com.sassa.scamshield.domain.model.EventType.CALL_FLAGGED,
        com.sassa.scamshield.domain.model.EventType.CALL_BLOCKED -> Icons.Outlined.Call
        com.sassa.scamshield.domain.model.EventType.NUMBER_BLOCKED -> Icons.Outlined.Block
        else -> Icons.Outlined.Info
    }
}

@Composable
fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    return when {
        diff < 60000 -> "Just now"
        diff < 3600000 -> "${diff / 60000} minutes ago"
        diff < 86400000 -> "${diff / 3600000} hours ago"
        else -> "${diff / 86400000} days ago"
    }
}
