package smartpantrymanager.com;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    Button findindRecipes,home, recipes,pantry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        findindRecipes =findViewById(R.id.btnFindRecipes);
        home =findViewById(R.id.btnHome);
        recipes =findViewById(R.id.btnRecipes);
        pantry =findViewById(R.id.btnPantry);


        findindRecipes.setOnClickListener(v->{
            Intent intent = new Intent(
                    MainActivity.this,
                    RecipesActivity.class
            );

            startActivity(intent);
        });

        recipes.setOnClickListener(v->{
            Intent intent = new Intent(
                    MainActivity.this,
                    RecipesActivity.class
            );

            startActivity(intent);

        });

        pantry.setOnClickListener(v->{

            Intent intent = new Intent(
                    MainActivity.this,
                    PantryActivity.class
            );

            startActivity(intent);

        });

    }
}
