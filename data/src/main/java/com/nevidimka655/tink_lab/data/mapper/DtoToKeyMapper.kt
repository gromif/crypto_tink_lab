package com.nevidimka655.tink_lab.data.mapper

import com.nevidimka655.tink_lab.data.dto.KeyDto
import com.nevidimka655.tink_lab.domain.model.DataType
import com.nevidimka655.tink_lab.domain.model.Key
import io.gromif.astracrypt.utils.Mapper

class DtoToKeyMapper(
    private val idToDataTypeMapper: Mapper<Int, DataType>
): Mapper<KeyDto, Key> {
    override fun invoke(item: KeyDto): Key = item.run {
        Key(
            dataType = idToDataTypeMapper(dataTypeId),
            aeadType = aeadType,
            rawKeyset = encryptedKeyset
        )
    }
}