package com.sassa.scamshield.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sassa.scamshield.ui.theme.Amber
import com.sassa.scamshield.ui.theme.CardBackground
import com.sassa.scamshield.ui.theme.CardBackgroundSecondary
import com.sassa.scamshield.ui.theme.GreenSuccess
import com.sassa.scamshield.ui.theme.RedError
import com.sassa.scamshield.ui.theme.TextPrimary
import com.sassa.scamshield.ui.theme.TextSecondary

@Composable
fun MessageAnalysisCard(
    riskScore: Int,
    riskLabel: String,
    riskColor: Color,
    message: String,
    reasons: List<String>,
    modifier: Modifier = Modifier,
    onReportClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onMarkSafeClick: () -> Unit = {},
    onLearnMoreClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CardBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = riskLabel,
            color = riskColor,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Risk Score",
            color = TextSecondary,
            fontSize = 12.sp
        )
        Text(
            text = "$riskScore / 100",
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Why was this flagged?",
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        reasons.forEach { reason ->
            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(text = "• ", color = riskColor, fontSize = 14.sp)
                Text(text = reason, color = TextSecondary, fontSize = 13.sp)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            color = TextPrimary,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(CardBackgroundSecondary)
                .padding(12.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            ActionButton(text = "Report Scam", color = RedError, onClick = onReportClick, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            ActionButton(text = "Delete", color = TextSecondary, onClick = onDeleteClick, modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            ActionButton(text = "Mark as Safe", color = GreenSuccess, onClick = onMarkSafeClick, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            ActionButton(text = "Learn More", color = Amber, onClick = onLearnMoreClick, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = Color.White
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = color.copy(alpha = 0.2f),
            contentColor = color
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}
