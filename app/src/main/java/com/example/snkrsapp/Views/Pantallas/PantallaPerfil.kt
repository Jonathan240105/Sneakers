package com.example.snkrsapp.Views.Pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.Usuario
import com.example.snkrsapp.Views.ViewModels.PerfilViewModel

@Composable
fun PantallaPerfil(onCerrarSesion: () -> Unit, myViewModel: PerfilViewModel) {

    LaunchedEffect(Unit) {
        myViewModel.cargarPerfil()
    }

    val model by myViewModel.model.collectAsState()
    val estadoScroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(horizontal = 20.dp)
            .verticalScroll(estadoScroll)
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
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
                    Modifier.size(60.dp),
                    tint = Color.White
                )
            }
            Spacer(Modifier.height(15.dp))
            Text(
                "@${model.usuarioActual.nombreUsuario}",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = Bold
            )
            Spacer(Modifier.height(5.dp))
            Text(
                model.usuarioActual.email,
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            "Información Personal",
            Modifier.padding(bottom = 12.dp),
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = Bold
        )
        Card(
            Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
            shape = RoundedCornerShape(25.dp)
        ) {
            Column(Modifier.padding(20.dp)) {
                ItemInfoPerfil(
                    Icons.Default.Person,
                    "Nombre Completo",
                    "${model.usuarioActual.nombreUsuario} ${model.usuarioActual.apellidos}"
                )
                HorizontalDivider(
                    Modifier.padding(vertical = 12.dp),
                    thickness = 1.dp,
                    color = Color(0xFF252525)
                )
                ItemInfoPerfil(
                    Icons.Default.DateRange,
                    "Fecha de Nacimiento",
                    model.usuarioActual.fechaNacimiento
                )
                HorizontalDivider(
                    Modifier.padding(vertical = 12.dp),
                    thickness = 1.dp,
                    color = Color(0xFF252525)
                )
                ItemInfoPerfil(
                    Icons.Default.Menu,
                    "User ID",
                    model.usuarioActual.UID
                )
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
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text("Cerrar Sesión", color = Color.Black, fontWeight = Bold, fontSize = 16.sp)
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
fun ItemInfoPerfil(icono: ImageVector, titulo: String, valor: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            icono,
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(15.dp))
        Column {
            Text(titulo, color = Color.Gray, fontSize = 12.sp)
            Text(valor, color = Color.White, fontSize = 15.sp, fontWeight = Bold)
        }
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
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    icono,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(25.dp)
                )
                Spacer(Modifier.width(15.dp))
                Text(titulo, color = Color.White, fontSize = 15.sp)
            }
        }
    }
}