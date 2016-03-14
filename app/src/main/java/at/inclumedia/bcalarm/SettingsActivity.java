package at.inclumedia.bcalarm;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by boehni on 13/03/16.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
