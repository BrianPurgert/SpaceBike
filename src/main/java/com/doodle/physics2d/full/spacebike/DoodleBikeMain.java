/*
 * Doodle Bike source 
 * by Brian Purgert
 * 
 *  * Copyright (c) 2010, DoodleBike - Brian Purgert
 * All rights reserved.
 * Emini Physics engine 
 * by Alexander Adensamer
 * Copyright (c) 2010, Emini Physics - Alexander Adensamer
 * All rights reserved.
 * 

 */

package com.doodle.physics2d.full.spacebike;

import static at.emini.a.d.R;

import android.graphics.Point;
import android.view.Display;
import android.widget.LinearLayout;
import android.widget.PopupWindow; 
import android.view.ViewGroup.LayoutParams; 
import android.media.MediaPlayer;
import android.os.AsyncTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import at.emini.physics2D.Body;
import at.emini.physics2D.World;
import at.emini.physics2D.util.PhysicsFileReader;

import com.doodle.physics2d.full.spacebike.R;
import com.doodle.physics2d.graphics.GraphicsWorld;
import com.doodle.physics2d.graphics.SimulationView;
import com.doodle.physics2d.graphics.UserImages;

/* TODO Update Forward, Backward Buttons Images */
/* TODO Center Camera on Biker */
/* TODO Update Biker Wheel Image */
/* TODO Update Biker Body */
/* TODO Increase Drawer Size so that all objects in the simulation view get Drawn */
 /*NOTE The Default Bike will load at 450 Units X and -300 Units Y*/

public class DoodleBikeMain extends Activity {

	TextView timer; // textview to display the countdown
	private boolean win;
	// -------Classes-----//
	private Handler handler;
	private GraphicsWorld world;
	public World bike;
	public LevelControl level;
	public GameEvents events;
	private SimulationView simulationView;
	private SensorManager mSensorManager;
	// -------Loading Level Variables-----//
	public static int background;
	public static int forgroundBottom;
	public static int forgroundTop;
	public static boolean Xaxis;
	public static boolean Drawlandscape;
	public static int pickedWorld;
	// -------Loading Bike Variables-----//
	public static int pickedBike;
	public static int WheelSpeed;
	// ------Turns on Content Views----//
	public static int controlsvisibility;
	public static boolean theWinMenu;
	public static boolean clicksound;
	// ------public static boolean theLoseMenu;
	public static boolean death;

	public static boolean info1;
	public static boolean info2;
	public static boolean info3;
	public static boolean info4;
	public static boolean info5;
	public static boolean pause = false;
	public static int blink;
	private int BLock = 0;
	private boolean poweroff;
	private boolean power;

	private int i1 = 0;
	private int i2 = 0;
	private int i3 = 0;
	private int i4 = 0;
	private int i5 = 0;

	public static int tiltX;
	public static int maxT;
	// -------level unlocker-------//
	public static int lvlId;
	public static int lvlsComplete=5;
	// -----Turns of event checking---//
	private boolean SeeEvents = true;
	private boolean forwardwasreleased;
	private boolean reversewasreleased;
	private boolean stopwasreleased;

	private boolean Up;
	private boolean Down;
	private boolean NoGravity;
	private boolean Left;
	private boolean Right;
	private boolean locker = false;
	private boolean gravitycontrolislocked = false;
	private boolean tickpassed;

	public static int bikeloc;
	private int fullscreenwidth;
	public static boolean AAON;

	Animation myFadeInAnimation;
	Animation myFadeOutAnimation;

	public static long timelimit;
	public static int batteryleft;
	public static int timeleft;
	public static int timerestart;
	public TextView mTime; // Where the time will be displayed
	public TextView rTime;
	private CountDownTimer tReset;
	public static int screenWidth;
	public static int screenHeight;
	public static int screenDirection;
	// ==============================================================//
	// ----------------------------Sound-----------------------------//
	// ==============================================================//
	// private boolean soundPlaying; //
	public SoundManager mSoundManager; //
	// ==============================================================//

	public void onCreate(Bundle savedInstanceState) {
		// setContentView(R.layout.loading);
		setRequestedOrientation(0);
		super.onCreate(savedInstanceState);


		// Get the device Width and Height to be used in centering playa
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenWidth = size.x;
		screenHeight = size.y;

		// =================================================//
		//               Set up shitty FX sounds            //
		// =================================================//
		mSoundManager = new SoundManager();

		mSoundManager.initSounds(getBaseContext());
		mSoundManager.addSound(1, R.raw.gravity1);
		mSoundManager.addSound(2, R.raw.crash);
		mSoundManager.addSound(3, R.raw.click);
		mSoundManager.addSound(4, R.raw.win);
		// mSoundManager.addSound(5, R.raw.powerdown);
		// =================================================//
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		handler = new Handler();
		UserImages.initBitmaps(getResources());

		createWorld();

		pause = false;

		simulationView = new GameEvents(this, world);
		simulationView.setKeepScreenOn(true);
		simulationView.setDims(screenWidth,screenHeight);

		pickloc();

		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
		// Load the bike and add it to the level //
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
		PhysicsFileReader readerTrain = new PhysicsFileReader(getResources()
				.openRawResource(pickedBike));
		GraphicsWorld bike = new GraphicsWorld(World.loadWorld(readerTrain,
				new UserImages()));
		world.addWorld(bike);

		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
		// Find the body for centering and Load motors //
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
		// mSoundManager.playSound(1);
		int bodyCount = world.getBodyCount();
		Body[] bodies = world.getBodies();
		for (int i = 0; i < bodyCount; i++) {
			UserImages userShapedata = (UserImages) (bodies[i].shape()
					.getUserData());
			if (userShapedata == null)
				continue;
			if (userShapedata.type == UserImages.M_TYPE_BIKE) {
				simulationView.setViewBody(bodies[i], true, true);
			}
			if (userShapedata.type == UserImages.M_TYPE_ONKILL) {
				simulationView.setKillBody(bodies[i]);
			}
			// if ( userShapedata.type == UserImages.M_TYPE_WHEEL )
			// {
			// simulationView.setWheelBody(bodies[i]);
			// }
		}
		// -----------Add On bike upside down Event-----------//
		((GameEvents) simulationView).addEvents();
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
		// Load Game layouts //
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

		setContentView(R.layout.rotategame);
		final RelativeLayout gamelayout = (RelativeLayout) findViewById(R.id.GameLayout);
		gamelayout.addView(simulationView, 0);

		final ImageButton up = (ImageButton) findViewById(R.id.arrow);
		final ImageButton none = (ImageButton) findViewById(R.id.nog);
		final ImageButton left = (ImageButton) findViewById(R.id.left);
		final ImageButton right = (ImageButton) findViewById(R.id.right);

		switch (lvlId) {
		case 0:
			up.setVisibility(View.GONE);
			none.setVisibility(View.GONE);
			left.setVisibility(View.GONE);
			right.setVisibility(View.GONE);
			gravitycontrolislocked = true;
			if (bikeloc == 0) {
				Controls();
			}
			break;
		case 1:
			up.setVisibility(View.GONE);
			none.setVisibility(View.GONE);
			left.setVisibility(View.GONE);
			right.setVisibility(View.GONE);
			gravitycontrolislocked = true;
			break;
		case 2:
			gravityinfo();
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;

		}

		if (Level.slidebar == true) {
			RelativeLayout slide = (RelativeLayout) findViewById(R.id.slidebuttons);
			RelativeLayout normal = (RelativeLayout) findViewById(R.id.normalbuttons);
			normal.setVisibility(View.GONE);
			slide.setVisibility(View.VISIBLE);
		} else {
			RelativeLayout slide = (RelativeLayout) findViewById(R.id.slidebuttons);
			RelativeLayout normal = (RelativeLayout) findViewById(R.id.normalbuttons);
			normal.setVisibility(View.VISIBLE);
			slide.setVisibility(View.GONE);
		}

		// ++++++++++++++++++++++++++++++++++++++++++//
		// set menus to not display //
		// ++++++++++++++++++++++++++++++++++++++++++//
		theWinMenu = false;
		death = false;
		// GravityUp = false;
		// ++++++++++++++++++++++++++++++++++++++++++//
		// Initiate Motors and put motors //
		// ++++++++++++++++++++++++++++++++++++++++++//
		world.initMotors();
		world.putmotor();

		forwardwasreleased = true;
		reversewasreleased = true;
		Up = true;
		Down = true;
		Left = true;
		Right = true;
		NoGravity = true;
		poweroff = false;
		power = true;
		tickpassed = true;
		screenDirection = 0;
		win = false;

		tReset = new restarttimer(6000, 1000);
		;

		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
		// Buttons For the menus //
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

		final Typeface sketch = Typeface.createFromAsset(getAssets(),
				"fonts/space.ttf");

		final Button mainmenu = (Button) findViewById(R.id.mainmenu);
		final Button restart = (Button) findViewById(R.id.restart);
		final Button nextlevel = (Button) findViewById(R.id.nextlevel);
		// final ImageButton restart2 = (ImageButton)
		// findViewById(R.id.restart2);
		final ImageButton openmenu = (ImageButton) findViewById(R.id.openmenu);
		final Button back = (Button) findViewById(R.id.back);

		mainmenu.setTypeface(sketch);
		restart.setTypeface(sketch);
		nextlevel.setTypeface(sketch);
		back.setTypeface(sketch);

		// -----------------Go To Level Select-------------//
		mainmenu.setOnClickListener(new View.OnClickListener() {
			public final void onClick(View v) {
				mSoundManager.playSound(3);
				finish();
				// Intent MainMenuIntent = new
				// Intent(DoodleBikeMain.this,Level.class);
				// startActivity(MainMenuIntent);
			}
		});

		// -----------------Reload Game Intent-------------//
		restart.setOnClickListener(new View.OnClickListener() {
			public final void onClick(View v) {
				mSoundManager.playSound(3);
				finish();
				Intent PlayIntent = new Intent(DoodleBikeMain.this, DoodleBikeMain.class);
				startActivity(PlayIntent);
				pause = false;
				tReset.cancel();
			}
		});
		// -----------------Reload Game Intent-------------//
		/*
		 * restart2.setOnClickListener(new View.OnClickListener() { public final
		 * void onClick(View v) { mSoundManager.playSound(3); finish(); Intent
		 * PlayIntent = new Intent(DoodleBikeMain.this,DoodleBikeMain.class);
		 * startActivity(PlayIntent); pause = false;
		 * 
		 * tReset.cancel(); } });
		 */
		// -----------------Open up in game menu-------------//
		openmenu.setOnClickListener(new View.OnClickListener() {
			public final void onClick(View v) {
				mSoundManager.playSound(3);
				pause = true;
				RelativeLayout open = (RelativeLayout) findViewById(R.id.RelativeLayout01);
				back.setVisibility(View.VISIBLE);
				nextlevel.setVisibility(View.INVISIBLE);
				open.setVisibility(View.VISIBLE);
			}
		});
		// -----------------Back to game from menu-------------//
		back.setOnClickListener(new View.OnClickListener() {
			public final void onClick(View v) {
				mSoundManager.playSound(3);
				RelativeLayout flip = (RelativeLayout) findViewById(R.id.RelativeLayout01);
				flip.setVisibility(View.GONE);
				pause = false;
			}
		});
		// -----------------Picks the next level-------------//
		nextlevel.setOnClickListener(new View.OnClickListener() {
			public final void onClick(View v) {
				mSoundManager.playSound(3);
				Intent PlayIntent = new Intent(DoodleBikeMain.this,
						DoodleBikeMain.class);
				switch (lvlId) {
				case 0: finish(); LevelControl.Level1(); startActivity(PlayIntent); break;
				case 1: finish(); LevelControl.Level2(); startActivity(PlayIntent); break;
				case 2: finish(); LevelControl.Level3(); startActivity(PlayIntent); break;
				case 3: finish(); break;
				case 4: finish(); break;
				case 5: finish(); LevelControl.Level6(); startActivity(PlayIntent); break;
				case 6: finish(); LevelControl.Level7(); startActivity(PlayIntent); break;
				case 7: finish(); LevelControl.Level8(); startActivity(PlayIntent); break;
				case 9: Finished(); break;
				// case 10: LevelControl.Level11(); finish();
				// startActivity(MainMenuIntent); break;
				// case 11: LevelControl.Level12(); finish();
				// startActivity(MainMenuIntent); break;
				// case 12: LevelControl.Level13(); finish();
				// startActivity(MainMenuIntent); break;
				// case 13: LevelControl.Level14(); finish();
				// startActivity(MainMenuIntent); break;
				// case 14: LevelControl.Level15(); finish();
				// startActivity(MainMenuIntent); break;
				// case 15: LevelControl.Level16(); finish();
				// startActivity(MainMenuIntent); break;
				// case 16: LevelControl.Level17(); finish();
				// startActivity(MainMenuIntent); break;

				}

			}
		});

		MyCount counter = new MyCount(timelimit, 1000);
		counter.start();

		// tickTimer physicsCounter = new tickTimer(2000,2000);
		// physicsCounter.start();

		mTime = (TextView) findViewById(R.id.TextView06);
		rTime = (TextView) findViewById(R.id.autorestart);
		rTime.setTypeface(sketch);
	}


	private void Finished() {
		finish();
	}

	@Override
	protected void onPause() {
		super.onPause();
		handler.removeCallbacks(update);
		mSensorManager.unregisterListener(world);

	}

	@Override
	protected void onResume() {
		super.onResume();
		handler.post(update);
		if (world != null) {
			mSensorManager.registerListener(world, SensorManager.SENSOR_ACCELEROMETER, SensorManager.SENSOR_DELAY_GAME);
		}

	}

	// --------------------//
	public GraphicsWorld createWorld() {
		PhysicsFileReader reader = new PhysicsFileReader(getResources().openRawResource(pickedWorld));
		world = new GraphicsWorld(World.loadWorld(reader)); // Note you add new UserImages() as a parm in loadWorld
		mSensorManager.registerListener(world, SensorManager.SENSOR_ACCELEROMETER, SensorManager.SENSOR_DELAY_GAME);
		return world;
	}

	private final void UnlockLevel() {
		if (locker == false) {
			locker = true;
			if (lvlId == lvlsComplete){
				lvlsComplete++;
			}
			/*
			switch (lvlId) {
				case 0: if (lvlsComplete == 0) { lvlsComplete = 1; } break;
				case 1: if (lvlsComplete == 1) { lvlsComplete = 2; } break;
				case 2: if (lvlsComplete == 2) { lvlsComplete = 3; } break;
				case 3: if (lvlsComplete == 3) { lvlsComplete = 4; } break;
				case 4: if (lvlsComplete == 4) { lvlsComplete = 5; } break;
				case 5: if (lvlsComplete == 5) { lvlsComplete = 6; } break;
				case 6: if (lvlsComplete == 6) { lvlsComplete = 7; } break;
				case 7: if (lvlsComplete == 7) { lvlsComplete = 8; } break;
				case 8: if (lvlsComplete == 8) { lvlsComplete = 9; } break;
				case 9: if (lvlsComplete == 9) { lvlsComplete = 10; } break;
				case 10: if (lvlsComplete == 10) { lvlsComplete = 11; } break;
				case 11: if (lvlsComplete == 11) { lvlsComplete = 12; } break;
				case 12: if (lvlsComplete == 12) { lvlsComplete = 13; } break;
				case 13: if (lvlsComplete == 13) { lvlsComplete = 14; } break;
				case 14: if (lvlsComplete == 14) { lvlsComplete = 15; } break;
				case 15: if (lvlsComplete == 15) { lvlsComplete = 16; } break;
				case 16: if (lvlsComplete == 16) { lvlsComplete = 17; } break;
				case 17: if (lvlsComplete == 17) { lvlsComplete = 18; } break;
				case 18: if (lvlsComplete == 18) { lvlsComplete = 19; } break;
				case 19: if (lvlsComplete == 19) { lvlsComplete = 20; } break;
				case 20: if (lvlsComplete == 20) { lvlsComplete = 21; } break;
				default: lvlsComplete = 0; break;
			}*/
			// System.out.println("levels complete" +lvlsComplete);
		}
	}

	private final void checkEventsHIGHP() {
		if (poweroff == true) {
			if (power == true) {
				world.takemotor();
				mSoundManager.addSound(5, R.raw.powerdown);
				mSoundManager.playSound(5);
				power = false;
			}
		} else if (DoodleBikeMain.theWinMenu == true) {
			theWinMenu = false;
			i5++;
			if (i5 == 2) {
				win = true;
				final Button nextlevel = (Button) findViewById(R.id.nextlevel);
				final Button back = (Button) findViewById(R.id.back);
				nextlevel.setVisibility(View.VISIBLE);
				back.setVisibility(View.INVISIBLE);
				world.putmotor();
				world.setWheelSpeed(0);
				UnlockLevel();
				SharedPreferences settings = getSharedPreferences("lvlsComplete", 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putInt("lvlsComplete", lvlsComplete);
				editor.commit();
				final RelativeLayout winmenu = (RelativeLayout) findViewById(R.id.RelativeLayout01);
				winmenu.setVisibility(View.VISIBLE);
				blinkstars();
				mSoundManager.playSound(4);
			}
		} else if (DoodleBikeMain.death == true) {
			world.destroyBike();
			if (Level.vibrate == true) {
				Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				v.vibrate(250);
			} else {
				mSoundManager.playSound(2);
			}
			SeeEvents = false;
			DoodleBikeMain.death = false;
			final Button nextlevel = (Button) findViewById(R.id.nextlevel);
			final Button back = (Button) findViewById(R.id.back);
			// final RelativeLayout flip = (RelativeLayout)
			// findViewById(R.id.RelativeLayout01);
			if (win == false) {
				tReset.start();
				nextlevel.setVisibility(View.INVISIBLE);
				rTime.setVisibility(View.VISIBLE);
			}
			back.setVisibility(View.INVISIBLE);
			// flip.setVisibility(View.VISIBLE);
		} else

		if (lvlId == 0) {
			// ----------------------------------
			if (info1 == true) {
				info1();
			} else
			// ------------------------------------
			if (info2 == true) {
				info2();
			} else
			// --------------------
			if (info3 == true) {
				info3();
			}
			// --------------------

		} else if (lvlId == 2) {
			switch (blink) {
			case 1: blinkright(); blink = 0; break;
			case 2: blinkup(); blink = 0; break;
			case 3: blinkleft(); blink = 0; break;
			case 4: blinkdown(); blink = 0; break;
			case 5: blinkmiddle(); blink = 0; break;
			}
		}

	}

	@Override
	public final boolean onTouchEvent(MotionEvent event) {
		final RelativeLayout gamelayout = (RelativeLayout) findViewById(R.id.GameLayout);

		final ImageButton forward = (ImageButton) findViewById(R.id.forwardactive);
		final ImageButton reverse = (ImageButton) findViewById(R.id.reverseactive); // gravity
																					// left
		final ImageButton stop = (ImageButton) findViewById(R.id.stopactive); // gravity
																				// right

		final ImageButton sforward = (ImageButton) findViewById(R.id.forwardactiveS);
		final ImageButton sreverse = (ImageButton) findViewById(R.id.reverseactiveS); // gravity
																						// left
		// final ImageButton sstop = (ImageButton)
		// findViewById(R.id.stopactiveS); // gravity right

		final double screensectionX2 = gamelayout.getWidth() * .4;
		final double screensectionX3 = gamelayout.getWidth() * .6;

		final double slideSectionX5 = gamelayout.getWidth() * 3 / 4;
		final double slideSectionX4 = gamelayout.getWidth() * 1 / 2;

		final double smallslideSectionX5 = gamelayout.getWidth() * 5 / 12;
		final double smallslideSectionX4 = gamelayout.getWidth() * 4 / 12;
		final double smallslideSectionX3 = gamelayout.getWidth() * 3 / 12;
		final double smallslideSectionX2 = gamelayout.getWidth() * 2 / 12;
		final double smallslideSectionX1 = gamelayout.getWidth() * 1 / 12;

		final int halfscreenwidth = gamelayout.getWidth() / 2;
		final int halfscreenheight = gamelayout.getHeight() / 2;

		final ImageButton up = (ImageButton) findViewById(R.id.arrow);
		final ImageButton none = (ImageButton) findViewById(R.id.nog); // no
																		// gravity
		final int boxsize = up.getHeight();
		final int boxsizeX2 = boxsize * 2;
		final int boxsizeX3 = boxsize * 3;

		int pointerCount = event.getPointerCount();

		if (event.getAction() == MotionEvent.ACTION_UP) {
			world.takemotor();
			world.setWheelSpeed(0);
			forwardwasreleased = true;
			reversewasreleased = true;
			stopwasreleased = true;
			forward.setVisibility(View.INVISIBLE);
			reverse.setVisibility(View.INVISIBLE);
			stop.setVisibility(View.INVISIBLE);

			sforward.setVisibility(View.INVISIBLE);
			sreverse.setVisibility(View.INVISIBLE);
			// sstop.setVisibility(View.INVISIBLE);

		} else {

			for (int i = 0; i < pointerCount; i++) {
				int x = (int) event.getX(i);
				int y = (int) event.getY(i);

				if (y > halfscreenheight) {
					// //=======================================================================================================//
					if (Level.slidebar == true) {
						// --------------------------SLIDER BAR IS
						// TRUE-----------------------------------//
						if (x > halfscreenwidth) {
							// ------------------------------
							if (x > slideSectionX5) {
								// ------------------------------------------------------------------
								if (forwardwasreleased == true
										&& reversewasreleased == true) {
									if (stopwasreleased == true) {
										if (poweroff == false) {
											world.putmotor();
											world.setWheelSpeed(WheelSpeed);
											sforward.setVisibility(View.VISIBLE);
											forwardwasreleased = false;
										}
									}
								}
								// ------------------------------------------------------------------
							} else {
								// +++++++++++++++++++++++++++++++
								if (forwardwasreleased == true
										&& reversewasreleased == true) {
									if (stopwasreleased == true) {
										if (poweroff == false) {
											world.putmotor();
											world.setWheelSpeed(-WheelSpeed);
											sreverse.setVisibility(View.VISIBLE);
										}
									}
									reversewasreleased = false;
								}
								// ++++++++++++++++++++++++++++++++

							}

							// -------------------------------
						} else {
							if (x > smallslideSectionX5) {
								// lean forward alot
								world.setBikeTurningPower(5);
								world.setBikeTurningSpeed(4);
							} else if (x > smallslideSectionX4) {
								// lean forward some
								world.setBikeTurningPower(4);
								world.setBikeTurningSpeed(3);
							} else if (x > smallslideSectionX3) {
								// lean forward little
								world.setBikeTurningPower(2);
								world.setBikeTurningSpeed(1);
							} else if (x > smallslideSectionX2) {
								// lean back little
								world.setBikeTurningPower(-2);
								world.setBikeTurningSpeed(-1);
							} else if (x > smallslideSectionX1) {
								// lean back some
								world.setBikeTurningPower(-4);
								world.setBikeTurningSpeed(-3);
							} else {
								// lean back alot
								world.setBikeTurningPower(-5);
								world.setBikeTurningSpeed(-4);
							}

						}
						// --------------------------SLIDER BAR IS
						// TRUE-----------------------------------//
					} else {
						if (x > screensectionX3) {
							// ------------------------------------------------------------------
							if (forwardwasreleased == true
									&& reversewasreleased == true) {
								if (stopwasreleased == true) {
									if (poweroff == false) {
										System.out.println("button down");
										world.putmotor();
										world.setWheelSpeed(WheelSpeed);
										forward.setVisibility(View.VISIBLE);
										forwardwasreleased = false;
									}
								}
							}
							// ------------------------------------------------------------------
						} else
						// -------------
						if (x > screensectionX2) {
							// +++++++++++++++++++++++++++++++
							if (forwardwasreleased == true
									&& reversewasreleased == true) {
								if (stopwasreleased == true) {
									world.putmotor();
									world.setWheelSpeed(0);
									stop.setVisibility(View.VISIBLE);
									stopwasreleased = false;

								}
							}
							// ++++++++++++++++++++++++++++++++
						} else
						// ------------------------------------------------------------------
						if (forwardwasreleased == true
								&& reversewasreleased == true) {
							if (stopwasreleased == true) {
								if (poweroff == false) {
									world.putmotor();
									world.setWheelSpeed(-WheelSpeed);
									reverse.setVisibility(View.VISIBLE);
								}
							}
							reversewasreleased = false;
						}
						// ------------------------------------------------------------------

					}
					// //=======================================================================================================//
				}

				// Gravity Panel
				if (gravitycontrolislocked == false) {
					if (x < boxsize && y > boxsize) { // First Box
						if (y < boxsizeX2) { // Left
							if (tickpassed == true) {

								// -------------
								switch (screenDirection) {
								case 0:
									RealLeft();
									break;
								case 1:
									RealDown();
									break;
								case 2:
									RealRight();
									break;
								case 3:
									RealUp();
									break;
								}
								tickpassed = false;
								tickTimer physicsCounter = new tickTimer(1000,
										1000);
								physicsCounter.start();
								// -------------

							}
						}

					} else
					// Row 2 -------------------------------
					if (x < boxsizeX2 && x > boxsize) { // Second Row
						if (y < boxsize) { // Up arrow
							if (tickpassed == true) {
								// -------------
								switch (screenDirection) {
								case 0:
									RealUp();
									break;
								case 1:
									RealLeft();
									break;
								case 2:
									RealDown();
									break;
								case 3:
									RealRight();
									break;
								}
								tickpassed = false;
								tickTimer physicsCounter = new tickTimer(1000,
										1000);
								physicsCounter.start();
								// -------------

							}
						} else if (y < boxsizeX2) { // Middle. no gravity
							if (tickpassed == true) {
								if (NoGravity == true) {
									none.setBackgroundResource(R.drawable.nogravityactive);
									world.setNewGravity(0, 0);
									mSoundManager.playSound(1);
									NoGravity = false;
									final ImageView middlelight = (ImageView) findViewById(R.id.middlelight);
									middlelight.clearAnimation();
									middlelight.setVisibility(View.GONE);
									tickpassed = false;
									tickTimer physicsCounter = new tickTimer(
											1000, 1000);
									physicsCounter.start();
								} else {
									if (tickpassed == true) {
										RealDown();
										tickpassed = false;
										tickTimer physicsCounter = new tickTimer(
												1000, 1000);
										physicsCounter.start();
									}
								}
							}

						} else if (y < boxsizeX3) { // down arrow
							if (tickpassed == true) {
								// -------------
								switch (screenDirection) {
								case 0:
									RealDown();
									break;
								case 1:
									RealRight();
									break;
								case 2:
									RealUp();
									break;
								case 3:
									RealLeft();
									break;
								}
								tickpassed = false;
								tickTimer physicsCounter = new tickTimer(1000,
										1000);
								physicsCounter.start();
								// -------------
							}
						}
						// Row 2 -------------------------------
					} else

					if (x > boxsizeX2 && x < boxsizeX3) { // Third row
						if (y > boxsize && y < boxsizeX2) { // Right arrow
							if (tickpassed == true) {
								// -------------
								switch (screenDirection) {
								case 0:
									RealRight();
									break;
								case 1:
									RealUp();
									break;
								case 2:
									RealLeft();
									break;
								case 3:
									RealDown();
									break;
								}
								tickpassed = false;
								tickTimer physicsCounter = new tickTimer(1000,
										1000);
								physicsCounter.start();
								// -------------

							}
						}
					}
				} // For gravity control on/off

			}

		}

		return true;

	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// Update World //
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	final private Runnable update = new Runnable() {

		public void run() {

			long start = System.currentTimeMillis();
			simulationView.tickWorld();
			if (SeeEvents == true) {
				checkEventsHIGHP();
			}
			long end = System.currentTimeMillis();
			// System.out.println(end - start);
			handler.postDelayed(update, (long) Math.max(35 - (end - start), 0)); // 35
																					// default
		}
	};

	// ===========================================================================================================//
	// FUNCTIONS //
	// ===========================================================================================================//
	private final void Controls() {
		pause = true;
		RelativeLayout closehelp = (RelativeLayout) findViewById(R.id.RelativeLayout02);
		final Button help = (Button) findViewById(R.id.Button01);
		final Button usebuttons = (Button) findViewById(R.id.pickbuttons);
		final RelativeLayout controls = (RelativeLayout) findViewById(R.id.RelativeLayout06);
		final RelativeLayout controlsacc = (RelativeLayout) findViewById(R.id.RelativeLayout07);

		final Typeface sketch = Typeface.createFromAsset(getAssets(),
				"fonts/space.ttf");
		help.setTypeface(sketch);
		usebuttons.setTypeface(sketch);
		closehelp.setVisibility(View.VISIBLE);
		mSoundManager.playSound(3);

		help.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Level.slidebar = false;
				RelativeLayout slide = (RelativeLayout) findViewById(R.id.slidebuttons);
				RelativeLayout normal = (RelativeLayout) findViewById(R.id.normalbuttons);
				normal.setVisibility(View.VISIBLE);
				slide.setVisibility(View.GONE);
				mSoundManager.playSound(3);
				RelativeLayout closehelp = (RelativeLayout) findViewById(R.id.RelativeLayout02);
				closehelp.setVisibility(View.GONE);
				pause = false;
				// -----------
				controlsacc.setVisibility(View.VISIBLE);
				// -----------
			}
		});

		usebuttons.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Level.slidebar = true;
				RelativeLayout slide = (RelativeLayout) findViewById(R.id.slidebuttons);
				RelativeLayout normal = (RelativeLayout) findViewById(R.id.normalbuttons);
				normal.setVisibility(View.GONE);
				slide.setVisibility(View.VISIBLE);
				mSoundManager.playSound(3);
				RelativeLayout closehelp = (RelativeLayout) findViewById(R.id.RelativeLayout02);
				closehelp.setVisibility(View.GONE);
				pause = false;

				SharedPreferences settings = getSharedPreferences("slidebarSP",
						0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("slidebarSP", Level.slidebar);
				editor.commit();
				// -----------
				controls.setVisibility(View.VISIBLE);
				// -----------
			}
		});

	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public static final void pickloc() {
		if (DoodleBikeMain.lvlId == 0) {
			switch (DoodleBikeMain.bikeloc) {
			case 0: DoodleBikeMain.pickedBike = R.raw.spacebike_01; break;
			case 1: DoodleBikeMain.pickedBike = R.raw.chalkbikeloc2; break;
			case 2: DoodleBikeMain.pickedBike = R.raw.chalkbikeloc3; break;
			case 3: DoodleBikeMain.pickedBike = R.raw.chalkbikeloc4; break;
			default: DoodleBikeMain.pickedBike = R.raw.chalkbike; break;
			}
		} else {
			DoodleBikeMain.pickedBike = R.raw.spacebike_01;
		}

	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	private final void info1() {
		i1++;
		if (i1 == 2) {

			final RelativeLayout controls = (RelativeLayout) findViewById(R.id.RelativeLayout06);
			final RelativeLayout controlsacc = (RelativeLayout) findViewById(R.id.RelativeLayout07);
			controls.setVisibility(View.GONE);
			controlsacc.setVisibility(View.GONE);

			bikeloc = 1;
			pause = true;
			final RelativeLayout tilt = (RelativeLayout) findViewById(R.id.TiltLayout);
			tilt.setVisibility(View.VISIBLE);

			final Button close = (Button) findViewById(R.id.tiltinfo);
			final Typeface sketch = Typeface.createFromAsset(getAssets(),
					"fonts/space.ttf");
			close.setTypeface(sketch);
			close.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					mSoundManager.playSound(3);
					tilt.setVisibility(View.GONE);
					pause = false;
				}
			});
			mSoundManager.playSound(3);
		}
		info1 = false;
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	private final void info2() {
		i2++;
		if (i2 == 2) {
			bikeloc = 2;
			// final Typeface sketch =
			// Typeface.createFromAsset(getAssets(),"fonts/space.ttf");
			final RelativeLayout movingobjects = (RelativeLayout) findViewById(R.id.movingobjects);
			final TextView display = (TextView) findViewById(R.id.TextView04);
			// display.setTypeface(sketch);
			movingobjects.setVisibility(View.VISIBLE);

			mSoundManager.playSound(3);
		}
		info2 = false;
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	private final void info3() {
		i3++;
		if (i3 == 2) {
			bikeloc = 3;
			final TextView display = (TextView) findViewById(R.id.TextView04);
			final RelativeLayout platforms = (RelativeLayout) findViewById(R.id.platforms);
			final RelativeLayout movingobjects = (RelativeLayout) findViewById(R.id.movingobjects);
			movingobjects.setVisibility(View.GONE);
			platforms.setVisibility(View.VISIBLE);
			// final Typeface sketch =
			// Typeface.createFromAsset(getAssets(),"fonts/space.ttf");
			// display.setTypeface(sketch);
			mSoundManager.playSound(3);
		}
		info3 = false;
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	private final void gravityinfo() {
		pause = true;
		// final RelativeLayout panel = (RelativeLayout)
		// findViewById(R.id.RelativeLayout04);
		final RelativeLayout closehelp = (RelativeLayout) findViewById(R.id.gravitypanel);
		final Button help = (Button) findViewById(R.id.Button04);
		final Typeface sketch = Typeface.createFromAsset(getAssets(),
				"fonts/space.ttf");
		help.setTypeface(sketch);

		closehelp.setVisibility(View.VISIBLE);
		// panel.bringToFront();

		help.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mSoundManager.playSound(3);
				RelativeLayout closehelp = (RelativeLayout) findViewById(R.id.gravitypanel);
				closehelp.setVisibility(View.GONE);
				pause = false;
			}
		});
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	private final void clearnAnimations() {
		final ImageView downlight = (ImageView) findViewById(R.id.downlight);
		final ImageView uplight = (ImageView) findViewById(R.id.uplight);
		final ImageView leftlight = (ImageView) findViewById(R.id.leftlight);
		final ImageView rightlight = (ImageView) findViewById(R.id.rightlight);
		final ImageView middlelight = (ImageView) findViewById(R.id.middlelight);
		downlight.clearAnimation();
		leftlight.clearAnimation();
		rightlight.clearAnimation();
		uplight.clearAnimation();
		middlelight.clearAnimation();

		downlight.setVisibility(View.GONE);
		uplight.setVisibility(View.GONE);
		leftlight.setVisibility(View.GONE);
		rightlight.setVisibility(View.GONE);
		middlelight.setVisibility(View.GONE);

	}

	private final void blinkstars() {
		final ImageView star1 = (ImageView) findViewById(R.id.imageView2);
		final ImageView star2 = (ImageView) findViewById(R.id.imageView3);
		final ImageView star3 = (ImageView) findViewById(R.id.imageView4);
		final ImageView star4 = (ImageView) findViewById(R.id.imageView5);
		final ImageView star5 = (ImageView) findViewById(R.id.imageView6);
		final ImageView star6 = (ImageView) findViewById(R.id.imageView7);
		final Animation myFadeInAnimation = AnimationUtils.loadAnimation(this,
				R.anim.starfade_in);
		final Animation myFadeOutAnimation = AnimationUtils.loadAnimation(this,
				R.anim.starfade_out);

		star1.startAnimation(myFadeInAnimation);
		star1.startAnimation(myFadeOutAnimation);
		star2.startAnimation(myFadeInAnimation);
		star2.startAnimation(myFadeOutAnimation);
		star3.startAnimation(myFadeInAnimation);
		star3.startAnimation(myFadeOutAnimation);
		star4.startAnimation(myFadeInAnimation);
		star4.startAnimation(myFadeOutAnimation);
		star5.startAnimation(myFadeInAnimation);
		star5.startAnimation(myFadeOutAnimation);
		star6.startAnimation(myFadeInAnimation);
		star6.startAnimation(myFadeOutAnimation);

		star1.setVisibility(View.VISIBLE);
		star2.setVisibility(View.VISIBLE);
		star3.setVisibility(View.VISIBLE);
		star4.setVisibility(View.VISIBLE);
		star5.setVisibility(View.VISIBLE);
		star6.setVisibility(View.VISIBLE);
	}

	private final void blinkup() {
		i1++;
		if (i1 == 1) {
			clearnAnimations();
			final ImageView uplight = (ImageView) findViewById(R.id.uplight);
			final Animation myFadeInAnimation = AnimationUtils.loadAnimation(
					this, R.anim.fade_in);
			final Animation myFadeOutAnimation = AnimationUtils.loadAnimation(
					this, R.anim.fade_out);
			uplight.startAnimation(myFadeInAnimation);
			uplight.startAnimation(myFadeOutAnimation);
			uplight.setVisibility(View.VISIBLE);
			i4 = 0;
			i2 = 0;
			i3 = 0;
			i5 = 0;
		}

	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	private final void blinkdown() {
		i2++;
		if (i2 == 1) {
			clearnAnimations();
			final ImageView downlight = (ImageView) findViewById(R.id.downlight);
			final Animation myFadeInAnimation = AnimationUtils.loadAnimation(
					this, R.anim.fade_in);
			final Animation myFadeOutAnimation = AnimationUtils.loadAnimation(
					this, R.anim.fade_out);
			downlight.startAnimation(myFadeInAnimation);
			downlight.startAnimation(myFadeOutAnimation);
			downlight.setVisibility(View.VISIBLE);
			i1 = 0;
			i4 = 0;
			i3 = 0;
			i5 = 0;
		}
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	private final void blinkleft() {
		i3++;
		if (i3 == 1) {
			clearnAnimations();
			final ImageView leftlight = (ImageView) findViewById(R.id.leftlight);
			final Animation myFadeInAnimation = AnimationUtils.loadAnimation(
					this, R.anim.fade_in);
			final Animation myFadeOutAnimation = AnimationUtils.loadAnimation(
					this, R.anim.fade_out);
			leftlight.startAnimation(myFadeInAnimation);
			leftlight.startAnimation(myFadeOutAnimation);
			leftlight.setVisibility(View.VISIBLE);
			i1 = 0;
			i2 = 0;
			i4 = 0;
			i5 = 0;
		}
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	private final void blinkright() {
		i4++;
		if (i4 == 1) {
			clearnAnimations();
			final ImageView rightlight = (ImageView) findViewById(R.id.rightlight);
			final Animation myFadeInAnimation = AnimationUtils.loadAnimation(
					this, R.anim.fade_in);
			final Animation myFadeOutAnimation = AnimationUtils.loadAnimation(
					this, R.anim.fade_out);
			rightlight.startAnimation(myFadeInAnimation);
			rightlight.startAnimation(myFadeOutAnimation);
			rightlight.setVisibility(View.VISIBLE);
			i1 = 0;
			i2 = 0;
			i3 = 0;
			i5 = 0;
		}
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	private final void blinkmiddle() {
		i5++;
		if (i5 == 1) {
			clearnAnimations();
			final ImageView middlelight = (ImageView) findViewById(R.id.middlelight);
			final Animation myFadeInAnimation = AnimationUtils.loadAnimation(
					this, R.anim.fade_in);
			final Animation myFadeOutAnimation = AnimationUtils.loadAnimation(
					this, R.anim.fade_out);
			middlelight.startAnimation(myFadeInAnimation);
			middlelight.startAnimation(myFadeOutAnimation);
			middlelight.setVisibility(View.VISIBLE);
			final RelativeLayout ginfo = (RelativeLayout) findViewById(R.id.RelativeLayout03);
			ginfo.setVisibility(View.VISIBLE);

			i1 = 0;
			i2 = 0;
			i3 = 0;
			i4 = 0;
		}
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	// final TextView displaytime = (TextView) findViewById(R.id.TextView06);
	// timer.setText(timeleft);
	/*
	 * @Override public void onBackPressed() { Level.updateStatus(); return; }
	 */
	// ================================================================//
	// Button Delay Timer for Multitouch //
	// ================================================================//
	public class tickTimer extends CountDownTimer {

		public tickTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public final void onFinish() {
			tickpassed = true;
		}

		@Override
		public final void onTick(long millisUntilFinished) {
		}
	}

	// ====================

	public class restarttimer extends CountDownTimer {

		public restarttimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public final void onFinish() {
			finish();
			Intent PlayIntent = new Intent(DoodleBikeMain.this,
					DoodleBikeMain.class);
			startActivity(PlayIntent);
			pause = false;
			mSoundManager.playSound(3);
		}

		@Override
		public final void onTick(long millisUntilFinished) {
			timerestart = (int) (millisUntilFinished / 1000);
			if (timerestart < 4) {
				RelativeLayout flip = (RelativeLayout) findViewById(R.id.RelativeLayout01);
				flip.setVisibility(View.VISIBLE);
			}
			rTime.setText("Auto Restart in: " + timerestart);
		}
	}

	// ================================================================//
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	public class MyCount extends CountDownTimer {

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public final void onFinish() {
			mTime.setText("System Down");
			mTime.setTextColor(Color.rgb(255, 0, 0));
			poweroff = true;
		}

		@Override
		public final void onTick(long millisUntilFinished) {

			batteryleft = (int) ((double) millisUntilFinished / timelimit * 95);
			timeleft = (int) (millisUntilFinished / 1000);
			mTime.setText(" " + timeleft);
			// ++++++++++
			ImageView batteryfill = (ImageView) findViewById(R.id.batteryfill);
			final RelativeLayout gamelayout = (RelativeLayout) findViewById(R.id.GameLayout);
			fullscreenwidth = gamelayout.getWidth();
			batteryfill.setMinimumWidth(batteryleft * (fullscreenwidth / 800));
			batteryfill.setMaxHeight(batteryleft * (fullscreenwidth / 800));

			batteryfill.refreshDrawableState();
			if (BLock == 0) {
				if (batteryleft < 75) {
					mTime.setTextColor(Color.rgb(255, 102, 0));
					batteryfill.setBackgroundResource(R.drawable.batteryorange);
					BLock = 1;
				}
			} else if (BLock == 1) {
				if (batteryleft < 30) {
					batteryfill.setBackgroundResource(R.drawable.batteryred);
					BLock = 2;
				}
			} else if (BLock == 2) {
				if (timeleft < 2) {
					batteryfill.setVisibility(View.GONE);
					BLock = 3;
				}

			}
			// +++++++++

		}

	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	private void RealLeft() {

		world.setNewGravity(-130, 0);
		mSoundManager.playSound(1);
		Up = true;
		Down = true;
		Right = true;
		NoGravity = true;
		Left = true; // <---- false
		final ImageView leftlight = (ImageView) findViewById(R.id.leftlight);
		final ImageButton none = (ImageButton) findViewById(R.id.nog); // no
																		// gravity
		leftlight.clearAnimation();
		leftlight.setVisibility(View.GONE);
		screenDirection = 3;
		none.setBackgroundResource(R.drawable.nogravity);
	}

	// -------------------------------------------------------------------------------------------------------------
	private void RealUp() {
		world.setNewGravity(0, -130);
		mSoundManager.playSound(1);
		Down = true;
		Left = true;
		Right = true;
		NoGravity = true;
		Up = true; // <---- false
		final ImageView uplight = (ImageView) findViewById(R.id.uplight);
		final ImageButton none = (ImageButton) findViewById(R.id.nog); // no
																		// gravity
		uplight.clearAnimation();
		uplight.setVisibility(View.GONE);
		screenDirection = 2;
		none.setBackgroundResource(R.drawable.nogravity);
	}

	// -------------------------------------------------------------------------------------------------------------
	private void RealDown() {
		world.setNewGravity(0, 130);
		mSoundManager.playSound(1);
		Up = true;
		Left = true;
		Right = true;
		NoGravity = true;
		Down = true; // <---- false
		final ImageView downlight = (ImageView) findViewById(R.id.downlight);
		final ImageButton none = (ImageButton) findViewById(R.id.nog); // no
																		// gravity
		downlight.clearAnimation();
		downlight.setVisibility(View.GONE);
		screenDirection = 0;
		none.setBackgroundResource(R.drawable.nogravity);
	}

	// -------------------------------------------------------------------------------------------------------------
	private void RealRight() {
		world.setNewGravity(130, 0);
		mSoundManager.playSound(1);
		Up = true;
		Down = true;
		Left = true;
		NoGravity = true;
		Right = true; // <---- false
		final ImageView rightlight = (ImageView) findViewById(R.id.rightlight);
		final ImageButton none = (ImageButton) findViewById(R.id.nog); // no
																		// gravity
		rightlight.clearAnimation();
		rightlight.setVisibility(View.GONE);
		screenDirection = 1;
		none.setBackgroundResource(R.drawable.nogravity);
	}
	// -------------------------------------------------------------------------------------------------------------

	// -------------------------------------------------------------------------------------------------------------

	// -------------------------------------------------------------------------------------------------------------

	// -------------------------------------------------------------------------------------------------------------

	// -------------------------------------------------------------------------------------------------------------

}
