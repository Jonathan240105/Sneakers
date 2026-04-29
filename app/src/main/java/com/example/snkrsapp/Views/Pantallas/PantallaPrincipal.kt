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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.snkrsapp.Domain.Marca
import com.example.snkrsapp.Domain.Producto
import com.example.snkrsapp.R
import com.example.snkrsapp.Views.ViewModels.PrincipalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(myViewModel: PrincipalViewModel) {

    val model by myViewModel.model.collectAsState()
    var nombreBuscado by remember { mutableStateOf("") }
    var mostrarFiltros by remember { mutableStateOf(false) }
    val estadoHoja = rememberModalBottomSheetState()
    val estadoLista = rememberLazyGridState()

    LaunchedEffect(estadoLista.canScrollForward, model.listaDeproductos.size) {
        if (!estadoLista.canScrollForward && !model.cargandoProductos && model.listaDeproductos.isNotEmpty()) {
            myViewModel.cargarPaginaProductos()
        }
    }

    val lista = listOf(
        Producto(modelo = "Jordan 1 Chicago", idMarca = 1, precio = 20, talla = 42),
        Producto(modelo = "Jordan 1 Chicago", idMarca = 1, precio = 20, talla = 42),
        Producto(modelo = "Jordan 1 Chicago", idMarca = 1, precio = 20, talla = 42),
        Producto(modelo = "Jordan 1 Chicago", idMarca = 1, precio = 20, talla = 42),
        Producto(modelo = "Jordan 1 Chicago", idMarca = 1, precio = 20, talla = 42),
        Producto(modelo = "Jordan 1 Chicago", idMarca = 1, precio = 20, talla = 42),
        Producto(modelo = "Jordan 1 Chicago", idMarca = 1, precio = 20, talla = 42),
        Producto(modelo = "Jordan 1 Chicago", idMarca = 1, precio = 20, talla = 42),
        Producto(modelo = "Jordan 1 Chicago", idMarca = 1, precio = 20, talla = 42),
        Producto(modelo = "Jordan 1 Chicago", idMarca = 1, precio = 20, talla = 42),
        Producto(modelo = "Jordan 1 Chicago", idMarca = 1, precio = 20, talla = 42),
        Producto(modelo = "Jordan 1 Chicago", idMarca = 1, precio = 20, talla = 42),
    )

    val listaMarcas = listOf("Nike", "Adidas", "Puma", "New Balance", "Reebok")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background((Color(0xFF121212)))
    ) {
        Column(Modifier.padding(horizontal = 16.dp)) {
            TituloPrincipal("Explorar Sneakers")
            Spacer(modifier = Modifier.height(12.dp))
            BuscadorConFiltros(nombreBuscado, { nombreBuscado = it }) { mostrarFiltros = true }
            Spacer(modifier = Modifier.height(12.dp))

        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            state = estadoLista
        ) {
            item(span = { GridItemSpan(2) }) {
                Column {
                    LazyRow {
                        items(model.listaMarcas) {
                            CardMarca(it)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            items(model.listaDeproductos) {
                CardProducto(it)
            }
            if (model.listaDeproductos.size < 53) {
                item(span = { GridItemSpan(2) }) {
                    Text(
                        "Buscando más zapatillas ...",
                        Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
            }
        }
        if (mostrarFiltros) {
            ModalBottomSheet(
                onDismissRequest = { mostrarFiltros = false },
                sheetState = estadoHoja,
                containerColor = Color(0xFF1E1E1E),
                dragHandle = {
                    BottomSheetDefaults.DragHandle(
                        color = Color.Gray
                    )
                }) {
                CardFiltros({ mostrarFiltros = false })
            }
        }
    }
}

@Composable
fun CardMarca(marca: Marca) {

    var seleccionada by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(end = 8.dp)
            .height(60.dp)
            .width(160.dp)
            .clickable(onClick = { seleccionada = !seleccionada })
            .testTag("cardMarca"),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        ),
        border = if (seleccionada) BorderStroke(width = 1.dp, color = Color.White) else null
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                marca.logoUrl,
                "",
                modifier = Modifier
                    .padding(8.dp)
                    .size(40.dp)
                    .align(Alignment.CenterStart),
                contentScale = ContentScale.Fit
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xFF1E1E1E).copy(alpha = 0.7f),
                                Color(0xFF1E1E1E)
                            ),
                        )
                    )
            )

            Text(
                text = marca.nombre,
                color = Color.White,
                fontWeight = Bold,
                fontSize = 15.sp,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.dp)
            )
        }
    }
}

@Composable
fun CardProducto(producto: Producto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(25.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color(0xFF252525)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = producto.imagenUrl, contentDescription = "",
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
                    fontSize = 18.sp,
                    fontWeight = Bold,
                    maxLines = 1
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = "Marca ${producto.idMarca}", color = Color.Gray, fontSize = 15.sp
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${producto.precio} €",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = Bold
                    )
                    Icon(
                        Icons.Default.FavoriteBorder, "", tint = Color.White
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
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            nombreBuscado,
            cambiarBuscador,
            placeholder = { Text("Buscar zapatilla, modelo ...") },
            modifier = Modifier
                .weight(1f)
                .testTag("TextFieldZapatillas"),
            trailingIcon = { Icon(Icons.Default.Search, "", tint = Color.White) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                focusedPlaceholderColor = Color.White,
                unfocusedPlaceholderColor = Color.LightGray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.LightGray
            )
        )
        Box(
            Modifier
                .height(50.dp)
                .width(50.dp)
                .clickable(onClick = mostrarFiltros)
                .border(
                    BorderStroke(width = 1.dp, color = Color.White),
                    shape = RoundedCornerShape(12.dp)
                )
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .testTag("botonFiltros"),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painterResource(R.drawable.baseline_filter_list_alt_24),
                "",
                modifier = Modifier.size(35.dp)
            )
        }
    }
}

@Composable
fun CardFiltros(cerrarFiltros: () -> Unit) {
    var rangoPrecio by remember { mutableStateOf(0f..500f) }
    var tallaSeleccionada by remember { mutableStateOf("42") }
    val tallas = listOf("35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46")
    val marcas = listOf("Nike", "Adidas", "Jordan", "New Balance", "Reebok")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp)
    ) {
        Text(
            text = "Filtros", style = TextStyle(
                fontSize = 25.sp, fontWeight = Bold, color = Color.White
            )
        )
        Spacer(Modifier.height(25.dp))

        Text("Rango de Precio", color = Color.Gray, fontSize = 14.sp)
        Spacer(Modifier.height(8.dp))
        Row(
            Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("${rangoPrecio.start.toInt()}€", color = Color.White)
            Text("${rangoPrecio.endInclusive.toInt()}€", color = Color.White)
        }
        RangeSlider(
            value = rangoPrecio,
            onValueChange = { rangoPrecio = it },
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

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val filasTallas = tallas.chunked(4)
            filasTallas.forEach { fila ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    fila.forEach { talla ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(45.dp)
                                .background(
                                    if (tallaSeleccionada == talla) Color.White else Color(
                                        0xFF1E1E1E
                                    ), RoundedCornerShape(12.dp)
                                )
                                .clickable { tallaSeleccionada = talla },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                talla,
                                color = if (tallaSeleccionada == talla) Color.Black else Color.White,
                                fontWeight = Bold
                            )
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(25.dp))

        Text("Marcas", color = Color.Gray, fontSize = 14.sp)
        Spacer(Modifier.height(12.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(marcas) { marca ->
                Box(
                    modifier = Modifier
                        .background(Color(0xFF1E1E1E), RoundedCornerShape(20.dp))
                        .border(1.dp, Color(0xFF333333), RoundedCornerShape(20.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(marca, color = Color.White, fontSize = 14.sp)
                }
            }
        }

        Spacer(Modifier.height(40.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .clickable { },
            shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .clickable(onClick = cerrarFiltros)
                    .testTag("botonAplicarFiltros"), contentAlignment = Alignment.Center
            ) {
                Text("Aplicar Filtros", color = Color.Black, fontWeight = Bold, fontSize = 16.sp)
            }
        }
        Spacer(Modifier.height(15.dp))
    }
}

@Composable
fun TituloPrincipal(texto: String) {
    Text(
        text = texto,
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

//@Preview(showBackground = true)
//@Composable
//fun Prev() {
//    PantallaPrincipal()
//}