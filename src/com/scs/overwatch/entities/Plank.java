package com.scs.overwatch.entities;

import ssmith.lang.NumberFunctions;

import com.jme3.asset.TextureKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;
import com.scs.overwatch.modules.GameModule;

public class Plank extends PhysicalEntity {

	private Geometry geometry;
	private RigidBodyControl floor_phy;
	
	public Plank(Overwatch _game, GameModule _module, float x, float z, float w, float h, float d, float rotDegrees) {
		super(_game, _module, "Plank");

		Box box1 = new Box(w/2, h/2, d/2);
		box1.scaleTextureCoordinates(new Vector2f(1, d));
		geometry = new Geometry("Crate", box1);
		int i = NumberFunctions.rnd(1,  5);
		TextureKey key3 = new TextureKey("Textures/wood_0/wood" + i + ".png");
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
		float rads = (float)Math.toRadians(rotDegrees);
		main_node.rotate(0, rads, 0);
		main_node.setLocalTranslation(x+(w/2), h/2, z+0.5f);

		floor_phy = new RigidBodyControl(1f);
		geometry.addControl(floor_phy);
		module.bulletAppState.getPhysicsSpace().add(floor_phy);
		
		this.geometry.setUserData(Settings.ENTITY, this);
		floor_phy.setUserObject(this);

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


}
