package bs.howdy.PrayerList.Data;

import bs.howdy.PrayerList.*;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import bs.howdy.PrayerList.Constants;
import bs.howdy.PrayerList.Entities.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataProvider implements bs.howdy.PrayerList.Service.DataProvider {

	private SimpleDateFormat _dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	protected DatabaseHelper _db;
	private static final String[] columns_prayers = new String[] { Constants.Database.COLUMN_ID, 
		Constants.Database.COLUMN_TITLE, Constants.Database.COLUMN_DESCRIPTION, 
		Constants.Database.COLUMN_DATECREATED, Constants.Database.COLUMN_DATEANSWERED };
	
	public DataProvider() {
		this(App.getContext());
	}
	
	public DataProvider(Context context) {
		_db = new DatabaseHelper(context);
	}
	
	/* ************************ PRAYERS ************************ */
	
	public List<Prayer> getPrayers() {
		SQLiteDatabase db = _db.getReadableDatabase();
		Cursor c = db.query(Constants.Database.TABLE_PRAYERS, columns_prayers, null, null, null, null, 
				Constants.Database.COLUMN_ID + " ASC");

		List<Prayer> prayers;
		try { prayers = parsePrayers(c); }
		catch(Exception ex) { prayers = new ArrayList<Prayer>(); }
		finally { c.close(); }
		return prayers;
	}

	public List<Prayer> getActivePrayers() {
		SQLiteDatabase db = _db.getReadableDatabase();
		Cursor c = db.query(Constants.Database.TABLE_PRAYERS, columns_prayers,  
				Constants.Database.COLUMN_DATEANSWERED + " IS NULL", null, null, null, 
				Constants.Database.COLUMN_ID + " ASC");

		List<Prayer> prayers;
		try { prayers = parsePrayers(c); }
		catch(Exception ex) { prayers = new ArrayList<Prayer>(); }
		finally { c.close(); }
		return prayers;
	}

	public List<Prayer> getAnsweredPrayers() {
		SQLiteDatabase db = _db.getReadableDatabase();
		Cursor c = db.query(Constants.Database.TABLE_PRAYERS, columns_prayers,  
				Constants.Database.COLUMN_DATEANSWERED + " IS NOT NULL", null, null, null, 
				Constants.Database.COLUMN_DATEANSWERED + " DESC");

		List<Prayer> prayers;
		try { prayers = parsePrayers(c); }
		catch(Exception ex) { prayers = new ArrayList<Prayer>(); }
		finally { c.close(); }
		return prayers;
	}
	
	public Prayer getPrayer(int id) {
		SQLiteDatabase db = _db.getReadableDatabase();
		Cursor c = db.query(Constants.Database.TABLE_PRAYERS, columns_prayers,  
				Constants.Database.COLUMN_ID + " = ?", new String[] { String.valueOf(id) }, null, null,
				null);
		if(c.getCount() == 0) {
			c.close();
			return null;
		}
		c.moveToFirst();
		Prayer prayer = null;
		try { prayer = parsePrayer(c); }
		finally { c.close(); }
		return prayer;
	}
	
	public boolean addPrayer(Prayer p) {
		return addPrayer(p, false);
	}
	
	public boolean addPrayer(Prayer p, boolean forceAdd) {
		if(p.Id > 0 && !forceAdd) {
			return updatePrayer(p);
		} 
		int id = (int)_db.getWritableDatabase()
			.insert(Constants.Database.TABLE_PRAYERS, null, createContentValues(p));
		if(id > 0) {
			p.Id = id;
			return true;
		}
		return false;
	}
	
	public boolean updatePrayer(Prayer p) {
		if(p.Id <= 0) {
			return addPrayer(p);
		}
		int rowsAffected = _db.getWritableDatabase()
				.update(Constants.Database.TABLE_PRAYERS, createContentValues(p),
					Constants.Database.COLUMN_ID + " = ?", new String[] { String.valueOf(p.Id) });
		return rowsAffected > 0;
	}
	
	public boolean removePrayer(int prayerId) {
		if(prayerId < 0)
			return true;
		int rowsAffected = _db.getWritableDatabase().delete(Constants.Database.TABLE_PRAYERS,
					Constants.Database.COLUMN_ID + " = ?", new String[] { String.valueOf(prayerId) });
		return rowsAffected > 0;
	}
	
	/* ************************ END PRAYERS ********************** */
	
	/* ************************ CATEGORIES ************************ */
	
	public List<String> getCategories()
	{
		SQLiteDatabase db = _db.getReadableDatabase();
		Cursor c = db.query(Constants.Database.TABLE_CATEGORIES, new String[] { Constants.Database.COLUMN_CATEGORY },  
				null, null, null, null,	null);

		List<String> categories = new ArrayList<String>();
		try { 
			if(c.getCount() == 0) {
				c.close();
				return new ArrayList<String>();
			}
			c.moveToFirst();
			while(!c.isAfterLast()) {
				String category = c.getString(c.getColumnIndex(Constants.Database.COLUMN_CATEGORY));
				if(category != null)
					categories.add(category);
				c.moveToNext();
			}
		}
		finally { c.close(); }
		return categories;
	}

	public int getCategoryId(String category) {
		SQLiteDatabase db = _db.getReadableDatabase();
		Cursor c = db.query(Constants.Database.TABLE_CATEGORIES, new String[] { Constants.Database.COLUMN_CATEGORYID },  
				Constants.Database.COLUMN_CATEGORY + " = ?", new String[] {category}, null, null, null);
		
		int id = -1;
		if(c.getCount() > 0) {
			c.moveToFirst();
			id = c.getInt(c.getColumnIndex(Constants.Database.COLUMN_CATEGORYID));
		}
		c.close();
		return id;
	}
	
	public String getCategory(int categoryId) {
		String category = null;
		SQLiteDatabase db = _db.getReadableDatabase();
		Cursor c = db.query(Constants.Database.TABLE_CATEGORIES, new String[] { Constants.Database.COLUMN_CATEGORY },  
				Constants.Database.COLUMN_CATEGORYID + " = ?", new String[] {String.valueOf(categoryId)}, null, null, null);
		try {
			if(c.getCount() > 0) {
				c.moveToFirst();
				category = c.getString(c.getColumnIndex(Constants.Database.COLUMN_CATEGORY));
			}
		} finally {
			c.close();
		}
		return category;
	}
	
	public boolean categoryExists(String category) {
		return getCategoryId(category) > 0;
	}
	
	public boolean addCategory(String category) {
		SQLiteDatabase db = _db.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Constants.Database.COLUMN_CATEGORY, category);
		db.insert(Constants.Database.TABLE_CATEGORIES, null, values);
		return true;
	}

	public boolean renameCategory(String oldName, String newName) {
		SQLiteDatabase db = _db.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Constants.Database.COLUMN_CATEGORY, newName);
		db.update(Constants.Database.TABLE_CATEGORIES, values, Constants.Database.COLUMN_CATEGORY + " = ?", new String[] {oldName});
		return true;
	}

	public boolean removecategory(int categoryId) {
		SQLiteDatabase db = _db.getWritableDatabase();
		db.delete(Constants.Database.TABLE_PRAYER_CATEGORIES, Constants.Database.COLUMN_CATEGORYID + " = ?", new String[] {String.valueOf(categoryId)});
		db.delete(Constants.Database.TABLE_CATEGORIES, Constants.Database.COLUMN_CATEGORYID + " = ?", new String[] {String.valueOf(categoryId)});
		return true;
	}
	
	public String getPrayerCategory(int prayerId) {
		String category = null;
		SQLiteDatabase db = _db.getReadableDatabase();
		Cursor c = db.query(Constants.Database.TABLE_PRAYER_CATEGORIES, new String[] { Constants.Database.COLUMN_CATEGORYID },  
				Constants.Database.COLUMN_PRAYERID + " = ?", new String[] {String.valueOf(prayerId)}, null, null, null);
		try {
			if(c.getCount() == 0)
				return null;
			c.moveToFirst();
			int categoryId = c.getInt(c.getColumnIndex(Constants.Database.COLUMN_CATEGORYID));
			category = getCategory(categoryId);
		} finally {
			c.close();
		}
		return category;
	}

	public boolean addPrayerToCategory(int prayerId, int categoryId) {
		SQLiteDatabase db = _db.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Constants.Database.COLUMN_PRAYERID, String.valueOf(prayerId));
		values.put(Constants.Database.COLUMN_CATEGORYID, String.valueOf(categoryId));
		db.insert(Constants.Database.TABLE_PRAYER_CATEGORIES, null, values);
		return true;
	}

	public boolean removePrayerFromCategory(int prayerId, int categoryId) {
		SQLiteDatabase db = _db.getWritableDatabase();
		db.delete(Constants.Database.TABLE_PRAYER_CATEGORIES, 
				Constants.Database.COLUMN_PRAYERID + " = ? AND " + Constants.Database.COLUMN_CATEGORYID + " = ?", 
				new String[] {String.valueOf(prayerId), String.valueOf(categoryId)});
		return true;
	}
	
	/* ********************** END CATEGORIES ********************** */
	
	/* ************************ REMINDERS ************************ */
	
	public boolean isReminderOn(Prayer p) {
		SQLiteDatabase db = _db.getReadableDatabase();
		Cursor c = db.query(Constants.Database.TABLE_REMINDERS, new String[] {Constants.Database.COLUMN_PRAYERID}, 
				Constants.Database.COLUMN_PRAYERID + " = ?", new String[] { String.valueOf(p.Id) }, null, null, 
				null);
		boolean isReminderOn = c.getCount() > 0;
		c.close();
		return isReminderOn;
	}

	public boolean toggleReminder(Prayer p) {
		if(p.Id < 0)
			return false;
		SQLiteDatabase db = _db.getWritableDatabase();
		boolean isOn = isReminderOn(p);
		if(isOn) {
			db.delete(Constants.Database.TABLE_REMINDERS,
				Constants.Database.COLUMN_PRAYERID + " = ?", new String[] { String.valueOf(p.Id) });
		}
		else {
			ContentValues values = new ContentValues();
			values.put(Constants.Database.COLUMN_PRAYERID, p.Id);
			db.insert(Constants.Database.TABLE_REMINDERS, null, values);
		}
		return !isOn;
	}
	
	public List<Prayer> getReminderPrayers() {
		SQLiteDatabase db = _db.getReadableDatabase();
		Cursor c = db.query(Constants.Database.TABLE_REMINDERS, new String[] {Constants.Database.COLUMN_PRAYERID}, null, null, null, null, 
				null);

		List<Prayer> prayers;
		try { prayers = parseReminderPrayers(c); }
		catch(Exception ex) { prayers = new ArrayList<Prayer>(); }
		finally { c.close(); }
		return prayers;
	}

	/* ********************** END REMINDERS ********************** */
	
	
	
	
	
	
	
	
	
	
	
	protected List<Prayer> parsePrayers(Cursor c) {
		ArrayList<Prayer> prayers = new ArrayList<Prayer>();
		if(c.getCount() > 0) {
			c.moveToFirst();
			while(!c.isAfterLast()) {
				Prayer p = parsePrayer(c);
				if(p != null)
					prayers.add(p);
				c.moveToNext();
			}
		}
		return prayers;
	}
	
	protected Prayer parsePrayer(Cursor c) {
		Prayer p = new Prayer();
		if(c.isAfterLast()) return null;
		p.Id = c.getInt(c.getColumnIndex(Constants.Database.COLUMN_ID));
		p.Title = c.getString(c.getColumnIndex(Constants.Database.COLUMN_TITLE));
		p.Description = c.getString(c.getColumnIndex(Constants.Database.COLUMN_DESCRIPTION));
		p.CreatedDate = stringToDate(c.getString(c.getColumnIndex(Constants.Database.COLUMN_DATECREATED)));
		p.AnsweredDate = stringToDate(c.getString(c.getColumnIndex(Constants.Database.COLUMN_DATEANSWERED)));
		return p;
	}
	
	protected List<Prayer> parseReminderPrayers(Cursor c) {
		ArrayList<Prayer> prayers = new ArrayList<Prayer>();
		
		if(c.getCount() > 0) {
			c.moveToFirst();
			while(!c.isAfterLast()) {
				int id = c.getInt(c.getColumnIndex(Constants.Database.COLUMN_PRAYERID));
				Prayer p = getPrayer(id);
				if(p != null)
					prayers.add(p);
				c.moveToNext();
			}
		}
		return prayers;
	}
	
	private ContentValues createContentValues(Prayer p) {
		ContentValues values = new ContentValues();
		if(p.Id > 0)
			values.put(Constants.Database.COLUMN_ID, p.Id);
		values.put(Constants.Database.COLUMN_TITLE, p.Title);
		values.put(Constants.Database.COLUMN_DESCRIPTION, p.Description);
		values.put(Constants.Database.COLUMN_DATECREATED, dateToString(p.CreatedDate));
		values.put(Constants.Database.COLUMN_DATEANSWERED, dateToString(p.AnsweredDate));
		return values;
	}

	protected Date stringToDate(String s) {
		if(s == null || s.equals(""))
			return null;
		try {
			return _dateFormat.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected String dateToString(Date d) {
		if(d == null) return null;
		return _dateFormat.format(d);
	}
}
