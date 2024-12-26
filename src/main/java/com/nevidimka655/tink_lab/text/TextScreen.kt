package com.nevidimka655.tink_lab.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NoEncryption
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.nevidimka655.astracrypt.resources.R
import com.nevidimka655.tink_lab.TinkLab
import com.nevidimka655.tink_lab.shared.ToolbarButton
import com.nevidimka655.ui.compose_core.OutlinedButtonWithIcon
import com.nevidimka655.ui.compose_core.ext.LocalWindowWidth
import com.nevidimka655.ui.compose_core.ext.isCompact
import com.nevidimka655.ui.compose_core.theme.spaces

@Composable
fun TinkLab.TextScreen(modifier: Modifier = Modifier) {
    val vm: TextViewModel = hiltViewModel()

    Screen(modifier = modifier)
}

@Preview(showBackground = true)
@Composable
private fun Screen(
    modifier: Modifier = Modifier,
    associatedData: String = "PREVIEW_AD",
    onAssociatedDataChange: (String) -> Unit = {},
    text: String = "PREVIEW_TEXT",
    onTextChange: (String) -> Unit = {},
    onPasteClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onCopyClick: () -> Unit = {}
) = Column(
    modifier = modifier.padding(MaterialTheme.spaces.spaceMedium),
    verticalArrangement =
        Arrangement.spacedBy(MaterialTheme.spaces.spaceMedium, Alignment.CenterVertically)
) {
    val defaultHorizontalArrangement = Arrangement.spacedBy(MaterialTheme.spaces.spaceSmall)

    @Composable
    fun encryptionToolbar(modifier: Modifier = Modifier) = Row(
        horizontalArrangement = defaultHorizontalArrangement,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        ToolbarButton(
            imageVector = Icons.Default.Lock,
            text = stringResource(id = R.string.encrypt),
            modifier = Modifier.weight(0.5f)
        )
        ToolbarButton(
            imageVector = Icons.Default.NoEncryption,
            text = stringResource(id = R.string.decrypt),
            modifier = Modifier.weight(0.5f)
        )
    }

    @Composable
    fun clipboardToolbar(modifier: Modifier = Modifier) = Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedIconButton(
            onClick = onPasteClick
        ) { Icon(imageVector = Icons.Default.ContentPaste, contentDescription = null) }
        OutlinedIconButton(
            onClick = onClearClick
        ) { Icon(imageVector = Icons.Default.Clear, contentDescription = null) }
        OutlinedIconButton(
            onClick = onCopyClick
        ) { Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null) }
    }

    @Composable
    fun clipboardToolbarExpanded(modifier: Modifier = Modifier) = Row(
        horizontalArrangement = defaultHorizontalArrangement,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        OutlinedButtonWithIcon(
            icon = Icons.Default.ContentPaste,
            title = stringResource(id = android.R.string.paste),
            modifier = Modifier.weight(1f),
            onClick = onPasteClick
        )
        OutlinedButtonWithIcon(
            icon = Icons.Default.Clear,
            title = stringResource(id = R.string.clear),
            modifier = Modifier.weight(0.8f),
            onClick = onClearClick
        )
        OutlinedButtonWithIcon(
            icon = Icons.Default.ContentCopy,
            title = stringResource(id = android.R.string.copy),
            modifier = Modifier.weight(1f),
            onClick = onCopyClick
        )
    }

    val localWindowWidth = LocalWindowWidth.current
    encryptionToolbar()
    AssociatedDataTextField(
        modifier = Modifier.fillMaxWidth(),
        value = associatedData,
        onValueChange = onAssociatedDataChange
    )
    if (localWindowWidth.isCompact) clipboardToolbar()
    else clipboardToolbarExpanded()
    UserTextField(value = text, onValueChange = onTextChange)
}

@Composable
private fun AssociatedDataTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) = OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    leadingIcon = { Icon(imageVector = Icons.Default.Dataset, null) },
    supportingText = {
        Text(
            text = "${value.length}",
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
    },
    label = { Text(text = stringResource(id = R.string.associatedData)) },
    singleLine = true,
    modifier = modifier
)

@Composable
private fun UserTextField(value: String, onValueChange: (String) -> Unit) = OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    label = { Text(text = stringResource(id = R.string.text)) },
    modifier = Modifier.fillMaxWidth()
)