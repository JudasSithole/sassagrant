package com.sassa.scamshield.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
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
import com.sassa.scamshield.ui.components.EmptyState
import com.sassa.scamshield.ui.components.MessageAnalysisCard
import com.sassa.scamshield.ui.components.ProtectionStatusCard
import com.sassa.scamshield.ui.components.RiskBadge
import com.sassa.scamshield.ui.components.ThreatCard
import com.sassa.scamshield.ui.theme.DarkCharcoal
import com.sassa.scamshield.ui.theme.TextPrimary
import com.sassa.scamshield.ui.theme.TextSecondary
import com.sassa.scamshield.ui.viewmodel.MessagesUiState
import com.sassa.scamshield.util.RiskLevel

import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MessagesScreen(
    viewModel: com.sassa.scamshield.ui.viewmodel.MessagesViewModel,
    onNavigateToAnalysis: (String) -> Unit
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
        Text(
            text = "Message Protection",
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )
        Text(
            text = "Your messages are being monitored for scam indicators.",
            color = TextSecondary,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        ProtectionStatusCard(
            isEnabled = uiState.isProtectionActive,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            ThreatCard(
                title = "Analyzed",
                value = uiState.totalAnalyzed.toString(),
                subtitle = "messages analyzed"
            )
            Spacer(modifier = Modifier.width(8.dp))
            ThreatCard(
                title = "Suspicious",
                value = uiState.suspiciousCount.toString(),
                subtitle = "flagged messages"
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            ThreatCard(
                title = "Blocked",
                value = uiState.blockedCount.toString(),
                subtitle = "high risk blocked"
            )
            Spacer(modifier = Modifier.width(8.dp))
            ThreatCard(
                title = "Safe",
                value = uiState.safeCount.toString(),
                subtitle = "safe messages"
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Analyzed Messages",
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        if (uiState.analyses.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                EmptyState(
                    title = "No messages analyzed",
                    message = "Messages will appear here once they are analyzed for scam indicators.",
                    icon = Icons.Outlined.Email
                )
            }
        } else {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                uiState.analyses.forEachIndexed { index, analysis ->
                    MessageAnalysisSummaryCard(
                        analysis = analysis,
                        onClick = { onNavigateToAnalysis(analysis.id) }
                    )
                    if (index < uiState.analyses.size - 1) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun MessageAnalysisSummaryCard(
    analysis: com.sassa.scamshield.domain.model.MessageAnalysis,
    onClick: () -> Unit
) {
    val riskColor = analysis.riskLevel.colorValue()
    val preview = analysis.messageContent.take(80)
    val previewText = if (analysis.messageContent.length > 80) "$preview..." else preview

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(com.sassa.scamshield.ui.theme.CardBackground)
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(riskColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = null,
                    tint = riskColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = analysis.sender.ifEmpty { "Unknown sender" },
                    color = TextSecondary,
                    fontSize = 12.sp
                )
                Text(
                    text = previewText,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    maxLines = 2
                )
                Text(
                    text = formatTimestamp(analysis.analyzedAt),
                    color = TextSecondary.copy(alpha = 0.6f),
                    fontSize = 11.sp
                )
            }
            RiskBadge(riskLevel = analysis.riskLevel)
        }
    }
}
