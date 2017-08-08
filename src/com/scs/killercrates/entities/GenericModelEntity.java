package com.scs.killercrates.entities;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.scs.killercrates.KillerCrates;
import com.scs.killercrates.Settings;
import com.scs.killercrates.components.IProcessable;
import com.scs.killercrates.modules.GameModule;

public class GenericModelEntity extends PhysicalEntity implements IProcessable {

	private RigidBodyControl floor_phy;

	public GenericModelEntity(KillerCrates _game, GameModule _module, float x, float z, float w, float h, float d, float rotDegrees, Node modelNode) {
		super(_game, _module, "Chair");

		modelNode.setShadowMode(ShadowMode.CastAndReceive);
		
		this.main_node.attachChild(modelNode);
		float rads = (float)Math.toRadians(rotDegrees);
		main_node.rotate(0, rads, 0);
		//main_node.setLocalTranslation(x+(w/2), h/2, z+0.5f);
		main_node.setLocalTranslation(x+(w/2), h/2, z+(d/2));

		floor_phy = new RigidBodyControl(1f);
		main_node.addControl(floor_phy);
		module.bulletAppState.getPhysicsSpace().add(floor_phy);

		modelNode.setUserData(Settings.ENTITY, this);
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
