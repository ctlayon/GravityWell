package com.ctlayon.gravitywell;

import java.util.Iterator;
import java.util.Random;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.AlphaParticleInitializer;
import org.andengine.entity.particle.initializer.BlendFunctionParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;
import org.andengine.entity.particle.modifier.RotationParticleModifier;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

import android.opengl.GLES20;
import android.util.Log;

public class GameScene extends Scene implements IOnSceneTouchListener {

	// ===PUBLIC VARIABLES===//
	public GravityWell well;
	public Ball mBall;

	Camera mCamera;
	BaseActivity activity;

	private PointParticleEmitter particleEmitter;
	private SpriteParticleSystem particleSystem;

	// ===CONSTANTS===//
	final static int OFFSET = 15;

	// ===CONSTRUCTOR===/
	public GameScene() {

		setBackground(new Background(.05f, .1f, .25f));
		this.attachChild(new BrickLayer());

		activity = BaseActivity.getSharedInstance();
		mCamera = activity.mCamera;

		well = GravityWell.getSharedInstance();
		this.attachChild(well.getSprite());

		mBall = new Ball(2, 2, 200, 300, activity.mBall,
				activity.getVertexBufferObjectManager());
		this.attachChild(mBall.getSprite());

		initTrail();

		activity.setCurrentScene(this);
		setOnSceneTouchListener(this);

		registerUpdateHandler(new GameLoopUpdateHandler());
	}

	// ===IMPLEMENTED INTERFACE===//
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		well.move(pSceneTouchEvent.getX());
		return true;
	}

	// ===PUBLIC FUNCTIONS===//
	public void moveBall() {
		mBall.move(well, mCamera);
	}

	public void moveTrail() {
		particleEmitter.setCenter(mBall.getX(), mBall.getY());
	}

	public void cleaner() {
		synchronized (this) {
			// if all Bricks are Hit
			levelOver();

			Iterator<Brick> bIt = BrickLayer.getIterator();
			while (bIt.hasNext()) {
				Brick b = bIt.next();
				if (mBall.collidesWith(b.getSprite())) {
					checkCollisionType(b);
					checkRemove(b, bIt);
					break;
				}
			}
		}
	}

	public void detach() {
		Log.v("GravityWell", "GameScene onDetach()");
		clearUpdateHandlers();
		detachChildren();
		GravityWell.instance = null;
		BrickPool.instance = null;
	}

	// ===PRIVATE FUNCTIONS===//
	private void initTrail() {
		this.particleEmitter = new PointParticleEmitter(mBall.getX(),mBall.getY());
		this.particleSystem = new SpriteParticleSystem(particleEmitter, 100,100, 600, this.activity.mRibbon,
				activity.getVertexBufferObjectManager());

		particleSystem.addParticleInitializer(new AlphaParticleInitializer<Sprite>(0));
		particleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
		particleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(0));
		particleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(2));
		particleSystem.addParticleModifier(new ScaleParticleModifier<Sprite>(0,2, 1, .5f));
		particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(0,2, 1, 0));
		this.attachChild(particleSystem);
	}

	private void checkCollisionType(Brick b) {
		if ( mBall.getX() + OFFSET > b.getX()
				&& mBall.getX() - OFFSET < b.getX() + b.getWidth() ) {
			if ( mBall.getY() + mBall.getHeight() - OFFSET < b.getY() ) {
				mBall.setYSpeed(-1 * Math.abs(mBall.getYSpeed()));
			} else if ( mBall.getY() + OFFSET >= b.getY() + b.getHeight() ) {
				mBall.setYSpeed( Math.abs( mBall.getYSpeed() ) );
			}
		}
		if ( mBall.getY() + OFFSET > b.getY()
				&& mBall.getY() - OFFSET < b.getY() + b.getHeight() ) {
			if ( mBall.getX() + mBall.getWidth() - OFFSET <= b.getX() ) {
				mBall.setXSpeed(-1 * Math.abs(mBall.getXSpeed()));
			} else if ( mBall.getX() + OFFSET >= b.getX() + b.getWidth() ) {
				mBall.setXSpeed(Math.abs(mBall.getXSpeed()) );
			}
		}
	}

	private void checkRemove(Brick b, Iterator<Brick> bIt) {
		if (!b.gotHit()) {
			createExplosion(b.getX(), b.getY(), b.getSprite().getParent(),BaseActivity.getSharedInstance());
			BrickPool.sharedBrickPool().recyclePoolItem(b);
			bIt.remove();
		}
	}

	private void levelOver() {
		if (BrickLayer.isEmpty()) {
			// Move To the next Layer of Bricks
			BrickLayer.nextLevel();
			BrickLayer.purgeAndRestart();
			mBall.setPosition(200, 300);
		}
	}

	private void createExplosion(final float posX, final float posY,
			final IEntity target, final SimpleBaseGameActivity activity) {
		int mNumPart = 15;
		int mTimePart = 5;

		PointParticleEmitter particleEmitter = new PointParticleEmitter(posX,
				posY);
		IEntityFactory<Rectangle> recFact = new IEntityFactory<Rectangle>() {
			@Override
			public Rectangle create(float pX, float pY) {
				Rectangle rect = new Rectangle(posX, posY, 10, 10,
						activity.getVertexBufferObjectManager());
				final Random item = new Random();
				if (item.nextInt() % 2 == 0) {
					rect.setColor(Color.BLUE);
				} else
					rect.setColor(Color.RED);

				return rect;
			}
		};
		final ParticleSystem<Rectangle> particleSystem = new ParticleSystem<Rectangle>(
				recFact, particleEmitter, 500, 500, mNumPart);

		particleSystem.addParticleInitializer(new VelocityParticleInitializer<Rectangle>(-25, 25, -25, -50));

		particleSystem.addParticleModifier(new AlphaParticleModifier<Rectangle>(0,0.6f * mTimePart, 1, 0));
		particleSystem.addParticleModifier(new RotationParticleModifier<Rectangle>(0,mTimePart, 0, 360));
		target.attachChild(particleSystem);
		target.registerUpdateHandler(new TimerHandler(mTimePart,
				new ITimerCallback() {
					@Override
					public void onTimePassed(final TimerHandler pTimerHandler) {
						particleSystem.detachSelf();
						target.sortChildren();
						target.unregisterUpdateHandler(pTimerHandler);
					}
				}));

	}

}
