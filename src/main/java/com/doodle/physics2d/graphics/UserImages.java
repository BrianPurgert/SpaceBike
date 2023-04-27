
package com.doodle.physics2d.graphics;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import at.emini.physics2D.Body;
import at.emini.physics2D.UserData;
import com.doodle.physics2d.full.spacebike.R;
import com.doodle.physics2d.full.spacebike.R.drawable;

public class UserImages implements UserData {
    public static final String[] Identifiers = {
			"killBody",
			"body",
			"wheel",
			"as4"
	};
    	public static int[] BitmapIds = {
				// Images associated with bike
    		R.drawable.testline,
    		R.drawable.body,
			R.drawable.wheel,
			R.drawable.as4
    };  
    
    public static Bitmap[] bitmaps = new Bitmap[BitmapIds.length];
      public int type = -1;
      public static int  M_TYPE_ONKILL = 0;
      public static int M_TYPE_BIKE = 1; 
      public static int M_TYPE_WHEEL = 2; 
    
    
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public UserImages() {
	}

	private UserImages(int type) {
		this.type = type;
	}
	
	public int type() {
	    return type;	    
	}
	
	public Bitmap getImage() {
		return bitmaps[type];
	}
	
	public static void initBitmaps(Resources res) {
	    for( int i = 0; i < BitmapIds.length; i++) {
			bitmaps[i] = BitmapFactory.decodeResource(res, BitmapIds[i]);
	    }
	}

	
	
	
	@Override
	public UserData copy() {
		return new UserImages(type);
	}

	@Override
	public UserData createNewUserData(String data, int type) {
		for( int i = 0; i < Identifiers.length; i++) {
		    if (data.equals(Identifiers[i])) {
				return new UserImages(i);
		    }
		}
		return null;
	}

}
