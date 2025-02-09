package com.nevidimka655.tink_lab.key

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.nevidimka655.tink_lab.domain.model.DataType
import com.nevidimka655.tink_lab.domain.usecase.CreateLabKeyUseCase
import com.nevidimka655.tink_lab.domain.usecase.GetFileAeadListUseCase
import com.nevidimka655.tink_lab.domain.usecase.GetTextAeadListUseCase
import com.nevidimka655.tink_lab.domain.usecase.LoadKeyUseCase
import com.nevidimka655.tink_lab.domain.usecase.SaveKeyUseCase
import com.nevidimka655.tink_lab.domain.util.KeyReader
import dagger.hilt.android.lifecycle.HiltViewModel
import io.gromif.astracrypt.utils.Mapper
import io.gromif.astracrypt.utils.dispatchers.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val KEYSET_PASSWORD = "kp"
private const val KEYSET_URI_LOAD = "kp_uri_load"
private const val DATA_TYPE = "data_type"
private const val AEAD_TYPE = "aead_type"

@HiltViewModel
internal class KeyViewModel @Inject constructor(
    @IoDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    private val state: SavedStateHandle,
    private val createLabKeyUseCase: CreateLabKeyUseCase,
    private val saveKeyUseCase: SaveKeyUseCase,
    private val loadKeyUseCase: LoadKeyUseCase,
    private val uriToStringMapper: Mapper<Uri, String>,
    getFileAeadListUseCase: GetFileAeadListUseCase,
    getTextAeadListUseCase: GetTextAeadListUseCase
) : ViewModel() {
    val fileAeadList = getFileAeadListUseCase()
    val textAeadList = getTextAeadListUseCase()

    val dataTypeState = state.getStateFlow(DATA_TYPE, 0).map { DataType.entries[it] }
    val aeadTypeState = state.getStateFlow(AEAD_TYPE, fileAeadList[0])
    private val keysetHandleFlow = dataTypeState.combine(aeadTypeState) { dataType, aeadType ->
        createLabKeyUseCase(dataType = dataType, aeadType = aeadType.uppercase())
    }

    val keysetUriToLoadState = state.getStateFlow(KEYSET_URI_LOAD, "")
    val keysetPasswordState = state.getStateFlow(KEYSET_PASSWORD, "")

    suspend fun createKey() = keysetHandleFlow.first()

    suspend fun save(uri: Uri) = withContext(defaultDispatcher) {
        val keysetPassword = keysetPasswordState.value
        val uriString = uriToStringMapper(uri)
        createKey().also {
            saveKeyUseCase(key = it, uriString = uriString, keysetPassword = keysetPassword)
        }
    }

    suspend fun load() = withContext(defaultDispatcher) {
        val result = loadKeyUseCase(
            uriString = keysetUriToLoadState.value,
            keysetPassword = keysetPasswordState.value
        )
        if (result is KeyReader.Result.Success) result.key else null
    }

    fun setDataType(dataType: DataType) = state.set(DATA_TYPE, dataType.ordinal)
    fun setAeadType(type: String) = state.set(AEAD_TYPE, type)
    fun setKeysetPassword(keysetPassword: String) = state.set(KEYSET_PASSWORD, keysetPassword)
    fun setKeysetUriLoad(uri: Uri) = state.set(KEYSET_URI_LOAD, uriToStringMapper(uri))

}