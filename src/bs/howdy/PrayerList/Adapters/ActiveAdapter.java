package bs.howdy.PrayerList.Adapters;

import java.util.*;

import com.google.inject.Inject;

import roboguice.adapter.IterableAdapter;

import bs.howdy.PrayerList.*;
import bs.howdy.PrayerList.Entities.*;
import bs.howdy.PrayerList.Service.PrayerService;
import bs.howdy.PrayerList.Views.DragDropListView;
import bs.howdy.PrayerList.Views.DragDropListView.ListDragDropListener;
import bs.howdy.PrayerList.Views.DragDropListView.ListFlingListener;
import android.app.Activity;
import android.view.*;
import android.widget.*;

public class ActiveAdapter extends IterableAdapter<Prayer> implements ListDragDropListener, ListFlingListener {
	private @Inject	PrayerService mPrayerService;
	private Activity mContext;
	private HashMap<Integer, Boolean> mLinesExpanded;

	public ActiveAdapter(Activity context, List<Prayer> prayers) {
		super(context, R.layout.active_prayer_list_item, prayers);
		mContext = context;
		mLinesExpanded = new HashMap<Integer, Boolean>();
	}
	
	public void update() {
		setPrayers(mPrayerService.getActivePrayers());
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
		View rowView = inflater.inflate(R.layout.active_prayer_list_item, null, true);
		final Prayer p = getCount() > position ? getItem(position) : null;
		if(p == null) return null;
			
		TextView title = (TextView) rowView.findViewById(R.id.title);
		title.setText(p.Title);
		TextView description = (TextView) rowView.findViewById(R.id.description);
		description.setText(p.Description);
		mLinesExpanded.put(position, false);
		
		description.setText(p.Description);
		
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

	public void flung(int position, int direction) {
		Prayer p = getItem(position);
		if(p == null) 
			return;
		if(direction == DragDropListView.DIRECTION_LEFT) {
			p.AnsweredDate = new Date();
			mPrayerService.updatePrayer(p);
		}
		
		update();
		notifyDataSetChanged();
	}

	public void drop(int startPosition, int endPosition) {
		if(startPosition == endPosition || endPosition < 0)
			return;
		
		int lowerPosition = startPosition < endPosition
				? startPosition : endPosition;
		int higherPosition = startPosition > endPosition
				? startPosition : endPosition;
		for(int i = lowerPosition + 1; i <= higherPosition; i++) {
			Prayer p = getItem(i);
			p.Ordinal--;
			mPrayerService.updatePrayer(p);
		}
		
		Prayer p = getItem(startPosition);
		p.Ordinal = endPosition;
		mPrayerService.updatePrayer(p);
		
		update();
		notifyDataSetChanged();
	}
}
