package com.doodle.physics2d.full.spacebike;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.doodle.physics2d.graphics.GraphicsWorld;

public class Level extends Activity {

	public static int lvldetail;
	public static boolean vibrate;
	public static boolean slidebar;
	public static int tiltforce;

	private static class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public EfficientAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return lvlselected.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.level2, null);
				holder = new ViewHolder();
				holder.text = (TextView) convertView.findViewById(R.id.TextView01);
				holder.text2 = (TextView) convertView.findViewById(R.id.TextView02);
				holder.text3 = (TextView) convertView.findViewById(R.id.TextView03);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.text.setText(curr[position]);
			holder.text2.setText(lvlselected[position]);
			holder.text3.setText(gravitypanel[position]);
			return convertView;
		}

		static class ViewHolder {
			TextView text;
			TextView text2;
			TextView text3;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.level3);

		SharedPreferences settings = getSharedPreferences("lvlsCompleted", 0);
		DoodleBikeMain.lvlsComplete = settings.getInt("lvlsCompleted", 0);

		SharedPreferences detail = getSharedPreferences("detaillevel", 0);
		lvldetail = detail.getInt("detaillevel", 2);

		SharedPreferences tilting = getSharedPreferences("tilting", 0);
		tiltforce = tilting.getInt("tilting", 0);

		SharedPreferences vibrateonorof = getSharedPreferences("vibrateb", 0);
		vibrate = vibrateonorof.getBoolean("vibrateb", true);

		SharedPreferences slidebarSP = getSharedPreferences("slidebarSP", 0);
		slidebar = slidebarSP.getBoolean("slidebarSP", false);

		ColorDrawable divcolor = new ColorDrawable(Color.rgb(20, 190, 20));

		final ImageView sign = (ImageView) findViewById(R.id.ImageView01);
		final Animation signAnimation = AnimationUtils.loadAnimation(this, R.anim.mainsign);
		sign.startAnimation(signAnimation);

		/*
		 * final ImageButton twitter = (ImageButton) findViewById(R.id.twitter);
		 * twitter.setOnClickListener(new View.OnClickListener() { public void
		 * onClick(View v) { Intent goToTwitter = new
		 * Intent(Intent.ACTION_VIEW,Uri
		 * .parse("http://twitter.com/spacebikegame"));
		 * startActivity(goToTwitter); } });
		 */

		final Button fullversion = (Button) findViewById(R.id.button1);
		fullversion.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("market://details?id=com.doodle.physics2d.paid.spacebike"));
				startActivity(intent);
			}
		});

		// ++++++++++++++++
		final Button settingsclose = (Button) findViewById(R.id.Button05);
		settingsclose.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final RelativeLayout settings = (RelativeLayout) findViewById(R.id.RelativeLayout01);
				settings.setVisibility(View.GONE);
			}
		});

		// ++++++++++++++
		final Button detailbutton = (Button) findViewById(R.id.Button01);
		switch (lvldetail) {
		case 0: detailbutton.setText("Low"); break;
		case 1: detailbutton.setText("Medium"); break;
		case 2: detailbutton.setText("High"); break;
		}

		detailbutton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				switch (lvldetail) {
				case 0: lvldetail = 1; detailbutton.setText("Medium"); break;
				case 1: lvldetail = 2; detailbutton.setText("High"); break;
				case 2: lvldetail = 0; detailbutton.setText("Low"); break;
				}
				SharedPreferences settings = getSharedPreferences("detaillevel", 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putInt("detaillevel", lvldetail);
				editor.commit();

			}
		});

		// ////////////////////////////////////////////////////
		// final Button openemail = (Button) findViewById(R.id.Button06);
		// openemail.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {

		// Intent emailintent = new Intent(Intent.ACTION_SENDTO);

		// emailintent .putExtra(android.content.Intent.EXTRA_EMAIL, new
		// String[]{"brianpurgert@aol.com"});
		// emailintent .putExtra(android.content.Intent.EXTRA_SUBJECT,
		// "Space Bike Beta");

		// emailintent .putExtra(android.content.Intent.EXTRA_TEXT,
		// "Phone Model:         ");

		// startActivity(emailintent);
		/*
		 * Intent EmailIntent = new Intent(Intent.ACTION_SEND);
		 * startActivity(EmailIntent);
		 */

		// }
		// });

		// ////////////////////////////////////////////////////
		final Button tiltingbutton = (Button) findViewById(R.id.Button03);

		switch (tiltforce) {
		case 0: tiltingbutton.setText("Slow"); break;
		case 1: tiltingbutton.setText("Normal"); break;
		case 2: tiltingbutton.setText("Fast"); break;
		}

		tiltingbutton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				switch (tiltforce) {
				case 0:tiltforce = 1;tiltingbutton.setText("Normal");break;
				case 1: tiltforce = 2; tiltingbutton.setText("Fast");break;
				case 2: tiltforce = 0; tiltingbutton.setText("Slow"); break;
				}

				SharedPreferences settings = getSharedPreferences("tilting", 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putInt("tilting", tiltforce);
				editor.commit();

			}
		});
		// ////////////////////////////////////////////////////
		final Button vibratebutton = (Button) findViewById(R.id.Button02);
		if (vibrate == true) {
			vibratebutton.setText("Vibrate");
		}
		if (vibrate == false) {
			vibratebutton.setText("Sound");
		}
		vibratebutton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (vibrate == false) {
					vibrate = true;
					vibratebutton.setText("Vibrate");
				} else {
					vibrate = false;
					vibratebutton.setText("Sound");
				}

				SharedPreferences settings = getSharedPreferences("vibrateb", 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("vibrateb", vibrate);
				editor.commit();

			}
		});
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++
		final Button slidebarbutton = (Button) findViewById(R.id.Button04);
		if (slidebar == true) {
			slidebarbutton.setText("Slider Bar");
		}
		if (slidebar == false) {
			slidebarbutton.setText("Normal");
		}
		slidebarbutton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (slidebar == false) {
					slidebar = true;
					slidebarbutton.setText("Slider Bar");
				} else {
					slidebar = false;
					slidebarbutton.setText("Normal");
				}

				SharedPreferences settings = getSharedPreferences("slidebarSP",
						0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("slidebarSP", slidebar);
				editor.commit();

			}
		});

		/*
		 * LayoutInflater inflater = (LayoutInflater)
		 * this.getSystemService(Context.LAYOUT_INFLATER_SERVICE); PopupWindow
		 * pw = new PopupWindow( inflater.inflate(R.layout.popup, null, false),
		 * 100, 100, true); // The code below assumes that the root container
		 * has an id called 'main'
		 * pw.showAtLocation(this.findViewById(R.id.LinearLayout),
		 * Gravity.CENTER, 0, 0);
		 */

		ListView l1 = (ListView) findViewById(R.id.ListView01);
		l1.setAdapter(new EfficientAdapter(this));

		l1.setDivider(divcolor);
		l1.setDividerHeight(2);

		l1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 > DoodleBikeMain.lvlsComplete) {
					Toast.makeText(getBaseContext(), lvlselected[arg2] + " is locked", Toast.LENGTH_LONG).show();
				} else {
					LevelControl.level(arg2);
					DoodleBikeMain.bikeloc = 0;
					DoodleBikeMain.WheelSpeed = 8; // 20

					GraphicsWorld.frontWheel = true;

					Intent PlayIntent = new Intent(Level.this, DoodleBikeMain.class);
					startActivity(PlayIntent);
				}
			}

		});
		// +=============================================================================
		switch (DoodleBikeMain.lvlsComplete) {
		case 0:
			curr[0] = "Unlocked";
			break; // level 1 says unlocked
		case 1:
			curr[1] = "Unlocked";
			curr[0] = "Complete";
			break; // level 2 says unlocked
		case 2:
			curr[2] = "Unlocked";
			curr[1] = "Complete";
			curr[0] = "Complete";
			break; // level 3 says unlocked
		case 3:
			curr[3] = "Unlocked";
			curr[2] = "Complete";
			curr[1] = "Complete";
			curr[0] = "Complete";
			break; // level 4
		case 4:
			curr[4] = "Complete";
			curr[3] = "Complete";
			curr[2] = "Complete";
			curr[1] = "Complete";
			curr[0] = "Complete";
			break; // level 5
		}

		// ==============================================================================
	}

	private static final String[] lvlselected = { "Level 1", "Level 2", "Level 3", "Level 4" };

	private static String[] curr = { "Locked", "Locked", "Locked", "Locked" };

	private static String[] gravitypanel = { "Disabled", "Disabled",
			"Disabled", "Disabled" };

	// lv1=(ListView)findViewById(R.id.ListView01);
	// By using setAdpater method in listview we an add string array in list.
	// lv1.setAdapter(new
	// ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 , lv_arr));

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// Load Buttons //
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		final RelativeLayout settings = (RelativeLayout) findViewById(R.id.RelativeLayout01);
		settings.setVisibility(View.VISIBLE);
		return true;
	}

	protected void onResume() {
		super.onResume();
		SharedPreferences settings = getSharedPreferences("lvlsComplete", 0);
		DoodleBikeMain.lvlsComplete = settings.getInt("lvlsComplete", 0);
		switch (DoodleBikeMain.lvlsComplete) {
		case 0:
			curr[0] = "Unlocked";
			break; // level 1 says unlocked
		case 1:
			curr[1] = "Unlocked";
			curr[0] = "Complete";
			break; // level 2 says unlocked
		case 2:
			curr[2] = "Unlocked";
			curr[1] = "Complete";
			curr[0] = "Complete";
			break; // level 3 says unlocked
		case 3:
			curr[3] = "Unlocked";
			curr[2] = "Complete";
			curr[1] = "Complete";
			curr[0] = "Complete";
			break; // level 4
		case 4:
			curr[4] = "Complete";
			curr[3] = "Complete";
			curr[2] = "Complete";
			curr[1] = "Complete";
			curr[0] = "Complete";
			break; // level 5

		}
	}

}
