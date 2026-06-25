package com.example.snkrsapp.Views.Pantallas

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.snkrsapp.Domain.Incidencia
import com.example.snkrsapp.Views.Controlador.NavigationUtils.BottomBarAdmin
import com.example.snkrsapp.Views.ViewModels.IncidenciasAdminViewModel
import com.example.snkrsapp.ui.theme.ColorBordeTextField
import com.example.snkrsapp.ui.theme.ColorNeutroFondo
import com.example.snkrsapp.ui.theme.ColorPrimario
import com.example.snkrsapp.ui.theme.ColorTextoSecundario
import com.example.snkrsapp.ui.theme.miTipografia

@Composable
fun PantallaIncidenciasAdmin(
    myViewModel: IncidenciasAdminViewModel,
    paddingValues: PaddingValues,
    navController: NavController
) {
    val model by myViewModel.model.collectAsState()
    val contexto = LocalContext.current
    var incidenciaSeleccionada by remember { mutableStateOf<Incidencia?>(null) }

    LaunchedEffect(Unit) {
        myViewModel.cargarIncidencias()
    }

    LaunchedEffect(model.mensaje) {
        if (!model.mensaje.isNullOrBlank()) {
            Toast.makeText(contexto, model.mensaje, Toast.LENGTH_SHORT).show()
            myViewModel.limpiarMensaje()
        }
    }

    Scaffold(
        containerColor = ColorNeutroFondo,
        bottomBar = { BottomBarAdmin(navController) }
    ) { paddingScaffold ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingScaffold)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(24.dp))
            Text(
                "Incidencias",
                color = ColorPrimario,
                fontFamily = miTipografia,
                fontSize = 28.sp,
                fontWeight = Bold
            )
            Spacer(Modifier.height(16.dp))

            if (model.cargando) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = ColorPrimario)
                }
            } else if (model.incidencias.isEmpty()) {
                MensajeListadoVacio("No hay incidencias pendientes.")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = paddingValues.calculateBottomPadding() + 90.dp)
                ) {
                    items(model.incidencias) { incidencia ->
                        CardIncidenciaResumen(
                            incidencia = incidencia,
                            onClick = { incidenciaSeleccionada = incidencia }
                        )
                    }
                }
            }
        }
    }

    incidenciaSeleccionada?.let { incidencia ->
        DialogIncidenciaAdmin(
            incidencia = incidencia,
            respondiendo = model.idRespondiendo == incidencia.idIncidencia,
            onDismiss = { incidenciaSeleccionada = null },
            onAceptar = { myViewModel.responderIncidencia(incidencia, true) },
            onRechazar = { myViewModel.responderIncidencia(incidencia, false) }
        )
    }
}

@Composable
fun CardIncidenciaResumen(incidencia: Incidencia, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, ColorBordeTextField),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = incidencia.urlImagen,
                contentDescription = incidencia.modelo,
                modifier = Modifier
                    .size(70.dp)
                    .background(Color.Black, RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(incidencia.modelo, color = ColorPrimario, fontFamily = miTipografia, fontWeight = Bold)
                Text("Comprador: ${incidencia.nombreComprador}", color = ColorTextoSecundario, fontFamily = miTipografia, fontSize = 13.sp)
                Text("Estado: ${incidencia.estado}", color = ColorTextoSecundario, fontFamily = miTipografia, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun DialogIncidenciaAdmin(
    incidencia: Incidencia,
    respondiendo: Boolean,
    onDismiss: () -> Unit,
    onAceptar: () -> Unit,
    onRechazar: () -> Unit
) {
    val estadoNormalizado = incidencia.estado.lowercase()
    val incidenciaResuelta = estadoNormalizado == "aceptada" || estadoNormalizado == "rechazada"

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, ColorBordeTextField)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Detalle de incidencia",
                        color = ColorPrimario,
                        fontFamily = miTipografia,
                        fontSize = 20.sp,
                        fontWeight = Bold,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = ColorTextoSecundario
                        )
                    }
                }

                AsyncImage(
                    model = incidencia.urlImagen,
                    contentDescription = incidencia.modelo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Color.Black, RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Fit
                )

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(incidencia.modelo, color = ColorPrimario, fontFamily = miTipografia, fontWeight = Bold)
                    Text("Comprador: ${incidencia.nombreComprador}", color = ColorTextoSecundario, fontFamily = miTipografia)
                    Text("Vendedor: ${incidencia.nombreVendedor}", color = ColorTextoSecundario, fontFamily = miTipografia)
                    Text("${incidencia.color} - Talla ${incidencia.talla} - Cantidad ${incidencia.cantidad}", color = ColorTextoSecundario, fontFamily = miTipografia)
                    Text("Motivo: ${incidencia.tipo}", color = ColorTextoSecundario, fontFamily = miTipografia)
                }

                HorizontalDivider(color = ColorBordeTextField)

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        "Descripción",
                        color = ColorPrimario,
                        fontFamily = miTipografia,
                        fontWeight = Bold
                    )
                    Text(
                        incidencia.descripcion,
                        color = ColorTextoSecundario,
                        fontFamily = miTipografia
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onRechazar,
                        enabled = !respondiendo && !incidenciaResuelta,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0392B))
                    ) {
                        Text(
                            "Razón al vendedor",
                            color = Color.White,
                            fontFamily = miTipografia,
                            fontWeight = Bold,
                            fontSize = 12.sp
                        )
                    }
                    Button(
                        onClick = onAceptar,
                        enabled = !respondiendo && !incidenciaResuelta,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = ColorPrimario)
                    ) {
                        Text(
                            "Razón al comprador",
                            color = Color.White,
                            fontFamily = miTipografia,
                            fontWeight = Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
