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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.snkrsapp.Views.ViewModels.PerfilViewModel

@Composable
fun PantallaPerfil(cambiarAConfig: () -> Unit, myViewModel: PerfilViewModel) {

    LaunchedEffect(Unit) {
        myViewModel.cargarPerfil()
    }

    val model by myViewModel.model.collectAsState()
    val estadoScroll = rememberScrollState()

    val coleccionEjemplo = listOf("Jordan 1", "Yeezy 350", "Dunk Low", "Air Max 90", "Forum Low")
    val ventasEjemplo = listOf("New Balance 550", "Travis Scott AJ1")
    val favoritos = listOf("Jordan 1", "Yeezy 350", "Dunk Low", "Air Max 90", "Forum Low")

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(horizontal = 20.dp)
            .verticalScroll(estadoScroll)
    ) {
        Spacer(Modifier.height(30.dp))

        Card(
            Modifier
                .fillMaxWidth()
                .testTag("HeaderPerfil"),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
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
                        Icon(Icons.Default.Person, null, Modifier.size(40.dp), tint = Color.White)
                    }
                    Spacer(Modifier.width(15.dp))
                    Column {
                        Text(
                            "@${model.usuarioActual.nombreUsuario}",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = Bold
                        )
                        Text(model.usuarioActual.email, color = Color.Gray, fontSize = 13.sp)
                    }
                    Spacer(Modifier.weight(1f))
                    Column {
                        Icon(
                            Icons.Default.Settings,
                            "",
                            tint = Color.White,
                            modifier = Modifier.clickable(
                                onClick = { cambiarAConfig() }))
                    }
                }
                HorizontalDivider(
                    Modifier.padding(vertical = 20.dp), thickness = 1.dp, color = Color(0xFF252525)
                )
                Row(
                    Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    EstadisticaItem("Colección", coleccionEjemplo.size.toString())
                    Box(
                        Modifier
                            .width(1.dp)
                            .height(30.dp)
                            .background(Color(0xFF333333))
                    )
                    EstadisticaItem("En Venta", ventasEjemplo.size.toString())
                    Box(
                        Modifier
                            .width(1.dp)
                            .height(30.dp)
                            .background(Color(0xFF333333))
                    )
                    EstadisticaItem("Favoritos", favoritos.size.toString())
                }
            }
        }
        Spacer(Modifier.height(30.dp))
        listaProductos(
            titulo = "Mi Colección", items = coleccionEjemplo, VerTodo = {})
        Spacer(Modifier.height(24.dp))
        listaProductos(
            titulo = "Mis Ventas", items = ventasEjemplo, VerTodo = {})
        Spacer(modifier = Modifier.height(24.dp))
        listaProductos(
            "Favoritos", favoritos, {})
    }
}

@Composable
fun listaProductos(
    titulo: String, items: List<String>, VerTodo: () -> Unit
) {
    Column {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(titulo, color = Color.White, fontSize = 18.sp, fontWeight = Bold)
            Text(
                "Ver todo",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.clickable { VerTodo() })
        }
        Spacer(Modifier.height(16.dp))

        if (items.isEmpty()) {
            Text(
                "No hay artículos todavía",
                color = Color.DarkGray,
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 20.dp)
            )
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items) { item ->
                    Card(
                        Modifier.size(width = 140.dp, height = 100.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = null,
                                tint = Color(0xFF333333),
                                modifier = Modifier.size(40.dp)
                            )
                            Text(
                                item,
                                color = Color.White,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EstadisticaItem(titulo: String, valor: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            valor, color = Color.White, fontSize = 18.sp, fontWeight = Bold
        )
        Text(
            titulo, color = Color.Gray, fontSize = 12.sp
        )
    }
}