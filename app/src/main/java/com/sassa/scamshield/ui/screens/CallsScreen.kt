package com.sassa.scamshield.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Call
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
import com.sassa.scamshield.ui.components.EmptyState
import com.sassa.scamshield.ui.components.ProtectionStatusCard
import com.sassa.scamshield.ui.components.ScamNumberCard
import com.sassa.scamshield.ui.components.ThreatCard
import com.sassa.scamshield.ui.theme.DarkCharcoal
import com.sassa.scamshield.ui.theme.TextPrimary
import com.sassa.scamshield.ui.theme.TextSecondary
import com.sassa.scamshield.ui.viewmodel.CallsUiState

@Composable
fun CallsScreen(
    viewModel: com.sassa.scamshield.ui.viewmodel.CallsViewModel
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
            text = "Call Protection",
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )
        Text(
            text = "Identify suspicious callers before you answer.",
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
                title = "Calls Screened",
                value = uiState.callsScreened.toString(),
                subtitle = "total screened"
            )
            Spacer(modifier = Modifier.width(8.dp))
            ThreatCard(
                title = "Suspicious",
                value = uiState.suspiciousCalls.toString(),
                subtitle = "flagged calls"
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            ThreatCard(
                title = "Blocked",
                value = uiState.blockedCalls.toString(),
                subtitle = "blocked calls"
            )
            Spacer(modifier = Modifier.width(8.dp))
            ThreatCard(
                title = "Numbers",
                value = uiState.scamNumbers.size.toString(),
                subtitle = "in database"
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Suspicious Numbers",
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        if (uiState.scamNumbers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                EmptyState(
                    title = "No suspicious numbers",
                    message = "Numbers reported by users will appear here.",
                    icon = Icons.Outlined.Call
                )
            }
        } else {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                uiState.scamNumbers.forEachIndexed { index, number ->
                    ScamNumberCard(number = number)
                    if (index < uiState.scamNumbers.size - 1) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}
