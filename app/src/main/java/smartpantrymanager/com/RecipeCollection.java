package smartpantrymanager.com;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecipeCollection extends BaseActivity  {

    private RecyclerView recyclerRecipes;
    private DatabaseHelper databaseHelper;
    private RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_collection);
        setupNavigation();
        recyclerRecipes = findViewById(R.id.recyclerRecipes);
        databaseHelper = new DatabaseHelper(this);

        recyclerRecipes.setLayoutManager(new LinearLayoutManager(this)
        );

        loadRecipes();
    }

    private void loadRecipes() {

        List<Recipe> recipes = databaseHelper.getAllRecipes();
        recipeAdapter = new RecipeAdapter(this, recipes);
        recyclerRecipes.setAdapter(recipeAdapter);
    }
}