package com.scs.overwatch;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.scs.overwatch.map.IPertinentMapData;

public class Sky {

	public static final float HEIGHT = 9f;
	
	public Geometry geom;
	
	public Sky(AssetManager assetManager, IPertinentMapData map) {
		super();
		
		Material mat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");  // create a simple material

		Texture t = assetManager.loadTexture("Textures/sky3.jpg");
		t.setWrap(WrapMode.Repeat);
		mat.setTexture("DiffuseMap", t);

		float SIZE = map.getWidth()*10;
		Quad quad = new Quad(SIZE, SIZE);
		quad.scaleTextureCoordinates(new Vector2f(SIZE/10, SIZE/10));
		geom = new Geometry("Billboard", quad);
		geom.setMaterial(mat);

		geom.setLocalTranslation(-SIZE/2, HEIGHT, -SIZE/2);
		geom.lookAt(new Vector3f(-SIZE/2, 0f, -SIZE/2), Vector3f.UNIT_Y);
		//geom.lookAt(new Vector3f(-map.getWidth()/4, 0f, -map.getDepth()/4), Vector3f.UNIT_Y);

	}

	
}
