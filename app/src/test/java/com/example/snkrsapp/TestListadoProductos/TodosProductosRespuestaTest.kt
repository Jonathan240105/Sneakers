package com.example.snkrsapp.TestListadoProductos

import com.example.snkrsapp.Data.RemoteData.ProductoDao.TodosProductosRespuesta
import junit.framework.TestCase.assertEquals
import org.junit.Test

class TodosProductosRespuestaTest {

    //Función que comprueba que la creación de un objeto
    // TodosProductosRespuesta se cree correctamente con los campos indicados
    @Test
    fun Creacion_Producto_Respuesta() {
        val producto = TodosProductosRespuesta(
            idProducto = 1,
            idMarca = 10,
            modelo = "Nike Air Max",
            precio = 120,
            talla = 42,
            uidVendedor = "usuario",
            imagenUrl = "url1"
        )

        assertEquals(1, producto.idProducto)
        assertEquals("Nike Air Max", producto.modelo)
        assertEquals(120, producto.precio)
        assertEquals("url1", producto.imagenUrl)
    }

    //Test que comprueba que la función copy,
    //actualiza los campos indicados y deja los demás igual que el copiado
    @Test
    fun Actualizar_Producto_Respuesta() {
        val producto1 = TodosProductosRespuesta(
            idProducto = 1,
            idMarca = 10,
            modelo = "Nike Air Max",
            precio = 120,
            talla = 42,
            uidVendedor = "usuario1",
            imagenUrl = "url1"
        )

        val producto2 = producto1.copy(modelo = "Nike Jordan", precio = 200)

        assertEquals("Nike Jordan", producto2.modelo)
        assertEquals(200, producto2.precio)
        assertEquals(producto1.idProducto, producto2.idProducto)
        assertEquals(producto1.uidVendedor, producto2.uidVendedor)
    }

    //Test que comprueba que crear un objeto TodosProductosRespuesta
    //con los mismos valores
    //de los mismos campos es un duplicado
    @Test
    fun Duplicar_Producto_Respuesta() {
        val producto1 = TodosProductosRespuesta(
            idProducto = 1,
            idMarca = 10,
            modelo = "Nike Air Max",
            precio = 120,
            talla = 42,
            uidVendedor = "usuario",
            imagenUrl = "url"
        )

        val producto2 = TodosProductosRespuesta(
            idProducto = 1,
            idMarca = 10,
            modelo = "Nike Air Max",
            precio = 120,
            talla = 42,
            uidVendedor = "usuario",
            imagenUrl = "url"
        )

        assertEquals(producto1, producto2)
    }
}
