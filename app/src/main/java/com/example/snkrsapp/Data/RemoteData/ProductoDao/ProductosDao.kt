package com.example.snkrsapp.Data.RemoteData.ProductoDao

import com.example.snkrsapp.Data.RemoteData.PublicacionDao.SugerenciaProductoRespuesta
import com.example.snkrsapp.Data.RemoteData.Variables.Endpoints
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ProductosDao {


    @GET(Endpoints.ObtenerPaginaProductos)
    suspend fun obtenerPaginaProductos(
        @Query("limit") limite: Int,
        @Query("offset") salto: Int
    ): Response<List<TodosProductosRespuesta>>

    @GET(Endpoints.ObtenerPaginaProductosFiltrados)
    suspend fun obtenerPaginaProductosFiltrados(
        @Header("Authorization") token: String,
        @Query("minPrecio") minPrecio: Double?,
        @Query("maxPrecio") maxPrecio: Double?,
        @Query("talla") talla: Double?,
        @Query("marcas") marcas: String?,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<List<TodosProductosRespuesta>>

    @GET(Endpoints.BuscarProductosGlobal)
    suspend fun buscarProductosGlobal(
        @Header("Authorization") token: String,
        @Query("busqueda") busqueda: String
    ): Response<List<TodosProductosRespuesta>>

    @GET(Endpoints.ObtenerMarcas)
    suspend fun obtenerMarcas(): Response<List<MarcaRespuesta>>

    @GET(Endpoints.ObtenerModelosBuscador)
    suspend fun buscarSugerenciasModelo(
        @Header("Authorization") token: String,
        @Query("idMarca") idMarca: Int,
        @Query("modeloBuscado") modeloBuscado: String
    ): Response<List<SugerenciaProductoRespuesta>>

    @HTTP(method = "DELETE", path = Endpoints.EliminarMarcas, hasBody = true)
    suspend fun eliminarMarcas(
        @Header("Authorization") token: String,
        @Body solicitud: EliminarMarcasSolicitud
    ): Response<EliminarMarcasRespuesta>

    @PUT(Endpoints.CompletarMarca)
    suspend fun completarMarca(
        @Header("Authorization") token: String,
        @Body solicitud: CompletarMarcaSolicitud
    ): Response<CompletarMarcaRespuesta>

    @POST(Endpoints.CrearMarcaAdmin)
    suspend fun crearMarca(
        @Header("Authorization") token: String,
        @Body solicitud: CrearMarcaSolicitud
    ) : Response<CrearMarcaRespuesta>
}