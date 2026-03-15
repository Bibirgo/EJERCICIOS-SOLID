package interfaces
import models.Customer
import logic.Cart


interface StoreSystem {

    fun procesarCompra(cliente: Customer, carrito: Cart): Boolean

}