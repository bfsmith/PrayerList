package bs.howdy.PrayerList.Activities;

import bs.howdy.PrayerList.Constants;
import bs.howdy.PrayerList.R;
import bs.howdy.PrayerList.Adapters.*;
import bs.howdy.PrayerList.Entities.Prayer;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AnsweredPrayers extends BaseListActivity {
	private AnsweredAdapter mAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.answered_list);
        mAdapter = new AnsweredAdapter(this, mPrayerService.getAnsweredPrayers());
        setListAdapter(mAdapter);
	}

    protected void updateList() {
    	mAdapter.setPrayers(mPrayerService.getAnsweredPrayers());
    	mAdapter.notifyDataSetChanged();
    }
    
    public void viewPrayer(View view) {
    	Prayer p = getPrayerFromView(view);
		if(p == null) return;
		Intent editIntent = new Intent(this, PrayerInfo.class);
		editIntent.putExtra(Constants.Extras.ID, p.Id);
		startActivity(editIntent);
    }

    public void prayersUnanswered(View v) {
    	for(int id : _rowsChecked) {
    		Prayer p = mPrayerService.getPrayer(id);
    		if(p == null) continue;
    		p.AnsweredDate = null;
    		mPrayerService.updatePrayer(p);
    	}
    	_rowsChecked.clear();
    	hideActionButtons();
		updateList();
    }

}
