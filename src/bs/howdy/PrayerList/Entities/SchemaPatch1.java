package bs.howdy.PrayerList.Entities;

import android.database.sqlite.SQLiteDatabase;

public class SchemaPatch1 implements SchemaPatch {

	public int getPatchNumber() {
		return 1;
	}

	public boolean applyPatch(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS schema_patches (patch INTEGER PRIMARY KEY);");
		return true;
	}

}
