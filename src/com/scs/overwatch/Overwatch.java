package com.scs.overwatch;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Random;
import java.util.prefs.BackingStoreException;

import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.asset.plugins.ClasspathLocator;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.font.BitmapFont;
import com.jme3.system.AppSettings;
import com.scs.overwatch.modules.IModule;
import com.scs.overwatch.modules.StartModule;

public class Overwatch extends MySimpleApplication { 

	private static final String PROPS_FILE = Settings.NAME.replaceAll(" ", "") + "_settings.txt";

	public static final Random rnd = new Random();

	public static Properties properties;
	private VideoRecorderAppState video_recorder;
	private IModule currentModule, pendingModule;
	public static BitmapFont guiFont_small;// = game.getAssetManager().loadFont("Interface/Fonts/Console.fnt");
	
	public static Overwatch instance;
	
	public static void main(String[] args) {
		try {
			properties = loadProperties();
			AppSettings settings = new AppSettings(true);
			try {
				settings.load(Settings.NAME);
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
			settings.setUseJoysticks(true);
			settings.setTitle(Settings.NAME + " (v" + Settings.VERSION + ")");
			if (Settings.SHOW_LOGO) {
				//settings.setSettingsDialogImage("/game_logo.png");
			} else {
				settings.setSettingsDialogImage(null);
			}

			Overwatch app = new Overwatch();
			instance = app;
			app.setSettings(settings);
			app.setPauseOnLostFocus(true);

			/*File video, audio;
			if (Settings.RECORD_VID) {
				//app.setTimer(new IsoTimer(60));
				video = File.createTempFile("JME-water-video", ".avi");
				audio = File.createTempFile("JME-water-audio", ".wav");
				Capture.captureVideo(app, video);
				Capture.captureAudio(app, audio);
			}*/

			app.start();

			/*if (Settings.RECORD_VID) {
				System.out.println("Video saved at " + video.getCanonicalPath());
				System.out.println("Audio saved at " + audio.getCanonicalPath());
			}*/

			try {
				settings.save(Settings.NAME);
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			Settings.p("Error: " + e);
			e.printStackTrace();
		}

	}


	@Override
	public void simpleInitApp() {
		assetManager.registerLocator("assets/", FileLocator.class); // default
		assetManager.registerLocator("assets/", ClasspathLocator.class);

		//guiFont_small = getAssetManager().loadFont("Interface/Fonts/Console.fnt");
		guiFont_small = getAssetManager().loadFont("Interface/Fonts/Console.fnt");
		
		cam.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 0.01f, Settings.CAM_DIST);
		cam.setViewPort(0f, 0.5f, 0f, 0.5f); // BL

		currentModule = new StartModule(this);//GameModule(this);
		currentModule.init();
		
		if (Settings.RECORD_VID) {
			Settings.p("Recording video");
			video_recorder = new VideoRecorderAppState();
			stateManager.attach(video_recorder);
		}
	}


	@Override
	public void simpleUpdate(float tpf_secs) {
		if (this.pendingModule != null) {
			this.currentModule.destroy();
			this.rootNode.detachAllChildren();
			this.guiNode.detachAllChildren();

			// Remove existing lights
			getRootNode().getWorldLightList().clear();
			getRootNode().getLocalLightList().clear();

			this.currentModule = pendingModule;
			this.currentModule.init();
			pendingModule = null;
		}
		
		currentModule.update(tpf_secs);
	}


	public void setNextModule(IModule newModule) {
		pendingModule = newModule;
	}
	
	
	private static Properties loadProperties() throws IOException {
		String filepath = PROPS_FILE;
		File propsFile = new File(filepath);
		if (propsFile.canRead() == false) {
			// Create the properties file
			PrintWriter out = new PrintWriter(propsFile.getAbsolutePath());
			out.println("#" + Settings.NAME + " settings file");
			out.println("# If you mess up this file, just move it out the way and another will be created.");
			out.println("mapSize=25");
			out.println("numInnerWalls=5");
			out.println("numCrates=35");
			out.println("numPlanks=10");
			out.close();
		}

		Properties props = new Properties();
		props.load(new FileInputStream(new File(filepath)));
		return props;
	}



}
