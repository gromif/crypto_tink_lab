package io.gromif.tink_lab.domain.usecase

import io.gromif.tink_lab.domain.model.Repository

class GetTextAeadListUseCase(
    private val repository: Repository
) {

    operator fun invoke(): List<String> {
        return repository.getTextAeadList()
    }

}