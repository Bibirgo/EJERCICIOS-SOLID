package logic

import interfaces.StoreSystem
import logic.StockValidator
import models.Customer
import models.Order

class SystemStore(private val validator: StockValidator) : StoreSystem {

    override fun procesarCompra(cliente: Customer, carrito: Cart): Boolean {
        val items = carrito.selectedProducts

        if (!validator.hasEnoughStock(items)) {
            println("ERROR: Not enough stock to complete the order.")
            return false
        }

        // Calculate total with 16% VAT (Tax)
        val subtotal = items.sumOf { it.price }
        val total = subtotal * 1.16

        // Register order and update stock
        val newOrder = Order(cliente, items.toList(), "2026-02-19", total)
        cliente.purchaseHistory.add(newOrder)

        items.forEach { it.stock -= 1 } // Reduce stock

        println("ORDER GENERATED: Total to pay (VAT included): $${String.format("%.2f", total)}")
        carrito.clear()
        return true
    }
}
