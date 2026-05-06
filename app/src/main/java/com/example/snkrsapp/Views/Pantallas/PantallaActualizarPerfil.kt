package com.example.snkrsapp.Views.Pantallas

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
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
import com.example.snkrsapp.Views.ViewModels.ActualizarPerfilViewModel
import com.example.snkrsapp.Views.ViewModels.PerfilViewModel

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
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
                    contentDescription = "Volver",
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
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    Modifier
                        .size(60.dp)
                        .testTag("IconoUsuario"),
                    tint = Color.White
                )
            }
            Spacer(Modifier.height(15.dp))
            Text(
                "@${model.usuarioActual.nombreUsuario}",
                color = Color.White,
                modifier = Modifier.testTag("NombreUsuario"),
                fontSize = 24.sp,
                fontWeight = Bold
            )
            Spacer(Modifier.height(5.dp))
            Text(
                model.usuarioActual.email, color = Color.Gray, fontSize = 14.sp
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
                color = Color.White,
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
                            .size(40.dp)
                            .background(Color.DarkGray, CircleShape)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            "",
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(Modifier.width(12.dp))

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
                            .size(40.dp)
                            .background(Color(0xFF1E1E1E), CircleShape)
                    ) {
                        Icon(
                            Icons.Default.Check,
                            "",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } else {
                    Button(
                        onClick = { camposHabilitados = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF252525)),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text("Editar", fontSize = 14.sp, color = Color.White)
                    }
                }
            }
        }

        Card(
            Modifier
                .fillMaxWidth()
                .testTag("CardPerfil"),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
            shape = RoundedCornerShape(25.dp)
        ) {
            Column(Modifier.padding(20.dp)) {
                if (camposHabilitados) {
                    TextFieldCampo(
                        Icons.Default.Person, "Nombre de usuario", nombreCambiado
                    ) { nombreCambiado = it }
                } else {
                    ItemInfoPerfil(
                        Icons.Default.Person,
                        "Nombre de usuario",
                        "${model.usuarioActual.nombreUsuario} "
                    )
                }
//                HorizontalDivider(
//                    Modifier.padding(vertical = 12.dp), thickness = 1.dp, color = Color(0xFF252525)
//                )
//                if (camposHabilitados) {
//                    TextFieldCampo(
//                        Icons.Default.Person, "Email", emailCambiado
//                    ) { emailCambiado = it }
//                } else {
//                    ItemInfoPerfil(
//                        Icons.Default.Person, "Email", model.usuarioActual.email
//                    )
//
//                }
                HorizontalDivider(
                    Modifier.padding(vertical = 12.dp), thickness = 1.dp, color = Color(0xFF252525)
                )
                if (model.usuarioActual.apellidos.isNotEmpty()) {
                    if (camposHabilitados) {
                        TextFieldCampo(
                            Icons.Default.Person, "Apellidos", apellidosCambiado
                        ) { apellidosCambiado = it }
                    } else {
                        ItemInfoPerfil(
                            Icons.Default.Menu, "Apellidos", model.usuarioActual.apellidos
                        )

                    }
                }
                HorizontalDivider(
                    Modifier.padding(vertical = 12.dp), thickness = 1.dp, color = Color(0xFF252525)
                )
                if (camposHabilitados) {
                    TextFieldCampo(
                        Icons.Default.DateRange, "Fecha de Nacimiento", fechaNacimientoCambiado
                    ) { fechaNacimientoCambiado = it }
                } else {
                    ItemInfoPerfil(
                        Icons.Default.DateRange,
                        "Fecha de Nacimiento",
                        model.usuarioActual.fechaNacimiento.substring(0, 10)
                    )
                }
            }
        }
        Spacer(Modifier.height(30.dp))
        Text(
            "Cuenta",
            Modifier.padding(bottom = 12.dp),
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = Bold
        )
        CardAccion(Icons.Default.Favorite, "Mis Favoritos") {}
        Spacer(Modifier.height(8.dp))
        CardAccion(Icons.Default.ShoppingCart, "Mis Pedidos") {}
        Spacer(Modifier.height(8.dp))
        CardAccion(Icons.Default.Settings, "Ajustes de Cuenta") {}
        Spacer(Modifier.height(40.dp))
        Button(
            onCerrarSesion,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .testTag("BotonCerrarSesion"),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text(
                "Cerrar Sesión",
                color = Color.Red,
                fontWeight = Bold,
                fontSize = 16.sp,
                modifier = Modifier
            )
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
fun ItemInfoPerfil(icono: ImageVector, titulo: String, valor: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            icono, "", tint = Color.LightGray, modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(15.dp))
        Column {
            Text(titulo, color = Color.Gray, fontSize = 12.sp)
            Text(valor, color = Color.White, fontSize = 15.sp, fontWeight = Bold)
        }
    }
}

@Composable
fun TextFieldCampo(
    icono: ImageVector, titulo: String, texto: String, cambiarTexto: (String) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
//        Icon(
//            icono, "", tint = Color.LightGray, modifier = Modifier.size(20.dp)
//        )
//        Spacer(Modifier.width(15.dp))
        OutlinedTextField(
            texto,
            cambiarTexto,
            label = { Text(titulo) },
            singleLine = true,
            leadingIcon = { Icon(icono, "") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White, unfocusedTextColor = Color.LightGray
            )
        )
    }
}

@Composable
fun CardAccion(icono: ImageVector, titulo: String, onClick: () -> Unit) {
    Card(
        Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(15.dp)
    ) {
        Row(
            Modifier.padding(16.dp), Arrangement.SpaceBetween, Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    icono, null, tint = Color.White, modifier = Modifier.size(25.dp)
                )
                Spacer(Modifier.width(15.dp))
                Text(titulo, color = Color.White, fontSize = 15.sp)
            }
        }
    }
}

