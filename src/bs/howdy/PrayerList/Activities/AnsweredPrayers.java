package bs.howdy.PrayerList.Activities;

import bs.howdy.PrayerList.Constants;
import bs.howdy.PrayerList.R;
import bs.howdy.PrayerList.Adapters.*;
import bs.howdy.PrayerList.Data.*;
import bs.howdy.PrayerList.Entities.Prayer;
import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

public class AnsweredPrayers extends ListActivity {
	private DataProvider _dataProvider;
	private AnsweredAdapter _adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.answered_list);

		_dataProvider = DataProvider.getInstance();
        _adapter = new AnsweredAdapter(this, _dataProvider.getAnsweredPrayers());
        setListAdapter(_adapter);
        Log.v(Constants.LOG_TAG, "AnsweredPrayers onCreate");
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.v(Constants.LOG_TAG, "AnsweredPrayers onStart");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		updateList();
		Log.v(Constants.LOG_TAG, "AnsweredPrayers onResume");
	}
	
	@Override
	public void onPause() {
		super.onResume();
		Log.v(Constants.LOG_TAG, "AnsweredPrayers onPause");
	}

    private void updateList() {
    	_adapter.setPrayers(_dataProvider.getAnsweredPrayers());
    	_adapter.notifyDataSetChanged();
    }

    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		Prayer p = (Prayer)this.getListAdapter().getItem(position);
		Log.v(Constants.LOG_TAG, "Selected " + p.Title);
		
		Intent editIntent = new Intent(this, AnsweredPrayerInfo.class);
		editIntent.putExtra(Constants.Extras.ID, p.Id);
		startActivityForResult(editIntent, Constants.Requests.PRAYER_CREATEUPDATE);
	}
    
}
