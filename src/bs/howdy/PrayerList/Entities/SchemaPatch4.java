package bs.howdy.PrayerList.Entities;

import android.database.sqlite.SQLiteDatabase;

public class SchemaPatch4 implements SchemaPatch {

	public int getPatchNumber() {
		return 4;
	}

	public boolean applyPatch(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS reminders (prayerId INTEGER PRIMARY KEY);");
		//db.execSQL("ALTER TABLE prayers ADD COLUMN ordinal INTEGER NOT NULL DEFAULT 0");
		return true;
	}

}
