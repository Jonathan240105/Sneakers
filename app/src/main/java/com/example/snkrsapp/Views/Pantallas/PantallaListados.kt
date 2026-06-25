package com.example.snkrsapp.Views.Pantallas

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import androidx.core.content.FileProvider
import com.example.snkrsapp.Domain.Incidencia
import com.example.snkrsapp.Domain.PedidoRecibido
import com.example.snkrsapp.Domain.PublicacionPerfilItem
import com.example.snkrsapp.Views.ViewModels.ListadoViewModel
import com.example.snkrsapp.ui.theme.ColorBordeTextField
import com.example.snkrsapp.ui.theme.ColorNeutroFondo
import com.example.snkrsapp.ui.theme.ColorPrimario
import com.example.snkrsapp.ui.theme.ColorTextFieldSeleccionado
import com.example.snkrsapp.ui.theme.ColorTextoSecundario
import com.example.snkrsapp.ui.theme.miTipografia
import java.io.File

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
    var mostrandoPedidosComprador by remember { mutableStateOf(false) }
    var pestanaPedidosSeleccionada by remember { mutableIntStateOf(0) }
    var pedidosIncidencia by remember { mutableStateOf<List<PedidoRecibido>?>(null) }
    LaunchedEffect(model.listaCarrito) {
        if (model.listaCarrito.isEmpty() && model.cargandoPago) {
            Toast.makeText(contexto, "¡Compra realizada con Éxito!", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(model.mensajeExito) {
        if (!model.mensajeExito.isNullOrBlank()) {
            Toast.makeText(contexto, model.mensajeExito, Toast.LENGTH_SHORT).show()
            myViewModel.limpiarMensajeExito()
        }
    }
    var pestanaSeleccionada by remember { mutableIntStateOf(id) }

    val listadoPestanas = if (esMiPerfil) {
        listOf("Colección", "Ventas", "Carrito")
    } else {
        listOf("Colección", "Ventas")
    }
    val indicePestanaActual = pestanaSeleccionada.coerceIn(0, listadoPestanas.lastIndex)
    val pestanaActual = listadoPestanas[indicePestanaActual]

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
                        texto = if (mostrandoPedidosComprador) "Mis pedidos" else if (esMiPerfil) "Mis Artículos" else "Artículos",
                        mostrarBotonVolver = !esMiPerfil || mostrandoPedidosComprador,
                        onVolverClick = {
                            if (mostrandoPedidosComprador) mostrandoPedidosComprador = false else navegarADetalle()
                        },
                        mostrarBotonPedidos = esMiPerfil && !mostrandoPedidosComprador,
                        onPedidosClick = { mostrandoPedidosComprador = true }
                    )

                    if (!mostrandoPedidosComprador) {
                        TabRow(
                            selectedTabIndex = indicePestanaActual,
                            containerColor = ColorNeutroFondo,
                            contentColor = Color.White,
                            indicator = { posicionesTab ->
                                TabRowDefaults.SecondaryIndicator(
                                    Modifier.tabIndicatorOffset(posicionesTab[indicePestanaActual]),
                                    color = Color.White
                                )
                            },
                            divider = { HorizontalDivider(color = ColorTextoSecundario) }
                        ) {
                            listadoPestanas.forEachIndexed { indice, titulo ->
                                Tab(
                                    indicePestanaActual == indice,
                                    { pestanaSeleccionada = indice },
                                    text = {
                                        Text(
                                            titulo,
                                            fontFamily = miTipografia,
                                            color = ColorTextoSecundario,
                                            fontSize = 18.sp,
                                            fontWeight = if (indicePestanaActual == indice) Bold else null
                                        )
                                    }
                                )
                            }
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
                    bottom = paddingValues.calculateBottomPadding() + 16.dp + (if (esMiPerfil && pestanaActual == "Carrito" && model.listaCarrito.isNotEmpty()) 100.dp else 0.dp),
                    start = 16.dp,
                    end = 16.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                if (mostrandoPedidosComprador) {
                    item(span = { GridItemSpan(2) }) {
                        TabRow(
                            selectedTabIndex = pestanaPedidosSeleccionada,
                            containerColor = ColorNeutroFondo,
                            contentColor = Color.White,
                            indicator = { posicionesTab ->
                                TabRowDefaults.SecondaryIndicator(
                                    Modifier.tabIndicatorOffset(posicionesTab[pestanaPedidosSeleccionada]),
                                    color = Color.White
                                )
                            },
                            divider = { HorizontalDivider(color = ColorTextoSecundario) }
                        ) {
                            listOf("Hechos por mí", "Pedidos a mí", "Incidencias").forEachIndexed { indice, titulo ->
                                Tab(
                                    selected = pestanaPedidosSeleccionada == indice,
                                    onClick = { pestanaPedidosSeleccionada = indice },
                                    text = {
                                        Text(
                                            titulo,
                                            fontFamily = miTipografia,
                                            color = ColorTextoSecundario,
                                            fontSize = 16.sp,
                                            fontWeight = if (pestanaPedidosSeleccionada == indice) Bold else null
                                        )
                                    }
                                )
                            }
                        }
                    }

                    if (pestanaPedidosSeleccionada == 0) {
                        val pedidosAgrupados = model.listaPedidosComprador
                            .groupBy { "${it.idPublicacion}_${it.estado}" }
                            .values
                            .toList()

                        if (pedidosAgrupados.isEmpty()) {
                            item(span = { GridItemSpan(2) }) {
                                MensajeListadoVacio("No tienes pedidos en seguimiento.")
                            }
                        } else {
                            items(
                                items = pedidosAgrupados,
                                span = { GridItemSpan(2) }
                            ) { pedidos ->
                                CardPedidoCompradorAgrupado(
                                    pedidos = pedidos,
                                    cargando = model.idPedidoRespondiendo == pedidos.first().idPedido,
                                    onConfirmarClick = { myViewModel.confirmarPedidosRecibidos(pedidos) },
                                    onIncidenciaClick = { pedidosIncidencia = pedidos }
                                )
                            }
                        }
                    } else if (pestanaPedidosSeleccionada == 1) {
                        val pedidosVendedorAgrupados = model.listaPedidosPendientes
                            .groupBy { "${it.uidComprador}_${it.idPublicacion}" }
                            .values
                            .toList()

                        if (model.cargandoPedidos) {
                            item(span = { GridItemSpan(2) }) {
                                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(color = ColorPrimario)
                                }
                            }
                        } else if (pedidosVendedorAgrupados.isEmpty()) {
                            item(span = { GridItemSpan(2) }) {
                                MensajeListadoVacio("No tienes pedidos pendientes.")
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
                            items(
                                items = pedidosVendedorAgrupados,
                                span = { GridItemSpan(2) }
                            ) { pedidos ->
                                CardPedidoVendedorAgrupado(
                                    pedidos = pedidos,
                                    respondiendo = model.idPedidoRespondiendo == pedidos.first().idPedido,
                                    onAceptarClick = { myViewModel.responderPedidos(pedidos, true) },
                                    onRechazarClick = { myViewModel.responderPedidos(pedidos, false) }
                                )
                            }
                        }
                    } else {
                        if (model.cargandoIncidencias) {
                            item(span = { GridItemSpan(2) }) {
                                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(color = ColorPrimario)
                                }
                            }
                        } else if (model.listaIncidenciasUsuario.isEmpty()) {
                            item(span = { GridItemSpan(2) }) {
                                MensajeListadoVacio("No tienes incidencias resueltas.")
                            }
                        } else {
                            items(
                                items = model.listaIncidenciasUsuario,
                                span = { GridItemSpan(2) }
                            ) { incidencia ->
                                CardIncidenciaUsuario(incidencia)
                            }
                        }
                    }
                } else when (pestanaActual) {
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
                            val carritoAgrupado = model.listaCarrito.groupBy { it.idPublicacion }.values.toList()

                            items(
                                items = carritoAgrupado,
                                span = { GridItemSpan(2) }
                            ) { articulosPublicacion ->
                                val articuloPrincipal = articulosPublicacion.first()
                                CardCarritoAgrupado(
                                    articulos = articulosPublicacion,
                                    pidiendo = model.idPublicacionPidiendo == articuloPrincipal.idPublicacion,
                                    onPedirClick = { myViewModel.pedirArticulo(articuloPrincipal) }
                                )
                            }
                        }
                    }
                }
            }
        }

    }

    pedidosIncidencia?.let { pedidos ->
        DialogReportarIncidencia(
            onDismiss = { pedidosIncidencia = null },
            onEnviar = { descripcion, imagen ->
                myViewModel.reportarPedidos(
                    pedidos = pedidos,
                    tipo = "otro",
                    descripcion = descripcion,
                    imagen = imagen
                )
                pedidosIncidencia = null
            }
        )
    }
}

@Composable
fun DialogReportarIncidencia(
    onDismiss: () -> Unit,
    onEnviar: (String, Uri?) -> Unit
) {
    val context = LocalContext.current
    var descripcion by remember { mutableStateOf("") }
    var imagen by remember { mutableStateOf<Uri?>(null) }
    var mostrarOpcionesImagen by remember { mutableStateOf(false) }
    var uriCamaraTemporal by remember { mutableStateOf<Uri?>(null) }

    val launcherGaleria = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        imagen = uri
    }

    val launcherCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito ->
        if (exito) {
            uriCamaraTemporal?.let { imagen = it }
        }
    }

    val launcherPermisoCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            val archivo = File.createTempFile("snkrs_incidencia_", ".jpg", context.cacheDir)
            val uriSegura = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                archivo
            )

            uriCamaraTemporal = uriSegura
            launcherCamara.launch(uriSegura)
        }
    }

    val formularioCompleto = descripcion.isNotBlank() && imagen != null

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ColorNeutroFondo),
            border = BorderStroke(1.dp, ColorBordeTextField)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Crear incidencia",
                    color = ColorPrimario,
                    fontFamily = miTipografia,
                    fontSize = 20.sp,
                    fontWeight = Bold
                )

                Text(
                    text = "Descripción",
                    color = ColorTextoSecundario,
                    fontFamily = miTipografia,
                    fontWeight = Bold,
                    fontSize = 15.sp
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    placeholder = { Text("Describe lo ocurrido") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = outlinedCustomColors()
                )

                Text(
                    text = "Imagen de la incidencia",
                    color = ColorTextoSecundario,
                    fontFamily = miTipografia,
                    fontWeight = Bold,
                    fontSize = 15.sp
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                        .background(Color(0xFF1E1E1E), RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFF333333), RoundedCornerShape(12.dp))
                        .clickable { mostrarOpcionesImagen = true },
                    contentAlignment = Alignment.Center
                ) {
                    if (imagen == null) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "+ Añadir Foto",
                                fontFamily = miTipografia,
                                color = Color.White,
                                fontWeight = Bold,
                                fontSize = 15.sp
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Cámara o Galería",
                                fontFamily = miTipografia,
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }
                    } else {
                        AsyncImage(
                            model = imagen,
                            contentDescription = "Imagen incidencia",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar", color = ColorTextoSecundario, fontFamily = miTipografia)
                    }

                    Button(
                        onClick = { onEnviar(descripcion, imagen) },
                        enabled = formularioCompleto,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorPrimario,
                            disabledContainerColor = ColorPrimario.copy(alpha = 0.45f)
                        )
                    ) {
                        Text(
                            "Crear incidencia",
                            color = Color.White,
                            fontFamily = miTipografia,
                            fontWeight = Bold
                        )
                    }
                }
            }
        }
    }

    if (mostrarOpcionesImagen) {
        AlertDialog(
            onDismissRequest = { mostrarOpcionesImagen = false },
            containerColor = Color(0xFF1E1E1E),
            title = { Text("Añadir imagen", color = Color.White, fontWeight = Bold) },
            text = {
                Text(
                    "Elige cómo quieres añadir la foto de la incidencia:",
                    color = Color.Gray,
                    fontFamily = miTipografia
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    mostrarOpcionesImagen = false
                    launcherPermisoCamara.launch(Manifest.permission.CAMERA)
                }) {
                    Text("Cámara", fontFamily = miTipografia, color = Color.White, fontWeight = Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    mostrarOpcionesImagen = false
                    launcherGaleria.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }) {
                    Text("Galería", fontFamily = miTipografia, color = Color.Gray)
                }
            }
        )
    }
}

@Composable
fun TituloListadosConVolver(
    texto: String,
    mostrarBotonVolver: Boolean,
    onVolverClick: () -> Unit,
    mostrarBotonPedidos: Boolean = false,
    onPedidosClick: () -> Unit = {}
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
            style = TextStyle(fontSize = 25.sp, fontWeight = Bold),
            modifier = Modifier.weight(1f)
        )
        if (mostrarBotonPedidos) {
            IconButton(
                onClick = onPedidosClick,
                modifier = Modifier
                    .background(ColorPrimario, CircleShape)
                    .size(40.dp)
            ) {
                Icon(
                    Icons.Default.Email,
                    contentDescription = "Mis pedidos",
                    tint = Color.White
                )
            }
        }
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
fun CardCarrito(
    articulo: PublicacionPerfilItem,
    pidiendo: Boolean,
    onPedirClick: () -> Unit
) {
    val estadoNormalizado = articulo.estadoPedido.lowercase()
    val textoEstado = when (estadoNormalizado) {
        "carrito" -> "En carrito"
        "disponible" -> "En carrito"
        "pendiente" -> "Pendiente"
        "enviado" -> "Enviado"
        "denegado" -> "Denegado"
        else -> articulo.estadoPedido.ifBlank { articulo.estado ?: "En carrito" }
    }
    val total = articulo.precio * articulo.cantidad
    val precioUnidadTexto = "${articulo.precio} €"
    val totalTexto = "$total €"

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, ColorBordeTextField)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .width(105.dp)
                    .height(105.dp)
                    .background(Color.Black, RoundedCornerShape(10.dp)),
                Alignment.Center
            ) {
                AsyncImage(
                    articulo.urlFoto,
                    articulo.modelo,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    articulo.modelo,
                    color = ColorPrimario,
                    fontFamily = miTipografia,
                    fontSize = 17.sp,
                    fontWeight = Bold,
                    maxLines = 1
                )
                Text(
                    "Color: ${articulo.nombreColor.ifBlank { "Sin color" }}",
                    fontFamily = miTipografia,
                    color = ColorTextoSecundario,
                    fontSize = 14.sp
                )
                Text(
                    "Cantidad: ${articulo.cantidad}",
                    fontFamily = miTipografia,
                    color = ColorTextoSecundario,
                    fontSize = 14.sp
                )
                Text(
                    "Precio unidad: $precioUnidadTexto",
                    fontFamily = miTipografia,
                    color = ColorTextoSecundario,
                    fontSize = 14.sp
                )
                Text(
                    "Total: $totalTexto",
                    fontFamily = miTipografia,
                    color = ColorPrimario,
                    fontSize = 15.sp,
                    fontWeight = Bold
                )
                Text(
                    "Estado: $textoEstado",
                    fontFamily = miTipografia,
                    color = if (estadoNormalizado == "denegado") Color(0xFFC0392B) else ColorPrimario,
                    fontSize = 14.sp,
                    fontWeight = Bold
                )

                if (estadoNormalizado == "carrito" || estadoNormalizado == "disponible") {
                    Button(
                        onClick = onPedirClick,
                        enabled = !pidiendo,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = ColorPrimario),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        if (pidiendo) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                "Pedir",
                                fontFamily = miTipografia,
                                color = Color.White,
                                fontWeight = Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CardCarritoAgrupado(
    articulos: List<PublicacionPerfilItem>,
    pidiendo: Boolean,
    onPedirClick: () -> Unit
) {
    if (articulos.isEmpty()) return

    val principal = articulos.first()
    val estadoNormalizado = principal.estadoPedido.lowercase()
    val textoEstado = when (estadoNormalizado) {
        "carrito" -> "En carrito"
        "disponible" -> "En carrito"
        "pendiente" -> "Pendiente"
        "enviado" -> "Enviado"
        "denegado" -> "Denegado"
        else -> principal.estadoPedido.ifBlank { principal.estado ?: "En carrito" }
    }
    val unidadesTotales = articulos.sumOf { it.cantidad }
    val total = articulos.sumOf { it.precio * it.cantidad }
    val totalTexto = "$total €"

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, ColorBordeTextField)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .width(105.dp)
                        .height(105.dp)
                        .background(Color.Black, RoundedCornerShape(10.dp)),
                    Alignment.Center
                ) {
                    AsyncImage(
                        principal.urlFoto,
                        principal.modelo,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        principal.modelo,
                        color = ColorPrimario,
                        fontFamily = miTipografia,
                        fontSize = 17.sp,
                        fontWeight = Bold,
                        maxLines = 1
                    )
                    Text(
                        "Variantes: ${articulos.size}",
                        fontFamily = miTipografia,
                        color = ColorTextoSecundario,
                        fontSize = 14.sp
                    )
                    Text(
                        "Unidades: $unidadesTotales",
                        fontFamily = miTipografia,
                        color = ColorTextoSecundario,
                        fontSize = 14.sp
                    )
                    Text(
                        "Total: $totalTexto",
                        fontFamily = miTipografia,
                        color = ColorPrimario,
                        fontSize = 15.sp,
                        fontWeight = Bold
                    )
                    Text(
                        "Estado: $textoEstado",
                        fontFamily = miTipografia,
                        color = if (estadoNormalizado == "denegado") Color(0xFFC0392B) else ColorPrimario,
                        fontSize = 14.sp,
                        fontWeight = Bold
                    )
                }
            }

            HorizontalDivider(color = ColorBordeTextField.copy(alpha = 0.5f))

            articulos.forEach { variante ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ColorNeutroFondo, RoundedCornerShape(10.dp))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(
                            "${variante.nombreColor.ifBlank { "Sin color" }} - Talla ${variante.talla}",
                            fontFamily = miTipografia,
                            color = ColorTextoSecundario,
                            fontSize = 14.sp,
                            fontWeight = Bold
                        )
                        Text(
                            "Cantidad: ${variante.cantidad}",
                            fontFamily = miTipografia,
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }
                    Text(
                        "${variante.precio * variante.cantidad} €",
                        fontFamily = miTipografia,
                        color = ColorPrimario,
                        fontSize = 14.sp,
                        fontWeight = Bold
                    )
                }
            }

            if (estadoNormalizado == "carrito" || estadoNormalizado == "disponible") {
                Button(
                    onClick = onPedirClick,
                    enabled = !pidiendo,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = ColorPrimario),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    if (pidiendo) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            "Pedir todo",
                            fontFamily = miTipografia,
                            color = Color.White,
                            fontWeight = Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CardPedidoVendedorAgrupado(
    pedidos: List<PedidoRecibido>,
    respondiendo: Boolean,
    onAceptarClick: () -> Unit,
    onRechazarClick: () -> Unit
) {
    if (pedidos.isEmpty()) return

    val principal = pedidos.first()
    val unidadesTotales = pedidos.sumOf { it.cantidad }
    val totalTexto = "${pedidos.sumOf { it.precioUnidad * it.cantidad }} €"

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, ColorBordeTextField)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .width(105.dp)
                        .height(105.dp)
                        .background(Color.Black, RoundedCornerShape(10.dp)),
                    Alignment.Center
                ) {
                    AsyncImage(principal.urlFoto, principal.modelo, modifier = Modifier.padding(12.dp))
                }

                Column(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(principal.modelo, color = ColorPrimario, fontFamily = miTipografia, fontSize = 17.sp, fontWeight = Bold, maxLines = 1)
                    Text("Comprador: ${principal.nombreComprador}", fontFamily = miTipografia, color = ColorTextoSecundario, fontSize = 14.sp)
                    Text("Variantes: ${pedidos.size}", fontFamily = miTipografia, color = ColorTextoSecundario, fontSize = 14.sp)
                    Text("Unidades: $unidadesTotales", fontFamily = miTipografia, color = ColorTextoSecundario, fontSize = 14.sp)
                    Text("Total: $totalTexto", fontFamily = miTipografia, color = ColorPrimario, fontSize = 15.sp, fontWeight = Bold)
                }
            }

            HorizontalDivider(color = ColorBordeTextField.copy(alpha = 0.5f))

            pedidos.forEach { pedido ->
                FilaVariantePedido(
                    color = pedido.color,
                    talla = pedido.talla,
                    cantidad = pedido.cantidad,
                    subtotal = pedido.precioUnidad * pedido.cantidad
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = onRechazarClick,
                    enabled = !respondiendo,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0392B)),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Text("Rechazar todo", fontFamily = miTipografia, color = Color.White, fontWeight = Bold, fontSize = 12.sp)
                }
                Button(
                    onClick = onAceptarClick,
                    enabled = !respondiendo,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = ColorPrimario),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    if (respondiendo) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                    } else {
                        Text("Enviar todo", fontFamily = miTipografia, color = Color.White, fontWeight = Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun CardPedidoCompradorAgrupado(
    pedidos: List<PedidoRecibido>,
    cargando: Boolean,
    onConfirmarClick: () -> Unit,
    onIncidenciaClick: () -> Unit
) {
    if (pedidos.isEmpty()) return

    val principal = pedidos.first()
    val estado = principal.estado.lowercase()
    val textoEstado = when (estado) {
        "pendiente" -> "Pendiente de respuesta"
        "enviado" -> "Enviado"
        "denegado" -> "Denegado"
        "recibido" -> "Recibido"
        "reportado" -> "Incidencia creada"
        else -> principal.estado
    }
    val unidadesTotales = pedidos.sumOf { it.cantidad }
    val totalTexto = "${pedidos.sumOf { it.precioUnidad * it.cantidad }} €"

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, ColorBordeTextField)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .width(105.dp)
                        .height(105.dp)
                        .background(Color.Black, RoundedCornerShape(10.dp)),
                    Alignment.Center
                ) {
                    AsyncImage(principal.urlFoto, principal.modelo, modifier = Modifier.padding(12.dp))
                }

                Column(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(principal.modelo, color = ColorPrimario, fontFamily = miTipografia, fontSize = 17.sp, fontWeight = Bold, maxLines = 1)
                    Text("Vendedor: ${principal.nombreVendedor.ifBlank { "Vendedor" }}", fontFamily = miTipografia, color = ColorTextoSecundario, fontSize = 14.sp)
                    Text("Variantes: ${pedidos.size}", fontFamily = miTipografia, color = ColorTextoSecundario, fontSize = 14.sp)
                    Text("Unidades: $unidadesTotales", fontFamily = miTipografia, color = ColorTextoSecundario, fontSize = 14.sp)
                    Text("Total: $totalTexto", fontFamily = miTipografia, color = ColorPrimario, fontSize = 15.sp, fontWeight = Bold)
                    Text("Estado: $textoEstado", fontFamily = miTipografia, color = ColorPrimario, fontSize = 14.sp, fontWeight = Bold)
                }
            }

            HorizontalDivider(color = ColorBordeTextField.copy(alpha = 0.5f))

            pedidos.forEach { pedido ->
                FilaVariantePedido(
                    color = pedido.color,
                    talla = pedido.talla,
                    cantidad = pedido.cantidad,
                    subtotal = pedido.precioUnidad * pedido.cantidad
                )
            }

            if (estado == "enviado") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = onIncidenciaClick,
                        enabled = !cargando,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0392B)),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        Text("Incidencia", fontFamily = miTipografia, color = Color.White, fontWeight = Bold, fontSize = 12.sp)
                    }
                    Button(
                        onClick = onConfirmarClick,
                        enabled = !cargando,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        if (cargando) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                        } else {
                            Text("Recibido todo", fontFamily = miTipografia, color = Color.White, fontWeight = Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilaVariantePedido(
    color: String,
    talla: Double,
    cantidad: Int,
    subtotal: Double
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorNeutroFondo, RoundedCornerShape(10.dp))
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                "${color.ifBlank { "Sin color" }} · Talla $talla",
                fontFamily = miTipografia,
                color = ColorTextoSecundario,
                fontSize = 14.sp,
                fontWeight = Bold
            )
            Text(
                "Cantidad: $cantidad",
                fontFamily = miTipografia,
                color = Color.Gray,
                fontSize = 13.sp
            )
        }
        Text(
            "$subtotal €",
            fontFamily = miTipografia,
            color = ColorPrimario,
            fontSize = 14.sp,
            fontWeight = Bold
        )
    }
}

@Composable
fun CardPedidoRecibido(
    pedido: PedidoRecibido,
    respondiendo: Boolean,
    onAceptarClick: () -> Unit,
    onRechazarClick: () -> Unit
) {
    val precioUnidadTexto = "${pedido.precioUnidad} €"
    val totalTexto = "${pedido.precioUnidad * pedido.cantidad} €"

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, ColorBordeTextField)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .width(105.dp)
                    .height(105.dp)
                    .background(Color.Black, RoundedCornerShape(10.dp)),
                Alignment.Center
            ) {
                AsyncImage(
                    pedido.urlFoto,
                    pedido.modelo,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    pedido.modelo,
                    color = ColorPrimario,
                    fontFamily = miTipografia,
                    fontSize = 17.sp,
                    fontWeight = Bold,
                    maxLines = 1
                )
                Text(
                    "Comprador: ${pedido.nombreComprador}",
                    fontFamily = miTipografia,
                    color = ColorTextoSecundario,
                    fontSize = 14.sp
                )
                Text(
                    "Color: ${pedido.color}",
                    fontFamily = miTipografia,
                    color = ColorTextoSecundario,
                    fontSize = 14.sp
                )
                Text(
                    "Cantidad: ${pedido.cantidad}",
                    fontFamily = miTipografia,
                    color = ColorTextoSecundario,
                    fontSize = 14.sp
                )
                Text(
                    "Precio unidad: $precioUnidadTexto",
                    fontFamily = miTipografia,
                    color = ColorTextoSecundario,
                    fontSize = 14.sp
                )
                Text(
                    "Total: $totalTexto",
                    fontFamily = miTipografia,
                    color = ColorPrimario,
                    fontSize = 15.sp,
                    fontWeight = Bold
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onRechazarClick,
                        enabled = !respondiendo,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0392B)),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        Text(
                            "Rechazar",
                            fontFamily = miTipografia,
                            color = Color.White,
                            fontWeight = Bold,
                            fontSize = 13.sp
                        )
                    }
                    Button(
                        onClick = onAceptarClick,
                        enabled = !respondiendo,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = ColorPrimario),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        if (respondiendo) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                "Enviar",
                                fontFamily = miTipografia,
                                color = Color.White,
                                fontWeight = Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CardPedidoComprador(
    pedido: PedidoRecibido,
    cargando: Boolean,
    onConfirmarClick: () -> Unit,
    onIncidenciaClick: () -> Unit
) {
    val totalTexto = "${pedido.precioUnidad * pedido.cantidad} €"
    val estado = pedido.estado.lowercase()
    val textoEstado = when (estado) {
        "pendiente" -> "Pendiente de respuesta"
        "enviado" -> "Enviado"
        "denegado" -> "Denegado"
        "recibido" -> "Recibido"
        "reportado" -> "Incidencia creada"
        else -> pedido.estado
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, ColorBordeTextField)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .width(105.dp)
                    .height(105.dp)
                    .background(Color.Black, RoundedCornerShape(10.dp)),
                Alignment.Center
            ) {
                AsyncImage(
                    pedido.urlFoto,
                    pedido.modelo,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    pedido.modelo,
                    color = ColorPrimario,
                    fontFamily = miTipografia,
                    fontSize = 17.sp,
                    fontWeight = Bold,
                    maxLines = 1
                )
                Text("Vendedor: ${pedido.nombreVendedor.ifBlank { "Vendedor" }}", fontFamily = miTipografia, color = ColorTextoSecundario, fontSize = 14.sp)
                Text("Color: ${pedido.color}", fontFamily = miTipografia, color = ColorTextoSecundario, fontSize = 14.sp)
                Text("Talla: ${pedido.talla}", fontFamily = miTipografia, color = ColorTextoSecundario, fontSize = 14.sp)
                Text("Cantidad: ${pedido.cantidad}", fontFamily = miTipografia, color = ColorTextoSecundario, fontSize = 14.sp)
                Text("Total: $totalTexto", fontFamily = miTipografia, color = ColorPrimario, fontSize = 15.sp, fontWeight = Bold)
                Text("Estado: $textoEstado", fontFamily = miTipografia, color = ColorPrimario, fontSize = 14.sp, fontWeight = Bold)

                if (estado == "enviado") {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = onIncidenciaClick,
                            enabled = !cargando,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0392B)),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            Text("Incidencia", fontFamily = miTipografia, color = Color.White, fontWeight = Bold, fontSize = 12.sp)
                        }
                        Button(
                            onClick = onConfirmarClick,
                            enabled = !cargando,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            if (cargando) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                            } else {
                                Text("Recibido", fontFamily = miTipografia, color = Color.White, fontWeight = Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
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
fun CardIncidenciaUsuario(incidencia: Incidencia) {
    val resultado = when (incidencia.estado.lowercase()) {
        "aceptada" -> "Resultado: razón al comprador"
        "rechazada" -> "Resultado: razón al vendedor"
        else -> "Resultado: ${incidencia.estado}"
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, ColorBordeTextField),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = incidencia.urlImagen,
                contentDescription = incidencia.modelo,
                modifier = Modifier
                    .size(86.dp)
                    .background(Color.Black, RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    "Incidencia resuelta",
                    color = ColorPrimario,
                    fontFamily = miTipografia,
                    fontWeight = Bold,
                    fontSize = 16.sp
                )
                Text(
                    incidencia.modelo,
                    color = ColorPrimario,
                    fontFamily = miTipografia,
                    fontWeight = Bold,
                    fontSize = 14.sp
                )
                Text(
                    resultado,
                    color = if (incidencia.estado.lowercase() == "aceptada") ColorPrimario else Color(0xFFC0392B),
                    fontFamily = miTipografia,
                    fontWeight = Bold,
                    fontSize = 13.sp
                )
                Text(
                    "Motivo: ${incidencia.tipo}",
                    color = ColorTextoSecundario,
                    fontFamily = miTipografia,
                    fontSize = 12.sp
                )
                if (incidencia.descripcion.isNotBlank()) {
                    Text(
                        incidencia.descripcion,
                        color = ColorTextoSecundario,
                        fontFamily = miTipografia,
                        fontSize = 12.sp
                    )
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

