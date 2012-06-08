package com.ctlayon.gravitywell;

import org.andengine.entity.primitive.Rectangle;

public class Brick {
	
	//===PUBLIC VARIABLES===//
	public Rectangle sprite;
	public int hp;	

	//===PROTECTED VARIABLES===//
	protected final int MAX_HEALTH = 2;
	
	//===CONSTRUCTOR===//
	public Brick() {
	    sprite = new Rectangle(0, 0, 60, 30,BaseActivity.getSharedInstance().getVertexBufferObjectManager());
	    sprite.setColor(0f, 0f, 0f);
	    init();
	}

	//===PUBLIC FUNCTIONS===//
	public void init() {
	    hp = MAX_HEALTH;
	}
	
	public void clean() {
	    sprite.clearUpdateHandlers();
	}
	
	public boolean gotHit() {
	    synchronized (this) {
	        //if(BrickCoolDown.getSharedInstance().checkValidity())
	            hp--;
	        if ( hp == 0 )
	        	return false;
	        else
	            return true;
	   }
	}
	
}
