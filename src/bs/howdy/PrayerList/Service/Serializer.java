package bs.howdy.PrayerList.Service;

import org.json.*;

public interface Serializer {
	String serialize();
	void deserialize(String json);
}
