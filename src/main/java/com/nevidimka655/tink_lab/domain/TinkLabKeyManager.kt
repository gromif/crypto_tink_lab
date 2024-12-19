package com.nevidimka655.tink_lab.domain

import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.KeysetHandle
import com.nevidimka655.crypto.tink.domain.usecase.SerializeKeysetByKeyUseCase
import com.nevidimka655.crypto.tink.domain.usecase.encoder.HexUseCase
import com.nevidimka655.crypto.tink.domain.usecase.hash.Sha256UseCase
import com.nevidimka655.tink_lab.domain.model.DataType
import com.nevidimka655.tink_lab.domain.model.TinkLabKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TinkLabKeyManager(
    private val serializeKeysetByKeyUseCase: SerializeKeysetByKeyUseCase,
    private val sha256UseCase: Sha256UseCase,
    private val hexUseCase: HexUseCase
) {
    private val key = MutableStateFlow(TinkLabKey())
    val keyState = key.asStateFlow()

    fun generate(
        keysetPassword: String,
        dataType: DataType,
        aeadType: String
    ) {
        val template = KeyTemplates.get(
            if (dataType == DataType.Files) "${aeadType}_1MB" else aeadType
        )
        val keysetHandle = KeysetHandle.generateNew(template)
        val serializedEncryptedKeyset = serializeKeysetByKeyUseCase.serialize(
            keysetHandle = keysetHandle,
            key = keysetPassword.toByteArray(),
            associatedData = keysetAssociatedData
        )
        val keysetHashArray = sha256UseCase.compute(
            value = serializedEncryptedKeyset.toByteArray()
        )
        val keysetHash = hexUseCase.encode(bytes = keysetHashArray)
        val labKey = TinkLabKey(
            dataType = dataType,
            encryptedKeyset = serializedEncryptedKeyset,
            aeadType = aeadType,
            hash = keysetHash
        )
        key.update { labKey }
    }

}

private val keysetAssociatedData = "labKey".toByteArray()