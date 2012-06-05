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
		
		mBall = new Ball(20,20,100,100,activity.mBall,activity.getVertexBufferObjectManager());
		this.attachChild(mBall);	
		
		activity.setCurrentScene(this);
		setOnSceneTouchListener(this);
		
		this.registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				mBall.setPosition(mBall.getX() + mBall.getXSpeed(),mBall.getY() + mBall.getYSpeed());
				if(mBall.collidesWith(well)) {
					mBall.bounceWithWell();
				}
				else if (mBall.getY() >= mCamera.getWidth() - 30) {
					mBall.bounceWithWell();
				}
				else if(mBall.getY() < mBall.getHeight()){
					mBall.bounceWithWell();
				}
				else if(mBall.getX() < mBall.getWidth() || 
						mBall.getX() >= mCamera.getWidth() - mBall.getWidth()) {
					mBall.bounceWithWell();
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
