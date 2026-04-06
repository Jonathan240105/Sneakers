package com.example.snkrsapp.Views.Pantallas

import android.graphics.drawable.Icon
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.snkrsapp.Views.ViewModels.InicioSesionViewModel

@Composable
fun PantallaInicioSesion(myViewModel: InicioSesionViewModel, cambiarAListado: () -> Unit) {

    val model by myViewModel.model.collectAsState()
    var nombreUsuario by remember { mutableStateOf("") }
    var contra by remember { mutableStateOf("") }
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
            nombreUsuario,
            { nombreUsuario = it },
            { Icon(Icons.Default.Email, "") })
        Spacer(Modifier.height(25.dp))
        TextoConTextFieldLogin(
            "Contraseña",
            contra,
            { contra = it },
            { Icon(Icons.Default.Lock, "") })
        Spacer(Modifier.height(30.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                "¿No tienes cuenta?",
                Modifier.clickable { },
                color = Color.White,
            )
        }
        BotonLogin(
            "Iniciar sesión",
            { myViewModel.iniciarSesion(nombreUsuario, contra) },
            !nombreUsuario.isEmpty() && !contra.isEmpty()
        )
        BotonLogin(
            "Invitado",
            { cambiarAListado() },
            !nombreUsuario.isEmpty() && !contra.isEmpty()
        )
    }

}

@Composable
fun TextoCentradoLogIn(texto: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(5.dp),
        Arrangement.Center
    ) {
        Text(
            texto,
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
    textoTexField: String,
    cambiarTextField: (String) -> Unit,
    icon: @Composable () -> Unit
) {
    OutlinedTextField(
        textoTexField, cambiarTextField, Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        label = { Text(texto) },
        trailingIcon = icon,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.LightGray,
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.LightGray
        )
    )
}

@Composable
fun BotonLogin(texto: String, onClick: () -> Unit, habilitado: Boolean) {
    Button(
        onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.DarkGray
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        enabled = habilitado
    ) {
        Text(texto, color = Color.Black)
    }
}