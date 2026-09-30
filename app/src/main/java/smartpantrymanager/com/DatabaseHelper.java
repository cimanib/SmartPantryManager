package smartpantrymanager.com;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 5;

    // Pantry table
    public static final String TABLE_PANTRY = "pantry_items";

    public static final String COLUMN_PANTRY_ID = "id";
    public static final String COLUMN_INGREDIENT_NAME = "ingredient_name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_EXPIRY_DATE = "expiry_date";



    // Recipe table
    public static final String TABLE_RECIPES = "recipes";

    public static final String COLUMN_RECIPE_ID = "id";
    public static final String COLUMN_RECIPE_NAME = "name";
    public static final String COLUMN_PREPARATION_STEPS = "preparation_steps";

    // Recipe ingredients table
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";

    public static final String COLUMN_RECIPE_INGREDIENT_ID = "id";
    public static final String COLUMN_RECIPE_ID_FK = "recipe_id";
    public static final String COLUMN_RECIPE_INGREDIENT_NAME = "ingredient_name";
    public static final String COLUMN_RECIPE_INGREDIENT_QUANTITY = "quantity";
    public static final String COLUMN_RECIPE_INGREDIENT_UNIT = "unit";

  //Settings Table
    public static final String TABLE_SETTINGS = "settings";

    public static final String COLUMN_SETTINGS_ID = "id";
    public static final String COLUMN_EXPIRY_ALERTS = "expiry_alerts";
    public static final String COLUMN_PREFERRED_UNIT = "preferred_unit";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // 1. Create pantry table
        String createPantryTable = "CREATE TABLE " + TABLE_PANTRY + " (" +
                COLUMN_PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_INGREDIENT_NAME + " TEXT NOT NULL, " +
                COLUMN_QUANTITY + " REAL NOT NULL, " +
                COLUMN_UNIT + " TEXT NOT NULL, " +
                COLUMN_EXPIRY_DATE + " TEXT" +
                ")";

        db.execSQL(createPantryTable);


        // 2. Create recipes table
        String createRecipesTable = "CREATE TABLE " + TABLE_RECIPES + " (" +
                COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                COLUMN_PREPARATION_STEPS + " TEXT NOT NULL" +
                ")";

        db.execSQL(createRecipesTable);


        // 3. Create recipe ingredients table
        String createRecipeIngredientsTable =
                "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                        COLUMN_RECIPE_INGREDIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_RECIPE_ID_FK + " INTEGER NOT NULL, " +
                        COLUMN_RECIPE_INGREDIENT_NAME + " TEXT NOT NULL, " +
                        COLUMN_RECIPE_INGREDIENT_QUANTITY + " REAL NOT NULL, " +
                        COLUMN_RECIPE_INGREDIENT_UNIT + " TEXT NOT NULL, " +
                        "FOREIGN KEY (" + COLUMN_RECIPE_ID_FK + ") REFERENCES " +
                        TABLE_RECIPES + "(" + COLUMN_RECIPE_ID + ")" +
                        ")";

        db.execSQL(createRecipeIngredientsTable);


        // 4. Create settings table
        String createSettingsTable =
                "CREATE TABLE " + TABLE_SETTINGS + " (" +
                        COLUMN_SETTINGS_ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_EXPIRY_ALERTS + " INTEGER NOT NULL, " +
                        COLUMN_PREFERRED_UNIT + " TEXT NOT NULL" +
                        ")";

        db.execSQL(createSettingsTable);


        // 5. Seed the recipe collection
        seedRecipes(db);
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        db.execSQL(
                "DROP TABLE IF EXISTS " +
                        TABLE_RECIPE_INGREDIENTS
        );

        db.execSQL(
                "DROP TABLE IF EXISTS " +
                        TABLE_RECIPES
        );

        db.execSQL(
                "DROP TABLE IF EXISTS " +
                        TABLE_SETTINGS
        );

        db.execSQL(
                "DROP TABLE IF EXISTS " +
                        TABLE_PANTRY
        );

        onCreate(db);
    }
    public long addPantryItem(PantryItem item) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_INGREDIENT_NAME, item.getIngredientName());
        values.put(COLUMN_QUANTITY, item.getQuantity());
        values.put(COLUMN_UNIT, item.getUnit());
        values.put(COLUMN_EXPIRY_DATE, item.getExpiryDate());

        long id = db.insert(
                TABLE_PANTRY,
                null,
                values
        );

        db.close();

        return id;
    }
    public java.util.List<PantryItem> getAllPantryItems() {

        java.util.List<PantryItem> pantryItems = new java.util.ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_PANTRY,
                null,
                null,
                null,
                null,
                null,
                COLUMN_INGREDIENT_NAME + " ASC"
        );

        if (cursor.moveToFirst()) {

            do {

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PANTRY_ID)
                );

                String ingredientName = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_INGREDIENT_NAME)
                );

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)
                );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_UNIT)
                );

                String expiryDate = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_EXPIRY_DATE)
                );

                PantryItem item = new PantryItem(
                        id,
                        ingredientName,
                        quantity,
                        unit,
                        expiryDate
                );

                pantryItems.add(item);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return pantryItems;
    }
    public int updatePantryItem(PantryItem item) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_INGREDIENT_NAME, item.getIngredientName());
        values.put(COLUMN_QUANTITY, item.getQuantity());
        values.put(COLUMN_UNIT, item.getUnit());
        values.put(COLUMN_EXPIRY_DATE, item.getExpiryDate());

        int rowsUpdated = db.update(
                TABLE_PANTRY,
                values,
                COLUMN_PANTRY_ID + " = ?",
                new String[]{
                        String.valueOf(item.getId())
                }
        );

        db.close();

        return rowsUpdated;
    }
    public java.util.List<Recipe> getAllRecipes() {

        java.util.List<Recipe> recipes = new java.util.ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor recipeCursor = db.query(
                TABLE_RECIPES,
                null,
                null,
                null,
                null,
                null,
                COLUMN_RECIPE_NAME + " ASC"
        );

        if (recipeCursor.moveToFirst()) {

            do {

                int recipeId = recipeCursor.getInt(
                        recipeCursor.getColumnIndexOrThrow(
                                COLUMN_RECIPE_ID
                        )
                );

                String recipeName = recipeCursor.getString(
                        recipeCursor.getColumnIndexOrThrow(
                                COLUMN_RECIPE_NAME
                        )
                );

                String preparationSteps = recipeCursor.getString(
                        recipeCursor.getColumnIndexOrThrow(
                                COLUMN_PREPARATION_STEPS
                        )
                );

                // Get ingredients for this recipe
                java.util.List<RecipeIngredient> ingredients =
                        getRecipeIngredients(db, recipeId);

                Recipe recipe = new Recipe(
                        recipeId,
                        recipeName,
                        preparationSteps,
                        ingredients
                );

                recipes.add(recipe);

            } while (recipeCursor.moveToNext());
        }

        recipeCursor.close();
        db.close();

        return recipes;
    }
    public PantryItem getPantryItem(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_PANTRY,
                null,
                COLUMN_PANTRY_ID  + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        PantryItem item = null;

        if (cursor.moveToFirst()) {

            String ingredientName = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_INGREDIENT_NAME)
            );

            double quantity = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)
            );

            String unit = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_UNIT)
            );

            String expiryDate = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_EXPIRY_DATE)
            );

            item = new PantryItem(
                    id,
                    ingredientName,
                    quantity,
                    unit,
                    expiryDate
            );
        }

        cursor.close();

        return item;
    }
    public int deletePantryItem(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(
                TABLE_PANTRY,
                COLUMN_PANTRY_ID  + " = ?",
                new String[]{String.valueOf(id)}
        );
    }
    private java.util.List<RecipeIngredient> getRecipeIngredients(
            SQLiteDatabase db,
            int recipeId) {

        java.util.List<RecipeIngredient> ingredients = new java.util.ArrayList<>();

        Cursor cursor = db.query(
                TABLE_RECIPE_INGREDIENTS,
                null,
                COLUMN_RECIPE_ID_FK + " = ?",
                new String[]{
                        String.valueOf(recipeId)
                },
                null,
                null,
                COLUMN_RECIPE_INGREDIENT_NAME + " ASC"
        );

        if (cursor.moveToFirst()) {

            do {

                int ingredientId = cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_RECIPE_INGREDIENT_ID
                        )
                );

                String ingredientName = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_RECIPE_INGREDIENT_NAME
                        )
                );

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_RECIPE_INGREDIENT_QUANTITY
                        )
                );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_RECIPE_INGREDIENT_UNIT
                        )
                );

                RecipeIngredient ingredient =
                        new RecipeIngredient(
                                ingredientId,
                                recipeId,
                                ingredientName,
                                quantity,
                                unit
                        );

                ingredients.add(ingredient);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return ingredients;
    }
    private void seedRecipes(SQLiteDatabase db) {

        addRecipe(
                db,
                "Chicken Curry",
                "1. Fry onion and garlic. 2. Add chicken and curry powder. 3. Add tomatoes and cook until the chicken is done. 4. Serve with rice.",
                new String[]{"Chicken", "Rice", "Onion", "Tomato", "Curry Powder"},
                new double[]{500, 2, 1, 2, 1},
                new String[]{"g", "cup", "unit", "unit", "tbsp"}
        );

        addRecipe(
                db,
                "Chicken Fried Rice",
                "1. Cook the chicken. 2. Fry onion and vegetables. 3. Add cooked rice and chicken. 4. Stir and season.",
                new String[]{"Chicken", "Rice", "Onion", "Carrot"},
                new double[]{300, 2, 1, 1},
                new String[]{"g", "cup", "unit", "unit"}
        );

        addRecipe(
                db,
                "Beef Stew",
                "1. Brown the beef. 2. Add onion and potatoes. 3. Add tomatoes and water. 4. Simmer until tender.",
                new String[]{"Beef", "Potato", "Onion", "Tomato"},
                new double[]{500, 3, 1, 2},
                new String[]{"g", "unit", "unit", "unit"}
        );

        addRecipe(
                db,
                "Vegetable Stir Fry",
                "1. Chop the vegetables. 2. Heat oil in a pan. 3. Stir fry vegetables until tender. 4. Season and serve.",
                new String[]{"Carrot", "Onion", "Bell Pepper", "Cabbage"},
                new double[]{2, 1, 1, 200},
                new String[]{"unit", "unit", "unit", "g"}
        );

        addRecipe(
                db,
                "Tomato Pasta",
                "1. Cook pasta. 2. Fry onion and garlic. 3. Add tomatoes and simmer. 4. Mix with pasta.",
                new String[]{"Pasta", "Tomato", "Onion", "Garlic"},
                new double[]{250, 3, 1, 2},
                new String[]{"g", "unit", "unit", "clove"}
        );

        addRecipe(
                db,
                "Chicken Pasta",
                "1. Cook pasta. 2. Cook chicken with onion. 3. Add tomatoes. 4. Combine with pasta.",
                new String[]{"Chicken", "Pasta", "Tomato", "Onion"},
                new double[]{300, 250, 2, 1},
                new String[]{"g", "g", "unit", "unit"}
        );

        addRecipe(
                db,
                "Beef Burger",
                "1. Season the beef. 2. Shape into patties. 3. Cook the patties. 4. Serve in burger buns with tomato and lettuce.",
                new String[]{"Beef", "Burger Bun", "Tomato", "Lettuce"},
                new double[]{300, 2, 1, 2},
                new String[]{"g", "unit", "unit", "leaves"}
        );

        addRecipe(
                db,
                "Chicken Sandwich",
                "1. Cook and slice chicken. 2. Toast bread. 3. Add lettuce and tomato. 4. Assemble the sandwich.",
                new String[]{"Chicken", "Bread", "Lettuce", "Tomato"},
                new double[]{200, 4, 2, 1},
                new String[]{"g", "slice", "leaves", "unit"}
        );

        addRecipe(
                db,
                "Vegetable Soup",
                "1. Chop vegetables. 2. Add vegetables to a pot with water. 3. Season. 4. Simmer until vegetables are soft.",
                new String[]{"Carrot", "Potato", "Onion", "Cabbage"},
                new double[]{2, 2, 1, 200},
                new String[]{"unit", "unit", "unit", "g"}
        );

        addRecipe(
                db,
                "Omelette",
                "1. Beat the eggs. 2. Fry onion and tomato. 3. Add eggs. 4. Cook until set.",
                new String[]{"Egg", "Onion", "Tomato", "Cheese"},
                new double[]{3, 1, 1, 50},
                new String[]{"unit", "unit", "unit", "g"}
        );

        addRecipe(
                db,
                "Egg Fried Rice",
                "1. Cook the rice. 2. Scramble the eggs. 3. Add onion and rice. 4. Stir fry and season.",
                new String[]{"Egg", "Rice", "Onion"},
                new double[]{2, 2, 1},
                new String[]{"unit", "cup", "unit"}
        );

        addRecipe(
                db,
                "Tuna Sandwich",
                "1. Drain the tuna. 2. Mix tuna with mayonnaise. 3. Add lettuce. 4. Place between slices of bread.",
                new String[]{"Tuna", "Bread", "Mayonnaise", "Lettuce"},
                new double[]{1, 4, 2, 2},
                new String[]{"can", "slice", "tbsp", "leaves"}
        );

        addRecipe(
                db,
                "Chicken Wrap",
                "1. Cook the chicken. 2. Chop tomato and lettuce. 3. Place ingredients on the wrap. 4. Roll and serve.",
                new String[]{"Chicken", "Wrap", "Tomato", "Lettuce"},
                new double[]{250, 2, 1, 2},
                new String[]{"g", "unit", "unit", "leaves"}
        );

        addRecipe(
                db,
                "Potato Salad",
                "1. Boil potatoes. 2. Allow them to cool. 3. Add onion and mayonnaise. 4. Mix and serve.",
                new String[]{"Potato", "Onion", "Mayonnaise"},
                new double[]{4, 1, 3},
                new String[]{"unit", "unit", "tbsp"}
        );

        addRecipe(
                db,
                "Chicken and Potatoes",
                "1. Season the chicken and potatoes. 2. Place them in a baking dish. 3. Bake until cooked. 4. Serve hot.",
                new String[]{"Chicken", "Potato", "Onion"},
                new double[]{500, 4, 1},
                new String[]{"g", "unit", "unit"}
        );
    }
    private void addRecipe(
            SQLiteDatabase db,
            String recipeName,
            String preparationSteps,
            String[] ingredientNames,
            double[] quantities,
            String[] units) {

        ContentValues recipeValues = new ContentValues();

        recipeValues.put(
                COLUMN_RECIPE_NAME,
                recipeName
        );

        recipeValues.put(
                COLUMN_PREPARATION_STEPS,
                preparationSteps
        );

        long recipeId = db.insert(
                TABLE_RECIPES,
                null,
                recipeValues
        );

        if (recipeId == -1) {
            return;
        }

        for (int i = 0; i < ingredientNames.length; i++) {

            ContentValues ingredientValues =
                    new ContentValues();

            ingredientValues.put(
                    COLUMN_RECIPE_ID_FK,
                    recipeId
            );

            ingredientValues.put(
                    COLUMN_RECIPE_INGREDIENT_NAME,
                    ingredientNames[i]
            );

            ingredientValues.put(
                    COLUMN_RECIPE_INGREDIENT_QUANTITY,
                    quantities[i]
            );

            ingredientValues.put(
                    COLUMN_RECIPE_INGREDIENT_UNIT,
                    units[i]
            );

            db.insert(
                    TABLE_RECIPE_INGREDIENTS,
                    null,
                    ingredientValues
            );
        }
    }
    public void saveSettings(
            boolean expiryAlerts,
            String preferredUnit) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(
                COLUMN_SETTINGS_ID,
                1
        );

        values.put(
                COLUMN_EXPIRY_ALERTS,
                expiryAlerts ? 1 : 0
        );

        values.put(
                COLUMN_PREFERRED_UNIT,
                preferredUnit
        );

        db.insertWithOnConflict(
                TABLE_SETTINGS,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );

        db.close();
    }
    public SettingsModel getSettings() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_SETTINGS,
                null,
                COLUMN_SETTINGS_ID + " = ?",
                new String[]{"1"},
                null,
                null,
                null
        );

        SettingsModel settings;

        if (cursor.moveToFirst()) {

            boolean expiryAlerts =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                    COLUMN_EXPIRY_ALERTS
                            )
                    ) == 1;

            String preferredUnit =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    COLUMN_PREFERRED_UNIT
                            )
                    );

            settings = new SettingsModel(
                    expiryAlerts,
                    preferredUnit
            );

        } else {

            // Default settings
            settings = new SettingsModel(
                    true,
                    "kg"
            );
        }

        cursor.close();
        db.close();

        return settings;
    }
}