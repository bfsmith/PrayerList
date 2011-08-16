package bs.howdy.PrayerList.Adapters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import bs.howdy.PrayerList.*;
import bs.howdy.PrayerList.Data.DataProvider;
import bs.howdy.PrayerList.Entities.*;
import android.app.Activity;
import android.view.*;
import android.widget.*;

public class AnsweredAdapter extends ArrayAdapter<Prayer> {
	private Activity _context;
	private SimpleDateFormat _dateFormat;

	public AnsweredAdapter(Activity context, List<Prayer> prayers) {
		super(context, R.layout.answered_prayer_list_item, prayers);
		_context = context;
		_dateFormat = new SimpleDateFormat(App.getContext().getString(R.string.DateFormat));
	}

	public void update() {
		setPrayers(DataProvider.getInstance().getAnsweredPrayers());
	}
	
	public void insert(Prayer prayer) {
		insert(prayer, this.getCount());
	}
	
	public void setPrayers(List<Prayer> prayers) {
		clear();
		int count = 0;
		for(Prayer p : prayers) {
			insert(p, count++);
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = _context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.answered_prayer_list_item, null, true);
		Prayer p = getCount() > position ? getItem(position) : null;
		if(p == null) return null;
		
		TextView title = (TextView) rowView.findViewById(R.id.answeredTitle);
		title.setText(truncateString(p.Title, 15));
		TextView description = (TextView) rowView.findViewById(R.id.answeredDescription);
		description.setText(truncateString(p.Description, 20));
		TextView answeredDate = (TextView) rowView.findViewById(R.id.answeredDate);
		answeredDate.setText(formatDate(p.AnsweredDate));
		
		return rowView;
	}
	
	private String truncateString(String s, int length) {
		if(s == null) return "";
		s = s.replace("\n", "").replace("\r", "");
		if(s.length() <= length) return s;
		int lastSpace = s.substring(0, length).lastIndexOf(" ");
		return lastSpace > 0 ? s.substring(0, lastSpace) : s.substring(0, length);
	}
	
	private String formatDate(Date date) {
		if(date == null) return "";
		return _dateFormat.format(date);
	}
}
