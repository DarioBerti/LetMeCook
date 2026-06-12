package com.example.letmecook_lab5.repository

import com.example.letmecook_lab5.domain.Collections
import com.example.letmecook_lab5.domain.IngredientRepository

import com.google.firebase.firestore.FirebaseFirestore
import com.example.letmecook_lab5.model.Ingredient
import kotlinx.coroutines.tasks.await

class FirebaseIngredientRepository(
    private val firestore: FirebaseFirestore
) : IngredientRepository {
    private val ingredientsCollection = firestore.collection(Collections.INGREDIENTS)

    suspend fun initializeData(){
        val existing = ingredientsCollection.limit(1).get().await()
        if (existing.isEmpty){
            firestore.runBatch { batch ->
                placeholderIngredients.forEach { ingredient ->
                    val docRef = ingredientsCollection.document(ingredient.id)
                    batch.set(docRef, ingredient)
                }
            }.await()
        }
    }
    override suspend fun getAllIngredients(): List<Ingredient> {
        return ingredientsCollection
            .get()
            .await()
            .toObjects(Ingredient::class.java)
    }
}

val placeholderIngredients = listOf(
    // Beef Wellington
    Ingredient("1", "Beef tenderloin", 800.0, "g", 0.04, 2.5),
    Ingredient("2", "Puff pastry", 500.0, "g", 0.005, 5.5),
    Ingredient("3", "Mushrooms", 400.0, "g", 0.008, 0.22),
    Ingredient("4", "Prosciutto", 100.0, "g", 0.03, 2.5),
    Ingredient("5", "Egg", 1.0, "pcs", 0.30, 70.0),

    // Vegan Lentil Curry
    Ingredient("6", "Red lentils", 200.0, "g", 0.004, 3.5),
    Ingredient("7", "Coconut milk", 400.0, "ml", 0.005, 2.3),
    Ingredient("8", "Curry powder", 2.0, "tbsp", 0.15, 20.0),
    Ingredient("9", "Spinach", 150.0, "g", 0.01, 0.23),
    Ingredient("10", "Onion", 1.0, "pcs", 0.40, 40.0),

    // Authentic Tiramisu
    Ingredient("11", "Mascarpone cheese", 500.0, "g", 0.01, 4.3),
    Ingredient("12", "Ladyfingers (Savoiardi)", 300.0, "g", 0.008, 3.6),
    Ingredient("13", "Espresso coffee", 300.0, "ml", 0.002, 0.0),
    Ingredient("14", "Eggs", 4.0, "pcs", 0.30, 70.0),
    Ingredient("15", "Cocoa powder", 20.0, "g", 0.015, 2.3),

    // Spicy Tuna Sushi Roll
    Ingredient("16", "Sushi rice", 200.0, "g", 0.005, 1.3),
    Ingredient("17", "Nori seaweed", 2.0, "sheets", 0.50, 5.0),
    Ingredient("18", "Sashimi tuna", 150.0, "g", 0.06, 1.3),
    Ingredient("19", "Sriracha mayo", 2.0, "tbsp", 0.20, 90.0),
    Ingredient("20", "Cucumber", 0.5, "pcs", 0.80, 16.0),

    // Classic French Omelette
    Ingredient("21", "Unsalted butter", 20.0, "g", 0.01, 7.1),
    Ingredient("22", "Chives", 5.0, "g", 0.05, 0.3),
    Ingredient("23", "Salt", 1.0, "pinch", 0.01, 0.0),
    Ingredient("24", "White pepper", 1.0, "pinch", 0.02, 0.0),

    // BBQ Pulled Pork Sandwich
    Ingredient("25", "Pork shoulder", 1.5, "kg", 8.0, 2400.0),
    Ingredient("26", "BBQ sauce", 300.0, "ml", 0.006, 1.7),
    Ingredient("27", "Brioche buns", 6.0, "pcs", 0.60, 250.0),
    Ingredient("28", "Coleslaw", 200.0, "g", 0.008, 1.5),
    Ingredient("29", "Paprika", 2.0, "tbsp", 0.10, 20.0),

    // Traditional Greek Salad
    Ingredient("30", "Tomatoes", 3.0, "pcs", 0.40, 22.0),
    Ingredient("31", "Feta cheese", 150.0, "g", 0.015, 2.6),
    Ingredient("32", "Kalamata olives", 50.0, "g", 0.012, 1.1),
    Ingredient("33", "Extra virgin olive oil", 3.0, "tbsp", 0.15, 120.0),

    // Chicken Tikka Masala
    Ingredient("34", "Chicken thighs", 600.0, "g", 0.009, 2.1),
    Ingredient("35", "Yogurt", 150.0, "g", 0.003, 0.6),
    Ingredient("36", "Tomato passata", 400.0, "ml", 0.002, 0.3),
    Ingredient("37", "Heavy cream", 100.0, "ml", 0.006, 3.4),
    Ingredient("38", "Garam masala", 2.0, "tbsp", 0.20, 30.0),

    // Classic Margherita Pizza
    Ingredient("39", "Pizza dough", 400.0, "g", 0.004, 2.6),
    Ingredient("40", "Tomato sauce", 150.0, "ml", 0.002, 0.3),
    Ingredient("41", "Fresh mozzarella", 125.0, "g", 0.012, 2.8),
    Ingredient("42", "Fresh basil", 5.0, "leaves", 0.10, 1.0),
    Ingredient("43", "Olive oil", 1.0, "tbsp", 0.15, 120.0),

    // Grilled Chicken Caesar Salad
    Ingredient("44", "Chicken breast", 150.0, "g", 0.01, 1.6),
    Ingredient("45", "Romaine lettuce", 100.0, "g", 0.003, 0.17),
    Ingredient("46", "Parmesan cheese", 30.0, "g", 0.02, 4.3),
    Ingredient("47", "Croutons", 40.0, "g", 0.008, 4.0),
    Ingredient("48", "Caesar dressing", 2.0, "tbsp", 0.25, 80.0),

    // Bluefin Tuna Tartare
    Ingredient("49", "Sashimi-grade Bluefin Tuna", 250.0, "g", 0.08, 1.4),
    Ingredient("50", "Avocado", 1.0, "pcs", 1.50, 240.0),
    Ingredient("51", "Soy sauce", 2.0, "tbsp", 0.10, 10.0),
    Ingredient("52", "Sesame oil", 1.0, "tsp", 0.05, 40.0),
    Ingredient("53", "Lime juice", 1.0, "tbsp", 0.10, 5.0),

    // Spaghetti Aglio e Olio
    Ingredient("54", "Spaghetti", 200.0, "g", 0.002, 3.6),
    Ingredient("55", "Garlic", 4.0, "cloves", 0.05, 4.0),
    Ingredient("56", "Red pepper flakes", 1.0, "tsp", 0.05, 5.0),
    Ingredient("57", "Parsley", 10.0, "g", 0.02, 0.3),

    // Black Truffle & Porcini Risotto
    Ingredient("58", "Carnaroli rice", 160.0, "g", 0.005, 3.5),
    Ingredient("59", "Fresh Porcini mushrooms", 200.0, "g", 0.025, 0.26),
    Ingredient("60", "Black Truffle shavings", 15.0, "g", 1.50, 0.0),
    Ingredient("61", "Aged Parmesan", 60.0, "g", 0.02, 4.3),
    Ingredient("62", "Beef broth", 0.8, "L", 2.0, 15.0),

    // Pancakes with Maple Syrup
    Ingredient("63", "Flour", 150.0, "g", 0.001, 3.6),
    Ingredient("64", "Milk", 200.0, "ml", 0.001, 0.6),
    Ingredient("65", "Baking powder", 1.0, "tsp", 0.05, 5.0),
    Ingredient("66", "Maple syrup", 50.0, "ml", 0.015, 2.6),

    // Garlic Butter Lobster Tail
    Ingredient("67", "Lobster tails", 2.0, "pcs", 12.0, 130.0),
    Ingredient("68", "Butter", 100.0, "g", 0.01, 7.1),
    Ingredient("69", "Lemon", 1.0, "pcs", 0.50, 17.0),

    // Avocado Toast with Poached Egg
    Ingredient("70", "Sourdough bread", 1.0, "slice", 0.50, 100.0),
    Ingredient("71", "Chili flakes", 0.5, "tsp", 0.05, 5.0),

    // Chocolate Chip Cookies
    Ingredient("72", "Brown sugar", 100.0, "g", 0.003, 3.8),
    Ingredient("73", "Chocolate chips", 200.0, "g", 0.008, 4.8),
    Ingredient("74", "Vanilla extract", 1.0, "tsp", 0.20, 12.0),

    // Seared A5 Wagyu Ribeye
    Ingredient("75", "A5 Wagyu Ribeye Steak", 200.0, "g", 0.25, 6.0),
    Ingredient("76", "Flaky sea salt", 1.0, "tsp", 0.10, 0.0),
    Ingredient("77", "Freshly ground black pepper", 0.5, "tsp", 0.05, 0.0)
)