package com.ctlayon.gravitywell;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Ball {

	// ===MEMBER VARIABLES===//
	private float xSpeed;
	private float ySpeed;

	// ===SPRITE FOR THE IMAGE OF THE BALL===//
	private Sprite sprite;

	// ===CONSTANTS===//
	final static float Y_MAX_SPEED = -5.0f;
	final static float X_MAX_SPEED = 4.0f;
	final static float Y_MIN_SPEED = -2.0f;
	final static float Y_ACCELERATION = 0.4f;
	final static float X_ACCELERATION = 0.4f;

	// ===CONSTRUCTOR===//

	public Ball(float xSpeed, float ySpeed, float pX, float pY,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		sprite = new Sprite(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
	}

	// ===ACCESS METHODS===//

	public void setSpeed(float xSpeed, float ySpeed) {
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
	}

	public void setXSpeed(float xSpeed) {
		this.xSpeed = xSpeed;
	}

	public void setYSpeed(float ySpeed) {
		this.ySpeed = ySpeed;
	}

	public float getXSpeed() {
		return this.xSpeed;
	}

	public float getYSpeed() {
		return this.ySpeed;
	}

	public float getWidth() {
		return sprite.getWidth();
	}

	public float getHeight() {
		return sprite.getHeight();
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
	
	// ===PUBLIC FUNCTIONS FOR MOVEMENT===//

	public void bounceVertical() {
		this.ySpeed = -this.ySpeed;
	}

	public void bounceHorizontal() {
		this.xSpeed = -this.xSpeed;
	}

	public void move(GravityWell well, Camera mCamera) {

		// update position based on previous speed

		sprite.setPosition(this.getX() + this.getXSpeed(),
				this.getY() + this.getYSpeed());

		// check if it collides with a GravityWell
		// check if it hasn't recently collided With a GravityWell
		//
		// Gravity well only pushes up so Update Speed to be negative

		if (this.collidesWith(well.getSprite())
				&& CoolDown.getSharedInstance().checkValidity()) {
			this.setYSpeed(-1 * Math.abs(this.getYSpeed()));
		}

		// bounced with floor change y direction

		else if (this.getY() >= mCamera.getHeight() - 30) {
			this.bounceVertical();
		}

		// bounced with ceiling change y direction

		else if (this.getY() < 0) {
			this.bounceVertical();
		}

		// bounced off a wall change the x direction

		if (this.getX() < 0
				|| this.getX() >= mCamera.getWidth() - this.getWidth()) {
			this.bounceHorizontal();
		}

		// calculate x and y pseudo distance

		float yDist = well.getY() - this.getY();
		float xDist = well.getX() - this.getX();

		// If the ball is moving really slow and is out of the GravityWell's
		// Range
		// set the speed to be the minimum

		if (yDist >= 95 && yDist < 100 && this.getYSpeed() > Y_MIN_SPEED
				&& this.getYSpeed() < 0)
			this.setYSpeed(Y_MIN_SPEED);

		// Update speed based on 'distance' from the GravityWell
		// Using Acceleration

		if (Math.abs(yDist) < 100 && Math.abs(xDist) < 100) {
			if (this.getYSpeed() > Y_MAX_SPEED)
				this.setSpeed(this.getXSpeed(), this.getYSpeed() - Y_ACCELERATION);
			if (xDist < 0) {
				if (this.getXSpeed() > -X_MAX_SPEED)
					this.setXSpeed(this.getXSpeed() - X_ACCELERATION);
			} else if (xDist > 0)
				if (this.getXSpeed() < X_MAX_SPEED)
					this.setXSpeed(this.getXSpeed() + X_ACCELERATION);
		}
	}

	public void setPosition(int i, int j) {
		sprite.setPosition(i, j);
	}

	public boolean collidesWith(IShape sprite2) {
		return sprite.collidesWith(sprite2);
	}

}
