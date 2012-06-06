package com.ctlayon.gravitywell;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import org.andengine.entity.Entity;
import org.andengine.entity.modifier.MoveModifier;

public class BrickLayer extends Entity {
	
	private LinkedList<Brick> bricks;
	public static BrickLayer instance;
	public int brickCount;
	
	public static BrickLayer getSharedInstance() {
		return instance;
	}
	
	public BrickLayer(int x) {
		bricks = new LinkedList<Brick>();
		instance = this;
		brickCount = x;
		restart();
	}
	
	public void restart() {
		bricks.clear();
		clearUpdateHandlers();
		
		for (int i = 0; i < brickCount; i++) {
			Brick b = BrickPool.sharedBrickPool().obtainPoolItem();
			float finalPosX = (i % 6) * 4 * b.sprite.getWidth();
			float finalPosY = ((int) (i / 6)) * b.sprite.getHeight() * 2;

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
		setPosition(50, 30);		
	}
	
	@Override
    public void onDetached() {
		purge();
		super.onDetached();
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
}
