package com.example.snkrsapp.Data.Repository

import com.example.snkrsapp.Data.RemoteData.InicioSesion.InicioSesionDao
import com.example.snkrsapp.Data.RemoteData.InicioSesion.Usuario
import com.example.snkrsapp.Domain.EstadoLogin
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RepositoryImp @Inject constructor(
    private val iniDao: InicioSesionDao
) : Repository {
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

                                val respuesta = iniDao.iniciarSesion("Bearer $token")

                                if (respuesta.isSuccessful && respuesta.body() != null) {
                                    trySend(
                                        EstadoLogin.Exito(
                                            respuesta.body()?.usuario ?: Usuario()
                                        )
                                    )
                                } else {
                                    trySend(
                                        EstadoLogin.Error(
                                            "Error en el servi: ${respuesta.message()}",
                                            false
                                        )
                                    )
                                }
                            } catch (e: Exception) {
                                trySend(EstadoLogin.Error("Error en la red: ${e.message}", false))
                            }
                        }
                    }
                }
                .addOnFailureListener { error ->
                    trySend(EstadoLogin.Error("Error de firebase: ${error.message}", true))
                }
            awaitClose {}
        }
}