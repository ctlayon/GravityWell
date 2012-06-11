package com.ctlayon.gravitywell;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import org.andengine.entity.Entity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.util.color.Color;

public class BrickLayer extends Entity {
	
	//===MEMBER VARIABLES===//
	private LinkedList<Brick> bricks;
	
	//===PUBLIC VARIABLES===//
	public static BrickLayer instance;
	public int brickCount;
	
	//===CONSTANTS===//
	final static int NUM_ROWS = 6;
	final static int X_OFFSET = 150;
	final static int Y_OFFSET = 30;
	
	//===CONSTRUCTORS===//
	public static BrickLayer getSharedInstance() {
		return instance;
	}
	
	public BrickLayer(int x) {
		bricks = new LinkedList<Brick>();
		instance = this;
		brickCount = x;
		restart();
	}
	
	//===PUBLIC MEHTODS===//
	
	public void restart() {
		
		// Clear current layer
		
		bricks.clear();
		clearUpdateHandlers();
		
		// Ask BrickPool for a Brick
		// Create Pattern by Setting finalPos
		// 
		// Move the Brick on Screen From a Random Location
		
		for (int i = 0; i < brickCount; i++) {
			Brick b = BrickPool.sharedBrickPool().obtainPoolItem();
			if( i % 2 == 0 &&(int) (i /NUM_ROWS) % 2 == 0) {
			    b.setColor(Color.RED);
			    b.setHealth(2);
			}
			else if( i % 2 == 0 && (int) (i / NUM_ROWS) % 2 == 1) {
				b.setColor(Color.BLUE);
			    b.setHealth(2);
			}
			else if( i % 2 == 1 && (int) (i/ NUM_ROWS) % 2 == 1) {
				b.setColor(Color.RED);
			    b.setHealth(2);
			}
			else {
			    b.setColor(Color.BLUE);
			    b.setHealth(2);
			}
			float finalPosX = (i % NUM_ROWS)* 1.1f * b.sprite.getWidth() + X_OFFSET;
			float finalPosY = ((int) (i / NUM_ROWS)) * 1.1f * b.sprite.getHeight() + Y_OFFSET;

			Random r = new Random();
			b.sprite.setPosition(r.nextInt(2) == 0 ? -b.sprite.getWidth() * 3
					: BaseActivity.CAMERA_WIDTH + b.sprite.getWidth() * 3,
					(r.nextInt(5) + 1) * b.sprite.getHeight());
			b.sprite.setVisible(true);

			attachChild(b.sprite);
			b.sprite.registerEntityModifier(new MoveModifier(2,
					b.sprite.getX(), finalPosX, b.sprite.getY(), finalPosY));
			
			bricks.add(b);
		}
		
		setVisible(true);
		setPosition(0, 0);		
	}
	

	
	public static void purgeAndRestart() {
	    instance.purge();
	    instance.restart();
	}
	
	public static boolean isEmpty() {
		if(instance.bricks.size() == 0) {
			return true;
		}
		return false;
	}
	
	public static Iterator<Brick> getIterator() {
		return instance.bricks.iterator();
	}
	
	public void purge() {
	    detachChildren();
	    for (Brick b : bricks) {
	        BrickPool.sharedBrickPool().recyclePoolItem(b);
	    }
	    bricks.clear();
	}	
	
	@Override
    public void onDetached() {
		purge();
		super.onDetached();
	}
}
