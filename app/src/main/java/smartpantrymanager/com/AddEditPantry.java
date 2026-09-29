package smartpantrymanager.com;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddEditPantry extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_pantry);
        setupNavigation();

        Button btnSavePantryItem = findViewById(R.id.btnSavePantryItem);
        EditText etIngredientName = findViewById(R.id.etIngredientName);
        EditText etQuantity = findViewById(R.id.etQuantity);
        Spinner spinnerUnit = findViewById(R.id.spinnerUnit);
        EditText etExpiryDate = findViewById(R.id.etExpiryDate);

        btnSavePantryItem.setOnClickListener(v -> {

            String ingredientName =
                    etIngredientName.getText().toString().trim();

            String quantityText =
                    etQuantity.getText().toString().trim();

            String unit =
                    spinnerUnit.getSelectedItem().toString();

            String expiryDate =
                    etExpiryDate.getText().toString().trim();

            if (ingredientName.isEmpty()) {
                etIngredientName.setError("Enter an ingredient");
                return;
            }

            if (quantityText.isEmpty()) {
                etQuantity.setError("Enter a quantity");
                return;
            }

            double quantity = Double.parseDouble(quantityText);

            PantryItem item = new PantryItem(
                    ingredientName,
                    quantity,
                    unit,
                    expiryDate
            );

            DatabaseHelper dbHelper =
                    new DatabaseHelper(this);

            long result = dbHelper.addPantryItem(item);

            Toast.makeText(
                    this,
                    "Database result: " + result,
                    Toast.LENGTH_LONG
            ).show();

            if (result != -1) {
                finish();
            }
        });
    }
}