package bs.howdy.PrayerList.Entities;

import android.content.Context;
import android.app.Application;

public class PrayerListApp extends Application {
	private static PrayerListApp instance;

    public PrayerListApp() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }
}
