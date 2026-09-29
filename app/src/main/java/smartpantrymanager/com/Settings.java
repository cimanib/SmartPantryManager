package smartpantrymanager.com;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;


public class Settings extends BaseActivity  {

    private Switch switchExpiryAlerts;
    private Spinner spinnerPreferredUnit;
    private Button btnSaveSettings;

    private SharedPreferences preferences;

    private static final String PREFS_NAME = "SmartPantrySettings";
    private static final String KEY_EXPIRY_ALERTS = "expiry_alerts";
    private static final String KEY_PREFERRED_UNIT = "preferred_unit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        setupNavigation();
        switchExpiryAlerts = findViewById(R.id.switchExpiryAlerts);

        spinnerPreferredUnit = findViewById(R.id.spinnerPreferredUnit);

        btnSaveSettings = findViewById(R.id.btnSaveSettings);

        preferences = getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
        );

        loadSettings();

        btnSaveSettings.setOnClickListener(v -> {
            saveSettings();
        });
    }

    private void loadSettings() {

        boolean expiryAlerts =
                preferences.getBoolean(
                        KEY_EXPIRY_ALERTS,
                        true
                );

        String preferredUnit =
                preferences.getString(
                        KEY_PREFERRED_UNIT,
                        "kg"
                );

        switchExpiryAlerts.setChecked(
                expiryAlerts
        );

        String[] units = getResources().getStringArray(R.array.unit_options);

        for (int i = 0; i < units.length; i++) {

            if (units[i].equalsIgnoreCase(preferredUnit)) {

                spinnerPreferredUnit.setSelection(i);
                break;
            }
        }
    }

    private void saveSettings() {

        boolean expiryAlerts = switchExpiryAlerts.isChecked();

        String preferredUnit = spinnerPreferredUnit.getSelectedItem().toString();

        preferences.edit().putBoolean(
                        KEY_EXPIRY_ALERTS,
                        expiryAlerts
                )
                .putString(
                        KEY_PREFERRED_UNIT,
                        preferredUnit
                )
                .apply();

        Toast.makeText(
                this,
                "Settings saved",
                Toast.LENGTH_SHORT
        ).show();
    }
}