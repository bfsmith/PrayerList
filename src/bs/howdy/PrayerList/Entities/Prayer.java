package bs.howdy.PrayerList.Entities;

import java.util.Date;

public class Prayer {
	public int Id;
	public String Title;
	public String Description;
	public Date CreatedDate;
	public Date AnsweredDate;
	public int Ordinal;
	
	public Prayer() {
		Id = -1;
		AnsweredDate = null;
	}
	
	public Prayer(String title, String description) {
		Id = -1;
		Title = title;
		Description = description;
		CreatedDate = new Date();
		AnsweredDate = null;
	}
	
	public boolean IsAnswered() {
		return AnsweredDate != null;
	}
}
