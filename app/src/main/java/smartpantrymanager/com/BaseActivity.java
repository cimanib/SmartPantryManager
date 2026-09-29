package smartpantrymanager.com;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity {

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    protected void setupNavigation() {

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        setSupportActionBar(toolbar);

        // Show hamburger menu
        toolbar.setNavigationIcon(R.drawable.ic_menu);

        // Open navigation drawer when hamburger is clicked
        toolbar.setNavigationOnClickListener(v -> {
            drawerLayout.openDrawer(Gravity.LEFT);
        });

        navigationView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));

            } else if (id == R.id.nav_pantry) {
                startActivity(new Intent(this, PantryActivity.class));

            } else if (id == R.id.nav_recipes) {
                startActivity(new Intent(this, RecipeCollection.class));

            } else if (id == R.id.nav_suggested) {
                startActivity(new Intent(this, SuggestedRecipes.class));

            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(this, Settings.class));
            }

            drawerLayout.closeDrawer(Gravity.LEFT);

            return true;
        });
    }
}