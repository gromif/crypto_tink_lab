package io.gromif.tink_lab.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.gromif.tink_lab.data.repository.RepositoryImpl
import io.gromif.tink_lab.domain.model.Repository
import io.gromif.tink_lab.domain.util.KeyGenerator
import io.gromif.tink_lab.domain.util.KeyReader
import io.gromif.tink_lab.domain.util.KeyWriter

@Module
@InstallIn(ViewModelComponent::class)
internal object RepositoryModule {

    @Provides
    fun provideRepository(
        keyGenerator: KeyGenerator,
        keyWriter: KeyWriter,
        keyReader: KeyReader
    ): Repository = RepositoryImpl(
        keyGenerator = keyGenerator,
        keyWriter = keyWriter,
        keyReader = keyReader
    )

}