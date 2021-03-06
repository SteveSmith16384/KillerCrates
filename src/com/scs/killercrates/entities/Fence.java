package com.scs.killercrates.entities;

import com.jme3.asset.TextureKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.scs.killercrates.KillerCrates;
import com.scs.killercrates.Settings;
import com.scs.killercrates.modules.GameModule;

public class Fence extends PhysicalEntity {

	private static final float WIDTH = 2f;
	//private static final float HEIGHT = 1.5f;

	//private Geometry geometry;
	private RigidBodyControl floor_phy;

	public Fence(KillerCrates _game, GameModule _module, float x, float height, float z, float rot, int texCode) {
		super(_game, _module, "Fence");

		Box box1 = new Box(WIDTH/2, height/2, .1f);
		box1.scaleTextureCoordinates(new Vector2f(WIDTH, height));
		Geometry geometry = new Geometry("Fence", box1);
		TextureKey key3 = null;
		
		switch (texCode) {
		case 0:
			//TextureKey key3 = new TextureKey("Textures/bricktex.jpg");
			key3 = new TextureKey("Textures/lliella_funinthesun_paper1.jpg");//seamless_bricks/bricks.png");
			break;

		case 1:
			key3 = new TextureKey("Textures/bricktex.jpg");
			break;
		}

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

		geometry.setShadowMode(ShadowMode.CastAndReceive);
		
		// Uncomment if tex is transparent
		//floor_mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		//geometry.setQueueBucket(Bucket.Transparent);

		this.main_node.attachChild(geometry);
		float rads = (float)Math.toRadians(rot);
		main_node.rotate(0, rads, 0);
		main_node.setLocalTranslation(x+(WIDTH/2), height/2, z+0.5f);

		floor_phy = new RigidBodyControl(0);
		main_node.addControl(floor_phy);

		module.bulletAppState.getPhysicsSpace().add(floor_phy);

		geometry.setUserData(Settings.ENTITY, this);
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
