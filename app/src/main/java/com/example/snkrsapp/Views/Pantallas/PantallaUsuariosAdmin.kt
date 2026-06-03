package com.example.snkrsapp.Views.Pantallas

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.Usuario
import com.example.snkrsapp.Views.Controlador.NavigationUtils.BottomBarAdmin
import com.example.snkrsapp.Views.ViewModels.UsuariosAdminViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PantallaUsuariosAdmin(
    myViewModel: UsuariosAdminViewModel, paddingValues: PaddingValues, navController: NavController
) {

    val model by myViewModel.model.collectAsState()

    val usuariosSeleccionados = remember { mutableStateListOf<String>() }
    val modoBorrado = usuariosSeleccionados.isNotEmpty()

    LaunchedEffect(Unit) {
        myViewModel.cargarUsuarios()
    }

    Scaffold(
        bottomBar = {
            if (modoBorrado) {
                BottomBarBorradoLocal(
                    cantidadSeleccionados = usuariosSeleccionados.size,
                    onBorrar = {
                        myViewModel.eliminarUsuarios(usuariosSeleccionados.toList())
                        usuariosSeleccionados.clear()
                    },
                    onCancelar = { usuariosSeleccionados.clear() })
            } else {
                BottomBarAdmin(
                    navController
                )
            }
        }) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF121212))
                .padding(top = paddingValues.calculateTopPadding())
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(Modifier.height(14.dp))
                TituloEventos("Lista de usuarios")
                Spacer(modifier = Modifier.height(12.dp))
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(model.listaUsuarios, key = { it.UID }) { usuario ->
                    val estaSeleccionado = usuariosSeleccionados.contains(usuario.UID)

                    ItemUsuarioSeleccionable(
                        usuario = usuario,
                        modoBorrado = modoBorrado,
                        estaSeleccionado = estaSeleccionado,
                        onLongClick = {
                            if (!estaSeleccionado) usuariosSeleccionados.add(usuario.UID)
                        },
                        seleccionar = { chequeado ->
                            if (chequeado) {
                                usuariosSeleccionados.add(usuario.UID)
                            } else {
                                usuariosSeleccionados.remove(usuario.UID)
                            }
                        })
                }
            }
        }
    }
}

@Composable
fun BottomBarBorradoLocal(
    cantidadSeleccionados: Int, onBorrar: () -> Unit, onCancelar: () -> Unit
) {
    androidx.compose.material3.Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        color = Color(0xFF1E1E1E),
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$cantidadSeleccionados seleccionados",
                color = Color.White,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                fontSize = 15.sp
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Cancelar", modifier = Modifier.clickable { onCancelar() }, color = Color.White
                )
                IconButton(onClick = onBorrar) {
                    Icon(Icons.Default.Delete, "", tint = Color.White)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemUsuarioSeleccionable(
    usuario: Usuario,
    modoBorrado: Boolean,
    estaSeleccionado: Boolean,
    onLongClick: () -> Unit,
    seleccionar: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = {
                if (modoBorrado) {
                    seleccionar(!estaSeleccionado)
                }
            }, onLongClick = { onLongClick() }), colors = CardDefaults.cardColors(
            containerColor = if (estaSeleccionado) Color(0xFF2A2A2A) else Color(0xFF1E1E1E)
        ), shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                usuario.urlFoto ?: "",
                "",
                modifier = Modifier
                    .size(55.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF252525)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(16.dp))

            Column(
                Modifier.weight(1f)
            ) {
                Text(
                    usuario.nombreUsuario ?: "Sin nombre",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    usuario.email ?: "Sin email", color = Color.Gray, fontSize = 14.sp
                )
            }

            if (modoBorrado) {
                Checkbox(
                    estaSeleccionado, { seleccionar(it) }, colors = CheckboxDefaults.colors(
                        checkedColor = Color.White,
                        uncheckedColor = Color.Gray,
                        checkmarkColor = Color.Black
                    )
                )
            }
        }
    }
}