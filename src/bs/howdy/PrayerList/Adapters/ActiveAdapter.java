package bs.howdy.PrayerList.Adapters;

import java.util.*;

import bs.howdy.PrayerList.*;
import bs.howdy.PrayerList.Data.DataProvider;
import bs.howdy.PrayerList.Entities.*;
import android.app.Activity;
import android.view.*;
import android.widget.*;

public class ActiveAdapter extends ArrayAdapter<Prayer> {
	private Activity _context;
	private HashMap<Integer, Boolean> _linesExpanded;

	public ActiveAdapter(Activity context, List<Prayer> prayers) {
		super(context, R.layout.active_prayer_list_item, prayers);
		_context = context;
		_linesExpanded = new HashMap<Integer, Boolean>();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = _context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.active_prayer_list_item, null, true);
		final Prayer p = getCount() > position ? getItem(position) : null;
		if(p == null) return null;
			
		TextView title = (TextView) rowView.findViewById(R.id.title);
		title.setText(p.Title);
		TextView description = (TextView) rowView.findViewById(R.id.description);
		description.setText(p.Description);
		_linesExpanded.put(position, false);
		
		description.setText(p.Description);
		
		ImageView moreImage = (ImageView)rowView.findViewById(R.id.moreImage);
		if(Utility.IsNullOrEmpty(p.Description)) {
			moreImage.setVisibility(View.GONE);
		} else {
			moreImage.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					toggleDescriptions((View)v.getParent().getParent(), p, position);
				}
			});
		}
		
		return rowView;
	}
	
	private void toggleDescriptions(View view, Prayer p, int position) {
		TextView description = (TextView) view.findViewById(R.id.description);
		ImageView triangle = (ImageView) view.findViewById(R.id.moreImage);
		
		if(_linesExpanded.get(position)) {
			description.setLines(1);
			triangle.setImageResource(R.drawable.triangle_right);
			_linesExpanded.put(position, false);
		} else {
			description.setLines(description.getLineCount());
			triangle.setImageResource(R.drawable.triangle_down);
			_linesExpanded.put(position, true);
		}
	}
}
