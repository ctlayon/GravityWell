package com.ctlayon.gravitywell;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.input.touch.TouchEvent;

public class GameScene extends Scene implements IOnSceneTouchListener {
	public GravityWell well;
	public Ball mBall;
	
	Camera mCamera;
	BaseActivity activity;
	
	public GameScene() {
		setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		attachChild(new BrickLayer(24));
		
		activity = BaseActivity.getSharedInstance();
		mCamera = activity.mCamera;
		
		well = GravityWell.getSharedInstance();
		attachChild(well);		
		
		mBall = new Ball(2,2,100,100,activity.mBall,activity.getVertexBufferObjectManager());
		this.attachChild(mBall);	
		
		activity.setCurrentScene(this);
		setOnSceneTouchListener(this);
		
		registerUpdateHandler(new GameLoopUpdateHandler());
	}
	
	public void moveBall() {
		mBall.move(well, mCamera);
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		well.move(pSceneTouchEvent.getX());
	    return true;
	}

}
