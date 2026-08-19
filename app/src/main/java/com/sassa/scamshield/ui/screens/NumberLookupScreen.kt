package com.sassa.scamshield.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Search
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sassa.scamshield.ui.theme.CardBackground
import com.sassa.scamshield.ui.theme.DarkCharcoal
import com.sassa.scamshield.ui.theme.GradientEnd
import com.sassa.scamshield.ui.theme.GradientStart
import com.sassa.scamshield.ui.theme.TextPrimary
import com.sassa.scamshield.ui.theme.TextSecondary
import com.sassa.scamshield.ui.viewmodel.NumberLookupViewModel
import com.sassa.scamshield.domain.usecase.CheckNumberUseCase

@Composable
fun NumberLookupScreen(
    onNavigateBack: () -> Unit,
    viewModel: NumberLookupViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
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
            Text(text = "Check Number", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Enter a phone number to check if it has been reported", color = TextSecondary, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = uiState.phoneNumber,
            onValueChange = { viewModel.updatePhoneNumber(it) },
            label = { Text("+27 XX XXX XXXX", color = TextSecondary) },
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Search, contentDescription = null, tint = TextSecondary)
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { viewModel.checkNumber() },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(16.dp),
            enabled = uiState.phoneNumber.isNotBlank() && !uiState.isChecking
        ) {
            Box(
                modifier = Modifier.fillMaxSize().background(
                    brush = Brush.horizontalGradient(colors = listOf(GradientStart, GradientEnd)),
                    shape = RoundedCornerShape(16.dp)
                ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Check Number",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
        if (uiState.error != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = uiState.error!!, color = Color(0xFFFF4444), fontSize = 13.sp)
        }
        uiState.result?.let { result ->
            Spacer(modifier = Modifier.height(24.dp))
            NumberLookupResultCard(result = result)
        }
    }
}

@Composable
fun NumberLookupResultCard(result: CheckNumberUseCase.NumberLookupResult) {
    val riskColor = result.riskLevel.colorValue()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CardBackground)
            .padding(20.dp)
    ) {
        Text(text = result.riskLevel.label.uppercase(), color = riskColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = result.phoneNumber, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Risk Score: ${result.riskScore}%", color = TextSecondary, fontSize = 14.sp)
        if (result.isKnownScam) {
            Text(text = "Reported as scam", color = Color(0xFFEF4444), fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (result.reasons.isNotEmpty()) {
            Text(text = "Why?", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            result.reasons.forEach { reason ->
                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text(text = "b ", color = riskColor, fontSize = 13.sp)
                    Text(text = reason, color = TextSecondary, fontSize = 13.sp)
                }
            }
        }
    }
}
