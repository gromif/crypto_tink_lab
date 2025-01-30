package com.nevidimka655.tink_lab.data.util

import com.nevidimka655.astracrypt.utils.Parser
import com.nevidimka655.tink_lab.data.dto.KeyDto
import io.gromif.crypto.tink.core.encoders.HexUtil
import kotlinx.serialization.json.Json

class KeyParser(
    private val hexUtil: HexUtil
): Parser<String, KeyDto> {
    override fun invoke(item: String): KeyDto {
        val json = hexUtil.decode(item).decodeToString()
        return Json.decodeFromString(json)
    }
}