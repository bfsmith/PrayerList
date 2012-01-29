package bs.howdy.PrayerList.Adapters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import roboguice.adapter.IterableAdapter;

import com.google.inject.Inject;

import bs.howdy.PrayerList.*;
import bs.howdy.PrayerList.Entities.*;
import bs.howdy.PrayerList.Service.PrayerService;
import android.app.Activity;
import android.view.*;
import android.widget.*;

public class AnsweredAdapter extends IterableAdapter<Prayer> {
	private @Inject	PrayerService mPrayerService;
	private Activity mContext;
	private SimpleDateFormat mDateFormat;
	private HashMap<Integer, Boolean> mLinesExpanded;

	public AnsweredAdapter(Activity context, List<Prayer> prayers) {
		super(context, R.layout.answered_prayer_list_item, prayers);
		mContext = context;
		mDateFormat = new SimpleDateFormat(App.getContext().getString(R.string.DateFormat));
		mLinesExpanded = new HashMap<Integer, Boolean>();
	}

	public void update() {
		setPrayers(mPrayerService.getAnsweredPrayers());
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
		LayoutInflater inflater = mContext.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.answered_prayer_list_item, null, true);
		final Prayer p = getCount() > position ? getItem(position) : null;
		if(p == null) return null;
		
		TextView title = (TextView) rowView.findViewById(R.id.title);
		title.setText(p.Title);
		TextView description = (TextView) rowView.findViewById(R.id.description);
		description.setText(p.Description);
		TextView answeredDate = (TextView) rowView.findViewById(R.id.answeredDate);
		answeredDate.setText(formatDate(p.AnsweredDate));

		mLinesExpanded.put(position, false);
		
		ImageView moreImage = (ImageView)rowView.findViewById(R.id.moreImage);
		if(Utility.isNullOrEmpty(p.Description)) {
			moreImage.setVisibility(View.GONE);
		} else {
			description.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					toggleDescriptions((View)v.getParent().getParent(), p, position);
				}
			});
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
		return mDateFormat.format(date);
	}
	
	private void toggleDescriptions(View view, Prayer p, int position) {
		TextView description = (TextView) view.findViewById(R.id.description);
		ImageView triangle = (ImageView) view.findViewById(R.id.moreImage);
		
		if(mLinesExpanded.get(position)) {
			description.setLines(1);
			triangle.setImageResource(R.drawable.circle_right);
			mLinesExpanded.put(position, false);
		} else {
			description.setLines(description.getLineCount());
			triangle.setImageResource(R.drawable.circle_down);
			mLinesExpanded.put(position, true);
		}
	}
}
