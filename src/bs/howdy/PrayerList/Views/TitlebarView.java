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
	@Inject SerializerService mSerializerService;
	
	public TitlebarView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		_context = context;
		((InjectorProvider)context).getInjector().injectMembers(this);
		
		boolean showAdd = getShowAddAttribute(attrs);
		
		LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.titlebar, this);
		
		LinearLayout layout = (LinearLayout)findViewById(R.id.addPrayerWrapper);
        if(showAdd) {
        	layout.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					_context.startActivity(new Intent(_context, CreateEditPrayer.class));
				}
            });
        } else {
        	layout.setVisibility(View.GONE);
        }
        
        ImageView saveImage = (ImageView)findViewById(R.id.saveLoadImg);
        saveImage.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				mSerializerService.loadAppData(_context, "data.txt");
			}
        });
	}
	
	private boolean getShowAddAttribute(AttributeSet attrs)
	{
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TitlebarView);
		return a.getBoolean(R.styleable.TitlebarView_showAdd, true);
	}
}
