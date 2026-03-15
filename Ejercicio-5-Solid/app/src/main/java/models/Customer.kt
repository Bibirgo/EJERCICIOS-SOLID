package models


data class Customer(
    val name: String,
    val email: String,
    val purchaseHistory: MutableList<Order> = mutableListOf()
)