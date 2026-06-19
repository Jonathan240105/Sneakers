package com.example.snkrsapp.Views.Pantallas

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.snkrsapp.Domain.Marca
import com.example.snkrsapp.Domain.Publicacion
import com.example.snkrsapp.Views.ViewModels.ProductoDetalladoViewModel
import com.example.snkrsapp.ui.theme.ColorAcento
import com.example.snkrsapp.ui.theme.ColorNeutroFondo
import com.example.snkrsapp.ui.theme.ColorPrimario
import com.example.snkrsapp.ui.theme.ColorTextoSecundario
import com.example.snkrsapp.ui.theme.miTipografia

@Composable
fun PantallaProductoDetallado(
    idZapatilla: Int,
    idMarca: Int,
    volverAPrincipal: () -> Unit,
    myViewModel: ProductoDetalladoViewModel,
    paddingValues: PaddingValues,
    navegarAPerfil: (String) -> Unit,
    contactarVendedor: (String, Int?, String, String) -> Unit
) {
    var pestañaSeleccionada by remember { mutableStateOf(0) }
    val model by myViewModel.model.collectAsState()
    val estadoScroll = rememberScrollState()
    val contexto = LocalContext.current

    LaunchedEffect(idZapatilla, idMarca) {
        myViewModel.cargarProducto(idZapatilla)
        myViewModel.cargarMarca(idMarca)
        myViewModel.cargarPublicacionesDelProducto(idZapatilla)
    }
    LaunchedEffect(model.mensaje) {
        if (!model.mensaje.isNullOrBlank()) {
            Toast.makeText(contexto, model.mensaje, Toast.LENGTH_SHORT).show()
            myViewModel.limpiarMensaje()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorNeutroFondo)
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(estadoScroll)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .padding(bottom = 20.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                ColorPrimario, // Un gris oscuro más suave arriba
                                ColorNeutroFondo   // Se difumina perfectamente con el fondo de abajo
                            )
                        )
                    ), contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = model.publicacionSeleccionada.urlFoto,
                    "",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp)
                        .padding(top = 40.dp)
                        .testTag("imagenProducto"),
                    contentScale = ContentScale.Fit
                )
            }

            Column(Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = model.productoSeleccionado.modelo,
                    modifier = Modifier.testTag("modelo"),
                    color = ColorPrimario,
                    fontFamily = miTipografia,
                    fontSize = 28.sp,
                    fontWeight = Bold
                )
                Spacer(Modifier.height(15.dp))
                val precioMostrar =
                    if (model.listaPublicaciones.isNotEmpty()) model.publicacionSeleccionada.precio
                    else model.productoSeleccionado.precio
                Text(
                    text = "$precioMostrar €",
                    color = ColorTextoSecundario,
                    fontFamily = miTipografia,
                    fontSize = 22.sp,
                    fontWeight = Bold
                )

                Spacer(modifier = Modifier.height(25.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ColorPrimario, RoundedCornerShape(15.dp))
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
                    ContenidoInformacionCompleta(
                        marca = model.marcaSeleccionada,
                        nombreVendedor = model.publicacionSeleccionada.nombreUsuario
                            ?: "Usuario SNKRS",
                        onVerPerfilClick = { navegarAPerfil(model.publicacionSeleccionada.uidUsuario) },
                        onContactarClick = {
                            contactarVendedor(
                                model.publicacionSeleccionada.uidUsuario,
                                model.productoSeleccionado.idProducto,
                                model.publicacionSeleccionada.nombreUsuario ?: "Usuario",
                                model.productoSeleccionado.modelo
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 10.dp)
                .statusBarsPadding(), horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = volverAPrincipal,
                modifier = Modifier
                    .background(Color(0xFF1E1E1E), CircleShape)
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = ColorTextoSecundario
                )
            }
        }

        Box(
            modifier = Modifier
                .size(75.dp)
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 20.dp)
                .background(
                    if (model.publicacionSeleccionada.estado == "disponible") ColorPrimario else ColorPrimario.copy(
                        0.8f
                    ),
                    RoundedCornerShape(10.dp)
                )
                .clickable {
                    if (model.publicacionSeleccionada.estado == "disponible") {
                        myViewModel.agregarACarrito()
                    } else {
                        Toast.makeText(
                            contexto,
                            "Este producto ya está vendido",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.ShoppingCart, "", tint = Color.White, modifier = Modifier.size(25.dp)
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
            text = texto,
            fontFamily = miTipografia,
            color = if (activo) ColorTextoSecundario else Color.White,
            fontWeight = Bold
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
        if (listaPublicaciones.isNotEmpty()) {
            Row {
                InfoPequeña(
                    titulo = "Talla de publicación seleccionada:", valor = "$talla"
                )
                Spacer(modifier = Modifier.width(20.dp))
            }
        }

        Text(
            "Publicaciones Disponibles",
            color = ColorTextoSecundario,
            fontSize = 16.sp,
            fontFamily = miTipografia,
            fontWeight = Bold
        )

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
                fontFamily = miTipografia,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            )
        } else {
            listaPublicaciones.forEach { publicacion ->
                val esLaSeleccionada = publicacion.idPublicacion == idPublicacionSeleccionada

                Card(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            navegarAPublicacion(publicacion)
                        },
                    shape = RoundedCornerShape(12.dp),
                    border = if (esLaSeleccionada) BorderStroke(1.5.dp, Color.White) else null,
                    colors = CardDefaults.cardColors(
                        containerColor = if (esLaSeleccionada) Color.LightGray.copy(0.9f) else Color.White
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
                                "Talla: ${publicacion.talla}",
                                color = ColorPrimario,
                                fontFamily = miTipografia,
                                fontSize = 18.sp,
                                fontWeight = Bold
                            )
                            Text(
                                "Vendedor : ${publicacion.nombreUsuario}",
                                color = ColorTextoSecundario,
                                fontSize = 15.sp,
                                fontFamily = miTipografia,
                                fontWeight = Bold
                            )
                            Text(
                                "Estado:  ${publicacion.estado}",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                fontFamily = miTipografia
                            )
                        }

                        Text(
                            text = "${publicacion.precio} €",
                            color = ColorTextoSecundario,
                            fontSize = 18.sp,
                            fontFamily = miTipografia,
                            fontWeight = Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ContenidoInformacionCompleta(
    marca: Marca,
    nombreVendedor: String,
    onVerPerfilClick: () -> Unit,
    onContactarClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            "Información de la Marca",
            fontFamily = miTipografia, color = ColorPrimario, fontSize = 16.sp
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier.padding(15.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        marca.logoUrl,
                        "",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(Modifier.width(15.dp))
                    Column {
                        Text(
                            marca.nombre ?: "",
                            color = ColorTextoSecundario,
                            fontFamily = miTipografia,
                            fontWeight = Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            "Fundada en 1990",
                            fontFamily = miTipografia,
                            color = ColorTextoSecundario,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Text(
            "Información del Vendedor",
            fontFamily = miTipografia, color = ColorPrimario, fontSize = 16.sp, fontWeight = Bold
        )
        Card(
            Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF252525)), contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            "",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(Modifier.width(15.dp))


                    Text(
                        text = nombreVendedor,
                        color = ColorPrimario,
                        fontFamily = miTipografia,
                        fontWeight = Bold,
                        fontSize = 17.sp
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Contactar",
                        color = ColorAcento,
                        fontSize = 14.sp,
                        fontFamily = miTipografia,
                        fontWeight = Bold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .clickable { onContactarClick() }
                            .padding(8.dp)
                    )
                    Text(
                        text = "Ver perfil",
                        color = Color.DarkGray,
                        fontSize = 14.sp,
                        fontFamily = miTipografia,
                        fontWeight = Bold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .clickable { onVerPerfilClick() }
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun InfoPequeña(titulo: String, valor: String) {
    Column {
        Text(
            titulo,
            fontFamily = miTipografia, color = ColorPrimario, fontSize = 16.sp, fontWeight = Bold
        )
        Text(
            valor,
            fontFamily = miTipografia,
            color = ColorTextoSecundario,
            fontWeight = Bold,
            fontSize = 15.sp
        )
    }
}
