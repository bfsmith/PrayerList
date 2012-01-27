package bs.howdy.PrayerList.Entities;

import android.database.sqlite.SQLiteDatabase;

public class SchemaPatch3 implements SchemaPatch {

	public int getPatchNumber() {
		return 3;
	}

	public boolean applyPatch(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS schema_patches");
		return true;
	}

}
