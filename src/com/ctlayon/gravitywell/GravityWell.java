package com.ctlayon.gravitywell;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class GravityWell extends Rectangle{
    public static GravityWell instance;
    
    Camera mCamera;
    boolean moveable;
    
    public float power;
    float previousPos;
    
    public static GravityWell getSharedInstance() {
        if (instance == null)
            instance = new GravityWell(0,0,70,30,
            		BaseActivity.getSharedInstance().getVertexBufferObjectManager());
        return instance;
    }

	public GravityWell(float pX, float pY, float pWidth, float pHeight, VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY,pWidth,pHeight,pVertexBufferObjectManager);

        mCamera = BaseActivity.getSharedInstance().mCamera;
        this.setPosition(mCamera.getWidth() / 2 - this.getWidth() / 2,
        		mCamera.getHeight() - this.getHeight() - 50);
        
        moveable = true;        
        previousPos=this.getX();
        power = 0;
	}

    public void move(float x) {
    	if(!moveable)	
    		return;
    	if(x > (mCamera.getWidth() - (int) this.getWidth()))
    		x = (mCamera.getWidth() - (int) this.getWidth());
    	else if( x < 0 )
    		x = 0;
    	previousPos = this.getX();
    	float diffPos = (previousPos - x);
    	
    	if( Math.abs(diffPos) >= 10 && Math.abs(diffPos) < 25) {
    		power = 0.5f;
    	}
    	else if( Math.abs(diffPos) >= 25 && Math.abs(diffPos) < 50) {
    		power = 1.0f;
    	}
    	else {
    		power = 0;
    	}
    		
    	this.setPosition(x, mCamera.getHeight() - this.getHeight() - 50);    	
    }	
}
