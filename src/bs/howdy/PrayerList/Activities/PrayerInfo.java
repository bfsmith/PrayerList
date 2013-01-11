package bs.howdy.PrayerList.Activities;

import java.text.SimpleDateFormat;

import com.google.inject.Inject;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import bs.howdy.PrayerList.*;
import bs.howdy.PrayerList.Entities.*;
import bs.howdy.PrayerList.Service.PrayerService;
import bs.howdy.PrayerList.Service.ReminderService;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class PrayerInfo extends RoboActivity {
	private @Inject PrayerService mPrayerService;
//	private @Inject ReminderService mReminderService;
//	private Button mReminderButton;
	private int mId;
	private SimpleDateFormat mDateFormat;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDateFormat = new SimpleDateFormat(App.getContext().getString(R.string.DateFormat));
        mId = 0;
        
        Bundle extras = getIntent().getExtras();
        
        if(extras == null || !extras.containsKey(Constants.Extras.ID))
        	finish();

    	mId = extras.getInt(Constants.Extras.ID);
    	Prayer p = mPrayerService.getPrayer(mId);
    	
    	if(p == null) {
    		finish();
    		return;
    	}
    	
        if(p.AnsweredDate == null)
        	setContentView(R.layout.active_prayer);
        else
        	setContentView(R.layout.answered_prayer);
        
		TextView titleText = (TextView)findViewById(R.id.title);
		titleText.setText(p.Title);
		TextView createdDate = (TextView)findViewById(R.id.createdDate);
		createdDate.setText(mDateFormat.format(p.CreatedDate));
		TextView descriptionText = (TextView)findViewById(R.id.description);
		descriptionText.setText(p.Description);
		
//		mReminderButton = (Button)findViewById(R.id.reminderButton);
//    	setReminderText(mReminderService.isReminderOn(p));
        
		if(p.AnsweredDate != null) {
			TextView answeredDate = (TextView)findViewById(R.id.answeredDate);
    		answeredDate.setText(mDateFormat.format(p.AnsweredDate));
		}	
	}

    public void sharePrayer(View v) {
		Prayer p = mPrayerService.getPrayer(mId);
		Utility.sharePrayer(p, this);
    }

    public void toggleReminder(View v) {
    	Prayer p = mPrayerService.getPrayer(mId);
//    	mReminderService.toggleReminder(p);
    }
    
    private void setReminderText(boolean reminderOn) {
    	String text = reminderOn
    			? getString(R.string.NoRemindMe)
				: getString(R.string.RemindMe);
//    	mReminderButton.setText(text);
    }
}
