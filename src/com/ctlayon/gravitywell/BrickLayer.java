package com.ctlayon.gravitywell;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier.ILoopEntityModifierListener;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.util.color.Color;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.LoopModifier;

import android.util.Log;

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
			if( i % 2 == 0) {
			    b.setColor(Color.RED);
			    b.setHealth(2);
			}
			else {
			    b.setColor(Color.YELLOW);
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
			
			final LoopEntityModifier entityModifer =
			        new LoopEntityModifier(
			                new IEntityModifierListener() {
                                
                                @Override
                                public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
                                    Log.v("onModiferStarted", "Modifer Started Successfully");                                    
                                }
                                
                                @Override
                                public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
                                    Log.v("onModiferFinished","Modifer Finished Successfully");
                                    
                                }
                            }, 
                            10000000,
                            new ILoopEntityModifierListener() {

                                @Override
                                public void onLoopStarted(
                                        LoopModifier<IEntity> pLoopModifier,
                                        int pLoop, int pLoopCount) {
                                    // TODO Auto-generated method stub
                                    
                                }

                                @Override
                                public void onLoopFinished(
                                        LoopModifier<IEntity> pLoopModifier,
                                        int pLoop, int pLoopCount) {
                                    // TODO Auto-generated method stub
                                    
                                }
                                
                            }, 
                            new SequenceEntityModifier(
                                    new AlphaModifier(1, .8f, .5f),
                                    new ColorModifier(1, b.color, Color.BLUE),
                                    new AlphaModifier(1, .5f, .8f),                                    
                                    new ColorModifier(1, Color.BLUE, b.color)
                            )
                        );
			                        
			
			b.sprite.registerEntityModifier(entityModifer.deepCopy());
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
