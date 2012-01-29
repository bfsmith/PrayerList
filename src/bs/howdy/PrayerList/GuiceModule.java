package bs.howdy.PrayerList;

import roboguice.config.AbstractAndroidModule;

public class GuiceModule extends AbstractAndroidModule  {

	@Override
	protected void configure() {
		bind(bs.howdy.PrayerList.Service.ReminderService.class)
			.to(bs.howdy.PrayerList.Implementation.ReminderService.class);
		bind(bs.howdy.PrayerList.Service.PrayerService.class)
			.to(bs.howdy.PrayerList.Implementation.PrayerService.class);
		bind(bs.howdy.PrayerList.Service.DataProvider.class)
			.to(bs.howdy.PrayerList.Data.DataProvider.class);
	}

}
