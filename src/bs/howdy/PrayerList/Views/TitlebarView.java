package bs.howdy.PrayerList.Views;

import com.google.inject.Inject;
import roboguice.inject.*;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import bs.howdy.PrayerList.*;
import bs.howdy.PrayerList.Activities.*;
import bs.howdy.PrayerList.Service.SerializerService;

public class TitlebarView extends LinearLayout {
	private Context _context;
	
	public TitlebarView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		_context = context;
		((InjectorProvider)context).getInjector().injectMembers(this);

		
		LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.titlebar, this);
		
		boolean showAdd = getShowAddAttribute(attrs);
		ImageView image = (ImageView)findViewById(R.id.addPrayerImg);
        if(showAdd) {
        	image.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					_context.startActivity(new Intent(_context, CreateEditPrayer.class));
				}
            });
        } else {
        	image.setVisibility(View.GONE);
        }
        

		boolean showSettings = getShowSettingsAttribute(attrs);
		image = (ImageView)findViewById(R.id.settingsImg);
        if(showSettings) {
        	image.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					_context.startActivity(new Intent(_context, Settings.class));
				}
	        });
        }
        else {
        	image.setVisibility(View.GONE);
        }
        	
	}
	
	private boolean getShowAddAttribute(AttributeSet attrs)
	{
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TitlebarView);
		return a.getBoolean(R.styleable.TitlebarView_showAdd, true);
	}

	private boolean getShowSettingsAttribute(AttributeSet attrs)
	{
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TitlebarView);
		return a.getBoolean(R.styleable.TitlebarView_showSettings, true);
	}
}
