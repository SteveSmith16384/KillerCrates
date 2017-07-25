package com.scs.overwatch.modules;

import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.font.BitmapFont;
import com.jme3.input.Joystick;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;
import com.scs.overwatch.Settings.GameMode;
import com.scs.overwatch.components.ICollideable;
import com.scs.overwatch.components.IEntity;
import com.scs.overwatch.components.IProcessable;
import com.scs.overwatch.entities.PhysicalEntity;
import com.scs.overwatch.entities.PlayersAvatar;
import com.scs.overwatch.hud.HUD;
import com.scs.overwatch.input.IInputDevice;
import com.scs.overwatch.input.JoystickCamera;
import com.scs.overwatch.input.MouseAndKeyboardCamera;
import com.scs.overwatch.map.BoxMap;
import com.scs.overwatch.map.IMapLoader;
import com.scs.overwatch.map.IPertinentMapData;
import com.scs.overwatch.map.SimpleCity;
import com.scs.overwatch.map.SimpleMapLoader;

import ssmith.lang.NumberFunctions;
import ssmith.util.TSArrayList;

public class GameModule implements IModule, PhysicsCollisionListener, ActionListener {

	private static final String QUIT = "Quit";
	private static final String TEST = "Test";

	protected Overwatch game;
	public BulletAppState bulletAppState;
	public TSArrayList<IEntity> entities = new TSArrayList<>();
	public TSArrayList<PlayersAvatar> avatars = new TSArrayList<>();
	public IPertinentMapData mapData;
	public TextureKey crateTexKey;

	public GameModule(Overwatch _game) {
		super();

		game = _game;
	}


	@Override
	public void init() {
		game.getInputManager().addMapping(QUIT, new KeyTrigger(KeyInput.KEY_ESCAPE));
		game.getInputManager().addListener(this, QUIT);            

		game.getInputManager().addMapping(TEST, new KeyTrigger(KeyInput.KEY_T));
		game.getInputManager().addListener(this, TEST);            

		// Set up Physics
		bulletAppState = new BulletAppState();
		game.getStateManager().attach(bulletAppState);
		bulletAppState.getPhysicsSpace().addCollisionListener(this);
		//bulletAppState.getPhysicsSpace().enableDebug(assetManager);

		game.getRenderManager().removeMainView(game.getViewPort()); // Since we create new ones for each player

		setUpLight();

		int i = NumberFunctions.rnd(1, 10);
		crateTexKey = new TextureKey("Textures/boxes and crates/" + i + ".png");

		if (Settings.gameMode == GameMode.KillerCrates) {
			IMapLoader maploader = new SimpleMapLoader(game, this, new BoxMap(game, this));
			mapData = maploader.loadMap();
		} else {
			mapData = new SimpleCity(game, this);
		}

		Joystick[] joysticks = game.getInputManager().getJoysticks();
		int numPlayers = 1+joysticks.length;

		// Auto-Create player 0 - keyboard and mouse
		{
			Camera newCam = this.createCamera(0, numPlayers);
			HUD hud = this.createHUD(newCam, 0);
			MouseAndKeyboardCamera keyboard = new MouseAndKeyboardCamera(newCam, game.getInputManager());
			this.addPlayersAvatar(0, newCam, keyboard, hud); // Keyboard player
		}

		// Create players for each joystick
		int nextid=1;
		if (joysticks == null || joysticks.length == 0) {
			Settings.p("NO JOYSTICKS/GAMEPADS");
		} else {
			for (Joystick j : joysticks) {
				int id = nextid++;
				Camera newCam = this.createCamera(id, numPlayers);
				HUD hud = this.createHUD(newCam, id);
				JoystickCamera joyCam = new JoystickCamera(newCam, j, game.getInputManager());
				this.addPlayersAvatar(id, newCam, joyCam, hud);
			}
		}
		if (Settings.ALWAYS_SHOW_4_CAMS) {
			// Create extra cameras
			for (int id=nextid ; id<=3 ; id++) {
				Camera c = this.createCamera(id, numPlayers);
				this.createHUD(c, id);
				switch (id) {
				case 1:
					c.setLocation(new Vector3f(2f, PlayersAvatar.PLAYER_HEIGHT, 2f));
					break;
				case 2:
					c.setLocation(new Vector3f(mapData.getWidth()-3, PlayersAvatar.PLAYER_HEIGHT, 2f));
					break;
				case 3:
					c.setLocation(new Vector3f(2f, PlayersAvatar.PLAYER_HEIGHT, mapData.getDepth()-3));
					break;
				}
				c.lookAt(new Vector3f(mapData.getWidth()/2, PlayersAvatar.PLAYER_HEIGHT, mapData.getDepth()/2), Vector3f.UNIT_Y);
			}
		}

		//stateManager.getState(StatsAppState.class).toggleStats(); // Turn off stats


	}


	private Camera createCamera(int id, int numPlayers) {
		Camera newCam = null;
		if (id == 0) {
			newCam = game.getCamera();
		} else {
			newCam = game.getCamera().clone();
		}

		if (Settings.ALWAYS_SHOW_4_CAMS || numPlayers > 2) {
			newCam.setFrustumPerspective(45f, (float) newCam.getWidth() / newCam.getHeight(), 0.01f, Settings.CAM_DIST);
			switch (id) { // left/right/bottom/top, from bottom-left!
			case 0: // TL
				Settings.p("Creating camera top-left");
				newCam.setViewPort(0f, 0.5f, 0.5f, 1f);
				newCam.setName("Cam_TL");
				break;
			case 1: // TR
				Settings.p("Creating camera top-right");
				newCam.setViewPort(0.5f, 1f, 0.5f, 1f);
				newCam.setName("Cam_TR");
				break;
			case 2: // BL
				Settings.p("Creating camera bottom-left");
				newCam.setViewPort(0f, 0.5f, 0f, .5f);
				newCam.setName("Cam_BL");
				break;
			case 3: // BR
				Settings.p("Creating camera bottom-right");
				newCam.setViewPort(0.5f, 1f, 0f, .5f);
				newCam.setName("Cam_BR");
				break;
			default:
				throw new RuntimeException("Unknown player id: " + id);
			}
		} else if (numPlayers == 2) {
			newCam.setFrustumPerspective(45f, (float) (newCam.getWidth()*2) / newCam.getHeight(), 0.01f, Settings.CAM_DIST);
			switch (id) { // left/right/bottom/top, from bottom-left!
			case 0: // TL
				Settings.p("Creating camera top");
				newCam.setViewPort(0f, 1f, 0.5f, 1f);
				newCam.setName("Cam_Top");
				break;
			case 1: // TR
				Settings.p("Creating camera bottom");
				newCam.setViewPort(0.0f, 1f, 0f, .5f);
				newCam.setName("Cam_bottom");
				break;
			default:
				throw new RuntimeException("Unknown player id: " + id);
			}
		} else if (numPlayers == 1) {
			newCam.setFrustumPerspective(45f, (float) newCam.getWidth() / newCam.getHeight(), 0.01f, Settings.CAM_DIST);
			Settings.p("Creating full-screen camera");
			newCam.setViewPort(0f, 1f, 0f, 1f);
			newCam.setName("Cam_FullScreen");

		} else {
			throw new RuntimeException("Unknown number of players");

		}
		// Look at the centre by default
		//c.lookAt(new Vector3f(map.getWidth()/2, 2f, map.getDepth()/2), Vector3f.UNIT_Y);

		final ViewPort view2 = game.getRenderManager().createMainView("viewport_"+newCam.toString(), newCam);
		//view2.setBackgroundColor(new ColorRGBA(0f, 0.9f, .9f, 0f)); // 148 187 242
		switch (Settings.gameMode) {
		case KillerCrates:
			view2.setBackgroundColor(new ColorRGBA(148f/255f, 187f/255f, 242f/255f, 0f));
			break;
		case BladeRunner:
			view2.setBackgroundColor(new ColorRGBA(0, 0, 0, 0f));
			break;
		}
		view2.setClearFlags(true, true, true);
		view2.attachScene(game.getRootNode());

		return newCam;
	}


	private HUD createHUD(Camera _cam, int id) {
		BitmapFont guiFont_small = game.getAssetManager().loadFont("Interface/Fonts/Console.fnt");

		// cam.getWidth() = 640x480, cam.getViewPortLeft() = 0.5f
		float x = _cam.getWidth() * _cam.getViewPortLeft();
		//float y = (_cam.getHeight() * _cam.getViewPortTop())-(_cam.getHeight()/2);
		float y = (_cam.getHeight() * _cam.getViewPortTop())-(_cam.getHeight()/2);
		float w = _cam.getWidth() * (_cam.getViewPortRight()-_cam.getViewPortLeft());
		float h = _cam.getHeight() * (_cam.getViewPortTop()-_cam.getViewPortBottom());
		HUD hud = new HUD(game, this, x, y, w, h, guiFont_small, id, _cam);
		game.getGuiNode().attachChild(hud);
		return hud;

	}


	private void addPlayersAvatar(int id, Camera c, IInputDevice input, HUD hud) {
		Settings.p("Creating player " + id);

		PlayersAvatar player = new PlayersAvatar(game, this, id, c, input, hud, crateTexKey);
		game.getRootNode().attachChild(player.getMainNode());
		this.entities.add(player);

		player.moveToStartPostion();

		// Look towards centre
		player.getMainNode().lookAt(new Vector3f(mapData.getWidth()/2, PlayersAvatar.PLAYER_HEIGHT, mapData.getDepth()/2), Vector3f.UNIT_Y);
	}


	private void setUpLight() {
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(3));
		game.getRootNode().addLight(al);
	}


	@Override
	public void update(float tpf) {
		this.entities.refresh();
		this.avatars.refresh();

		for(IEntity e : entities) {
			if (e instanceof IProcessable) {
				IProcessable ip = (IProcessable)e;
				ip.process(tpf);
			}
		}


	}


	@Override
	public void collision(PhysicsCollisionEvent event) {
		String s = event.getObjectA().getUserObject().toString() + " collided with " + event.getObjectB().getUserObject().toString();
		//System.out.println(s);
		if (s.equals("Entity:Player collided with cannon ball (Geometry)")) {
			int f = 3;
		}
		PhysicalEntity a=null, b=null;
		Object oa = event.getObjectA().getUserObject(); 
		if (oa instanceof Spatial) {
			Spatial ga = (Spatial)event.getObjectA().getUserObject(); 
			a = ga.getUserData(Settings.ENTITY);
		} else if (oa instanceof PhysicalEntity) {
			a = (PhysicalEntity)oa;
		}

		Object ob = event.getObjectB().getUserObject(); 
		if (ob instanceof Spatial) {
			Spatial gb = (Spatial)event.getObjectB().getUserObject(); 
			b = gb.getUserData(Settings.ENTITY);
		} else if (oa instanceof PhysicalEntity) {
			b = (PhysicalEntity)ob;
		}

		if (a != null && b != null) {
			//CollisionLogic.collision(this, a, b);
			if (a instanceof ICollideable && b instanceof ICollideable) {
				ICollideable ica = (ICollideable)a;
				ICollideable icb = (ICollideable)b;
				ica.collidedWith(icb);
				icb.collidedWith(ica);
			}
		}
	}


	public void addEntity(IEntity e) {
		this.entities.add(e);

		if (e instanceof PlayersAvatar) {
			PlayersAvatar a = (PlayersAvatar)e;
			this.avatars.add(a);
		}
	}


	public void removeEntity(IEntity e) {
		this.entities.remove(e);

		if (e instanceof PlayersAvatar) {
			PlayersAvatar a = (PlayersAvatar)e;
			this.avatars.remove(a);
		}
	}


	public BulletAppState getBulletAppState() {
		return bulletAppState;
	}


	@Override
	public void onAction(String name, boolean value, float tpf) {
		if (!value) {
			return;
		}

		if (name.equals(TEST)) {
			/*for(IEntity e : entities) {
				if (e instanceof PlayersAvatar) {
					PlayersAvatar ip = (PlayersAvatar)e;
					ip.hud.showDamageBox();
					break;
				}
			}*/

			/*for(IEntity e : entities) {
				if (e instanceof PlayersAvatar) {
					PlayersAvatar ip = (PlayersAvatar)e;
					ip.hasSuccessfullyHit(e);
					break;
				}
			}*/

		} else if (name.equals(QUIT)) {
			game.setNextModule(new StartModule(game));
		}

	}


	@Override
	public void destroy() {
		game.getInputManager().clearMappings();
		game.getInputManager().clearRawInputListeners();//.removeRawInputListener(this);
		//game.getInputManager().removeListener(this);

	}


}
