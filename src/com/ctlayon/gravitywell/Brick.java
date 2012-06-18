package com.ctlayon.gravitywell;

import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.IShape;
import org.andengine.util.color.Color;

public class Brick {

	// ===PRIVATE VARIABLES===//
	private Rectangle sprite;

	// ===PUBLIC VARIABLES===//

	public int hp;
	public Color color;

	// ===PROTECTED VARIABLES===//
	protected int MAX_HEALTH = 20;

	// ===CONSTRUCTOR===//
	public Brick() {
		sprite = new Rectangle(0, 0, 60, 30, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
		sprite.setColor(0f, 0f, 0f);
		init();
	}

	// ===PUBLIC FUNCTIONS===//
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
			hp--;
			if (hp == 0)
				return false;
			else
				return true;
		}
	}

	public float getX() {
		return sprite.getX();
	}

	public float getY() {
		return sprite.getY();
	}

	public float getHeight() {
		return sprite.getHeight();
	}

	public float getWidth() {
		return sprite.getWidth();
	}

	public IShape getSprite() {
		return sprite;
	}

	public void setPosition(float x, float y) {
		sprite.setPosition(x, y);

	}

	public void setVisible(boolean visibility) {
		sprite.setVisible(visibility);

	}

	public void registerEntityModifier(IEntityModifier pEntityModifier) {
		sprite.registerEntityModifier(pEntityModifier);
	}

	public void detachSelf() {
		sprite.detachSelf();

	}

}
