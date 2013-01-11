package bs.howdy.PrayerList.Service;

import org.json.*;

public interface JsonSerializer {
	JSONObject serialize();
	void deserialize(final JSONObject json);
}
