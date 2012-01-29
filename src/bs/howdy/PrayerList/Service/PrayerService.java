package bs.howdy.PrayerList.Service;

import java.util.List;

import bs.howdy.PrayerList.Entities.Prayer;

public interface PrayerService {
	List<Prayer> getPrayers();
	List<Prayer> getActivePrayers();
	List<Prayer> getAnsweredPrayers();
	Prayer getPrayer(int id);
	boolean addPrayer(Prayer p);
	boolean updatePrayer(Prayer p);
	boolean removePrayer(Prayer p);
}
