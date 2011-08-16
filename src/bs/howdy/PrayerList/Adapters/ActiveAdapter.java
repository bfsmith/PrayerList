package bs.howdy.PrayerList.Adapters;

import java.util.List;

import bs.howdy.PrayerList.*;
import bs.howdy.PrayerList.Data.DataProvider;
import bs.howdy.PrayerList.Entities.*;
import android.app.Activity;
import android.view.*;
import android.widget.*;

public class ActiveAdapter extends ArrayAdapter<Prayer> {
	private Activity _context;

	public ActiveAdapter(Activity context, List<Prayer> prayers) {
		super(context, R.layout.active_prayer_list_item, prayers);
		_context = context;
	}
	
	public void update() {
		setPrayers(DataProvider.getInstance().getActivePrayers());
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
		View rowView = inflater.inflate(R.layout.active_prayer_list_item, null, true);
		Prayer p = getCount() > position ? getItem(position) : null;
		if(p == null) return null;
			
//		final int id = p.Id;
		
		TextView title = (TextView) rowView.findViewById(R.id.activeTitle);
		title.setText(truncateString(p.Title, 100));
		TextView description = (TextView) rowView.findViewById(R.id.activeDescription);
		description.setText(truncateString(p.Description, 100));
		
//		Button answeredButton = (Button) rowView.findViewById(R.id.answeredButton);
//		answeredButton.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				DataProvider dp = DataProvider.getInstance();
//				Prayer p = dp.getPrayer(id);
//				if(p == null) return;
//				p.AnsweredDate = new Date();
//				dp.updatePrayer(p);
//				update();
//				notifyDataSetChanged();
//			}
//		});
		
		return rowView;
	}

	private String truncateString(String s, int length) {
		if(s == null) return "";
		s = s.replace("\n", "").replace("\r", "");
		if(s.length() <= length) return s;
		int lastSpace = s.substring(0, length).lastIndexOf(" ");
		return lastSpace > 0 ? s.substring(0, lastSpace) : s.substring(0, length);
	}
	
}
