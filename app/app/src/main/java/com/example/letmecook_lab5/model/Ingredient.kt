package com.example.letmecook_lab5.model

import java.util.UUID

data class Ingredient(
    val id : String = UUID.randomUUID().toString(),
    val name: String = "",
    val quantity: Double = 0.0,
    val unit: String = "",
    val unitCost : Double = 0.0,
    val unitCalories : Double = 0.0
)