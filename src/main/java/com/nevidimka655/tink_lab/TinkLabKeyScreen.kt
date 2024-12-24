package com.nevidimka655.tink_lab

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nevidimka655.astracrypt.resources.R
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
import kotlinx.coroutines.channels.Channel

private val dataTypesList = listOf(
    DataItem(R.string.files, DataType.Files),
    DataItem(R.string.text, DataType.Text)
)

@Composable
fun TinkLabKeyScreen(
    modifier: Modifier = Modifier,
    onRequestKeysetChannel: Channel<Unit>,
    onFinish: () -> Unit
) {
    val vm: TinkLabKeyViewModel = hiltViewModel()

    var selectedDataTypeIndex by rememberSaveable { mutableIntStateOf(0) }
    var keysetPassword by rememberSaveable { mutableStateOf("") }
    var aeadType by rememberSaveable { mutableStateOf("") }
    val keyset by vm.keyState.collectAsStateWithLifecycle()

    LaunchedEffect(keysetPassword, aeadType) {
        if (aeadType.isNotEmpty()) vm.shuffleKeyset(
            keysetPassword = keysetPassword,
            dataType = dataTypesList[selectedDataTypeIndex].type,
            aeadType = aeadType
        )
    }

    val openContract = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {

    }

    Screen(
        modifier = modifier,
        keysetHash = keyset.hash.take(16),
        dataTypes = dataTypesList,
        selectedDataType = dataTypesList[selectedDataTypeIndex],
        onSelectDataType = { selectedDataTypeIndex = it },
        onSelectAeadType = { aeadType = it },
        onLoadClick = { openContract.launch(arrayOf("text/plain")) },
        keysetKey = keysetPassword,
        onChangeKeysetKey = { keysetPassword = it }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun Screen(
    modifier: Modifier = Modifier,
    keysetHash: String = "keyset_hash",
    dataTypes: List<DataItem> = dataTypesList,
    selectedDataType: DataItem = dataTypes[0],
    onSelectDataType: (Int) -> Unit = {},
    onSelectAeadType: (String) -> Unit = {},
    onLoadClick: () -> Unit = {},
    keysetKey: String = "",
    onChangeKeysetKey: (String) -> Unit = {}
) = Box(
    modifier = modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()),
    contentAlignment = Alignment.Center
) {
    ElevatedCard {
        val localWindowWidth = LocalWindowWidth.current
        val defaultVerticalArrangement = Arrangement.spacedBy(
            MaterialTheme.spaces.spaceMedium, Alignment.CenterVertically
        )
        val defaultHorizontalArrangement = Arrangement.spacedBy(MaterialTheme.spaces.spaceSmall)
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(MaterialTheme.spaces.spaceMedium),
            verticalArrangement = defaultVerticalArrangement,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var showDataTypeMenu by remember { mutableStateOf(false) }

            @Composable
            fun dataTypeMenu(modifier: Modifier = Modifier.fillMaxWidth()) = DataTypeMenu(
                modifier = modifier,
                expanded = showDataTypeMenu,
                text = stringResource(id = selectedDataType.titleResId),
                label = stringResource(id = R.string.lab_dataType),
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
                label = stringResource(id = R.string.encryption_type),
                onExpandedChange = { showAeadTypeMenu = it },
                items = aeadTypes,
                onSelect = { selectedAeadType = it }
            )

            @Composable
            fun keysetKeyField() = KeysetKeyTextField(
                value = keysetKey,
                onValueChange = onChangeKeysetKey,
                label = stringResource(id = R.string.lab_keySetPassword)
            )

            @Composable
            fun toolbar(modifier: Modifier = Modifier) = Row(
                horizontalArrangement = defaultHorizontalArrangement,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
            ) {
                ToolbarButton(
                    imageVector = Icons.Default.Add,
                    text = stringResource(id = R.string.load),
                    modifier = Modifier.weight(0.5f),
                    onClick = onLoadClick
                )
                ToolbarButton(
                    imageVector = Icons.Default.Save,
                    text = stringResource(id = R.string.save),
                    modifier = Modifier.weight(0.5f)
                )
            }

            @Composable
            fun keysetInfo() = Text(keysetHash)

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
                        modifier = Modifier.weight(0.5f),
                        verticalArrangement = defaultVerticalArrangement,
                        horizontalAlignment = horizontalAlignment
                    ) {
                        keysetKeyField()
                        dataTypeMenu()
                    }
                    Column(
                        modifier = Modifier.weight(0.5f),
                        verticalArrangement = defaultVerticalArrangement,
                        horizontalAlignment = horizontalAlignment
                    ) {
                        aeadTypeMenu()
                        toolbar(
                            modifier = Modifier.height(TextFieldDefaults.MinHeight)
                        )
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
    OutlinedTextField(
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
                    } else Icons.Default.Visibility, contentDescription = null
                )
            }
        },
        visualTransformation = if (!passwordToggleState) {
            PasswordVisualTransformation()
        } else VisualTransformation.None,
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
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
    modifier = modifier, enabled = enabled, icon = imageVector, title = text, onClick = onClick
)