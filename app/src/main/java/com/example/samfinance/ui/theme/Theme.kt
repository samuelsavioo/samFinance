package com.example.samfinance.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = ElectricBluePrimary,
    onPrimary = Color.Black,
    primaryContainer = ElectricBlueVariant,
    onPrimaryContainer = Color.White,
    secondary = AccentCyan,
    onSecondary = Color.Black,
    background = BackgroundDeepBlack,
    onBackground = TextPrimaryLight,
    surface = BackgroundCharcoal,
    onSurface = TextPrimaryLight,
    surfaceVariant = GlassSurface,
    onSurfaceVariant = TextSecondaryMuted,
    outline = GlassBorder
)

@Composable
fun SamFinanceTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val border = BorderStroke(1.dp, GlassBorder)
    val shape = RoundedCornerShape(16.dp)

    if (onClick != null) {
        Surface(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            color = GlassCard,
            border = border,
            content = { Column(content = content) }
        )
    } else {
        Surface(
            modifier = modifier,
            shape = shape,
            color = GlassCard,
            border = border,
            content = { Column(content = content) }
        )
    }
}
