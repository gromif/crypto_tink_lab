package io.gromif.tink_lab.domain.usecase

import io.gromif.tink_lab.domain.model.Key
import io.gromif.tink_lab.domain.model.Repository

class SaveKeyUseCase(
    private val repository: Repository
) {

    operator fun invoke(
        key: Key,
        uriString: String,
        keysetPassword: String
    ) {
        repository.save(key = key, uriString = uriString, keysetPassword = keysetPassword)
    }

}