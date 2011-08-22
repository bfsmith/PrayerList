package bs.howdy.PrayerList.Activities;

import bs.howdy.PrayerList.Constants;
import bs.howdy.PrayerList.R;
import bs.howdy.PrayerList.Adapters.*;
import bs.howdy.PrayerList.Data.DataProvider;
import bs.howdy.PrayerList.Entities.Prayer;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AnsweredPrayers extends BaseListActivity {
	private AnsweredAdapter _adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.answered_list);
        _adapter = new AnsweredAdapter(this, _dataProvider.getAnsweredPrayers());
        setListAdapter(_adapter);
	}

    protected void updateList() {
    	_adapter.setPrayers(_dataProvider.getAnsweredPrayers());
    	_adapter.notifyDataSetChanged();
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
    		Prayer p = DataProvider.getInstance().getPrayer(id);
    		if(p == null) continue;
    		p.AnsweredDate = null;
    		DataProvider.getInstance().updatePrayer(p);
    	}
    	_rowsChecked.clear();
    	hideActionButtons();
		updateList();
    }

}
