package com.example.snkrsapp.Data.LocalData.UsuariosConectados

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.Usuario

@Entity(tableName = "UsuariosConectados")
data class UsuarioEntity(
    @PrimaryKey val UID: String = "",
    val nombreUsuario: String = "",
    val email: String = "",
    val apellidos: String = "",
    val fechaNacimiento: String = "",
    val esAdmin: Boolean = false
)

fun EntityToUsuario(usuario: UsuarioEntity): Usuario {
    return Usuario(
        UID = usuario.UID,
        nombreUsuario = usuario.nombreUsuario,
        email = usuario.email,
        apellidos = usuario.apellidos,
        fechaNacimiento = usuario.fechaNacimiento,
        esAdmin = if (usuario.esAdmin) 1 else 0
    )
}

fun UsuarioToEntity(usuario: Usuario): UsuarioEntity {
    return UsuarioEntity(
        UID = usuario.UID,
        nombreUsuario = usuario.nombreUsuario,
        email = usuario.email,
        apellidos = usuario.apellidos,
        fechaNacimiento = usuario.fechaNacimiento,
        esAdmin = usuario.esAdmin == 1
    )
}