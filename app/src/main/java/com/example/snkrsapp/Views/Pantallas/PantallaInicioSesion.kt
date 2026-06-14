package com.example.snkrsapp.Views.Pantallas

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.snkrsapp.R
import com.example.snkrsapp.Views.ViewModels.InicioSesionViewModel
import com.example.snkrsapp.ui.theme.ColorAcento
import com.example.snkrsapp.ui.theme.ColorAlerta
import com.example.snkrsapp.ui.theme.ColorNeutroFondo
import com.example.snkrsapp.ui.theme.ColorPrimario
import com.example.snkrsapp.ui.theme.ColorTextFieldNoSeleccionado
import com.example.snkrsapp.ui.theme.ColorTextFieldSeleccionado
import com.example.snkrsapp.ui.theme.ColorTextoSecundario
import com.example.snkrsapp.ui.theme.miTipografia
import com.example.snkrsapp.ui.theme.otraTipografia


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
            .background(ColorNeutroFondo)
            .padding(horizontal = 30.dp),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        TextoCentradoLogIn("¡Bienvenido a\nGrails Up!")
        Spacer(Modifier.height(40.dp))
        TextoConTextFieldLogin(
            "Email",
            "textFieldEmail",
            model.email,
            {
                if (it.length <= 50) {
                    myViewModel.cambiarEmail(it)
                }
            },
            { Icon(Icons.Default.Email, "") },
            false
        )
        Spacer(Modifier.height(25.dp))
        TextoConTextFieldLogin(
            "Contraseña",
            "textFieldContra",
            model.contra,
            {
                if (it.length <= 50) {
                    myViewModel.cambiarContra(it)
                }
            },
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
                text = "Crear cuenta",
                fontFamily = miTipografia,
                modifier = Modifier.clickable { cambiarARegistro() },
                color = ColorTextoSecundario,
                fontWeight = Bold,
                fontSize = 18.sp
            )
        }

        if (model.error.isNotBlank()) {
            Text(
                model.error,
                color = ColorAlerta,
                fontFamily = miTipografia,
                fontSize = 14.sp,
                fontWeight = Bold,
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 8.dp)
                    .align(Alignment.Start)
                    .testTag("errorLoginMensaje")
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
            fontSize = 30.sp,
            color = ColorPrimario,
            fontFamily = miTipografia,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 2.sp,
            textAlign = TextAlign.Center,
            lineHeight = 38.sp
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
        label = { Text(texto, fontWeight = Bold, fontSize = 15.sp, fontFamily = miTipografia) },
        visualTransformation = if (esContra && !contraVisible) PasswordVisualTransformation() else VisualTransformation.None,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
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

            cursorColor = Color.White
        )
    )
}

@Composable
fun BotonLogin(texto: String, tag: String, onClick: () -> Unit, habilitado: Boolean) {
    Button(
        onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = ColorAcento,
            disabledContainerColor = ColorAcento.copy(alpha = 0.5f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp)
            .padding(vertical = 15.dp)
            .testTag(tag),
        enabled = habilitado
    ) {
        Text(texto, color = Color.White, fontFamily = miTipografia, fontWeight = Bold, fontSize = 15.sp)
    }
}