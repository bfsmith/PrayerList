package bs.howdy.PrayerList.Activities;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import roboguice.activity.RoboListActivity;
import roboguice.inject.InjectView;

import bs.howdy.PrayerList.Constants;
import bs.howdy.PrayerList.R;
import bs.howdy.PrayerList.Entities.Prayer;
import bs.howdy.PrayerList.Service.CategoryService;
import bs.howdy.PrayerList.Service.PrayerService;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

public abstract class BaseListActivity extends RoboListActivity {
	@Inject CategoryService mCategoryService;
	@InjectView(R.id.actionWrapper) LinearLayout actionWrapper;
	
	protected @Inject PrayerService mPrayerService;
	protected ArrayList<Integer> _rowsChecked;
	protected Animation _slideUp;
	protected Animation _slideDown;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);      

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
		actionWrapper.setVisibility(View.GONE);
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

    public void editCategory(View v) {
    	ArrayList<String> options = new ArrayList<String>();
    	options.add(getResources().getString(R.string.NewCategory));
    	options.add(getResources().getString(R.string.ClearCategory));
    	List<String> categories = mCategoryService.getCategories();
    	for(String cat : categories) {
    		options.add(cat);
    	}
    	
    	final String[] optionArray = new String[options.size()];
    	options.toArray(optionArray);
    	
    	final RoboListActivity _this = this;
    	new AlertDialog.Builder(this)
    		.setTitle(R.string.PickACategory)
    		.setCancelable(true)
    		.setItems(optionArray, new DialogInterface.OnClickListener() {
    			
	           	public void onClick(DialogInterface dialog, int optionIndex) {
	           		String picked = optionArray[optionIndex];
	           		if(picked.equals(getResources().getString(R.string.NewCategory))) {
	           			final EditText input = new EditText(_this);
	           			
	           			new AlertDialog.Builder(_this)
	           				.setTitle(R.string.CategoryName)
	           				.setCancelable(true)
	           				.setView(input)
	           				.setPositiveButton(getResources().getString(R.string.Save), new DialogInterface.OnClickListener() {
	           					public void onClick(DialogInterface dialog, int id) {
	           						String picked = input.getText().toString();
	           						mCategoryService.addCategory(picked);
	           						addPrayersToCategory(_rowsChecked, picked);
	           						clearChecksAndUpdate();
	           		           	}
	           		       	})
	           		       .setNegativeButton(getResources().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
	           		           	public void onClick(DialogInterface dialog, int id) {
	           		           	}
	           		       	})
	           		       .create()
	           		       .show();
	           			
	           			return;
	           		}
	           		else if(picked.equals(getResources().getString(R.string.ClearCategory))) {
	           			// Keep category = null
	           			for(int pid : _rowsChecked) {
			           		Prayer p = mPrayerService.getPrayer(pid);
			           		if(p == null) continue;
	           				String cat = mCategoryService.getPrayerCategory(p);
	           				if(cat != null)
	           					mCategoryService.removePrayerFromCategory(p, cat);
	           			}
	           			clearChecksAndUpdate();
	           			return;
	           		}
	           		else {
		           		addPrayersToCategory(_rowsChecked, picked);
		           		clearChecksAndUpdate();
	           		}
	           	}
	       	})
	       	.create()
	       	.show();
    }
    
    private void clearChecksAndUpdate() {
    	_rowsChecked.clear();
       	hideActionButtons();
   		updateList();
    }
    
    private void addPrayersToCategory(List<Integer> pids, String category) {
    	for(int pid : pids) {
       		Prayer p = mPrayerService.getPrayer(pid);
       		if(p == null) continue;
			String cat = mCategoryService.getPrayerCategory(p);
			if(cat != null && cat.equals(category))
				continue;
			if(cat != null)
				mCategoryService.removePrayerFromCategory(p, cat);
       		mCategoryService.addPrayerToCategory(p, category);
	   	}
    }
    
    public void deletePrayers(View v) {
    	new AlertDialog.Builder(this)
    		.setMessage(getResources().getString(R.string.DeleteConfirm))
	       .setCancelable(true)
	       .setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
	           	public void onClick(DialogInterface dialog, int id) {
	        	   	for(int pid : _rowsChecked) {
		           		Prayer p = mPrayerService.getPrayer(pid);
		           		if(p == null) continue;
		           		mPrayerService.removePrayer(p);
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
    	actionWrapper.setVisibility(View.VISIBLE);
    	actionWrapper.startAnimation(_slideUp);
    }
    
    protected void hideActionButtons() {
    	actionWrapper.startAnimation(_slideDown);
    	actionWrapper.setVisibility(View.GONE);
    }
    
    protected Prayer getPrayerFromView(View view) {
    	final int position = getListView().getPositionForView((View) view.getParent());
    	if(position < 0) return null;
    	return (Prayer)getListView().getAdapter().getItem(position);
    }
    
    protected abstract void updateList();
}
