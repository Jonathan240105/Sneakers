package com.example.snkrsapp.Data.LocalData.UsuariosConectados

import androidx.room.Entity
import com.example.snkrsapp.Data.RemoteData.InicioSesion.UsuarioSolicitud

@Entity(tableName = "UsuariosConectados")
data class UsuarioEntity(
    val UID: String = "",
    val nombreUsuario: String = "",
    val email: String = "",
    val apellidos: String = "",
    val fechaNacimiento: String = ""
)

fun UsuarioEntity.toUsuario(usuario: UsuarioEntity): UsuarioSolicitud {
    return UsuarioSolicitud(
        UID = usuario.UID,
        nombreUsuario = usuario.nombreUsuario,
        email = usuario.email,
        apellidos = usuario.apellidos,
        fechaNacimiento = usuario.fechaNacimiento
    )
}

fun UsuarioSolicitud.toUsuarioEntity(usuario: UsuarioSolicitud): UsuarioEntity {
    return UsuarioEntity(
        UID = usuario.UID,
        nombreUsuario = usuario.nombreUsuario,
        email = usuario.email,
        apellidos = usuario.apellidos,
        fechaNacimiento = usuario.fechaNacimiento
    )
}