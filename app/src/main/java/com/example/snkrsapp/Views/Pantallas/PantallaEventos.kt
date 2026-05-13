package com.example.snkrsapp.Views.Pantallas

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.snkrsapp.Data.RemoteData.EventoDao.EventosSolicitud
import com.example.snkrsapp.Domain.Evento
import com.example.snkrsapp.Views.ViewModels.EventosViewModel
import kotlinx.datetime.LocalDateTime
import network.chaintech.kmp_date_time_picker.ui.datetimepicker.WheelDateTimePickerView
import network.chaintech.kmp_date_time_picker.utils.now
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEventos(
    myViewModel: EventosViewModel,
    paddingValues: PaddingValues,
    mostrarSheet: Boolean,
    cambiarSheet: (Boolean) -> Unit
) {

    val model by myViewModel.model.collectAsState()

    val listaDias = (1..model.mesSeleccionado.lengthOfMonth()).toList()
    val nombreMes = model.mesSeleccionado.month.getDisplayName(
        TextStyle.FULL,
        Locale("es", "ES")
    )
    var mostrarPicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val fecha = LocalDate.of(
            model.mesSeleccionado.year,
            model.mesSeleccionado.monthValue,
            model.diaSeleccionado
        )

        myViewModel.cargarEventos(fecha)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(paddingValues)
            .padding(horizontal = 15.dp)
    ) {
        Spacer(Modifier.height(30.dp))

        TituloEventos("Próximos Eventos")

        Text(
            "$nombreMes 2024",
            color = Color.Gray,
            fontSize = 15.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(listaDias) { dia ->
                cardDia(
                    {
                        myViewModel.seleccionarDia(dia)
                    },
                    nombreMes,
                    dia.toString(),
                    dia == model.diaSeleccionado
                )
            }
        }

        Spacer(Modifier.height(25.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Eventos disponibles", color = Color.White, fontSize = 18.sp, fontWeight = Bold)
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            items(model.listaEventos) { evento ->
                CardEvento(evento)
            }
        }
        if (mostrarSheet) {
            ModalBottomSheet(
                onDismissRequest = { cambiarSheet(false) },
                containerColor = Color(0xFF121212),
                dragHandle = { BottomSheetDefaults.DragHandle(color = Color.Gray) }
            ) {
                CardNuevoEvento(
                    titulo = model.tituloNuevoEvento,
                    onTituloChange = { myViewModel.cambiarTituloNuevoEvento(it) },
                    descripcion = model.descripcionNuevoEvento,
                    onDescripcionChange = { myViewModel.cambiarDescripcionNuevoEvento(it) },
                    fecha = model.fechaNuevoEvento,
                    abrirSelector = { mostrarPicker = true },
                    onCrearEvento = {
                        myViewModel.crearEvento(EventosSolicitud(model.tituloNuevoEvento,model.descripcionNuevoEvento,model.fechaNuevoEventoBd))
                        cambiarSheet(false)
                        myViewModel.resetearModelEvento()
                    }
                )
            }
        }

        SelectorFechaConHora(
            mostrarPicker,
            {
                mostrarPicker = false
            },
            { myViewModel.cambiarFechaNuevoEvento(it) }
        )
    }
}

@Composable
fun CardEvento(evento: Evento) {
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Row(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color(0xFF252525)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.DateRange,
                    "",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    evento.titulo,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = Bold
                )
                Text(
                    evento.descripcion,
                    color = Color.Gray,
                    fontSize = 13.sp,
                    maxLines = 2,
                    lineHeight = 18.sp
                )
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        evento.fechaEvento,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = Bold
                    )
                    Spacer(Modifier.width(8.dp))
                    Box(
                        Modifier
                            .size(4.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Presencial",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun cardDia(
    seleccionarDia: () -> Unit,
    nombreDelMes: String,
    diaDelMes: String,
    seleccionado: Boolean
) {

    Card(
        Modifier
            .size(width = 55.dp, height = 70.dp)
            .clickable {
                seleccionarDia()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (seleccionado) Color.White else Color(0xFF1E1E1E)
        ),
        border = if (seleccionado) null else BorderStroke(1.dp, Color(0xFF333333))
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                nombreDelMes.substring(0, 3).uppercase(),
                fontSize = 10.sp,
                color = if (seleccionado) Color.Black else Color.Gray,
                fontWeight = Bold
            )
            Text(
                diaDelMes,
                fontSize = 18.sp,
                color = if (seleccionado) Color.Black else Color.White,
                fontWeight = Bold
            )
        }
    }
}

@Composable
fun CardNuevoEvento(
    titulo: String,
    onTituloChange: (String) -> Unit,
    descripcion: String,
    onDescripcionChange: (String) -> Unit,
    fecha: String,
    abrirSelector: () -> Unit,
    onCrearEvento: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(25.dp)
    ) {
        Text(
            "Nuevo Evento",
            fontSize = 25.sp,
            fontWeight = Bold,
            color = Color.White

        )
        Spacer(Modifier.height(25.dp))

        Text("Título", color = Color.Gray, fontSize = 14.sp)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = titulo,
            onValueChange = onTituloChange,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedTextColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedBorderColor = Color(0xFF333333),
                focusedBorderColor = Color.White,
                cursorColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(Modifier.height(25.dp))

        Text("Descripción", color = Color.Gray, fontSize = 14.sp)
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            descripcion,
            onDescripcionChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedTextColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedBorderColor = Color(0xFF333333),
                focusedBorderColor = Color.White,
                cursorColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(25.dp))

        Text("Fecha del evento", color = Color.Gray, fontSize = 14.sp)
        Spacer(Modifier.height(12.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .height(45.dp)
                .background(Color(0xFF1E1E1E), RoundedCornerShape(12.dp))
                .border(1.dp, Color(0xFF333333), RoundedCornerShape(12.dp))
                .clickable { abrirSelector() }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (fecha.isEmpty()) "Seleccionar..." else fecha,
                    color = if (fecha.isEmpty()) Color.Gray else Color.White,
                    fontSize = 14.sp
                )
                Icon(
                    Icons.Default.DateRange,
                    "",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(Modifier.height(40.dp))

        Card(
            Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .clickable(onClick = onCrearEvento),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Crear Evento",
                    color = Color.Black,
                    fontWeight = Bold,
                    fontSize = 16.sp
                )
            }
        }
        Spacer(Modifier.height(15.dp))
    }
}

@Composable
fun SelectorFechaConHora(
    mostrar: Boolean,
    cerrarSelector: () -> Unit,
    confirmarFecha: (String) -> Unit
) {
    WheelDateTimePickerView(
        showDatePicker = mostrar,
        height = 250.dp,
        rowCount = 5,
        yearsRange = LocalDateTime.now().year..2030,
        onDoneClick = { fecha ->
            confirmarFecha(fecha.toString())
            cerrarSelector()
        },
        onDismiss = cerrarSelector,
        title = "Fecha del evento",
        doneLabel = "Aceptar",
        shape = RoundedCornerShape(12.dp),
        showMonthAsNumber = false
    )
}
//@Preview(showBackground = true)
//@Composable
//fun prevw() {
//    PantallaEventos()
//}