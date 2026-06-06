package com.example.snkrsapp.Views.Pantallas

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.ExtraBold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.snkrsapp.Views.ViewModels.RegistroViewModel
import com.example.snkrsapp.Views.ViewModels.ViewmodelAgregarProducto
import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerView
import java.io.File


@Composable
fun PantallaRegistro(
    myViewModel: RegistroViewModel,
    agregarProductoViewModel: ViewmodelAgregarProducto,
    volverAInicioSesion: () -> Unit
) {

    var paso by remember { mutableIntStateOf(0) }
    var mostrarSelectorFecha by remember { mutableStateOf(false) }

    val model by myViewModel.model.collectAsState()
    val model2 by agregarProductoViewModel.model.collectAsState()

    LaunchedEffect(model.exito) {
        if (model.exito) {
            volverAInicioSesion()
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .background(Color(0xFF121212))
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Espacio superior aumentado para bajar la cabecera
        Spacer(Modifier.height(65.dp))

        // --- CABECERA REESTRUCTURADA (CANCELAR Y TÍTULO) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Cancelar",
                color = Color.Gray,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clickable {
                        volverAInicioSesion()
                        myViewModel.resetearPantalla()
                    }
                    .padding(8.dp)
            )

        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = "¡Únete!",
            fontSize = 24.sp,
            color = Color.White,
            fontWeight = ExtraBold,
            modifier = Modifier.testTag("tituloRegistro")
        )

        Spacer(Modifier.height(20.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(4) { index ->
                val color = if (paso == index) Color.White else Color.Gray
                Box(
                    Modifier
                        .padding(5.dp)
                        .size(width = if (paso == index) 24.dp else 8.dp, height = 8.dp)
                        .background(color, RoundedCornerShape(10.dp))
                )
            }
        }

        Spacer(Modifier.weight(1f))

        AnimatedContent(
            targetState = paso,
            transitionSpec = {
                if (targetState > initialState) {
                    slideInHorizontally { width -> width } togetherWith slideOutHorizontally { width -> -width }
                } else {
                    slideInHorizontally { width -> -width } togetherWith slideOutHorizontally { width -> width }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = "stepAnimation"
        ) { pasoActual ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                when (pasoActual) {

                    0 -> PrimerPaso(
                        model.email,
                        { myViewModel.cambiarEmail(it) },
                        model.contra,
                        { myViewModel.cambiarContra(it) },
                        { paso++ })

                    1 -> SegundoPaso(
                        model.nombreUsuario,
                        { myViewModel.cambiarNombreUsuario(it) },
                        model.apellidos,
                        { myViewModel.cambiarApellidos(it) },
                        { paso++ },
                        { paso-- })

                    2 -> {
                        TercerPaso(
                            model.fecha,
                            { mostrarSelectorFecha = true },
                            { paso-- },
                            { paso++ }
                        )
                        SelectorFecha(
                            mostrarSelectorFecha,
                            { mostrarSelectorFecha = false },
                            { myViewModel.cambiarFecha(it) },
                            LocalDate(1926, 1, 1),
                            LocalDate(2026, 12, 31)
                        )
                    }

                    3 -> {
                        CuartoPasoFotoPerfil(
                            cargandoImagen = model2.cargandoImagen == true,
                            urlImagen = model2.urlImagenNuevaPublicacion,
                            errorImagen = model2.errorImagen,
                            onSubirImagen = {
                                agregarProductoViewModel.subirFotoACloudinary(it)
                            },
                            volver = { paso-- },
                            onFinalizar = {
                                myViewModel.registrarUsuario(
                                    model.email,
                                    model.contra,
                                    model.nombreUsuario,
                                    model.apellidos,
                                    model.fecha,
                                    model2.urlImagenNuevaPublicacion ?: ""
                                )
                            }
                        )
                    }
                }
            }
        }

        if (model.intentadoRegistrar && !model.cargando) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                if (!model.error.isNullOrBlank()) {
                    Text(
                        model.error ?: "",
                        color = Color(0xFFEF5350),
                        fontSize = 14.sp,
                        fontWeight = Bold,
                        modifier = Modifier.testTag("errorRegistroMensaje")
                    )
                } else if (model.errorFirebase) {
                    Text(
                        "Error de autenticación con Firebase. Inténtalo de nuevo.",
                        color = Color(0xFFEF5350),
                        fontSize = 14.sp,
                        fontWeight = Bold
                    )
                } else if (!model.exito && !model.cargando && (model.email.isNotEmpty() && paso == 3)) {
                    Text(
                        "Error de conexión con el servidor.",
                        color = Color(0xFFEF5350),
                        fontSize = 14.sp,
                        fontWeight = Bold
                    )
                }
            }
        }
        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun PrimerPaso(
    email: String,
    cambiarEmail: (String) -> Unit,
    contra: String,
    cambiarContra: (String) -> Unit,
    siguientePaso: () -> Unit
) {
    val camposCompletados = email.isNotBlank() && contra.isNotBlank()

    Column(Modifier.fillMaxSize()) {
        Spacer(Modifier.height(15.dp))
        OutlinedTextField(
            email,
            cambiarEmail,
            Modifier
                .fillMaxWidth()
                .testTag("textFieldEmail"),
            trailingIcon = { Icon(Icons.Default.Email, "") },
            shape = RoundedCornerShape(12.dp),
            label = { Text("Email") },
            placeholder = { Text("ejemplo@gmail.com") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.LightGray,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            )
        )
        Spacer(Modifier.height(45.dp))
        OutlinedTextField(
            contra,
            cambiarContra,
            Modifier
                .fillMaxWidth()
                .testTag("textFieldContra"),
            trailingIcon = { Icon(Icons.Default.Lock, "") },
            label = { Text("Contraseña") },
            placeholder = { Text("123abc456") },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.LightGray,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            )
        )
        Spacer(Modifier.height(15.dp))
        Row(Modifier.fillMaxWidth()) {
            Text("MInimo 8 caracteres", color = Color.Gray, modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(50.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = siguientePaso,
                enabled = camposCompletados,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    disabledContainerColor = Color(0xFF2E2E2E),
                    contentColor = Color.Black,
                    disabledContentColor = Color.Gray
                ),
                modifier = Modifier
                    .width(130.dp)
                    .height(56.dp)
                    .testTag("botonPrimerPaso"),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Continuar", fontWeight = FontWeight.Bold) }
        }
    }
}

@Composable
fun SegundoPaso(
    nombreUsuario: String,
    cambiarNombreUsuario: (String) -> Unit,
    apellidos: String,
    cambiarApellidos: (String) -> Unit,
    siguientePaso: () -> Unit,
    volverPaso: () -> Unit
) {
    val camposCompletados = nombreUsuario.isNotBlank() && apellidos.isNotBlank()

    Column(Modifier.fillMaxSize()) {
        Spacer(Modifier.height(15.dp))
        OutlinedTextField(
            nombreUsuario,
            cambiarNombreUsuario,
            Modifier
                .fillMaxWidth()
                .testTag("textFieldNombreUsuario"),
            trailingIcon = { Icon(Icons.Default.Person, "") },
            label = { Text("Nombre de usuario") },
            placeholder = { Text("usuario123") },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.LightGray,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            )
        )
        Spacer(Modifier.height(45.dp))
        OutlinedTextField(
            apellidos,
            cambiarApellidos,
            Modifier
                .fillMaxWidth()
                .testTag("textFieldApellidos"),
            trailingIcon = { Icon(Icons.Default.Face, "") },
            label = { Text("Apellidos") },
            placeholder = { Text("apellido1") },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.LightGray,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            )
        )
        Spacer(Modifier.height(50.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = volverPaso,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .width(130.dp)
                    .height(56.dp)
                    .testTag("botonVolver1")
            ) {
                Text("Volver")
            }
            Button(
                onClick = siguientePaso,
                enabled = camposCompletados,
                modifier = Modifier
                    .width(130.dp)
                    .height(56.dp)
                    .testTag("botonSegundoPaso"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    disabledContainerColor = Color(0xFF2E2E2E),
                    contentColor = Color.Black,
                    disabledContentColor = Color.Gray
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Continuar", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun TercerPaso(
    fecha: String,
    abrirSelector: () -> Unit,
    volver: () -> Unit,
    siguientePaso: () -> Unit
) {
    val fechaSeleccionada = fecha.isNotBlank()

    Column(Modifier.fillMaxSize()) {
        Spacer(Modifier.height(35.dp))
        OutlinedTextField(
            fecha,
            {},
            label = { Text("Fecha de nacimiento") },
            placeholder = { Text("dd/mm/aaaa") },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedTextColor = Color.White,
                focusedTextColor = Color.White,
                disabledTextColor = Color.White,
                disabledBorderColor = Color.LightGray,
                disabledTrailingIconColor = Color.White,
                disabledPlaceholderColor = Color.White,
                focusedLabelColor = Color.White,
                disabledLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            ),
            trailingIcon = { Icon(Icons.Default.DateRange, "") },
            shape = RoundedCornerShape(12.dp),
            readOnly = true,
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { abrirSelector() }
                .testTag("SelectorFecha")
        )

        Spacer(Modifier.height(60.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                volver, Modifier
                    .width(130.dp)
                    .height(56.dp)
                    .testTag("botonVolver2"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Volver")
            }
            Button(
                onClick = siguientePaso,
                enabled = fechaSeleccionada,
                modifier = Modifier
                    .width(130.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    disabledContainerColor = Color(0xFF2E2E2E),
                    contentColor = Color.Black,
                    disabledContentColor = Color.Gray
                ),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Continuar", fontWeight = FontWeight.Bold) }
        }
    }
}

@Composable
fun CuartoPasoFotoPerfil(
    cargandoImagen: Boolean,
    urlImagen: String?,
    errorImagen: String?,
    onSubirImagen: (Uri) -> Unit,
    volver: () -> Unit,
    onFinalizar: () -> Unit
) {
    val context = LocalContext.current
    var mostrarOpciones by remember { mutableStateOf(false) }
    var uriCamaraTemporal by remember { mutableStateOf<Uri?>(null) }

    val launcherGaleria = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { onSubirImagen(it) }
    }

    val launcherCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito ->
        if (exito) {
            uriCamaraTemporal?.let { onSubirImagen(it) }
        }
    }

    val launcherPermisoCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            val archivo = File.createTempFile("avatar_registro_", ".jpg", context.cacheDir)
            val uriSegura = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                archivo
            )
            uriCamaraTemporal = uriSegura
            launcherCamara.launch(uriSegura)
        }
    }

    Column(Modifier.fillMaxSize()) {
        Text(
            text = "Foto de Perfil",
            color = Color.Gray,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(Color(0xFF1E1E1E), RoundedCornerShape(12.dp))
                .background(
                    color = if (!errorImagen.isNullOrBlank()) Color(0x1AFF5252) else Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
                .border(
                    width = 1.dp,
                    color = if (!errorImagen.isNullOrBlank()) Color.Red else Color(0xFF333333),
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable(enabled = !cargandoImagen) { mostrarOpciones = true },
            contentAlignment = Alignment.Center
        ) {
            if (cargandoImagen) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Color.White)
                    Spacer(Modifier.height(8.dp))
                    Text("Subiendo avatar...", color = Color.Gray, fontSize = 12.sp)
                }
            } else if (!urlImagen.isNullOrBlank()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Foto subida con éxito",
                        color = Color.White,
                        fontWeight = Bold,
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text("Pulsa para cambiar la foto", color = Color.Gray, fontSize = 12.sp)
                }
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("+ Añadir Foto", color = Color.White, fontWeight = Bold, fontSize = 15.sp)
                    Spacer(Modifier.height(4.dp))
                    Text("Cámara o Galería", color = Color.Gray, fontSize = 12.sp)
                }
            }
        }

        if (!errorImagen.isNullOrBlank()) {
            Spacer(Modifier.height(6.dp))
            Text(
                text = errorImagen,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }

        Spacer(Modifier.height(40.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = volver,
                modifier = Modifier
                    .width(130.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Volver")
            }
            Button(
                onClick = onFinalizar,
                enabled = !cargandoImagen,
                modifier = Modifier
                    .width(130.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    disabledContainerColor = Color(0xFF2E2E2E),
                    contentColor = Color.Black,
                    disabledContentColor = Color.Gray
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Finalizar")
            }
        }
    }

    if (mostrarOpciones) {
        AlertDialog(
            onDismissRequest = { mostrarOpciones = false },
            containerColor = Color(0xFF1E1E1E),
            title = { Text("Añadir imagen", color = Color.White, fontWeight = Bold) },
            text = { Text("Elige cómo quieres capturar tu foto de perfil:", color = Color.Gray) },
            confirmButton = {
                TextButton(onClick = {
                    mostrarOpciones = false
                    launcherPermisoCamara.launch(Manifest.permission.CAMERA)
                }) {
                    Text("Cámara", color = Color.White, fontWeight = Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    mostrarOpciones = false
                    launcherGaleria.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }) {
                    Text("Galería", color = Color.Gray)
                }
            }
        )
    }
}

@Composable
fun SelectorFecha(
    mostrar: Boolean,
    cerrarSelectorFecha: () -> Unit,
    confirmarFecha: (String) -> Unit,
    fechaMinima: LocalDate,
    fechaMaxima: LocalDate
) {
    WheelDatePickerView(
        showDatePicker = mostrar,
        height = 200.dp,
        rowCount = 3,
        yearsRange = 1920..2026,
        onDoneClick = { fecha ->
            confirmarFecha(fecha.toString())
            cerrarSelectorFecha()
        },
        minDate = fechaMinima,
        maxDate = fechaMaxima,
        onDismiss = cerrarSelectorFecha,
        title = "Fecha de nacimiento",
        doneLabel = "Aceptar",
        shape = RoundedCornerShape(12.dp),
        showMonthAsNumber = false
    )
}