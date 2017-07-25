package com.scs.overwatch.entities;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Spatial;
import com.scs.overwatch.JMEFunctions;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.models.Tree_arbol_seco_Model;
import com.scs.overwatch.modules.GameModule;

public class Tree extends PhysicalEntity {
	
	private Spatial floor_geo;
	private RigidBodyControl floor_phy;
	
	public Tree(Overwatch _game, GameModule _module, float x, float z) {
		super(_game, _module, "Tree");
		
		floor_geo = new Tree_arbol_seco_Model(game.getAssetManager());
		JMEFunctions.SetTextureOnSpatial(game.getAssetManager(), floor_geo, "Textures/scarybark.jpg");
		floor_geo.setLocalTranslation(x, 0, z);
		floor_geo.scale(1f + (Overwatch.rnd.nextFloat()));
		floor_geo.rotate(0, (float)(Overwatch.rnd.nextFloat() * Math.PI), 0); // rotate random amount, and maybe scale slightly

		this.main_node.attachChild(floor_geo);

		floor_phy = new RigidBodyControl(0f);
		floor_geo.addControl(floor_phy);
		module.bulletAppState.getPhysicsSpace().add(floor_phy);
		floor_phy.setFriction(1f);
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
