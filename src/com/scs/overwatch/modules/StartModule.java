package com.scs.overwatch.modules;

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
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.ui.Picture;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;


public class StartModule implements IModule, ActionListener, RawInputListener {

	private static final String QUIT = "Quit";
	private static final String START = "Start";

	protected Overwatch game;
	private BitmapText numPlayerText;

	public StartModule(Overwatch _game) {
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

		BitmapText score = new BitmapText(Overwatch.guiFont_small, false);
		score.setText("Version " + Settings.VERSION + "\n\nThe winner is the first player to score 100.\n\nPress FIRE to start!");
		score.setLocalTranslation(0, game.getCamera().getHeight()-40, 0);
		game.getGuiNode().attachChild(score);

		numPlayerText = new BitmapText(Overwatch.guiFont_small, false);
		numPlayerText.setLocalTranslation(0, game.getCamera().getHeight()-20, 0);
		game.getGuiNode().attachChild(numPlayerText);
	}


	@Override
	public void update(float tpf) {
		Joystick[] joysticks = game.getInputManager().getJoysticks();
		numPlayerText.setText((1+joysticks.length) + " player(s) found.");


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
	 * (non-Javadoc)
	 * @see com.jme3.input.RawInputListener#onJoyButtonEvent(com.jme3.input.event.JoyButtonEvent)
	 * 1 = X
	 * 2 = O
	 * 5 = R1
	 * 7 = R2
	 */
	@Override
	public void onJoyButtonEvent(JoyButtonEvent evt) {
		JoystickButton button = evt.getButton();
		//Settings.p("button.getButtonId()=" + button.getButtonId());
		if (button.getButtonId() == 1) {
			startGame();
		}
	}

	public void beginInput() {}
	public void endInput() {}
	public void onMouseMotionEvent(MouseMotionEvent evt) {}
	public void onMouseButtonEvent(MouseButtonEvent evt) {}
	public void onKeyEvent(KeyInputEvent evt) {}
	public void onTouchEvent(TouchEvent evt) {}


	// End of Raw Input Listener

}
