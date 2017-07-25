package com.scs.killercrates.map;

public interface ISimpleMapData {

	int getWidth();
	
	int getDepth();
	
	int getCodeForSquare(int x, int z);
	
	void addMisc();

}
