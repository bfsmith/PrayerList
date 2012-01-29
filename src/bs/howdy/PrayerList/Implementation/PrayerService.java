package bs.howdy.PrayerList.Implementation;

import java.util.List;

import com.google.inject.Inject;

import bs.howdy.PrayerList.Entities.Prayer;
import bs.howdy.PrayerList.Service.DataProvider;

public class PrayerService implements bs.howdy.PrayerList.Service.PrayerService  {
	private DataProvider mdp;
	
	@Inject
	public PrayerService(DataProvider dataProvider) {
		mdp = dataProvider;
	}
	
	public List<Prayer> getPrayers() {
		return mdp.getPrayers();
	}

	public List<Prayer> getActivePrayers() {
		return mdp.getActivePrayers();
	}

	public List<Prayer> getAnsweredPrayers() {
		return mdp.getAnsweredPrayers();
	}

	public Prayer getPrayer(int id) {
		return mdp.getPrayer(id);
	}

	public boolean addPrayer(Prayer p) {
		return mdp.addPrayer(p);
	}

	public boolean updatePrayer(Prayer p) {
		return mdp.updatePrayer(p);
	}

	public boolean removePrayer(Prayer p) {
		if(p == null)
			return true;
		return mdp.removePrayer(p.Id);
	}
	
}
