package com.example.quicknote.presentation.component


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.quicknote.R
import com.example.quicknote.presentation.theme.NoteTheme

@Composable
fun CustomChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(label, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        },
        leadingIcon = if (selected) {
            {
                Icon(
                    painter = painterResource(R.drawable.check_24dp),
                    contentDescription = stringResource(R.string.apply_sort),
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
        shape = RoundedCornerShape(15.dp),
        border = FilterChipDefaults.filterChipBorder(
            enabled = false,
            selected = false,
            borderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            borderWidth = 0.dp,
            selectedBorderWidth = 0.dp
        ),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = NoteTheme.colors.backgroundSecondary,
            labelColor = NoteTheme.colors.textPrimary,
            selectedContainerColor = NoteTheme.colors.backgroundBrand,
            iconColor = NoteTheme.colors.textPrimary,
            selectedLabelColor = NoteTheme.colors.textPrimary,
            disabledContainerColor = NoteTheme.colors.backgroundSecondary,
            selectedLeadingIconColor = NoteTheme.colors.textPrimary,
            disabledLeadingIconColor = NoteTheme.colors.textPrimary,
            disabledSelectedContainerColor = NoteTheme.colors.backgroundSecondary
        ),
        modifier = modifier
    )
}