package com.doodle.physics2d.full.spacebike;

import com.doodle.physics2d.full.spacebike.R;// This was previously removed, Added it back in for testing
import com.doodle.physics2d.graphics.SimulationView;// This was previously removed, Added it back in for testing

public class LevelControl {

	// -------- Levels --------//
   public static void level(int x){
	   switch(x){
	   case 0: Level0(); break;
	   case 1: Level1(); break;
	   case 2: Level2(); break;
	   case 3: Level3(); break;
	   case 4: Level4(); break;
	   case 5: Level5(); break;
	   case 6: Level6(); break;
	   case 7: Level7(); break;
	   case 8: Level8(); break;
	   case 9: Level9(); break;
	   case 10: Level10(); break;
	   
	   case 11: Level11(); break;
	   case 12: Level12(); break;
	   case 13: Level13(); break;
	   case 14: Level14(); break;
//	   case 15: Level15(); break;
/*	   case 16: Level16(); break;
	   case 17: Level17(); break;
	   case 18: Level18(); break;
	   case 19: Level19(); break;
	   case 20: Level20(); break;  */
	   default: Level0(); break;
	   }
    };
	
	public static final void Level0(){						
		DoodleBikeMain.Xaxis = true;                       // If the camera goes up and down
		DoodleBikeMain.Drawlandscape = true;                // Draw Landscape 
		DoodleBikeMain.pickedWorld = R.raw.tutorial;			 // Originally .infotest before simpletest
		DoodleBikeMain.timelimit = 250000;
	    DoodleBikeMain.lvlId = 0;  // for unlocking levels	and determining the level
	}
	
	
	public static final void Level1(){						
		DoodleBikeMain.Xaxis = true;                       // If the camera goes up and down				
		DoodleBikeMain.Drawlandscape = true;                // Draw Landscape 
		DoodleBikeMain.pickedWorld = R.raw.level0;			 // World to load						     	     
	    DoodleBikeMain.lvlId = 1;
	    DoodleBikeMain.timelimit = 60000;
	}
	
	public static void Level2(){						
		DoodleBikeMain.Xaxis = true;                       // If the camera goes up and down
		DoodleBikeMain.Drawlandscape = true;                // Draw Landscape 
		DoodleBikeMain.pickedWorld = R.raw.level;			 // World to load	
	    DoodleBikeMain.lvlId = 2;  // for unlocking levels	and determining the level     	
	    DoodleBikeMain.timelimit = 150000;
	}
	
	public static void Level3(){						
		DoodleBikeMain.Xaxis = true;                       // If the camera goes up and down
		DoodleBikeMain.Drawlandscape = true;                // Draw Landscape 
		DoodleBikeMain.pickedWorld = R.raw.practice0;			 // World to load	
		DoodleBikeMain.lvlId = 3;  // for unlocking levels	and determining the level
		DoodleBikeMain.timelimit = 150000;
	}
	
	public static void Level4(){						
		DoodleBikeMain.Xaxis = true;                       // If the camera goes up and down
		DoodleBikeMain.Drawlandscape = true;                // Draw Landscape 
		DoodleBikeMain.pickedWorld = R.raw.five;			 // World to load		     
	    DoodleBikeMain.lvlId = 4;
	}
	
	public static void Level5(){								
		defaultSettings();               
		DoodleBikeMain.pickedWorld = R.raw.level4;			 	     
		DoodleBikeMain.lvlId = 5;
	}
	public static void Level6(){								
		defaultSettings();               
		DoodleBikeMain.pickedWorld = R.raw.level4;			 	     
		DoodleBikeMain.lvlId = 6;
	}
	
	
	public static void Level7(){								
		defaultSettings();               
		DoodleBikeMain.pickedWorld = R.raw.level4;			 	     
		DoodleBikeMain.lvlId = 7;
	}
	public static void Level8(){								
		defaultSettings();               
		DoodleBikeMain.pickedWorld = R.raw.level5;			 	     
	     DoodleBikeMain.lvlId = 8;      			 
	}
	public static void Level9(){								
		defaultSettings();               
	//	DoodleBikeMain.pickedWorld = R.raw.level6;			 	     
	     DoodleBikeMain.lvlId = 9;      			 
	}
	public static void Level10(){								
		defaultSettings();               
//		DoodleBikeMain.pickedWorld = R.raw.level7;			 	     
	     DoodleBikeMain.lvlId = 10;      			 
	}
	public static void Level11(){								
		defaultSettings();               
//		DoodleBikeMain.pickedWorld = R.raw.level8;			 	     
	     DoodleBikeMain.lvlId = 11;      			 
	}
	public static void Level12(){								
		defaultSettings();               
//		DoodleBikeMain.pickedWorld = R.raw.level9;			 	     
	     DoodleBikeMain.lvlId = 12;      			 
	}
	public static void Level13(){								
		defaultSettings();               
//		DoodleBikeMain.pickedWorld = R.raw.level10;			 	     
	     DoodleBikeMain.lvlId = 13;      			 
	}
	public static void Level14(){								
		defaultSettings();               
//		DoodleBikeMain.pickedWorld = R.raw.level11;			 	     
	     DoodleBikeMain.lvlId = 14;      			 
	}

private static void defaultSettings(){
	DoodleBikeMain.Xaxis = true;                     

}
	// -------- Levels --------//
	
}
