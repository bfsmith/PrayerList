package bs.howdy.PrayerList.Adapters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
	private HashMap<Integer, Boolean> _linesExpanded;

	public AnsweredAdapter(Activity context, List<Prayer> prayers) {
		super(context, R.layout.answered_prayer_list_item, prayers);
		_context = context;
		_dateFormat = new SimpleDateFormat(App.getContext().getString(R.string.DateFormat));
		_linesExpanded = new HashMap<Integer, Boolean>();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = _context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.answered_prayer_list_item, null, true);
		final Prayer p = getCount() > position ? getItem(position) : null;
		if(p == null) return null;
		
		TextView title = (TextView) rowView.findViewById(R.id.title);
		title.setText(p.Title);
		TextView description = (TextView) rowView.findViewById(R.id.description);
		description.setText(p.Description);
		TextView answeredDate = (TextView) rowView.findViewById(R.id.answeredDate);
		answeredDate.setText(formatDate(p.AnsweredDate));

		_linesExpanded.put(position, false);
		
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
	
	private String formatDate(Date date) {
		if(date == null) return "";
		return _dateFormat.format(date);
	}
	
	private void toggleDescriptions(View view, Prayer p, int position) {
		TextView description = (TextView) view.findViewById(R.id.description);
		
		if(_linesExpanded.get(position)) {
			description.setLines(1);
			_linesExpanded.put(position, false);
		} else {
			description.setLines(description.getLineCount());
			_linesExpanded.put(position, true);
		}
	}
}
