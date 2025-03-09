package io.gromif.tink_lab.presentation.text

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.gromif.astracrypt.resources.R
import io.gromif.tink_lab.presentation.TinkLab
import io.gromif.tink_lab.presentation.shared.AssociatedDataTextField
import io.gromif.tink_lab.presentation.shared.EncryptionToolbar
import io.gromif.ui.compose.core.OutlinedButtonWithIcon
import io.gromif.ui.compose.core.ext.LocalWindowWidth
import io.gromif.ui.compose.core.ext.isCompact
import io.gromif.ui.compose.core.theme.spaces
import kotlinx.coroutines.launch

@Composable
fun TinkLab.TextScreen(
    modifier: Modifier = Modifier,
    rawKeyset: String
) {
    val vm: TextViewModel = hiltViewModel()
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) { vm.parseKeysetHandle(rawKeyset = rawKeyset) }

    val associatedData by vm.associatedDataState.collectAsStateWithLifecycle()
    val text by vm.textState.collectAsStateWithLifecycle()

    Screen(
        modifier = modifier,
        associatedData = associatedData,
        onAssociatedDataChange = { vm.setAssociatedData(data = it) },
        text = text,
        onTextChange = { vm.setText(text = it) },
        onPasteClick = {
            val textInClipboard = clipboardManager.getText()
            if (textInClipboard != null) vm.setText(textInClipboard.text)
        },
        onClearClick = { vm.setText(text = "") },
        onCopyClick = {
            val annotatedString = buildAnnotatedString { append(text) }
            clipboardManager.setText(annotatedString)
        },
        onEncrypt = { vm.encrypt() },
        onDecrypt = {
            scope.launch {
                val decryptResult = vm.decrypt()
                if (!decryptResult) Toast.makeText(
                    context, R.string.error, Toast.LENGTH_SHORT
                ).show()
            }
        }
    )
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
    onCopyClick: () -> Unit = {},
    onEncrypt: () -> Unit = {},
    onDecrypt: () -> Unit = {}
) = Column(
    modifier = modifier.padding(MaterialTheme.spaces.spaceMedium),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spaces.spaceMedium)
) {
    val defaultHorizontalArrangement = Arrangement.spacedBy(MaterialTheme.spaces.spaceSmall)

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
    EncryptionToolbar(onEncrypt = onEncrypt, onDecrypt = onDecrypt)
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
private fun UserTextField(value: String, onValueChange: (String) -> Unit) = OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    label = { Text(text = stringResource(id = R.string.text)) },
    modifier = Modifier.fillMaxWidth()
)