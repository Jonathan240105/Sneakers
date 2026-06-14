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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import com.example.snkrsapp.Views.Controlador.NavigationUtils.BottomBarAdmin
import com.example.snkrsapp.Views.ViewModels.PantallaEventosViewModel
import com.example.snkrsapp.ui.theme.ColorAcento
import com.example.snkrsapp.ui.theme.ColorBordeTextField
import com.example.snkrsapp.ui.theme.ColorNeutroFondo
import com.example.snkrsapp.ui.theme.ColorPrimario
import com.example.snkrsapp.ui.theme.ColorTextoSecundario
import com.example.snkrsapp.ui.theme.miTipografia
import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.utils.now
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PantallaEventosAdmin(
    myViewModel: PantallaEventosViewModel,
    paddingValues: PaddingValues,
    navController: NavController
) {
    val model by myViewModel.model.collectAsState()

    val eventosSeleccionados = remember { mutableStateListOf<Int>() }
    val modoBorrado = eventosSeleccionados.isNotEmpty()

    var mostrarDialogoCrear by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        myViewModel.cargarEventos()
    }

    Scaffold(
        bottomBar = {
            if (modoBorrado) {
                BottomBarBorradoLocal(
                    cantidadSeleccionados = eventosSeleccionados.size,
                    onBorrar = {
                        myViewModel.eliminarEventos(eventosSeleccionados.toList())
                        eventosSeleccionados.clear()
                    },
                    onCancelar = { eventosSeleccionados.clear() }
                )
            } else {
                BottomBarAdmin(navController)
            }
        },
        floatingActionButton = {
            if (!modoBorrado) {
                BotonCrearEvento {
                    mostrarDialogoCrear = true
                }
            }
        }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .background(ColorNeutroFondo)
                .padding(top = paddingValues.calculateTopPadding())
                .padding(bottom = innerPadding.calculateBottomPadding())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(30.dp))

            Text(
                "Listado de Eventos",
                color = ColorPrimario,
                fontFamily = miTipografia,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                style = TextStyle(fontSize = 25.sp, fontWeight = Bold)
            )

            Spacer(Modifier.height(10.dp))

            LazyColumn(
                Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 15.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (model.listaEventos.isEmpty()) {
                    item {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "No hay eventos registrados",
                                color = ColorTextoSecundario,
                                fontSize = 17.sp,
                                fontFamily = miTipografia
                            )
                        }
                    }
                }

                items(model.listaEventos, key = { it.idEvento }) { evento ->
                    val estaSeleccionado = eventosSeleccionados.contains(evento.idEvento)
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .combinedClickable(
                                onClick = {
                                    if (modoBorrado) {
                                        if (estaSeleccionado) eventosSeleccionados.remove(evento.idEvento)
                                        else eventosSeleccionados.add(evento.idEvento)
                                    }
                                },
                                onLongClick = {
                                    if (!estaSeleccionado) {
                                        eventosSeleccionados.add(evento.idEvento)
                                    }
                                }
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (estaSeleccionado) Color.DarkGray else Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, ColorBordeTextField)
                    ) {
                        Row(
                            Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    evento.titulo,
                                    color = ColorPrimario,
                                    fontSize = 17.sp,
                                    fontFamily = miTipografia,
                                    fontWeight = Bold
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    evento.descripcion,
                                    color = ColorTextoSecundario,
                                    fontSize = 16.sp,
                                    fontFamily = miTipografia
                                )
                                Spacer(Modifier.height(6.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        "",
                                        tint = ColorTextoSecundario,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = evento.fechaEvento.formatearFecha(),
                                        color = ColorTextoSecundario,
                                        fontSize = 15.sp
                                    )
                                }
                            }

                            if (modoBorrado) {
                                Checkbox(
                                    checked = estaSeleccionado,
                                    onCheckedChange = { chk ->
                                        if (chk) eventosSeleccionados.add(evento.idEvento)
                                        else eventosSeleccionados.remove(evento.idEvento)
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Color.White,
                                        uncheckedColor = Color.Gray,
                                        checkmarkColor = Color.Black
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogoCrear) {
        DialogCrearEventoAdmin(
            onDismiss = { mostrarDialogoCrear = false },
            onConfirmar = { titulo, desc, fecha ->
                myViewModel.crearEvento(titulo, desc, fecha)
                mostrarDialogoCrear = false
            }
        )
    }
}

@Composable
fun DialogCrearEventoAdmin(
    onDismiss: () -> Unit,
    onConfirmar: (titulo: String, desc: String, fecha: String) -> Unit
) {
    var tituloText by remember { mutableStateOf("") }
    var descText by remember { mutableStateOf("") }
    var fechaText by remember { mutableStateOf("") }

    var mostrarSelectorFecha by remember { mutableStateOf(false) }

    val todoCompletado = tituloText.isNotBlank() &&
            descText.isNotBlank() &&
            fechaText.isNotBlank()

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
                    text = "Registrar Nuevo Evento",
                    color = ColorPrimario,
                    fontSize = 20.sp,
                    fontFamily = miTipografia,
                    fontWeight = Bold
                )

                Column {
                    Text(
                        "Título del Evento",
                        color = ColorTextoSecundario,
                        fontSize = 16.sp,
                        fontFamily = miTipografia,
                        fontWeight = Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = tituloText,
                        onValueChange = {
                            if (it.length <= 30) {
                                tituloText = it
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = outlinedCustomColors(),
                        placeholder = {
                            Text(
                                "Ej. Lanzamiento Yeezy, Drop Nike...",
                                color = Color.White.copy(0.9f),
                                fontFamily = miTipografia
                            )
                        }
                    )
                }

                Column {
                    Text(
                        "Descripción",
                        color = ColorTextoSecundario,
                        fontSize = 16.sp,
                        fontFamily = miTipografia,
                        fontWeight = Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = descText,
                        onValueChange = {
                            if (it.length <= 100) {
                                descText = it
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = outlinedCustomColors(),
                        placeholder = {
                            Text(
                                "Detalles del evento...",
                                color = Color.White.copy(0.9f),
                                fontFamily = miTipografia
                            )
                        }
                    )
                }

                Column {
                    Text(
                        "Fecha del Evento",
                        color = ColorTextoSecundario,
                        fontSize = 16.sp,
                        fontWeight = Bold,
                        fontFamily = miTipografia
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = fechaText,
                        onValueChange = {},
                        placeholder = { Text("dd/mm/aaaa",
                            fontFamily = miTipografia, color = Color.White.copy(0.8f)) },
                        colors = outlinedCustomColors(),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.DateRange,
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
                        Text("Cancelar",
                            fontFamily = miTipografia, color = Color.Black, fontWeight = Bold)
                    }

                    Button(
                        onClick = {
                            if (todoCompletado) {
                                onConfirmar(tituloText, descText, fechaText)
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
                            color = Color.White,
                            fontWeight = Bold,
                            fontFamily = miTipografia
                        )
                    }
                }
            }
        }

        SelectorFecha(
            mostrar = mostrarSelectorFecha,
            cerrarSelectorFecha = { mostrarSelectorFecha = false },
            confirmarFecha = { fechaText = it },
            LocalDate.now(),
            LocalDate(2036, 12, 31)
        )
    }
}

@Composable
fun BotonCrearEvento(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(50.dp)
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, ColorBordeTextField, RoundedCornerShape(12.dp))
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Añadir Evento",
            tint = ColorPrimario,
            modifier = Modifier.size(30.dp)
        )
    }
}

fun String.formatearFecha(): String {
    return try {
        val fechaUtc = ZonedDateTime.parse(this)

        val fechaLocal = fechaUtc.withZoneSameInstant(ZoneId.systemDefault())

        val formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm", Locale.getDefault())


        fechaLocal.format(formateador)
    } catch (e: Exception) {
        this
    }
}