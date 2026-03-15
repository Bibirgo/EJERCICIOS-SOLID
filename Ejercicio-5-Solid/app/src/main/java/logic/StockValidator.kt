package logic
import models.Product



class StockValidator {

    fun hasEnoughStock(products: List<Product>): Boolean {
        return products.all { it.stock > 0 }
    }
}