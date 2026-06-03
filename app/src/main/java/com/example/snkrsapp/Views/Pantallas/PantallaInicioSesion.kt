package com.example.snkrsapp.Views.Pantallas

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.snkrsapp.R
import com.example.snkrsapp.Views.ViewModels.InicioSesionViewModel


@Composable
fun PantallaInicioSesion(
    myViewModel: InicioSesionViewModel,
    cambiarARegistro: () -> Unit,
    cambiarAListado: () -> Unit,
    cambiarAAdmin: () -> Unit
) {

    val model by myViewModel.model.collectAsState()

    LaunchedEffect(model.exito) {
        if (model.exito) {
            if (model.usuario.esAdmin == 1) {
                cambiarAAdmin()
            } else {
                cambiarAListado()
            }
            myViewModel.resetearEstadoPantalla()
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(horizontal = 30.dp),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        TextoCentradoLogIn("Bienvenido!")
        Spacer(Modifier.height(40.dp))
        TextoConTextFieldLogin(
            "Email",
            "textFieldEmail",
            model.email,
            { myViewModel.cambiarEmail(it) },
            { Icon(Icons.Default.Email, "") },
            false
        )
        Spacer(Modifier.height(25.dp))
        TextoConTextFieldLogin(
            "Contraseña",
            "textFieldContra",
            model.contra,
            { myViewModel.cambiarContra(it) },
            { Icon(Icons.Default.Lock, "") },
            true
        )
        Spacer(Modifier.height(30.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                "Crear cuenta",
                Modifier.clickable { cambiarARegistro() },
                color = Color.White,
            )
        }

        if (!model.error.isNullOrBlank()) {
            Text(
                model.error,
                color = Color(0xFFEF5350),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 8.dp)
                    .align(Alignment.Start)
                    .testTag("errorLoginMensaje")
            )
        } else if (model.errorFirebase) {
            Text(
                "Credenciales incorrectas",
                color = Color(0xFFEF5350),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 8.dp)
                    .align(Alignment.Start)
            )
        }
        BotonLogin(
            "Iniciar sesión",
            "botonInicioSesion",
            { myViewModel.iniciarSesion(model.email, model.contra) },
            !model.email.isEmpty() && !model.contra.isEmpty()
        )
    }

}

@Composable
fun TextoCentradoLogIn(texto: String) {
    Row(
        Modifier
            .fillMaxWidth(),
        Arrangement.Center
    ) {
        Text(
            texto,
            modifier = Modifier.testTag("tituloInicioSesion"),
            fontSize = 32.sp,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 2.sp
        )
    }
}

@Composable
fun TextoConTextFieldLogin(
    texto: String,
    tag: String,
    textoTexField: String,
    cambiarTextField: (String) -> Unit,
    icon: @Composable () -> Unit,
    esContra: Boolean
) {

    var contraVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = textoTexField,
        onValueChange = cambiarTextField,
        modifier = Modifier
            .fillMaxWidth()
            .testTag(tag),
        leadingIcon = icon,
        singleLine = true,
        trailingIcon = {
            if (esContra) {
                Crossfade(
                    targetState = contraVisible,
                    animationSpec = tween(durationMillis = 300)
                ) { visible ->
                    Icon(
                        painter = painterResource(if (visible) R.drawable.ojoabierto else R.drawable.eyeclose),
                        contentDescription = if (visible) "Ocultar contraseña" else "Mostrar contraseña",
                        modifier = Modifier
                            .clickable { contraVisible = !contraVisible }
                    )
                }
            }
        },
        label = { Text(texto) },
        visualTransformation = if (esContra && !contraVisible) PasswordVisualTransformation() else VisualTransformation.None,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.LightGray,
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.LightGray,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.Gray,
            focusedTrailingIconColor = Color.White,
            unfocusedTrailingIconColor = Color.DarkGray,
            focusedLeadingIconColor = Color.White,
            unfocusedLeadingIconColor = Color.DarkGray
        )
    )
}

@Composable
fun BotonLogin(texto: String, tag: String, onClick: () -> Unit, habilitado: Boolean) {
    Button(
        onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.DarkGray
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp)
            .padding(vertical = 15.dp)
            .testTag(tag),
        enabled = habilitado
    ) {
        Text(texto, color = Color.Black)
    }
}