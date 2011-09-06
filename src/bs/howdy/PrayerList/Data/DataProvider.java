package bs.howdy.PrayerList.Data;

import bs.howdy.PrayerList.*;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import bs.howdy.PrayerList.Constants;
import bs.howdy.PrayerList.Entities.*;

import java.util.*;

public class DataProvider {
	private static DataProvider _instance;
	//private Context _context;
	protected DatabaseHelper _db;
	private static final String[] columns_prayers = new String[] { Constants.Database.COLUMN_ID, 
		Constants.Database.COLUMN_TITLE, Constants.Database.COLUMN_DESCRIPTION, 
		Constants.Database.COLUMN_DATECREATED, Constants.Database.COLUMN_DATEANSWERED };
	
	private DataProvider() {
		this(App.getContext());
	}
	
	private DataProvider(Context context) {
		//_context = context;
		_db = new DatabaseHelper(context);
	}
	
	public static DataProvider createInstance(Context context) {
		if(_instance == null)
			_instance = new DataProvider(context);
		return _instance;
	}
	
	public static DataProvider getInstance() {
		if(_instance == null)
			_instance = new DataProvider();
		return _instance;
	}

	public ArrayList<Prayer> getPrayers() {
		SQLiteDatabase db = _db.getReadableDatabase();
		Cursor c = db.query(Constants.Database.TABLE_PRAYERS, columns_prayers, null, null, null, null, 
				Constants.Database.COLUMN_ID + " ASC");
		return parsePrayers(c);
	}

	public ArrayList<Prayer> getActivePrayers() {
		SQLiteDatabase db = _db.getReadableDatabase();
		Cursor c = db.query(Constants.Database.TABLE_PRAYERS, columns_prayers,  
				Constants.Database.COLUMN_DATEANSWERED + " IS NULL", null, null, null, 
				Constants.Database.COLUMN_ID + " ASC");
		return parsePrayers(c);
	}

	public ArrayList<Prayer> getAnsweredPrayers() {
		SQLiteDatabase db = _db.getReadableDatabase();
		Cursor c = db.query(Constants.Database.TABLE_PRAYERS, columns_prayers,  
				Constants.Database.COLUMN_DATEANSWERED + " IS NOT NULL", null, null, null, 
				Constants.Database.COLUMN_DATEANSWERED + " DESC");
		return parsePrayers(c);
	}
	
	public Prayer getPrayer(int id) {
		SQLiteDatabase db = _db.getReadableDatabase();
		Cursor c = db.query(Constants.Database.TABLE_PRAYERS, columns_prayers,  
				Constants.Database.COLUMN_ID + " = ?", new String[] { String.valueOf(id) }, null, null,
				null);
		if(c.getCount() == 0) return null;
		c.moveToFirst();
		return parsePrayer(c);
	}
	
	public boolean addPrayer(Prayer p) {
		if(p.Id > 0) {
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
	
	public boolean removePrayer(Prayer p) {
		if(p.Id < 0)
			return true;
		int rowsAffected = _db.getWritableDatabase().delete(Constants.Database.TABLE_PRAYERS,
					Constants.Database.COLUMN_ID + " = ?", new String[] { String.valueOf(p.Id) });
		return rowsAffected > 0;
	}
	
	protected ArrayList<Prayer> parsePrayers(Cursor c) {
		ArrayList<Prayer> prayers = new ArrayList<Prayer>();
		
		if(c.getCount() > 0) {
			c.moveToFirst();
			while(!c.isAfterLast()) {
				prayers.add(parsePrayer(c));
				c.moveToNext();
			}
		}
		c.close();
		return prayers;
	}
	
	protected Prayer parsePrayer(Cursor c) {
		if(c.isAfterLast()) return null;
		Prayer p = new Prayer();
		p.Id = c.getInt(c.getColumnIndex(Constants.Database.COLUMN_ID));
		p.Title = c.getString(c.getColumnIndex(Constants.Database.COLUMN_TITLE));
		p.Description = c.getString(c.getColumnIndex(Constants.Database.COLUMN_DESCRIPTION));
		p.CreatedDate = stringToDate(c.getString(c.getColumnIndex(Constants.Database.COLUMN_DATECREATED)));
		p.AnsweredDate = stringToDate(c.getString(c.getColumnIndex(Constants.Database.COLUMN_DATEANSWERED)));
		return p;
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
		Date d = new Date();
		if(s == null || s.equals(""))
			return null;
		String[] parts = s.split("-");
		if(parts.length != 3)
			return null;
		d.setYear(Integer.parseInt(parts[0]));
		d.setMonth(Integer.parseInt(parts[1]));
		d.setDate(Integer.parseInt(parts[2]));
		return d;
	}
	
	protected String dateToString(Date d) {
		if(d == null) return null;
		return d.getYear() + "-" + d.getMonth() + "-" + d.getDate();
	}
}
