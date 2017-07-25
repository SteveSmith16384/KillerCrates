package com.scs.overwatch.entities;

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

public class SimplePillar extends PhysicalEntity {

	private static final float THICKNESS = .3f;
	private static final float HEIGHT = 4f;
	private static final String TEX = "Textures/OldRedBricks_T.jpg";

	private Geometry geometry;
	private RigidBodyControl floor_phy;
	
	public SimplePillar(Overwatch _game, GameModule _module, float x, float z) {
		super(_game, _module, "SimplePillar");

		Box vert = new Box(THICKNESS/2, HEIGHT/2, THICKNESS/2);
		vert.scaleTextureCoordinates(new Vector2f(THICKNESS, HEIGHT));
		geometry = new Geometry("Fence", vert);
		TextureKey key3 = new TextureKey(TEX);
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
		
		main_node.setLocalTranslation(x+(THICKNESS/2), HEIGHT/2, z+(THICKNESS/2));

		floor_phy = new RigidBodyControl(0);
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
