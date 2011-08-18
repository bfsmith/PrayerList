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
			
		TextView title = (TextView) rowView.findViewById(R.id.activeTitle);
		title.setText(p.Title);
		TextView description = (TextView) rowView.findViewById(R.id.activeDescription);
		description.setText(p.Description);
		
		return rowView;
	}
}
