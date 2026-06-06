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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.snkrsapp.Domain.ProductoColeccionItem
import com.example.snkrsapp.Domain.PublicacionPerfilItem
import com.example.snkrsapp.Views.ViewModels.PerfilViewModel

@Composable
fun PantallaPerfil(
    uidPerfilAVisualizar: String? = null,
    cambiarAConfig: () -> Unit,
    volverAtras: () -> Unit,
    myViewModel: PerfilViewModel,
    navegarAListado: (Int) -> Unit
) {

    LaunchedEffect(uidPerfilAVisualizar) {
        myViewModel.cargarPerfil(uidPerfilAVisualizar)
        myViewModel.cargarListados(uidPerfilAVisualizar)
    }

    val model by myViewModel.model.collectAsState()
    val estadoScroll = rememberScrollState()

    val esMiPerfil = model.esMiPerfil

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(horizontal = 20.dp)
            .verticalScroll(estadoScroll)
    ) {
        Spacer(Modifier.height(20.dp))

        HeaderPerfilTop(
            texto = if (esMiPerfil) "Mi Perfil" else "Perfil",
            mostrarBotonVolver = uidPerfilAVisualizar != null,
            onVolverClick = volverAtras
        )

        Spacer(Modifier.height(10.dp))

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
                        AsyncImage(model.usuarioActual.urlFoto, "", Modifier.size(40.dp))
                    }
                    Spacer(Modifier.weight(1f))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "@${model.usuarioActual.nombreUsuario}",
                            color = Color.White,
                            fontSize = 20.sp,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            fontWeight = Bold
                        )
                        Text(model.usuarioActual.email, color = Color.Gray, fontSize = 13.sp)
                    }

                    Spacer(Modifier.weight(1f))

                    if (esMiPerfil) {
                        Column {
                            Icon(
                                Icons.Default.Settings,
                                "",
                                tint = Color.White,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable(onClick = { cambiarAConfig() })
                                    .padding(15.dp)
                                    .testTag("BotonConfig")
                            )
                        }
                    }
                }

                HorizontalDivider(
                    Modifier.padding(vertical = 20.dp), thickness = 1.dp, color = Color(0xFF252525)
                )

                Row(
                    Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    EstadisticaItem("Colección", model.listaColeccion.size.toString())
                    Box(
                        Modifier
                            .width(1.dp)
                            .height(30.dp)
                            .background(Color(0xFF333333))
                    )
                    EstadisticaItem("En Venta", model.listaVentas.size.toString())

                    if (esMiPerfil) {
                        Box(
                            Modifier
                                .width(1.dp)
                                .height(30.dp)
                                .background(Color(0xFF333333))
                        )
                        EstadisticaItem("Carrito", model.listaCarrito.size.toString())
                        Box(
                            Modifier
                                .width(1.dp)
                                .height(30.dp)
                                .background(Color(0xFF333333))
                        )
                        EstadisticaItem("Saldo", model.usuarioActual.saldo.toString())
                    }
                }
            }
        }

        Spacer(Modifier.height(30.dp))

        ListaProductos(
            titulo = if (esMiPerfil) "Mi Colección" else "Su Colección",
            items = model.listaColeccion.take(4),
            verTodo = { navegarAListado(0) }
        )
        Spacer(Modifier.height(24.dp))

        ListaProductosVentasYCarrito(
            titulo = if (esMiPerfil) "Mis Ventas" else "En Venta",
            items = model.listaVentas.take(4),
            verTodo = { navegarAListado(1) }
        )

        if (esMiPerfil) {
            Spacer(modifier = Modifier.height(24.dp))
            ListaProductosVentasYCarrito(
                titulo = "Carrito",
                items = model.listaCarrito.take(4),
                verTodo = { navegarAListado(2) },
                modifier = Modifier.testTag("listaFavoritos")
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}


@Composable
fun HeaderPerfilTop(
    texto: String,
    mostrarBotonVolver: Boolean,
    onVolverClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (mostrarBotonVolver) {
            IconButton(
                onClick = onVolverClick,
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
            Spacer(Modifier.width(15.dp))
        }

        Text(
            text = texto,
            color = Color.White,
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 25.sp,
                fontWeight = Bold
            )
        )
    }
}

@Composable
fun ListaProductos(
    titulo: String,
    items: List<ProductoColeccionItem>,
    verTodo: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Text(titulo, color = Color.White, fontSize = 18.sp, fontWeight = Bold)
            Text(
                "Ver todo",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.clickable { verTodo() })
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
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(items) { item ->
                    Card(
                        Modifier.size(width = 140.dp, height = 120.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            AsyncImage(
                                item.urlFoto,
                                "",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 12.dp)
                                    .padding(top = 8.dp, bottom = 24.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(45.dp)
                                    .align(Alignment.BottomCenter)
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color(0xAA000000),
                                                Color(0xFF000000)
                                            )
                                        )
                                    )
                            )

                            Text(
                                text = item.modelo,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 6.dp)
                                    .padding(horizontal = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ListaProductosVentasYCarrito(
    titulo: String,
    items: List<PublicacionPerfilItem>,
    verTodo: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Text(titulo, color = Color.White, fontSize = 18.sp, fontWeight = Bold)
            Text(
                "Ver todo",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.clickable { verTodo() })
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
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(items) { item ->
                    Card(
                        Modifier.size(width = 140.dp, height = 100.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {

                            AsyncImage(
                                model = item.urlFoto,
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 12.dp)
                                    .padding(
                                        top = 8.dp,
                                        bottom = 24.dp
                                    )
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(45.dp)
                                    .align(Alignment.BottomCenter)
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color(0xAA000000),
                                                Color(0xFF000000)
                                            )
                                        )
                                    )
                            )

                            Text(
                                text = item.modelo,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 6.dp)
                                    .padding(horizontal = 8.dp)
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
        Text(valor, color = Color.White, fontSize = 18.sp, fontWeight = Bold)
        Text(titulo, color = Color.Gray, fontSize = 12.sp)
    }
}