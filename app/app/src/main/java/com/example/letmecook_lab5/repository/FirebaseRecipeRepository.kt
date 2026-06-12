package com.example.letmecook_lab5.repository

import android.util.Log
import com.example.letmecook_lab5.domain.RecipeRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.example.letmecook_lab5.domain.Collections
import com.example.letmecook_lab5.model.Ingredient
import com.example.letmecook_lab5.model.Recipe
import com.example.letmecook_lab5.model.Review
import com.example.letmecook_lab5.model.Step
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirebaseRecipeRepository(
    private val firestore: FirebaseFirestore
) : RecipeRepository {

    private val recipesCollection = firestore.collection(Collections.RECIPES)

    suspend fun initializeData(){
        val existing = recipesCollection.limit(1).get().await()
        if(existing.isEmpty){
            firestore.runBatch { batch ->
                placeholderRecipes.forEach { recipe ->
                    val docRef = recipesCollection.document(recipe.id)
                    recipe.reviews.forEach { review ->
                        val reviewRef = docRef.collection(Collections.REVIEWS).document(review.id)
                        batch.set(reviewRef, review)
                    }
                    batch.set(docRef, recipe)
                }
            }.await()
        }
    }

    override fun getAllRecipes(): Flow<List<Recipe>> {
        return recipesCollection.snapshots()
            .map{snapshot ->
                snapshot.toObjects(Recipe::class.java)
            }
    }

    override fun getRecipesByOwner(ownerId: String): Flow<List<Recipe>> {
        return recipesCollection.snapshots()
            .map { snapshot ->
                snapshot.toObjects(Recipe::class.java)
                    .filter { recipe ->
                        recipe.ownerId == ownerId
                    }
            }
    }

    override fun getTopTenRecipes(): Flow<List<Recipe>> {
        return recipesCollection
            .orderBy("averageRating", Query.Direction.DESCENDING)
            .limit(10)
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(Recipe::class.java)
            }
    }

    override fun getNewRecipes(): Flow<List<Recipe>> {
        return recipesCollection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(10)
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(Recipe::class.java)
            }
    }

    override fun getFastRecipes(): Flow<List<Recipe>> {
        return recipesCollection
            .orderBy("cookingTime")
            .limit(10)
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(Recipe::class.java)
            }
    }

    override suspend fun getRecipeById(recipeId: String): Recipe{
        val recipe = recipesCollection.document(recipeId).get().await().toObject(Recipe::class.java)
        if(recipe != null){
            return recipe
        }else{
            throw Exception("Recipe $recipeId not found")
        }
    }

    override suspend fun addRecipe(recipe: Recipe) {
        try{
            recipesCollection.document(recipe.id).set(recipe).await()
        }catch (e: Exception){
            Log.d("FirebaseRecipeRepository", "Errore create: ${e.message}")
        }
    }

    override suspend fun deleteRecipe(recipeId: String) {
        try{
            recipesCollection.document(recipeId).delete().await()
        }catch (e: Exception){
            Log.d("FirebaseRecipeRepository", "Errore delete: ${e.message}")
        }
    }

    override suspend fun updateRecipe(recipe: Recipe) {
        try{
            recipesCollection.document(recipe.id).set(recipe).await()
        }catch (e: Exception){
            Log.d("FirebaseRecipeRepository", "Errore update: ${e.message}")
        }
    }
}


val placeholderRecipes = listOf(
    Recipe(
        id = "11",
        ownerId = "204",
        title = "Classic Beef Wellington",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_images%2F11.jpg?alt=media&token=93fb5eee-c215-4e1d-b277-09f58a273507",
        averageRating = 4.3,
        tags = listOf("luxury", "dinner", "baking"),
        difficulty = "Hard",
        cookingTime = 120,
        calories = 5100,
        servings = 4,
        cost = 50.30,
        ingredients = listOf(
            Ingredient(name = "Beef tenderloin", quantity = 800.0, unit = "g", unitCost = 0.05, unitCalories = 2.5),
            Ingredient(name = "Puff pastry", quantity = 500.0, unit = "g", unitCost = 0.01, unitCalories = 5.5),
            Ingredient(name = "Mushrooms", quantity = 400.0, unit = "g", unitCost = 0.005, unitCalories = 0.2),
            Ingredient(name = "Prosciutto", quantity = 100.0, unit = "g", unitCost = 0.03, unitCalories = 2.0),
            Ingredient(name = "Egg", quantity = 1.0, unit = "pcs", unitCost = 0.30, unitCalories = 70.0)
        ),
        steps = listOf(
            Step(title = "Sear beef", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_sear_beef_tenderloin.jpg?alt=media&token=1bb55a9c-ab7a-497a-a26b-2433d599ed0d", description = "Quickly sear the beef tenderloin on all sides in a hot pan. Let it cool."),
            Step(title = "Make duxelles", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_mushroom.jpg?alt=media&token=74f973eb-0158-43fd-b3e2-b527bee7c718", description = "Finely chop mushrooms and cook until all moisture evaporates."),
            Step(title = "Wrap", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_pastry.jpg?alt=media&token=b7005358-d8f7-4df6-965c-b353e8b70b25", description = "Layer prosciutto, duxelles, and beef. Wrap tightly in puff pastry and brush with egg wash."),
            Step(title = "Bake", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_bake.jpg?alt=media&token=dcb28e78-9c6e-4028-8f84-f69c9dcad8f4", description = "Bake at 200°C for 35 minutes until pastry is golden brown.")
        ),
        storageInstructions = "Wrap in foil and refrigerate up to 2 days. Do not freeze once assembled.",
        reviews = listOf(
            Review(recipeId = "11", authorId = "101", rating = 5, imageUrl = "", comment = "Incredibly rich and perfectly executed recipe. The beef was tender and the pastry stayed crisp. Worth every minute of effort.", doTips = listOf("Let the beef rest completely before wrapping", "Use a high-quality puff pastry", "Seal the layers tightly to avoid leaks"), dontTips = listOf("Don't skip chilling before baking", "Don't overcook the beef during searing")),
            Review(recipeId = "11", authorId = "202", rating = 5, imageUrl = "", comment = "Amazing dish for a special occasion. The flavor combination is excellent, but it requires patience and precision.", doTips = listOf("Use mushrooms cooked until completely dry", "Chill each layer before assembling"), dontTips = listOf("Don't rush the duxelles preparation", "Don't use low-quality meat")),
            Review(recipeId = "11", authorId = "303", rating = 3, imageUrl = "", comment = "Great recipe but definitely not beginner-friendly. My pastry slightly overbrowned, but taste was still excellent.", doTips = listOf("Monitor oven temperature closely", "Use foil if the pastry browns too quickly"), dontTips = listOf("Don't leave it unattended in the oven", "Don't skip egg wash for shine")),
            Review(recipeId = "11", authorId = "404", authorNickname = "Giulia", rating = 4, imageUrl = "", comment = "Impressive result, took some patience.", doTips = listOf("Chill before baking"), dontTips = listOf("Don't rush the duxelles"))
        )
    ),
    Recipe(
        id = "12",
        ownerId = "baIhsE6qFzMuvrwiqyA83NSnzwE2",
        title = "Vegan Lentil Curry",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_images%2F12.jpg?alt=media&token=b122cc2d-399d-4a9a-827a-c65b6d193514",
        averageRating = 4.5,
        tags = listOf("vegan", "healthy", "dinner"),
        difficulty = "Easy",
        cookingTime = 40,
        calories = 1610, // Ricalcolato
        servings = 3,
        cost = 5.20, // Ricalcolato
        ingredients = listOf(
            Ingredient(name = "Red lentils", quantity = 200.0, unit = "g", unitCost = 0.004, unitCalories = 3.5),
            Ingredient(name = "Coconut milk", quantity = 400.0, unit = "ml", unitCost = 0.005, unitCalories = 2.0),
            Ingredient(name = "Curry powder", quantity = 2.0, unit = "tbsp", unitCost = 0.20, unitCalories = 20.0),
            Ingredient(name = "Spinach", quantity = 150.0, unit = "g", unitCost = 0.01, unitCalories = 0.2),
            Ingredient(name = "Onion", quantity = 1.0, unit = "pcs", unitCost = 0.50, unitCalories = 40.0)
        ),
        steps = listOf(
            Step(title = "Sauté aromatics", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_onion_curry.jpg?alt=media&token=3a78f24f-bced-4eee-af87-a6d0714f41d5", description = "Cook diced onion with curry powder until fragrant."),
            Step(title = "Simmer", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_simmer.jpg?alt=media&token=b34ca111-5ab2-4d71-9a08-237a46dfcfe6", description = "Add lentils and coconut milk. Simmer for 25 minutes until lentils are soft."),
            Step(title = "Add greens", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_curry_greens.jpg?alt=media&token=64ffe2c5-71ad-4e50-81dc-665a78d706f0", description = "Stir in fresh spinach and cook for 2 more minutes until wilted.")
        ),
        storageInstructions = "Store in an airtight container in the fridge for up to 4 days. Reheat gently on the stovetop.",
        reviews = listOf(
            Review(recipeId = "12", authorId = "201", authorNickname = "Marco", rating = 5, imageUrl = "", comment = "Delicious and easy!", doTips = listOf("Use fresh spinach"), dontTips = listOf("Don't overcook the lentils")),
            Review(recipeId = "12", authorId = "202", authorNickname = "Sara",  rating = 4, imageUrl = "", comment = "Very tasty", doTips = emptyList(), dontTips = emptyList()),
            Review(recipeId = "12", authorId = "203", authorNickname = "Luca",  rating = 5, imageUrl = "", comment = "Made it twice already", doTips = emptyList(), dontTips = emptyList()),
            Review(recipeId = "12", authorId = "204", authorNickname = "Anna",  rating = 4, imageUrl = "", comment = "Great weeknight dinner", doTips = emptyList(), dontTips = emptyList())
        )
    ),
    Recipe(
        id = "13",
        ownerId = "baIhsE6qFzMuvrwiqyA83NSnzwE2",
        title = "Authentic Tiramisu",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_images%2F13.jpg?alt=media&token=04f4895f-651b-4920-8042-a6f5e29e5952",
        averageRating = 4.9,
        tags = listOf("dessert", "italian", "sweet", "coffee"),
        difficulty = "Medium",
        cookingTime = 30,
        calories = 3466, // Ricalcolato
        servings = 6,
        cost = 15.10, // Ricalcolato
        ingredients = listOf(
            Ingredient(name = "Mascarpone cheese", quantity = 500.0, unit = "g", unitCost = 0.015, unitCalories = 4.0),
            Ingredient(name = "Ladyfingers (Savoiardi)", quantity = 300.0, unit = "g", unitCost = 0.01, unitCalories = 3.8),
            Ingredient(name = "Espresso coffee", quantity = 300.0, unit = "ml", unitCost = 0.01, unitCalories = 0.0),
            Ingredient(name = "Eggs", quantity = 4.0, unit = "pcs", unitCost = 0.30, unitCalories = 70.0),
            Ingredient(name = "Cocoa powder", quantity = 20.0, unit = "g", unitCost = 0.02, unitCalories = 2.3)
        ),
        steps = listOf(
            Step(title = "Make cream", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_whisk.jpg?alt=media&token=13e56c10-dc60-48f3-af7e-b8d46214c3bf", description = "Whip egg yolks with sugar, then fold in mascarpone. Fold in whipped egg whites gently."),
            Step(title = "Dip cookies", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_ladyfinger_dip.jpg?alt=media&token=5fd40152-2aa4-4124-95e0-9a67846aacf6", description = "Quickly dip ladyfingers into cold espresso and layer them in a dish."),
            Step(title = "Layer and chill", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_tiramisu_layer2.jpg?alt=media&token=41057dc4-535b-43c6-abb8-789eb8bb09ba", description = "Alternate layers of cookies and cream. Chill overnight and dust with cocoa powder before serving.")
        ),
        storageInstructions = "Keep refrigerated in a covered container for up to 3 days. Do not freeze.",
        reviews = emptyList()
    ),
    Recipe(
        id = "14",
        ownerId = "U3xYzBPl3UV04pgChCmaGEnMQIR2",
        title = "Spicy Tuna Sushi Roll",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_images%2F14.jpg?alt=media&token=667b63ca-a9f0-4732-8a9c-5bc8f2cb858b",
        averageRating = 4.6,
        tags = listOf("appetizer", "dinner", "raw"),
        difficulty = "Hard",
        cookingTime = 50,
        calories = 605, // Ricalcolato
        servings = 2,
        cost = 14.40, // Ricalcolato
        ingredients = listOf(
            Ingredient(name = "Sushi rice", quantity = 200.0, unit = "g", unitCost = 0.005, unitCalories = 1.3),
            Ingredient(name = "Nori seaweed", quantity = 2.0, unit = "sheets", unitCost = 0.30, unitCalories = 10.0),
            Ingredient(name = "Sashimi tuna", quantity = 150.0, unit = "g", unitCost = 0.08, unitCalories = 1.4),
            Ingredient(name = "Sriracha mayo", quantity = 2.0, unit = "tbsp", unitCost = 0.15, unitCalories = 50.0),
            Ingredient(name = "Cucumber", quantity = 0.5, unit = "pcs", unitCost = 1.0, unitCalories = 30.0)
        ),
        steps = listOf(
            Step(title = "Prep rice", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_sushi_rice.jpg?alt=media&token=a9e1a5fb-5cb0-4616-9fcc-547875631079", description = "Cook sushi rice and season with rice vinegar. Let it cool."),
            Step(title = "Mix tuna", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_spicy_tuna_mix.jpg?alt=media&token=07409624-7960-4907-b675-95c95eb5679d", description = "Chop the tuna finely and mix with sriracha mayo."),
            Step(title = "Roll", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_sushi_roll.jpg?alt=media&token=4f88b65f-3220-4890-a49e-3279dd60fe7e", description = "Spread rice on nori, add tuna and cucumber sticks. Roll tightly using a bamboo mat.")
        ),
        storageInstructions = "Consume immediately. Raw fish should not be stored as leftovers.",
        reviews = emptyList()
    ),
    Recipe(
        id = "15",
        ownerId = "289",
        title = "Classic French Omelette",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_images%2F15.jpg?alt=media&token=4f5bcda2-cba1-4870-9311-83831a972047",
        averageRating = 4.4,
        tags = listOf("breakfast", "quick"),
        difficulty = "Medium",
        cookingTime = 5,
        calories = 353, // Ricalcolato
        servings = 1,
        cost = 1.51, // Ricalcolato
        ingredients = listOf(
            Ingredient(name = "Eggs", quantity = 3.0, unit = "pcs", unitCost = 0.30, unitCalories = 70.0),
            Ingredient(name = "Unsalted butter", quantity = 20.0, unit = "g", unitCost = 0.015, unitCalories = 7.1),
            Ingredient(name = "Chives", quantity = 5.0, unit = "g", unitCost = 0.05, unitCalories = 0.3),
            Ingredient(name = "Salt", quantity = 1.0, unit = "pinch", unitCost = 0.01, unitCalories = 0.0),
            Ingredient(name = "White pepper", quantity = 1.0, unit = "pinch", unitCost = 0.05, unitCalories = 0.0)
        ),
        steps = listOf(
            Step(title = "Beat eggs", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_whisk.jpg?alt=media&token=13e56c10-dc60-48f3-af7e-b8d46214c3bf", description = "Whisk eggs vigorously until completely smooth with no visible whites."),
            Step(title = "Cook slowly", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_omelette_cook.jpg?alt=media&token=e4b70702-0d44-41d6-a7a6-af4322e4f364", description = "Melt butter in a non-stick pan over medium-low heat. Add eggs and stir constantly."),
            Step(title = "Roll and serve", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_omelette_roll.jpg?alt=media&token=1fc247a3-0466-4d35-9088-fe6a4430d18d", description = "When mostly set but still creamy on top, roll the omelette carefully and serve with chives.")
        ),
        storageInstructions = "Best enjoyed immediately while hot. Do not store.",
        reviews = emptyList()
    ),
    Recipe(
        id = "16",
        ownerId = "501",
        title = "BBQ Pulled Pork Sandwich",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_images%2F16.jpg?alt=media&token=719e0678-2486-4bda-9937-d2bc90639ef6",
        averageRating = 4.7,
        tags = listOf("american", "lunch", "comfort"),
        difficulty = "Easy",
        cookingTime = 480,
        calories = 6040, // Ricalcolato
        servings = 6,
        cost = 23.50, // Ricalcolato
        ingredients = listOf(
            Ingredient(name = "Pork shoulder", quantity = 1.5, unit = "kg", unitCost = 10.0, unitCalories = 2500.0),
            Ingredient(name = "BBQ sauce", quantity = 300.0, unit = "ml", unitCost = 0.005, unitCalories = 1.5),
            Ingredient(name = "Brioche buns", quantity = 6.0, unit = "pcs", unitCost = 0.80, unitCalories = 250.0),
            Ingredient(name = "Coleslaw", quantity = 200.0, unit = "g", unitCost = 0.01, unitCalories = 1.5),
            Ingredient(name = "Paprika", quantity = 2.0, unit = "tbsp", unitCost = 0.10, unitCalories = 20.0)
        ),
        steps = listOf(
            Step(title = "Slow cook", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_slowcook.jpg?alt=media&token=68eb5eee-2576-418c-8ac3-04bc295ea776", description = "Rub pork with spices and slow cook on low for 8 hours until fork-tender."),
            Step(title = "Shred", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_shred.jpg?alt=media&token=eeed37c8-2e14-4265-8fbb-d372fba5d29f", description = "Remove pork, shred it with two forks, and mix generously with BBQ sauce."),
            Step(title = "Assemble", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_sandwich_assemble.jpg?alt=media&token=fac23461-0b26-48e1-898b-46324cec86c7", description = "Toast the buns, add a mountain of pulled pork, and top with fresh coleslaw.")
        ),
        storageInstructions = "Store the pulled pork separately from the buns and coleslaw in an airtight container for up to 4 days. Can be frozen.",
        reviews = emptyList()
    ),
    Recipe(
        id = "17",
        ownerId = "109",
        title = "Traditional Greek Salad",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_images%2F17.jpg?alt=media&token=94808d50-e5b8-416a-9e1b-dd4dec177d37",
        averageRating = 4.6,
        tags = listOf("salad", "vegetarian", "healthy"),
        difficulty = "Easy",
        cookingTime = 10,
        calories = 975, // Ricalcolato
        servings = 2,
        cost = 7.15, // Ricalcolato
        ingredients = listOf(
            Ingredient(name = "Tomatoes", quantity = 3.0, unit = "pcs", unitCost = 0.60, unitCalories = 25.0),
            Ingredient(name = "Cucumber", quantity = 1.0, unit = "pcs", unitCost = 1.0, unitCalories = 30.0),
            Ingredient(name = "Feta cheese", quantity = 150.0, unit = "g", unitCost = 0.02, unitCalories = 2.6),
            Ingredient(name = "Kalamata olives", quantity = 50.0, unit = "g", unitCost = 0.015, unitCalories = 2.4),
            Ingredient(name = "Extra virgin olive oil", quantity = 3.0, unit = "tbsp", unitCost = 0.20, unitCalories = 120.0)
        ),
        steps = listOf(
            Step(title = "Chop veggies", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_chop.jpg?alt=media&token=b5a15111-3325-41a2-b9a8-cb5e0908a154", description = "Cut tomatoes into wedges and slice the cucumber thickly."),
            Step(title = "Combine", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_avocado.jpg?alt=media&token=f7c20102-1ee1-48ac-8e8e-6d303af6065a", description = "Place veggies in a bowl, add olives, and top with a whole block of feta cheese."),
            Step(title = "Dress", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_garlic_oil.jpg?alt=media&token=df94954e-ba06-4f3f-b66a-79acfdf7a825", description = "Drizzle generously with olive oil and sprinkle with dried oregano. Do not mix until serving.")
        ),
        storageInstructions = "Best eaten fresh. Store without dressing in the fridge for up to 1 day.",
        reviews = emptyList()
    ),
    Recipe(
        id = "18",
        ownerId = "888",
        title = "Chicken Tikka Masala",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_images%2F18.jpg?alt=media&token=f3852e57-dd70-4293-bc1e-f04eb56a9a9f",
        averageRating = 4.8,
        tags = listOf("indian", "dinner", "spicy"),
        difficulty = "Medium",
        cookingTime = 60,
        calories = 1790, // Ricalcolato
        servings = 4,
        cost = 9.15, // Ricalcolato
        ingredients = listOf(
            Ingredient(name = "Chicken thighs", quantity = 600.0, unit = "g", unitCost = 0.01, unitCalories = 2.0),
            Ingredient(name = "Yogurt", quantity = 150.0, unit = "g", unitCost = 0.005, unitCalories = 0.6),
            Ingredient(name = "Tomato passata", quantity = 400.0, unit = "ml", unitCost = 0.003, unitCalories = 0.3),
            Ingredient(name = "Heavy cream", quantity = 100.0, unit = "ml", unitCost = 0.008, unitCalories = 3.4),
            Ingredient(name = "Garam masala", quantity = 2.0, unit = "tbsp", unitCost = 0.20, unitCalories = 20.0)
        ),
        steps = listOf(
            Step(title = "Marinate", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_chicken_marinate.jpg?alt=media&token=3ceef181-417c-48dc-b66b-9db3fe41a806", description = "Marinate chicken chunks in yogurt and spices for at least 30 minutes."),
            Step(title = "Cook chicken", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_chicken_brown.jpg?alt=media&token=6a852133-71a4-4705-a6df-4da90a0f55fc", description = "Brown the chicken in a hot pan, then set aside."),
            Step(title = "Make sauce", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_tikka_sauce.jpg?alt=media&token=3176c099-623d-4de9-9fb4-26dec1026ee8", description = "Simmer tomato passata with spices, add the chicken back in, and finish with heavy cream.")
        ),
        storageInstructions = "Store in an airtight container in the fridge for up to 3 days. Flavors will develop nicely overnight.",
        reviews = emptyList()
    ),
    Recipe(
        id = "1",
        ownerId = "101",
        title = "Classic Margherita Pizza",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_images%2F1.jpg?alt=media&token=2d6ecbd2-5ebf-4c91-b73d-661e5fbaae37",
        averageRating = 4.0,
        tags = listOf("italian", "vegetarian", "dinner"),
        difficulty = "Medium",
        cookingTime = 90,
        calories = 1515, // Ricalcolato
        servings = 2,
        cost = 4.77, // Ricalcolato
        ingredients = listOf(
            Ingredient(name = "Pizza dough", quantity = 400.0, unit = "g", unitCost = 0.005, unitCalories = 2.5),
            Ingredient(name = "Tomato sauce", quantity = 150.0, unit = "ml", unitCost = 0.003, unitCalories = 0.3),
            Ingredient(name = "Fresh mozzarella", quantity = 125.0, unit = "g", unitCost = 0.015, unitCalories = 2.8),
            Ingredient(name = "Fresh basil", quantity = 5.0, unit = "leaves", unitCost = 0.05, unitCalories = 0.0),
            Ingredient(name = "Olive oil", quantity = 1.0, unit = "tbsp", unitCost = 0.20, unitCalories = 120.0)
        ),
        steps = listOf(
            Step(title = "Preheat oven", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_bake.jpg?alt=media&token=dcb28e78-9c6e-4028-8f84-f69c9dcad8f4", description = "Preheat your oven to the maximum temperature (usually 250°C)."),
            Step(title = "Shape dough", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_dough.jpg?alt=media&token=b87b954f-3854-4e8a-87b0-113e60650237", description = "Stretch the pizza dough into a round shape on a floured surface."),
            Step(title = "Add toppings", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_pizza_topping.jpg?alt=media&token=7319c824-3058-443d-a1d0-ed8b3deedb89", description = "Spread tomato sauce, add torn mozzarella, and drizzle with olive oil."),
            Step(title = "Bake", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_bake.jpg?alt=media&token=dcb28e78-9c6e-4028-8f84-f69c9dcad8f4", description = "Bake for 10-12 minutes until the crust is golden. Add basil before serving.")
        ),
        storageInstructions = "Keep leftovers in the fridge for up to 3 days. Reheat in an oven or a dry pan, not a microwave.",
        reviews = listOf(
            Review(recipeId = "1", authorId = "888", rating = 5, imageUrl = "", comment = "tastes like naples", doTips = listOf("let it cool down a bit", "use a good tomato"), dontTips = listOf("don't let it burn")),
            Review(recipeId = "1", authorId = "202", rating = 4, imageUrl = "", comment = "Really authentic taste, very close to what I had in Naples. Dough texture was perfect after resting overnight.", doTips = listOf("Let the dough ferment for at least 12 hours", "Use high hydration dough for better crust"), dontTips = listOf("Don't rush the rising time", "Don't overload with toppings")),
            Review(recipeId = "1", authorId = "303", rating = 3, imageUrl = "", comment = "Very good recipe overall, but I found it slightly tricky to get the crust right in a home oven.", doTips = listOf("Preheat oven at maximum temperature for at least 30 minutes", "Use a pizza stone if possible"), dontTips = listOf("Don't use cold dough straight from the fridge", "Don't skip preheating"))
        )
    ),
    Recipe(
        id = "2",
        ownerId = "101",
        title = "Grilled Chicken Caesar Salad",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_images%2F2.jpg?alt=media&token=8425d8e8-4b03-463a-95c9-33958d761187",
        averageRating = 4.5,
        tags = listOf("healthy", "lunch"),
        difficulty = "Easy",
        cookingTime = 20,
        calories = 679, // Ricalcolato
        servings = 1,
        cost = 3.65, // Ricalcolato
        ingredients = listOf(
            Ingredient(name = "Chicken breast", quantity = 150.0, unit = "g", unitCost = 0.01, unitCalories = 1.6),
            Ingredient(name = "Romaine lettuce", quantity = 100.0, unit = "g", unitCost = 0.005, unitCalories = 0.1),
            Ingredient(name = "Parmesan cheese", quantity = 30.0, unit = "g", unitCost = 0.025, unitCalories = 4.3),
            Ingredient(name = "Croutons", quantity = 40.0, unit = "g", unitCost = 0.01, unitCalories = 4.0),
            Ingredient(name = "Caesar dressing", quantity = 2.0, unit = "tbsp", unitCost = 0.25, unitCalories = 70.0)
        ),
        steps = listOf(
            Step(title = "Grill chicken", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_grill_chicken.jpg?alt=media&token=9a1944bb-91f4-4fad-ba13-865605e52b03", description = "Season chicken and grill until cooked through. Slice into strips."),
            Step(title = "Prepare greens", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_chop.jpg?alt=media&token=b5a15111-3325-41a2-b9a8-cb5e0908a154", description = "Chop the lettuce and place it in a large bowl."),
            Step(title = "Assemble", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_salad_assemble.jpg?alt=media&token=cc2ea474-8882-456b-914d-a3614f3ae4ef", description = "Top lettuce with chicken, croutons, and parmesan. Drizzle with dressing.")
        ),
        storageInstructions = "Store dressing and croutons separately from greens and chicken. Refrigerate up to 2 days.",
        reviews = emptyList()
    ),
    Recipe(
        id = "3",
        ownerId = "101",
        title = "Bluefin Tuna Tartare",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_images%2F3.jpg?alt=media&token=64b14369-654b-4683-b9c8-1312dc2da51e",
        averageRating = 4.7,
        tags = listOf("raw", "appetizer", "gourmet"),
        difficulty = "Medium",
        cookingTime = 15,
        calories = 665, // Ricalcolato
        servings = 2,
        cost = 21.90, // Ricalcolato
        ingredients = listOf(
            Ingredient(name = "Sashimi-grade Bluefin Tuna", quantity = 250.0, unit = "g", unitCost = 0.08, unitCalories = 1.4),
            Ingredient(name = "Avocado", quantity = 1.0, unit = "pcs", unitCost = 1.50, unitCalories = 250.0),
            Ingredient(name = "Soy sauce", quantity = 2.0, unit = "tbsp", unitCost = 0.10, unitCalories = 10.0),
            Ingredient(name = "Sesame oil", quantity = 1.0, unit = "tsp", unitCost = 0.10, unitCalories = 40.0),
            Ingredient(name = "Lime juice", quantity = 1.0, unit = "tbsp", unitCost = 0.10, unitCalories = 5.0)
        ),
        steps = listOf(
            Step(title = "Dice tuna", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_tuna_dice.jpg?alt=media&token=fe037de8-7449-4cc1-8782-0e4414d06763", description = "Carefully dice the fresh tuna into small, even cubes."),
            Step(title = "Prepare avocado", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_avocado.jpg?alt=media&token=f7c20102-1ee1-48ac-8e8e-6d303af6065a", description = "Dice the avocado and gently mix with lime juice."),
            Step(title = "Season", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_spices.jpg?alt=media&token=37a3ad22-9581-4ca6-bd4d-7cc7c608f7f5", description = "Mix tuna with soy sauce and sesame oil. Plate using a ring mold.")
        ),
        storageInstructions = "Consume immediately after preparation. Unsafe to store.",
        reviews = emptyList()
    ),
    Recipe(
        id = "4",
        ownerId = "101",
        title = "Spaghetti Aglio e Olio",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_images%2F4.jpg?alt=media&token=5fe41a96-865a-438f-a97d-c1356b036ee8",
        averageRating = 4.6,
        tags = listOf("italian", "pasta", "quick", "cheap"),
        difficulty = "Easy",
        cookingTime = 15,
        calories = 1328, // Ricalcolato
        servings = 2,
        cost = 2.25, // Ricalcolato
        ingredients = listOf(
            Ingredient(name = "Spaghetti", quantity = 200.0, unit = "g", unitCost = 0.003, unitCalories = 3.5),
            Ingredient(name = "Olive oil", quantity = 5.0, unit = "tbsp", unitCost = 0.20, unitCalories = 120.0),
            Ingredient(name = "Garlic", quantity = 4.0, unit = "cloves", unitCost = 0.10, unitCalories = 5.0),
            Ingredient(name = "Red pepper flakes", quantity = 1.0, unit = "tsp", unitCost = 0.05, unitCalories = 5.0),
            Ingredient(name = "Parsley", quantity = 10.0, unit = "g", unitCost = 0.02, unitCalories = 0.3)
        ),
        steps = listOf(
            Step(title = "Boil pasta", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_pasta_boil.jpg?alt=media&token=8fa4c907-5469-4888-9dd8-15abbe178a7f", description = "Cook spaghetti in salted water until al dente."),
            Step(title = "Prepare oil", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_garlic_oil.jpg?alt=media&token=df94954e-ba06-4f3f-b66a-79acfdf7a825", description = "Gently heat olive oil and thinly sliced garlic in a pan until golden."),
            Step(title = "Mix", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_pasta_toss.jpg?alt=media&token=a7010e8a-7dc5-4fcf-ae34-942e54d58342", description = "Add red pepper flakes, drained pasta, and a splash of pasta water. Toss with parsley.")
        ),
        storageInstructions = "Best served immediately, but can be refrigerated for 2 days. Reheat with a drizzle of oil.",
        reviews = emptyList()
    ),
    Recipe(
        id = "5",
        ownerId = "101",
        title = "Black Truffle & Porcini Risotto",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_images%2F5.jpg?alt=media&token=0d82b712-fb30-4045-bb4e-25ad24d98e52",
        averageRating = 4.8,
        tags = listOf("italian", "rice", "gourmet", "luxury"),
        difficulty = "Hard",
        cookingTime = 45,
        calories = 881, // Ricalcolato
        servings = 2,
        cost = 61.38, // Ricalcolato
        ingredients = listOf(
            Ingredient(name = "Carnaroli rice", quantity = 160.0, unit = "g", unitCost = 0.008, unitCalories = 3.5),
            Ingredient(name = "Fresh Porcini mushrooms", quantity = 200.0, unit = "g", unitCost = 0.06, unitCalories = 0.2),
            Ingredient(name = "Black Truffle shavings", quantity = 15.0, unit = "g", unitCost = 3.0, unitCalories = 0.5),
            Ingredient(name = "Aged Parmesan", quantity = 60.0, unit = "g", unitCost = 0.025, unitCalories = 4.3),
            Ingredient(name = "Beef broth", quantity = 0.8, unit = "L", unitCost = 2.0, unitCalories = 20.0)
        ),
        steps = listOf(
            Step(title = "Toast rice", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_rice_toast.jpg?alt=media&token=7b7ec5a4-ef44-475c-b750-e8d2c46457ab", description = "Toast rice in a dry pan, then start adding hot broth ladle by ladle."),
            Step(title = "Add porcini", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_mushroom.jpg?alt=media&token=74f973eb-0158-43fd-b3e2-b527bee7c718", description = "Midway through cooking, add the sautéed porcini mushrooms."),
            Step(title = "Mantecatura", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_risotto.jpg?alt=media&token=3976369f-0580-4a5b-a602-aa4a69bf6619", description = "Remove from heat, stir in cold butter and parmesan. Shave fresh truffle on top.")
        ),
        storageInstructions = "Risotto is best eaten fresh. Leftovers can be refrigerated and used to make arancini within 3 days.",
        reviews = emptyList()
    ),
    Recipe(
        id = "6",
        ownerId = "101",
        title = "Pancakes with Maple Syrup",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_images%2F6.jpg?alt=media&token=9af0804a-96b5-452a-bcc0-875d222699cc",
        averageRating = 4.9,
        tags = listOf("breakfast", "sweet", "american"),
        difficulty = "Easy",
        cookingTime = 20,
        calories = 862, // Ricalcolato
        servings = 3,
        cost = 1.85, // Ricalcolato
        ingredients = listOf(
            Ingredient(name = "Flour", quantity = 150.0, unit = "g", unitCost = 0.002, unitCalories = 3.6),
            Ingredient(name = "Milk", quantity = 200.0, unit = "ml", unitCost = 0.001, unitCalories = 0.6),
            Ingredient(name = "Egg", quantity = 1.0, unit = "pcs", unitCost = 0.30, unitCalories = 70.0),
            Ingredient(name = "Baking powder", quantity = 1.0, unit = "tsp", unitCost = 0.05, unitCalories = 2.0),
            Ingredient(name = "Maple syrup", quantity = 50.0, unit = "ml", unitCost = 0.02, unitCalories = 2.6)
        ),
        steps = listOf(
            Step(title = "Mix batter", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_whisk.jpg?alt=media&token=13e56c10-dc60-48f3-af7e-b8d46214c3bf", description = "Whisk flour, baking powder, milk, and egg until smooth."),
            Step(title = "Cook", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_pancake_cook.jpg?alt=media&token=9bad112d-1b0c-4d8d-affc-c0a7633f675a", description = "Pour batter onto a hot buttered pan. Flip when bubbles form."),
            Step(title = "Serve", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_pancake_serve.jpg?alt=media&token=6fb900a3-6d4f-450f-956e-e04879847433", description = "Stack pancakes and pour maple syrup generously.")
        ),
        storageInstructions = "Cooked pancakes can be refrigerated for 3 days or frozen for up to 1 month.",
        reviews = emptyList()
    ),
    Recipe(
        id = "7",
        ownerId = "101",
        title = "Garlic Butter Lobster Tail",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_images%2F7.jpg?alt=media&token=2d5e8e3c-9a95-4630-bf05-3dfbc78aa65b",
        averageRating = 4.7,
        tags = listOf("seafood", "luxury", "dinner", "keto"),
        difficulty = "Medium",
        cookingTime = 20,
        calories = 1008, // Ricalcolato
        servings = 2,
        cost = 32.50, // Ricalcolato
        ingredients = listOf(
            Ingredient(name = "Lobster tails", quantity = 2.0, unit = "pcs", unitCost = 15.0, unitCalories = 130.0),
            Ingredient(name = "Butter", quantity = 100.0, unit = "g", unitCost = 0.015, unitCalories = 7.1),
            Ingredient(name = "Garlic", quantity = 3.0, unit = "cloves", unitCost = 0.10, unitCalories = 5.0),
            Ingredient(name = "Lemon", quantity = 1.0, unit = "pcs", unitCost = 0.50, unitCalories = 20.0),
            Ingredient(name = "Parsley", quantity = 10.0, unit = "g", unitCost = 0.02, unitCalories = 0.3)
        ),
        steps = listOf(
            Step(title = "Butterfly lobster", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_butterfly_lobster.jpg?alt=media&token=591eff6c-f5b6-450d-8273-fd48e3201fe5", description = "Cut the shell down the back and lift the meat to rest on top of the shell."),
            Step(title = "Make garlic butter", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_garlic_oil.jpg?alt=media&token=df94954e-ba06-4f3f-b66a-79acfdf7a825", description = "Melt butter and mix with minced garlic, parsley, and lemon juice."),
            Step(title = "Broil", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_lobster_broil.jpg?alt=media&token=1310ddcd-06de-4a91-a5e4-1e163659895a", description = "Brush lobster heavily with butter and broil for 8-10 minutes until meat is opaque.")
        ),
        storageInstructions = "Best served hot immediately. Leftover meat can be refrigerated for 2 days for lobster rolls.",
        reviews = emptyList()
    ),
    Recipe(
        id = "8",
        ownerId = "101",
        title = "Avocado Toast with Poached Egg",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_images%2F8.jpg?alt=media&token=9f78faa6-14b4-4040-9bff-1e7d42e341cd",
        averageRating = 4.6,
        tags = listOf("breakfast", "brunch", "healthy", "vegetarian"),
        difficulty = "Medium",
        cookingTime = 10,
        calories = 302, // Ricalcolato
        servings = 1,
        cost = 1.67, // Ricalcolato
        ingredients = listOf(
            Ingredient(name = "Sourdough bread", quantity = 1.0, unit = "slice", unitCost = 0.50, unitCalories = 100.0),
            Ingredient(name = "Avocado", quantity = 0.5, unit = "pcs", unitCost = 1.50, unitCalories = 250.0),
            Ingredient(name = "Egg", quantity = 1.0, unit = "pcs", unitCost = 0.30, unitCalories = 70.0),
            Ingredient(name = "Chili flakes", quantity = 0.5, unit = "tsp", unitCost = 0.05, unitCalories = 5.0),
            Ingredient(name = "Lemon juice", quantity = 1.0, unit = "tsp", unitCost = 0.03, unitCalories = 1.5)
        ),
        steps = listOf(
            Step(title = "Toast bread", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_toast_bread.jpg?alt=media&token=89418d3a-b860-4ef0-9221-9461f1ad2bbb", description = "Toast the sourdough slice until crispy."),
            Step(title = "Mash avocado", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_avocado.jpg?alt=media&token=f7c20102-1ee1-48ac-8e8e-6d303af6065a", description = "Mash avocado with lemon juice, salt, and pepper."),
            Step(title = "Poach egg", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_egg_poach.jpg?alt=media&token=f56b2ea3-1ee0-45be-a43e-a0ba30a2c362", description = "Create a whirlpool in simmering water, drop the egg, cook for 3 mins."),
            Step(title = "Assemble", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_avocado_toast_assemble.jpg?alt=media&token=69fc94e0-15e4-41e7-a923-df23783a39ff", description = "Spread avocado on toast, top with poached egg and chili flakes.")
        ),
        storageInstructions = "Best consumed immediately. Mashed avocado will turn brown if stored.",
        reviews = emptyList()
    ),
    Recipe(
        id = "9",
        ownerId = "101",
        title = "Chocolate Chip Cookies",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_images%2F9.jpg?alt=media&token=ea6abf68-eaea-4b02-963c-e7021a305ace",
        averageRating = 4.9,
        tags = listOf("dessert", "baking", "sweet", "snack"),
        difficulty = "Medium",
        cookingTime = 30,
        calories = 3357, // Ricalcolato
        servings = 12,
        cost = 6.35, // Ricalcolato
        ingredients = listOf(
            Ingredient(name = "Flour", quantity = 250.0, unit = "g", unitCost = 0.002, unitCalories = 3.6),
            Ingredient(name = "Butter", quantity = 150.0, unit = "g", unitCost = 0.015, unitCalories = 7.1),
            Ingredient(name = "Brown sugar", quantity = 100.0, unit = "g", unitCost = 0.004, unitCalories = 3.8),
            Ingredient(name = "Chocolate chips", quantity = 200.0, unit = "g", unitCost = 0.015, unitCalories = 5.0),
            Ingredient(name = "Vanilla extract", quantity = 1.0, unit = "tsp", unitCost = 0.20, unitCalories = 12.0)
        ),
        steps = listOf(
            Step(title = "Cream butter", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_whisk.jpg?alt=media&token=13e56c10-dc60-48f3-af7e-b8d46214c3bf", description = "Beat softened butter with sugar until creamy. Add vanilla."),
            Step(title = "Add dry ingredients", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_cookie_flour.jpg?alt=media&token=6e2f9dd6-23c9-4604-b47e-7180b084f7be", description = "Mix in flour until just combined, then fold in chocolate chips."),
            Step(title = "Bake", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_bake.jpg?alt=media&token=dcb28e78-9c6e-4028-8f84-f69c9dcad8f4", description = "Form dough balls and bake at 180°C for 10-12 minutes. Let cool.")
        ),
        storageInstructions = "Store baked cookies in an airtight container at room temperature for up to 5 days. Cookie dough can be frozen for 3 months.",
        reviews = emptyList()
    ),
    Recipe(
        id = "10",
        ownerId = "101",
        title = "Seared A5 Wagyu Ribeye",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_images%2F10.jpg?alt=media&token=d96cc62c-b44c-4b4f-981f-924d7fe418d3",
        averageRating = 5.0,
        tags = listOf("beef", "luxury", "steak", "dinner"),
        difficulty = "Medium",
        cookingTime = 10,
        calories = 810, // Ricalcolato
        servings = 2,
        cost = 60.23, // Ricalcolato
        ingredients = listOf(
            Ingredient(name = "A5 Wagyu Ribeye Steak", quantity = 200.0, unit = "g", unitCost = 0.30, unitCalories = 4.0),
            Ingredient(name = "Flaky sea salt", quantity = 1.0, unit = "tsp", unitCost = 0.01, unitCalories = 0.0),
            Ingredient(name = "Freshly ground black pepper", quantity = 0.5, unit = "tsp", unitCost = 0.05, unitCalories = 0.0),
            Ingredient(name = "Garlic", quantity = 2.0, unit = "cloves", unitCost = 0.10, unitCalories = 5.0)
        ),
        steps = listOf(
            Step(title = "Prep pan", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_cast_iron_heat.jpg?alt=media&token=32959248-1fad-4563-bdfd-17950ef1cf0c", description = "Heat a cast-iron skillet until it is smoking hot. No oil needed as Wagyu is highly marbled."),
            Step(title = "Sear", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_sear.jpg?alt=media&token=c2487832-766a-4de0-ae14-4d4d7b898165", description = "Sear the steak for exactly 60-90 seconds per side for a perfect medium-rare."),
            Step(title = "Rest", photo = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_steps%2Fstep_meat_rest.jpg?alt=media&token=2e16dbda-68e4-4126-ba30-260b701655f3", description = "Remove from heat, let it rest for 5 minutes, then slice thinly and top with flaky salt.")
        ),
        storageInstructions = "Best consumed immediately after resting. Leftovers can be refrigerated for 2 days but reheating will alter the perfect medium-rare texture.",
        reviews = emptyList()
    )
)