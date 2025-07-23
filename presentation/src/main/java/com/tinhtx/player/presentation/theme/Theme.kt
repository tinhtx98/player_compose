// presentation/src/main/kotlin/com/tinhtx/player/theme/Theme.kt
package com.tinhtx.player.presentation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.tinhtx.player.domain.model.AgeGroup
import com.tinhtx.player.domain.model.ThemeMode

@Composable
fun TinhTXTheme(
    ageGroup: AgeGroup = AgeGroup.ADULT,
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> getColorSchemeForAgeGroup(ageGroup, darkTheme)
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = getTypographyForAgeGroup(ageGroup),
        shapes = getShapesForAgeGroup(ageGroup),
        content = content
    )
}

private fun getColorSchemeForAgeGroup(ageGroup: AgeGroup, darkTheme: Boolean) = when (ageGroup) {
    AgeGroup.CHILD -> if (darkTheme) ChildDarkColorScheme else ChildLightColorScheme
    AgeGroup.TEEN -> if (darkTheme) TeenDarkColorScheme else TeenLightColorScheme
    AgeGroup.ADULT -> if (darkTheme) AdultDarkColorScheme else AdultLightColorScheme
}

private fun getTypographyForAgeGroup(ageGroup: AgeGroup): Typography {
    val baseSize = ageGroup.baseFontSize
    return Typography(
        displayLarge = TextStyle(
            fontSize = (baseSize * 3.5f).sp,
            fontWeight = FontWeight.Normal
        ),
        displayMedium = TextStyle(
            fontSize = (baseSize * 2.8f).sp,
            fontWeight = FontWeight.Normal
        ),
        displaySmall = TextStyle(
            fontSize = (baseSize * 2.2f).sp,
            fontWeight = FontWeight.Normal
        ),
        headlineLarge = TextStyle(
            fontSize = (baseSize * 2.0f).sp,
            fontWeight = FontWeight.Normal
        ),
        headlineMedium = TextStyle(
            fontSize = (baseSize * 1.7f).sp,
            fontWeight = FontWeight.Normal
        ),
        headlineSmall = TextStyle(
            fontSize = (baseSize * 1.5f).sp,
            fontWeight = FontWeight.Normal
        ),
        titleLarge = TextStyle(
            fontSize = (baseSize * 1.3f).sp,
            fontWeight = FontWeight.Normal
        ),
        titleMedium = TextStyle(
            fontSize = (baseSize * 1.1f).sp,
            fontWeight = FontWeight.Normal
        ),
        titleSmall = TextStyle(
            fontSize = baseSize.sp,
            fontWeight = FontWeight.Normal
        ),
        bodyLarge = TextStyle(
            fontSize = baseSize.sp,
            fontWeight = FontWeight.Normal
        ),
        bodyMedium = TextStyle(
            fontSize = (baseSize * 0.9f).sp,
            fontWeight = FontWeight.Normal
        ),
        bodySmall = TextStyle(
            fontSize = (baseSize * 0.8f).sp,
            fontWeight = FontWeight.Normal
        ),
        labelLarge = TextStyle(
            fontSize = (baseSize * 0.9f).sp,
            fontWeight = FontWeight.Normal
        ),
        labelMedium = TextStyle(
            fontSize = (baseSize * 0.8f).sp,
            fontWeight = FontWeight.Normal
        ),
        labelSmall = TextStyle(
            fontSize = (baseSize * 0.7f).sp,
            fontWeight = FontWeight.Normal
        )
    )
}

private fun getShapesForAgeGroup(ageGroup: AgeGroup): Shapes {
    val cornerRadius = ageGroup.cornerRadius.dp
    return Shapes(
        extraSmall = androidx.compose.foundation.shape.RoundedCornerShape(cornerRadius * 0.25f),
        small = androidx.compose.foundation.shape.RoundedCornerShape(cornerRadius * 0.5f),
        medium = androidx.compose.foundation.shape.RoundedCornerShape(cornerRadius),
        large = androidx.compose.foundation.shape.RoundedCornerShape(cornerRadius * 1.5f),
        extraLarge = androidx.compose.foundation.shape.RoundedCornerShape(cornerRadius * 2f)
    )
}
