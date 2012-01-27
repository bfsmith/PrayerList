package bs.howdy.PrayerList.Activities;

import java.util.Date;

import bs.howdy.PrayerList.*;
import bs.howdy.PrayerList.Adapters.*;
import bs.howdy.PrayerList.Data.DataProvider;
import bs.howdy.PrayerList.Entities.Prayer;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActivePrayers extends BaseListActivity {
	private ActiveAdapter _adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		        
		setContentView(R.layout.active_list);
        _adapter = new ActiveAdapter(this, _dataProvider.getActivePrayers());
        setListAdapter(_adapter);

//        DragDropListView list = (DragDropListView)getListView();
//        list.addDragDropListener(_adapter);
//        list.addFlingListener(_adapter);
	}

    protected void updateList() {
    	_adapter.setPrayers(_dataProvider.getActivePrayers());
    	_adapter.notifyDataSetChanged();
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
    		Prayer p = DataProvider.getInstance().getPrayer(id);
    		if(p == null) continue;
    		p.AnsweredDate = new Date();
    		DataProvider.getInstance().updatePrayer(p);
    	}
    	_rowsChecked.clear();
    	hideActionButtons();
		updateList();
    }
}