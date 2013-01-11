package bs.howdy.PrayerList.Implementation;

import java.util.List;

import com.google.inject.Inject;

import bs.howdy.PrayerList.Entities.Prayer;
import bs.howdy.PrayerList.Service.DataProvider;

public class CategoryService implements bs.howdy.PrayerList.Service.CategoryService {
	private DataProvider mDP;
	
	@Inject
	public CategoryService(DataProvider dataService) {
		mDP = dataService;
	}

	public List<String> getCategories() {
		return mDP.getCategories();
	}

	public boolean addCategory(String category) {
		if(mDP.categoryExists(category))
			return false;
		
		return mDP.addCategory(category);
	}

	public boolean renameCategory(String oldName, String newName) {
		if(!mDP.categoryExists(oldName))
			throw new IllegalArgumentException("No category found matching name \"" + oldName + "\".");
		if(mDP.categoryExists(newName))
			throw new IllegalArgumentException("Category with name \"" + newName + "\" already exists.");
		return mDP.renameCategory(oldName, newName);
	}

	public boolean removecategory(String category) {
		int categoryId = mDP.getCategoryId(category);
		if(categoryId < 0)
			return true;
		return mDP.removecategory(categoryId);
	}

	public String getPrayerCategory(Prayer prayer) {
		return mDP.getPrayerCategory(prayer.Id);
	}

	public boolean addPrayerToCategory(Prayer prayer, String category) {
		int categoryId = mDP.getCategoryId(category);
		if(categoryId < 0)
			throw new IllegalArgumentException("No category found with name \"" + category + "\".");
		
		String cat = mDP.getPrayerCategory(prayer.Id);
		if(cat != null && cat.equals(category))
			return false;
		
		return mDP.addPrayerToCategory(prayer.Id, categoryId);
	}

	public boolean removePrayerFromCategory(Prayer prayer, String category) {
		int categoryId = mDP.getCategoryId(category);
		if(categoryId < 0)
			throw new IllegalArgumentException("No category found with name \"" + category + "\".");
		return mDP.removePrayerFromCategory(prayer.Id, categoryId);
	}
	
}
