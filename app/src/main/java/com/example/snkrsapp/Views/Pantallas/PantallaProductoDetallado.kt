package com.example.snkrsapp.Views.Pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.snkrsapp.Domain.Marca
import com.example.snkrsapp.Domain.Producto
import com.example.snkrsapp.Views.ViewModels.ProductoDetalladoViewModel

@Composable
fun PantallaProductoDetallado(
    idZapatilla: Int,
    idMarca: Int,
    volverAPrincipal: () -> Unit,
    myViewModel: ProductoDetalladoViewModel
) {
    var pestañaSeleccionada by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        myViewModel.cargarProducto(idZapatilla)
        myViewModel.cargarMarca(idMarca)
    }
    val model by myViewModel.model.collectAsState()
    val estadoScroll = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(estadoScroll)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF252525), Color(0xFF121212))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = "https://i.ebayimg.com/images/g/GOoAAeSw7H5o9rmS/s-l1200.webp",
                    contentDescription = model.productoSeleccionado.modelo,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp),
                    contentScale = ContentScale.Fit
                )

                Box(
                    Modifier
                        .align(Alignment.TopStart)
                        .padding(20.dp)
                        .size(45.dp)
                        .background(Color.Black, RoundedCornerShape(12.dp))
                        .clickable(onClick = volverAPrincipal),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.ArrowBack, "", tint = Color.White)
                }
            }

            Column(Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = model.productoSeleccionado.modelo,
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = Bold
                )
                Text(
                    text = "${model.productoSeleccionado.precio} €",
                    color = Color.LightGray,
                    fontSize = 22.sp,
                    fontWeight = Bold
                )

                Spacer(modifier = Modifier.height(25.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1E1E1E), RoundedCornerShape(15.dp))
                        .padding(5.dp)
                ) {
                    BotonTab(
                        texto = "Detalles",
                        activo = pestañaSeleccionada == 0,
                        modifier = Modifier.weight(1f)
                    ) { pestañaSeleccionada = 0 }

                    BotonTab(
                        texto = "Información",
                        activo = pestañaSeleccionada == 1,
                        modifier = Modifier.weight(1f)
                    ) { pestañaSeleccionada = 1 }
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (pestañaSeleccionada == 0) {
                    ContenidoDetalles(model.productoSeleccionado)
                } else {
                    ContenidoMarca(model.marcaSeleccionada, model.productoSeleccionado.uidVendedor)
                }
            }


        }
        Box(
            modifier = Modifier
                .size(75.dp)
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 20.dp)
                .background(Color(0xFF1E1E1E), RoundedCornerShape(25.dp))
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.FavoriteBorder,
                "",
                tint = Color.White,
                modifier = Modifier.size(25.dp)
            )
        }
    }
}

@Composable
fun BotonTab(texto: String, activo: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .height(45.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (activo) Color.White else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            color = if (activo) Color.Black else Color.Gray,
            fontWeight = Bold
        )
    }
}

@Composable
fun ContenidoDetalles(producto: Producto) {
    Column {
        Row {
            InfoPequeña(titulo = "Talla", valor = "${producto.talla}")
            Spacer(modifier = Modifier.width(20.dp))
            InfoPequeña(titulo = "Estado", valor = "Nuevo")
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text("Descripción", color = Color.Gray, fontSize = 14.sp)
        Text(
            "" +
                    "Esta zapatilla de alta gama ofrece una combinación única de materiales premium y diseño icónico. Perfecta para el uso diario o coleccionistas." +
                    "Esta zapatilla de alta gama ofrece una combinación única de materiales premium y diseño icónico. Perfecta para el uso diario o coleccionistas." +
                    "Esta zapatilla de alta gama ofrece una combinación única de materiales premium y diseño icónico. Perfecta para el uso diario o coleccionistas." +
                    "Esta zapatilla de alta gama ofrece una combinación única de materiales premium y diseño icónico. Perfecta para el uso diario o coleccionistas." +
                    "Esta zapatilla de alta gama ofrece una combinación única de materiales premium y diseño icónico. Perfecta para el uso diario o coleccionistas." +
                    "Esta zapatilla de alta gama ofrece una combinación única de materiales premium y diseño icónico. Perfecta para el uso diario o coleccionistas." +
                    "Esta zapatilla de alta gama ofrece una combinación única de materiales premium y diseño icónico. Perfecta para el uso diario o coleccionistas." +
                    "",
            color = Color.White
        )

    }
}

@Composable
fun ContenidoMarca(marca: Marca, uidVendedor: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E), RoundedCornerShape(20.dp))
            .padding(15.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = marca.logoUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Fit
            )
            Spacer(Modifier.width(15.dp))
            Column {
                Text(marca.nombre, color = Color.White, fontWeight = Bold, fontSize = 18.sp)
                Text("Fundada en 1990", color = Color.Gray, fontSize = 14.sp)
            }
        }
        Spacer(Modifier.height(15.dp))
        Text("País de origen: España", color = Color.White)
        Text("Vendedor ID: $uidVendedor", color = Color.LightGray, fontSize = 12.sp)
    }
}

@Composable
fun InfoPequeña(titulo: String, valor: String) {
    Column {
        Text(titulo, color = Color.Gray, fontSize = 12.sp)
        Text(valor, color = Color.White, fontWeight = Bold, fontSize = 16.sp)
    }
}