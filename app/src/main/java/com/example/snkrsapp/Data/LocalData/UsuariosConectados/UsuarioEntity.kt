package com.example.snkrsapp.Data.LocalData.UsuariosConectados

import androidx.room.Entity
import com.example.snkrsapp.Domain.Usuario

@Entity(tableName = "UsuariosConectados")
data class UsuarioEntity(
    val UID: String = "",
    val nombreUsuario: String = "",
    val email: String = "",
    val apellidos: String = "",
    val fechaNacimiento: String = ""
)

fun UsuarioEntity.toUsuario(usuario: UsuarioEntity): Usuario {
    return Usuario(
        UID = usuario.UID,
        nombreUsuario = usuario.nombreUsuario,
        email = usuario.email,
        apellidos = usuario.apellidos,
        fechaNacimiento = usuario.fechaNacimiento
    )
}

fun Usuario.toUsuarioEntity(usuario: Usuario): UsuarioEntity {
    return UsuarioEntity(
        UID = usuario.UID,
        nombreUsuario = usuario.nombreUsuario,
        email = usuario.email,
        apellidos = usuario.apellidos,
        fechaNacimiento = usuario.fechaNacimiento
    )
}