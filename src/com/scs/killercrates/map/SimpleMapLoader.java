package com.scs.killercrates.map;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Node;
import com.scs.killercrates.KillerCrates;
import com.scs.killercrates.Settings;
import com.scs.killercrates.entities.Fence;
import com.scs.killercrates.entities.PhysicalEntity;
import com.scs.killercrates.entities.SimplePillar;
import com.scs.killercrates.entities.Tree;
import com.scs.killercrates.modules.GameModule;
import com.scs.killercrates.shapes.CreateShapes;

public class SimpleMapLoader implements IMapLoader {

	public static final float CEILING_HEIGHT = 3f;
	
	private KillerCrates game;
	private GameModule module;
	private Node rootNode;
	private ISimpleMapData map;
	
	public SimpleMapLoader(KillerCrates _game, GameModule _module, ISimpleMapData _data) {
		game = _game;
		module = _module;
		this.map = _data;
		this.rootNode = game.getRootNode();
		
	}


	public IPertinentMapData loadMap() {
		// Floor first
		for (int z=0 ; z<map.getDepth() ; z+= Settings.FLOOR_SECTION_SIZE) {
			for (int x=0 ; x<map.getWidth() ; x+= Settings.FLOOR_SECTION_SIZE) {
				//p("Creating floor at " + x + "," + z);
				CreateShapes.CreateFloorTL(game.getAssetManager(), module.bulletAppState, this.rootNode, x, 0f, z, Settings.FLOOR_SECTION_SIZE, 0.1f, Settings.FLOOR_SECTION_SIZE, "Textures/carpet1.jpg");//sandstone.png");
			}			
		}

		// Ceiling
		for (int z=0 ; z<map.getDepth() ; z+= Settings.FLOOR_SECTION_SIZE) {
			for (int x=0 ; x<map.getWidth() ; x+= Settings.FLOOR_SECTION_SIZE) {
				//p("Creating floor at " + x + "," + z);
				RigidBodyControl rbc = CreateShapes.CreateFloorTL(game.getAssetManager(), module.bulletAppState, this.rootNode, x, CEILING_HEIGHT, z, Settings.FLOOR_SECTION_SIZE, 0.1f, Settings.FLOOR_SECTION_SIZE, "Textures/drywall-ceiling-texture.jpg");//sandstone.png");
				module.bulletAppState.getPhysicsSpace().remove(rbc); // Remove physics so we can fall through the floor.
			}			
		}

		// Now add scenery
		for (int z=0 ; z<map.getDepth() ; z++) {
			for (int x=0 ; x<map.getWidth() ; x++) {
				int code = map.getCodeForSquare(x, z);
				switch (code) {
				case Settings.MAP_NOTHING:
					break;

				case Settings.MAP_TREE:
					PhysicalEntity tree = new Tree(game, module, x, z);
					this.rootNode.attachChild(tree.getMainNode());
					break;

				case Settings.MAP_FENCE_LR_HIGH:
					PhysicalEntity fence1 = new Fence(game, module, x, CEILING_HEIGHT, z, 0, 0);
					this.rootNode.attachChild(fence1.getMainNode());
					break;

				case Settings.MAP_FENCE_FB_HIGH:
					PhysicalEntity fence2 = new Fence(game, module, x, CEILING_HEIGHT, z, 90, 0);
					this.rootNode.attachChild(fence2.getMainNode());
					break;

				case Settings.MAP_SIMPLE_PILLAR:
					PhysicalEntity lg = new SimplePillar(game, module, x, z);
					this.rootNode.attachChild(lg.getMainNode());
					break;

				case Settings.MAP_FENCE_LR_NORMAL:
					PhysicalEntity fence1n = new Fence(game, module, x, 1.5f, z, 0, 1);
					this.rootNode.attachChild(fence1n.getMainNode());
					break;

				case Settings.MAP_FENCE_FB_NORMAL:
					PhysicalEntity fence2n = new Fence(game, module, x, 1.5f, z, 90, 1);
					this.rootNode.attachChild(fence2n.getMainNode());
					break;

				default:
					Settings.p("Ignoring map code " + code);
					//throw new RuntimeException("Unknown type:" + code);
				}
			}
		}
		
		map.addMisc();
		
		return (IPertinentMapData)map;

	}


}
