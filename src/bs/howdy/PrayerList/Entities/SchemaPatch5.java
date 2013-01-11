package bs.howdy.PrayerList.Entities;

import bs.howdy.PrayerList.Constants;
import android.database.sqlite.SQLiteDatabase;

public class SchemaPatch5 implements SchemaPatch {

	public int getPatchNumber() {
		return 5;
	}

	public boolean applyPatch(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + Constants.Database.TABLE_CATEGORIES + " (\n" +
				Constants.Database.COLUMN_CATEGORYID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + 
				Constants.Database.COLUMN_CATEGORY + " TEXT NOT NULL);");
		db.execSQL("CREATE TABLE IF NOT EXISTS " + Constants.Database.TABLE_PRAYER_CATEGORIES + " (\n" +
				Constants.Database.COLUMN_CATEGORYID + " INTEGER NOT NULL, " +
				Constants.Database.COLUMN_PRAYERID + " INTEGER NOT NULL);");
		return true;
	}

}
