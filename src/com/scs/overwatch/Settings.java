package com.scs.overwatch;

public class Settings {
	
	public enum GameMode {KillerCrates, BladeRunner}
	
	public static final GameMode gameMode = GameMode.BladeRunner;
	public static final String VERSION = "0.01";
	public static final boolean DEBUG_TARGETTER = false;
	public static final boolean DEBUG_AI = true;
	public static final boolean SHOW_LOGO = false;
	public static final boolean ALWAYS_SHOW_4_CAMS = false;
	public static final boolean DEBUG_HUD = false;
	public static final boolean RECORD_VID = false;
	
	// Our movement speed
	public static final float DEFAULT_MOVE_SPEED = 3f;
	public static final float DEFAULT_STRAFE_SPEED = 3f;

	public static final float CAM_DIST = 50f;
	public static final int FLOOR_SECTION_SIZE = 12;
	public static final boolean LIGHTING = true;
	public static final String NAME = "Killer Crates";
	
	// User Data
	public static final String ENTITY = "Entity";
	
	// Map codes
	public static final int MAP_NOTHING = 0;
	public static final int MAP_TREE = 1;
	//public static final int MAP_PLAYER = 2;
	public static final int MAP_FENCE_LR_HIGH = 4;
	public static final int MAP_FENCE_FB_HIGH = 5;
	public static final int MAP_SIMPLE_PILLAR = 7;
	public static final int MAP_FENCE_LR_NORMAL = 8;
	public static final int MAP_FENCE_FB_NORMAL = 9;

	
	public static void p(String s) {
		System.out.println(System.currentTimeMillis() + ": " + s);
	}


}
