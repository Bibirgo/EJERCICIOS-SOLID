package models


data class Order(
    val customer: Customer,
    val products: List<Product>,
    val date: String,
    val total: Double
)