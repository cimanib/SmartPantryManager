package smartpantrymanager.com;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class SuggestedRecipes extends BaseActivity  {
    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerSuggestedRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_suggested_recipes);
        setupNavigation();

        databaseHelper = new DatabaseHelper(this);

        recyclerSuggestedRecipes = findViewById(R.id.recyclerSuggestedRecipes);

        recyclerSuggestedRecipes.setLayoutManager(new LinearLayoutManager(this));
        loadSuggestedRecipes();
    }

    private boolean canMakeRecipe(
            Recipe recipe,
            List<PantryItem> pantryItems) {

        for (RecipeIngredient requiredIngredient :
                recipe.getIngredients()) {

            boolean found = false;

            for (PantryItem pantryItem :
                    pantryItems) {

                if (pantryItem.getIngredientName()
                        .equalsIgnoreCase(
                                requiredIngredient.getIngredientName())) {

                    found = true;
                    break;
                }
            }

            if (!found) {
                return false;
            }
        }

        return true;
    }
    private List<Recipe> getSuggestedRecipes(
            List<Recipe> recipes,
            List<PantryItem> pantryItems) {

        List<Recipe> suggestedRecipes = new ArrayList<>();

        for (Recipe recipe : recipes) {
            if (canMakeRecipe(recipe, pantryItems)) {
                suggestedRecipes.add(recipe);
            }
        }
        return suggestedRecipes;
    }
    private void loadSuggestedRecipes() {

        List<Recipe> recipes = databaseHelper.getAllRecipes();
        List<PantryItem> pantryItems = databaseHelper.getAllPantryItems();

        List<Recipe> suggestedRecipes = getSuggestedRecipes(recipes, pantryItems);

        RecipeAdapter recipeAdapter = new RecipeAdapter(this, suggestedRecipes);
        recyclerSuggestedRecipes.setAdapter(recipeAdapter);
    }
}