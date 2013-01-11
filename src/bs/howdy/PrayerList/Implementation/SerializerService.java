package bs.howdy.PrayerList.Implementation;

import java.io.*;

import org.json.*;

import roboguice.service.RoboService;

import com.google.inject.Inject;

import bs.howdy.PrayerList.Service.Serializer;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SerializerService implements bs.howdy.PrayerList.Service.SerializerService {
	@Inject Serializer mSerializer;
		
	public boolean loadAppData( Context appContext, final String baseName ) {
	    Exception ex = null;
        try {
		    FileInputStream fis = appContext.openFileInput(baseName);
	        String data = readAll(fis);
	        fis.close();
	        mSerializer.deserialize(data);
        } catch(Exception ex1) {
        	Log.e(SerializerService.class.getName(), "Unable to load data: " + ex1.toString(), ex1);
        	return false;
        }
        return true;
	}

	private String readAll( final InputStream is ) throws IOException {
	    if( null == is ) {
	        throw new IllegalArgumentException(JsonSerializer.class.getName()+".readAll() was passed a null stream!");
	    }
	    StringBuilder sb = new StringBuilder();
	    {
	        int rc = 0;
	        while( (rc = is.read()) >= 0 )
	        {
	            sb.append( (char) rc );
	        }
	    }
	    return sb.toString();
	}

	public boolean saveAppData( Context appContext, final String baseName) {
	    try{
	        String data = mSerializer.serialize();
	        final FileOutputStream fos = appContext.openFileOutput( baseName, Context.MODE_PRIVATE);
	        fos.write( data.getBytes() );
	        fos.close();
	    }
	    catch(IOException e){
	        Log.e(JsonSerializer.class.getName(), "Unable to save data: " + e, e);
	        return false;
	    }
	    return true;
	}
}
