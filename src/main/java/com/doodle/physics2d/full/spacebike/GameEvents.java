
package com.doodle.physics2d.full.spacebike;

import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import at.emini.physics2D.Body;
import at.emini.physics2D.Contact;
import at.emini.physics2D.Event;
import at.emini.physics2D.PhysicsEventListener;
import at.emini.physics2D.World;

import android.app.Activity;
import android.view.View.OnTouchListener;

import com.doodle.physics2d.graphics.GraphicsWorld;
import com.doodle.physics2d.graphics.SimulationView;
import com.doodle.physics2d.graphics.UserImages;


public class GameEvents extends SimulationView implements PhysicsEventListener {
	//	DoodleBikeMain game;
	public GameEvents(Context context, GraphicsWorld world) {
		super(context, world);
		world.setPhysicsEventListener(this);
	}


	public final void addEvents() {
		Event event = Event.createBodyEvent(killBody, null, Event.TYPE_BODY_COLLISION, 0, 0, 1, 0);
		//     Event wheel = Event.createBodyEvent(wheelBody, null, Event.TYPE_BODY_COLLISION, 0, 0, 1, 0);

		world.addEvent(event);
		//     world.addEvent(wheel);
	}

	public final void eventTriggered(Event event, Object object) {

		System.out.println("Event occurred: " + event.type() + " -- " + event.getIdentifier() + " -- " + object);

		if (event.getIdentifier() == 0) {
			DoodleBikeMain.theWinMenu = true;
		} else if (DoodleBikeMain.lvlId == 0) {
			if (event.getIdentifier() == 1) {
				DoodleBikeMain.info1 = true;
			} else if (event.getIdentifier() == 2) {
				DoodleBikeMain.info2 = true;
			} else if (event.getIdentifier() == 3) {
				DoodleBikeMain.info3 = true;
			} else if (event.getIdentifier() >= 4) {
				DoodleBikeMain.death = true;
			}
		} else if (DoodleBikeMain.lvlId == 2) {
			switch (event.getIdentifier()) {
				case 1: DoodleBikeMain.blink = 1; break;
				case 2: DoodleBikeMain.blink = 1; break;
				case 3: DoodleBikeMain.blink = 1; break;
				case 4: DoodleBikeMain.blink = 1; break;
				case 5: DoodleBikeMain.blink = 5; break;
				case 6: DoodleBikeMain.blink = 5; break;
				case 7: DoodleBikeMain.blink = 1; break;
				case 8: DoodleBikeMain.death = true; break;
				case 9: DoodleBikeMain.death = true; break;
			}

		} else if (event.getIdentifier() >= 1) {
			DoodleBikeMain.death = true;
		}


	}
}
