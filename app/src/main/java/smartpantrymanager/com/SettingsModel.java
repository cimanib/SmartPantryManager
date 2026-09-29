package smartpantrymanager.com;

public class SettingsModel {

    private boolean expiryAlerts;
    private String preferredUnit;

    public SettingsModel(
            boolean expiryAlerts,
            String preferredUnit) {

        this.expiryAlerts = expiryAlerts;
        this.preferredUnit = preferredUnit;
    }

    public boolean isExpiryAlerts() {
        return expiryAlerts;
    }

    public String getPreferredUnit() {
        return preferredUnit;
    }
}