package bs.howdy.PrayerList.Service;

import java.util.List;

import bs.howdy.PrayerList.Entities.Prayer;

public interface DataProvider {
	public List<Prayer> getPrayers();
	public List<Prayer> getActivePrayers();
	public List<Prayer> getAnsweredPrayers();
	public Prayer getPrayer(int id);
	public boolean addPrayer(Prayer p);
	public boolean updatePrayer(Prayer p);
	public boolean removePrayer(int prayerId);

	boolean isReminderOn(Prayer p);
	boolean toggleReminder(Prayer p);
	List<Prayer> getReminderPrayers();
}