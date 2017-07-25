package com.scs.overwatch.entities;

import com.jme3.asset.TextureKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.texture.Texture;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;
import com.scs.overwatch.components.ICanShoot;
import com.scs.overwatch.modules.GameModule;

public class GenericBullet extends PhysicalEntity {

	public ICanShoot shooter;
	private RigidBodyControl ball_phy;
	private float timeLeft;
	
	public GenericBullet(Overwatch _game, GameModule _module, ICanShoot _shooter, String tex, float mass, float speed, float dur, float rad) {
		super(_game, _module, "AbstractBullet");

		this.shooter = _shooter;
		this.timeLeft = dur;
		
		Sphere sphere = new Sphere(8, 8, rad, true, false);
		sphere.setTextureMode(TextureMode.Projected);
		/** Create a cannon ball geometry and attach to scene graph. */
		Geometry ball_geo = new Geometry("cannon ball", sphere);

		TextureKey key3 = new TextureKey(tex);
		Texture tex3 = game.getAssetManager().loadTexture(key3);
		Material floor_mat = null;
		if (Settings.LIGHTING) {
			floor_mat = new Material(game.getAssetManager(),"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
			floor_mat.setTexture("DiffuseMap", tex3);
		} else {
			floor_mat = new Material(game.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
			floor_mat.setTexture("ColorMap", tex3);
		}
		ball_geo.setMaterial(floor_mat);

		this.main_node.attachChild(ball_geo);
		game.getRootNode().attachChild(this.main_node);
		/** Position the cannon ball  */
		ball_geo.setLocalTranslation(shooter.getLocation().add(shooter.getShootDir().multLocal(PlayersAvatar.PLAYER_RAD*2)));
		/** Make the ball physical with a mass > 0.0f */
		ball_phy = new RigidBodyControl(mass);
		/** Add physical ball to physics space. */
		ball_geo.addControl(ball_phy);
		module.bulletAppState.getPhysicsSpace().add(ball_phy);
		/** Accelerate the physical ball to shoot it. */
		ball_phy.setLinearVelocity(shooter.getShootDir().mult(speed));
		
		this.getMainNode().setUserData(Settings.ENTITY, this);
		ball_phy.setUserObject(this);

	}

	
	@Override
	public void process(float tpf) {
		this.timeLeft -= tpf;
		if (this.timeLeft < 0) {
			//Settings.p("Bullet removed");
			this.remove();
		}
		
	}


	@Override
	public void remove() {
		super.remove();
		this.module.bulletAppState.getPhysicsSpace().remove(this.ball_phy);
	}

}
