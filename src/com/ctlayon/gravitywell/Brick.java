package com.ctlayon.gravitywell;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.util.color.Color;

public class Brick {
	
	//===PUBLIC VARIABLES===//
	public Rectangle sprite;
	public int hp;
	public Color color;

	//===PROTECTED VARIABLES===//
	protected int MAX_HEALTH = 20;
	
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
	
	public void setColor(Color color) {
	    this.color = color;
	    sprite.setColor(this.color);
	}
	
	public void setHealth(int health) {
	    this.hp = health;
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
