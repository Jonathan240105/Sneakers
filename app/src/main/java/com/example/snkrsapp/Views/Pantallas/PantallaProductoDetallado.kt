package com.example.snkrsapp.Views.Pantallas

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.snkrsapp.Domain.Marca
import com.example.snkrsapp.Domain.Producto
import com.example.snkrsapp.Domain.Publicacion
import com.example.snkrsapp.Views.ViewModels.ProductoDetalladoViewModel

@Composable
fun PantallaProductoDetallado(
    idZapatilla: Int,
    idMarca: Int,
    volverAPrincipal: () -> Unit,
    myViewModel: ProductoDetalladoViewModel
) {
    var pestañaSeleccionada by remember { mutableStateOf(0) }
    val model by myViewModel.model.collectAsState()
    val estadoScroll = rememberScrollState()

    LaunchedEffect(idZapatilla, idMarca) {
        myViewModel.cargarProducto(idZapatilla)
        myViewModel.cargarMarca(idMarca)
        myViewModel.cargarPublicacionesDelProducto(idZapatilla)
    }

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
                    ), contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = model.productoSeleccionado.imagenUrl,
                    contentDescription = model.productoSeleccionado.modelo,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp)
                        .testTag("imagenProducto"),
                    contentScale = ContentScale.Fit
                )

                Box(
                    Modifier
                        .align(Alignment.TopStart)
                        .padding(20.dp)
                        .size(45.dp)
                        .background(Color.Black, RoundedCornerShape(12.dp))
                        .clickable(onClick = volverAPrincipal), contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.ArrowBack, "", tint = Color.White)
                }
            }

            Column(Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = model.productoSeleccionado.modelo,
                    modifier = Modifier.testTag("modelo"),
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = Bold
                )
                val precioMostrar =
                    if (model.listaPublicaciones.isNotEmpty())
                        model.publicacionSeleccionada.precio
                    else
                        model.productoSeleccionado.precio
                Text(
                    text = "$precioMostrar €",
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
                    ContenidoDetalles(
                        listaPublicaciones = model.listaPublicaciones,
                        cargandoPublicaciones = model.cargandoPublicaciones,
                        navegarAPublicacion = {
                            myViewModel.seleccionarPublicacion(it)
                        },
                        talla = model.publicacionSeleccionada.talla,
                        idPublicacionSeleccionada = model.publicacionSeleccionada.idPublicacion
                    )
                } else {
                    ContenidoMarca(
                        model.marcaSeleccionada, model.productoSeleccionado.uidVendedor ?: ""
                    )
                }

                Spacer(modifier = Modifier.height(100.dp))
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
            .clickable { onClick() }
            .testTag("botonTab"), contentAlignment = Alignment.Center) {
        Text(
            text = texto, color = if (activo) Color.Black else Color.Gray, fontWeight = Bold
        )
    }
}

@Composable
fun ContenidoDetalles(
    listaPublicaciones: List<Publicacion>,
    cargandoPublicaciones: Boolean,
    navegarAPublicacion: (Publicacion) -> Unit,
    talla: Double?,
    idPublicacionSeleccionada: Int
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if(listaPublicaciones.isNotEmpty()){
            Row {
                InfoPequeña(
                    titulo = "Talla de publicación seleccionada:",
                    valor = "$talla"
                )
                Spacer(modifier = Modifier.width(20.dp))
            }
        }

        Text("Publicaciones Disponibles", color = Color.Gray, fontSize = 14.sp)

        if (cargandoPublicaciones) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        } else if (listaPublicaciones.isEmpty()) {
            Text(
                text = "No hay zapatillas en venta de este modelo actualmente.",
                color = Color.LightGray,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            )
        } else {
            listaPublicaciones.forEach { publicacion ->
                val esLaSeleccionada = publicacion.idPublicacion == idPublicacionSeleccionada

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navegarAPublicacion(publicacion)
                        },
                    shape = RoundedCornerShape(12.dp),
                    border = if (esLaSeleccionada) BorderStroke(1.5.dp, Color.White) else null,
                    colors = CardDefaults.cardColors(
                        containerColor = if (esLaSeleccionada) Color(0xFF262626) else Color(0xFF1E1E1E)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Talla: ${publicacion.talla}",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Vendedor ID: ${publicacion.uidUsuario.take(8)}...",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "Estado: ${publicacion.estado}",
                                color = Color.LightGray,
                                fontSize = 12.sp
                            )
                        }

                        Text(
                            text = "${publicacion.precio} €",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = Bold
                        )
                    }
                }
            }
        }
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
                Text(marca.nombre ?: "", color = Color.White, fontWeight = Bold, fontSize = 18.sp)
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