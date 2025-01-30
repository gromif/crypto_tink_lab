package com.nevidimka655.tink_lab.data.util

import com.nevidimka655.astracrypt.utils.Serializer
import com.nevidimka655.tink_lab.data.dto.KeyDto
import io.gromif.crypto.tink.core.encoders.HexUtil
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class KeySerializer(
    private val hexUtil: HexUtil
): Serializer<KeyDto, String> {
    override fun invoke(item: KeyDto): String {
        val keyDtoJson = Json.encodeToString<KeyDto>(item)
        return hexUtil.encode(keyDtoJson.toByteArray())
    }
}