package com.nevidimka655.tink_lab.data

import com.nevidimka655.crypto.tink.KeysetTemplates

internal val AeadTypesText = KeysetTemplates.AEAD.entries.map { it.name.lowercase() }

internal val AeadTypesFiles = KeysetTemplates.Stream.entries
    .filter { it.name.endsWith("MB") }
    .map { it.name.removeSuffix("_1MB").lowercase() }