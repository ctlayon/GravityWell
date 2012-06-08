package com.ctlayon.gravitywell;

import java.util.Iterator;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.RotationParticleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

import android.util.Log;

public class GameScene extends Scene implements IOnSceneTouchListener {
	
	//===PUBLIC VARIABLES===//	
	public GravityWell well;
	public Ball mBall;
	
	Camera mCamera;
	BaseActivity activity;
	
	//===CONSTRUCTOR===/	
	public GameScene() {
		
		setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		attachChild(new BrickLayer(24));
		
		activity = BaseActivity.getSharedInstance();
		mCamera = activity.mCamera;
		
		well = GravityWell.getSharedInstance();
		attachChild(well);		
		
		mBall = new Ball(2,2,200,300,activity.mBall,activity.getVertexBufferObjectManager());
		this.attachChild(mBall);	
		
		activity.setCurrentScene(this);
		setOnSceneTouchListener(this);
		
		registerUpdateHandler(new GameLoopUpdateHandler());
	}

	//===IMPLEMENTED INTERFACE===//
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		well.move(pSceneTouchEvent.getX());
	    return true;
	}
	
	//===PUBLIC FUNCTIONS===//
	public void moveBall() {
		mBall.move(well, mCamera);
	}
	
	public void cleaner() {
	    synchronized (this) {	    	
	    	// if all Bricks are Hit	    	
	    	if (BrickLayer.isEmpty()) {
	    		this.detach();
	    	    activity.setCurrentScene(new GameScene());
	    	}

	    	Iterator<Brick> bIt = BrickLayer.getIterator();
	    	while(bIt.hasNext()){
	    		Brick b = bIt.next();
	            if( mBall.collidesWith(b.sprite)) {
                    Log.v("GameScene",mBall.getX()+" is mBall X");
                    Log.v("GameScene",mBall.getY() +" is mBall Y");
                    Log.v("GameScene",mBall.getWidth()+" is mBall Width");
                    Log.v("GameScene",mBall.getHeight()+" is mBall Height");
                    Log.v("GameScene",b.sprite.getX()+" is Brick X");
                    Log.v("GameScene",b.sprite.getY()+" is Brick Y");
                    Log.v("GameScene",b.sprite.getWidth() +" is Brick Width");
                    Log.v("GameScene",b.sprite.getHeight() +" is Brick Height");
                    
                    final int OFFSET = 14;
                    
                    if(mBall.getX() + OFFSET > b.sprite.getX() && mBall.getX() - OFFSET < b.sprite.getX() + b.sprite.getWidth()) {
                        if(mBall.getY() + mBall.getHeight() - OFFSET < b.sprite.getY()) {
                            mBall.setYSpeed(-1*Math.abs(mBall.getYSpeed()));
                        }
                        else if(mBall.getY() + OFFSET >= b.sprite.getY() +b.sprite.getHeight()) {
                            mBall.setYSpeed(Math.abs(mBall.getYSpeed()));
                        }
                    }
                    if(mBall.getY() + OFFSET > b.sprite.getY() && mBall.getY() - OFFSET < b.sprite.getY() + b.sprite.getHeight()) {
                        if(mBall.getX() + mBall.getWidth() - OFFSET <= b.sprite.getX()) {
                            mBall.setXSpeed(-1*Math.abs(mBall.getXSpeed()));
                        }
                        else if(mBall.getX() + OFFSET >= b.sprite.getX() + b.sprite.getWidth() ) {
                            mBall.setXSpeed(Math.abs(mBall.getXSpeed()));
                        }
                    }    
	            	if( !b.gotHit()) {	            		
	            		createExplosion(b.sprite.getX(), b.sprite.getY(), b.sprite.getParent(), BaseActivity.getSharedInstance());
	            		
	            		BrickPool.sharedBrickPool().recyclePoolItem(b);
	            		bIt.remove();
	            	}            	
        	
	            	break;
	            }
	    	}
	    }
	}
	
	public void detach() {
		Log.v("GravityWell","GameScene onDetach()");
		clearUpdateHandlers();
		detachChildren();
		GravityWell.instance = null;
		BrickPool.instance = null;
	}
	
	private void createExplosion(final float posX, final float posY, final IEntity target, final SimpleBaseGameActivity activity) {
		int mNumPart = 15;
		int mTimePart = 2;

		PointParticleEmitter particleEmitter = new PointParticleEmitter(posX,posY);
		IEntityFactory<Rectangle> recFact = new IEntityFactory<Rectangle>() {
		    @Override
		    public Rectangle create(float pX, float pY) {
		        Rectangle rect = new Rectangle(posX, posY, 10, 10, activity.getVertexBufferObjectManager());
		        rect.setColor(Color.GREEN);
		        return rect;
		    }
		};
		final ParticleSystem<Rectangle> particleSystem = new ParticleSystem<Rectangle>( recFact, particleEmitter, 500, 500, mNumPart);
		
		particleSystem.addParticleInitializer(new VelocityParticleInitializer<Rectangle>(-50, 50, -50, 50));

		particleSystem.addParticleModifier(new AlphaParticleModifier<Rectangle>(0,0.6f * mTimePart, 1, 0));
		particleSystem.addParticleModifier(new RotationParticleModifier<Rectangle>(0, mTimePart, 0, 360));

		target.attachChild(particleSystem);
		
		target.registerUpdateHandler(new TimerHandler(mTimePart, new ITimerCallback() {
		    @Override
		    public void onTimePassed(final TimerHandler pTimerHandler) {
		        particleSystem.detachSelf();
		        target.sortChildren();
		        target.unregisterUpdateHandler(pTimerHandler);
		    }
		}));


	}

}
