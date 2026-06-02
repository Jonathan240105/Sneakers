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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.snkrsapp.Domain.PublicacionPerfilItem
import com.example.snkrsapp.Views.ViewModels.ListadoViewModel

@Composable
fun PantallaListados(
    navegarADetalle: () -> Unit,
    myViewModel: ListadoViewModel,
    paddingValues: PaddingValues,
    id: Int = 0,
    uid: String? = null
) {

    LaunchedEffect(uid) {
        myViewModel.cargarDatosPerfil(uid)
    }

    val model by myViewModel.model.collectAsState()
    val esMiPerfil = model.esMiPerfil

    var pestañaSeleccionada by remember { mutableIntStateOf(id) }

    val listadoPestañas = if (esMiPerfil) {
        listOf("Colección", "Ventas", "Carrito")
    } else {
        listOf("Colección", "Ventas")
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            Modifier.fillMaxSize(),
            containerColor = Color(0xFF121212),
            topBar = {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF121212))
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp)
                ) {
                    Spacer(Modifier.height(14.dp))

                    TituloListadosConVolver(
                        texto = if (esMiPerfil) "Mis Artículos" else "Artículos",
                        mostrarBotonVolver = !esMiPerfil,
                        onVolverClick = navegarADetalle
                    )

                    TabRow(
                        selectedTabIndex = pestañaSeleccionada.coerceAtMost(listadoPestañas.lastIndex),
                        containerColor = Color(0xFF121212),
                        contentColor = Color.White,
                        indicator = { posicionesTab ->
                            val indiceFijo =
                                pestañaSeleccionada.coerceAtMost(listadoPestañas.lastIndex)
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(posicionesTab[indiceFijo]),
                                color = Color.White
                            )
                        },
                        divider = { HorizontalDivider(color = Color(0xFF252525)) }
                    ) {
                        listadoPestañas.forEachIndexed { indice, titulo ->
                            Tab(
                                pestañaSeleccionada == indice,
                                { pestañaSeleccionada = indice },
                                text = {
                                    Text(
                                        titulo,
                                        fontSize = 15.sp,
                                        fontWeight = if (pestañaSeleccionada == indice) Bold else null
                                    )
                                }
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        ) { paddingScaffold ->

            LazyVerticalGrid(
                GridCells.Fixed(2),
                contentPadding = PaddingValues(
                    top = paddingScaffold.calculateTopPadding() + 8.dp,
                    bottom = paddingValues.calculateBottomPadding() + 16.dp + (if (esMiPerfil && listadoPestañas[pestañaSeleccionada] == "Carrito" && model.listaCarrito.isNotEmpty()) 100.dp else 0.dp),
                    start = 16.dp,
                    end = 16.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                when (listadoPestañas[pestañaSeleccionada]) {
                    "Colección" -> {
                        if (model.listaColeccion.isEmpty()) {
                            item(span = { GridItemSpan(2) }) {
                                MensajeListadoVacio(if (esMiPerfil) "Tu armario de colección está vacío." else "Este armario está vacío.")
                            }
                        } else {
                            items(model.listaColeccion) { sneaker ->
                                CardItemSneakerUnificada(
                                    sneaker.modelo,
                                    sneaker.precio,
                                    sneaker.urlFoto,
                                    navegarADetalle
                                )
                            }
                        }
                    }

                    "Ventas" -> {
                        if (model.listaVentas.isEmpty()) {
                            item(span = { GridItemSpan(2) }) {
                                MensajeListadoVacio(if (esMiPerfil) "No hay artículos publicados para la venta." else "No tiene productos en venta.")
                            }
                        } else {
                            items(model.listaVentas) { publicacion ->
                                CardItemSneakerUnificada(
                                    publicacion.modelo,
                                    publicacion.precio,
                                    publicacion.urlFoto,
                                    onClick = navegarADetalle
                                )
                            }
                        }
                    }

                    "Carrito" -> {
                        if (model.listaCarrito.isEmpty()) {
                            item(span = { GridItemSpan(2) }) {
                                MensajeListadoVacio("No tienes productos añadidos al carrito.")
                            }
                        } else {
                            items(model.listaCarrito) { articulo ->
                                CardItemSneakerUnificada(
                                    articulo.modelo,
                                    articulo.precio,
                                    articulo.urlFoto,
                                    navegarADetalle
                                )
                            }
                        }
                    }
                }
            }
        }

        if (esMiPerfil && listadoPestañas[pestañaSeleccionada] == "Carrito" && model.listaCarrito.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = paddingValues.calculateBottomPadding())
            ) {
                BarraInferiorCompra(
                    model.listaCarrito,
                    { myViewModel.procesarCompra() }
                )
            }
        }
    }
}

@Composable
fun TituloListadosConVolver(
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
                    .background(Color(0xFF1E1E1E),CircleShape)
                    .size(40.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }
            Spacer(Modifier.width(15.dp))
        }

        Text(
            text = texto,
            color = Color.White,
            style = TextStyle(fontSize = 25.sp, fontWeight = Bold)
        )
    }
}

@Composable
fun CardItemSneakerUnificada(modelo: String, precio: Double, urlFoto: String, onClick: () -> Unit) {
    Card(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(25.dp)
    ) {
        Column {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color(0xFF252525)),
                Alignment.Center
            ) {
                AsyncImage(urlFoto, modelo, modifier = Modifier.padding(12.dp))
            }
            Column(modifier = Modifier.padding(15.dp)) {
                Text(modelo, color = Color.White, fontSize = 17.sp, fontWeight = Bold, maxLines = 1)
                Spacer(Modifier.height(5.dp))
                Text("$precio €", color = Color.White, fontSize = 16.sp, fontWeight = Bold)
            }
        }
    }
}

@Composable
fun BarraInferiorCompra(listaCarrito: List<PublicacionPerfilItem>, onComprarClick: () -> Unit) {
    val costeTotal = listaCarrito.sumOf { it.precio }
    Card(
        Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
    ) {
        Row(
            Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {
            Column {
                Text("Total", color = Color.Gray, fontSize = 14.sp)
                Text("$costeTotal €", color = Color.White, fontSize = 22.sp, fontWeight = Bold)
            }
            Button(
                onComprarClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text("Comprar", color = Color.Black, fontWeight = Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun MensajeListadoVacio(texto: String) {
    Text(
        texto,
        color = Color.DarkGray,
        fontSize = 15.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 60.dp)
    )
}