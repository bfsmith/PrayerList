package bs.howdy.PrayerList;

import java.util.List;

import com.google.inject.Module;

import roboguice.application.RoboApplication;
import android.content.Context;

public class App extends RoboApplication {
	private static Context _context;
	
	public void onCreate() {
		super.onCreate();
		_context = this;
	}
	
	public static Context getContext() {
		return _context;
	}
	
	protected void addApplicationModules(List<Module> modules) {
        modules.add(new GuiceModule());
    }
}
