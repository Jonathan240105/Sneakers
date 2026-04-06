package com.example.snkrsapp.Data.Repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ModuloRepository {

    @Binds
    @Singleton
    abstract fun bindRepository(
        repositoryImp: RepositoryImp
    ): Repository
}