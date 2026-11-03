package com.plcoding.spendlesssample.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.plcoding.spendlesssample.R

private val Figtree =
    FontFamily(
        Font(R.font.figtree_regular, FontWeight.Normal),
        Font(R.font.figtree_medium, FontWeight.Medium),
        Font(R.font.figtree_semibold, FontWeight.SemiBold),
    )

private val DefaultTypography = Typography()

private fun TextStyle.withFigtree(): TextStyle = copy(fontFamily = Figtree)

val SpendLessTypography =
    Typography(
        displayLarge =
            TextStyle(
                fontFamily = Figtree,
                fontWeight = FontWeight.SemiBold,
                fontSize = 45.sp,
                lineHeight = 52.sp,
                letterSpacing = 0.sp,
            ),
        displayMedium =
            TextStyle(
                fontFamily = Figtree,
                fontWeight = FontWeight.SemiBold,
                fontSize = 36.sp,
                lineHeight = 44.sp,
                letterSpacing = 0.sp,
            ),
        displaySmall = DefaultTypography.displaySmall.withFigtree(),
        headlineLarge =
            TextStyle(
                fontFamily = Figtree,
                fontWeight = FontWeight.SemiBold,
                fontSize = 32.sp,
                lineHeight = 40.sp,
                letterSpacing = 0.1.sp,
            ),
        headlineMedium =
            TextStyle(
                fontFamily = Figtree,
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                lineHeight = 34.sp,
                letterSpacing = 0.1.sp,
            ),
        headlineSmall = DefaultTypography.headlineSmall.withFigtree(),
        titleLarge =
            TextStyle(
                fontFamily = Figtree,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                lineHeight = 26.sp,
                letterSpacing = 0.sp,
            ),
        titleMedium =
            TextStyle(
                fontFamily = Figtree,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.sp,
            ),
        titleSmall =
            TextStyle(
                fontFamily = Figtree,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.sp,
            ),
        bodyLarge = DefaultTypography.bodyLarge.withFigtree(),
        bodyMedium =
            TextStyle(
                fontFamily = Figtree,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.sp,
            ),
        bodySmall =
            TextStyle(
                fontFamily = Figtree,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.sp,
            ),
        labelLarge = DefaultTypography.labelLarge.withFigtree(),
        labelMedium =
            TextStyle(
                fontFamily = Figtree,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.sp,
            ),
        labelSmall =
            TextStyle(
                fontFamily = Figtree,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.sp,
            ),
    )

internal val BodyXSmall =
    TextStyle(
        fontFamily = Figtree,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
    )

internal val LocalBodyXSmall = staticCompositionLocalOf { BodyXSmall }

val Typography.bodyXSmall: TextStyle
    @Composable
    @ReadOnlyComposable
    get() = LocalBodyXSmall.current
