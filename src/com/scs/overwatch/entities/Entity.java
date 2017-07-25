package com.scs.overwatch.entities;

import java.io.IOException;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.components.IEntity;
import com.scs.overwatch.modules.GameModule;

public class Entity implements IEntity, Savable {
	
	private static int nextId = 0;
	
	public final int id;
	protected Overwatch game;
	protected GameModule module;

	public Entity(Overwatch _game, GameModule _module) {
		id = nextId++;
		game = _game;
		module = _module;
	}


	public void remove() {
		module.removeEntity(this);
	}
	
	
	@Override
	public void write(JmeExporter ex) throws IOException {
		
	}


	@Override
	public void read(JmeImporter im) throws IOException {
		
	}

}
