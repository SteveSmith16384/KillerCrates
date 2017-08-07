package com.scs.killercrates.models;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class CupboardModel extends Node {
	
	public CupboardModel(AssetManager assetManager) {
		super("CupboardModel");
		
		Spatial s = assetManager.loadModel("Models/furniture/blend/cupboard.blend");
		//s.rotate(0, 90 * FastMath.DEG_TO_RAD, 0);
		//s.scale(1.3f);
		//s.setLocalTranslation(0, -0.1f, 0);

		this.attachChild(s);
	}

}
