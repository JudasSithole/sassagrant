package com.sassa.scamshield.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sassa.scamshield.ui.components.EducationCard
import com.sassa.scamshield.ui.theme.DarkCharcoal
import com.sassa.scamshield.ui.theme.Purple500
import com.sassa.scamshield.ui.theme.TextPrimary
import com.sassa.scamshield.ui.theme.TextSecondary

data class EducationArticle(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val iconTint: Color = Purple500,
    val content: String = ""
)

val educationArticles = listOf(
    EducationArticle(
        title = "How SASSA Scams Work",
        description = "Learn common tactics used by scammers targeting SASSA beneficiaries",
        icon = Icons.Outlined.Info,
        iconTint = Color(0xFFEF4444)
    ),
    EducationArticle(
        title = "Identifying Fake Grant Messages",
        description = "Red flags to watch for in suspicious SMS messages",
        icon = Icons.Outlined.Warning,
        iconTint = Color(0xFFFFA947)
    ),
    EducationArticle(
        title = "Never Share Your OTP",
        description = "Why you should never give your one-time password to anyone",
        icon = Icons.Outlined.Lock,
        iconTint = Color(0xFF3B82F6)
    ),
    EducationArticle(
        title = "Fake SASSA Links",
        description = "How to identify fraudulent websites and links",
        icon = Icons.Outlined.LinkOff,
        iconTint = Color(0xFFEF4444)
    ),
    EducationArticle(
        title = "Government Scammers",
        description = "Recognizing impersonation attempts by fake officials",
        icon = Icons.Outlined.Badge,
        iconTint = Color(0xFFF9320E)
    ),
    EducationArticle(
        title = "What to Do If You Clicked",
        description = "Steps to take if you accidentally clicked a suspicious link",
        icon = Icons.Outlined.HealthAndSafety,
        iconTint = Color(0xFF22C55E)
    )
)

@Composable
fun EducationScreen(
    onNavigateBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
            .verticalScroll(scrollState)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Icon(
                imageVector = Icons.Outlined.ArrowBack,
                contentDescription = "Back",
                tint = TextPrimary,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onNavigateBack)
            )
            Text(
                text = "Scam Education",
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Learn to recognize common scams and protect yourself and your family.",
            color = TextSecondary,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            educationArticles.forEachIndexed { index, article ->
                EducationCard(
                    title = article.title,
                    description = article.description,
                    icon = article.icon,
                    iconTint = article.iconTint,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                if (index < educationArticles.size - 1) {
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}