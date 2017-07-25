package com.scs.overwatch.entities;

import com.jme3.asset.TextureKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;
import com.scs.overwatch.components.ICollideable;
import com.scs.overwatch.components.IShowOnHUD;
import com.scs.overwatch.modules.GameModule;

public class Collectable extends PhysicalEntity implements ICollideable, IShowOnHUD {

	private static final float SIZE = .1f;

	private Geometry geometry;
	private RigidBodyControl floor_phy;
	
	public Collectable(Overwatch _game, GameModule _module, float x, float z) {
		super(_game, _module, "Collectable");

		Box box1 = new Box(SIZE, SIZE, SIZE);
		geometry = new Geometry("Collectable", box1);
		TextureKey key3 = new TextureKey("Textures/sun.jpg");
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

		this.main_node.attachChild(geometry);
		main_node.setLocalTranslation(x, 5f, z); // Drop from sky

		floor_phy = new RigidBodyControl(0.1f);
		geometry.addControl(floor_phy);

		module.bulletAppState.getPhysicsSpace().add(floor_phy);
		
		this.geometry.setUserData(Settings.ENTITY, this);
		floor_phy.setUserObject(this);

		floor_phy.setRestitution(.5f);

		module.addEntity(this); // need this to target it
	}


	@Override
	public void process(float tpf) {
		// Do nothing
	}


	@Override
	public void remove() {
		super.remove();
		this.module.bulletAppState.getPhysicsSpace().remove(this.floor_phy);
		
	}


	@Override
	public void collidedWith(ICollideable other) {
		
	}


}
