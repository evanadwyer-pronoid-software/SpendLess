package com.plcoding.spendlesssample.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun SpendLessSampleTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalSpendLessExtraColors provides SpendLessLightExtraColors,
        LocalBodyXSmall provides BodyXSmall,
    ) {
        MaterialTheme(
            colorScheme = SpendLessLightColorScheme,
            typography = SpendLessTypography,
            content = content,
        )
    }
}
