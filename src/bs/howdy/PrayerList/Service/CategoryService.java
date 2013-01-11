package bs.howdy.PrayerList.Service;

import java.util.List;

import bs.howdy.PrayerList.Entities.Prayer;

public interface CategoryService {
	public List<String> getCategories();
	public boolean addCategory(String category);
	public boolean renameCategory(String oldName, String newName);
	public boolean removecategory(String category);
	public String getPrayerCategory(Prayer prayer);
	public boolean addPrayerToCategory(Prayer prayer, String category);
	public boolean removePrayerFromCategory(Prayer prayer, String category);
}
