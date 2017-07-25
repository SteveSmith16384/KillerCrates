package com.scs.overwatch.hud;

import com.jme3.scene.Node;
import com.jme3.ui.Picture;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.components.IEntity;
import com.scs.overwatch.components.IProcessable;
import com.scs.overwatch.modules.GameModule;

public class AbstractHUDImage extends Picture implements IEntity, IProcessable {

	Overwatch game;
	private GameModule module;
	private float timeLeft;

	public AbstractHUDImage(Overwatch _game, GameModule _module, Node guiNode, String tex, float w, float h, float dur) {
		super("AbstractHUDImage");
		
		game = _game;
		module = _module;
		this.timeLeft = dur;
		
		setImage(game.getAssetManager(), tex, true);
		setWidth(w);
		setHeight(h);
		//this.setPosition(w/2, h/2);
		
		guiNode.attachChild(this);
		module.addEntity(this);
		
	}

	
	@Override
	public void process(float tpf) {
		this.timeLeft -= tpf;
		if (this.timeLeft <= 0) {
			this.removeFromParent();
			module.removeEntity(this);
		}
	}

}
