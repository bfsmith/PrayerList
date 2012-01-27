package bs.howdy.PrayerList.Entities;

import android.database.sqlite.SQLiteDatabase;

public class SchemaPatch2 implements SchemaPatch {

	public int getPatchNumber() {
		return 2;
	}

	public boolean applyPatch(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS prayers (\n" + 
				"_id INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
				"title TEXT NOT NULL, \n" + 
				"description TEXT, \n" +
				"dateCreated INTEGER NOT NULL, \n" +
				"dateAnswered INTEGER);");
		return true;
	}

}
