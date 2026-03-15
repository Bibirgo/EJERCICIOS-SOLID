package logic

import models.Product

class Cart {

    val selectedProducts = mutableListOf<Product>()

    fun addProduct(product: Product) {

        selectedProducts.add(product)
    }

    fun clear() {
        selectedProducts.clear()
    }
}