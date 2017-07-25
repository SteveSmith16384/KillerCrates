package com.scs.overwatch.map;

import java.awt.Point;

import ssmith.lang.NumberFunctions;

import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Sky;
import com.scs.overwatch.entities.Crate;
import com.scs.overwatch.entities.Plank;
import com.scs.overwatch.modules.GameModule;

public class BoxMap implements ISimpleMapData, IPertinentMapData {

	int[][] data = new int[][]{
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},  
			{0, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 0},  
			{0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0},  
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},  
			{0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0},  
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},  
			{0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0},  
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 0, 0},  
			{0, 5, 0, 0, 0, 0, 9, 0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0},  
			{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},  
			{0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0},  
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 0},  
			{0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0},  
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},  
			{0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 5, 0},  
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},  
			{0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0},  
			{0, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 0},  
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},  
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0}
	};

	private Overwatch game;
	private GameModule module;

	public BoxMap(Overwatch _game, GameModule _module) {
		game = _game;
		module = _module;
	}


	@Override
	public int getWidth() {
		return data[0].length;
	}


	@Override
	public int getDepth() {
		return data.length;
	}


	@Override
	public int getCodeForSquare(int x,  int z) {
		return data[z][x];
	}


	@Override
	public Point getPlayerStartPos(int id) {
		int x = NumberFunctions.rnd(4, getWidth()-5);
		int z = NumberFunctions.rnd(4, getDepth()-5);
		return new Point(x, z);
	}


	@Override
	public void addMisc() {
		//Sky sky = new Sky(game.getAssetManager(), module.mapData); // todo - NPE
		//game.getRootNode().attachChild(sky.geom);

		int numCrates = 35;
		try {
			numCrates = Integer.parseInt(game.properties.getProperty("numCrates"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		// Sprinkle lots of boxes
		for (int i=0 ; i<numCrates ; i++) {
			int x = NumberFunctions.rnd(4, getWidth()-5);
			int z = NumberFunctions.rnd(4, getDepth()-5);
			float w = NumberFunctions.rndFloat(.2f, 1f);
			float d = NumberFunctions.rndFloat(w, w+0.3f);
			Crate crate = new Crate(game, module, x, z, w, w, d, NumberFunctions.rnd(0, 359), module.crateTexKey);
			game.getRootNode().attachChild(crate.getMainNode());
		}

		// Sprinkle lots of planks
		int numPlanks = 10;
		try {
			numPlanks = Integer.parseInt(game.properties.getProperty("numPlanks"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		for (int i=0 ; i<numPlanks ; i++) {
			int x = NumberFunctions.rnd(4, getWidth()-5);
			int z = NumberFunctions.rnd(4, getDepth()-5);
			float w = NumberFunctions.rndFloat(.2f, .4f);
			float d = NumberFunctions.rndFloat(3f, 5f);
			Plank plank = new Plank(game, module, x, z, w, d, w, NumberFunctions.rnd(0, 359));
			game.getRootNode().attachChild(plank.getMainNode());
		}

	}


	@Override
	public Point getRandomCollectablePos() {
		int x = NumberFunctions.rnd(4, getWidth()-5);
		int z = NumberFunctions.rnd(4, getDepth()-5);
		return new Point(x, z);
	}


}
