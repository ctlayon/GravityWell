package com.ctlayon.gravitywell;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class GravityWell extends Rectangle{
	
	//===SHARED INSTANCE OF GRAVITYWELL===//
    public static GravityWell instance;
    
    //===CONTROLLERS SORT OF===//
    Camera mCamera;
    boolean moveable;
    
    //===CONSTRUCTORS===//
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
	}

	//===PUBLIC METHOD FOR MOVE===//
    public void move(float x) {
    	if(!moveable)	
    		return;
    	if(x > (mCamera.getWidth() - (int) this.getWidth()))
    		x = (mCamera.getWidth() - (int) this.getWidth());
    	else if( x < 0 )
    		x = 0;    		
    	this.setPosition(x, mCamera.getHeight() - this.getHeight() - 50);    	
    }	
}
