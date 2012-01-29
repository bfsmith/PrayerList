package bs.howdy.PrayerList;

import android.content.Context;
import android.content.Intent;
import bs.howdy.PrayerList.Entities.Prayer;

public class Utility {
	public static boolean isNullOrEmpty(String s) {
		return s == null || s.trim().equals("");
	}
	
	public static void sharePrayer(Prayer p, Context context) {
		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String prefix = p.IsAnswered() 
				? context.getString(R.string.ShareAnsweredPrayerPrefix)
				: context.getString(R.string.ShareUnansweredPrayerPrefix);
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, prefix + p.Title + "\n" + p.Description);
		context.startActivity(Intent.createChooser(sharingIntent, "Share using"));
	}
	
	public static boolean toggleReminder(Prayer p) {
		return false;
	}
}
