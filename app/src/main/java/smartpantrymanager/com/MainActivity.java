package smartpantrymanager.com;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    Button btnSuggestedRecipes,btnPantry,btnRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPantry = findViewById(R.id.btnPantry);
        btnPantry.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, PantryActivity.class);

            startActivity(intent);
        });

        btnSuggestedRecipes = findViewById(R.id.btnSuggestedRecipes);
        btnSuggestedRecipes.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    SuggestedRecipes.class
            );

            startActivity(intent);
        });

        btnRecipes = findViewById(R.id.btnRecipes);

        btnRecipes.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    RecipeCollection.class
            );

            startActivity(intent);
        });

        Button btnSettings =
                findViewById(R.id.btnSettings);

        btnSettings.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    Settings.class
            );

            startActivity(intent);
        });
    }
}