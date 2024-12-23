package com.nevidimka655.tink_lab

import androidx.lifecycle.ViewModel
import com.nevidimka655.tink_lab.domain.model.DataType
import com.nevidimka655.tink_lab.domain.model.TinkLabKey
import com.nevidimka655.tink_lab.domain.usecase.CreateLabKeyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TinkLabKeyViewModel @Inject constructor(
    private val createLabKeyUseCase: CreateLabKeyUseCase
) : ViewModel() {
    //@IoDispatcher TODO(Core)
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO

    private val key = MutableStateFlow(TinkLabKey())
    val keyState = key.asStateFlow()

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