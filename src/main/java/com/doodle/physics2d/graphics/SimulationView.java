
package com.doodle.physics2d.graphics;

import com.doodle.physics2d.full.spacebike.DoodleBikeMain;
import com.doodle.physics2d.full.spacebike.Level;
import com.doodle.physics2d.full.spacebike.R;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import at.emini.physics2D.Motor;
import at.emini.physics2D.Body;
import at.emini.physics2D.util.FXUtil;


public class SimulationView extends SurfaceView implements SurfaceHolder.Callback, OnTouchListener {
    protected GraphicsWorld world;


    //Loading Level Variables
    int SD = 0;
    public final float FGBRateX = (float) .4;
    public static int canvascolor = Color.rgb(0, 15, 0);

//   public static float BGX;
//   public static float BGY;

//   public static float FGBX;
//   public static float FGBY;

//   public static float FGTX;
//   public static float FGTY;


//    public static Bitmap particle1 = null;
//    public static Bitmap particle2 = null;
//    public static Bitmap particle3 = null;
//    public static Bitmap particle4 = null;
//    public static Bitmap landscapetexture = null;
//    public static Bitmap landscapetexture2 = null;


    public Body viewBody = null;          //view centered around this body
    public Body killBody = null;
    public Body wheelBody = null;
    private boolean useX = false;
    private boolean useY = false;
    private int screenWidth;
    private int screenHeight;

    public SimulationView(Context context, GraphicsWorld world) {
        super(context);
        this.world = world;
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        setOnTouchListener(this);
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    //                             View Center                                 //
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//  
/*       public final void setViewCenter(Body viewBody) {
    		this.viewBody = viewBody;
				}*/
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    public final void setViewBody(Body viewBody, boolean useX, boolean useY) {
        this.viewBody = viewBody;
        this.useX = useX;
        this.useY = useY;
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//    
    public final void setKillBody(Body killBody) {
        this.killBody = killBody;
    }

    public final void setWheelBody(Body wheelBody) {
        this.wheelBody = wheelBody;
    }


    public final int getViewTranslateX() {
        return (useX && viewBody != null) ? ((viewBody.positionFX().xFX >> FXUtil.DECIMAL) - this.screenWidth / 2) : 0; //390
    }

    public final int getViewTranslateY() {                                                                                                                     // * 3  YViewPosition   XViewPosition
        return (int) ((useY && viewBody != null) ? ((viewBody.positionFX().yFX >> FXUtil.DECIMAL) - this.screenHeight / 2) : 0); //time 3
    }

    public final void resetWorld(GraphicsWorld world) {
        this.world = world;
    }

    int count = 0;
    public final void tickWorld() {

        if (world != null) {
            world.tick();
            Canvas c = null;
            SurfaceHolder holder = getHolder();
            try {
                c = holder.lockCanvas(null);
                synchronized (holder) {
                    doDraw(c);
                }
            } finally {
                if (c != null)
                    holder.unlockCanvasAndPost(c);
            }
        }
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    //                                 Canvas                                   //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    public final void doDraw(Canvas canvas) {
        final int lvldetailfinal = Level.lvldetail;
        if (world != null && canvas != null) {
            canvas.drawColor(canvascolor);


            doRotate(canvas);
            final float X1 = -500;
            final float Y1 = -500;
            final float Y2 = 2000;
            final float X2 = 2000;
            final int linestart = (int) (((getViewTranslateX() * .2) + 2000) / 150);
            final int lineend = (linestart - 9);
//-------------------Background Vertical Lines--------------------//
            for (int i = lineend; i < linestart; i++) {
                final float XSec = getViewTranslateX();
                canvas.drawLine((float) (-(XSec * .2)) + (150 * i), Y1, (float) (-(XSec * .2) + (150 * i)), Y2, GraphicsWorld.greenline2);
                canvas.drawLine(-(XSec * FGBRateX) + (300 * i), Y1, -(XSec * FGBRateX) + (300 * i), Y2, GraphicsWorld.greenline);
            }
//-------------------Background Horizontal Lines--------------------//
            int Ylinestart = (int) ((getViewTranslateY() * .2 + 2000) / 150);
            int Ylineend = (Ylinestart - 9);
            for (int i = Ylineend; i < Ylinestart; i++) {
                final float YSec = getViewTranslateY();
                canvas.drawLine(X1, (float) (-(YSec * .2) + (150 * i)), X2, (float) (-(YSec * .2) + (150 * i)), GraphicsWorld.greenline2);
                switch (lvldetailfinal) {
                    case 2:
                        canvas.drawLine(X1, -(YSec * FGBRateX) + (300 * i), X2, -(YSec * FGBRateX) + (300 * i), GraphicsWorld.greenline);
                        break;
                }
            }

            final float xx = getHeight() / 480f;
            final float yy = getWidth() / 800f;
            //   canvas.scale(xx , xx);
            // Need to center on player
            world.draw(canvas, getViewTranslateX(), getViewTranslateY(), this.screenWidth, this.screenHeight);

        }
    }

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
//                                 Canvas                                   //
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++// 
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
//                            Rotate Canvas                                 //
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

    final int rotateStep = 5;
    private void doRotate(Canvas canvas) {
        final int rotateX = (int) (getWidth() * .375);
        final int rotateY = getHeight() / 2;
        //-------------------------------//
        if (DoodleBikeMain.screenDirection == 1) {
            if (SD == 90) {
                canvas.rotate(90, rotateX, rotateY);
            } else {
                if (SD < 90) {
                    SD = SD + rotateStep;
                } else {
                    SD = SD - rotateStep;
                }
                canvas.rotate(SD, rotateX, rotateY);
            }
        } else if (DoodleBikeMain.screenDirection == 2) {
                if (SD == 180) {
                    canvas.rotate(180, rotateX, rotateY);
                } else {
                    if (SD < 180) {
                        SD = SD + rotateStep;
                    } else {
                        SD = SD - rotateStep;
                    }
                    canvas.rotate(SD, rotateX, rotateY);
                }
            } else if (DoodleBikeMain.screenDirection == 3) {
                    if (SD == 270) {
                        canvas.rotate(270, rotateX, rotateY);
                    } else {
                        if (SD < 270 && SD > 100) {
                            SD = SD + rotateStep;
                        } else {
                            if (SD <= 0) {
                                SD = 360;
                            }
                            SD = SD - rotateStep;
                        }
                        canvas.rotate(SD, rotateX, rotateY);
                    }
                } else if (DoodleBikeMain.screenDirection == 0) {
                        if (SD == 0) {

                        } else {
                            if (SD < 360 && SD > 100) {
                                SD = SD + rotateStep;
                                if (SD >= 360) {
                                    SD = 0;
                                }
                            } else {
                                SD = SD - rotateStep;
                            }
                            canvas.rotate(SD, rotateX, rotateY);
                        }
                    }

        if (SD != 0 && SD != 90 && SD != 180 && SD != 270) {
            if (SD < 45 || SD > 315) {
                GraphicsWorld.greenline.setColor(Color.rgb(0, 108, 0));
                GraphicsWorld.greenline2.setColor(Color.rgb(0, 75, 0));
                canvascolor = Color.rgb(0, 15, 0);
            }
            if (SD > 45 && SD < 135) {
                GraphicsWorld.greenline.setColor(Color.rgb(20, 20, 118));
                GraphicsWorld.greenline2.setColor(Color.rgb(10, 10, 95));
                canvascolor = Color.rgb(5, 5, 25);
            }
            if (SD > 135 && SD < 225) {
                GraphicsWorld.greenline.setColor(Color.rgb(118, 5, 5));
                GraphicsWorld.greenline2.setColor(Color.rgb(85, 5, 5));
                canvascolor = Color.rgb(20, 3, 3);
            }
            if (SD > 225 && SD < 315) {
                GraphicsWorld.greenline.setColor(Color.rgb(55, 55, 55));
                GraphicsWorld.greenline2.setColor(Color.rgb(35, 35, 35));
                canvascolor = Color.rgb(10, 10, 10);
            }
            int colorset = SD;
            if (SD > 250) {
                colorset = 255;
            }
        }
    }


//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
//                            Rotate Canvas                                 //
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//   

    public final boolean onTouch(View v, MotionEvent event) {
        int x = (int) (event.getX() + getViewTranslateX());
        int y = (int) (event.getY() + getViewTranslateY());
        Body b = world.findBodyAt(x * FXUtil.ONE_FX, y * FXUtil.ONE_FX);
        if (b != null) {
            return onTouchBody(b, event);
        }
        return false;
    }

    public boolean onTouchBody(Body body, MotionEvent event) {
        return false;
    }

    public void surfaceCreated(SurfaceHolder holder) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public void setDims(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }
}
