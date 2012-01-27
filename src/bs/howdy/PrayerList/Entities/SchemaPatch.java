package bs.howdy.PrayerList.Entities;

import android.database.sqlite.SQLiteDatabase;

public interface SchemaPatch {
	int getPatchNumber();
	boolean applyPatch(SQLiteDatabase db);
}
