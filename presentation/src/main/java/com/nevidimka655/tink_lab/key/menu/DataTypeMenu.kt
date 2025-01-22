package com.nevidimka655.tink_lab.key.menu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileOpen
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nevidimka655.tink_lab.domain.model.DataItem
import com.nevidimka655.tink_lab.domain.model.DataType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DataTypeMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    enabled: Boolean = true,
    text: String = "Field",
    label: String = "Label",
    items: List<DataItem>,
    onExpandedChange: (Boolean) -> Unit = {},
    onSelect: (DataType) -> Unit = {},
) = ExposedDropdownMenuBox(
    expanded = expanded,
    onExpandedChange = onExpandedChange
) {
    OutlinedTextField(
        value = text,
        onValueChange = {},
        enabled = enabled,
        readOnly = true,
        leadingIcon = { Icon(imageVector = Icons.Outlined.FileOpen, null) },
        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
        label = { Text(text = label) },
        modifier = modifier.menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
    )

    ExposedDropdownMenu(
        expanded = expanded,
        onDismissRequest = { onExpandedChange(false) }
    ) {
        items.forEach {
            DropdownMenuItem(
                text = { Text(text = stringResource(id = it.titleResId)) },
                onClick = {
                    onSelect(it.type)
                    onExpandedChange(false)
                }
            )
        }
    }
}