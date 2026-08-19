@file:OptIn(ExperimentalMaterial3Api::class)

package com.sassa.scamshield.ui.screens

import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sassa.scamshield.ui.theme.CardBackground
import com.sassa.scamshield.ui.theme.DarkCharcoal
import com.sassa.scamshield.ui.theme.GradientEnd
import com.sassa.scamshield.ui.theme.GradientStart
import com.sassa.scamshield.ui.theme.TextPrimary
import com.sassa.scamshield.ui.theme.TextSecondary
import androidx.compose.material.icons.outlined.Check
import com.sassa.scamshield.ui.viewmodel.ReportScamViewModel
import com.sassa.scamshield.ui.viewmodel.ReportType
import com.sassa.scamshield.util.ScamCategory

@Composable
fun ReportScamScreen(
    onNavigateBack: () -> Unit,
    onReportSubmitted: () -> Unit,
    viewModel: ReportScamViewModel
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
            Text(text = "Report a Scam", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isSubmitted) {
            SuccessState(onNavigateBack = onNavigateBack)
        } else {
            Text(text = "What would you like to report?", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))

            ReportTypeSelector(
                selectedType = uiState.reportType,
                onSelect = { viewModel.updateReportType(it) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            when (uiState.reportType) {
                ReportType.PHONE_NUMBER -> {
                    OutlinedTextField(
                        value = uiState.phoneNumber,
                        onValueChange = { viewModel.updatePhoneNumber(it) },
                        label = { Text("+27 XX XXX XXXX", color = TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CardBackground,
                            unfocusedContainerColor = CardBackground,
                            focusedBorderColor = GradientStart,
                            unfocusedBorderColor = Color(0xFF2D2D2D),
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                }
                ReportType.SMS_MESSAGE -> {
                    OutlinedTextField(
                        value = uiState.messageContent,
                        onValueChange = { viewModel.updateMessageContent(it) },
                        label = { Text("Message content", color = TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CardBackground,
                            unfocusedContainerColor = CardBackground,
                            focusedBorderColor = GradientStart,
                            unfocusedBorderColor = Color(0xFF2D2D2D),
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        minLines = 4,
                        maxLines = 10
                    )
                }
                ReportType.SUSPICIOUS_LINK -> {
                    OutlinedTextField(
                        value = uiState.link,
                        onValueChange = { viewModel.updateLink(it) },
                        label = { Text("https://example.com", color = TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CardBackground,
                            unfocusedContainerColor = CardBackground,
                            focusedBorderColor = GradientStart,
                            unfocusedBorderColor = Color(0xFF2D2D2D),
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            CategoryDropdown(
                selectedCategory = uiState.category,
                onCategorySelected = { viewModel.updateCategory(it) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.description,
                onValueChange = { viewModel.updateDescription(it) },
                label = { Text("Additional description (required)", color = TextSecondary) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardBackground,
                    unfocusedContainerColor = CardBackground,
                    focusedBorderColor = GradientStart,
                    unfocusedBorderColor = Color(0xFF2D2D2D),
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                minLines = 2,
                maxLines = 5
            )
            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.error != null) {
                Text(text = uiState.error!!, color = Color(0xFFFF4444), fontSize = 13.sp)
                Spacer(modifier = Modifier.height(12.dp))
            }

            Button(
                onClick = { viewModel.submitReport() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(16.dp),
                enabled = !uiState.isSubmitting
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().background(
                        brush = Brush.horizontalGradient(colors = listOf(GradientStart, GradientEnd)),
                        shape = RoundedCornerShape(16.dp)
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (uiState.isSubmitting) "Submitting..." else "Submit Report",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ReportTypeSelector(
    selectedType: ReportType,
    onSelect: (ReportType) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ReportTypeCard(
            text = "Phone Number",
            isSelected = selectedType == ReportType.PHONE_NUMBER,
            onClick = { onSelect(ReportType.PHONE_NUMBER) },
            modifier = Modifier.weight(1f)
        )
        ReportTypeCard(
            text = "SMS Message",
            isSelected = selectedType == ReportType.SMS_MESSAGE,
            onClick = { onSelect(ReportType.SMS_MESSAGE) },
            modifier = Modifier.weight(1f)
        )
        ReportTypeCard(
            text = "Suspicious Link",
            isSelected = selectedType == ReportType.SUSPICIOUS_LINK,
            onClick = { onSelect(ReportType.SUSPICIOUS_LINK) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ReportTypeCard(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = if (isSelected) GradientStart else CardBackground
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else TextSecondary,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun CategoryDropdown(
    selectedCategory: ScamCategory,
    onCategorySelected: (ScamCategory) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedCategory.displayName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Scam Category", color = TextSecondary) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CardBackground,
                unfocusedContainerColor = CardBackground,
                focusedBorderColor = GradientStart,
                unfocusedBorderColor = Color(0xFF2D2D2D),
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(CardBackground)
        ) {
            ScamCategory.values().forEach { category ->
                DropdownMenuItem(
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    },
                    text = {
                        Text(text = category.displayName, color = TextPrimary, fontSize = 14.sp)
                    }
                )
            }
        }
    }
}

@Composable
fun SuccessState(onNavigateBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(48.dp))
                .background(Color(0xFF22C55E).copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = null,
                tint = Color(0xFF22C55E),
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Thank you!", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Your report helps protect other users.", color = TextSecondary, fontSize = 14.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onNavigateBack,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = "Back to Reports", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
