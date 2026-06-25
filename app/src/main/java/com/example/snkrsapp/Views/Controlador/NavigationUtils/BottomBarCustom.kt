package com.example.snkrsapp.Views.Controlador.NavigationUtils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.snkrsapp.R
import com.example.snkrsapp.ui.theme.ColorNeutroFondo
import com.example.snkrsapp.ui.theme.ColorPrimario
import com.example.snkrsapp.ui.theme.ColorTextoSecundario


@Composable
fun BottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        color = ColorPrimario,
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 2.dp,
        shadowElevation = 8.dp
    ) {
        NavigationBar(
            modifier = Modifier.height(65.dp),
            tonalElevation = 0.dp,
            containerColor = Color.Transparent
        ) {
            NavigationBarItem(
                selected = currentRoute == "Principal",
                onClick = {
                    navController.navigate("Principal")
                },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(width = 52.dp, height = 34.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (currentRoute == "Principal") Color.LightGray
                                else Color.Transparent
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Home,
                            "",
                            tint = if (currentRoute == "Principal") ColorTextoSecundario else Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
            NavigationBarItem(
                selected = currentRoute == "Eventos",
                onClick = {
                    navController.navigate("Eventos")
                },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(width = 52.dp, height = 34.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (currentRoute == "Eventos") Color.LightGray
                                else Color.Transparent
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.DateRange,
                            "",
                            tint = if (currentRoute == "Eventos") ColorTextoSecundario else Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )

            )
            NavigationBarItem(
                selected = currentRoute == "AgregarProducto",
                onClick = {
                    navController.navigate("AgregarProducto")
                },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(width = 52.dp, height = 34.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (currentRoute == "AgregarProducto") Color.LightGray
                                else Color.Transparent
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painterResource(R.drawable.editperson),
                            "",
                            tint = if (currentRoute == "AgregarProducto") ColorTextoSecundario else Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
            NavigationBarItem(
                selected = currentRoute == "Listados/0",
                onClick = {
                    navController.navigate("Listados/0")
                },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(width = 52.dp, height = 34.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (currentRoute?.startsWith("Listados") == true) Color.LightGray
                                else Color.Transparent
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Favorite,
                            "",
                            tint = if (currentRoute?.startsWith("Listados") == true) ColorTextoSecundario else Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
            NavigationBarItem(
                selected = currentRoute == "Conversaciones",
                onClick = {
                    navController.navigate("Conversaciones")
                },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(width = 52.dp, height = 34.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (currentRoute == "Conversaciones") Color.LightGray
                                else Color.Transparent
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Email,
                            "",
                            tint = if (currentRoute == "Conversaciones") ColorTextoSecundario else Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
            NavigationBarItem(
                selected = currentRoute == "Perfil",
                onClick = {
                    navController.navigate("Perfil")
                },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(width = 52.dp, height = 34.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (currentRoute == "Perfil") Color.LightGray
                                else Color.Transparent
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            "",
                            tint = if (currentRoute == "Perfil") ColorTextoSecundario else Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun BottomBarAdmin(navController: NavController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        color = ColorPrimario,
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 2.dp,
        shadowElevation = 8.dp
    ) {
        NavigationBar(
            modifier = Modifier.height(65.dp),
            tonalElevation = 0.dp,
            containerColor = Color.Transparent
        ) {

            NavigationBarItem(
                selected = currentRoute == "UsuariosAdmin",
                onClick = { navController.navigate("UsuariosAdmin") },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(width = 52.dp, height = 34.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (currentRoute == "UsuariosAdmin") Color.LightGray
                                else Color.Transparent
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            "",
                            tint = if (currentRoute == "UsuariosAdmin") ColorTextoSecundario else Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )

            NavigationBarItem(
                selected = currentRoute == "MarcasAdmin",
                onClick = { navController.navigate("MarcasAdmin") },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(width = 52.dp, height = 34.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (currentRoute == "MarcasAdmin") Color.LightGray
                                else Color.Transparent
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Menu,
                            "",
                            tint = if (currentRoute == "MarcasAdmin") ColorTextoSecundario else Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )
            NavigationBarItem(
                selected = currentRoute == "EventosAdmin",
                onClick = { navController.navigate("EventosAdmin") },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(width = 52.dp, height = 34.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (currentRoute == "EventosAdmin") Color.LightGray
                                else Color.Transparent
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Menu,
                            "",
                            tint = if (currentRoute == "EventosAdmin") ColorTextoSecundario else Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )
            NavigationBarItem(
                selected = currentRoute == "IncidenciasAdmin",
                onClick = { navController.navigate("IncidenciasAdmin") },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(width = 52.dp, height = 34.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (currentRoute == "IncidenciasAdmin") Color.LightGray
                                else Color.Transparent
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Email,
                            "",
                            tint = if (currentRoute == "IncidenciasAdmin") ColorTextoSecundario else Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )
            NavigationBarItem(
                selected = currentRoute == "PerfilAdmin",
                onClick = { navController.navigate("PerfilAdmin") },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(width = 52.dp, height = 34.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (currentRoute == "PerfilAdmin") Color.LightGray
                                else Color.Transparent
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            "",
                            tint = if (currentRoute == "PerfilAdmin") ColorTextoSecundario else Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )
        }
    }
}
