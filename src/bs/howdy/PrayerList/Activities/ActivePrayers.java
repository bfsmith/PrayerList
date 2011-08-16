package bs.howdy.PrayerList.Activities;

import bs.howdy.PrayerList.*;
import bs.howdy.PrayerList.Adapters.*;
import bs.howdy.PrayerList.Data.*;
import bs.howdy.PrayerList.Entities.*;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ListView;

public class ActivePrayers extends ListActivity {
	private static final int ADDPRAYER_ID = 1;
	
	private DataProvider _dataProvider;
	private ActiveAdapter _adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.active_list);

        _dataProvider = DataProvider.getInstance();
        _adapter = new ActiveAdapter(this, _dataProvider.getActivePrayers());
        setListAdapter(_adapter);
        Log.v(Constants.LOG_TAG, "ActivePrayers onCreate");
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.v(Constants.LOG_TAG, "ActivePrayers onStart");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.v(Constants.LOG_TAG, "ActivePrayers onResume");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Log.v(Constants.LOG_TAG, "ActivePrayers onPause");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, ADDPRAYER_ID, 0, R.string.AddPrayer);
        return result;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
	        case ADDPRAYER_ID:
	        	startActivityForResult(new Intent(this, ActivePrayerInfo.class), Constants.Requests.PRAYER_CREATEUPDATE);
	            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	if(requestCode == Constants.Requests.PRAYER_CREATEUPDATE) {
    		if(resultCode == RESULT_OK) {
		        updateList();
    		}
    	}
    	else {
    		super.onActivityResult(requestCode, resultCode, intent);
    	}
    }
    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		Prayer p = (Prayer)this.getListAdapter().getItem(position);
		Log.v(Constants.LOG_TAG, "Selected " + p.Title);
		
		Intent editIntent = new Intent(this, ActivePrayerInfo.class);
		editIntent.putExtra(Constants.Extras.ID, p.Id);
		startActivityForResult(editIntent, Constants.Requests.PRAYER_CREATEUPDATE);
	}
    
    private void updateList() {
    	Log.v(Constants.LOG_TAG, "ActivePrayers.updateList()");
    	_adapter.setPrayers(_dataProvider.getActivePrayers());
    	_adapter.notifyDataSetChanged();
    }
}