package com.nevidimka655.tink_lab.data.mapper

import com.nevidimka655.tink_lab.domain.model.DataType
import io.gromif.astracrypt.utils.Mapper

class DataTypeToIdMapper: Mapper<DataType, Int> {
    override fun invoke(item: DataType): Int = item.run {
        item.ordinal
    }
}