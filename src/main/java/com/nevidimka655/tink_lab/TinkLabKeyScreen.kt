package com.nevidimka655.tink_lab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.nevidimka655.tink_lab.data.AeadTypesFiles
import com.nevidimka655.tink_lab.data.AeadTypesText
import com.nevidimka655.tink_lab.domain.model.DataItem
import com.nevidimka655.tink_lab.domain.model.DataType
import com.nevidimka655.tink_lab.menu.AeadTypeMenu
import com.nevidimka655.tink_lab.menu.DataTypeMenu
import com.nevidimka655.ui.compose_core.FilledTonalButtonWithIcon
import com.nevidimka655.ui.compose_core.ext.LocalWindowWidth
import com.nevidimka655.ui.compose_core.ext.isCompact
import com.nevidimka655.ui.compose_core.theme.spaces

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TinkLabKeyScreen(
    modifier: Modifier = Modifier,
    keysetHash: String = "keyset_hash",
    aeadTypeLabel: String = "Encryption type",
    dataTypeLabel: String = "Data type",
    keysetKeyLabel: String = "Keyset key",
    dataTypes: List<DataItem> = listOf(
        DataItem("Files", DataType.Files),
        DataItem("Text", DataType.Text)
    ),
    selectedDataType: DataItem = dataTypes[0],
    onSelectDataType: (Int) -> Unit = {},
    onSelectAeadType: (String) -> Unit = {},
    keysetKey: String = "",
    onChangeKeysetKey: (String) -> Unit = {},
    buttonLoadText: String = "Load",
    buttonSaveText: String = "Save"
) = Box(
    modifier = modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()),
    contentAlignment = Alignment.Center
) {
    val defaultVerticalArrangement = Arrangement.spacedBy(MaterialTheme.spaces.spaceMedium)
    val defaultHorizontalArrangement = Arrangement.spacedBy(MaterialTheme.spaces.spaceSmall)
    ElevatedCard {
        Column(
            modifier = Modifier.padding(MaterialTheme.spaces.spaceMedium),
            verticalArrangement = defaultVerticalArrangement,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var showDataTypeMenu by remember { mutableStateOf(false) }

            @Composable
            fun dataTypeMenu() = DataTypeMenu(
                expanded = showDataTypeMenu,
                text = selectedDataType.title,
                label = dataTypeLabel,
                onExpandedChange = { showDataTypeMenu = it },
                items = dataTypes,
                onSelect = onSelectDataType
            )

            var showAeadTypeMenu by remember { mutableStateOf(false) }
            val aeadTypes = remember(selectedDataType) {
                when (selectedDataType.type) {
                    DataType.Files -> AeadTypesFiles
                    DataType.Text -> AeadTypesText
                }
            }
            var selectedAeadType by rememberSaveable(selectedDataType) { mutableStateOf(aeadTypes.first()) }
            LaunchedEffect(selectedAeadType) { onSelectAeadType(selectedAeadType.uppercase()) }

            @Composable
            fun aeadTypeMenu() = AeadTypeMenu(
                expanded = showAeadTypeMenu,
                text = selectedAeadType,
                label = aeadTypeLabel,
                onExpandedChange = { showAeadTypeMenu = it },
                items = aeadTypes,
                onSelect = { selectedAeadType = it }
            )

            @Composable
            fun keysetKeyField() = KeysetKeyTextField(
                value = keysetKey,
                onValueChange = onChangeKeysetKey,
                label = keysetKeyLabel
            )

            @Composable
            fun toolbar(modifier: Modifier = Modifier) = Row(
                horizontalArrangement = defaultHorizontalArrangement,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
            ) {
                ToolbarButton(imageVector = Icons.Default.Add, text = buttonLoadText)
                ToolbarButton(imageVector = Icons.Default.Save, text = buttonSaveText)
            }

            @Composable
            fun keysetInfo() = Text(keysetHash)

            val localWindowWidth = LocalWindowWidth.current
            if (localWindowWidth.isCompact) {
                keysetKeyField()
                dataTypeMenu()
                aeadTypeMenu()
                toolbar()
                keysetInfo()
            } else {
                Row(horizontalArrangement = defaultHorizontalArrangement) {
                    val horizontalAlignment = Alignment.CenterHorizontally
                    Column(
                        verticalArrangement = defaultVerticalArrangement,
                        horizontalAlignment = horizontalAlignment
                    ) {
                        keysetKeyField()
                        dataTypeMenu()
                    }
                    Column(
                        verticalArrangement = defaultVerticalArrangement,
                        horizontalAlignment = horizontalAlignment
                    ) {
                        aeadTypeMenu()
                        toolbar(modifier = Modifier.height(TextFieldDefaults.MinHeight))
                    }
                }
                keysetInfo()
            }
        }
    }
}

@Composable
private fun KeysetKeyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
) {
    var passwordToggleState by remember { mutableStateOf(false) }
    TextField(
        value = value,
        onValueChange = onValueChange,
        leadingIcon = { Icon(imageVector = Icons.Default.Key, null) },
        supportingText = {
            Text(
                text = "${value.length}",
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        },
        label = { Text(text = label) },
        trailingIcon = {
            IconButton(onClick = { passwordToggleState = !passwordToggleState }) {
                Icon(
                    imageVector = if (!passwordToggleState) {
                        Icons.Default.VisibilityOff
                    } else Icons.Default.Visibility,
                    contentDescription = null
                )
            }
        },
        visualTransformation = if (!passwordToggleState) {
            PasswordVisualTransformation()
        } else VisualTransformation.None,
        singleLine = true
    )
}

@Preview
@Composable
private fun ToolbarButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector = Icons.Default.Save,
    text: String = "Button",
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) = FilledTonalButtonWithIcon(
    modifier = modifier,
    enabled = enabled,
    icon = imageVector,
    title = text,
    onClick = onClick
)