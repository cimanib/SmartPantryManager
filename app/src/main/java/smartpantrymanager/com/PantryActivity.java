package smartpantrymanager.com;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PantryActivity extends BaseActivity {

    private RecyclerView recyclerPantry;
    private TextView tvEmptyPantry;

    private DatabaseHelper databaseHelper;
    private PantryAdapter pantryAdapter;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);
        setupNavigation();
        Button btnAddPantryItem = findViewById(R.id.btnAddPantryItem);

        btnAddPantryItem.setOnClickListener(v -> {

            Intent intent = new Intent(
                    PantryActivity.this,
                    AddEditPantry.class
            );

            startActivity(intent);
        });

        recyclerPantry = findViewById(R.id.recyclerPantry);
        tvEmptyPantry = findViewById(R.id.tvEmptyPantry);
        databaseHelper = new DatabaseHelper(this);

        recyclerPantry.setLayoutManager(
                new LinearLayoutManager(this)
        );
        loadPantryItems();
    }
    private void loadPantryItems() {

        List<PantryItem> pantryItems =
                databaseHelper.getAllPantryItems();

        if (pantryItems.isEmpty()) {

            tvEmptyPantry.setVisibility(View.VISIBLE);

            recyclerPantry.setVisibility(View.GONE);

        } else {

            tvEmptyPantry.setVisibility(View.GONE);

            recyclerPantry.setVisibility(View.VISIBLE);

            pantryAdapter =
                    new PantryAdapter(pantryItems);

            recyclerPantry.setAdapter(pantryAdapter);
        }
    }
}