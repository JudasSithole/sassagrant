package com.sassa.scamshield.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sassa.scamshield.domain.model.ScamReport
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sassa.scamshield.ui.components.EmptyState
import com.sassa.scamshield.ui.components.RiskBadge
import com.sassa.scamshield.util.RiskLevel
import com.sassa.scamshield.ui.theme.DarkCharcoal
import com.sassa.scamshield.ui.theme.Purple500
import com.sassa.scamshield.ui.theme.TextPrimary
import com.sassa.scamshield.ui.theme.TextSecondary
import com.sassa.scamshield.ui.theme.CardBackground
import com.sassa.scamshield.ui.viewmodel.ReportsUiState

@Composable
fun ReportsScreen(
    viewModel: com.sassa.scamshield.ui.viewmodel.ReportsViewModel,
    onNavigateToReportScam: () -> Unit
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
            text = "Reports",
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )
        Text(
            text = "View and manage your scam reports.",
            color = TextSecondary,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (uiState.reports.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                EmptyState(
                    title = "No reports yet",
                    message = "Submit scam reports to help protect other users.",
                    icon = Icons.Outlined.Description,
                    actionText = "Report a Scam",
                    onActionClick = onNavigateToReportScam
                )
            }
        } else {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                uiState.reports.forEachIndexed { index, report ->
                    ReportItemCard(report = report)
                    if (index < uiState.reports.size - 1) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ReportItemCard(report: ScamReport) {
    val riskColor = Color(0xFFFFA947)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBackground)
            .padding(16.dp)
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
                    imageVector = Icons.Outlined.Description,
                    contentDescription = null,
                    tint = riskColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = report.category.displayName,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
                Text(
                    text = report.phoneNumber ?: report.messageContent?.take(60) ?: "Link: ${report.link}",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    maxLines = 2
                )
                Text(
                    text = "Status: ${if (report.isVerified) "Verified" else "Pending"}",
                    color = TextSecondary.copy(alpha = 0.6f),
                    fontSize = 11.sp
                )
            }
            RiskBadge(riskLevel = RiskLevel.Suspicious)
        }
    }
}
