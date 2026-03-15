package edu.itvo.sistemadeventas

import models.*
import logic.*
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class TestStore {

    private lateinit var validator: StockValidator
    private lateinit var system: SystemStore

    @Before
    fun setUp() {
        // Centralized initialization for code clarity
        validator = StockValidator()
        system = SystemStore(validator)
    }

    @Test
    fun shouldverifystockbeforeconfirmingtheorder() {
        // Out of stock product: Gaming Laptop
        val laptop = Product("Gaming Laptop", 25000.0, 0)
        val customer = Customer("Bibiana Rubi Gaytan Ortiz", "bibianagaytan417@gmail.com")
        val cart = Cart().apply { addProduct(laptop) }

        val purchaseResult = system.procesarCompra(customer, cart)

        println("Customer: ${customer.name} | Error: Insufficient stock for ${laptop.name} | Order: Not generated")
        assertFalse(purchaseResult)
    }

    @Test
    fun shouldcalculatetotalapplying16percenttax() {
        // Product: Sony Camera ($5000)
        val product = Product("Sony Camera", 5000.0, 10)
        val customer = Customer("Rubi", "Rubi@gmail.com")
        val cart = Cart().apply { addProduct(product) }

        system.procesarCompra(customer, cart)

        // Calculation: 5000 * 1.16 = 5800.0
        val expectedTotal = 5800.0
        val actualTotal = customer.purchaseHistory.last().total

        println("Customer: ${customer.name} | Error: None | Order Generated: #001 - Total: $actualTotal (VAT included)")
        assertEquals(expectedTotal, actualTotal, 0.01)
    }

    @Test
    fun shouldregistertheorderincustomerhistoryafterpurchase() {
        // Product: Samsung Tablet
        val product = Product("Samsung Tablet", 8000.0, 5)
        val customer = Customer("Eneyda Juliet", "EneydaJul@gmail.com")
        val cart = Cart().apply { addProduct(product) }

        system.procesarCompra(customer, cart)

        println("Customer: ${customer.name} | Error: None | Order Generated: Registered in History (${customer.purchaseHistory.size} order)")
        assertEquals(1, customer.purchaseHistory.size)
    }
}