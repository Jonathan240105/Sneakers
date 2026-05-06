package com.example.snkrsapp.Data.Repository.UsuarioRepository

import com.example.snkrsapp.Data.LocalData.UsuariosConectados.EntityToUsuario
import com.example.snkrsapp.Data.LocalData.UsuariosConectados.UsuarioEntity
import com.example.snkrsapp.Data.LocalData.UsuariosConectados.UsuarioToEntity
import com.example.snkrsapp.Data.LocalData.UsuariosConectados.UsuariosConectadosDao
import com.example.snkrsapp.Data.RemoteData.ActualizacionDao.ActualizacionDao
import com.example.snkrsapp.Data.RemoteData.ActualizacionDao.ActualizarPerfilSolicitud
import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.AutorizacionDao
import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.Usuario
import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.UsuarioSolicitud
import com.example.snkrsapp.Domain.EstadoLogin
import com.example.snkrsapp.Domain.EstadoRegistro
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

class UsuarioRepositoryImp @Inject constructor(
    private val autDao: AutorizacionDao,
    private val usuarioDao: UsuariosConectadosDao,
    private val actuDao: ActualizacionDao
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
                                            "Error en el servi: ${respuesta.message()}", false
                                        )
                                    )
                                }
                            } catch (e: Exception) {
                                trySend(EstadoLogin.Error("Error en la red: ${e.message}", false))
                            }
                        }
                    }
                }.addOnFailureListener { error ->
                    trySend(EstadoLogin.Error("Error de firebase: ${error.message}", true))
                }
            awaitClose {}
        }

    override suspend fun registrarUsuario(
        email: String, contra: String, nombre: String, apellidos: String?, fecha: String
    ): Flow<EstadoRegistro> = flow {
        emit(EstadoRegistro.Cargando)

        try {
            val resultadoAuth =
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, contra).await()

            val uid = resultadoAuth.user!!.uid
            val usuario = UsuarioSolicitud(
                uid, nombre, email, apellidos ?: "", fecha
            )

            val respuesta = autDao.registrarUsuario(usuario)
            if (respuesta.isSuccessful) {
                println("Todo bien ${respuesta.body()?.exito}")
                emit(EstadoRegistro.Exito("Usuario añadido a la base de datos"))
            } else {
                emit(
                    EstadoRegistro.Error(
                        "Error al añadir en la base de datos: ${respuesta.code()}", false
                    )
                )
            }
        } catch (e: Exception) {
            val estadoError = when (e) {
                is FirebaseAuthException -> {
                    println("Error de Auth: ${e.errorCode}")
                    EstadoRegistro.Error(
                        "Error en Firebase: ${e.message}", errorFirebase = true
                    )
                }

                is IOException -> {
                    EstadoRegistro.Error("no hay internet", errorFirebase = false)
                }

                else -> {
                    EstadoRegistro.Error("Error inesperado: ${e.message}", errorFirebase = false)
                }
            }
            emit(estadoError)
        }
    }

    override suspend fun traerPerfil(token: String): Flow<Usuario> = flow {

        val usuarioLocal = usuarioDao.obtenerUsuarioPorUID(token)
        if (usuarioLocal != null) {
            emit(EntityToUsuario(usuarioLocal))
            return@flow
        }

        try {
            val response = autDao.getPerfil("Bearer $token")
            if (response.isSuccessful) {
                val usuarioFresco = response.body()
                if (usuarioFresco != null) {

                    usuarioDao.añadirUsuario(UsuarioToEntity(usuarioFresco))
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
                email,nombre, apellidos
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
}