package com.scs.overwatch.entities;

import java.util.List;

import ssmith.util.RealtimeInterval;

import com.jme3.asset.TextureKey;
import com.jme3.bullet.collision.PhysicsRayTestResult;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;
import com.scs.overwatch.components.ICanShoot;
import com.scs.overwatch.components.ICollideable;
import com.scs.overwatch.components.IEntity;
import com.scs.overwatch.components.IProcessable;
import com.scs.overwatch.components.IShowOnHUD;
import com.scs.overwatch.components.ITargetByAI;
import com.scs.overwatch.modules.GameModule;
import com.scs.overwatch.weapons.IMainWeapon;
import com.scs.overwatch.weapons.LaserRifle;

public class RoamingAI extends PhysicalEntity implements IProcessable, ICanShoot, IShowOnHUD {

	private Geometry geometry;
	private RigidBodyControl floor_phy;
	private Vector3f currDir = new Vector3f(0, 0, 1);
	private Vector3f shotDir = new Vector3f(0, 0, 0);
	protected RealtimeInterval targetCheck = new RealtimeInterval(1000);
	private Vector3f lastPos;
	private IMainWeapon weapon;

	public RoamingAI(Overwatch _game, GameModule _module, float x, float z) {
		super(_game, _module, "RoamingAI");

		float w = 1f;//0.5f;
		float h = 1f;//0.5f;
		float d = 1f;//0.5f;
		
		Box box1 = new Box(w/2, h/2, d/2);
		geometry = new Geometry("Crate", box1);
		TextureKey key3 = new TextureKey("Textures/boxes and crates/1.jpg");
		key3.setGenerateMips(true);
		Texture tex3 = game.getAssetManager().loadTexture(key3);
		tex3.setWrap(WrapMode.Repeat);

		Material floor_mat = null;
		if (Settings.LIGHTING) {
			floor_mat = new Material(game.getAssetManager(),"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
			floor_mat.setTexture("DiffuseMap", tex3);
		} else {
			floor_mat = new Material(game.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
			floor_mat.setTexture("ColorMap", tex3);
		}
		geometry.setMaterial(floor_mat);
		//floor_mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		//geometry.setQueueBucket(Bucket.Transparent);

		this.main_node.attachChild(geometry);
		main_node.setLocalTranslation(x+(w/2), h/2, z+(d/2));

        CapsuleCollisionShape shape = new CapsuleCollisionShape(w, h);
		floor_phy = new RigidBodyControl(shape, 1f);
		geometry.addControl(floor_phy);
		module.bulletAppState.getPhysicsSpace().add(floor_phy);

		this.geometry.setUserData(Settings.ENTITY, this);
		floor_phy.setUserObject(this);

		module.addEntity(this);

		weapon = new LaserRifle(_game, _module, this);

	}


	@Override
	public void process(float tpf) {
		this.floor_phy.applyCentralForce(currDir.mult(5));

		if (targetCheck.hitInterval()) {
			// Check position
			if (lastPos == null) {
				lastPos = this.getMainNode().getWorldTranslation().clone();
			} else {
				float dist = this.getMainNode().getWorldTranslation().subtract(lastPos).length();
				if (dist < 0.01f) {
					this.currDir.multLocal(-1);
					Settings.p("New dir " + this.currDir);
				}
			}
			// todo
			for(IEntity e : module.entities) {
				if (e instanceof ITargetByAI) {
					ITargetByAI enemy = (ITargetByAI)e;
					if (this.canSee(enemy)) {
						Vector3f dir = enemy.getLocation().subtract(this.getLocation()).normalize();
						this.shotDir.set(dir);
						//Settings.p("AI shooting at " + enemy);
						//todo -r-add this.weapon.shoot();
					}
				}
			}

			
		}
	}


	private boolean canSee(ITargetByAI enemy) {
		List<PhysicsRayTestResult> results = module.bulletAppState.getPhysicsSpace().rayTest(this.getLocation(), enemy.getLocation());
		return results.size() <= 2;
	}
	
	
	@Override
	public void remove() {
		super.remove();
		this.module.bulletAppState.getPhysicsSpace().remove(this.floor_phy);

	}


	@Override
	public Vector3f getShootDir() {
		return shotDir;
	}


	@Override
	public void hasSuccessfullyHit(IEntity e) {
		Settings.p("AI has hit " + e.toString());
		
	}



}
