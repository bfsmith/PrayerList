package bs.howdy.PrayerList.Activities;

import java.text.SimpleDateFormat;

import com.google.inject.Inject;

import roboguice.activity.RoboActivity;
import roboguice.activity.RoboPreferenceActivity;
import roboguice.inject.InjectView;

import bs.howdy.PrayerList.*;
import bs.howdy.PrayerList.Entities.*;
import bs.howdy.PrayerList.Service.PrayerService;
import bs.howdy.PrayerList.Service.ReminderService;
import bs.howdy.PrayerList.Service.SerializerService;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.*;

public class Settings extends RoboPreferenceActivity{
	@Inject SerializerService mSerializerService;
	private String file = "data.txt";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        
		Preference importPrayers = findPreference(Constants.Preferences.IMPORT_PRAYERS);
		importPrayers.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				mSerializerService.loadAppData(App.getContext(), file);
				Toast.makeText(App.getContext(), R.string.PrayersImportedSuccess, Toast.LENGTH_SHORT).show();
				return true;
			}
        });
		Preference exportPrayers = findPreference(Constants.Preferences.EXPORT_PRAYERS);
		exportPrayers.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				mSerializerService.saveAppData(App.getContext(), file);
				Toast.makeText(App.getContext(), R.string.PrayersExportedSuccess, Toast.LENGTH_SHORT).show();
				return true;
			}
        });
	}
}
