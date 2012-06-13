package com.ctlayon.gravitywell;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;

import android.graphics.Typeface;

public class BaseActivity extends SimpleBaseGameActivity {
	
	//===CONSTANTS===//
	static final int CAMERA_WIDTH = 800;
	static final int CAMERA_HEIGHT = 480;
	
	//===PUBLIC VARIABLES===//
	public Font mFont;
	public Camera mCamera;
	
	public ITextureRegion mBall;
	public ITextureRegion mParticleTextureRegion;
	public ITextureRegion mRibbon;
	private BitmapTextureAtlas mBitmapTextureAtlas;
	

	public Scene mCurrentScene;
	public static BaseActivity instance;
	
	//===IMPLEMENTED INTERFACE===//
	@Override
	public EngineOptions onCreateEngineOptions() {
		instance = this;
		mCamera = new Camera(0,0,CAMERA_WIDTH,CAMERA_HEIGHT);
		
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
	}

	@Override
	protected void onCreateResources() {
		try{
			ITexture ball = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/blueBall.png");
		        }
		    });
			ITexture ribbon = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {							
				@Override
				public InputStream open() throws IOException {
					return getAssets().open("gfx/trailShip.png");
				}
			});
			this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			this.mParticleTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "gfx/blueBall.png", 0, 0);
			
			ball.load();
			ribbon.load();
			this.mBitmapTextureAtlas.load();
			
			this.mBall = TextureRegionFactory.extractFromTexture(ball);
			this.mRibbon = TextureRegionFactory.extractFromTexture(ribbon);
			
			mFont = FontFactory.create(this.getFontManager(),this.getTextureManager(), 256, 256,Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
			mFont.load();
			
		} catch (IOException e) {
		    Debug.e(e);
		}
		
		
	}

	@Override
	protected Scene onCreateScene() {
		mEngine.registerUpdateHandler(new FPSLogger());
		mCurrentScene = new GameScene();
		return mCurrentScene;
	}
	

	@Override
	public void onBackPressed() {
	    if (mCurrentScene instanceof GameScene)
	        ((GameScene) mCurrentScene).detach();
	
	    mCurrentScene = null;
	    super.onBackPressed();
	}

	
	//===PUBLIC FUNCTIONS===//
	public void setCurrentScene(Scene scene) {
		mCurrentScene = scene;
		getEngine().setScene(mCurrentScene);
	}

	public static BaseActivity getSharedInstance() {
		return instance;
	}
}