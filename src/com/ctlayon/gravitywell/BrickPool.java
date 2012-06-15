package com.ctlayon.gravitywell;

import org.andengine.util.adt.pool.GenericPool;

public class BrickPool extends GenericPool<Brick> {
	
	public static BrickPool instance;
	public static BrickPool sharedBrickPool() {
		if(instance == null) {
			return new BrickPool();
		}
		return instance;
	}
	
	private BrickPool() {
		super();
	}
	
	@Override
	protected void onHandleObtainItem(Brick pItem) {
		pItem.init();
	}

	@Override
	protected Brick onAllocatePoolItem() {
		return new Brick();
	}
	/** Called when a projectile is sent to the pool */
	protected void onHandleRecycleItem(final Brick b) {
	    b.setVisible(false);
	    b.detachSelf();
	    b.clean();
	}

}
