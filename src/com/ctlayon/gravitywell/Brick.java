package com.ctlayon.gravitywell;

import org.andengine.entity.primitive.Rectangle;

public class Brick {
	public Rectangle sprite;
	public int hp;	

	protected final int MAX_HEALTH = 2;
	
	public Brick() {
	    sprite = new Rectangle(0, 0, 60, 20,BaseActivity.getSharedInstance().getVertexBufferObjectManager());
	    sprite.setColor(0.09904f, 0.8574f, 0.1786f);
	    init();
	}

	public void init() {
	    hp = MAX_HEALTH;
	}
	
	public void clean() {
	    sprite.clearUpdateHandlers();
	}
	
	public boolean gotHit() {
	    synchronized (this) {
	        hp--;
	        if ( hp == 0 )
	        	return false;
	        else
	            return true;
	   }
	}
	
}
