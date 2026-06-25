package com.example.snkrsapp.Views.ViewModels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snkrsapp.Data.RemoteData.PublicacionDao.AgregarPublicacionesSolicitud
import com.example.snkrsapp.Data.RemoteData.PublicacionDao.VariantePublicacionSolicitud
import com.example.snkrsapp.Data.Repository.ProductoRepository.ProductoRepository
import com.example.snkrsapp.Domain.ColorPublicacion
import com.example.snkrsapp.Domain.ModelAgregarProducto
import com.example.snkrsapp.Domain.VarianteNuevaPublicacion
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

    fun cargarColores() {
        val usuario = FirebaseAuth.getInstance().currentUser ?: return

        usuario.getIdToken(true).addOnCompleteListener { tarea ->
            if (tarea.isSuccessful) {
                val token = tarea.result.token ?: return@addOnCompleteListener

                viewModelScope.launch {
                    productoRepository.traerColores(token).collect { colores ->
                        _model.update { it.copy(coloresDisponibles = colores) }
                    }
                }
            }
        }
    }


    fun cambiarModoColeccion(esColeccion: Boolean) {
        _model.update { it.copy(esColeccion = esColeccion) }
    }

    fun agregarVariante() {
        _model.update { it.copy(variantes = it.variantes + VarianteNuevaPublicacion()) }
    }

    fun eliminarVariante(indice: Int) {
        _model.update { estado ->
            if (estado.variantes.size <= 1) {
                estado
            } else {
                estado.copy(variantes = estado.variantes.filterIndexed { index, _ -> index != indice })
            }
        }
    }

    fun cambiarColorVariante(indice: Int, color: ColorPublicacion) {
        _model.update { estado ->
            val fotoExistenteColor = estado.variantes
                .firstOrNull { it.idColor == color.idColor && it.urlFoto.isNotBlank() }
                ?.urlFoto

            estado.copy(
                variantes = estado.variantes.mapIndexed { index, variante ->
                    if (index == indice) {
                        variante.copy(
                            idColor = color.idColor,
                            nombreColor = color.nombre,
                            hexColor = color.hex,
                            urlFoto = fotoExistenteColor ?: variante.urlFoto
                        )
                    } else {
                        variante
                    }
                }
            )
        }
    }

    fun cambiarTallaVariante(indice: Int, talla: Double) {
        actualizarVariante(indice) { it.copy(talla = talla) }
    }

    fun cambiarStockVariante(indice: Int, cantidad: Int) {
        actualizarVariante(indice) { it.copy(cantidadDisponible = cantidad.coerceAtLeast(1)) }
    }

    fun subirFotoVariante(indice: Int, uri: Uri) {
        viewModelScope.launch {
            actualizarVariante(indice) { it.copy(cargandoImagen = true, errorImagen = null) }

            val urlCloudinary = productoRepository.subirImagenACloudinary(uri)

            if (urlCloudinary != null) {
                _model.update { estado ->
                    val colorVariante = estado.variantes.getOrNull(indice)?.idColor ?: 0

                    estado.copy(
                        variantes = estado.variantes.mapIndexed { index, variante ->
                            val debeCompartirFoto = colorVariante != 0 && variante.idColor == colorVariante

                            if (index == indice || debeCompartirFoto) {
                                variante.copy(
                                    urlFoto = urlCloudinary,
                                    cargandoImagen = false,
                                    errorImagen = null
                                )
                            } else {
                                variante
                            }
                        }
                    )
                }
            } else {
                actualizarVariante(indice) {
                    it.copy(cargandoImagen = false, errorImagen = "Error al subir la imagen")
                }
            }
        }
    }

    fun agregarPublicacion() {
        val estadoActual = _model.value

        val variantesSolicitud = if (estadoActual.esColeccion) {
            emptyList()
        } else {
            val variantesValidas = estadoActual.variantes.filter {
                it.idColor != 0 &&
                    it.talla > 0.0 &&
                    it.cantidadDisponible > 0 &&
                    it.urlFoto.isNotBlank()
            }

            if (variantesValidas.size != estadoActual.variantes.size) {
                _model.update {
                    it.copy(mensajeError = "Completa color, talla, stock e imagen en todas las variantes")
                }
                return
            }

            if (variantesValidas.any { it.talla !in 16.0..60.0 }) {
                _model.update {
                    it.copy(mensajeError = "La talla debe estar entre 16 y 60")
                }
                return
            }

            variantesValidas.map {
                VariantePublicacionSolicitud(
                    idColor = it.idColor,
                    talla = it.talla,
                    cantidadDisponible = it.cantidadDisponible,
                    urlFoto = it.urlFoto
                )
            }
        }

        val fotoPrincipal = if (estadoActual.esColeccion) {
            estadoActual.urlImagenNuevaPublicacion
        } else {
            variantesSolicitud.firstOrNull()?.urlFoto.orEmpty()
        }

        val tallaPrincipal = if (estadoActual.esColeccion) {
            estadoActual.tallaNuevaPublicacion
        } else {
            variantesSolicitud.firstOrNull()?.talla ?: 0.0
        }

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
            talla = tallaPrincipal,
            estado = "disponible",
            urlFoto = fotoPrincipal,
            fecha_publicacion = LocalDateTime.now().toString(),
            variantes = variantesSolicitud,
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

    private fun actualizarVariante(
        indice: Int,
        transformacion: (VarianteNuevaPublicacion) -> VarianteNuevaPublicacion
    ) {
        _model.update { estado ->
            estado.copy(
                variantes = estado.variantes.mapIndexed { index, variante ->
                    if (index == indice) transformacion(variante) else variante
                }
            )
        }
    }
}
