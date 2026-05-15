package com.plcoding.spendlesssample.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.plcoding.spendlesssample.R

@Preview(showBackground = true, heightDp = 1400)
@Composable
private fun DesignSystemColorPreview() {
    SpendLessSampleTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Swatch("primary", MaterialTheme.colorScheme.primary)
            Swatch("primaryContainer", MaterialTheme.colorScheme.primaryContainer)
            Swatch("secondary", MaterialTheme.colorScheme.secondary)
            Swatch("secondaryContainer", MaterialTheme.colorScheme.secondaryContainer)
            Swatch("tertiaryContainer", MaterialTheme.colorScheme.tertiaryContainer)
            Swatch("error", MaterialTheme.colorScheme.error)
            Swatch("background", MaterialTheme.colorScheme.background)
            Swatch("surface", MaterialTheme.colorScheme.surface)
            Swatch("surfaceContainer", MaterialTheme.colorScheme.surfaceContainer)
            Swatch("onSurface", MaterialTheme.colorScheme.onSurface)
            Swatch("outline", MaterialTheme.colorScheme.outline)
            Swatch("inverseSurface", MaterialTheme.colorScheme.inverseSurface)
            Swatch("success (extra)", MaterialTheme.spendLessColors.success)
        }
    }
}

@Composable
private fun Swatch(name: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(color),
        )
        Text(
            text = name,
            modifier = Modifier.padding(start = 12.dp),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview(showBackground = true, heightDp = 1200)
@Composable
private fun DesignSystemTypographyPreview() {
    SpendLessSampleTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text("displayLarge", style = MaterialTheme.typography.displayLarge)
            Text("displayMedium", style = MaterialTheme.typography.displayMedium)
            Text("headlineLarge", style = MaterialTheme.typography.headlineLarge)
            Text("headlineMedium", style = MaterialTheme.typography.headlineMedium)
            Text("titleLarge", style = MaterialTheme.typography.titleLarge)
            Text("titleMedium", style = MaterialTheme.typography.titleMedium)
            Text("titleSmall", style = MaterialTheme.typography.titleSmall)
            Text("labelMedium", style = MaterialTheme.typography.labelMedium)
            Text("labelSmall", style = MaterialTheme.typography.labelSmall)
            Text("bodyMedium", style = MaterialTheme.typography.bodyMedium)
            Text("bodySmall", style = MaterialTheme.typography.bodySmall)
            Text("bodyXSmall", style = MaterialTheme.typography.bodyXSmall)
        }
    }
}

@Preview(showBackground = true, heightDp = 600)
@Composable
private fun DesignSystemIconsPreview() {
    SpendLessSampleTheme {
        LazyVerticalGrid(
            columns = GridCells.Fixed(6),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(560.dp),
        ) {
            items(IconResIds) { resId ->
                Icon(
                    painter = painterResource(resId),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp),
                )
            }
        }
    }
}

private val IconResIds = listOf(
    R.drawable.ic_arrow_back,
    R.drawable.ic_arrow_forward,
    R.drawable.ic_check,
    R.drawable.ic_close,
    R.drawable.ic_add,
    R.drawable.ic_arrow_drop_up,
    R.drawable.ic_arrow_drop_down,
    R.drawable.ic_navigate_before,
    R.drawable.ic_navigate_next,
    R.drawable.ic_download,
    R.drawable.ic_today,
    R.drawable.ic_settings,
    R.drawable.ic_check_indeterminate_small,
    R.drawable.ic_backspace,
    R.drawable.ic_fingerprint,
    R.drawable.ic_logout,
    R.drawable.ic_lock,
    R.drawable.ic_notes,
    R.drawable.ic_account_balance_wallet,
    R.drawable.ic_trending_down,
    R.drawable.ic_trending_up,
)
