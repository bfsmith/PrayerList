package bs.howdy.PrayerList.Implementation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import bs.howdy.PrayerList.Entities.Prayer;
import bs.howdy.PrayerList.Service.DataProvider;

import com.google.inject.Inject;

// http://developer.android.com/reference/org/json/package-summary.html

public class JsonSerializer implements bs.howdy.PrayerList.Service.Serializer {
	private DataProvider mdp;
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@Inject
	public JsonSerializer(DataProvider dp) {
		mdp = dp;
	}
	
	public String serialize() {
		JSONObject json = new JSONObject();
		
		JSONArray activePrayers = new JSONArray();
		for(Prayer p : mdp.getActivePrayers()) {
			JSONObject o = serialize(p);
			if(o != null)
				activePrayers.put(o);
		}
		try {
			json.put("activeprayers", activePrayers);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		JSONArray answeredPrayers = new JSONArray();
		for(Prayer p : mdp.getAnsweredPrayers()) {
			JSONObject o = serialize(p);
			if(o != null)
				answeredPrayers.put(o);
		}
		try {
			json.put("answeredprayers", answeredPrayers);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return json.toString();
	}
	
	private JSONObject serialize(Prayer p) {
		if(p == null) return null;
		JSONObject json = new JSONObject();
		try {
			json.put("id", p.Id);
		json.put("title", p.Title);
		json.put("description", p.Description);
		json.put("createddate", dateToString(p.CreatedDate));
		if(p.AnsweredDate != null)
			json.put("answereddate", dateToString(p.AnsweredDate));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return json;
	}

	private String dateToString(Date d) {
		if(d == null) return null;
		return mDateFormat.format(d);
	}

	private Date stringToDate(String s) {
		if(s == null) return null;
		try {
			return mDateFormat.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Prayer deserialize(JSONObject json) {
		if(json == null) return null;
		try {
			Prayer p = new Prayer();
			p.Id = json.getInt("id");
			p.Title = json.getString("title");
			p.Description = json.getString("description");
			p.CreatedDate = stringToDate(json.getString("createddate"));
//			if(!json.isNull("answereddate"))
//				p.AnsweredDate = stringToDate(json.getString("answereddate"));
			return p;
		} catch (JSONException e) {
			Log.e(JsonSerializer.class.getName(), "Error parsing JSON", e);
			return null;
		}
	}
	
	public void deserialize(String data) {
		try {
			JSONObject json = new JSONObject(data);
			JSONArray activePrayers = json.getJSONArray("activeprayers");
			if(activePrayers != null) {
				for(int i = 0; i < activePrayers.length(); i++) {
					loadPrayer(activePrayers.getJSONObject(i));
					// Don't add if they're identical
				}
			}
			
			JSONArray answeredPrayers = json.getJSONArray("answeredprayers");
			if(answeredPrayers != null) {
				for(int i = 0; i < answeredPrayers.length(); i++) {
					loadPrayer(answeredPrayers.getJSONObject(i));
					// Don't add if they're identical
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	private void loadPrayer(JSONObject json) {
		Prayer p = deserialize(json);
		if(p == null) return;
		Prayer existingPrayer = mdp.getPrayer(p.Id);
		if(existingPrayer == null) {
			mdp.addPrayer(p);
		} else if(!existingPrayer.Title.equals(p.Title) 
				|| !existingPrayer.Description.equals(p.Description) 
				|| !existingPrayer.CreatedDate.equals(p.CreatedDate)
				|| existingPrayer.AnsweredDate != p.AnsweredDate
				|| !(existingPrayer.AnsweredDate != null && p.AnsweredDate != null && existingPrayer.AnsweredDate.equals(p.AnsweredDate))) {
			mdp.addPrayer(p);
		}
	}

}
