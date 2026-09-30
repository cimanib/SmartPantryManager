package smartpantrymanager.com;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class AddEditPantry extends BaseActivity {

    private EditText etIngredientName;
    private EditText etQuantity;
    private EditText etExpiryDate;
    private Spinner spinnerUnit;

    private Button btnSavePantryItem;
    private Button btnDeletePantryItem;

    private DatabaseHelper dbHelper;

    // -1 means we are adding a new item
    private int pantryId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_edit_pantry);

        // Set up hamburger menu / navigation drawer
        setupNavigation();

        // Database
        dbHelper = new DatabaseHelper(this);

        // Find views
        etIngredientName = findViewById(R.id.etIngredientName);
        etQuantity = findViewById(R.id.etQuantity);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        etExpiryDate = findViewById(R.id.etExpiryDate);

        btnSavePantryItem = findViewById(R.id.btnSavePantryItem);
        btnDeletePantryItem = findViewById(R.id.btnDeletePantryItem);

        // Check whether we are adding or editing
        pantryId = getIntent().getIntExtra("pantry_id", -1);

        if (pantryId == -1) {

            // ADD MODE
            btnDeletePantryItem.setVisibility(View.GONE);

        } else {

            // EDIT MODE
            btnDeletePantryItem.setVisibility(View.VISIBLE);

            loadPantryItem();
        }

        // Save button
        btnSavePantryItem.setOnClickListener(v -> savePantryItem());

        // Delete button
        btnDeletePantryItem.setOnClickListener(v -> deletePantryItem());
    }
    private void loadPantryItem() {

        PantryItem item = dbHelper.getPantryItem(pantryId);

        if (item == null) {

            Toast.makeText(
                    this,
                    "Pantry item not found",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        // Load ingredient name
        etIngredientName.setText(
                item.getIngredientName()
        );

        // Load quantity
        etQuantity.setText(
                String.valueOf(item.getQuantity())
        );

        // Load expiry date
        if (item.getExpiryDate() != null) {

            etExpiryDate.setText(
                    item.getExpiryDate()
            );
        }

        // Select saved unit in Spinner
        String savedUnit = item.getUnit();

        if (savedUnit != null) {

            for (int i = 0; i < spinnerUnit.getCount(); i++) {

                String spinnerUnitValue =
                        spinnerUnit
                                .getItemAtPosition(i)
                                .toString();

                if (spinnerUnitValue.equals(savedUnit)) {

                    spinnerUnit.setSelection(i);
                    break;
                }
            }
        }
    }

    /**
     * Add a new pantry item or update an existing one.
     */
    private void savePantryItem() {

        String ingredientName =
                etIngredientName
                        .getText()
                        .toString()
                        .trim();

        String quantityText =
                etQuantity
                        .getText()
                        .toString()
                        .trim();

        String unit =
                spinnerUnit
                        .getSelectedItem()
                        .toString();

        String expiryDate =
                etExpiryDate
                        .getText()
                        .toString()
                        .trim();

        // Validate ingredient
        if (ingredientName.isEmpty()) {

            etIngredientName.setError(
                    "Enter an ingredient"
            );

            etIngredientName.requestFocus();

            return;
        }

        // Validate quantity
        if (quantityText.isEmpty()) {

            etQuantity.setError(
                    "Enter a quantity"
            );

            etQuantity.requestFocus();

            return;
        }

        double quantity;

        try {

            quantity = Double.parseDouble(quantityText);

        } catch (NumberFormatException e) {

            etQuantity.setError(
                    "Enter a valid quantity"
            );

            etQuantity.requestFocus();

            return;
        }

        long result;

        if (pantryId == -1) {

            // --------------------------------
            // ADD NEW PANTRY ITEM
            // --------------------------------

            PantryItem item = new PantryItem(
                    ingredientName,
                    quantity,
                    unit,
                    expiryDate
            );

            result = dbHelper.addPantryItem(item);

            if (result != -1) {

                Toast.makeText(
                        this,
                        "Ingredient added",
                        Toast.LENGTH_SHORT
                ).show();

                finish();
            } else {

                Toast.makeText(
                        this,
                        "Failed to add ingredient",
                        Toast.LENGTH_SHORT
                ).show();
            }

        } else {

            PantryItem item = new PantryItem(
                    pantryId,
                    ingredientName,
                    quantity,
                    unit,
                    expiryDate
            );

            result = dbHelper.updatePantryItem(item);

            if (result > 0) {

                Toast.makeText(
                        this,
                        "Ingredient updated",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        this,
                        "No changes were made",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }

    /**
     * Delete the current pantry item.
     */
    private void deletePantryItem() {

        if (pantryId == -1) {
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Delete Ingredient")
                .setMessage(
                        "Are you sure you want to delete this ingredient?"
                )
                .setPositiveButton(
                        "Delete",
                        (dialog, which) -> {

                            int result =
                                    dbHelper.deletePantryItem(
                                            pantryId
                                    );

                            if (result > 0) {

                                Toast.makeText(
                                        this,
                                        "Ingredient deleted",
                                        Toast.LENGTH_SHORT
                                ).show();

                                finish();

                            } else {

                                Toast.makeText(
                                        this,
                                        "Failed to delete ingredient",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                )
                .setNegativeButton(
                        "Cancel",
                        null
                )
                .show();
    }
}