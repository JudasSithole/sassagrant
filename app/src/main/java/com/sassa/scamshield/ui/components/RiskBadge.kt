package com.sassa.scamshield.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sassa.scamshield.util.RiskLevel
import com.sassa.scamshield.ui.theme.CardBackground
import com.sassa.scamshield.ui.theme.TextPrimary
import com.sassa.scamshield.ui.theme.TextSecondary

@Composable
fun RiskBadge(riskLevel: RiskLevel, modifier: Modifier = Modifier) {
    val bgColor = riskLevel.colorValue().copy(alpha = 0.2f)
    val borderColor = riskLevel.colorValue()
    val textColor = riskLevel.colorValue()
    val label = when (riskLevel) {
        RiskLevel.Safe -> "SAFE"
        RiskLevel.ProbablySafe -> "PROBABLY SAFE"
        RiskLevel.LowRisk -> "LOW RISK"
        RiskLevel.Suspicious -> "SUSPICIOUS"
        RiskLevel.HighRisk -> "HIGH RISK"
        RiskLevel.KnownScam -> "KNOWN SCAM"
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun RiskScoreBadge(score: Int, modifier: Modifier = Modifier) {
    val level = RiskLevel.fromScore(score)
    RiskBadge(riskLevel = level, modifier = modifier)
}
