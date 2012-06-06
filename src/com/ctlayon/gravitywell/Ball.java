package com.ctlayon.gravitywell;

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
}
