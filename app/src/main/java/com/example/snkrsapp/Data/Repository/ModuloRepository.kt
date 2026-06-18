package com.example.snkrsapp.Data.Repository

import com.example.snkrsapp.Data.Repository.ChatRepository.ChatRepository
import com.example.snkrsapp.Data.Repository.ChatRepository.ChatRepositoryImp
import com.example.snkrsapp.Data.Repository.EventoRepository.EventoRepository
import com.example.snkrsapp.Data.Repository.EventoRepository.EventoRepositoryImp
import com.example.snkrsapp.Data.Repository.ProductoRepository.ProductoRepository
import com.example.snkrsapp.Data.Repository.ProductoRepository.ProductoRepositoryImp
import com.example.snkrsapp.Data.Repository.UsuarioRepository.UsuarioRepository
import com.example.snkrsapp.Data.Repository.UsuarioRepository.UsuarioRepositoryImp
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
        repositoryImp: UsuarioRepositoryImp
    ): UsuarioRepository

    @Binds
    @Singleton
    abstract fun bindProductoRepository(
        productoRepositoryImp: ProductoRepositoryImp
    ): ProductoRepository

    @Binds
    @Singleton
    abstract fun bindEventoRepository(
        eventoRepositoryImp: EventoRepositoryImp
    ): EventoRepository

    @Binds
    @Singleton
    abstract fun bindChatRepository(
        chatRepositoryImp: ChatRepositoryImp
    ): ChatRepository
}
