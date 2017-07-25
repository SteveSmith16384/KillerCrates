package com.scs.overwatch.map;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import ssmith.lang.NumberFunctions;

import com.scs.overwatch.Settings;

/**
 * Format is "code|w|h|d"
 *
 */
public class ComplexCSVMap implements IPertinentMapData {

	private ArrayList<String> al = new ArrayList<>();

	public ComplexCSVMap(String filename) throws IOException {
		Settings.p("Trying to load " + filename + "...");
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException ex) {
			// Try to load from jar
			br =  new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(filename)));
		}
		try {
			while (true) {
				String line = br.readLine();
				if (line == null) {
					break;
				}
				al.add(line.trim());
			}
		} finally {
			br.close();
		}
	}


	@Override
	public Point getPlayerStartPos(int id) {
		return new Point(2, 2); // todo
	}


	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getDepth() {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public Point getRandomCollectablePos() {
		int x = NumberFunctions.rnd(4, getWidth()-5);
		int z = NumberFunctions.rnd(4, getDepth()-5);
		return new Point(x, z);
	}

}
