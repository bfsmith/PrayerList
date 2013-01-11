package bs.howdy.PrayerList.Service;

import java.io.IOException;

import org.json.JSONException;

import android.content.Context;

public interface SerializerService {
	public boolean loadAppData( Context appContext, final String baseName);
	public boolean saveAppData( Context appContext, final String baseName);
}
