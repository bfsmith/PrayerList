package bs.howdy.PrayerList.Data;

import bs.howdy.PrayerList.*;
import bs.howdy.PrayerList.Entities.*;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.*;

import org.w3c.dom.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {
	protected Context context;
	private ArrayList<SchemaPatch> _patches;
	
	public DatabaseHelper(Context context) {
		super(context, Constants.Database.NAME, null, Constants.Database.VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		applyLatestSchemaPatches(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		applyLatestSchemaPatches(db);
	}
	
	private void applyLatestSchemaPatches(SQLiteDatabase db) {
		int currentVersion = getCurrentSchemaVersion(db);
		
		ArrayList<SchemaPatch> schemaPatches = getPatches();
		for(SchemaPatch patch : schemaPatches) {
			if(patch.getNumber() > currentVersion) {
				 applySchemaPatch(db, patch);
			}
		}
	}
	
	private int getCurrentSchemaVersion(SQLiteDatabase db) {
		Cursor cursor = db.rawQuery("SELECT CASE ( SELECT 1 FROM sqlite_master WHERE type='table' AND name='?' ) WHEN 1 THEN MAX(?) ELSE 0 END FROM ?;"
				, new String[] { Constants.Database.TABLE_SCHEMAPATCHES, Constants.Database.COLUMN_PATCH, 
						Constants.Database.TABLE_SCHEMAPATCHES });
		
		if(cursor == null)
			return 0;
		
		cursor.moveToFirst();
		int patchNumber = cursor.getInt(0);
		return patchNumber;
	}
	
	private void applySchemaPatch(SQLiteDatabase db, SchemaPatch patch) {
		 db.execSQL(patch.getSql());
		 
		 ContentValues values = new ContentValues();
		 values.put(Constants.Database.COLUMN_PATCH, patch.getNumber());
		 db.insert(Constants.Database.TABLE_SCHEMAPATCHES, null, values);
	}
	
	private ArrayList<SchemaPatch> getPatches() {
		if(_patches == null) {
			_patches = new ArrayList<SchemaPatch>();
			try {
				InputStream in = context.getResources().openRawResource(R.raw.sql);
				
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				Document doc = builder.parse(in, null);
				doc.getDocumentElement().normalize();
				 
				Node schemaPatchNode = doc.getElementsByTagName("schemaPatch").item(0);
				Element schemaPatchElement = (Element) schemaPatchNode;
				NodeList patchList = schemaPatchElement.getElementsByTagName("patch");
		 
				for (int i = 0; i < patchList.getLength(); i++) {
			       _patches.add(new SchemaPatch(patchList.item(i)));  
				}
			} catch(Throwable t) {
				Toast.makeText(context, "Error loading db patches... " + t.toString(), Toast.LENGTH_SHORT);
			}
		}
		return _patches;
	}
}
