package com.scs.killercrates.map;

import java.awt.Point;

import ssmith.lang.NumberFunctions;

import com.jme3.scene.Node;
import com.scs.killercrates.KillerCrates;
import com.scs.killercrates.Settings;
import com.scs.killercrates.entities.Fence;
import com.scs.killercrates.entities.GenericModelEntity;
import com.scs.killercrates.entities.PhysicalEntity;
import com.scs.killercrates.entities.Plank;
import com.scs.killercrates.modules.GameModule;

public class ConfigMap implements ISimpleMapData, IPertinentMapData {

	private KillerCrates game;
	private GameModule module;

	public ConfigMap(KillerCrates _game, GameModule _module) {
		game = _game;
		module = _module;
	}


	@Override
	public int getWidth() {
		return game.getPropertyAsInt("mapSize", 20);
	}


	@Override
	public int getDepth() {
		return game.getPropertyAsInt("mapSize", 25);
	}


	@Override
	public int getCodeForSquare(int x,  int z) {
		return Settings.MAP_NOTHING;
	}


	@Override
	public Point getPlayerStartPos(int id) {
		int x = NumberFunctions.rnd(4, getWidth()-5);
		int z = NumberFunctions.rnd(4, getDepth()-5);
		return new Point(x, z);
	}


	@Override
	public void addMisc() {
		// Add outer walls
		for (int x=2 ; x<this.getWidth() ; x+=2) {
			PhysicalEntity fence1 = new Fence(game, module, x, SimpleMapLoader.CEILING_HEIGHT, 1, 0, 0);
			game.getRootNode().attachChild(fence1.getMainNode());

			PhysicalEntity fence2 = new Fence(game, module, x, SimpleMapLoader.CEILING_HEIGHT, this.getDepth()-2, 0, 0);
			game.getRootNode().attachChild(fence2.getMainNode());

		}
		
		for (int y=2 ; y<this.getDepth() ; y+=2) {
			PhysicalEntity fence1 = new Fence(game, module, 1, SimpleMapLoader.CEILING_HEIGHT, y, 90, 0);
			game.getRootNode().attachChild(fence1.getMainNode());

			PhysicalEntity fence2 = new Fence(game, module, this.getWidth()-2, SimpleMapLoader.CEILING_HEIGHT, y, 90, 0);
			game.getRootNode().attachChild(fence2.getMainNode());

		}
		
		
		
		int numFurniture = game.getPropertyAsInt("numFurniture", 35);
		
		// Sprinkle lots of boxes
		for (int i=0 ; i<numFurniture ; i++) {
			int x = NumberFunctions.rnd(4, getWidth()-5);
			int z = NumberFunctions.rnd(4, getDepth()-5);
			float w = NumberFunctions.rndFloat(.2f, 1f);
			float d = NumberFunctions.rndFloat(w, w+0.3f);
			//Crate crate = new Crate(game, module, x, z, w, w, d, NumberFunctions.rnd(0, 359), module.crateTexKey);
			/*int id = NumberFunctions.rnd(1, 3);
			Node model = null;
			switch (id) {
			case 1:
				model = new ChairModel(game.getAssetManager());
				break;
			case 2:
				model = new TableSimpleModel(game.getAssetManager());
				break;
			case 3:
				model = new StoolModel(game.getAssetManager());
				break;
			}*/
			Node model = game.getRandomModel(false);
			GenericModelEntity crate = new GenericModelEntity(game, module, x, z, w, w, d, NumberFunctions.rnd(0, 359), model);
			game.getRootNode().attachChild(crate.getMainNode());
		}

		// Sprinkle lots of planks
		/*int numPlanks = game.getPropertyAsInt("numPlanks", 10);
		for (int i=0 ; i<numPlanks ; i++) {
			int x = NumberFunctions.rnd(4, getWidth()-5);
			int z = NumberFunctions.rnd(4, getDepth()-5);
			float w = NumberFunctions.rndFloat(.2f, .4f);
			float d = NumberFunctions.rndFloat(3f, 5f);
			Plank plank = new Plank(game, module, x, z, w, d, w, NumberFunctions.rnd(0, 359));
			game.getRootNode().attachChild(plank.getMainNode());
		}*/

	}


	@Override
	public Point getRandomCollectablePos() {
		int x = NumberFunctions.rnd(4, getWidth()-5);
		int z = NumberFunctions.rnd(4, getDepth()-5);
		return new Point(x, z);
	}

}
