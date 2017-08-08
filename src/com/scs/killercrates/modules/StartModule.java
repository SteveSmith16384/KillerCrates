package com.scs.killercrates.modules;

import java.util.List;

import com.jme3.font.BitmapText;
import com.jme3.input.Joystick;
import com.jme3.input.JoystickButton;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;
import com.scs.killercrates.KillerCrates;
import com.scs.killercrates.Settings;
import com.scs.killercrates.map.ConfigMap;


public class StartModule implements IModule, ActionListener, RawInputListener {

	private static final String QUIT = "Quit";
	private static final String START = "Start";

	protected KillerCrates game;
	private BitmapText numPlayerText;
	private Node model;
	
	public StartModule(KillerCrates _game) {
		super();

		game = _game;
	}


	@Override
	public void init() {
		List<ViewPort> views = game.getRenderManager().getMainViews();
		while (!views.isEmpty()) {
			game.getRenderManager().removeMainView(views.get(0));
			views = game.getRenderManager().getMainViews();
		}

		// Create viewport
		Camera newCam = game.getCamera();
		newCam.setFrustumPerspective(45f, (float) newCam.getWidth() / newCam.getHeight(), 0.01f, Settings.CAM_DIST);
		newCam.setViewPort(0f, 1f, 0f, 1f);
		newCam.setLocation(Vector3f.ZERO);
		newCam.lookAt(Vector3f.UNIT_Z, Vector3f.UNIT_Y);

		final ViewPort view2 = game.getRenderManager().createMainView("viewport_" + newCam.toString(), newCam);
		//view2.setBackgroundColor(new ColorRGBA(0f, 0.9f, .9f, 0f)); // 148 187 242
		view2.setBackgroundColor(new ColorRGBA(148f/255f, 187f/255f, 242f/255f, 0f));
		view2.setClearFlags(true, true, true);
		view2.attachScene(game.getRootNode());

		game.getInputManager().addMapping(QUIT, new KeyTrigger(KeyInput.KEY_ESCAPE));
		game.getInputManager().addListener(this, QUIT);            

		// Lights
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White);//.mult(3));
		game.getRootNode().addLight(al);

		// Auto-Create player 0 - keyboard and mouse
		{
			game.getInputManager().addMapping(START, new MouseButtonTrigger(MouseInput.BUTTON_LEFT), new KeyTrigger(KeyInput.KEY_SPACE));
			game.getInputManager().addListener(this, START);            
		}

		game.getInputManager().addRawInputListener(this);

		if (Settings.SHOW_LOGO) {
			Picture pic = new Picture("HUD Picture");
			pic.setImage(game.getAssetManager(), "Textures/killercrates_logo.png", true);
			pic.setWidth(game.getCamera().getWidth());
			pic.setHeight(game.getCamera().getWidth()/7);
			game.getGuiNode().attachChild(pic);
		}

		Joystick[] joysticks = game.getInputManager().getJoysticks();

		ConfigMap cfgMap = new ConfigMap(game, null);
		
		BitmapText score = new BitmapText(KillerCrates.guiFont_small, false);
		score.setText(Settings.NAME + "\n\nVersion " + Settings.VERSION + "\n\n" + 
				(1+joysticks.length) + " player(s) found.  Please restart if you plug any more gamepads in.\n\n" + 
				"The winner is the first player to score 100.\n\nPress FIRE to start!\n\n" + 
				"Map settings: " + cfgMap.getWidth() + " by " + cfgMap.getDepth() + " with " + game.getPropertyAsInt("numFurniture", 35) + " pieces of furniture.\n" + 
				"Edit the file '" + KillerCrates.PROPS_FILE + "' to change this.");
		score.setLocalTranslation(20, game.getCamera().getHeight()-20, 0);
		game.getGuiNode().attachChild(score);

		numPlayerText = new BitmapText(KillerCrates.guiFont_small, false);
		numPlayerText.setLocalTranslation(20, game.getCamera().getHeight()-120, 0);
		game.getGuiNode().attachChild(numPlayerText);
		
		
		// Audio
		/*todo - re-add AudioNode audio_nature = new AudioNode(game.getAssetManager(), "sfx/independent_nu_ljudbank-wood_crack_hit_destruction/wood_impact/impactwood25.mp3.flac", true, false);
		//AudioNode audio_nature = new AudioNode(game.getAssetManager(), "sfx/megasong.mp3", true, false);
	    audio_nature.setLooping(true);  // activate continuous playing
	    audio_nature.setPositional(false);
	    audio_nature.setVolume(3);
	    game.getRootNode().attachChild(audio_nature);
	    audio_nature.play(); // play continuously!*/
		
		model = game.getRandomModel(false);
		model.setLocalTranslation(0, -.5f, 2);
		game.getRootNode().attachChild(model);
	}


	@Override
	public void update(float tpf) {
		model.rotate(0,  tpf,  0);
	}


	@Override
	public void destroy() {
		game.getInputManager().clearMappings();
		game.getInputManager().clearRawInputListeners();
		game.getInputManager().removeListener(this);

	}


	@Override
	public void onAction(String name, boolean value, float tpf) {
		if (!value) {
			return;
		}

		if (name.equals(START)) {
			startGame();
		} else if (name.equals(QUIT)) {
			KillerCrates.saveProperties();
			game.stop();
		}		
	}


	private void startGame() {
		game.setNextModule(new GameModule(game));

	}

	// Raw Input Listener ------------------------

	@Override
	public void onJoyAxisEvent(JoyAxisEvent evt) {
	}

	/*
	 * 1 = X
	 * 2 = O
	 * 5 = R1
	 * 7 = R2
	 */
	@Override
	public void onJoyButtonEvent(JoyButtonEvent evt) {
		JoystickButton button = evt.getButton();
		//Settings.p("button.getButtonId()=" + button.getButtonId());
		//if (button.getButtonId() == 1) {
			startGame();
		//}
	}

	public void beginInput() {}
	public void endInput() {}
	public void onMouseMotionEvent(MouseMotionEvent evt) {}
	public void onMouseButtonEvent(MouseButtonEvent evt) {}
	public void onKeyEvent(KeyInputEvent evt) {}
	public void onTouchEvent(TouchEvent evt) {}


	// End of Raw Input Listener

}
