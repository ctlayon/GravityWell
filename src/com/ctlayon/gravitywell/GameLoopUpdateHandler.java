package com.ctlayon.gravitywell;

import org.andengine.engine.handler.IUpdateHandler;

public class GameLoopUpdateHandler implements IUpdateHandler {

	@Override
	public void onUpdate(float pSecondsElapsed) {
		((GameScene)BaseActivity.getSharedInstance().mCurrentScene).moveBall();

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

}
