package com.example.snkrsapp.Views.Pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.snkrsapp.Domain.Mensaje
import com.example.snkrsapp.Views.ViewModels.ChatViewModel
import com.example.snkrsapp.ui.theme.ColorAcento
import com.example.snkrsapp.ui.theme.ColorAlerta
import com.example.snkrsapp.ui.theme.ColorNeutroFondo
import com.example.snkrsapp.ui.theme.ColorPrimario
import com.example.snkrsapp.ui.theme.ColorTextFieldNoSeleccionado
import com.example.snkrsapp.ui.theme.ColorTextFieldSeleccionado
import com.example.snkrsapp.ui.theme.ColorTextoSecundario
import com.example.snkrsapp.ui.theme.miTipografia
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PantallaChat(
    idChat: String,
    chatViewModel: ChatViewModel,
    paddingValues: PaddingValues,
    volver: () -> Unit
) {
    val model by chatViewModel.model.collectAsState()
    val uidActual = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val uidOtroUsuario = model.chatActual.participantes.firstOrNull { it != uidActual } ?: ""
    val nombreOtroUsuario = model.chatActual.nombresParticipantes[uidOtroUsuario] ?: "Chat"
    val fotoOtroUsuario = model.chatActual.fotosParticipantes[uidOtroUsuario] ?: ""

    LaunchedEffect(idChat) {
        chatViewModel.observarMensajes(idChat)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorNeutroFondo)
            .padding(paddingValues)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = volver) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    "",
                    tint = ColorPrimario
                )
            }
            AvatarChat(fotoUrl = fotoOtroUsuario, size = 42)
            Spacer(Modifier.size(12.dp))
            Column {
                Text(
                    nombreOtroUsuario,
                    color = ColorPrimario,
                    fontFamily = miTipografia,
                    fontWeight = Bold,
                    fontSize = 21.sp
                )
                if (model.chatActual.modeloProducto.isNotBlank()) {
                    Text(
                        "Chat para: ${model.chatActual.modeloProducto}",
                        color = ColorTextoSecundario,
                        fontFamily = miTipografia,
                        fontWeight = Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        if (model.error.isNotBlank()) {
            Text(
                model.error,
                color = ColorAlerta,
                fontFamily = miTipografia,
                fontWeight = Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when {
                model.cargando -> CircularProgressIndicator(
                    color = ColorPrimario,
                    modifier = Modifier.align(Alignment.Center)
                )

                model.mensajes.isEmpty() -> Text(
                    "Aun no hay mensajes",
                    color = ColorTextoSecundario,
                    fontFamily = miTipografia,
                    modifier = Modifier.align(Alignment.Center)
                )

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(model.mensajes) { mensaje ->
                        BurbujaMensaje(
                            mensaje = mensaje,
                            esPropio = mensaje.uidEmisor == uidActual,
                            fotoOtroUsuario = fotoOtroUsuario
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = model.textoMensaje,
                onValueChange = {
                    if (it.length <= 500) {
                        chatViewModel.cambiarTextoMensaje(it)
                    }
                },
                modifier = Modifier.weight(1f),
                singleLine = false,
                maxLines = 4,
                placeholder = {
                    Text(
                        "Mensaje",
                        fontFamily = miTipografia
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = ColorTextFieldSeleccionado,
                    unfocusedContainerColor = ColorTextFieldNoSeleccionado,
                    focusedBorderColor = ColorPrimario,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color.White
                )
            )

            IconButton(
                onClick = { chatViewModel.enviarMensaje() },
                enabled = model.textoMensaje.isNotBlank(),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .background(
                        if (model.textoMensaje.isNotBlank()) ColorAcento
                        else ColorAcento.copy(alpha = 0.5f),
                        RoundedCornerShape(12.dp)
                    )
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, "", tint = Color.White)
            }
        }
    }
}

@Composable
fun BurbujaMensaje(
    mensaje: Mensaje,
    esPropio: Boolean,
    fotoOtroUsuario: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (esPropio) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!esPropio) {
            AvatarChat(fotoUrl = fotoOtroUsuario, size = 32)
            Spacer(Modifier.size(8.dp))
        }

        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(
                    if (esPropio) ColorPrimario else Color.White,
                    RoundedCornerShape(14.dp)
                )
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                mensaje.texto,
                color = if (esPropio) Color.White else ColorPrimario,
                fontFamily = miTipografia,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun AvatarChat(fotoUrl: String, size: Int) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(ColorPrimario),
        contentAlignment = Alignment.Center
    ) {
        if (fotoUrl.isNotBlank()) {
            AsyncImage(
                model = fotoUrl,
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                Icons.Default.Person,
                "",
                tint = Color.White,
                modifier = Modifier.size((size * 0.6).dp)
            )
        }
    }
}
