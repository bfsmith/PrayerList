package bs.howdy.PrayerList.Activities;

import java.util.Date;

import bs.howdy.PrayerList.*;
import bs.howdy.PrayerList.Adapters.*;
import bs.howdy.PrayerList.Entities.Prayer;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActivePrayers extends BaseListActivity {
	private ActiveAdapter mAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		        
		setContentView(R.layout.active_list);
        mAdapter = new ActiveAdapter(this, mPrayerService.getActivePrayers());
        setListAdapter(mAdapter);

//        DragDropListView list = (DragDropListView)getListView();
//        list.addDragDropListener(_adapter);
//        list.addFlingListener(_adapter);
	}

    protected void updateList() {
    	mAdapter.setPrayers(mPrayerService.getActivePrayers());
    	mAdapter.notifyDataSetChanged();
    }

    public void editPrayer(View view) {
    	Prayer p = getPrayerFromView(view);
		if(p == null) return;
		Intent editIntent = new Intent(this, CreateEditPrayer.class);
		editIntent.putExtra(Constants.Extras.ID, p.Id);
		startActivityForResult(editIntent, Constants.Requests.PRAYER_CREATEUPDATE);
    }

    public void viewPrayer(View view) {
    	Prayer p = getPrayerFromView(view);
		if(p == null) return;
		Intent editIntent = new Intent(this, PrayerInfo.class);
		editIntent.putExtra(Constants.Extras.ID, p.Id);
		startActivityForResult(editIntent, Constants.Requests.PRAYER_CREATEUPDATE);
    }
    
    public void prayersAnswered(View v) {
    	for(int id : _rowsChecked) {
    		Prayer p = mPrayerService.getPrayer(id);
    		if(p == null) continue;
    		p.AnsweredDate = new Date();
    		mPrayerService.updatePrayer(p);
    	}
    	_rowsChecked.clear();
    	hideActionButtons();
		updateList();
    }
}