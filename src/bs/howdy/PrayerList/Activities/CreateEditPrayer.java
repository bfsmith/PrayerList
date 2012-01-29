package bs.howdy.PrayerList.Activities;

import roboguice.activity.RoboActivity;

import com.google.inject.Inject;

import bs.howdy.PrayerList.*;
import bs.howdy.PrayerList.Entities.*;
import bs.howdy.PrayerList.Service.PrayerService;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateEditPrayer extends RoboActivity {
	private @Inject	PrayerService mPrayerService;
	private int mId;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_edit_prayer);
        
        mId = 0;
        
        Bundle extras = getIntent().getExtras();
        
        if(extras != null && extras.containsKey(Constants.Extras.ID)) {
        	mId = extras.getInt(Constants.Extras.ID);
        	Prayer p = mPrayerService.getPrayer(mId);
        	if(p != null) {
        		EditText titleText = (EditText)findViewById(R.id.title);
        		titleText.setText(p.Title);
        		EditText descriptionText = (EditText)findViewById(R.id.description);
        		descriptionText.setText(p.Description);
        	}
        }
	}
	
	public void saveButton_Click(View view) {
		EditText titleText = (EditText)findViewById(R.id.title);
		EditText descriptionText = (EditText)findViewById(R.id.description);
		String title = titleText.getText().toString();
		String description = descriptionText.getText().toString();
		 
		if(title == null || title.trim().equals("")) {
			Toast.makeText(getApplicationContext(), R.string.NoTitleText, Toast.LENGTH_SHORT).show();
			return;
		}
		 
		if(description == null) {
			description = "";
		}
		
		if(mId <= 0) {
			Prayer p = new Prayer(title.trim(), description.trim());
			mPrayerService.addPrayer(p);
		} else {
			Prayer p = mPrayerService.getPrayer(mId);
			p.Title = title.trim();
			p.Description = description.trim();
			mPrayerService.updatePrayer(p);
		}
		setResult(RESULT_OK);
		finish();
	 }
	
	public void cancelButton_Click(View view) {
		setResult(RESULT_CANCELED);
		finish();
	 }
}
