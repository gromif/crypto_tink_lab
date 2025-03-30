package io.gromif.tink_lab.domain.model

import io.gromif.tink_lab.domain.util.KeyReader

interface Repository {

    fun createKey(dataType: DataType, aeadType: String) : Key

    fun save(key: Key, uriString: String, keysetPassword: String)

    fun load(uriString: String, keysetPassword: String): KeyReader.Result

    fun getFileAeadList() : List<String>

    fun getTextAeadList() : List<String>

    fun isKeysetLoaded(): Boolean

    suspend fun parseKeyset(rawKeyset: String)

    suspend fun encryptText(text: String, associatedData: String): EncryptionResult

    suspend fun decryptText(encryptedText: String, associatedData: String): EncryptionResult

}