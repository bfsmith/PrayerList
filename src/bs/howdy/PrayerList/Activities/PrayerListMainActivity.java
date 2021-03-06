package bs.howdy.PrayerList.Activities;

import bs.howdy.PrayerList.*;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.*;
import android.widget.TabHost.TabSpec;

public class PrayerListMainActivity extends TabActivity {

	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        /* TabHost will have Tabs */
        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
        
        /* TabSpec used to create a new tab. 
         * By using TabSpec only we can able to setContent to the tab.
         * By using TabSpec setIndicator() we can set name to tab. */
        
        /* tid1 is firstTabSpec Id. Its used to access outside. */
        TabSpec activeTab = tabHost.newTabSpec("tid1");
        TabSpec answeredTab = tabHost.newTabSpec("tid1");
        
        /* TabSpec setIndicator() is used to set name for the tab. */
        /* TabSpec setContent() is used to set content for a particular tab. */
        activeTab.setIndicator(getString(R.string.Active)).setContent(new Intent(this,ActivePrayers.class));
        answeredTab.setIndicator(getString(R.string.Answered)).setContent(new Intent(this,AnsweredPrayers.class));
        
        /* Add tabSpec to the TabHost to display. */
        tabHost.addTab(activeTab);
        tabHost.addTab(answeredTab);
    }
}