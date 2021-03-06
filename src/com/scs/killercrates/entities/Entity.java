package com.scs.killercrates.entities;

import java.io.IOException;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.scs.killercrates.KillerCrates;
import com.scs.killercrates.components.IEntity;
import com.scs.killercrates.modules.GameModule;

public class Entity implements IEntity, Savable {
	
	private static int nextId = 0;
	
	public final int id;
	protected KillerCrates game;
	protected GameModule module;

	public Entity(KillerCrates _game, GameModule _module) {
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
