package com.example.snkrsapp.Views.Controlador.NavigationUtils

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.snkrsapp.R

@Composable
fun BotonCrearEvento(navController: NavController, crearEvento: () -> Unit) {

    FloatingActionButton(
        onClick = {
            crearEvento()
        },
        containerColor = Color(0xFF1E1E1E),
        shape = RoundedCornerShape(18.dp),
    ) {

        Icon(
            painterResource(R.drawable.calendaraddfillout),
            "",
            tint = Color.White,
            modifier = Modifier.size(26.dp)
        )
    }

}