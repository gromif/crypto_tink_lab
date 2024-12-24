package com.nevidimka655.tink_lab

import androidx.lifecycle.ViewModel
import com.nevidimka655.astracrypt.core.di.IoDispatcher
import com.nevidimka655.tink_lab.domain.model.DataType
import com.nevidimka655.tink_lab.domain.model.Key
import com.nevidimka655.tink_lab.domain.usecase.CreateLabKeyUseCase
import com.nevidimka655.tink_lab.domain.usecase.GetFileAeadListUseCase
import com.nevidimka655.tink_lab.domain.usecase.GetTextAeadListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TinkLabKeyViewModel @Inject constructor(
    @IoDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    private val createLabKeyUseCase: CreateLabKeyUseCase,
    getFileAeadListUseCase: GetFileAeadListUseCase,
    getTextAeadListUseCase: GetTextAeadListUseCase
) : ViewModel() {
    private val key = MutableStateFlow(Key())
    val keyState = key.asStateFlow()

    val fileAeadList = getFileAeadListUseCase()
    val textAeadList = getTextAeadListUseCase()

    suspend fun shuffleKeyset(
        keysetPassword: String,
        dataType: DataType,
        aeadType: String
    ) = withContext(defaultDispatcher) {
        val newTinkLabKey = createLabKeyUseCase(
            keysetPassword = keysetPassword,
            dataType = dataType,
            aeadType = aeadType
        )
        key.update { newTinkLabKey }
    }

}