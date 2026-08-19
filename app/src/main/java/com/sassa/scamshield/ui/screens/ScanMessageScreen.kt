package com.sassa.scamshield.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sassa.scamshield.ui.components.MessageAnalysisCard
import com.sassa.scamshield.ui.theme.CardBackground
import com.sassa.scamshield.ui.theme.DarkCharcoal
import com.sassa.scamshield.ui.theme.GradientEnd
import com.sassa.scamshield.ui.theme.GradientStart
import com.sassa.scamshield.ui.theme.TextPrimary
import com.sassa.scamshield.ui.theme.TextSecondary
import com.sassa.scamshield.ui.viewmodel.ScanMessageViewModel

@Composable
fun ScanMessageScreen(
    onNavigateBack: () -> Unit,
    viewModel: ScanMessageViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onNavigateBack) {
                Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "Back", tint = TextPrimary)
            }
            Text(text = "Scan Message", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = uiState.messageText,
            onValueChange = { viewModel.updateMessage(it) },
            label = { Text("Paste message here...", color = TextSecondary) },
            placeholder = { Text("Enter or paste the message to analyze", color = TextSecondary.copy(0.5f)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedContainerColor = CardBackground,
                unfocusedContainerColor = CardBackground,
                focusedBorderColor = GradientStart,
                unfocusedBorderColor = Color(0xFF2D2D2D)
            ),
            minLines = 4,
            maxLines = 10,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = uiState.senderText,
            onValueChange = { viewModel.updateSender(it) },
            label = { Text("Sender (optional)", color = TextSecondary) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedContainerColor = CardBackground,
                unfocusedContainerColor = CardBackground,
                focusedBorderColor = GradientStart,
                unfocusedBorderColor = Color(0xFF2D2D2D)
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { viewModel.analyzeMessage() },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(16.dp),
            enabled = uiState.messageText.isNotBlank() && !uiState.isAnalyzing
        ) {
            Box(
                modifier = Modifier.fillMaxSize().background(
                    brush = Brush.horizontalGradient(colors = listOf(GradientStart, GradientEnd)),
                    shape = RoundedCornerShape(16.dp)
                ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (uiState.isAnalyzing) "Analyzing..." else "Analyze Message",
                    color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp
                )
            }
        }
        if (uiState.analysis != null) {
            Spacer(modifier = Modifier.height(24.dp))
            MessageAnalysisCard(
                riskScore = uiState.analysis!!.riskScore,
                riskLabel = "${uiState.analysis!!.riskLevel.label} MESSAGE",
                riskColor = uiState.analysis!!.riskLevel.colorValue(),
                message = uiState.analysis!!.messageContent,
                reasons = uiState.analysis!!.reasons,
                onReportClick = { viewModel.reportAnalysis() },
                onDeleteClick = { viewModel.clearAnalysis() },
                onMarkSafeClick = { },
                onLearnMoreClick = { }
            )
        }
    }
}
