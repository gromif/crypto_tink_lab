package com.nevidimka655.tink_lab.menu

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AeadTypeMenu(
    expanded: Boolean = false,
    text: String = "Field",
    label: String = "Label",
    items: List<String>,
    onExpandedChange: (Boolean) -> Unit = {},
    onSelect: (String) -> Unit = {}
) = ExposedDropdownMenuBox(
    expanded = expanded,
    onExpandedChange = onExpandedChange
) {
    OutlinedTextField(
        value = text,
        onValueChange = {},
        readOnly = true,
        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
        label = { Text(text = label) },
        modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
    )

    ExposedDropdownMenu(
        expanded = expanded,
        onDismissRequest = { onExpandedChange(false) }
    ) {
        items.forEach {
            DropdownMenuItem(
                text = { Text(text = it) },
                onClick = {
                    onSelect(it)
                    onExpandedChange(false)
                }
            )
        }
    }
}