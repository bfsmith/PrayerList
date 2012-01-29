package bs.howdy.PrayerList.Implementation;

import java.util.List;

import com.google.inject.Inject;

import bs.howdy.PrayerList.Entities.Prayer;
import bs.howdy.PrayerList.Service.DataProvider;

public class ReminderService implements bs.howdy.PrayerList.Service.ReminderService {
	private DataProvider mdp;
	
	@Inject
	public ReminderService(DataProvider dataService) {
		mdp = dataService;
	}
	
	public boolean isReminderOn(Prayer p) {
		return mdp.isReminderOn(p);
	}

	public boolean toggleReminder(Prayer p) {
		return mdp.toggleReminder(p);
	}

	public List<Prayer> getReminderPrayers() {
		return mdp.getReminderPrayers();
	}
	
}
