package com.example.snkrsapp.Views.Pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.snkrsapp.Domain.Chat
import com.example.snkrsapp.Views.ViewModels.ChatViewModel
import com.example.snkrsapp.ui.theme.ColorNeutroFondo
import com.example.snkrsapp.ui.theme.ColorPrimario
import com.example.snkrsapp.ui.theme.ColorTextoSecundario
import com.example.snkrsapp.ui.theme.miTipografia
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PantallaConversaciones(
    chatViewModel: ChatViewModel,
    paddingValues: PaddingValues,
    navegarAChat: (String) -> Unit
) {
    val model by chatViewModel.model.collectAsState()
    val uidActual = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    LaunchedEffect(Unit) {
        chatViewModel.observarChatsUsuarioActual()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorNeutroFondo)
            .padding(paddingValues)
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Text(
            "Chats",
            color = ColorPrimario,
            fontFamily = miTipografia,
            fontWeight = Bold,
            fontSize = 28.sp
        )

        Spacer(Modifier.height(18.dp))

        if (model.chats.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Aun no tienes conversaciones",
                    color = ColorTextoSecundario,
                    fontFamily = miTipografia,
                    fontWeight = Bold,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(model.chats) { chat ->
                    ItemConversacion(
                        chat = chat,
                        uidActual = uidActual,
                        abrirChat = { navegarAChat(chat.idChat) }
                    )
                }
            }
        }
    }
}

@Composable
fun ItemConversacion(
    chat: Chat,
    uidActual: String,
    abrirChat: () -> Unit
) {
    val uidOtroUsuario = chat.participantes.firstOrNull { it != uidActual } ?: "Usuario"
    val nombreOtroUsuario = chat.nombresParticipantes[uidOtroUsuario]
        ?: uidOtroUsuario.take(12)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { abrirChat() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(ColorPrimario),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, "", tint = Color.White)
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            ) {
                Text(
                    nombreOtroUsuario,
                    color = ColorPrimario,
                    fontFamily = miTipografia,
                    fontWeight = Bold,
                    fontSize = 16.sp
                )
                if (chat.modeloProducto.isNotBlank()) {
                    Text(
                        "Chat para: ${chat.modeloProducto}",
                        color = ColorTextoSecundario,
                        fontFamily = miTipografia,
                        fontWeight = Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}
