package bs.howdy.PrayerList.Activities;

import java.util.*;

import bs.howdy.PrayerList.*;
import bs.howdy.PrayerList.Adapters.*;
import bs.howdy.PrayerList.Data.*;
import bs.howdy.PrayerList.Entities.*;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.animation.*;
import android.widget.*;

public class ActivePrayers extends ListActivity {
	private static final int ADDPRAYER_ID = 1;
	
	private DataProvider _dataProvider;
	private ActiveAdapter _adapter;
	private ArrayList<Integer> _rowsChecked;
	private Animation _slideUp;
	private Animation _slideDown;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.active_list);        

        _dataProvider = DataProvider.getInstance();
        _adapter = new ActiveAdapter(this, _dataProvider.getActivePrayers());
        setListAdapter(_adapter);
        
        _rowsChecked = new ArrayList<Integer>();
        _slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        _slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        

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
		_rowsChecked.clear();
		updateList();
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
    
    public void prayerSelected(View view) {
    	Prayer p = getPrayerFromView(view);
		if(p == null) return;
		Intent editIntent = new Intent(this, ActivePrayerInfo.class);
		editIntent.putExtra(Constants.Extras.ID, p.Id);
		startActivityForResult(editIntent, Constants.Requests.PRAYER_CREATEUPDATE);
    }
    
    public void rowChecked(View view) {
    	CheckBox c = (CheckBox)view;
    	Prayer p = getPrayerFromView(view);
    	if(c.isChecked())
    		_rowsChecked.add(p.Id);
    	else {
    		int index = _rowsChecked.indexOf(p.Id);
    		_rowsChecked.remove(index);
    	}

    	if(_rowsChecked.size() == 1 && c.isChecked()) {
    		showActionButtons();
    	}
    	else if(_rowsChecked.size() == 0) {
    		hideActionButtons();
    	}
    }
    
    private void showActionButtons() {
    	LinearLayout ll = (LinearLayout)findViewById(R.id.LinearLayout01);
		ll.setVisibility(View.VISIBLE);
		ll.startAnimation(_slideUp);
    }
    
    private void hideActionButtons() {
    	LinearLayout ll = (LinearLayout)findViewById(R.id.LinearLayout01);
		ll.startAnimation(_slideDown);
		ll.setVisibility(View.GONE);
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

    public void deletePrayers(View v) {
    	for(int id : _rowsChecked) {
    		Prayer p = DataProvider.getInstance().getPrayer(id);
    		if(p == null) continue;
    		p.AnsweredDate = new Date();
    		DataProvider.getInstance().removePrayer(p);
    	}
    	_rowsChecked.clear();
    	hideActionButtons();
		updateList();
    }
    
    private Prayer getPrayerFromView(View view) {
    	final int position = getListView().getPositionForView((View) view.getParent());
    	if(position < 0) return null;
    	return (Prayer)getListView().getAdapter().getItem(position);
    }
    
    private void updateList() {
    	Log.v(Constants.LOG_TAG, "ActivePrayers.updateList()");
    	_adapter.setPrayers(_dataProvider.getActivePrayers());
    	_adapter.notifyDataSetChanged();
    }
}