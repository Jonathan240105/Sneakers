package com.example.snkrsapp.Views.Pantallas

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.snkrsapp.Views.ViewModels.ViewmodelAgregarProducto
import java.io.File

@Composable
fun SelectorImagen(
    myViewModel: ViewmodelAgregarProducto,
    esColeccion: Boolean
) {
    val context = LocalContext.current
    val model by myViewModel.model.collectAsState()

    var mostrarOpciones by remember { mutableStateOf(false) }
    var uriCamaraTemporal by remember { mutableStateOf<Uri?>(null) }

    val launcherGaleria = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { myViewModel.subirFotoACloudinary(it) }
    }

    val launcherCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito ->
        if (exito) {
            uriCamaraTemporal?.let { myViewModel.subirFotoACloudinary(it) }
        }
    }

    val launcherPermisoCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            val archivo = File.createTempFile("snkrs_foto_", ".jpg", context.cacheDir)

            val uriSegura = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                archivo
            )

            uriCamaraTemporal = uriSegura
            launcherCamara.launch(uriSegura)
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = if (esColeccion) "Imagen de la zapatilla" else "Imagen real del par",
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
                    color = if (!model.errorImagen.isNullOrBlank()) Color(0x1AFF5252) else Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
                .border(
                    width = 1.dp,
                    color = if (!model.errorImagen.isNullOrBlank()) Color.Red else Color(0xFF333333),
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable(enabled = model.cargandoImagen != true) { mostrarOpciones = true },
            contentAlignment = Alignment.Center
        ) {
            if (model.cargandoImagen == true) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Color.White)
                    Spacer(Modifier.height(8.dp))
                    Text("Subiendo a Cloudinary...", color = Color.Gray, fontSize = 12.sp)
                }
            } else if (!model.urlImagenNuevaPublicacion.isNullOrBlank()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Imagen cargada con éxito",
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

        if (!model.errorImagen.isNullOrBlank()) {
            Spacer(Modifier.height(6.dp))
            Text(
                text = model.errorImagen!!,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }

    if (mostrarOpciones) {
        AlertDialog(
            onDismissRequest = { mostrarOpciones = false },
            containerColor = Color(0xFF1E1E1E),
            title = { Text("Añadir imagen", color = Color.White, fontWeight = Bold) },
            text = {
                Text(
                    "Elige cómo quieres capturar la foto de tus sneakers:",
                    color = Color.Gray
                )
            },
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