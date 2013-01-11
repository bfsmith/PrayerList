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
	
	public List<String> getCategories();
	public int getCategoryId(String category);
	public boolean categoryExists(String category);
	public boolean addCategory(String category);
	public boolean renameCategory(String oldName, String newName);
	public boolean removecategory(int categoryId);
	public String getPrayerCategory(int prayerId);
	public boolean addPrayerToCategory(int prayerId, int categoryId);
	public boolean removePrayerFromCategory(int prayerId, int categoryId);

	boolean isReminderOn(Prayer p);
	boolean toggleReminder(Prayer p);
	List<Prayer> getReminderPrayers();
}