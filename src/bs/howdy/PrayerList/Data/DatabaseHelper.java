package bs.howdy.PrayerList.Data;

import bs.howdy.PrayerList.*;
import bs.howdy.PrayerList.Entities.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	protected Context context;
	private SchemaPatch[] _patches = 
			{
				new SchemaPatch1(),
				new SchemaPatch2(),
				new SchemaPatch3(),
				new SchemaPatch4(),
				//new SchemaPatch5(),
			};
	
	public DatabaseHelper(Context context) {
		super(context, Constants.Database.NAME, null, Constants.Database.VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		applyLatestSchemaPatches(db, 0, 0);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		applyLatestSchemaPatches(db, oldVersion, newVersion);
	}
	
	private void applyLatestSchemaPatches(SQLiteDatabase db, int oldVersion, int newVersion) {
		for(SchemaPatch patch : _patches) {
			if(patch.getPatchNumber() > oldVersion) {
				boolean patched = patch.applyPatch(db);
				if(!patched) {
					Log.e(Constants.LOG_TAG, "Error updating database, patch " + patch.getPatchNumber() + " failed! :(");
					break;
				}
			}
		}
	}
}
