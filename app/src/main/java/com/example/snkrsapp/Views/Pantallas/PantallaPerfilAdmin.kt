package com.example.snkrsapp.Views.Pantallas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.snkrsapp.Views.Controlador.NavigationUtils.BottomBarAdmin
import com.example.snkrsapp.Views.ViewModels.ActualizarPerfilViewModel
import com.example.snkrsapp.Views.ViewModels.PerfilViewModel
import com.example.snkrsapp.ui.theme.ColorNeutroFondo
import com.example.snkrsapp.ui.theme.ColorPrimario
import com.example.snkrsapp.ui.theme.ColorTextoSecundario
import com.example.snkrsapp.ui.theme.miTipografia

@Composable
fun PantallaPerfilAdmin(
    volverAtras: () -> Unit,
    actualizarPerfilViewModel: ActualizarPerfilViewModel,
    onCerrarSesion: () -> Unit,
    myViewModel: PerfilViewModel,
    navController: NavController
) {
    LaunchedEffect(Unit) {
        myViewModel.cargarPerfil()
    }
    val model by myViewModel.model.collectAsState()

    Scaffold(
        bottomBar = {
            BottomBarAdmin(navController)
        }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .background(ColorNeutroFondo)
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {


            Spacer(Modifier.height(20.dp))

            HeaderPerfilTop(
                texto = "Mi Perfil",
                mostrarBotonVolver = false,
                onVolverClick = volverAtras
            )

            Spacer(Modifier.height(175.dp))
            Card(
                Modifier
                    .fillMaxWidth()
                    .testTag("HeaderPerfil"),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(25.dp)
            ) {
                Column(
                    Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier
                                .size(70.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0xFF252525)), contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(model.usuarioActual.urlFoto, "", Modifier.size(40.dp))
                        }
                        Spacer(Modifier.weight(1f))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "@${model.usuarioActual.nombreUsuario}",
                                fontFamily = miTipografia,
                                color = ColorPrimario,
                                fontSize = 20.sp,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                fontWeight = Bold
                            )
                            Text(
                                model.usuarioActual.email,
                                fontFamily = miTipografia,
                                color = ColorTextoSecundario,
                                fontSize = 15.sp,
                                fontWeight = Bold
                            )
                        }

                        Spacer(Modifier.weight(1f))
                    }
                }
            }
            Spacer(Modifier.height(20.dp))

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
                    fontFamily = miTipografia,
                    color = Color(0xFFE57373),
                    fontWeight = Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}