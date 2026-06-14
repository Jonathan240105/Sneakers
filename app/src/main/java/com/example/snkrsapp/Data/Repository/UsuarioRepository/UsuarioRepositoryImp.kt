package com.example.snkrsapp.Data.Repository.UsuarioRepository

import com.example.snkrsapp.Data.LocalData.Perfil.toCarritoEntity
import com.example.snkrsapp.Data.LocalData.Perfil.toColeccionEntity
import com.example.snkrsapp.Data.LocalData.Perfil.toDomain
import com.example.snkrsapp.Data.LocalData.Perfil.toVentasEntity
import com.example.snkrsapp.Data.LocalData.PublicacionPropia.PublicacionesPropiasLocalDao
import com.example.snkrsapp.Data.LocalData.UsuariosConectados.EntityToUsuario
import com.example.snkrsapp.Data.LocalData.UsuariosConectados.UsuarioEntity
import com.example.snkrsapp.Data.LocalData.UsuariosConectados.UsuarioToEntity
import com.example.snkrsapp.Data.LocalData.UsuariosConectados.UsuariosConectadosDao
import com.example.snkrsapp.Data.RemoteData.ActualizacionDao.ActualizacionDao
import com.example.snkrsapp.Data.RemoteData.ActualizacionDao.ActualizarPerfilSolicitud
import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.AutorizacionDao
import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.EliminarUsuariosSolicitud
import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.Usuario
import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.UsuarioSolicitud
import com.example.snkrsapp.Data.RemoteData.PublicacionDao.PublicacionDao
import com.example.snkrsapp.Domain.EstadoCompra
import com.example.snkrsapp.Domain.EstadoEliminarUsuarios
import com.example.snkrsapp.Domain.EstadoLogin
import com.example.snkrsapp.Domain.EstadoRegistro
import com.example.snkrsapp.Domain.ProductoColeccionItem
import com.example.snkrsapp.Domain.PublicacionPerfilItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

class UsuarioRepositoryImp @Inject constructor(
    private val autDao: AutorizacionDao,
    private val usuarioDao: UsuariosConectadosDao,
    private val actuDao: ActualizacionDao,
    private val publicacionLocalDao: PublicacionesPropiasLocalDao,
    private val publicacionDao: PublicacionDao
) : UsuarioRepository {
    override suspend fun iniciarSesion(email: String, contra: String): Flow<EstadoLogin> =
        callbackFlow {
            //Primero se envía el estado de cargando
            trySend(EstadoLogin.Cargando)

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, contra)
                .addOnSuccessListener { resultadoAuth ->

                    val usuario = resultadoAuth.user

                    usuario?.getIdToken(true)?.addOnSuccessListener { resultadoToken ->

                        val token = resultadoToken.token

                        CoroutineScope(Dispatchers.IO).launch {
                            try {

                                val respuesta = autDao.iniciarSesion("Bearer $token")

                                if (respuesta.isSuccessful && respuesta.body() != null) {
                                    trySend(
                                        EstadoLogin.Exito(
                                            respuesta.body()?.usuario ?: Usuario()
                                        )
                                    )
                                } else {
                                    trySend(
                                        EstadoLogin.Error(
                                            "Error en el servidor. Inténtalo de nuevo más tarde", false
                                        )
                                    )
                                }
                            } catch (e: Exception) {
                                trySend(EstadoLogin.Error("No tienes conexión a internet", false))
                            }
                        }
                    }
                }.addOnFailureListener { error ->
                    val mensaje = if (error is FirebaseAuthException) {
                        when (error.errorCode) {
                            "ERROR_INVALID_CREDENTIALS",
                            "ERROR_WRONG_PASSWORD",
                            "ERROR_USER_NOT_FOUND" -> "Credenciales incorrectas"
                            "ERROR_INVALID_EMAIL" -> "El formato del correo electrónico no es válido."
                            "ERROR_USER_DISABLED" -> "Esta cuenta ha sido deshabilitada."
                            "ERROR_TOO_MANY_REQUESTS" -> "Demasiados intentos. Inténtalo más tarde."
                            else -> "Error al iniciar sesión. Inténtalo de nuevo."
                        }
                    } else {
                        "Ha ocurrido un error inesperado."
                    }

                    trySend(EstadoLogin.Error(mensaje, true))
                }
            awaitClose {}
        }

    override suspend fun registrarUsuario(
        email: String, contra: String, nombre: String, apellidos: String?, fecha: String,urlFoto : String
    ): Flow<EstadoRegistro> = flow {
        emit(EstadoRegistro.Cargando)

        var firebaseUser: com.google.firebase.auth.FirebaseUser? = null

        try {
            val resultadoAuth = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, contra).await()
            firebaseUser = resultadoAuth.user

            val uid = firebaseUser!!.uid
            val usuario = UsuarioSolicitud(
                uid, nombre, email, apellidos ?: "", fecha, urlFoto
            )

            val respuesta = autDao.registrarUsuario(usuario)

            if (respuesta.isSuccessful) {
                println("Todo bien ${respuesta.body()?.exito}")
                emit(EstadoRegistro.Exito("Usuario creado correctamente"))
            } else {
                println("Error en la base de datos")
                firebaseUser?.delete()?.await()

                emit(
                    EstadoRegistro.Error(
                        "Error, inténtelo de nuevo más tarde", errorFirebase = false
                    )
                )
            }
        } catch (e: Exception) {
            if (e !is FirebaseAuthException && firebaseUser != null) {
                try { firebaseUser.delete().await() } catch (_: Exception) {}
            }

            val estadoError = when (e) {
                is FirebaseAuthException -> {
                    val mensajeAmigable = when (e.errorCode) {
                        "ERROR_EMAIL_ALREADY_IN_USE" -> "Este correo electrónico ya está registrado."
                        "ERROR_INVALID_EMAIL" -> "El formato del correo electrónico no es válido."
                        "ERROR_WEAK_PASSWORD" -> "La contraseña debe tener al menos 8 caracteres."
                        else -> "Error de autenticación: Revisa tus datos."
                    }
                    EstadoRegistro.Error(mensajeAmigable, errorFirebase = true)
                }
                is IOException -> {
                    EstadoRegistro.Error("No tienes conexión a Internet o el servidor está caído.", false)
                }
                else -> {
                    EstadoRegistro.Error("Ha ocurrido un error inesperado durante el registro.", false)
                }
            }
            emit(estadoError)
        }
    }

    override suspend fun traerPerfil(
        token: String,
        uidSoli: String?,
        miUid: String
    ): Flow<Usuario> = flow {

        if (uidSoli == null || uidSoli == miUid) {
            val usuarioLocal = usuarioDao.obtenerUsuarioPorUID(miUid)
            if (usuarioLocal != null) {
                emit(EntityToUsuario(usuarioLocal))
            }
        }

        try {
            val response = autDao.getPerfil("Bearer $token", uidSoli)
            if (response.isSuccessful) {
                val usuarioFresco = response.body()
                if (usuarioFresco != null) {
                    if (uidSoli == null || uidSoli == miUid) {
                        usuarioDao.añadirUsuario(UsuarioToEntity(usuarioFresco))
                    }
                    emit(usuarioFresco)
                }
            }
        } catch (e: Exception) {
            println("Error al obtener el perfil: ${e.message}")
        }
    }

    override suspend fun actualizarPerfil(
        token: String,
        nombre: String?,
        email: String?,
        apellidos: String?,
        contra: String?,
        uid: String
    ): Flow<Usuario?> = flow {

        val usuarioLocal = EntityToUsuario(usuarioDao.obtenerUsuarioPorUID(uid) ?: UsuarioEntity())

        try {
            if (!contra.isNullOrEmpty()) {
                FirebaseAuth.getInstance().currentUser?.updatePassword(contra)?.await()
            }
            if (!email.isNullOrEmpty()) {
                FirebaseAuth.getInstance().currentUser?.verifyBeforeUpdateEmail(email)?.await()
            }

            val solicitud = ActualizarPerfilSolicitud(
                email, nombre, apellidos
            )

            val respuesta = actuDao.actualizarPerfil("Bearer $token", solicitud)

            if (respuesta.isSuccessful && respuesta.body()?.ok == true) {

                val usuarioActualizado = usuarioLocal.copy(
                    nombreUsuario = nombre ?: usuarioLocal.nombreUsuario,
                    apellidos = apellidos ?: usuarioLocal.apellidos
                )
                usuarioDao.añadirUsuario(UsuarioToEntity(usuarioActualizado))
                emit(usuarioActualizado)
            }
        } catch (e: Exception) {
            println("Error al actualizar perfil: ${e.message}")
            emit(usuarioLocal)
        }
    }

    override suspend fun traerCarrito(
        token: String,
        uidUsuario: String
    ): Flow<List<PublicacionPerfilItem>> = flow {
        try {
            val carritoLocal = publicacionLocalDao.obtenerCarritoLocal(uidUsuario).first()
            if (carritoLocal.isNotEmpty()) {
                emit(carritoLocal.map { it.toDomain() })
            }
        } catch (e: Exception) {
            println("Error al traer carrito local: ${e.message}")
        }

        try {
            val respuesta = publicacionDao.obtenerCarritoUsuario("Bearer $token")
            if (respuesta.isSuccessful && respuesta.body() != null) {
                val listaRemota = respuesta.body()!!

                publicacionLocalDao.vaciarCarritoLocal(uidUsuario)
                val entidadesALocal = listaRemota.map { it.toCarritoEntity(uidUsuario) }
                publicacionLocalDao.insertarCarritoLocal(entidadesALocal)
                emit(entidadesALocal.map { it.toDomain() })
            }
        } catch (e: Exception) {
            println("Error al traer carrito remoto: ${e.message}")
        }
    }

    override suspend fun traerColeccionUsuario(
        token: String,
        uidObjetivo: String
    ): Flow<List<ProductoColeccionItem>> = flow {

        try {
            val coleccionLocal = publicacionLocalDao.obtenerColeccionLocal(uidObjetivo).first()
            if (coleccionLocal.isNotEmpty()) {
                emit(coleccionLocal.map { it.toDomain() })
            }
        } catch (e: Exception) {
            println("Error en Local: ${e.message}")
        }


        try {
            val respuesta = publicacionDao.obtenerColeccionUsuario("Bearer $token", uidObjetivo)

            if (respuesta.isSuccessful && respuesta.body() != null) {
                val listaRemota = respuesta.body()!!
                publicacionLocalDao.vaciarColeccionLocal(uidObjetivo)
                val entidadesALocal = listaRemota.map { it.toColeccionEntity(uidObjetivo) }
                publicacionLocalDao.insertarColeccionLocal(entidadesALocal)
                emit(entidadesALocal.map { it.toDomain() })
            }
        } catch (e: Exception) {
            println(
                "Error al traer colección: ${e.message}"
            )
        }
    }

    override suspend fun traerVentasUsuario(
        token: String,
        uidObjetivo: String
    ): Flow<List<PublicacionPerfilItem>> = flow {

        try {
            val ventasLocales = publicacionLocalDao.obtenerVentasLocal(uidObjetivo).first()
            if (ventasLocales.isNotEmpty()) {
                emit(ventasLocales.map { it.toDomain() })
            }
        } catch (e: Exception) {
            println(
                "Error al traer ventas: ${e.message}"
            )
        }
        try {
            val respuesta = publicacionDao.obtenerVentasUsuario("Bearer $token", uidObjetivo)

            if (respuesta.isSuccessful && respuesta.body() != null) {
                val listaRemota = respuesta.body()!!

                publicacionLocalDao.vaciarVentasLocal(uidObjetivo)
                val entidadesALocal = listaRemota.map { it.toVentasEntity(uidObjetivo) }
                publicacionLocalDao.insertarVentasLocal(entidadesALocal)
                emit(entidadesALocal.map { it.toDomain() })
            }
        } catch (e: Exception) {
            println(
                "Error al traer ventas: ${e.message}"
            )
        }
    }

    override suspend fun procesarPagoCarrito(token: String): Flow<EstadoCompra> = flow {
        emit(EstadoCompra.Cargando)

        try {
            val respuesta = publicacionDao.comprarCarrito("Bearer $token")
            if (respuesta.isSuccessful && respuesta.body()?.ok == true) {
                emit(EstadoCompra.Exito("Compra realizada con éxito"))
            } else {
                val mensaje =
                    respuesta.errorBody()?.string() ?: "Error inesperado en el servidor"
                emit(EstadoCompra.Error(mensaje))
            }
        } catch (e: Exception) {
            print("Error : ${e.message}")
            emit(EstadoCompra.Error("Error en la red"))
        }
    }

    override suspend fun getUsuarios(token: String): Flow<List<Usuario>> = flow {
        try {
            val respuesta = autDao.getUsuarios("Bearer $token")
            if (respuesta.isSuccessful) {
                emit(respuesta.body()!!)
            } else {
                emit(emptyList())
            }
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun eliminarUsuarios(
        token: String,
        uids: List<String>
    ): Flow<EstadoEliminarUsuarios> = flow {

        emit(EstadoEliminarUsuarios.Cargando)

        try {
            val body = EliminarUsuariosSolicitud(uids)
            val respuesta = autDao.eliminarUsuarios("Bearer $token", body)

            if (respuesta.isSuccessful) {
                emit(
                    EstadoEliminarUsuarios.Exito(
                        respuesta.body()?.mensaje ?: "Usuarios eliminados"
                    )
                )
            } else {
                emit(EstadoEliminarUsuarios.Error(respuesta.body()?.mensaje ?: respuesta.message()))
            }
        } catch (e: Exception) {
            emit(EstadoEliminarUsuarios.Error("Error en la red ${e.message}"))
        }

    }
}