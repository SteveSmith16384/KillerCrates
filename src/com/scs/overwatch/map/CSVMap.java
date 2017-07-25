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

public class CSVMap implements ISimpleMapData, IPertinentMapData {

	private ArrayList<String> al = new ArrayList<>();

	public CSVMap(String filename) throws IOException {
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
	public int getWidth() {
		return al.get(0).split("\t").length;
	}


	@Override
	public int getDepth() {
		return al.size();
	}


	@Override
	public int getCodeForSquare(int x, int z) {
		String line = al.get(z);
		String parts[] = line.split("\t");
		return Integer.parseInt(parts[x]);
	}


	@Override
	public Point getPlayerStartPos(int id) {
		return new Point(2, 2); // todo
	}


	@Override
	public void addMisc() {
		
	}


	@Override
	public Point getRandomCollectablePos() {
		int x = NumberFunctions.rnd(4, getWidth()-5);
		int z = NumberFunctions.rnd(4, getDepth()-5);
		return new Point(x, z);
	}

}
