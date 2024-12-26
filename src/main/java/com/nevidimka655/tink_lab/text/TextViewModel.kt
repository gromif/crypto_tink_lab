package com.nevidimka655.tink_lab.text

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.crypto.tink.Aead
import com.nevidimka655.astracrypt.core.di.IoDispatcher
import com.nevidimka655.crypto.tink.core.encoders.Base64Util
import com.nevidimka655.crypto.tink.core.parsers.KeysetParser
import com.nevidimka655.crypto.tink.extensions.aeadPrimitive
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val ASSOCIATED_DATA = "adata"
private const val TEXT = "utext"

@HiltViewModel
class TextViewModel @Inject constructor(
    @IoDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    private val state: SavedStateHandle,
    private val keysetParser: KeysetParser,
    private val base64Util: Base64Util
) : ViewModel() {
    private var aead: Aead? = null
    val associatedDataState = state.getStateFlow(ASSOCIATED_DATA, "")
    val textState = state.getStateFlow(TEXT, "")

    suspend fun parseKeysetHandle(rawKeyset: String) = withContext(defaultDispatcher) {
        if (aead == null) aead = keysetParser(rawKeyset).aeadPrimitive()
    }

    fun encrypt() = viewModelScope.launch(defaultDispatcher) {
        aead?.let {
            val associatedData = associatedDataState.value.toByteArray()
            val bytes = textState.value.toByteArray()
            val encryptedBytes = it.encrypt(bytes, associatedData)
            setText(text = base64Util.encode(encryptedBytes))
        }
    }

    suspend fun decrypt() = withContext(defaultDispatcher) {
        try {
            aead?.let {
                val associatedData = associatedDataState.value.toByteArray()
                val text = textState.value
                val encryptedBytes = base64Util.decode(text)
                val bytes = it.decrypt(encryptedBytes, associatedData)
                setText(text = bytes.decodeToString())
            }
            true
        } catch (_: Exception) {
            false
        }
    }

    fun setAssociatedData(data: String) = state.set(ASSOCIATED_DATA, data)
    fun setText(text: String) = state.set(TEXT, text)

}