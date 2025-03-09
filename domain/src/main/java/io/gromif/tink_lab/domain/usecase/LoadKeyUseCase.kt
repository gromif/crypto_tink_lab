package io.gromif.tink_lab.domain.usecase

import io.gromif.tink_lab.domain.model.Repository
import io.gromif.tink_lab.domain.util.KeyReader

class LoadKeyUseCase(
    private val repository: Repository
) {

    operator fun invoke(uriString: String, keysetPassword: String): KeyReader.Result {
        return repository.load(uriString = uriString, keysetPassword = keysetPassword)
    }

}