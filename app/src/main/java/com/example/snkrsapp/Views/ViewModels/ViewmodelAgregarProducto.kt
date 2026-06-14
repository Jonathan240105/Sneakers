package com.example.snkrsapp.Views.ViewModels

import android.net.Uri
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


    fun cambiarModoColeccion(esColeccion: Boolean) {
        _model.update { it.copy(esColeccion = esColeccion) }
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
            estado = "disponible",
            urlFoto = _model.value.urlImagenNuevaPublicacion,
            fecha_publicacion = LocalDateTime.now().toString(),
            esParaColeccion = _model.value.esColeccion
        )

        val usuario = FirebaseAuth.getInstance().currentUser

        usuario?.getIdToken(true)?.addOnCompleteListener { tarea ->
            if (tarea.isSuccessful) {
                val token = tarea.result.token
                val uid = usuario?.uid
                if (token != null) {
                    viewModelScope.launch {
                        productoRepository.agregarPublicacion(
                            body = bodySolicitud,
                            token = token,
                            uid = uid ?: "",
                            nombreMarcaSeleccionada = _model.value.marcaSeleccionada,
                            nombreModeloSeleccionado = _model.value.modeloSeleccionado
                        ).collect { resultado ->
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

    fun subirFotoACloudinary(uri: Uri) {
        viewModelScope.launch {
            _model.update { it.copy(cargandoImagen = true, errorImagen = null) }

            val urlCloudinary = productoRepository.subirImagenACloudinary(uri)

            if (urlCloudinary != null) {
                _model.update {
                    it.copy(
                        urlImagenNuevaPublicacion = urlCloudinary,
                        cargandoImagen = false
                    )
                }
            } else {
                _model.update {
                    it.copy(
                        cargandoImagen = false,
                        errorImagen = "Error al subir la imagen"
                    )
                }
            }
        }
    }

    fun resetearEstadoNuevaPublicacion() {
        _model.update {
            ModelAgregarProducto()
        }
    }
}