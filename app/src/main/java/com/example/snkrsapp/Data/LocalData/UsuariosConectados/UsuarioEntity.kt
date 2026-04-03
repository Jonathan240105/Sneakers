package com.example.snkrsapp.Data.LocalData.UsuariosConectados

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.snkrsapp.Data.RemoteData.InicioSesion.Usuario
import com.example.snkrsapp.Data.RemoteData.InicioSesion.UsuarioSolicitud

@Entity(tableName = "UsuariosConectados")
data class UsuarioEntity(
    @PrimaryKey val UID: String = "",
    val nombreUsuario: String = "",
    val email: String = "",
    val apellidos: String = "",
    val fechaNacimiento: String = ""
)

fun EntityToUsuario(usuario: UsuarioEntity): Usuario {
    return Usuario(
        UID = usuario.UID,
        nombreUsuario = usuario.nombreUsuario,
        email = usuario.email,
        apellidos = usuario.apellidos,
        fechaNacimiento = usuario.fechaNacimiento
    )
}

fun UsuarioToEntity(usuario: Usuario): UsuarioEntity {
    return UsuarioEntity(
        UID = usuario.UID,
        nombreUsuario = usuario.nombreUsuario,
        email = usuario.email,
        apellidos = usuario.apellidos,
        fechaNacimiento = usuario.fechaNacimiento
    )
}