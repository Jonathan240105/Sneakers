package com.example.snkrsapp.Data.Repository

import com.example.snkrsapp.Domain.EstadoLogin
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun iniciarSesion(email: String, contra: String): Flow<EstadoLogin>
}