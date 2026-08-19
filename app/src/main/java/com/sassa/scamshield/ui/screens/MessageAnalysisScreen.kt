package com.sassa.scamshield.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sassa.scamshield.ui.components.MessageAnalysisCard
import com.sassa.scamshield.ui.components.RiskBadge
import com.sassa.scamshield.ui.theme.DarkCharcoal
import com.sassa.scamshield.ui.theme.TextPrimary
import com.sassa.scamshield.ui.viewmodel.MessageAnalysisViewModel

@Composable
fun MessageAnalysisScreen(
    onNavigateBack: () -> Unit,
    viewModel: MessageAnalysisViewModel
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onNavigateBack) {
                Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "Back", tint = TextPrimary)
            }
            Text(text = "Message Analysis", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.height(24.dp))

        if (uiState.isLoading) {
            Text(text = "Loading...", color = TextPrimary, fontSize = 16.sp)
        } else if (uiState.analysis != null) {
            val analysis = uiState.analysis!!
            MessageAnalysisCard(
                riskScore = analysis.riskScore,
                riskLabel = "${analysis.riskLevel.label} MESSAGE",
                riskColor = analysis.riskLevel.colorValue(),
                message = analysis.messageContent,
                reasons = analysis.reasons,
                onReportClick = { },
                onDeleteClick = { },
                onMarkSafeClick = { },
                onLearnMoreClick = { }
            )
        } else {
            Text(text = "Analysis not found", color = TextPrimary, fontSize = 16.sp)
        }
    }
}
