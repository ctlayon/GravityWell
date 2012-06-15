package com.ctlayon.gravitywell;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.IShape;

public class GravityWell {

	// ===SHARED INSTANCE OF GRAVITYWELL===//
	public static GravityWell instance;

	// ===SPRITE FOR THE IMAGE===//
	private Rectangle sprite;

	// ===CONTROLLERS SORT OF===//
	Camera mCamera;
	boolean moveable;

	// ===CONSTRUCTORS===//
	public static GravityWell getSharedInstance() {
		if (instance == null)
			instance = new GravityWell();
		return instance;
	}

	public GravityWell() {

		sprite = new Rectangle(0, 0, 70, 30, BaseActivity.getSharedInstance()
				.getVertexBufferObjectManager());

		mCamera = BaseActivity.getSharedInstance().mCamera;
		sprite.setPosition(mCamera.getWidth() / 2 - sprite.getWidth() / 2,
				mCamera.getHeight() - sprite.getHeight() - 50);
		moveable = true;

	}

	// ===PUBLIC METHOD FOR MOVE===//
	public void move(float x) {
		if (!moveable)
			return;
		if (x > (mCamera.getWidth() - (int) sprite.getWidth()))
			x = (mCamera.getWidth() - (int) sprite.getWidth());
		else if (x < 0)
			x = 0;
		sprite.setPosition(x, mCamera.getHeight() - sprite.getHeight() - 50);
	}

	public float getY() {
		return sprite.getY();
	}

	public float getX() {
		return sprite.getX();
	}

	public IShape getSprite() {
		return sprite;
	}
}
