package com.ctlayon.gravitywell;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Ball extends Sprite{
	private int xSpeed;
	private int ySpeed;
	
	public Ball(int xSpeed, int ySpeed, float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }
	
	public void setSpeed(int xSpeed, int ySpeed) {
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
	}
	
	public int getXSpeed() {
		return this.xSpeed;
	}
	
	public int getYSpeed() {
		return this.ySpeed;
	}
	
	public void bounceWithWell() {
		this.ySpeed = -this.ySpeed;
	}

}
