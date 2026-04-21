package com.example.snkrsapp.TestListadoProductos

import com.example.snkrsapp.Data.LocalData.Productos.ProductoEntity
import com.example.snkrsapp.Data.LocalData.Productos.ProductoEntityToProducto
import com.example.snkrsapp.Data.LocalData.Productos.ProductoToProductoEntity
import com.example.snkrsapp.Data.RemoteData.ProductoDao.TodosProductosRespuesta
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class ProductosTest {
    //Test que comprueba que la creación de un objeto ProductoEntity
    //se cree correctamente con los campos indicados
    @Test
    fun Creacion_Producto_Entity() {
        val producto1 = ProductoEntity(
            1,
            10,
            "Nike Air Max",
            120,
            42,
            "usuario"
        )

        assertEquals(1, producto1.idProducto)
        assertEquals("Nike Air Max", producto1.modelo)
    }

    //Test para comprobar que la función copy, actualiza los campos indicados y deja los demás igual que el copiado
    @Test
    fun Actualizar_Producto_Entity() {
        val producto1 = ProductoEntity(
            1,
            10,
            "Nike Air Max",
            120,
            42,
            "usuario"
        )

        val producto2 = producto1.copy(idProducto = 2)

        assertEquals(2, producto2.idProducto)
        assertEquals("Nike Air Max", producto2.modelo)
    }

    //Test que comprueba que crear un objeto con los mismos valores
    //de los mismos campos es un duplicado
    @Test
    fun Duplicar_Producto_Entity() {
        val producto1 = ProductoEntity(
            1,
            10,
            "Nike Air Max",
            120,
            42,
            "usuario"
        )
        val producto2 = ProductoEntity(
            1,
            10,
            "Nike Air Max",
            120,
            42,
            "usuario"
        )

        assertEquals(producto1, producto2)
    }

    //Test que comprueba que la función ProductoToProductoEntity,
    //convierte un objeto TodosProductosRespuesta en un objeto ProductoEntity
    @Test
    fun Convertir_Producto_a_ProductoEntity() {
        val producto = TodosProductosRespuesta(
            idProducto = 1,
            idMarca = 10,
            modelo = "Nike Air Max",
            precio = 120,
            talla = 42,
            uidVendedor = "usuario",
            imagenUrl = "url"
        )

        val entity = ProductoToProductoEntity(producto)

        assertTrue(entity is ProductoEntity)
        assertEquals(producto.idProducto, entity.idProducto)
    }

    //test que comprueba que la función ProductoEntityToProducto,
    //convierte un objeto ProductoEntity en un objeto TodosProductosRespuesta
    @Test
    fun Convertir_ProductoEntity_a_Producto() {
        val entity = ProductoEntity(
            1,
            10,
            "Nike Air Max",
            120,
            42,
            "usuario"
        )

        val producto = ProductoEntityToProducto(entity)

        assertTrue(producto is TodosProductosRespuesta)
        assertEquals(entity.idProducto, producto.idProducto)
    }
}