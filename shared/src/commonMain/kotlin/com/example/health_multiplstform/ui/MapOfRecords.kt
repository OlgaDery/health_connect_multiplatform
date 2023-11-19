package com.example.health_multiplstform.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
expect fun MapToEnterLocation(
    modifier: Modifier,
    size: Dp,
    viewModel: RecordEntryViewModel
)