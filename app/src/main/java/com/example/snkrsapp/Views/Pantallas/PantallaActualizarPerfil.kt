package com.example.snkrsapp.Views.Pantallas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.snkrsapp.Views.ViewModels.ActualizarPerfilViewModel
import com.example.snkrsapp.Views.ViewModels.PerfilViewModel
import com.example.snkrsapp.ui.theme.ColorAcento
import com.example.snkrsapp.ui.theme.ColorBordeTextField
import com.example.snkrsapp.ui.theme.ColorNeutroFondo
import com.example.snkrsapp.ui.theme.ColorPrimario
import com.example.snkrsapp.ui.theme.ColorTextFieldNoSeleccionado
import com.example.snkrsapp.ui.theme.ColorTextFieldSeleccionado
import com.example.snkrsapp.ui.theme.ColorTextoSecundario
import com.example.snkrsapp.ui.theme.miTipografia
import kotlinx.datetime.LocalDate

@Composable
fun PantallaActualizarPerfil(
    volver: () -> Unit,
    onCerrarSesion: () -> Unit,
    perfilViewModel: PerfilViewModel,
    actualizarPerfilViewModel: ActualizarPerfilViewModel
) {

    LaunchedEffect(Unit) {
        perfilViewModel.cargarPerfil()
    }

    val model by perfilViewModel.model.collectAsState()
    val estadoScroll = rememberScrollState()
    var camposHabilitados by remember { mutableStateOf(false) }
    var nombreCambiado by remember { mutableStateOf("") }
    var emailCambiado by remember { mutableStateOf("") }
    var apellidosCambiado by remember { mutableStateOf("") }
    var fechaNacimientoCambiado by remember { mutableStateOf("") }
    var mostrarSelectorFecha by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorNeutroFondo)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp)
            .verticalScroll(estadoScroll)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = volver,
                modifier = Modifier
                    .background(Color(0xFF1E1E1E), CircleShape)
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    "",
                    tint = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1E1E1E)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model.usuarioActual.urlFoto,
                    "",
                    modifier = Modifier.size(60.dp)
                )
            }
            Spacer(Modifier.height(15.dp))
            Text(
                "@${model.usuarioActual.nombreUsuario}",
                fontFamily = miTipografia,
                color = ColorPrimario,
                modifier = Modifier.testTag("NombreUsuario"),
                fontSize = 24.sp,
                fontWeight = Bold
            )
            Spacer(Modifier.height(5.dp))
            Text(
                model.usuarioActual.email,
                fontFamily = miTipografia, color = Color.DarkGray, fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Información Personal",
                color = ColorTextoSecundario,
                fontFamily = miTipografia,
                fontSize = 18.sp,
                fontWeight = Bold
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (camposHabilitados) {
                    IconButton(
                        onClick = {
                            camposHabilitados = false
                            nombreCambiado = ""
                            emailCambiado = ""
                            apellidosCambiado = ""
                            fechaNacimientoCambiado = ""
                        },
                        modifier = Modifier
                            .size(38.dp)
                            .border(BorderStroke(1.dp, Color(0xFF444444)), CircleShape)
                            .background(Color.Transparent, CircleShape)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Cancelar",
                            tint = Color(0xFFE57373),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(Modifier.width(20.dp))

                    IconButton(
                        onClick = {
                            actualizarPerfilViewModel.actualizaPerfil(
                                emailCambiado,
                                nombreCambiado,
                                apellidosCambiado,
                                null
                            )
                            perfilViewModel.cargarPerfil()
                            camposHabilitados = false
                        },
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color.White, CircleShape)
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Guardar",
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                } else {
                    Button(
                        onClick = {
                            camposHabilitados = true
                            nombreCambiado = model.usuarioActual.nombreUsuario
                            apellidosCambiado = model.usuarioActual.apellidos
                            fechaNacimientoCambiado = model.usuarioActual.fechaNacimiento.take(10)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorAcento),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text("Editar",
                            fontFamily = miTipografia, fontSize = 15.sp, fontWeight = Bold, color = Color.White)
                    }
                }
            }
        }

        Card(
            Modifier
                .fillMaxWidth()
                .testTag("CardPerfil"),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(25.dp),
            border = BorderStroke(1.dp, ColorBordeTextField)
        ) {
            Column(Modifier.padding(20.dp)) {
                if (camposHabilitados) {
                    TextFieldCampo(
                        Icons.Default.Person, "Nombre de usuario", nombreCambiado
                    ) {
                        if (it.length <= 30) {
                            nombreCambiado = it
                        }
                    }
                } else {
                    ItemInfoPerfil(
                        Icons.Default.Person,
                        "Nombre de usuario",
                        "${model.usuarioActual.nombreUsuario} "
                    )
                }
                HorizontalDivider(
                    Modifier.padding(vertical = 12.dp), thickness = 1.dp, color = Color(0xFF252525)
                )

                if (camposHabilitados || model.usuarioActual.apellidos.isNotEmpty()) {
                    if (camposHabilitados) {
                        TextFieldCampo(
                            Icons.Default.Person, "Apellidos", apellidosCambiado
                        ) {
                            if (it.length <= 30) {
                                apellidosCambiado = it
                            }
                        }
                    } else {
                        ItemInfoPerfil(
                            Icons.Default.Menu, "Apellidos", model.usuarioActual.apellidos
                        )
                    }
                    HorizontalDivider(
                        Modifier.padding(vertical = 12.dp),
                        thickness = 1.dp,
                        color = Color(0xFF252525)
                    )
                }

                if (camposHabilitados) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { mostrarSelectorFecha = true }
                    ) {
                        OutlinedTextField(
                            value = fechaNacimientoCambiado,
                            onValueChange = {},
                            label = { Text("Fecha de Nacimiento",
                                fontFamily = miTipografia, color = Color.Gray) },
                            singleLine = true,
                            readOnly = true,
                            enabled = false,
                            leadingIcon = {
                                Icon(
                                    Icons.Default.DateRange,
                                    "",
                                    tint = Color.LightGray
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,

                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = ColorBordeTextField,

                                cursorColor = Color.White,

                                focusedLeadingIconColor = Color.White,
                                unfocusedLeadingIconColor = Color.DarkGray,

                                focusedContainerColor = ColorTextFieldSeleccionado,
                                unfocusedContainerColor = ColorTextFieldNoSeleccionado,
                                focusedLabelColor = ColorBordeTextField
                            )
                        )
                    }

                    SelectorFecha(
                        mostrarSelectorFecha,
                        { mostrarSelectorFecha = false },
                        { fechaNacimientoCambiado = it },
                        fechaMinima = LocalDate(1926, 1, 1),
                        fechaMaxima = LocalDate(2026, 12, 31)
                    )
                } else {
                    ItemInfoPerfil(
                        Icons.Default.DateRange,
                        "Fecha de Nacimiento",
                        if (model.usuarioActual.fechaNacimiento.length >= 10) model.usuarioActual.fechaNacimiento.substring(
                            0,
                            10
                        ) else ""
                    )
                }
            }
        }
        Spacer(Modifier.height(30.dp))
        Button(
            {
                actualizarPerfilViewModel.cerrarSesion()
                onCerrarSesion()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .testTag("BotonCerrarSesion"),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E1E)),
            shape = RoundedCornerShape(25.dp),
            border = BorderStroke(1.dp, Color(0xFF333333))
        ) {
            Text(
                "Cerrar Sesión",
                color = Color(0xFFE57373),
                fontFamily = miTipografia,
                fontWeight = Bold,
                fontSize = 16.sp
            )
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
fun ItemInfoPerfil(icono: ImageVector, titulo: String, valor: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            icono, "", tint = ColorPrimario, modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(15.dp))
        Column {
            Text(titulo, color = Color.DarkGray,
                fontFamily = miTipografia, fontSize = 12.sp)
            Text(valor, color = ColorTextoSecundario,
                fontFamily = miTipografia, fontSize = 15.sp, fontWeight = Bold)
        }
    }
}

@Composable
fun TextFieldCampo(
    icono: ImageVector, titulo: String, texto: String, cambiarTexto: (String) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = texto,
            onValueChange = cambiarTexto,
            label = { Text(titulo, color = Color.Gray, fontFamily = miTipografia) },
            singleLine = true,
            leadingIcon = { Icon(icono, "", tint = Color.LightGray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,

                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = ColorBordeTextField,

                cursorColor = Color.White,

                focusedLeadingIconColor = Color.White,
                unfocusedLeadingIconColor = Color.DarkGray,

                focusedContainerColor = ColorTextFieldSeleccionado,
                unfocusedContainerColor = ColorTextFieldNoSeleccionado,
                focusedLabelColor = ColorBordeTextField
            )
        )
    }
}