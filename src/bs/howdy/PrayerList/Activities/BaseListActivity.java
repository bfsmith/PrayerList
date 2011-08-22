package bs.howdy.PrayerList.Activities;

import java.util.ArrayList;

import bs.howdy.PrayerList.Constants;
import bs.howdy.PrayerList.R;
import bs.howdy.PrayerList.Data.DataProvider;
import bs.howdy.PrayerList.Entities.Prayer;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public abstract class BaseListActivity extends ListActivity {
	
	protected DataProvider _dataProvider;
	protected ArrayList<Integer> _rowsChecked;
	protected Animation _slideUp;
	protected Animation _slideDown;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);      

        _dataProvider = DataProvider.getInstance();
        
        _rowsChecked = new ArrayList<Integer>();
        _slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        _slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		_rowsChecked.clear();
		LinearLayout ll = (LinearLayout)findViewById(R.id.actionWrapper);
		ll.setVisibility(View.GONE);
		updateList();
	}
	
	@Override
	public void onPause() {
		super.onPause();
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

    public void deletePrayers(View v) {
    	new AlertDialog.Builder(this)
    		.setMessage(getResources().getString(R.string.DeleteConfirm))
	       .setCancelable(true)
	       .setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
	           	public void onClick(DialogInterface dialog, int id) {
	        	   	for(int pid : _rowsChecked) {
		           		Prayer p = DataProvider.getInstance().getPrayer(pid);
		           		if(p == null) continue;
		           		DataProvider.getInstance().removePrayer(p);
	        	   	}
		           	_rowsChecked.clear();
		           	hideActionButtons();
		       		updateList();
	           	}
	       	})
	       .setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
	           	public void onClick(DialogInterface dialog, int id) {    
	           	}
	       	})
	       .create()
	       .show();
    }
    
    protected void showActionButtons() {
    	LinearLayout ll = (LinearLayout)findViewById(R.id.actionWrapper);
		ll.setVisibility(View.VISIBLE);
		ll.startAnimation(_slideUp);
    }
    
    protected void hideActionButtons() {
    	LinearLayout ll = (LinearLayout)findViewById(R.id.actionWrapper);
		ll.startAnimation(_slideDown);
		ll.setVisibility(View.GONE);
    }
    
    protected Prayer getPrayerFromView(View view) {
    	final int position = getListView().getPositionForView((View) view.getParent());
    	if(position < 0) return null;
    	return (Prayer)getListView().getAdapter().getItem(position);
    }
    
    protected abstract void updateList();
}
