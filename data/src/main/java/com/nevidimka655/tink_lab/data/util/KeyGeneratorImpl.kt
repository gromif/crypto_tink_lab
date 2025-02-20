package com.nevidimka655.tink_lab.data.util

import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.KeysetHandle
import com.nevidimka655.tink_lab.domain.model.DataType
import com.nevidimka655.tink_lab.domain.model.Key
import com.nevidimka655.tink_lab.domain.util.KeyGenerator
import io.gromif.crypto.tink.core.serializers.KeysetSerializer

class KeyGeneratorImpl(
    private val keysetSerializer: KeysetSerializer
): KeyGenerator {

    override fun invoke(dataType: DataType, aeadType: String): Key {
        val template = KeyTemplates.get(
            if (dataType == DataType.Files) "${aeadType}_1MB" else aeadType
        )
        val keysetHandle = KeysetHandle.generateNew(template)
        val serializedEncryptedKeyset = keysetSerializer(keysetHandle)
        return Key(
            dataType = dataType,
            rawKeyset = serializedEncryptedKeyset,
            aeadType = aeadType
        )
    }

}