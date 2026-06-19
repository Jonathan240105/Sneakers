package com.example.snkrsapp.Data.Repository.ChatRepository

import android.content.Context
import com.example.snkrsapp.Data.Crypto.CifradoChat
import com.example.snkrsapp.Data.Crypto.MensajeCifrado
import com.example.snkrsapp.Domain.Chat
import com.example.snkrsapp.Domain.Mensaje
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.UUID
import javax.crypto.SecretKey
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

data class DispositivoChat(
    val idDispositivo: String,
    val publicKey: String
)

class ChatRepositoryImp @Inject constructor(
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) : ChatRepository {

    override suspend fun prepararUsuarioChat(
        uid: String,
        nombreUsuario: String,
        fotoUsuario: String
    ) {
        val deviceId = obtenerDeviceId()
        CifradoChat.crearParClavesSiNoExiste(uid, deviceId)
        val publicKey = CifradoChat.obtenerClavePublicaBase64(uid, deviceId)
        val datosUsuario = mutableMapOf<String, Any>(
            "uid" to uid
        )

        if (nombreUsuario.isNotBlank()) {
            datosUsuario["nombreUsuario"] = nombreUsuario
        }

        if (fotoUsuario.isNotBlank()) {
            datosUsuario["fotoUsuario"] = fotoUsuario
        }

        firestore.collection(COLECCION_USUARIOS_CHAT)
            .document(uid)
            .set(datosUsuario, SetOptions.merge())
            .await()

        firestore.collection(COLECCION_USUARIOS_CHAT)
            .document(uid)
            .collection(COLECCION_DISPOSITIVOS)
            .document(deviceId)
            .set(
                mapOf(
                    "deviceId" to deviceId,
                    "publicKey" to publicKey,
                    "fechaRegistro" to FieldValue.serverTimestamp()
                ),
                SetOptions.merge()
            )
            .await()
    }

    override suspend fun crearORecuperarChat(
        uidActual: String,
        uidOtroUsuario: String,
        idProducto: Int?,
        nombreUsuarioActual: String,
        fotoUsuarioActual: String,
        nombreOtroUsuario: String,
        modeloProducto: String
    ): String {
        prepararUsuarioChat(uidActual, nombreUsuarioActual, fotoUsuarioActual)

        val idChat = crearIdChat(uidActual, uidOtroUsuario, idProducto)
        val chatRef = firestore.collection(COLECCION_CHATS).document(idChat)
        val chatExistente = chatRef.get().await()
        val nombresParticipantes = mapOf(
            uidActual to nombreUsuarioActual.ifBlank { obtenerNombreUsuarioChat(uidActual) },
            uidOtroUsuario to nombreOtroUsuario.ifBlank { "Usuario" }
        )
        val fotosParticipantes = mapOf(
            uidActual to fotoUsuarioActual.ifBlank { obtenerFotoUsuarioChat(uidActual) },
            uidOtroUsuario to ""
        )

        if (chatExistente.exists()) {
            chatRef.set(
                mapOf(
                    "nombresParticipantes" to nombresParticipantes,
                    "fotosParticipantes" to fotosParticipantes,
                    "modeloProducto" to modeloProducto
                ),
                SetOptions.merge()
            ).await()

            val deviceId = obtenerDeviceId()
            val claveChatCifrada = chatExistente.getClaveChatCifrada(uidActual, deviceId)
                ?: throw IllegalStateException("Este dispositivo todavia no tiene acceso a este chat. Abre el chat una vez desde un dispositivo ya autorizado.")
            val claveValida = try {
                CifradoChat.descifrarClaveChat(
                    uidActual,
                    deviceId,
                    claveChatCifrada
                ).encoded.isNotEmpty()
            } catch (e: Exception) {
                false
            }

            if (claveValida) {
                sincronizarDispositivosChat(chatRef.path, uidActual)
                return idChat
            }

            throw IllegalStateException("No se pudo descifrar la clave de este chat en este dispositivo")
        }

        val claveChat = CifradoChat.generarClaveChat()
        val clavesCifradas = cifrarClaveParaParticipantes(
            claveChat = claveChat,
            participantes = listOf(uidActual, uidOtroUsuario)
        )

        chatRef.set(
            mapOf(
                "idChat" to idChat,
                "participantes" to listOf(uidActual, uidOtroUsuario),
                "nombresParticipantes" to nombresParticipantes,
                "fotosParticipantes" to fotosParticipantes,
                "idProducto" to idProducto,
                "modeloProducto" to modeloProducto,
                "ultimoMensajeCifrado" to "",
                "fechaUltimoMensaje" to FieldValue.serverTimestamp(),
                "clavesChatCifradas" to clavesCifradas
            )
        ).await()

        return idChat
    }

    override fun observarChats(uidUsuario: String): Flow<List<Chat>> = callbackFlow {
        val listener = firestore.collection(COLECCION_CHATS)
            .whereArrayContains("participantes", uidUsuario)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val chats = snapshot?.documents?.map { documento ->
                    documento.toChat()
                }?.sortedByDescending { it.fechaUltimoMensaje } ?: emptyList()

                trySend(chats)
            }

        awaitClose { listener.remove() }
    }

    override suspend fun obtenerChat(idChat: String): Chat {
        return firestore.collection(COLECCION_CHATS)
            .document(idChat)
            .get()
            .await()
            .toChat()
    }

    override fun observarMensajes(idChat: String, uidActual: String): Flow<List<Mensaje>> =
        callbackFlow {
            val chatRef = firestore.collection(COLECCION_CHATS).document(idChat)
            var mensajesListener: ListenerRegistration? = null

            chatRef.get()
                .addOnSuccessListener { chat ->
                    try {
                        val deviceId = obtenerDeviceId()
                        val claveChatCifrada = chat.getClaveChatCifrada(uidActual, deviceId)

                        if (claveChatCifrada == null) {
                            close(IllegalStateException("No existe clave cifrada para este usuario"))
                            return@addOnSuccessListener
                        }

                        val claveChat = CifradoChat.descifrarClaveChat(uidActual, deviceId, claveChatCifrada)
                        mensajesListener = chatRef.collection(COLECCION_MENSAJES)
                            .orderBy("fecha", Query.Direction.ASCENDING)
                            .addSnapshotListener { snapshot, error ->
                                if (error != null) {
                                    close(error)
                                    return@addSnapshotListener
                                }

                                val mensajes = snapshot?.documents?.mapNotNull { documento ->
                                    try {
                                        documento.toMensajeDescifrado(idChat, claveChat)
                                    } catch (e: Exception) {
                                        null
                                    }
                                } ?: emptyList()

                                trySend(mensajes)
                            }
                    } catch (e: Exception) {
                        close(e)
                    }
                }
                .addOnFailureListener { error ->
                    close(error)
                }

            awaitClose { mensajesListener?.remove() }
        }

    override suspend fun enviarMensaje(idChat: String, uidEmisor: String, texto: String) {
        val textoLimpio = texto.trim()
        if (textoLimpio.isBlank()) return

        val chatRef = firestore.collection(COLECCION_CHATS).document(idChat)
        val chat = chatRef.get().await()
        val deviceId = obtenerDeviceId()
        val claveChatCifrada = chat.getClaveChatCifrada(uidEmisor, deviceId)
            ?: throw IllegalStateException("No existe clave cifrada para este dispositivo")

        val claveChat = CifradoChat.descifrarClaveChat(uidEmisor, deviceId, claveChatCifrada)
        sincronizarDispositivosChat(chatRef.path, uidEmisor, claveChat)
        val mensajeCifrado = CifradoChat.cifrarMensaje(textoLimpio, claveChat)

        chatRef.collection(COLECCION_MENSAJES)
            .add(
                mapOf(
                    "uidEmisor" to uidEmisor,
                    "textoCifrado" to mensajeCifrado.textoCifrado,
                    "iv" to mensajeCifrado.iv,
                    "fecha" to FieldValue.serverTimestamp(),
                    "leido" to false
                )
            )
            .await()

        chatRef.update(
            mapOf(
                "ultimoMensajeCifrado" to mensajeCifrado.textoCifrado,
                "fechaUltimoMensaje" to FieldValue.serverTimestamp()
            )
        ).await()
    }

    private suspend fun repararClavesChatExistente(
        chatPath: String,
        uidActual: String,
        uidOtroUsuario: String
    ) {
        val claveChat = CifradoChat.generarClaveChat()
        val clavesCifradas = cifrarClaveParaParticipantes(
            claveChat = claveChat,
            participantes = listOf(uidActual, uidOtroUsuario)
        )

        firestore.document(chatPath)
            .update(
                mapOf(
                    "clavesChatCifradas" to clavesCifradas,
                    "ultimoMensajeCifrado" to "",
                    "fechaUltimoMensaje" to FieldValue.serverTimestamp()
                )
            )
            .await()
    }

    private suspend fun obtenerDispositivos(uid: String): List<DispositivoChat> {
        val snapshot = firestore.collection(COLECCION_USUARIOS_CHAT)
            .document(uid)
            .collection(COLECCION_DISPOSITIVOS)
            .get()
            .await()

        return snapshot.documents.mapNotNull { documento ->
            val deviceId = documento.getString("deviceId") ?: documento.id
            val publicKey = documento.getString("publicKey")
            if (publicKey != null) DispositivoChat(deviceId, publicKey) else null
        }
    }

    private suspend fun cifrarClaveParaParticipantes(
        claveChat: SecretKey,
        participantes: List<String>
    ): Map<String, Map<String, String>> {
        return participantes.associateWith { uid ->
            obtenerDispositivos(uid).associate { dispositivo ->
                dispositivo.idDispositivo to CifradoChat.cifrarClaveChatParaUsuario(
                    claveChat,
                    dispositivo.publicKey
                )
            }
        }
    }

    private suspend fun sincronizarDispositivosChat(
        chatPath: String,
        uidActual: String,
        claveChatRecibida: SecretKey? = null
    ) {
        val chat = firestore.document(chatPath).get().await()
        val participantes = (chat.get("participantes") as? List<*>)
            ?.mapNotNull { it as? String }
            ?: return
        val deviceId = obtenerDeviceId()
        val claveChat = claveChatRecibida ?: chat.getClaveChatCifrada(uidActual, deviceId)
            ?.let { CifradoChat.descifrarClaveChat(uidActual, deviceId, it) }
            ?: return

        firestore.document(chatPath)
            .set(
                mapOf("clavesChatCifradas" to cifrarClaveParaParticipantes(claveChat, participantes)),
                SetOptions.merge()
            )
            .await()
    }

    private fun crearIdChat(uidA: String, uidB: String, idProducto: Int?): String {
        val participantes = listOf(uidA, uidB).sorted()
        return listOfNotNull(participantes[0], participantes[1], idProducto?.toString())
            .joinToString("_")
    }

    private suspend fun obtenerNombreUsuarioChat(uid: String): String {
        val documento = firestore.collection(COLECCION_USUARIOS_CHAT)
            .document(uid)
            .get()
            .await()

        return documento.getString("nombreUsuario")
            ?: FirebaseAuth.getInstance().currentUser?.displayName
            ?: FirebaseAuth.getInstance().currentUser?.email
            ?: uid
    }

    private suspend fun obtenerFotoUsuarioChat(uid: String): String {
        val documento = firestore.collection(COLECCION_USUARIOS_CHAT)
            .document(uid)
            .get()
            .await()

        return documento.getString("fotoUsuario")
            ?: FirebaseAuth.getInstance().currentUser?.photoUrl?.toString()
            ?: ""
    }

    private fun DocumentSnapshot.toChat(): Chat {
        val claves = get("clavesChatCifradas") as? Map<*, *>
        val nombres = get("nombresParticipantes") as? Map<*, *>
        val fotos = get("fotosParticipantes") as? Map<*, *>

        return Chat(
            idChat = getString("idChat") ?: id,
            participantes = (get("participantes") as? List<*>)
                ?.mapNotNull { it as? String }
                ?: emptyList(),
            idProducto = getLong("idProducto")?.toInt(),
            modeloProducto = getString("modeloProducto") ?: "",
            ultimoMensajeCifrado = getString("ultimoMensajeCifrado") ?: "",
            fechaUltimoMensaje = getTimestamp("fechaUltimoMensaje")?.toDate()?.time ?: 0L,
            nombresParticipantes = nombres?.mapNotNull { (uid, nombre) ->
                val uidString = uid as? String
                val nombreString = nombre as? String
                if (uidString != null && nombreString != null) uidString to nombreString else null
            }?.toMap() ?: emptyMap(),
            fotosParticipantes = fotos?.mapNotNull { (uid, foto) ->
                val uidString = uid as? String
                val fotoString = foto as? String
                if (uidString != null && fotoString != null) uidString to fotoString else null
            }?.toMap() ?: emptyMap(),
            clavesChatCifradas = claves?.mapNotNull { (uid, clavesPorDispositivo) ->
                val uidString = uid as? String
                val clavesMap = clavesPorDispositivo as? Map<*, *>
                if (uidString != null && clavesMap != null) {
                    uidString to clavesMap.mapNotNull { (deviceId, clave) ->
                        val deviceIdString = deviceId as? String
                        val claveString = clave as? String
                        if (deviceIdString != null && claveString != null) {
                            deviceIdString to claveString
                        } else {
                            null
                        }
                    }.toMap()
                } else {
                    null
                }
            }?.toMap() ?: emptyMap()
        )
    }

    private fun DocumentSnapshot.toMensajeDescifrado(
        idChat: String,
        claveChat: SecretKey
    ): Mensaje? {
        val textoCifrado = getString("textoCifrado") ?: return null
        val iv = getString("iv") ?: return null
        val texto = CifradoChat.descifrarMensaje(MensajeCifrado(textoCifrado, iv), claveChat)

        return Mensaje(
            idMensaje = id,
            idChat = idChat,
            uidEmisor = getString("uidEmisor") ?: "",
            texto = texto,
            fecha = getTimestamp("fecha")?.toDate()?.time ?: 0L,
            leido = getBoolean("leido") ?: false
        )
    }

    private fun DocumentSnapshot.getClaveChatCifrada(uid: String, deviceId: String): String? {
        val claves = get("clavesChatCifradas") as? Map<*, *>
        val clavesUsuario = claves?.get(uid)
        return when (clavesUsuario) {
            is String -> clavesUsuario
            is Map<*, *> -> clavesUsuario[deviceId] as? String
            else -> null
        }
    }

    private fun obtenerDeviceId(): String {
        val prefs = context.getSharedPreferences("chat_device", Context.MODE_PRIVATE)
        val existente = prefs.getString("device_id", null)
        if (existente != null) return existente

        val nuevo = UUID.randomUUID().toString()
        prefs.edit().putString("device_id", nuevo).apply()
        return nuevo
    }

    private suspend fun <T> Task<T>.await(): T =
        suspendCancellableCoroutine { continuation ->
            addOnSuccessListener { result ->
                if (continuation.isActive) continuation.resume(result)
            }
            addOnFailureListener { error ->
                if (continuation.isActive) continuation.resumeWithException(error)
            }
            addOnCanceledListener {
                continuation.cancel()
            }
        }

    private companion object {
        const val COLECCION_USUARIOS_CHAT = "usuarios_chat"
        const val COLECCION_DISPOSITIVOS = "dispositivos"
        const val COLECCION_CHATS = "chats"
        const val COLECCION_MENSAJES = "mensajes"
    }
}
