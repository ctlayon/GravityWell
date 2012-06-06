package com.ctlayon.gravitywell;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Ball extends Sprite{
	private float xSpeed;
	private float ySpeed;
	
	public Ball(float xSpeed, float ySpeed, float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }
	
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
	
	public void bounceVertical() {
		this.ySpeed = -this.ySpeed;
	}
	
	public void bounceHorizontal() {
	    this.xSpeed = -this.xSpeed;
	}
	
	public void move(GravityWell well, Camera mCamera) {
		this.setPosition(this.getX() + this.getXSpeed(),this.getY() + this.getYSpeed());
		
		if(this.collidesWith(well) && CoolDown.getSharedInstance().checkValidity()) {
			this.setYSpeed(-Math.abs(this.getYSpeed()));
		}
		else if (this.getY() >= mCamera.getHeight() - 30) {
			this.bounceVertical();
		}
		else if ( this.getY() < 0 ) {
		    this.bounceVertical();
		}
		if( this.getX() < 0 || 
				this.getX() >= mCamera.getWidth() - this.getWidth() ) {
			this.bounceHorizontal();
		}
		
		float yDist = well.getY() - this.getY();
		float xDist = well.getX() - this.getX();
		if( yDist  >= 95 && yDist < 100 && this.getYSpeed() > -1.5f && this.getYSpeed() < 0)
			this.setYSpeed(-2.0f);
		if( Math.abs( yDist ) < 100 && Math.abs( xDist ) < 100) {
			if( this.getYSpeed() > -5.0f )
				this.setSpeed(this.getXSpeed(), this.getYSpeed() - 0.4f);
			if(xDist < 0) {
				if(this.getXSpeed() > -4.0f )
					this.setXSpeed(this.getXSpeed() - 0.4f);
			}
			else if(xDist > 0)
				if( this.getXSpeed() < 4.0f )
					this.setXSpeed(this.getXSpeed() + 0.4f);
		}
	}
}
