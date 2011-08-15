package bs.howdy.PrayerList.Data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import bs.howdy.PrayerList.Constants;
import bs.howdy.PrayerList.Entities.*;

import java.util.*;

public class DataProvider {
	private static DataProvider _instance;
	private ArrayList<Prayer> _prayers;
	private int _count;
	protected DatabaseHelper _db;
	private static final String[] columns_prayers = new String[] { Constants.Database.COLUMN_ID, 
		Constants.Database.COLUMN_TITLE, Constants.Database.COLUMN_DESCRIPTION, 
		Constants.Database.COLUMN_DATECREATED, Constants.Database.COLUMN_DATEANSWERED };
	
	private DataProvider() {
		_count = 1;
		_prayers = new ArrayList<Prayer>();
		_db = new DatabaseHelper(PrayerListApp.getContext());
	}
	
	public static DataProvider getInstance() {
		if(_instance == null)
			_instance = new DataProvider();
		return _instance;
	}

	public ArrayList<Prayer> getPrayers() {
		return _prayers;
//		SQLiteDatabase db = _db.getReadableDatabase();
//		Cursor c = db.query(Constants.Database.TABLE_PRAYERS, columns_prayers, null, null, null, null, 
//				Constants.Database.COLUMN_ID + " ASC");
//		return parsePrayers(c);
	}

	public ArrayList<Prayer> getActivePrayers() {
		ArrayList<Prayer> active = new ArrayList<Prayer>();
		for(Prayer p : _prayers) {
			if(!p.IsAnswered())
				active.add(p);
		}
		return active;
	}

	public ArrayList<Prayer> getAnsweredPrayers() {
		ArrayList<Prayer> answered = new ArrayList<Prayer>();
		for(Prayer p : _prayers) {
			if(p.IsAnswered())
				answered.add(p);
		}
		return answered;
	}
	
	public Prayer getPrayer(int id) {
		for(Prayer p : _prayers) {
			if(p.Id == id)
				return p;
		}
		return null;
	}
	
	public void addPrayer(Prayer p) {
		if(p.Id <= 0)
			p.Id = _count++;
		if(!updatePrayer(p))
			_prayers.add(p);
	}
	
	public boolean updatePrayer(Prayer p) {
		for(int i = 0; i < _prayers.size(); i++) {
			if(_prayers.get(i).Id == p.Id) {
				_prayers.set(i, p);
				return true;
			}
		}
		return false;
	}
	
	public void removePrayer(Prayer p) {
		_prayers.remove(p);
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
		return _prayers;
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
