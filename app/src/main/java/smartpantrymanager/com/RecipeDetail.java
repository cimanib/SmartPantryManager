package smartpantrymanager.com;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class RecipeDetail extends BaseActivity  {

    private TextView tvRecipeName;
    private TextView tvRecipeIngredients;
    private TextView tvRecipeMethod;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_detail);
        setupNavigation();
        tvRecipeName = findViewById(R.id.tvRecipeName);
        tvRecipeIngredients = findViewById(R.id.tvRecipeIngredients);
        tvRecipeMethod = findViewById(R.id.tvRecipeMethod);

        databaseHelper = new DatabaseHelper(this);

        int recipeId = getIntent().getIntExtra("recipe_id", -1);

        if (recipeId != -1) {
            loadRecipe(recipeId);
        }
    }

    private void loadRecipe(int recipeId) {

        List<Recipe> recipes = databaseHelper.getAllRecipes();

        for (Recipe recipe : recipes) {

            if (recipe.getId() == recipeId) {

                tvRecipeName.setText(recipe.getName());

                tvRecipeMethod.setText(recipe.getPreparationSteps());

                displayIngredients(recipe.getIngredients());

                break;
            }
        }
    }
    private void displayIngredients(
            List<RecipeIngredient> ingredients) {

        StringBuilder ingredientText = new StringBuilder();

        for (RecipeIngredient ingredient : ingredients) {
            ingredientText
                    .append("• ")
                    .append(ingredient.getIngredientName())
                    .append(" - ")
                    .append(ingredient.getQuantity())
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append("\n");
        }
        tvRecipeIngredients.setText(ingredientText.toString());
    }
}