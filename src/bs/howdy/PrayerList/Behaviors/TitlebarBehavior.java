package bs.howdy.PrayerList.Behaviors;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import bs.howdy.PrayerList.*;
import bs.howdy.PrayerList.Activities.*;

public class TitlebarBehavior {
	private Activity _activity;
	private final boolean _customTitleSupported;
	
	public TitlebarBehavior(Activity activity) {
		_activity = activity;
		_customTitleSupported = activity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	}
	
	public void setTitleBar() {
		setTitleBar(true);
	}
	
	public void setTitleBar(boolean showAdd) {
		if ( _customTitleSupported ) {
        	_activity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
            ImageView image = (ImageView)_activity.findViewById(R.id.addPrayerImage);
            if(showAdd) {
	            image.setOnClickListener(new OnClickListener() {
					public void onClick(View view) {
						_activity.startActivityForResult(new Intent(_activity, CreateEditPrayer.class), Constants.Requests.PRAYER_CREATEUPDATE);
					}
	            });
            } else {
            	image.setVisibility(View.GONE);
            }
        }
	}
}
