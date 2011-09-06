package bs.howdy.PrayerList.Activities;

import java.text.SimpleDateFormat;

import bs.howdy.PrayerList.*;
import bs.howdy.PrayerList.Data.*;
import bs.howdy.PrayerList.Entities.*;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;

public class PrayerInfo extends Activity {
	private DataProvider _dataProvider;
	private int _id;
	private SimpleDateFormat _dateFormat;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _dateFormat = new SimpleDateFormat(App.getContext().getString(R.string.DateFormat));
        
        _dataProvider = DataProvider.getInstance();
        _id = 0;
        
        Bundle extras = getIntent().getExtras();
        
        if(extras == null || !extras.containsKey(Constants.Extras.ID))
        	finish();

    	_id = extras.getInt(Constants.Extras.ID);
    	Prayer p = _dataProvider.getPrayer(_id);
    	
    	if(p == null)
    		finish();
        
        if(p.AnsweredDate == null)
        	setContentView(R.layout.active_prayer);
        else
        	setContentView(R.layout.answered_prayer);
        
		TextView titleText = (TextView)findViewById(R.id.title);
		titleText.setText(p.Title);
		TextView createdDate = (TextView)findViewById(R.id.createdDate);
		createdDate.setText(_dateFormat.format(p.CreatedDate));
		TextView descriptionText = (TextView)findViewById(R.id.description);
		descriptionText.setText(p.Description);
		
		if(p.AnsweredDate != null) {
			TextView answeredDate = (TextView)findViewById(R.id.answeredDate);
    		answeredDate.setText(_dateFormat.format(p.AnsweredDate));
		}
	}
}
