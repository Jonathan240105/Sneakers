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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.derivedStateOf
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
import com.example.snkrsapp.Domain.VariantePublicacion
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
    var pestanaSeleccionada by remember { mutableStateOf(0) }
    val model by myViewModel.model.collectAsState()
    val estadoScroll = rememberScrollState()
    val contexto = LocalContext.current
    var mostrarDialogoCarrito by remember { mutableStateOf(false) }
    var cantidadesSeleccionadas by remember { mutableStateOf<Map<Int, Int>>(emptyMap()) }
    var idColorImagenSeleccionada by remember { mutableStateOf<Int?>(null) }
    val variantesDisponibles by remember(model.publicacionSeleccionada.variantes) {
        derivedStateOf {
            model.publicacionSeleccionada.variantes.filter { it.cantidadDisponible > 0 }
        }
    }
    val varianteImagenSeleccionada by remember(variantesDisponibles, idColorImagenSeleccionada) {
        derivedStateOf {
            variantesDisponibles.firstOrNull { it.idColor == idColorImagenSeleccionada }
                ?: variantesDisponibles.firstOrNull()
        }
    }

    LaunchedEffect(idZapatilla, idMarca) {
        myViewModel.cargarProducto(idZapatilla)
        myViewModel.cargarMarca(idMarca)
        myViewModel.cargarPublicacionesDelProducto(idZapatilla)
    }
    LaunchedEffect(model.publicacionSeleccionada.idPublicacion) {
        idColorImagenSeleccionada =
            model.publicacionSeleccionada.variantes.firstOrNull { it.cantidadDisponible > 0 }?.idColor
    }
    LaunchedEffect(model.mensaje) {
        if (!model.mensaje.isNullOrBlank()) {
            Toast.makeText(contexto, model.mensaje, Toast.LENGTH_SHORT).show()
            myViewModel.limpiarMensaje()
        }
    }
    LaunchedEffect(mostrarDialogoCarrito) {
        if (mostrarDialogoCarrito) {
            cantidadesSeleccionadas = emptyMap()
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
                                ColorPrimario,
                                ColorNeutroFondo
                            )
                        )
                    ), contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = varianteImagenSeleccionada?.urlFoto ?: model.publicacionSeleccionada.urlFoto,
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
                        activo = pestanaSeleccionada == 0,
                        modifier = Modifier.weight(1f)
                    ) { pestanaSeleccionada = 0 }

                    BotonTab(
                        texto = "Información",
                        activo = pestanaSeleccionada == 1,
                        modifier = Modifier.weight(1f)
                    ) { pestanaSeleccionada = 1 }
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (pestanaSeleccionada == 0) {
                    ContenidoDetalles(
                        listaPublicaciones = model.listaPublicaciones,
                        cargandoPublicaciones = model.cargandoPublicaciones,
                        navegarAPublicacion = {
                            myViewModel.seleccionarPublicacion(it)
                        },
                        talla = model.publicacionSeleccionada.talla,
                        idPublicacionSeleccionada = model.publicacionSeleccionada.idPublicacion,
                        variantesSeleccionadas = model.publicacionSeleccionada.variantes,
                        idColorSeleccionado = varianteImagenSeleccionada?.idColor,
                        onColorSeleccionado = { idColorImagenSeleccionada = it }
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
                        mostrarDialogoCarrito = true
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

        if (mostrarDialogoCarrito) {
            DialogoAgregarCarrito(
                variantes = model.publicacionSeleccionada.variantes,
                cantidadesSeleccionadas = cantidadesSeleccionadas,
                onCantidadMenos = { variante ->
                    val cantidadActual = cantidadesSeleccionadas[variante.idVariante] ?: 0
                    val nuevaCantidad = (cantidadActual - 1).coerceAtLeast(0)

                    cantidadesSeleccionadas = if (nuevaCantidad == 0) {
                        cantidadesSeleccionadas - variante.idVariante
                    } else {
                        cantidadesSeleccionadas + (variante.idVariante to nuevaCantidad)
                    }
                },
                onCantidadMas = { variante ->
                    val cantidadActual = cantidadesSeleccionadas[variante.idVariante] ?: 0
                    val nuevaCantidad =
                        (cantidadActual + 1).coerceAtMost(variante.cantidadDisponible.coerceAtLeast(0))

                    cantidadesSeleccionadas =
                        cantidadesSeleccionadas + (variante.idVariante to nuevaCantidad)
                },
                onDismiss = { mostrarDialogoCarrito = false },
                onConfirmar = {
                    val variantesParaAgregar = cantidadesSeleccionadas
                        .filterValues { it > 0 }
                        .map { it.key to it.value }

                    if (variantesParaAgregar.isEmpty()) {
                        Toast.makeText(contexto, "Selecciona al menos una variante", Toast.LENGTH_SHORT).show()
                    } else {
                        myViewModel.agregarVariantesACarrito(variantesParaAgregar)
                        mostrarDialogoCarrito = false
                    }
                }
            )
        }
    }
}

@Composable
fun DialogoAgregarCarrito(
    variantes: List<VariantePublicacion>,
    cantidadesSeleccionadas: Map<Int, Int>,
    onCantidadMenos: (VariantePublicacion) -> Unit,
    onCantidadMas: (VariantePublicacion) -> Unit,
    onDismiss: () -> Unit,
    onConfirmar: () -> Unit
) {
    val totalSeleccionado = cantidadesSeleccionadas.values.sum()

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ColorNeutroFondo,
        title = {
            Text(
                "Añadir al carrito",
                fontFamily = miTipografia,
                color = ColorPrimario,
                fontWeight = Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    "Elige cantidades",
                    fontFamily = miTipografia,
                    color = ColorTextoSecundario,
                    fontWeight = Bold
                )
                if (variantes.isEmpty()) {
                    Text(
                        "No hay variantes disponibles para esta publicación.",
                        fontFamily = miTipografia,
                        color = ColorTextoSecundario
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        variantes.forEach { variante ->
                            val cantidadVariante = cantidadesSeleccionadas[variante.idVariante] ?: 0
                            val seleccionado = cantidadVariante > 0

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (seleccionado) ColorPrimario else Color.White)
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Text(
                                        "${variante.color} · Talla ${variante.talla}",
                                        fontFamily = miTipografia,
                                        color = if (seleccionado) Color.White else ColorTextoSecundario,
                                        fontWeight = Bold
                                    )
                                    Text(
                                        "Stock: ${variante.cantidadDisponible}",
                                        fontFamily = miTipografia,
                                        color = if (seleccionado) Color.White.copy(alpha = 0.85f) else Color.Gray,
                                        fontSize = 12.sp
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { onCantidadMenos(variante) },
                                        enabled = cantidadVariante > 0,
                                        shape = RoundedCornerShape(10.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
                                    ) {
                                        Text("-", fontSize = 18.sp, fontWeight = Bold)
                                    }
                                    Text(
                                        cantidadVariante.toString(),
                                        fontFamily = miTipografia,
                                        color = if (seleccionado) Color.White else ColorPrimario,
                                        fontSize = 18.sp,
                                        fontWeight = Bold
                                    )
                                    OutlinedButton(
                                        onClick = { onCantidadMas(variante) },
                                        enabled = cantidadVariante < variante.cantidadDisponible,
                                        shape = RoundedCornerShape(10.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
                                    ) {
                                        Text("+", fontSize = 18.sp, fontWeight = Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                Text(
                    "Total seleccionado: $totalSeleccionado",
                    fontFamily = miTipografia,
                    color = ColorTextoSecundario,
                    fontWeight = Bold
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirmar,
                enabled = totalSeleccionado > 0,
                colors = ButtonDefaults.buttonColors(containerColor = ColorPrimario),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Añadir", fontFamily = miTipografia, color = Color.White, fontWeight = Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(12.dp)) {
                Text("Cancelar", fontFamily = miTipografia, color = ColorTextoSecundario)
            }
        }
    )
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
    idPublicacionSeleccionada: Int,
    variantesSeleccionadas: List<VariantePublicacion>,
    idColorSeleccionado: Int?,
    onColorSeleccionado: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (listaPublicaciones.isNotEmpty()) {
            Row {
                InfoPequena(
                    titulo = "Talla de publicación seleccionada:", valor = "$talla"
                )
                Spacer(modifier = Modifier.width(20.dp))
            }
        }

        ResumenVariantesPublicacion(
            variantes = variantesSeleccionadas,
            idColorSeleccionado = idColorSeleccionado,
            onColorSeleccionado = onColorSeleccionado
        )

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
fun ResumenVariantesPublicacion(
    variantes: List<VariantePublicacion>,
    idColorSeleccionado: Int?,
    onColorSeleccionado: (Int) -> Unit
) {
    if (variantes.isEmpty()) return

    val tallasDisponibles = variantes
        .filter { it.cantidadDisponible > 0 }
        .map { it.talla }
        .distinct()
        .sorted()

    val coloresDisponibles = variantes
        .filter { it.cantidadDisponible > 0 }
        .distinctBy { it.idColor }

    val stockTotal = variantes.sumOf { it.cantidadDisponible.coerceAtLeast(0) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Disponibilidad de esta publicación",
            color = ColorTextoSecundario,
            fontSize = 16.sp,
            fontFamily = miTipografia,
            fontWeight = Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Stock total: $stockTotal",
                    color = ColorPrimario,
                    fontFamily = miTipografia,
                    fontWeight = Bold,
                    fontSize = 15.sp
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Tallas disponibles",
                        color = ColorTextoSecundario,
                        fontFamily = miTipografia,
                        fontWeight = Bold,
                        fontSize = 14.sp
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(tallasDisponibles) { talla ->
                            Box(
                                modifier = Modifier
                                    .background(ColorPrimario, RoundedCornerShape(9.dp))
                                    .padding(horizontal = 12.dp, vertical = 7.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = talla.toString(),
                                    color = Color.White,
                                    fontFamily = miTipografia,
                                    fontWeight = Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Colores disponibles",
                        color = ColorTextoSecundario,
                        fontFamily = miTipografia,
                        fontWeight = Bold,
                        fontSize = 14.sp
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(coloresDisponibles) { variante ->
                            val seleccionado = variante.idColor == idColorSeleccionado
                            val colorVisual = colorDesdeHex(variante.hexColor)

                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (seleccionado) ColorPrimario else ColorNeutroFondo)
                                    .clickable { onColorSeleccionado(variante.idColor) }
                                    .padding(5.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(9.dp))
                                        .background(colorVisual)
                                        .then(
                                            if (seleccionado) {
                                                Modifier
                                            } else {
                                                Modifier
                                            }
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun colorDesdeHex(hex: String?): Color =
    runCatching {
        Color(android.graphics.Color.parseColor(hex ?: "#BDBDBD"))
    }.getOrDefault(Color(0xFFBDBDBD))

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
fun InfoPequena(titulo: String, valor: String) {
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


