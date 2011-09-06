package bs.howdy.PrayerList.Activities;

import bs.howdy.PrayerList.*;
import bs.howdy.PrayerList.Data.*;
import bs.howdy.PrayerList.Entities.*;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateEditPrayer extends Activity {
	private DataProvider _dataProvider;
	private int _id;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_edit_prayer);
        
        _dataProvider = DataProvider.getInstance();
        _id = 0;
        
        Bundle extras = getIntent().getExtras();
        
        if(extras != null && extras.containsKey(Constants.Extras.ID)) {
        	_id = extras.getInt(Constants.Extras.ID);
        	Prayer p = _dataProvider.getPrayer(_id);
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
		
		if(_id <= 0) {
			Prayer p = new Prayer(title.trim(), description.trim());
			_dataProvider.addPrayer(p);
		} else {
			Prayer p = _dataProvider.getPrayer(_id);
			p.Title = title.trim();
			p.Description = description.trim();
			_dataProvider.updatePrayer(p);
		}
		setResult(RESULT_OK);
		finish();
	 }
	
	public void cancelButton_Click(View view) {
		setResult(RESULT_CANCELED);
		finish();
	 }
}
