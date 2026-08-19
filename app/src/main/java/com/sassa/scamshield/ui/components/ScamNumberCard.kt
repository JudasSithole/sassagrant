package com.sassa.scamshield.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sassa.scamshield.domain.model.ScamNumber
import com.sassa.scamshield.ui.theme.CardBackground
import com.sassa.scamshield.ui.theme.CardBackgroundSecondary
import com.sassa.scamshield.ui.theme.TextPrimary
import com.sassa.scamshield.ui.theme.TextSecondary

@Composable
fun ScamNumberCard(
    number: ScamNumber,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val riskColor = number.riskLevel().colorValue()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBackground)
            .let { if (onClick != null) it.clickable(onClick = { onClick() }) else it }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(riskColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Outlined.Phone,
                    contentDescription = null,
                    tint = riskColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = number.displayNumber(),
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Text(
                    text = "${number.category.displayName} • ${number.reportCount} reports",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
            RiskScoreBadge(score = number.riskScore, modifier = Modifier)
        }
    }
}
