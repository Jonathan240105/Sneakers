package com.example.snkrsapp.Views.Pantallas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.snkrsapp.Domain.Marca
import com.example.snkrsapp.Domain.ModelPrincipal
import com.example.snkrsapp.Domain.Producto
import com.example.snkrsapp.R
import com.example.snkrsapp.Views.ViewModels.PrincipalViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(myViewModel: PrincipalViewModel, navegarADetalle: (Int, Int) -> Unit) {

    val model by myViewModel.model.collectAsState()
    var nombreBuscado by remember { mutableStateOf("") }
    var mostrarFiltros by remember { mutableStateOf(false) }

    val estadoHoja = rememberModalBottomSheetState()
    val estadoLista = rememberLazyGridState()

    LaunchedEffect(estadoLista.canScrollForward, model.listaDeproductos.size) {
        if (!estadoLista.canScrollForward && !model.cargandoProductos && model.listaDeproductos.isNotEmpty() && !model.esBusquedaTexto) {
            myViewModel.cargarPaginaProductos()
        }
    }
    LaunchedEffect(nombreBuscado) {
        delay(350)
        myViewModel.buscarZapatillasPorTexto(nombreBuscado)
    }

    LaunchedEffect(Unit) {
        myViewModel.cargarPaginaProductos()
        myViewModel.cargarMarcas()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .background((Color(0xFF121212)))
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(14.dp))
            TituloEventos("Explorar Sneakers")
            Spacer(modifier = Modifier.height(12.dp))

            BuscadorConFiltros(
                nombreBuscado = nombreBuscado,
                cambiarBuscador = { texto -> nombreBuscado = texto },
                mostrarFiltros = { mostrarFiltros = true }
            )
            Spacer(Modifier.height(5.dp))
            BarraFiltrosActivos(
                model,
                {
                    myViewModel.cambiarTalla(null)
                    myViewModel.aplicarFiltrosDesdeHoja()
                },
                {
                    myViewModel.cambiarRangoPrecio(0, 1000)
                    myViewModel.aplicarFiltrosDesdeHoja()
                },
                { idMarca ->
                    myViewModel.alternarMarcaTemporal(idMarca)
                    myViewModel.aplicarFiltrosDesdeHoja()
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                top = 8.dp,
                bottom = 80.dp,
                start = 16.dp,
                end = 16.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            state = estadoLista,
            modifier = Modifier.fillMaxSize()
        ) {
            item(span = { GridItemSpan(2) }) {
                Column {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        contentPadding = PaddingValues(end = 12.dp)
                    ) {
                        items(model.listaMarcas) { marca ->
                            val estaSeleccionada =
                                model.marcasSeleccionadas?.contains(marca.idMarca) == true
                            CardMarca(
                                marca = marca,
                                seleccionada = estaSeleccionada,
                                elegirMarca = {
                                    myViewModel.alternarMarcaTemporal(marca.idMarca)
                                    myViewModel.aplicarFiltrosDesdeHoja()
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }

            items(model.listaDeproductos) {
                CardProducto(
                    it,
                    { navegarADetalle(it.idProducto ?: 0, it.idMarca) },
                    myViewModel.getNombreMarca(it.idMarca)
                )
            }

            if (model.cargandoProductos) {
                item(span = { GridItemSpan(2) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            Modifier.size(40.dp),
                            color = Color.White,
                            strokeWidth = 4.dp
                        )
                    }
                }
            }
        }

        if (mostrarFiltros) {
            ModalBottomSheet(
                onDismissRequest = { mostrarFiltros = false },
                sheetState = estadoHoja,
                containerColor = Color(0xFF1E1E1E),
                dragHandle = { BottomSheetDefaults.DragHandle(color = Color.Gray) }
            ) {
                CardFiltros(
                    model = model,
                    onPrecioChange = { min, max -> myViewModel.cambiarRangoPrecio(min, max) },
                    onTallaClick = { myViewModel.cambiarTalla(it) },
                    onMarcaClick = { myViewModel.alternarMarcaTemporal(it) },
                    onAplicarClick = {
                        myViewModel.aplicarFiltrosDesdeHoja()
                        mostrarFiltros = false
                    }
                )
            }
        }
    }
}

@Composable
fun CardMarca(marca: Marca, seleccionada: Boolean, elegirMarca: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(end = 4.dp)
            .height(55.dp)
            .width(150.dp)
            .clickable(onClick = elegirMarca)
            .testTag("cardMarca"),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        border = if (seleccionada) BorderStroke(width = 1.5.dp, color = Color.White) else null
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
            AsyncImage(
                marca.logoUrl,
                "",
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(50.dp),
                contentScale = ContentScale.Fit
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color(0xFF1E1E1E)
                    )
            )
            Text(
                text = marca.nombre ?: "",
                color = Color.White,
                fontWeight = Bold,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 14.dp)
            )
        }
    }
}

@Composable
fun CardProducto(producto: Producto, onclick: () -> Unit, nombreMarca: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onclick),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(25.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color(0xFF252525)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = producto.imagenUrl ?: "",
                    contentDescription = "",
                    modifier = Modifier.padding(12.dp)
                )
            }
            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = producto.modelo,
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = Bold,
                    maxLines = 1
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = nombreMarca ?: "",
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${producto.precio ?: ""} €",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = Bold
                    )
                }
            }
        }
    }
}

@Composable
fun BuscadorConFiltros(
    nombreBuscado: String, cambiarBuscador: (String) -> Unit, mostrarFiltros: () -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = nombreBuscado,
            onValueChange = cambiarBuscador,
            placeholder = { Text("Buscar zapatilla, modelo ...", color = Color.Gray) },
            modifier = Modifier
                .weight(1f)
                .height(54.dp)
                .testTag("TextFieldZapatillas"),
            trailingIcon = { Icon(Icons.Default.Search, "", tint = Color.White) },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                unfocusedBorderColor = Color(0xFF333333),
                focusedBorderColor = Color.White,
                cursorColor = Color.White,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        Box(
            Modifier
                .size(54.dp)
                .clickable(onClick = mostrarFiltros)
                .border(
                    BorderStroke(width = 1.dp, color = Color(0xFF333333)),
                    shape = RoundedCornerShape(12.dp)
                )
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF1E1E1E))
                .testTag("botonFiltros"),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painterResource(R.drawable.baseline_filter_list_alt_24),
                "",
                modifier = Modifier.size(24.dp),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
            )
        }
    }
}
@Composable
fun BarraFiltrosActivos(
    model: ModelPrincipal,
    onQuitarTalla: () -> Unit,
    onQuitarPrecio: () -> Unit,
    onQuitarMarca: (Int) -> Unit
) {
    val tieneTalla = model.talla != null
    val tienePrecio = (model.minPrecio != null && model.minPrecio != 0) || (model.maxPrecio != null && model.maxPrecio != 1000)
    val marcasActivas = model.marcasSeleccionadas ?: emptyList()

    val hayFiltros = tieneTalla || tienePrecio || marcasActivas.isNotEmpty()

    if (hayFiltros) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (tieneTalla) {
                item {
                    ChipFiltro(texto = "Talla: ${model.talla}", onBorrar = onQuitarTalla)
                }
            }

            if (tienePrecio) {
                item {
                    ChipFiltro(
                        texto = "${model.minPrecio ?: 0}€ - ${model.maxPrecio ?: 1000}€",
                        onBorrar = onQuitarPrecio
                    )
                }
            }

            items(marcasActivas) { idMarca ->
                val nombreMarca = model.listaMarcas.find { it.idMarca == idMarca }?.nombre ?: "Marca"
                ChipFiltro(texto = nombreMarca, onBorrar = { onQuitarMarca(idMarca) })
            }
        }
        Spacer(Modifier.height(4.dp))
    }
}

@Composable
fun ChipFiltro(texto: String, onBorrar: () -> Unit) {
    Row(
        Modifier
            .background(Color(0xFF252525), RoundedCornerShape(50.dp))
            .border(1.dp, Color(0xFF333333), RoundedCornerShape(50.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text = texto, color = Color.White, fontSize = 12.sp, fontWeight = Bold)
        Icon(
            Icons.Default.Close,
            contentDescription = "Quitar filtro",
            tint = Color.Gray,
            modifier = Modifier
                .size(14.dp)
                .clickable { onBorrar() }
        )
    }
}
@Composable
fun CardFiltros(
    model: ModelPrincipal,
    onPrecioChange: (Int, Int) -> Unit,
    onTallaClick: (Double?) -> Unit,
    onMarcaClick: (Int) -> Unit,
    onAplicarClick: () -> Unit
) {
    val minActual = model.minPrecio?.toFloat() ?: 0f
    val maxActual = model.maxPrecio?.toFloat() ?: 500f

    val tallas = (60..100).map { it / 2.0 }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp)
    ) {
        Text(
            text = "Filtros",
            style = TextStyle(fontSize = 25.sp, fontWeight = Bold, color = Color.White)
        )
        Spacer(Modifier.height(25.dp))

        Text("Rango de Precio", color = Color.Gray, fontSize = 14.sp)
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("${minActual.toInt()}€", color = Color.White)
            Text("${maxActual.toInt()}€", color = Color.White)
        }

        RangeSlider(
            value = minActual..maxActual,
            onValueChange = { onPrecioChange(it.start.toInt(), it.endInclusive.toInt()) },
            valueRange = 0f..1000f,
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White,
                inactiveTrackColor = Color(0xFF333333)
            )
        )
        Spacer(Modifier.height(25.dp))

        Text("Talla", color = Color.Gray, fontSize = 14.sp)
        Spacer(Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.height(180.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tallas) { tallaItem ->
                val esTallaSeleccionada = model.talla == tallaItem
                Box(
                    modifier = Modifier
                        .height(45.dp)
                        .background(
                            if (esTallaSeleccionada) Color.White else Color(0xFF1E1E1E),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            onTallaClick(if (esTallaSeleccionada) null else tallaItem)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tallaItem.toString(),
                        color = if (esTallaSeleccionada) Color.Black else Color.White,
                        fontWeight = Bold
                    )
                }
            }
        }
        Spacer(Modifier.height(25.dp))

        Text("Marcas", color = Color.Gray, fontSize = 14.sp)
        Spacer(Modifier.height(12.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(model.listaMarcas) { marca ->
                val estaMarcada = model.marcas?.contains(marca.idMarca) == true
                Box(
                    modifier = Modifier
                        .background(
                            if (estaMarcada) Color.White else Color(0xFF1E1E1E),
                            RoundedCornerShape(20.dp)
                        )
                        .border(
                            1.dp,
                            if (estaMarcada) Color.White else Color(0xFF333333),
                            RoundedCornerShape(20.dp)
                        )
                        .clickable { onMarcaClick(marca.idMarca) }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = marca.nombre ?: "",
                        color = if (estaMarcada) Color.Black else Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(40.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .clickable { onAplicarClick() }
                    .testTag("botonAplicarFiltros"),
                contentAlignment = Alignment.Center
            ) {
                Text("Aplicar Filtros", color = Color.Black, fontWeight = Bold, fontSize = 16.sp)
            }
        }
        Spacer(Modifier.height(15.dp))
    }
}

@Composable
fun TituloEventos(texto: String) {
    Text(
        texto,
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .testTag("tituloPrincipal"),
        style = TextStyle(
            fontSize = 25.sp, fontWeight = Bold
        )
    )
}