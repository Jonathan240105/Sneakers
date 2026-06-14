package com.example.snkrsapp.Views.Pantallas

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.snkrsapp.Domain.PublicacionPerfilItem
import com.example.snkrsapp.Views.ViewModels.ListadoViewModel
import com.example.snkrsapp.ui.theme.ColorBordeTextField
import com.example.snkrsapp.ui.theme.ColorNeutroFondo
import com.example.snkrsapp.ui.theme.ColorPrimario
import com.example.snkrsapp.ui.theme.ColorTextFieldSeleccionado
import com.example.snkrsapp.ui.theme.ColorTextoSecundario
import com.example.snkrsapp.ui.theme.miTipografia

@Composable
fun PantallaListados(
    navegarADetalle: () -> Unit,
    myViewModel: ListadoViewModel,
    paddingValues: PaddingValues,
    id: Int = 0,
    uid: String? = null
) {

    val contexto = LocalContext.current
    LaunchedEffect(uid) {
        myViewModel.cargarDatosPerfil(uid)
    }

    val model by myViewModel.model.collectAsState()
    val esMiPerfil = model.esMiPerfil
    LaunchedEffect(model.listaCarrito) {
        if (model.listaCarrito.isEmpty() && model.cargandoPago) {
            Toast.makeText(contexto, "¡Compra realizada con éxito!", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(model.mensajeExito) {
        if (!model.mensajeExito.isNullOrBlank()) {
            Toast.makeText(contexto, model.mensajeExito, Toast.LENGTH_SHORT).show()
            myViewModel.limpiarMensajeExito()
        }
    }
    var pestañaSeleccionada by remember { mutableIntStateOf(id) }

    val listadoPestañas = if (esMiPerfil) {
        listOf("Colección", "Ventas", "Carrito")
    } else {
        listOf("Colección", "Ventas")
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            Modifier.fillMaxSize(),
            containerColor = ColorNeutroFondo,
            topBar = {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(ColorNeutroFondo)
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
                        containerColor = ColorNeutroFondo,
                        contentColor = Color.White,
                        indicator = { posicionesTab ->
                            val indiceFijo =
                                pestañaSeleccionada.coerceAtMost(listadoPestañas.lastIndex)
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(posicionesTab[indiceFijo]),
                                color = Color.White
                            )
                        },
                        divider = { HorizontalDivider(color = ColorTextoSecundario) }
                    ) {
                        listadoPestañas.forEachIndexed { indice, titulo ->
                            Tab(
                                pestañaSeleccionada == indice,
                                { pestañaSeleccionada = indice },
                                text = {
                                    Text(
                                        titulo,
                                        fontFamily = miTipografia,
                                        color = ColorTextoSecundario,
                                        fontSize = 18.sp,
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
                                CardPublicacion(
                                    sneaker.modelo,
                                    sneaker.precio,
                                    sneaker.urlFoto,
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
                                CardPublicacion(
                                    publicacion.modelo,
                                    publicacion.precio,
                                    publicacion.urlFoto,
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
                            if (model.error != null) {
                                item(span = { GridItemSpan(2) }) {
                                    Text(
                                        text = model.error!!,
                                        color = Color(0xFFFF6B6B),
                                        fontSize = 14.sp,
                                        fontFamily = miTipografia,
                                        fontWeight = Bold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                Color(0xFF2D1616),
                                                RoundedCornerShape(12.dp)
                                            )
                                            .padding(12.dp)
                                    )
                                }
                            }
                            items(model.listaCarrito) { articulo ->
                                CardPublicacion(
                                    articulo.modelo,
                                    articulo.precio,
                                    articulo.urlFoto,

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
                    { myViewModel.procesarCompra() },
                    model.cargandoPago
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
                    .background(Color.White, CircleShape)
                    .size(40.dp)
                    .border(BorderStroke(2.dp, ColorTextFieldSeleccionado))
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    "",
                    tint = ColorTextoSecundario
                )
            }
            Spacer(Modifier.width(15.dp))
        }

        Text(
            text = texto,
            fontFamily = miTipografia,
            color = ColorPrimario,
            style = TextStyle(fontSize = 25.sp, fontWeight = Bold)
        )
    }
}

@Composable
fun CardPublicacion(modelo: String, precio: Double, urlFoto: String) {
    Card(
        Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(25.dp),
        border = BorderStroke(1.dp, ColorBordeTextField)
    ) {
        Column {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color.Black),
                Alignment.Center
            ) {
                AsyncImage(urlFoto, modelo, modifier = Modifier.padding(12.dp))
            }
            Column(modifier = Modifier.padding(15.dp)) {
                Text(modelo, color = ColorPrimario,
                    fontFamily = miTipografia, fontSize = 17.sp, fontWeight = Bold, maxLines = 1)
                Spacer(Modifier.height(5.dp))
                Text("$precio €", fontFamily = miTipografia, color = ColorTextoSecundario, fontSize = 16.sp, fontWeight = Bold)
            }
        }
    }
}

@Composable
fun BarraInferiorCompra(
    listaCarrito: List<PublicacionPerfilItem>,
    onComprarClick: () -> Unit,
    cargandopago: Boolean
) {
    val costeTotal = listaCarrito.sumOf { it.precio }
    Card(
        Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ColorPrimario),
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
                Text("Total", color = Color.White,
                    fontFamily = miTipografia, fontSize = 17.sp, fontWeight = Bold)
                Text("$costeTotal €", fontFamily = miTipografia, color = Color.White, fontSize = 22.sp, fontWeight = Bold)
            }
            Button(
                onComprarClick,
                enabled = !cargandopago,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
            ) {
                if (cargandopago) {
                    CircularProgressIndicator(
                        color = Color.Black,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {

                    Text("Comprar",
                        fontFamily = miTipografia, color = Color.Black, fontWeight = Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun MensajeListadoVacio(texto: String) {
    Text(
        texto,
        color = ColorTextoSecundario,
        fontWeight = Bold,
        fontFamily = miTipografia,
        fontSize = 18.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 60.dp)
    )
}