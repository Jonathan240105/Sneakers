package com.example.snkrsapp.Views.Pantallas

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.ExtraBold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.snkrsapp.Domain.ColorPublicacion
import com.example.snkrsapp.Domain.Marca
import com.example.snkrsapp.Domain.ProductoItem
import com.example.snkrsapp.Domain.VarianteNuevaPublicacion
import com.example.snkrsapp.Views.ViewModels.PrincipalViewModel
import com.example.snkrsapp.Views.ViewModels.ViewmodelAgregarProducto
import com.example.snkrsapp.ui.theme.ColorAcento
import com.example.snkrsapp.ui.theme.ColorNeutroFondo
import com.example.snkrsapp.ui.theme.ColorPrimario
import com.example.snkrsapp.ui.theme.ColorTextFieldNoSeleccionado
import com.example.snkrsapp.ui.theme.ColorTextFieldSeleccionado
import com.example.snkrsapp.ui.theme.ColorTextoSecundario
import com.example.snkrsapp.ui.theme.miTipografia
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAgregarProducto(
    navegarAtras: () -> Unit,
    principalViewModel: PrincipalViewModel,
    agregarProductoViewModel: ViewmodelAgregarProducto,
    paddingValues: PaddingValues
) {
    val modelPrincipal by principalViewModel.model.collectAsState()
    val model by agregarProductoViewModel.model.collectAsState()

    val creandoMarca = model.marcaSeleccionada.lowercase() == "otro"

    LaunchedEffect(Unit) {
        agregarProductoViewModel.cargarColores()
    }

    LaunchedEffect(model.exito) {
        if (model.exito) {
            agregarProductoViewModel.resetearEstadoNuevaPublicacion()
            navegarAtras()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorNeutroFondo)
            .padding(paddingValues)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(30.dp))

        TituloAgregarPublicacion(
            if (model.esColeccion) "Añadir a mi Colección" else "Publicar Sneaker"
        )


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .background(ColorPrimario, RoundedCornerShape(12.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val itemModifier = Modifier.weight(1f)

            Box(
                modifier = itemModifier
                    .background(
                        color = if (!model.esColeccion) Color.White else Color.Transparent,
                        shape = RoundedCornerShape(9.dp)
                    )
                    .clickable { agregarProductoViewModel.cambiarModoColeccion(false) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Para Vender",
                    fontFamily = miTipografia,
                    color = if (!model.esColeccion) ColorTextoSecundario else ColorTextoSecundario,
                    fontWeight = Bold,
                    fontSize = 16.sp,

                    )
            }

            Box(
                modifier = itemModifier
                    .background(
                        color = if (model.esColeccion) Color.White else Color.Transparent,
                        shape = RoundedCornerShape(9.dp)
                    )
                    .clickable { agregarProductoViewModel.cambiarModoColeccion(true) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Mi Colección",
                    fontFamily = miTipografia,
                    fontWeight = Bold,
                    color = if (model.esColeccion) Color.Black else ColorTextoSecundario,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(bottom = 15.dp)
        ) {
            item {

                Selector(
                    "Marca",
                    model.cargando,
                    model.marcaSeleccionada,
                    modelPrincipal.listaMarcas,
                    { id, marca -> agregarProductoViewModel.cambiarMarca(id, marca) },
                    { id, producto -> agregarProductoViewModel.cambiarProducto(id, producto) })

                AnimatedVisibility(visible = model.marcaSeleccionada.lowercase() == "otro") {
                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        TextoConTextField(
                            label = "Nombre de la nueva Marca",
                            valor = model.nombreNuevaMarcaText
                        ) {
                            if (it.length <= 30) {
                                agregarProductoViewModel.cambiarNombreNuevaMarca(it)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(15.dp))

                val marcaYaElegida = model.marcaSeleccionada.isNotEmpty()
                Buscador(
                    "Modelo",
                    model.cargando,
                    creandoMarca,
                    marcaYaElegida,
                    model.textoBuscador,
                    {
                        if (it.length <= 30) {
                            agregarProductoViewModel.cambiarBusquedaModelo(it)
                        }
                    },
                    model.sugerenciasModelos,
                    { id, producto -> agregarProductoViewModel.cambiarProducto(id, producto) },
                    model.modeloSeleccionado,
                    {
                        if (it.length <= 30) {
                            agregarProductoViewModel.cambiarNombreNuevoProducto(it)
                        }
                    }
                )

                AnimatedVisibility(visible = model.modeloSeleccionado.lowercase() == "otro" || creandoMarca) {
                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        TextoConTextField(
                            label = "Confirmación del nuevo Modelo",
                            valor = model.nombreNuevoProductoText
                        ) { agregarProductoViewModel.cambiarNombreNuevoProducto(it) }
                    }
                }

                Spacer(Modifier.height(15.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    Box(Modifier.weight(if (model.esColeccion) 1f else 2f)) {
                        TextoConTextField(
                            label = if (model.esColeccion) "Precio Valor (€)" else "Precio Venta (€)",
                            valor = if (model.precioNuevaPublicacion == 0.0) "" else model.precioNuevaPublicacion.toString(),
                            isNumeric = true
                        ) {
                            val valorPrecio = it.toDoubleOrNull() ?: 0.0
                            agregarProductoViewModel.cambiarPrecio(valorPrecio)
                        }
                    }
                    if (model.esColeccion) {
                        Box(Modifier.weight(1f)) {
                            TextoConTextField(
                                label = "Talla",
                                valor = if (model.tallaNuevaPublicacion == 0.0) "" else model.tallaNuevaPublicacion.toString(),
                                isNumeric = true
                            ) {
                                val valorTalla = it.toDoubleOrNull() ?: 0.0
                                agregarProductoViewModel.cambiarTalla(valorTalla)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(15.dp))

                AnimatedVisibility(visible = model.esColeccion) {
                    Column {
                        SelectorImagen(
                            agregarProductoViewModel,
                            esColeccion = model.esColeccion
                        )
                        Spacer(Modifier.height(15.dp))
                    }
                }

                AnimatedVisibility(visible = !model.esColeccion) {
                    VariantesPublicacion(
                        variantes = model.variantes,
                        colores = model.coloresDisponibles,
                        cambiarColor = agregarProductoViewModel::cambiarColorVariante,
                        cambiarTalla = agregarProductoViewModel::cambiarTallaVariante,
                        cambiarStock = agregarProductoViewModel::cambiarStockVariante,
                        subirFoto = agregarProductoViewModel::subirFotoVariante,
                        agregarVariante = agregarProductoViewModel::agregarVariante,
                        eliminarVariante = agregarProductoViewModel::eliminarVariante
                    )
                }

                if (model.mensajeError != null) {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = model.mensajeError!!,
                        color = Color.Red,
                        fontFamily = miTipografia,
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.height(30.dp))
            }
        }

        BotonesInferiores(
            model.cargando,
            textoBoton = if (model.esColeccion) "Añadir a la Colección" else "Publicar Sneaker",
            agregarPublicacion = { agregarProductoViewModel.agregarPublicacion() },
            volverAtras = {
                agregarProductoViewModel.resetearEstadoNuevaPublicacion(); navegarAtras()
            })
    }
}

@Composable
fun VariantesPublicacion(
    variantes: List<VarianteNuevaPublicacion>,
    colores: List<ColorPublicacion>,
    cambiarColor: (Int, ColorPublicacion) -> Unit,
    cambiarTalla: (Int, Double) -> Unit,
    cambiarStock: (Int, Int) -> Unit,
    subirFoto: (Int, Uri) -> Unit,
    agregarVariante: () -> Unit,
    eliminarVariante: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Variantes disponibles",
                color = ColorTextoSecundario,
                fontFamily = miTipografia,
                fontSize = 17.sp,
                fontWeight = Bold
            )
            IconButton(onClick = agregarVariante) {
                Icon(Icons.Default.Add, contentDescription = "Añadir variante", tint = ColorPrimario)
            }
        }

        variantes.forEachIndexed { index, variante ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Variante ${index + 1}",
                            color = ColorPrimario,
                            fontFamily = miTipografia,
                            fontWeight = Bold,
                            fontSize = 16.sp
                        )
                        if (variantes.size > 1) {
                            IconButton(onClick = { eliminarVariante(index) }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Eliminar variante",
                                    tint = Color.Red
                                )
                            }
                        }
                    }

                    SelectorColorVariante(
                        variante = variante,
                        colores = colores,
                        cambiarColor = { cambiarColor(index, it) }
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(Modifier.weight(1f)) {
                            TextoConTextField(
                                label = "Talla",
                                valor = if (variante.talla == 0.0) "" else variante.talla.toString(),
                                isNumeric = true
                            ) { texto ->
                                cambiarTalla(index, texto.toDoubleOrNull() ?: 0.0)
                            }
                        }
                        Box(Modifier.weight(1f)) {
                            TextoConTextField(
                                label = "Stock",
                                valor = variante.cantidadDisponible.toString(),
                                isNumeric = true
                            ) { texto ->
                                cambiarStock(index, texto.toIntOrNull() ?: 1)
                            }
                        }
                    }

                    SelectorImagenVariante(
                        variante = variante,
                        subirFoto = { uri -> subirFoto(index, uri) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorColorVariante(
    variante: VarianteNuevaPublicacion,
    colores: List<ColorPublicacion>,
    cambiarColor: (ColorPublicacion) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            "Color",
            color = ColorTextoSecundario,
            fontFamily = miTipografia,
            fontSize = 17.sp,
            fontWeight = Bold
        )
        Spacer(Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = variante.nombreColor.ifBlank { "Selecciona un color" },
                onValueChange = {},
                readOnly = true,
                leadingIcon = {
                    ColorMuestra(hex = variante.hexColor)
                },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = outlinedTextFieldCustomColors(),
                shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color(0xFF1E1E1E))
            ) {
                colores.forEach { color ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                ColorMuestra(hex = color.hex)
                                Text(
                                    text = color.nombre,
                                    color = Color.White,
                                    fontFamily = miTipografia
                                )
                            }
                        },
                        onClick = {
                            cambiarColor(color)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ColorMuestra(hex: String?) {
    val color = remember(hex) {
        runCatching {
            Color(android.graphics.Color.parseColor(hex ?: "#BDBDBD"))
        }.getOrDefault(Color(0xFFBDBDBD))
    }

    Box(
        modifier = Modifier
            .size(22.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(color)
            .border(1.dp, Color(0xFF333333), RoundedCornerShape(6.dp))
    )
}

@Composable
fun SelectorImagenVariante(
    variante: VarianteNuevaPublicacion,
    subirFoto: (Uri) -> Unit
) {
    val context = LocalContext.current
    var mostrarOpciones by remember { mutableStateOf(false) }
    var uriCamaraTemporal by remember { mutableStateOf<Uri?>(null) }

    val launcherGaleria = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let(subirFoto)
    }

    val launcherCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito ->
        if (exito) {
            uriCamaraTemporal?.let(subirFoto)
        }
    }

    val launcherPermisoCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            val archivo = File.createTempFile("snkrs_variante_", ".jpg", context.cacheDir)
            val uriSegura = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                archivo
            )

            uriCamaraTemporal = uriSegura
            launcherCamara.launch(uriSegura)
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Foto de este color",
            color = ColorTextoSecundario,
            fontFamily = miTipografia,
            fontSize = 17.sp,
            fontWeight = Bold
        )
        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(145.dp)
                .background(Color(0xFF1E1E1E), RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = if (!variante.errorImagen.isNullOrBlank()) Color.Red else Color(0xFF333333),
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable(enabled = !variante.cargandoImagen) { mostrarOpciones = true },
            contentAlignment = Alignment.Center
        ) {
            when {
                variante.cargandoImagen -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Color.White)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Subiendo imagen...",
                            fontFamily = miTipografia,
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                }

                variante.urlFoto.isNotBlank() -> {
                    AsyncImage(
                        model = variante.urlFoto,
                        contentDescription = "Foto de variante",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                else -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "+ Añadir foto del color",
                            fontFamily = miTipografia,
                            color = Color.White,
                            fontWeight = Bold,
                            fontSize = 15.sp
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Cámara o galería",
                            fontFamily = miTipografia,
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        if (!variante.errorImagen.isNullOrBlank()) {
            Spacer(Modifier.height(6.dp))
            Text(
                text = variante.errorImagen,
                fontFamily = miTipografia,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }

    if (mostrarOpciones) {
        AlertDialog(
            onDismissRequest = { mostrarOpciones = false },
            containerColor = Color(0xFF1E1E1E),
            title = {
                Text("Añadir imagen", color = Color.White, fontWeight = Bold)
            },
            text = {
                Text(
                    "Elige la foto correspondiente a este color.",
                    color = Color.Gray,
                    fontFamily = miTipografia
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    mostrarOpciones = false
                    launcherPermisoCamara.launch(Manifest.permission.CAMERA)
                }) {
                    Text("Cámara", fontFamily = miTipografia, color = Color.White, fontWeight = Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    mostrarOpciones = false
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
fun Buscador(
    texto: String,
    estaCargando: Boolean,
    creandoMarca: Boolean,
    marcaYaElegida: Boolean,
    textoBuscador: String,
    cambiarTextoBuscador: (String) -> Unit,
    listaSugerencias: List<ProductoItem>,
    cambiarProducto: (Int, String) -> Unit,
    modeloSeleccionado: String,
    cambiarNombreNuevoProducto: (String) -> Unit
) {

    Text(
        texto, color = ColorTextoSecundario,
        fontFamily = miTipografia, fontSize = 17.sp, fontWeight = Bold
    )
    Spacer(Modifier.height(8.dp))

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = if (creandoMarca) "Otro" else textoBuscador,
            onValueChange = { cambiarTextoBuscador(it) },
            enabled = marcaYaElegida &&
                    !creandoMarca &&
                    !estaCargando,
            placeholder = {
                Text(
                    text = if (marcaYaElegida) "Busca un modelo (Ej: Air Force 1) " else "Primero selecciona una marca",
                    color = Color.Gray,
                    fontFamily = miTipografia
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = outlinedTextFieldCustomColors(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        if (marcaYaElegida && (listaSugerencias.isNotEmpty() || textoBuscador.isNotBlank())) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column {
                    listaSugerencias.forEach { sugerencia ->
                        Text(
                            text = sugerencia.modelo,
                            color = Color.White,
                            fontFamily = miTipografia,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    cambiarProducto(
                                        sugerencia.idProducto,
                                        sugerencia.modelo
                                    )
                                }
                                .padding(14.dp),
                            fontSize = 15.sp
                        )
                    }

                    if (textoBuscador.isNotBlank() && modeloSeleccionado != textoBuscador) {
                        Text(
                            text = "Crear nuevo modelo: \"${textoBuscador}\"",
                            color = Color.White,
                            fontFamily = miTipografia,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    cambiarProducto(0, "Otro")
                                    cambiarNombreNuevoProducto(
                                        textoBuscador
                                    )
                                }
                                .padding(14.dp),
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Selector(
    texto: String,
    estaCargando: Boolean,
    marcaSeleccionada: String,
    listaMarcas: List<Marca>,
    cambiarMarca: (Int, String) -> Unit,
    cambiarProducto: (Int, String) -> Unit,
) {

    var expandedMarcas by remember { mutableStateOf(false) }

    Text(texto, color = ColorTextoSecundario, fontSize = 17.sp, fontWeight = Bold)
    Spacer(Modifier.height(8.dp))
    ExposedDropdownMenuBox(
        expanded = expandedMarcas,
        onExpandedChange = { if (!estaCargando) expandedMarcas = !expandedMarcas }
    ) {
        OutlinedTextField(
            value = if (marcaSeleccionada.isEmpty()) "Selecciona una marca" else marcaSeleccionada,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMarcas) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            colors = outlinedTextFieldCustomColors(),
            shape = RoundedCornerShape(12.dp)
        )
        ExposedDropdownMenu(
            expanded = expandedMarcas,
            onDismissRequest = { expandedMarcas = false },
            modifier = Modifier.background(Color(0xFF1E1E1E))
        ) {
            listaMarcas.forEach { marca ->
                DropdownMenuItem(
                    text = {
                        Text(
                            marca.nombre ?: "",
                            fontFamily = miTipografia, color = Color.White
                        )
                    },
                    onClick = {
                        cambiarMarca(
                            marca.idMarca ?: 0,
                            marca.nombre ?: ""
                        )
                        expandedMarcas = false
                    }
                )
            }
            DropdownMenuItem(
                text = {
                    Text(
                        "Otro (Añadir nueva marca...)",
                        color = Color.White,
                        fontFamily = miTipografia
                    )
                },
                onClick = {
                    cambiarMarca(0, "Otro")
                    cambiarProducto(0, "Otro")
                    expandedMarcas = false
                }
            )
        }
    }
}

@Composable
fun BotonesInferiores(
    estaCargando: Boolean,
    textoBoton: String,
    agregarPublicacion: () -> Unit,
    volverAtras: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            agregarPublicacion,
            colors = ButtonDefaults.buttonColors(
                containerColor = ColorAcento,
                disabledContainerColor = ColorAcento.copy(alpha = 0.5f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(85.dp)
                .padding(vertical = 15.dp)
        ) {
            if (estaCargando) {
                CircularProgressIndicator(color = Color.Black)
            } else {
                Text(
                    textoBoton,
                    color = Color.White,
                    fontWeight = Bold,
                    fontFamily = miTipografia,
                    fontSize = 19.sp
                )
            }
        }

        Text(
            "Cancelar",
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .clickable(enabled = !estaCargando) { volverAtras() },
            textAlign = TextAlign.Center,
            fontSize = 17.sp,
            fontFamily = miTipografia,
            fontWeight = Bold
        )
    }
}

@Composable
fun TituloAgregarPublicacion(texto: String) {
    Text(
        texto,
        color = ColorPrimario,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .testTag("tituloPrincipal"),
        style = TextStyle(
            fontSize = 25.sp, fontWeight = ExtraBold,
            fontFamily = miTipografia
        )
    )
}

@Composable
fun TextoConTextField(
    label: String,
    valor: String,
    isNumeric: Boolean = false,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(label, color = ColorTextoSecundario,
            fontFamily = miTipografia, fontSize = 17.sp, fontWeight = Bold)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = valor,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = if (isNumeric) KeyboardOptions(keyboardType = KeyboardType.Decimal) else KeyboardOptions.Default,
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = outlinedTextFieldCustomColors()
        )
    }
}

@Composable
fun outlinedTextFieldCustomColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,

    focusedContainerColor = ColorTextFieldSeleccionado,
    unfocusedContainerColor = ColorTextFieldNoSeleccionado,

    focusedBorderColor = Color(0xFF011681),
    unfocusedBorderColor = Color.Transparent,

    focusedLabelColor = Color(0xFF011681),
    unfocusedLabelColor = Color.Gray,

    focusedLeadingIconColor = Color.White,
    unfocusedLeadingIconColor = Color.DarkGray,

    focusedTrailingIconColor = Color.White,
    unfocusedTrailingIconColor = Color.LightGray,
    disabledContainerColor = ColorTextFieldSeleccionado,

    cursorColor = Color.White
)
