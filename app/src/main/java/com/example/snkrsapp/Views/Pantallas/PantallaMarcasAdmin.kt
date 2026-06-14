package com.example.snkrsapp.Views.Pantallas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.snkrsapp.Data.RemoteData.ProductoDao.CrearMarcaSolicitud
import com.example.snkrsapp.Domain.Marca
import com.example.snkrsapp.Views.Controlador.NavigationUtils.BottomBarAdmin
import com.example.snkrsapp.Views.ViewModels.MarcasAdminViewModel
import com.example.snkrsapp.Views.ViewModels.ViewmodelAgregarProducto
import com.example.snkrsapp.ui.theme.ColorAcento
import com.example.snkrsapp.ui.theme.ColorBordeTextField
import com.example.snkrsapp.ui.theme.ColorNeutroFondo
import com.example.snkrsapp.ui.theme.ColorPrimario
import com.example.snkrsapp.ui.theme.ColorTextFieldNoSeleccionado
import com.example.snkrsapp.ui.theme.ColorTextFieldSeleccionado
import com.example.snkrsapp.ui.theme.ColorTextoSecundario
import com.example.snkrsapp.ui.theme.miTipografia
import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.utils.now

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PantallaMarcasAdmin(
    myViewModel: MarcasAdminViewModel,
    agregarProductoViewModel: ViewmodelAgregarProducto,
    paddingValues: PaddingValues,
    navController: NavController
) {
    val model by myViewModel.model.collectAsState()

    var tabSeleccionada by remember { mutableStateOf(0) }
    val marcasSeleccionadas = remember { mutableStateListOf<Int>() }
    val modoBorrado = marcasSeleccionadas.isNotEmpty() && tabSeleccionada == 1

    var marcaParaCompletar by remember { mutableStateOf<Marca?>(null) }
    var mostrarDialogoCompletar by remember { mutableStateOf(false) }

    var mostrarDialogoCrear by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        myViewModel.cargarMarcas()
    }

    LaunchedEffect(tabSeleccionada) {
        marcasSeleccionadas.clear()
    }

    Scaffold(
        bottomBar = {
            if (modoBorrado) {
                BottomBarBorradoLocal(
                    cantidadSeleccionados = marcasSeleccionadas.size,
                    onBorrar = {
                        myViewModel.eliminarMarcas(marcasSeleccionadas.toList())
                        marcasSeleccionadas.clear()
                    },
                    onCancelar = { marcasSeleccionadas.clear() }
                )
            } else {
                BottomBarAdmin(navController)
            }
        },
        floatingActionButton = {
            BotonCrearMarca {
                mostrarDialogoCrear = true
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorNeutroFondo)
                .padding(top = paddingValues.calculateTopPadding())
                .padding(bottom = innerPadding.calculateBottomPadding())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(30.dp))

            Text(
                text = "Listado de Marcas",
                fontFamily = miTipografia,
                color = ColorPrimario,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                style = TextStyle(fontSize = 25.sp, fontWeight = Bold)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .background(ColorPrimario, RoundedCornerShape(12.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    Modifier
                        .weight(1f)
                        .background(
                            if (tabSeleccionada == 0) Color.White else Color.Transparent,
                            RoundedCornerShape(9.dp)
                        )
                        .clickable { tabSeleccionada = 0 }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Pendientes (${model.listaMarcasIncompletas.size})",
                        color = if (tabSeleccionada == 0) ColorTextoSecundario else Color.White,
                        fontWeight = Bold,
                        fontFamily = miTipografia,
                        fontSize = 16.sp
                    )
                }
                Box(
                    Modifier
                        .weight(1f)
                        .background(
                            if (tabSeleccionada == 1) Color.White else Color.Transparent,
                            RoundedCornerShape(9.dp)
                        )
                        .clickable { tabSeleccionada = 1 }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Verificadas",
                        fontFamily = miTipografia,
                        color = if (tabSeleccionada == 1) ColorTextoSecundario else Color.White,
                        fontWeight = Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            LazyColumn(
                Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 15.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val listaActual =
                    if (tabSeleccionada == 0) model.listaMarcasIncompletas else model.listaMarcas

                if (listaActual.isEmpty()) {
                    item {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "No hay marcas en esta sección",
                                color = ColorTextoSecundario,
                                fontSize = 17.sp,
                                fontFamily = miTipografia
                            )
                        }
                    }
                }

                items(listaActual, key = { it.idMarca }) { marca ->
                    val estaSeleccionado = marcasSeleccionadas.contains(marca.idMarca)
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .combinedClickable(
                                onClick = {
                                    if (tabSeleccionada == 1 && modoBorrado) {
                                        if (estaSeleccionado) marcasSeleccionadas.remove(marca.idMarca)
                                        else marcasSeleccionadas.add(marca.idMarca)
                                    }
                                },
                                onLongClick = {
                                    if (tabSeleccionada == 1 && !estaSeleccionado) marcasSeleccionadas.add(
                                        marca.idMarca
                                    )
                                }
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (estaSeleccionado) Color.LightGray else Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, ColorBordeTextField)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            AsyncImage(marca.logoUrl ?: "", "", modifier = Modifier.size(50.dp))
                            Spacer(Modifier.width(15.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = marca.nombre ?: "Sin nombre",
                                    color = ColorPrimario,
                                    fontSize = 16.sp,
                                    fontWeight = Bold,
                                    fontFamily = miTipografia
                                )

                                if (tabSeleccionada == 1) {
                                    Spacer(Modifier.height(4.dp))

                                    val pais = marca.paisOrigen ?: ""
                                    val fecha = marca.fechaFundacion ?: ""

                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        if (pais.isNotBlank()) {
                                            Text(
                                                text = pais,
                                                fontFamily = miTipografia,
                                                color = ColorTextoSecundario,
                                                fontSize = 15.sp
                                            )
                                        }

                                        if (pais.isNotBlank() && fecha.isNotBlank()) {
                                            Text(
                                                text = "•",
                                                fontFamily = miTipografia,
                                                color = ColorTextoSecundario,
                                                fontSize = 15.sp
                                            )
                                        }

                                        if (fecha.isNotBlank()) {
                                            Text(
                                                text = fecha,
                                                fontFamily = miTipografia,
                                                color = ColorTextoSecundario,
                                                fontSize = 15.sp
                                            )
                                        }
                                    }
                                }
                            }

                            if (modoBorrado && tabSeleccionada == 1) {
                                Checkbox(
                                    checked = estaSeleccionado,
                                    onCheckedChange = {
                                        if (it) marcasSeleccionadas.add(marca.idMarca) else marcasSeleccionadas.remove(
                                            marca.idMarca
                                        )
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Color.White,
                                        uncheckedColor = Color.Gray,
                                        checkmarkColor = Color.Black
                                    )
                                )
                            } else if (tabSeleccionada == 0) {
                                IconButton(onClick = {
                                    marcaParaCompletar = marca
                                    mostrarDialogoCompletar = true
                                }) {
                                    Icon(Icons.Default.Edit, "", tint = ColorTextoSecundario)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogoCompletar && marcaParaCompletar != null) {
        DialogCompletarMarcaAdmin(
            marca = marcaParaCompletar!!,
            agregarProductoViewModel = agregarProductoViewModel,
            onDismiss = {
                mostrarDialogoCompletar = false
                marcaParaCompletar = null
            },
            onConfirmar = { idMarca, nombre, pais, fecha, web ->
                val urlSubida = agregarProductoViewModel.model.value.urlImagenNuevaPublicacion
                val urlFinalLogo =
                    if (!urlSubida.isNullOrBlank()) urlSubida else (marcaParaCompletar!!.logoUrl
                        ?: "")

                myViewModel.completarMarca(idMarca, pais, fecha, urlFinalLogo, web)
                mostrarDialogoCompletar = false
                marcaParaCompletar = null
            }
        )
    }

    if (mostrarDialogoCrear) {
        DialogCrearMarcaAdmin(
            agregarProductoViewModel = agregarProductoViewModel,
            onDismiss = { mostrarDialogoCrear = false },
            onConfirmar = { nombre, pais, fecha, web ->
                val urlFinalLogo =
                    agregarProductoViewModel.model.value.urlImagenNuevaPublicacion ?: ""

                myViewModel.crearMarca(CrearMarcaSolicitud(nombre, pais, fecha, urlFinalLogo, web))
                mostrarDialogoCrear = false
            }
        )
    }
}

@Composable
fun DialogCompletarMarcaAdmin(
    marca: Marca,
    agregarProductoViewModel: ViewmodelAgregarProducto,
    onDismiss: () -> Unit,
    onConfirmar: (idMarca: Int, nombre: String, pais: String, fechaFundacion: String, web: String) -> Unit
) {
    var nombreMarcaText by remember { mutableStateOf(marca.nombre ?: "") }
    var paisOrigenText by remember { mutableStateOf("") }
    var webOficialText by remember { mutableStateOf("") }
    var fechaFundacionText by remember { mutableStateOf("") }
    var mostrarSelectorFecha by remember { mutableStateOf(false) }

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
            colors = CardDefaults.cardColors(containerColor = ColorNeutroFondo)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Aprobar y Completar Marca",
                    color = ColorPrimario,
                    fontSize = 20.sp,
                    fontFamily = miTipografia,
                    fontWeight = Bold
                )

                SelectorImagen(agregarProductoViewModel, true)

                Column {
                    Text(
                        "Nombre de la Marca",
                        color = ColorTextoSecundario,
                        fontSize = 16.sp,
                        fontFamily = miTipografia,
                        fontWeight = Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = nombreMarcaText,
                        onValueChange = { nombreMarcaText = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = outlinedCustomColors()
                    )
                }

                Column {
                    Text(
                        "País de Origen",
                        fontFamily = miTipografia,
                        color = ColorTextoSecundario,
                        fontSize = 16.sp,
                        fontWeight = Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = paisOrigenText,
                        onValueChange = { paisOrigenText = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = outlinedCustomColors()
                    )
                }

                Column {
                    Text(
                        "Sitio Web Oficial",
                        fontFamily = miTipografia,
                        color = ColorPrimario,
                        fontSize = 16.sp,
                        fontWeight = Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = webOficialText,
                        onValueChange = { webOficialText = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = outlinedCustomColors()
                    )
                }

                Column {
                    Text(
                        "Fecha de Fundación",
                        fontFamily = miTipografia,
                        color = ColorPrimario,
                        fontSize = 16.sp,
                        fontWeight = Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = fechaFundacionText,
                        onValueChange = {},
                        placeholder = { Text("dd/mm/aaaa",
                            fontFamily = miTipografia, color = Color.White.copy(0.5f)) },
                        colors = outlinedCustomColors(),
                        trailingIcon = {
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = "Calendario",
                                tint = Color.Gray
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        readOnly = true,
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { mostrarSelectorFecha = true }
                    )
                }

                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, ColorBordeTextField)
                    ) {
                        Text(
                            "Cancelar",
                            fontFamily = miTipografia,
                            color = ColorTextoSecundario,
                            fontSize = 15.sp,
                            fontWeight = Bold
                        )
                    }

                    Button(
                        onClick = {
                            onConfirmar(
                                marca.idMarca,
                                nombreMarcaText,
                                paisOrigenText,
                                fechaFundacionText,
                                webOficialText
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ColorAcento),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Verificar",
                            fontFamily = miTipografia, color = Color.White, fontSize = 15.sp, fontWeight = Bold)
                    }
                }
            }
        }

        SelectorFecha(
            mostrarSelectorFecha,
            { mostrarSelectorFecha = false },
            { fechaFundacionText = it },
            LocalDate(1800, 1, 1),
            LocalDate.now()
        )
    }
}

@Composable
fun DialogCrearMarcaAdmin(
    agregarProductoViewModel: ViewmodelAgregarProducto,
    onDismiss: () -> Unit,
    onConfirmar: (nombre: String, pais: String, fechaFundacion: String, web: String) -> Unit
) {
    var nombreMarcaText by remember { mutableStateOf("") }
    var paisOrigenText by remember { mutableStateOf("") }
    var webOficialText by remember { mutableStateOf("") }
    var fechaFundacionText by remember { mutableStateOf("") }
    var mostrarSelectorFecha by remember { mutableStateOf(false) }

    val productoState by agregarProductoViewModel.model.collectAsState()
    val urlLogo = productoState.urlImagenNuevaPublicacion ?: ""

    val todoCompletado = nombreMarcaText.isNotBlank() &&
            paisOrigenText.isNotBlank() &&
            webOficialText.isNotBlank() &&
            fechaFundacionText.isNotBlank() &&
            urlLogo.isNotBlank()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ColorNeutroFondo)
        ) {
            Column(
                Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Registrar Nueva Marca",
                    color = ColorPrimario,
                    fontFamily = miTipografia,
                    fontSize = 20.sp,
                    fontWeight = Bold
                )

                SelectorImagen(agregarProductoViewModel, true)

                Column {
                    Text(
                        "Nombre de la Marca",
                        color = ColorTextoSecundario,
                        fontSize = 16.sp,
                        fontFamily = miTipografia,
                        fontWeight = Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        nombreMarcaText,
                        {
                            if (it.length <= 50) {
                                nombreMarcaText = it
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = outlinedCustomColors(),
                        placeholder = { Text("Ej. Nike, Jordan...",
                            fontFamily = miTipografia, color = Color.DarkGray) }
                    )
                }

                Column {
                    Text(
                        "País de Origen",
                        color = ColorTextoSecundario,
                        fontFamily = miTipografia,
                        fontSize = 16.sp,
                        fontWeight = Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        paisOrigenText,
                        {
                            if (it.length <= 30) {
                                paisOrigenText = it
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = outlinedCustomColors(),
                        placeholder = { Text("Ej. Estados Unidos", color = Color.White.copy(0.5f)) }
                    )
                }

                Column {
                    Text(
                        "Sitio Web Oficial",
                        color = ColorTextoSecundario,
                        fontFamily = miTipografia,
                        fontSize = 16.sp,
                        fontWeight = Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = webOficialText,
                        onValueChange = {
                            if (it.length <= 50) {
                                webOficialText = it
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = outlinedCustomColors(),
                        placeholder = { Text("https://...",
                            fontFamily = miTipografia, color = Color.White.copy(0.5f)) }
                    )
                }

                Column {
                    Text(
                        "Fecha de Fundación",
                        color = ColorTextoSecundario,
                        fontSize = 16.sp,
                        fontFamily = miTipografia,
                        fontWeight = Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = fechaFundacionText,
                        onValueChange = {},
                        placeholder = { Text("dd/mm/aaaa", color = Color.White.copy(0.5f)) },
                        colors = outlinedCustomColors(),
                        trailingIcon = {
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = "Calendario",
                                tint = Color.Gray
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        readOnly = true,
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { mostrarSelectorFecha = true }
                    )
                }

                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, ColorBordeTextField)
                    ) {
                        Text(
                            "Cancelar",
                            fontFamily = miTipografia,
                            color = ColorTextoSecundario,
                            fontSize = 15.sp,
                            fontWeight = Bold
                        )
                    }

                    Button(
                        onClick = {
                            if (todoCompletado) {
                                onConfirmar(
                                    nombreMarcaText,
                                    paisOrigenText,
                                    fechaFundacionText,
                                    webOficialText
                                )
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (todoCompletado) ColorAcento else ColorAcento.copy(
                                0.5f
                            )
                        ),
                        shape = RoundedCornerShape(12.dp),
                        enabled = todoCompletado
                    ) {
                        Text(
                            text = "Guardar",
                            fontFamily = miTipografia,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = Bold
                        )
                    }
                }
            }
        }

        SelectorFecha(
            mostrar = mostrarSelectorFecha,
            cerrarSelectorFecha = { mostrarSelectorFecha = false },
            confirmarFecha = { fechaFundacionText = it },
            LocalDate(1800, 1, 1),
            LocalDate.now()
        )
    }
}

@Composable
fun outlinedCustomColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    disabledTextColor = Color.White,

    unfocusedBorderColor = Color.Transparent,
    focusedBorderColor = ColorBordeTextField,

    cursorColor = Color.White,

    focusedLeadingIconColor = Color.White,
    unfocusedLeadingIconColor = Color.DarkGray,

    focusedContainerColor = ColorTextFieldSeleccionado,
    unfocusedContainerColor = ColorTextFieldNoSeleccionado,
    focusedLabelColor = ColorBordeTextField,
    disabledContainerColor = ColorTextFieldNoSeleccionado,
    unfocusedPlaceholderColor = Color.White.copy(0.9f),
    disabledLabelColor = Color.White,
    disabledPlaceholderColor = Color.White.copy(0.9f)
)

@Composable
fun BotonCrearMarca(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(50.dp)
            .border(1.dp, ColorBordeTextField, RoundedCornerShape(12.dp))
            .background(Color.White, RoundedCornerShape(12.dp))
    ) {
        Icon(
            Icons.Default.Add,
            "",
            tint = ColorTextoSecundario,
            modifier = Modifier.size(30.dp)
        )
    }
}