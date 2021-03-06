package com.scs.killercrates.entities;

import java.awt.Point;

import com.jme3.asset.TextureKey;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.Camera.FrustumIntersect;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial;
import com.scs.killercrates.KillerCrates;
import com.scs.killercrates.MyBetterCharacterControl;
import com.scs.killercrates.Settings;
import com.scs.killercrates.abilities.IAbility;
import com.scs.killercrates.abilities.NoAbility;
import com.scs.killercrates.components.IBullet;
import com.scs.killercrates.components.ICanShoot;
import com.scs.killercrates.components.ICollideable;
import com.scs.killercrates.components.IEntity;
import com.scs.killercrates.hud.AbstractHUDImage;
import com.scs.killercrates.hud.HUD;
import com.scs.killercrates.input.IInputDevice;
import com.scs.killercrates.map.SimpleMapLoader;
import com.scs.killercrates.modules.GameModule;
import com.scs.killercrates.weapons.IMainWeapon;
import com.scs.killercrates.weapons.KillerCrateGun;

public class PlayersAvatar extends PhysicalEntity implements ICollideable, ICanShoot {

	// Player dimensions
	public static final float PLAYER_HEIGHT = 0.5f;//1.5f;
	public static final float PLAYER_RAD = 0.25f; //.2f; //.5f;//.35f; // if you increase this, player bounces!?
	private static final float WEIGHT = 3f;

	public Vector3f walkDirection = new Vector3f();
	public float moveSpeed = KillerCrates.getPropertyAsFloat("moveSpeed", 3f);//. Settings.DEFAULT_MOVE_SPEED;
	private IInputDevice input;

	//Temporary vectors used on each frame.
	private Camera cam;
	private Vector3f camDir = new Vector3f();
	private Vector3f camLeft = new Vector3f(); // Used for strafing

	public HUD hud;
	public MyBetterCharacterControl playerControl;
	public final int id;
	private float timeSinceLastMove = 0;
	private IAbility ability;
	public Spatial playerGeometry;
	private int score = 20;
	private IMainWeapon weapon;

	public PlayersAvatar(KillerCrates _game, GameModule _module, int _id, Camera _cam, IInputDevice _input, HUD _hud, TextureKey key3) {
		super(_game, _module, "Player");

		id = _id;
		cam = _cam;
		input = _input;
		hud = _hud;

		weapon = new KillerCrateGun(_game, _module, this);
		
		playerGeometry = game.getRandomModel(true);
		
		/*Box box1 = new Box(PLAYER_RAD, PLAYER_HEIGHT/2, PLAYER_RAD);
		playerGeometry = new Geometry("Player", box1);
		key3.setGenerateMips(true);
		Texture tex3 = game.getAssetManager().loadTexture(key3);
		Material floor_mat = null;
		if (Settings.LIGHTING) {
			floor_mat = new Material(game.getAssetManager(),"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
			floor_mat.setTexture("DiffuseMap", tex3);
		} else {
			floor_mat = new Material(game.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
			floor_mat.setTexture("ColorMap", tex3);
		}
		playerGeometry.setMaterial(floor_mat);
		//playerGeometry.setLocalTranslation(new Vector3f(0, PLAYER_HEIGHT/2, 0)); // Need this to ensure the crate is on the floor
		playerGeometry.setLocalTranslation(new Vector3f(0, (PLAYER_HEIGHT/2)-.075f, 0)); // Need this to ensure the crate is on the floor*/
		this.getMainNode().attachChild(playerGeometry);
		//this.getMainNode().setLocalTranslation(new Vector3f(0,PLAYER_HEIGHT,0)); // Need this to ensure the crate is on the floor
		
		playerGeometry.setShadowMode(ShadowMode.CastAndReceive);
		
		// create character control parameters (Radius,Height,Weight)
		playerControl = new MyBetterCharacterControl(PLAYER_RAD, PLAYER_HEIGHT, WEIGHT);
		float jumpForce = KillerCrates.getPropertyAsFloat("jumpForce", 6f);
		playerControl.setJumpForce(new Vector3f(0, jumpForce, 0)); 
		this.getMainNode().addControl(playerControl);

		module.bulletAppState.getPhysicsSpace().add(playerControl);

		this.getMainNode().setUserData(Settings.ENTITY, this);
		playerControl.getPhysicsRigidBody().setUserObject(this);

		this.ability = new NoAbility();
		//this.hud.setAbilityText(this.ability.getHudText());
	}


	public void moveToStartPostion() {
		Point p = module.mapData.getPlayerStartPos(id);
		playerControl.warp(new Vector3f(p.x, SimpleMapLoader.CEILING_HEIGHT-1f, p.y));

	}


	@Override
	public void process(float tpf) {
		timeSinceLastMove += tpf;
		if (ability.process(tpf)) {
			this.hud.setAbilityText(this.ability.getHudText());
		}
		hud.process(tpf);

		if (input.isAbility1Pressed()) { // Must be before we set the walkDirection & moveSpeed, as this method may affect it
			//Settings.p("Using " + this.ability.toString());
			this.ability.activate(tpf);
		}

		/*
		 * The direction of character is determined by the camera angle
		 * the Y direction is set to zero to keep our character from
		 * lifting of terrain. For free flying games simply add speed 
		 * to Y axis
		 */
		camDir.set(cam.getDirection()).multLocal(moveSpeed, 0.0f, moveSpeed);
		camLeft.set(cam.getLeft()).multLocal(moveSpeed);
		walkDirection.set(0, 0, 0);
		if (input.getFwdValue() > 0) {		
			//Settings.p("fwd=" + input.getFwdValue());
			walkDirection.addLocal(camDir.mult(input.getFwdValue()));
			timeSinceLastMove = 0;
		}
		if (input.getBackValue() > 0) {
			walkDirection.addLocal(camDir.negate().mult(input.getBackValue()));
			timeSinceLastMove = 0;
		}
		if (input.getStrafeLeftValue() > 0) {		
			walkDirection.addLocal(camLeft.mult(input.getStrafeLeftValue()));
			timeSinceLastMove = 0;
		}
		if (input.getStrafeRightValue() > 0) {		
			walkDirection.addLocal(camLeft.negate().mult(input.getStrafeRightValue()));
			timeSinceLastMove = 0;
		}
		playerControl.setWalkDirection(walkDirection);

		if (input.isJumpPressed() || timeSinceLastMove > 10) {
			//Settings.p("timeSinceLastMove=" + timeSinceLastMove);
			timeSinceLastMove = 0;
			this.jump();
		}

		if (input.isShootPressed()) {
			timeSinceLastMove = 0;
			shoot();
		}

		/*
		 * By default the location of the box is on the bottom of the terrain
		 * we make a slight offset to adjust for head height.
		 */
		Vector3f vec = getMainNode().getWorldTranslation();
		cam.setLocation(new Vector3f(vec.x, vec.y + (PLAYER_HEIGHT/2), vec.z));

		// Rotate us to point in the direction of the camera
		Vector3f lookAtPoint = cam.getLocation().add(cam.getDirection().mult(10));
		lookAtPoint.y = cam.getLocation().y;
		this.playerGeometry.lookAt(lookAtPoint, Vector3f.UNIT_Y);

		// Move cam fwd so we don't see ourselves
		//cam.setLocation(cam.getLocation().add(cam.getDirection().mult(PLAYER_RAD*2))); // todo - adjust by model size
	
		this.input.resetFlags();

		// Have we fallen off the edge
		if (this.getMainNode().getWorldTranslation().y < -5f) {
			this.moveToStartPostion();
		}
	}


	public void shoot() {
		//if (shotInterval.hitInterval()) {
		if (this.weapon.shoot()) {
			//Bullet b = new Bullet(game, module, this);
			//module.addEntity(b);
			this.score--;
			this.hud.setScore(this.score);
			/*if (this.score <= 0) {
				module.playerOut(this);
			}*/
		}
	}


	public FrustumIntersect getInsideOutside(PhysicalEntity entity) {
		FrustumIntersect insideoutside = cam.contains(entity.getMainNode().getWorldBound());
		return insideoutside;
	}


	@Override
	public void remove() {
		super.remove();
		this.module.bulletAppState.getPhysicsSpace().remove(this.playerControl);

	}


	@Override
	public Vector3f getLocation() {
		return this.cam.getLocation();
	}


	@Override
	public Vector3f getShootDir() {
		return this.cam.getDirection();
	}


	public void jump() {
		this.playerControl.jump();
	}


	public void hitByBullet() {
		this.hud.showDamageBox();
		this.moveToStartPostion();
	}


	@Override
	public void hasSuccessfullyHit(IEntity e) {
		this.incScore(20);
	}


	public void incScore(int amt) {
		this.score += amt;
		this.hud.setScore(this.score);

		if (this.score < 100) {
			this.jump();
			new AbstractHUDImage(game, module, this.hud, "Textures/text/hit.png", this.hud.hud_width, this.hud.hud_height, 2);
		} else {
			new AbstractHUDImage(game, module, this.hud, "Textures/text/winner.png", this.hud.hud_width, this.hud.hud_height, 10);
		}
	}


	@Override
	public void collidedWith(ICollideable other) {
		if (other instanceof IBullet) {
			IBullet bullet = (IBullet)other;
			if (bullet.getShooter() != this) {
				this.hitByBullet();
				bullet.getShooter().hasSuccessfullyHit(this);
			}
		} else if (other instanceof Collectable) {
			Collectable col = (Collectable)other;
			col.remove();
			this.incScore(10);
			this.hud.showCollectBox();

			// Drop new collectable
			Point p = module.mapData.getRandomCollectablePos();
			Collectable c = new Collectable(KillerCrates.instance, module, p.x, p.y);
			KillerCrates.instance.getRootNode().attachChild(c.getMainNode());

		}
	}


}
