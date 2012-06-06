package com.ctlayon.gravitywell;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.input.touch.TouchEvent;

public class GameScene extends Scene implements IOnSceneTouchListener {
	public GravityWell well;
	
	Camera mCamera;
	BaseActivity activity;
	
	public GameScene() {
		setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		
		activity = BaseActivity.getSharedInstance();
		mCamera = activity.mCamera;
		
		well = GravityWell.getSharedInstance();
		attachChild(well);		
		
		final Ball mBall;
		
		mBall = new Ball(2,2,100,100,activity.mBall,activity.getVertexBufferObjectManager());
		this.attachChild(mBall);	
		
		activity.setCurrentScene(this);
		setOnSceneTouchListener(this);
		
		this.registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void onUpdate(float pSecondsElapsed) {			    
                		    
				mBall.setPosition(mBall.getX() + mBall.getXSpeed(),mBall.getY() + mBall.getYSpeed());
				
				if(mBall.collidesWith(well) && CoolDown.getSharedInstance().checkValidity()) {
					mBall.setYSpeed(-Math.abs(mBall.getYSpeed()));
				}
				else if (mBall.getY() >= mCamera.getHeight() - 30) {
					mBall.bounceVertical();
				}
				else if ( mBall.getY() < 0 ) {
				    mBall.bounceVertical();
				}
				if( mBall.getX() < 0 || 
						mBall.getX() >= mCamera.getWidth() - mBall.getWidth() ) {
					mBall.bounceHorizontal();
				}
				
				float yDist = well.getY() - mBall.getY();
				float xDist = well.getX() - mBall.getX();
				if( yDist  >= 95 && yDist < 100 && mBall.getYSpeed() > -1.5f && mBall.getYSpeed() < 0)
					mBall.setYSpeed(-2.0f);
				if( Math.abs( yDist ) < 100 && Math.abs( xDist ) < 100) {
					if( mBall.getYSpeed() > -5.0f )
						mBall.setSpeed(mBall.getXSpeed(), mBall.getYSpeed() - 0.4f);
					if(xDist < 0) {
						if(mBall.getXSpeed() > -4.0f )
							mBall.setXSpeed(mBall.getXSpeed() - 0.4f);
					}
					else if(xDist > 0)
						if( mBall.getXSpeed() < 4.0f )
							mBall.setXSpeed(mBall.getXSpeed() + 0.4f);
				}
			}

			@Override
			public void reset() {
				// TODO Auto-generated method stub
				
			}
			
		});
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		well.move(pSceneTouchEvent.getX());
	    return true;
	}

}
