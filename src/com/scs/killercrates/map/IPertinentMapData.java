package com.scs.killercrates.map;

import java.awt.Point;

public interface IPertinentMapData {

	int getWidth();
	
	int getDepth();
	
	Point getPlayerStartPos(int id);
	
	Point getRandomCollectablePos();
	
}
