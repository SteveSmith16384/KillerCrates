package com.scs.overwatch;

import com.jme3.app.Application;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.AppState;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext.Type;
import com.jme3.system.JmeSystem;

public abstract class MySimpleApplication extends Application {

    public static final String INPUT_MAPPING_EXIT = "SIMPLEAPP_Exit";
    public static final String INPUT_MAPPING_CAMERA_POS = DebugKeysAppState.INPUT_MAPPING_CAMERA_POS;
    public static final String INPUT_MAPPING_MEMORY = DebugKeysAppState.INPUT_MAPPING_MEMORY;
    public static final String INPUT_MAPPING_HIDE_STATS = "SIMPLEAPP_HideStats";
                                                                         
    protected Node rootNode = new Node("Root Node");
    protected Node guiNode = new Node("Gui Node");
    protected BitmapText fpsText;
    protected BitmapFont guiFont;
    //protected FlyByCamera flyCam;
    protected boolean showSettings = true;
    private AppActionListener actionListener = new AppActionListener();
    
    private class AppActionListener implements ActionListener {

        public void onAction(String name, boolean value, float tpf) {
            if (!value) {
                return;
            }

            if (name.equals(INPUT_MAPPING_EXIT)) {
                stop();
            } else if (name.equals(INPUT_MAPPING_HIDE_STATS)){
                if (stateManager.getState(StatsAppState.class) != null) {
                    stateManager.getState(StatsAppState.class).toggleStats();
                }
            }
        }
    }

    public MySimpleApplication() {
        //this( new StatsAppState(), new MyFlyCamAppState(), new DebugKeysAppState() );
    	//StatsAppState stats = new StatsAppState();//this.guiNode, this.guiFont);
        this(new DebugKeysAppState()); // todo - re-add StatsAppState
    }

    
    public MySimpleApplication( AppState... initialStates ) {
        super();
        
        if (initialStates != null) {
            for (AppState a : initialStates) {
                if (a != null) {
                    stateManager.attach(a);
                }
            }
        }
    }
    

    @Override
    public void start() {
        // set some default settings in-case
        // settings dialog is not shown
        boolean loadSettings = false;
        if (settings == null) {
            setSettings(new AppSettings(true));
            loadSettings = true;
        }

        // show settings dialog
        if (showSettings) {
            if (!JmeSystem.showSettingsDialog(settings, loadSettings)) {
                return;
            }
        }
        //re-setting settings they can have been merged from the registry.
        setSettings(settings);
        super.start();
    }

    /**
     * Retrieves flyCam
     * @return flyCam Camera object
     *
     */
    /*public FlyByCamera getFlyByCamera() {
        return flyCam;
    }*/

    /**
     * Retrieves guiNode
     * @return guiNode Node object
     *
     */
    public Node getGuiNode() {
        return guiNode;
    }

    /**
     * Retrieves rootNode
     * @return rootNode Node object
     *
     */
    public Node getRootNode() {
        return rootNode;
    }

    public boolean isShowSettings() {
        return showSettings;
    }

    /**
     * Toggles settings window to display at start-up
     * @param showSettings Sets true/false
     *
     */
    public void setShowSettings(boolean showSettings) {
        this.showSettings = showSettings;
    }

    /**
     *  Creates the font that will be set to the guiFont field
     *  and subsequently set as the font for the stats text.
     */
    protected BitmapFont loadGuiFont() {
        return assetManager.loadFont("Interface/Fonts/Default.fnt");
    }

    @Override
    public void initialize() {
        super.initialize();

        // Several things rely on having this
        guiFont = loadGuiFont();

        guiNode.setQueueBucket(Bucket.Gui);
        guiNode.setCullHint(CullHint.Never);
        viewPort.attachScene(rootNode);
        guiViewPort.attachScene(guiNode);

        if (inputManager != null) {        
            // We have to special-case the FlyCamAppState because too
            // many SimpleApplication subclasses expect it to exist in
            // simpleInit().  But at least it only gets initialized if
            // the app state is added.
            /*if (stateManager.getState(MyFlyCamAppState.class) != null) {
                flyCam = new FlyByCamera(cam);
                flyCam.setMoveSpeed(1f); // odd to set this here but it did it before
                stateManager.getState(MyFlyCamAppState.class).setCamera( flyCam ); 
            }*/

            if (context.getType() == Type.Display) {
                inputManager.addMapping(INPUT_MAPPING_EXIT, new KeyTrigger(KeyInput.KEY_ESCAPE));
            }

            if (stateManager.getState(StatsAppState.class) != null) {
                inputManager.addMapping(INPUT_MAPPING_HIDE_STATS, new KeyTrigger(KeyInput.KEY_F5));
                inputManager.addListener(actionListener, INPUT_MAPPING_HIDE_STATS);            
            }
            
            inputManager.addListener(actionListener, INPUT_MAPPING_EXIT);            
        }

        if (stateManager.getState(StatsAppState.class) != null) {
            // Some of the tests rely on having access to fpsText
            // for quick display.  Maybe a different way would be better.
            stateManager.getState(StatsAppState.class).setFont(guiFont);
            fpsText = stateManager.getState(StatsAppState.class).getFpsText();
        }

        // call user code
        simpleInitApp();
    }

    @Override
    public void update() {
        super.update(); // makes sure to execute AppTasks
        if (speed == 0 || paused) {
            return;
        }

        float tpf = timer.getTimePerFrame() * speed;

        // update states
        stateManager.update(tpf);

        // simple update and root node
        simpleUpdate(tpf);
 
        rootNode.updateLogicalState(tpf);
        guiNode.updateLogicalState(tpf);
        
        rootNode.updateGeometricState();
        guiNode.updateGeometricState();

        // render states
        stateManager.render(renderManager);
        renderManager.render(tpf, context.isRenderable());
        simpleRender(renderManager);
        stateManager.postRender();        
    }

    public void setDisplayFps(boolean show) {
        if (stateManager.getState(StatsAppState.class) != null) {
            stateManager.getState(StatsAppState.class).setDisplayFps(show);
        }
    }

    public void setDisplayStatView(boolean show) {
        if (stateManager.getState(StatsAppState.class) != null) {
            stateManager.getState(StatsAppState.class).setDisplayStatView(show);
        }
    }

    public abstract void simpleInitApp();

    public void simpleUpdate(float tpf) {
    }

    public void simpleRender(RenderManager rm) {
    }
}
