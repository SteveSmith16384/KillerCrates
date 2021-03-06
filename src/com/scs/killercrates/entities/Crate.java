package com.scs.killercrates.entities;

import com.jme3.asset.TextureKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.scs.killercrates.KillerCrates;
import com.scs.killercrates.Settings;
import com.scs.killercrates.components.IProcessable;
import com.scs.killercrates.modules.GameModule;

public class Crate extends PhysicalEntity implements IProcessable {

	//private static final Vector3f JUMP = new Vector3f(0, -50, 0);

	//private Geometry geometry;
	private RigidBodyControl floor_phy;

	public Crate(KillerCrates _game, GameModule _module, float x, float z, float w, float h, float d, float rotDegrees, TextureKey key3) {
		super(_game, _module, "Crate");

		Box box1 = new Box(w/2, h/2, d/2);
		//box1.scaleTextureCoordinates(new Vector2f(WIDTH, HEIGHT));
		Geometry geometry = new Geometry("Crate", box1);
		//TextureKey key3 = new TextureKey("Textures/crate.png");
		//TextureKey key3 = new TextureKey("Textures/boxes and crates/1.jpg");
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

		geometry.setShadowMode(ShadowMode.CastAndReceive);
		
		this.main_node.attachChild(geometry);
		float rads = (float)Math.toRadians(rotDegrees);
		main_node.rotate(0, rads, 0);
		//main_node.setLocalTranslation(x+(w/2), h/2, z+0.5f);
		main_node.setLocalTranslation(x+(w/2), h/2, z+(d/2));

		floor_phy = new RigidBodyControl(1f);
		main_node.addControl(floor_phy);
		module.bulletAppState.getPhysicsSpace().add(floor_phy);

		geometry.setUserData(Settings.ENTITY, this);
		main_node.setUserData(Settings.ENTITY, this);
		floor_phy.setUserObject(this);

	}


	@Override
	public void process(float tpf) {
	}


	@Override
	public void remove() {
		super.remove();
		this.module.bulletAppState.getPhysicsSpace().remove(this.floor_phy);

	}


}
