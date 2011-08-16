package bs.howdy.PrayerList;

public class Constants {
	public static final String LOG_TAG = "bs.howdy.PrayerList";
	
	public class Extras {
		public static final String ID = "id";
	}
	
	public class Requests {
		public static final int PRAYER_CREATEUPDATE = 1;
	}
	
	public class Results {
		public static final int OK = 1;
		public static final int CANCEL = 2;
	}
	
	public class Database {
		public static final String NAME = "prayers";
		public static final int VERSION = 2;
		public static final String TABLE_PRAYERS = "prayers";
		public static final String TABLE_SCHEMAPATCHES = "schema_patches";
		public static final String COLUMN_ID = "_id";
		public static final String COLUMN_TITLE = "title";
		public static final String COLUMN_DESCRIPTION = "description";
		public static final String COLUMN_DATECREATED = "dateCreated";
		public static final String COLUMN_DATEANSWERED = "dateAnswered";
		public static final String COLUMN_PATCH = "patch";
		
	}
}
