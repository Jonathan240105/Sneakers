package com.example.snkrsapp.Views.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snkrsapp.Data.RemoteData.PublicacionDao.AgregarPublicacionesSolicitud
import com.example.snkrsapp.Data.Repository.ProductoRepository.ProductoRepository
import com.example.snkrsapp.Domain.ModelAgregarProducto
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ViewmodelAgregarProducto @Inject constructor(
    private val productoRepository: ProductoRepository
) : ViewModel() {

    private val _model = MutableStateFlow(ModelAgregarProducto())
    val model = _model.asStateFlow()

    fun cambiarMarca(id: Int, nombre: String) {
        _model.update { it.copy(idMarcaSeleccionada = id, marcaSeleccionada = nombre) }
    }

    fun cambiarNombreNuevaMarca(nombre: String) {
        _model.update { it.copy(nombreNuevaMarcaText = nombre) }
    }

    fun cambiarNombreNuevoProducto(nombre: String) {
        _model.update { it.copy(nombreNuevoProductoText = nombre) }
    }

    fun cambiarPrecio(precio: Double) {
        _model.update { it.copy(precioNuevaPublicacion = precio) }
    }

    fun cambiarTalla(talla: Double) {
        _model.update { it.copy(tallaNuevaPublicacion = talla) }
    }

    fun cambiarEstadoZapato(estado: String) {
        _model.update { it.copy(estadoNuevaPublicacion = estado) }
    }

    fun cambiarImagenUrl(imagenUrl: String) {
        _model.update { it.copy(urlImagenNuevaPublicacion = imagenUrl) }
    }

    fun agregarPublicacion() {

        _model.update { it.copy(cargando = true, mensajeError = null) }

        val esMarcaNueva = _model.value.marcaSeleccionada.lowercase() == "otro"
        val esProductoNuevo = _model.value.modeloSeleccionado.lowercase() == "otro"

        val bodySolicitud = AgregarPublicacionesSolicitud(
            esMarcaNueva = esMarcaNueva,
            idMarca = if (esMarcaNueva) null else _model.value.idMarcaSeleccionada,
            nombreMarcaNueva = if (esMarcaNueva) _model.value.nombreNuevaMarcaText else null,

            esProductoNuevo = esProductoNuevo,
            idProducto = if (esProductoNuevo) null else _model.value.idProductoSeleccionado,
            nombreProductoNuevo = if (esProductoNuevo) _model.value.nombreNuevoProductoText else null,

            precio = _model.value.precioNuevaPublicacion,
            talla = _model.value.tallaNuevaPublicacion,
            estado = _model.value.estadoNuevaPublicacion,
            urlFoto = _model.value.urlImagenNuevaPublicacion,
            fecha_publicacion = LocalDateTime.now().toString(),
            disponible = true
        )

        val usuario = FirebaseAuth.getInstance().currentUser

        usuario?.getIdToken(true)?.addOnCompleteListener { tarea ->
            if (tarea.isSuccessful) {
                val token = tarea.result.token
                val uid = usuario?.uid
                if (token != null) {
                    viewModelScope.launch {
                        productoRepository.agregarPublicacion(bodySolicitud, token, uid ?: "")
                            .collect { resultado ->
                                if (resultado.exito) {
                                    _model.update {
                                        it.copy(
                                            exito = true,
                                            cargando = false,
                                            mensajeExito = resultado.mensaje
                                        )
                                    }
                                } else {
                                    _model.update {
                                        it.copy(
                                            exito = false,
                                            cargando = false,
                                            mensajeError = resultado.mensaje
                                        )
                                    }
                                }
                            }
                    }
                }
            }
        }
    }


    fun cambiarBusquedaModelo(texto: String) {
        _model.update { it.copy(textoBuscador = texto) }

        if (texto.isBlank()) {
            _model.update { it.copy(sugerenciasModelos = emptyList()) }
            return
        }
        val usuario = FirebaseAuth.getInstance().currentUser

        usuario?.getIdToken(true)?.addOnCompleteListener { tarea ->
            if (tarea.isSuccessful) {
                val token = tarea.result.token

                viewModelScope.launch {
                    val idMarca = _model.value.idMarcaSeleccionada

                    val listaSugerencias = productoRepository.buscarSugerencias(
                        token = token ?: "",
                        idMarca = idMarca,
                        busqueda = texto
                    )

                    _model.update { it.copy(sugerenciasModelos = listaSugerencias) }
                }
            }
        }
    }

    fun cambiarProducto(id: Int, nombre: String) {
        _model.update {
            it.copy(
                idProductoSeleccionado = id,
                modeloSeleccionado = nombre,
                textoBuscador = if (nombre.lowercase() == "otro") it.textoBuscador else nombre,
                sugerenciasModelos = emptyList()
            )
        }
    }

    fun resetearEstadoNuevaPublicacion() {
        _model.update {
            ModelAgregarProducto()
        }
    }
}