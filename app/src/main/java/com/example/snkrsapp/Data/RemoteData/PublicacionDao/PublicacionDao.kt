package com.example.snkrsapp.Data.RemoteData.PublicacionDao

import com.example.snkrsapp.Data.RemoteData.Variables.Endpoints
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PublicacionDao {

    @GET(Endpoints.ObtenerPublicacionesPorModelo)
    suspend fun obtenerPublicacionesPorModelo(
        @Query("modelo") idProducto: Int
    ): Response<List<PublicacionesRespuesta>>

    @POST(Endpoints.AgregarPublicacion)
    suspend fun agregarPublicacion(
        @Header("Authorization") token: String,
        @Body body: AgregarPublicacionesSolicitud
    ): Response<AgregarPublicacionRespuesta>

    @GET(Endpoints.ObtenerCarritoUsuario)
    suspend fun obtenerCarritoUsuario(
        @Header("Authorization") token: String
    ): Response<List<PublicacionPropiaListado>>

    @GET(Endpoints.ObtenerColeccionUsuario)
    suspend fun obtenerColeccionUsuario(
        @Header("Authorization") token: String,
        @Path("uid") uid: String
    ): Response<List<PublicacionCarritoRespuesta>>

    @GET(Endpoints.ObtenerVentasUsuario)
    suspend fun obtenerVentasUsuario(
        @Header("Authorization") token: String,
        @Path("uid") uid: String
    ): Response<List<PublicacionPropiaListado>>

    @GET(Endpoints.ObtenerPublicacionesPorProducto)
    suspend fun getPublicacionesPorProducto(
        @Path("idProducto") idProducto: Int,
        @Header("Authorization") token: String
    ): Response<List<PublicacionesProductoRespuesta>>

    @POST(Endpoints.AgregarAlCarrito)
    suspend fun agregarAlCarrito(
        @Header("Authorization") token: String,
        @Body body: AgregarCarritoSolicitud
    ): Response<AgregarACarritoRespuesta>

    @POST(Endpoints.PedirCarrito)
    suspend fun pedirCarrito(
        @Header("Authorization") token: String,
        @Body body: PedirCarritoSolicitud
    ): Response<OperacionCarritoRespuesta>

    @POST(Endpoints.ComprarCarrito)
    suspend fun comprarCarrito(
        @Header("Authorization") token: String,
        @Body body: ComprarCarritoSolicitud
    ): Response<CompraRespuesta>

    @GET(Endpoints.ObtenerColores)
    suspend fun obtenerColores(
        @Header("Authorization") token: String
    ): Response<List<ColorPublicacionRespuesta>>

    @GET(Endpoints.ObtenerPedidosPendientesVendedor)
    suspend fun obtenerPedidosPendientesVendedor(
        @Header("Authorization") token: String
    ): Response<PedidosPendientesRespuesta>

    @POST(Endpoints.ResponderPedidoVendedor)
    suspend fun responderPedidoVendedor(
        @Header("Authorization") token: String,
        @Body body: ResponderPedidoSolicitud
    ): Response<OperacionCarritoRespuesta>

    @GET(Endpoints.ObtenerPedidosComprador)
    suspend fun obtenerPedidosComprador(
        @Header("Authorization") token: String
    ): Response<PedidosPendientesRespuesta>

    @POST(Endpoints.ConfirmarPedidoRecibido)
    suspend fun confirmarPedidoRecibido(
        @Header("Authorization") token: String,
        @Body body: ConfirmarPedidoSolicitud
    ): Response<OperacionCarritoRespuesta>

    @POST(Endpoints.ReportarPedido)
    suspend fun reportarPedido(
        @Header("Authorization") token: String,
        @Body body: ReportarPedidoSolicitud
    ): Response<OperacionCarritoRespuesta>

    @GET(Endpoints.ObtenerIncidenciasAdmin)
    suspend fun obtenerIncidenciasAdmin(
        @Header("Authorization") token: String
    ): Response<IncidenciasAdminRespuesta>

    @GET(Endpoints.ObtenerIncidenciasUsuario)
    suspend fun obtenerIncidenciasUsuario(
        @Header("Authorization") token: String
    ): Response<IncidenciasAdminRespuesta>

    @POST(Endpoints.ResponderIncidenciaAdmin)
    suspend fun responderIncidenciaAdmin(
        @Header("Authorization") token: String,
        @Body body: ResponderIncidenciaSolicitud
    ): Response<OperacionCarritoRespuesta>

}
