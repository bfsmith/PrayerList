package bs.howdy.PrayerList.Entities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bs.howdy.PrayerList.Constants;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SchemaPatch4 implements SchemaPatch {

	private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static final String[] columns_prayers = new String[] { Constants.Database.COLUMN_ID, 
		Constants.Database.COLUMN_TITLE, Constants.Database.COLUMN_DESCRIPTION, 
		Constants.Database.COLUMN_DATECREATED, Constants.Database.COLUMN_DATEANSWERED };
	
	public int getPatchNumber() {
		return 4;
	}

	public boolean applyPatch(SQLiteDatabase db) {
		Cursor c = db.query(Constants.Database.TABLE_PRAYERS, columns_prayers, null, null, null, null, 
				Constants.Database.COLUMN_ID + " ASC");
		List<Prayer> prayers = parsePrayers(c);
		for(Prayer p : prayers) {
			updatePrayer(p, db);
		}
		return true;
	}

	private List<Prayer> parsePrayers(Cursor c) {
		ArrayList<Prayer> prayers = new ArrayList<Prayer>();
		try {
			if(c.getCount() > 0) {
				c.moveToFirst();
				while(!c.isAfterLast()) {
					Prayer p = parsePrayer(c);
					if(p != null)
						prayers.add(p);
					c.moveToNext();
				}
			}
		}
		finally {
			c.close();
		}
		return prayers;
	}
	
	private Prayer parsePrayer(Cursor c) {
		Prayer p = new Prayer();
		if(c.isAfterLast()) return null;
		p.Id = c.getInt(c.getColumnIndex(Constants.Database.COLUMN_ID));
		p.Title = c.getString(c.getColumnIndex(Constants.Database.COLUMN_TITLE));
		p.Description = c.getString(c.getColumnIndex(Constants.Database.COLUMN_DESCRIPTION));
		p.CreatedDate = stringToDate(c.getString(c.getColumnIndex(Constants.Database.COLUMN_DATECREATED)));
		p.AnsweredDate = stringToDate(c.getString(c.getColumnIndex(Constants.Database.COLUMN_DATEANSWERED)));
		return p;
	}

	private Date stringToDate(String s) {
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
	

	public boolean updatePrayer(Prayer p, SQLiteDatabase db) {
		int rowsAffected = db
				.update(Constants.Database.TABLE_PRAYERS, createContentValues(p),
					Constants.Database.COLUMN_ID + " = ?", new String[] { String.valueOf(p.Id) });
		return rowsAffected > 0;
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
	
	private String dateToString(Date d) {
		if(d == null) return null;
		return mDateFormat.format(d);
	}
	
}
