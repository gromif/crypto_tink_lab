package com.nevidimka655.tink_lab.domain

import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.KeysetHandle
import com.nevidimka655.crypto.tink.core.encoders.HexService
import com.nevidimka655.crypto.tink.core.hash.Sha256Service
import com.nevidimka655.crypto.tink.data.serializers.SerializeKeysetByKeyService
import com.nevidimka655.tink_lab.domain.model.DataType
import com.nevidimka655.tink_lab.domain.model.TinkLabKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TinkLabKeyManager(
    private val serializeKeysetByKeyService: SerializeKeysetByKeyService,
    private val sha256Service: Sha256Service,
    private val hexService: HexService
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
        val serializedEncryptedKeyset = serializeKeysetByKeyService.serialize(
            keysetHandle = keysetHandle,
            key = keysetPassword.toByteArray(),
            associatedData = keysetAssociatedData
        )
        val keysetHashArray = sha256Service.compute(
            value = serializedEncryptedKeyset.toByteArray()
        )
        val keysetHash = hexService.encode(bytes = keysetHashArray)
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