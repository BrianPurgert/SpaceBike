// Author Brian A. Purgert
// Physics by Alexander Adensamer

package com.doodle.physics2d.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.VectorDrawable;
import android.hardware.SensorListener;
import android.hardware.SensorManager;

import at.emini.physics2D.Body;
import at.emini.physics2D.Constraint;
import at.emini.physics2D.Event;
import at.emini.physics2D.Landscape;
import at.emini.physics2D.Motor;
import at.emini.physics2D.Shape;
import at.emini.physics2D.Spring;
import at.emini.physics2D.UserData;
import at.emini.physics2D.World;
import at.emini.physics2D.util.FXUtil;
import at.emini.physics2D.util.FXVector;

import com.doodle.physics2d.full.spacebike.DoodleBikeMain;
import com.doodle.physics2d.full.spacebike.Level;
import com.doodle.physics2d.full.spacebike.R;


@SuppressWarnings("deprecation")
public class GraphicsWorld extends World implements SensorListener {
	public static  boolean frontWheel; // Enable front wheel motor

    private FXVector p1 = new FXVector();
    private FXVector p2 = new FXVector();

  //--------------Turning Body--------------//
    private final int mBikeMotorForceFX = -415 * FXUtil.ONE_FX;//was -415
    final private long mBikeSpeedFactorFX = -5000 * FXUtil.ONE_FX;
    public  Motor mBikeMotor;  
    public  Event crash;
    public  static  Body crashBody; 
    private final int baseGravityFX = 130 * FXUtil.ONE_FX;
  //--------------wheels--------------//
    private final int mWheelMotorForceFX = 200000 * FXUtil.ONE_FX;
    private final long mWheelSpeedFactorFX = 10000 * FXUtil.ONE_FX;
    
  //  public static int landscapeWidth;
  //  public static int lColor1;
  //  public static int lColor2;
  //  public static final int lColor3;
    
    private Motor mWheelMotor1;
    private Motor mWheelMotor2;
    
 //  public static final BitmapShader pencil  = new BitmapShader(SimulationView.back, TileMode .REPEAT , TileMode .REPEAT );
    public static final Paint renderBodies = new Paint();
    public static final Paint renderBodies2 = new Paint();
    public static final Paint renderBodies3 = new Paint();
    public static final Paint renderStaticBodies = new Paint();
    public static final Paint renderSpring = new Paint();
    public static final Paint renderLandscape2 = new Paint();
    public static final Paint renderLandscape3 = new Paint();
    public static final Paint renderLandscape4 = new Paint();   
    public static final Paint greenline = new Paint();
    public static final Paint greenline2 = new Paint();   
    public static final Paint lowline = new Paint();
    public static final Paint lowobj = new Paint();
    public static final Paint renderCircle1 = new Paint();
    public static final Paint renderCircle2 = new Paint();


    static {



        greenline.setColor(Color.rgb(0, 108, 0));               //Background Lines
        greenline2.setColor(Color.rgb(0, 75, 0));
            greenline.setStrokeWidth(2);
        renderCircle1.setColor(Color.rgb(238, 118, 0));         //Circle Objects
        renderCircle2.setColor(Color.rgb(238, 118, 0));
            renderCircle1.setStrokeWidth(3);
            renderCircle1.setStyle(Style.STROKE);
            renderCircle2.setAlpha(130);
        renderBodies2.setColor(Color.rgb(130, 47, 42));         //Rectangle Objects
        renderBodies3.setColor(Color.rgb(184, 136, 133));
        lowobj.setColor(Color.rgb(157, 32, 32));
            renderBodies.setStrokeWidth(7);
            renderBodies.setStrokeJoin(Paint.Join.ROUND);  //bevel miter round
            renderBodies.setStrokeCap(Paint.Cap.ROUND);
            renderBodies.setAlpha(180);
            renderBodies2.setStrokeWidth(10);
            renderBodies2.setStrokeJoin(Paint.Join.ROUND);  //bevel miter round
            renderBodies2.setStrokeCap(Paint.Cap.ROUND);
            renderBodies3.setStrokeWidth(3);
            lowobj.setStrokeWidth(5);
            lowobj.setStyle(Style.FILL_AND_STROKE);
            lowobj.setStrokeCap(Paint.Cap.ROUND);              // but round square
            lowobj.setAlpha(180);
        renderSpring.setColor(Color.rgb(255, 140, 0));          //Springs
            renderSpring.setStrokeWidth(4);
            renderSpring.setAntiAlias(true);
            renderSpring.setStrokeCap(Paint.Cap.ROUND);
        renderLandscape2.setColor(Color.rgb(14, 80, 109));      //Landscape Lines
        renderLandscape3.setColor(Color.rgb(144, 175, 188));
        renderLandscape4.setColor(Color.rgb(235, 241, 243));
        lowline.setColor(Color.rgb(61, 139, 255));
            renderLandscape2.setStrokeWidth(17);
            renderLandscape2.setStrokeJoin(Paint.Join.ROUND);  //Options: bevel miter round
            renderLandscape2.setStrokeCap(Paint.Cap.ROUND);    //Options: but round square
            renderLandscape3.setStrokeWidth(9);
            renderLandscape3.setStrokeCap(Paint.Cap.ROUND);
            renderLandscape4.setStrokeWidth(3);
            lowline.setStrokeWidth(11);
            lowline.setStyle(Style.FILL_AND_STROKE);
            lowline.setStrokeCap(Paint.Cap.ROUND);
            lowline.setAlpha(180);
    }
    

    public GraphicsWorld() {
        super();
    }
    
    public GraphicsWorld(int baseGravityFX) {
        super();
    }

    public GraphicsWorld(World world) {
        super(world);    
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    //                    Initiate Motors                        //
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    public final void initMotors() {
    	int bodyCount = getBodyCount();
        Body[] bodies = getBodies();
        crashBody = bodies[2];
        for( int i = 0; i < bodyCount; i++) {
            UserImages userShapedata = (UserImages) (bodies[i].shape().getUserData());
            if(userShapedata == null) {
                continue; //If the current body in the Array does not have an Image, Go to next body
            }

             if ( userShapedata.type == UserImages.M_TYPE_BIKE ) {
                 // The Motor used for "Leaning" the bike back and forth
                 mBikeMotor = new Motor(bodies[i], 0, mBikeMotorForceFX);
                 addConstraint(mBikeMotor);
                 mBikeMotor.setParameter(0, 0, true, false,false);//(int targetAFX,*/ int targetBFX, boolean rotate, boolean isRelative, boolean isOrthogonal)

            } else if (userShapedata.type == UserImages.M_TYPE_WHEEL ) {
                 // The Actual Motor used for driving the bike forward
                 Motor wheelMotor = new Motor(bodies[i], 0, mWheelMotorForceFX);
                    if (mWheelMotor1 == null) {
                        mWheelMotor1 = wheelMotor;
                    } else if (mWheelMotor2 == null) {
                        mWheelMotor2 = wheelMotor;
                    }
            }
        }
    }
    

    public final void translate(FXVector translation) {
        int bodyCount = getBodyCount();
        Body[] bodies = getBodies();
        for( int i = 0; i < bodyCount; i++) {
            bodies[i].positionFX().add( translation);
        }
    }

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++//   
//            Draw Canvas in Camera Bounds                 //
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    public final void draw(Canvas canvas, int xTranslate, int yTranslate,int screenWidth, int screenHeight) {
    	final int lvldetailfinal = Level.lvldetail;
        final boolean skipOptimize = false;
        canvas.translate(-xTranslate, -yTranslate);

       /* This is used for optimization purposes it checks to to see if the line segments are are inside
       of the camera and if they are, they will be drawn to the canvas and only then */
        /* it does not fucking work ATM, not displaying half of the shit */
//------------------ Render Landscape ------------------//
        final  int leftscreen = xTranslate - screenHeight; //-100
        final int rightscreen = xTranslate + screenHeight; //+1000
        final  int upscreen = yTranslate - screenWidth*2;
        final  int downscreen = yTranslate + screenWidth;

            Landscape landscape = getLandscape();
            FXVector startPoint, endPoint;
            for( int i = 0; i < landscape.segmentCount(); i++) {
                startPoint = landscape.startPoint(i);
                endPoint = landscape.endPoint(i);                
                final int XStart =   startPoint.xAsInt();  
                final int YStart =   startPoint.yAsInt();            
                final int XEnd =   endPoint.xAsInt();  
                final int YEnd =   endPoint.yAsInt();
                if((XStart > leftscreen || XEnd > leftscreen && XStart < rightscreen || XEnd < rightscreen && YStart > upscreen || YEnd > upscreen && YStart < downscreen || YEnd < downscreen) || skipOptimize ){

                                canvas.drawLine(startPoint.xFX >> FXUtil.DECIMAL, startPoint.yFX >> FXUtil.DECIMAL, endPoint.xFX >> FXUtil.DECIMAL, endPoint.yFX >> FXUtil.DECIMAL, lowline);
                                canvas.drawLine(startPoint.xFX >> FXUtil.DECIMAL, startPoint.yFX >> FXUtil.DECIMAL, endPoint.xFX >> FXUtil.DECIMAL, endPoint.yFX >> FXUtil.DECIMAL, renderLandscape2);
                                canvas.drawLine(startPoint.xFX >> FXUtil.DECIMAL, startPoint.yFX >> FXUtil.DECIMAL, endPoint.xFX >> FXUtil.DECIMAL, endPoint.yFX >> FXUtil.DECIMAL, renderLandscape2);

                    canvas.drawLine(startPoint.xFX >> FXUtil.DECIMAL, startPoint.yFX >> FXUtil.DECIMAL, endPoint.xFX >> FXUtil.DECIMAL, endPoint.yFX >> FXUtil.DECIMAL, renderLandscape3);
                    canvas.drawLine(startPoint.xFX >> FXUtil.DECIMAL, startPoint.yFX >> FXUtil.DECIMAL, endPoint.xFX >> FXUtil.DECIMAL, endPoint.yFX >> FXUtil.DECIMAL, renderLandscape3);

                    canvas.drawLine(startPoint.xFX >> FXUtil.DECIMAL, startPoint.yFX >> FXUtil.DECIMAL, endPoint.xFX >> FXUtil.DECIMAL, endPoint.yFX >> FXUtil.DECIMAL, renderLandscape4);
                			}
             }

//+++++++++++++++++++++++++++++++++++++++++++++++++//
//                  Render Springs                  //        
//+++++++++++++++++++++++++++++++++++++++++++++++++//        
        int constraintCount = getConstraintCount();
        Constraint[] constraints = getConstraints();
        
        for(int i = 0; i < constraintCount; i++) {
            if (constraints[i] instanceof Spring) {
                Spring spring = (Spring) constraints[i];
                spring.getPoint1(p1);
                spring.getPoint2(p2);
                canvas.drawLine(p1.xFX >> FXUtil.DECIMAL, p1.yFX >> FXUtil.DECIMAL, p2.xFX >> FXUtil.DECIMAL, p2.yFX >> FXUtil.DECIMAL, renderSpring);
            }
        }
        Body[] bodies = getBodies();
        int bodyEndIndex = getBodyEndIndex();        
        for(int i = getBodyStartIndex(); i < bodyEndIndex; i++) {
        	drawBody( canvas, bodies[i], xTranslate, yTranslate);
        }
        canvas.translate(xTranslate, yTranslate);
    }

//+++++++++++++++++++++++++++++++++++++++++++++++++++++//
//                   Render Bodies                     //
//+++++++++++++++++++++++++++++++++++++++++++++++++++++//
    private Matrix matrix = new Matrix();    
    public final void drawBody(Canvas canvas, Body body, int xTranslate, int yTranslate) {
        // Checks if Body is within bounds
        if ( body.getAABBMinXFX() > (900 + xTranslate ) * FXUtil.ONE_FX || body.getAABBMaxXFX() < xTranslate * FXUtil.ONE_FX ||
             body.getAABBMinYFX() > (900 + yTranslate ) * FXUtil.ONE_FX || body.getAABBMaxYFX() < yTranslate * FXUtil.ONE_FX ) {
        //    return;
        }
        
       
        Shape shape = body.shape();        
        Bitmap image = null;
        
        UserData userdata = shape.getUserData();
        if (userdata != null && userdata instanceof UserImages) {
            image = ((UserImages) userdata).getImage();
        }
        
        if (image != null) {
            //draw the image if one was found
            FXVector position = body.positionFX();
            matrix.reset();
            matrix.postRotate( ((float) body.rotation2FX() * 360f / FXUtil.TWO_PI_2FX), image.getWidth() / 2, image.getHeight() / 2);
            matrix.postTranslate(
                    (position.xFX >> FXUtil.DECIMAL) - image.getWidth() / 2,
                    (position.yFX >> FXUtil.DECIMAL) - image.getHeight() / 2);
            Paint paint = renderBodies;
            if(! body.isDynamic()) {
                paint = renderStaticBodies;
            }  
            canvas.drawBitmap(image, matrix, paint);
        } else {
            FXVector[] positions = body.getVertices();
            if (positions.length == 1) {
              /*  canvas.drawCircle(
                        body.positionFX().xFX >> FXUtil.DECIMAL, 
                        body.positionFX().yFX >> FXUtil.DECIMAL, 
                        body.shape().getBoundingRadiusFX()-(body.shape().getBoundingRadiusFX()/4) >> FXUtil.DECIMAL, 
                        renderCircle1);  */
                canvas.drawCircle(body.positionFX().xFX >> FXUtil.DECIMAL, body.positionFX().yFX >> FXUtil.DECIMAL, body.shape().getBoundingRadiusFX() >> FXUtil.DECIMAL, renderCircle2);
                canvas.drawCircle(body.positionFX().xFX >> FXUtil.DECIMAL, body.positionFX().yFX >> FXUtil.DECIMAL, body.shape().getBoundingRadiusFX() >> FXUtil.DECIMAL, renderCircle1);
            } else {
                for (int j = positions.length - 1, i = 0; i < positions.length; j = i, i++) {
                    canvas.drawLine(positions[i].xFX >> FXUtil.DECIMAL, positions[i].yFX >> FXUtil.DECIMAL, positions[j].xFX >> FXUtil.DECIMAL, positions[j].yFX >> FXUtil.DECIMAL, lowobj);
                    canvas.drawLine(positions[i].xFX >> FXUtil.DECIMAL, positions[i].yFX >> FXUtil.DECIMAL, positions[j].xFX >> FXUtil.DECIMAL, positions[j].yFX >> FXUtil.DECIMAL, renderBodies2);
                    canvas.drawLine(positions[i].xFX >> FXUtil.DECIMAL, positions[i].yFX >> FXUtil.DECIMAL, positions[j].xFX >> FXUtil.DECIMAL, positions[j].yFX >> FXUtil.DECIMAL, renderBodies3);
                    canvas.drawLine(positions[i].xFX >> FXUtil.DECIMAL, positions[i].yFX >> FXUtil.DECIMAL, positions[j].xFX >> FXUtil.DECIMAL, positions[j].yFX >> FXUtil.DECIMAL, renderBodies2);
                    canvas.drawLine(positions[i].xFX >> FXUtil.DECIMAL, positions[i].yFX >> FXUtil.DECIMAL, positions[j].xFX >> FXUtil.DECIMAL, positions[j].yFX >> FXUtil.DECIMAL, renderBodies3);
                }
            }
        }
    }
   
    public final void tick(){
    if(DoodleBikeMain.pause == false){	
        super.tick();
    }}
      
    
/*    public final boolean isDrawLandscape(){
       return drawLandscape;
   }*/

/*   public final void setDrawLandscape(boolean drawLandscape){
       this.drawLandscape = drawLandscape;
    }
*/
    public final void setNewGravity(int newGravityX, int newGravityY) {
        FXVector gravityRaw = new FXVector( newGravityX,newGravityY);
    	gravityRaw.normalize();
    	gravityRaw.multFX(baseGravityFX);
    	setGravity(gravityRaw );   
    	
    }
    
    public final void onAccuracyChanged(int sensor, int accuracy){
    }
  //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
  //               Add Bike and create the motor attached to it                  //
  //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//  

    float xvalue;
	private static  int sensitivity;
	private int max;
	final int tilt = Level.tiltforce;
	
    public final void onSensorChanged(int sensor, float[] values){
    	 xvalue = values[0];
          switch(tilt){
              case 0: 	max = 10 - sensitivity;     sensitivity = 5;   break;
              case 1:  	max = 12 - sensitivity; 	sensitivity = 7;   break;
              case 2:   max = 15 - sensitivity;     sensitivity = 11;   break;
        }
 
    switch(sensor) {
      case SensorManager.SENSOR_ACCELEROMETER:

    //-----Set The Maximum tilting force allowed-----//
    //---------and set the power of the tilt---------// 
    	
 		//--------------------------------
 			 if(Level.slidebar == false){
               if (xvalue < max && xvalue > - max ){
                                setBikeTurningPower(xvalue);
                                setBikeTurningSpeed(xvalue);
                } else if (xvalue > max){
                    setBikeTurningPower(max);
                    setBikeTurningSpeed(max);

                }else if (xvalue < -max){
                    setBikeTurningPower(-max);
                    setBikeTurningSpeed(-max);
                }
 		  }
      //---------------------------------
            break;
        default:break;
        }  
    }
    
    public final void setBikeTurningPower(float value) {
        if (mBikeMotor != null) {
            int maxForceFX = (int) (value * mBikeMotorForceFX *  sensitivity);            
            mBikeMotor.setMaxForceFX(maxForceFX);
        }
    }

    public final void setBikeTurningSpeed(float value) {
        if (mBikeMotor != null) {
            int targetAFX = (int) (value * mBikeSpeedFactorFX *  sensitivity);
            mBikeMotor.setParameter(targetAFX, 0, true, false,false);//,false);
        }
    }

    public final void setWheelSpeed(float value) {
        if (mWheelMotor1 != null && mWheelMotor2 != null){
            int targetAFX = -(int) (value * mWheelSpeedFactorFX);
            mWheelMotor1.setParameter(targetAFX, 0, true, false,false);//,false);
            mWheelMotor2.setParameter(targetAFX, 0, true, false,false);//,false);
        }
    }
   
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
//                       Wheel On/Off                        //
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//    
    
    public final void destroyBike(){
    	  int constraintCount = getConstraintCount();
          Constraint[] constraints = getConstraints();
          
          for(int i = constraintCount - 1; i >= 0; i--) {
              removeConstraint(constraints[i]);
          }                 
      putmotor();
      setWheelSpeed(10);
    }
 
    
    public final void putmotor(){
    	if(frontWheel == true){
    	addConstraint(mWheelMotor1);     // Front Wheel
    	    }
    	addConstraint(mWheelMotor2);     // Rear Wheel
    }
    public final void takemotor(){
    	removeConstraint(mWheelMotor1);    // Front Wheel
    	removeConstraint(mWheelMotor2);    // Rear Wheel
    }	

/*  // Not sure what this function does
        private static int width;
        private static int height;
        private float scale = 1.0f;
	 public static void drawCrosshairsAndText(int x, int y, Paint paint, Canvas c) {
		 c.drawLine(0, y, width, y, paint);
		 c.drawLine(x, 0, x, height, paint);
		 int textY = (int)((15 + 20 * ptr) * scale);
		 c.drawText("x" + ptr + "=" + x, 10 * scale, textY, textPaint);
		 c.drawText("y" + ptr + "=" + y, 70 * scale, textY, textPaint);
		 c.drawText("id" + ptr + "=" + id, width - 55 * scale, textY, textPaint);
	 }*/

    
    
    	
    }
    
    
    
    
    

