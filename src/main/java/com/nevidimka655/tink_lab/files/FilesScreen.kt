package com.nevidimka655.tink_lab.files

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Output
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.Source
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.nevidimka655.astracrypt.resources.R
import com.nevidimka655.tink_lab.TinkLab
import com.nevidimka655.tink_lab.shared.AssociatedDataTextField
import com.nevidimka655.tink_lab.shared.EncryptionToolbar
import com.nevidimka655.ui.compose_core.FilledTonalIconButton
import com.nevidimka655.ui.compose_core.ext.LocalWindowWidth
import com.nevidimka655.ui.compose_core.ext.isCompact
import com.nevidimka655.ui.compose_core.theme.spaces

@Composable
fun TinkLab.FilesScreen(modifier: Modifier = Modifier) {

    Screen()
}

@Preview(showBackground = true)
@Composable
private fun Screen(
    modifier: Modifier = Modifier,
    onEncrypt: () -> Unit = {},
    onDecrypt: () -> Unit = {},
    associatedData: String = "PREVIEW_AD",
    onAssociatedDataChange: (String) -> Unit = {},
    source: String = "SOURCE",
    onSourceClick: () -> Unit = {},
    destination: String = "Destination",
    onDestinationClick: () -> Unit = {},
    details: String = "Processing file",
    progress: Float = 0.5f
) = Column(
    modifier = modifier.padding(MaterialTheme.spaces.spaceMedium),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spaces.spaceMedium)
) {
    val localWindowWidth = LocalWindowWidth.current
    if (localWindowWidth.isCompact) {
        EncryptionToolbar(onEncrypt = onEncrypt, onDecrypt = onDecrypt)
        AssociatedDataTextField(
            modifier = Modifier.fillMaxWidth(),
            value = associatedData,
            onValueChange = onAssociatedDataChange
        )
        SourceTextField(value = source, onSourceClick = onSourceClick)
        DestinationTextField(value = destination, onDestinationClick = onDestinationClick)
        DetailsCard(details = details, progress = progress)
    } else Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spaces.spaceMedium)) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spaces.spaceMedium)
        ) {
            EncryptionToolbar(onEncrypt = onEncrypt, onDecrypt = onDecrypt)
            DetailsCard(details = details, progress = progress)
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spaces.spaceMedium)
        ) {
            AssociatedDataTextField(
                modifier = Modifier.fillMaxWidth(),
                value = associatedData,
                onValueChange = onAssociatedDataChange
            )
            SourceTextField(value = source, onSourceClick = onSourceClick)
            DestinationTextField(value = destination, onDestinationClick = onDestinationClick)
        }
    }
}

@Composable
private fun SourceTextField(
    value: String,
    onSourceClick: () -> Unit
) = OutlinedTextField(
    value = value,
    onValueChange = {},
    label = { Text(text = stringResource(id = R.string.source)) },
    leadingIcon = {
        Icon(imageVector = Icons.Outlined.Source, stringResource(R.string.add))
    },
    trailingIcon = {
        FilledTonalIconButton(icon = Icons.Outlined.FolderOpen, onClick = onSourceClick)
    },
    readOnly = true,
    singleLine = true,
    modifier = Modifier.fillMaxWidth()
)

@Composable
private fun DestinationTextField(
    value: String,
    onDestinationClick: () -> Unit
) = OutlinedTextField(
    value = value,
    onValueChange = {},
    label = { Text(text = stringResource(id = R.string.destination)) },
    leadingIcon = {
        Icon(imageVector = Icons.Default.Output, null)
    },
    trailingIcon = {
        FilledTonalIconButton(icon = Icons.Outlined.FolderOpen, onClick = onDestinationClick)
    },
    readOnly = true,
    singleLine = true,
    modifier = Modifier.fillMaxWidth()
)

@Composable
private fun DetailsCard(details: String, progress: Float) = OutlinedCard {
    Column(
        modifier = Modifier.fillMaxWidth().padding(MaterialTheme.spaces.spaceMedium),
        verticalArrangement = Arrangement.spacedBy(
            MaterialTheme.spaces.spaceMedium
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.files_options_details), fontWeight = FontWeight.SemiBold)
        Text(text = details)
        LinearProgressIndicator(progress = { progress })
    }
}