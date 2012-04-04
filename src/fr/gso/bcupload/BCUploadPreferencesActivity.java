package fr.gso.bcupload;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class BCUploadPreferencesActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(BCUploadPrefs.PREFS_NAME);
        addPreferencesFromResource(R.xml.preferences);
    }
}
