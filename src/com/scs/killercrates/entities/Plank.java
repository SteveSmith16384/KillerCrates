package com.scs.killercrates.entities;

import ssmith.lang.NumberFunctions;

import com.jme3.asset.TextureKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.BufferUtils;
import com.scs.killercrates.KillerCrates;
import com.scs.killercrates.Settings;
import com.scs.killercrates.modules.GameModule;

public class Plank extends PhysicalEntity {

	private RigidBodyControl floor_phy;
	
	public Plank(KillerCrates _game, GameModule _module, float x, float z, float w, float h, float d, float rotDegrees) {
		super(_game, _module, "Plank");

		Box box1 = new Box(w/2, h/2, d/2);
		//box1.scaleTextureCoordinates(new Vector2f(1, d));
		box1.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(new float[]{
				0, h, w, h, w, 0, 0, 0, // back
				0, h, d, h, d, 0, 0, 0, // right
		        0, h, w, h, w, 0, 0, 0, // front
		        0, h, d, h, d, 0, 0, 0, // left
		        w, 0, w, d, 0, d, 0, 0, // top
		        w, 0, w, d, 0, d, 0, 0  // bottom
				}));


		Geometry geometry = new Geometry("Crate", box1);
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
		
		geometry.setShadowMode(ShadowMode.CastAndReceive);
		
		this.main_node.attachChild(geometry);
		float rads = (float)Math.toRadians(rotDegrees);
		main_node.rotate(0, rads, 0);
		main_node.setLocalTranslation(x+(w/2), h/2, z+0.5f);

		floor_phy = new RigidBodyControl(1f);
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
