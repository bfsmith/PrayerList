package bs.howdy.PrayerList.Service;

import java.util.List;

import bs.howdy.PrayerList.Entities.Prayer;

public interface ReminderService {
	boolean isReminderOn(Prayer p);
	boolean toggleReminder(Prayer p);
	List<Prayer> getReminderPrayers();
}
