package com.nevidimka655.tink_lab.data.util

import com.nevidimka655.tink_lab.data.dto.KeyDto
import io.gromif.astracrypt.utils.Parser
import io.gromif.crypto.tink.core.encoders.HexEncoder
import kotlinx.serialization.json.Json

class KeyParser(
    private val hexEncoder: HexEncoder
): Parser<String, KeyDto> {
    override fun invoke(item: String): KeyDto {
        val json = hexEncoder.decode(item).decodeToString()
        return Json.decodeFromString(json)
    }
}