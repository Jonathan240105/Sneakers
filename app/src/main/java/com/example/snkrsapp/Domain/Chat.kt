package com.example.snkrsapp.Domain

data class Chat(
    val idChat: String = "",
    val participantes: List<String> = emptyList(),
    val idProducto: Int? = null,
    val modeloProducto: String = "",
    val ultimoMensajeCifrado: String = "",
    val fechaUltimoMensaje: Long = 0L,
    val nombresParticipantes: Map<String, String> = emptyMap(),
    val fotosParticipantes: Map<String, String> = emptyMap(),
    val clavesChatCifradas: Map<String, Map<String, String>> = emptyMap()
)
