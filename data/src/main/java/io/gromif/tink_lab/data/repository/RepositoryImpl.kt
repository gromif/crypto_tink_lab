package io.gromif.tink_lab.data.repository

import io.gromif.crypto.tink.model.KeysetTemplates
import io.gromif.tink_lab.domain.model.DataType
import io.gromif.tink_lab.domain.model.Key
import io.gromif.tink_lab.domain.model.Repository
import io.gromif.tink_lab.domain.util.KeyGenerator
import io.gromif.tink_lab.domain.util.KeyReader
import io.gromif.tink_lab.domain.util.KeyWriter

private val keysetAssociatedData = "labKey".toByteArray()

class RepositoryImpl(
    private val keyGenerator: KeyGenerator,
    private val keyWriter: KeyWriter,
    private val keyReader: KeyReader
) : Repository {
    override fun createKey(dataType: DataType, aeadType: String): Key {
        return keyGenerator(dataType = dataType, aeadType = aeadType)
    }

    override fun save(key: Key, uriString: String, keysetPassword: String) {
        keyWriter(
            uriString = uriString,
            key = key,
            keysetPassword = keysetPassword,
            keysetAssociatedData = keysetAssociatedData
        )
    }

    override fun load(uriString: String, keysetPassword: String): KeyReader.Result {
        return keyReader(
            uriString = uriString,
            keysetPassword = keysetPassword,
            keysetAssociatedData = keysetAssociatedData
        )
    }

    override fun getFileAeadList(): List<String> {
        return KeysetTemplates.Stream.entries
            .filter { it.name.endsWith("MB") }
            .map { it.name.removeSuffix("_1MB").lowercase() }
    }

    override fun getTextAeadList(): List<String> {
        return KeysetTemplates.AEAD.entries.map { it.name.lowercase() }
    }
}