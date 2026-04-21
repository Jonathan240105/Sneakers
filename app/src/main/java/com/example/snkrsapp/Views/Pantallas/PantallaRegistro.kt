package com.example.snkrsapp.Views.Pantallas

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.ExtraBold
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.snkrsapp.Views.ViewModels.RegistroViewModel
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerView

@Composable
fun PantallaRegistro(myViewModel: RegistroViewModel) {

    var paso by remember { mutableIntStateOf(0) }
    var nombreUsuario by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contra by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var mostrarSelectorFecha by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .background(Color(0xFF121212))
            .padding(top = 40.dp)
    ) {
        Spacer(Modifier.height(50.dp))
        TituloRegistro("¡Unete a nosotros!")
        Spacer(Modifier.height(50.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(3) { index ->
                val color = if (paso == index) Color.White else Color.Gray
                Box(
                    Modifier
                        .padding(5.dp)
                        .size(width = if (paso == index) 24.dp else 8.dp, height = 8.dp)
                        .background(color, RoundedCornerShape(10.dp))
                )
            }
        }
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
                .fillMaxSize()
                .padding(16.dp), label = "stepAnimation"
        ) { pasoActual ->
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color(0xFF1E1E1E), RoundedCornerShape(24.dp))
                    .padding(16.dp)
            ) {
                when (pasoActual) {

                    0 -> PrimerPaso(email, { email = it }, contra, { contra = it }, { paso++ })
                    1 -> SegundoPaso(
                        nombreUsuario,
                        { nombreUsuario = it },
                        apellidos,
                        { apellidos = it },
                        { paso++ },
                        { paso-- })

                    2 -> {
                        TercerPaso(
                            fecha,
                            { mostrarSelectorFecha = true },
                            { paso-- },
                            {
                                myViewModel.registrarUsuario(
                                    email,
                                    contra,
                                    nombreUsuario,
                                    apellidos,
                                    fecha
                                )
                            })
                        SelectorFecha(
                            mostrarSelectorFecha,
                            { mostrarSelectorFecha = false },
                            { fecha = it })
                    }
                }
            }
        }

    }
}

@Composable
fun TituloRegistro(titulo: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(titulo, fontSize = 32.sp, color = Color.White, fontWeight = ExtraBold, modifier = Modifier.testTag("tituloRegistro"))
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
    Column(
        Modifier.fillMaxSize()
    ) {
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
                .fillMaxWidth().testTag("textFieldContra"),
            trailingIcon = { Icon(Icons.Default.Lock, "") },
            visualTransformation = PasswordVisualTransformation(),
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
        Spacer(Modifier.weight(1f))
        Button(
            siguientePaso,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                disabledContainerColor = Color.DarkGray,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("botonPrimerPaso"),
            shape = RoundedCornerShape(12.dp)
        ) { Text("Continuar", fontWeight = FontWeight.Bold) }
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
    Column(
        Modifier.fillMaxSize()
    ) {
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
        Spacer(Modifier.weight(1f))
        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = volverPaso,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .testTag("botonVolver1")
            ) {
                Text("Volver")
            }
            Button(
                siguientePaso,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .testTag("botonSegundoPaso"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
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
    registrarUsuario: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
    ) {
        Spacer(Modifier.height(15.dp))
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
            modifier = Modifier.clickable { abrirSelector() }.testTag("SelectorFecha"))

        Spacer(Modifier.weight(1f))
        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                volver, Modifier
                    .weight(1f)
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
                registrarUsuario,
                Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Finalizar") }
        }
    }
}

@Composable
fun SelectorFecha(
    mostrar: Boolean, cerrarSelectorFecha: () -> Unit, confirmarFecha: (String) -> Unit
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
        onDismiss = cerrarSelectorFecha,
        title = "Fecha de nacimiento",
        doneLabel = "Aceptar",
        shape = RoundedCornerShape(12.dp),
        showMonthAsNumber = false

    )
}

//@Preview(showBackground = true)
//@Composable
//fun previeww() {
//    PantallaRegistro()
//}