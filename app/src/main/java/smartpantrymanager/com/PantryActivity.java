package smartpantrymanager.com;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PantryActivity extends BaseActivity {

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerPantry;
    private TextView tvEmptyPantry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pantry);

        setupNavigation();

        databaseHelper = new DatabaseHelper(this);

        recyclerPantry = findViewById(R.id.recyclerPantry);
        tvEmptyPantry = findViewById(R.id.tvEmptyPantry);

        Button btnAddPantryItem =
                findViewById(R.id.btnAddPantryItem);

        recyclerPantry.setLayoutManager(
                new LinearLayoutManager(this)
        );

        btnAddPantryItem.setOnClickListener(v -> {

            Intent intent = new Intent(
                    PantryActivity.this,
                    AddEditPantry.class
            );

            startActivity(intent);
        });

        loadPantryItems();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Reload the database every time
        // we return to the Pantry screen
        if (databaseHelper != null) {
            loadPantryItems();
        }
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

            PantryAdapter adapter =
                    new PantryAdapter(
                            this,
                            pantryItems
                    );

            recyclerPantry.setAdapter(adapter);
        }
    }
}