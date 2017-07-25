package com.scs.overwatch.input;

import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.input.JoystickAxis;
import com.jme3.input.JoystickButton;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.renderer.Camera;
import com.scs.overwatch.MyFlyByCamera;
import com.scs.overwatch.Settings;

/**
 * Class to control the direction of the camera with a joystick
 *
 */
public class JoystickCamera extends MyFlyByCamera implements IInputDevice, RawInputListener {

	protected Joystick joystick;
	private boolean strafeLeft = false, strafeRight = false, fwd = false, backward = false, jump = false, shoot = false, ability1 = false;
	private int id;

	public JoystickCamera(Camera _cam, Joystick _joystick, InputManager _inputManager) {
		super(_cam);

		this.inputManager = _inputManager;
		this.joystick = _joystick;
		id = joystick.getJoyId();

		super.setMoveSpeed(1f);
		super.setRotationSpeed(1.5f);//1.4f); 

		this.inputManager.addRawInputListener(this);

		// both mouse and button - rotation of cam
		//inputManager.addMapping("jFLYCAM_Left", new MouseAxisTrigger(MouseInput.AXIS_X, true),
		//		new KeyTrigger(KeyInput.KEY_LEFT));
		inputManager.addListener(this, "jFLYCAM_Left"+id);

		//inputManager.addMapping("jFLYCAM_Right", new MouseAxisTrigger(MouseInput.AXIS_X, false),
		//		new KeyTrigger(KeyInput.KEY_RIGHT));
		inputManager.addListener(this, "jFLYCAM_Right"+id);

		/*inputManager.addMapping("jFLYCAM_Up", new MouseAxisTrigger(MouseInput.AXIS_Y, false),
				new KeyTrigger(KeyInput.KEY_UP));*/
		inputManager.addListener(this, "jFLYCAM_Up"+id);

		/*inputManager.addMapping("jFLYCAM_Down", new MouseAxisTrigger(MouseInput.AXIS_Y, true),
				new KeyTrigger(KeyInput.KEY_DOWN));*/
		inputManager.addListener(this, "jFLYCAM_Down"+id);

		/*
        // mouse only - zoom in/out with wheel, and rotate drag
        inputManager.addMapping("FLYCAM_ZoomIn", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping("FLYCAM_ZoomOut", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        inputManager.addMapping("FLYCAM_RotateDrag", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

        // keyboard only WASD for movement and WZ for rise/lower height
        inputManager.addMapping("FLYCAM_StrafeLeft", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("FLYCAM_StrafeRight", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("FLYCAM_Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("FLYCAM_Backward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("FLYCAM_Rise", new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping("FLYCAM_Lower", new KeyTrigger(KeyInput.KEY_Z));
		 */
		inputManager.addListener(this, "jFLYCAM_StrafeLeft" + id);
		inputManager.addListener(this, "jFLYCAM_StrafeRight" + id);
		inputManager.addListener(this, "jFLYCAM_Forward" + id);
		inputManager.addListener(this, "jFLYCAM_Backward" + id);

		/*      inputManager.addListener(this, mappings);
        inputManager.setCursorVisible(dragToRotate || !isEnabled());
		 */
		/*Joystick[] joysticks = inputManager.getJoysticks();
		if (joysticks != null && joysticks.length > 0){
			for (Joystick j : joysticks) {
				if (j == joystick) {*/
		mapJoystick(joystick, id);
		/*}
			}
		}*/
	}


	protected void mapJoystick( Joystick joystick, int id ) {
		// Map it differently if there are Z axis
		if( joystick.getAxis( JoystickAxis.Z_ROTATION ) != null && joystick.getAxis( JoystickAxis.Z_AXIS ) != null ) {

			// Make the left stick move
			joystick.getXAxis().assignAxis( "jFLYCAM_StrafeRight"+id, "jFLYCAM_StrafeLeft"+id );
			joystick.getYAxis().assignAxis( "jFLYCAM_Backward"+id, "jFLYCAM_Forward"+id );

			// And the right stick control the camera                       
			joystick.getAxis( JoystickAxis.Z_ROTATION ).assignAxis( "jFLYCAM_Down"+id, "jFLYCAM_Up"+id );
			joystick.getAxis( JoystickAxis.Z_AXIS ).assignAxis(  "jFLYCAM_Right"+id, "jFLYCAM_Left"+id );
		} else {             
			joystick.getPovXAxis().assignAxis("jFLYCAM_StrafeRight"+id, "jFLYCAM_StrafeLeft"+id);
			joystick.getPovYAxis().assignAxis("jFLYCAM_Forward"+id, "jFLYCAM_Backward"+id);
			joystick.getXAxis().assignAxis("jFLYCAM_Right"+id, "jFLYCAM_Left"+id);
			joystick.getYAxis().assignAxis("jFLYCAM_Down"+id, "jFLYCAM_Up"+id);
		}
	}


	@Override
	public boolean isFwdPressed() {
		return fwd;
	}


	@Override
	public boolean isBackPressed() {
		return backward;
	}


	@Override
	public boolean isStrafeLeftPressed() {
		return strafeLeft;
	}


	@Override
	public boolean isStrafeRightPressed() {
		return strafeRight;
	}


	@Override
	public boolean isJumpPressed() {
		return jump;
	}


	@Override
	public boolean isShootPressed() {
		return shoot;
	}


	@Override
	public boolean isAbility1Pressed() {
		return ability1;
	}


	@Override
	public void onAnalog(String name, float value, float tpf) {
		if (!enabled)
			return;

		float CUTOFF = 0.0015f;
		//Settings.p("name=" + name + "  value=" + value);

		if (name.equals("jFLYCAM_Left" + id)) {
			rotateCamera(value, initialUpVec);
		} else if (name.equals("jFLYCAM_Right" + id)) {
			rotateCamera(-value, initialUpVec);
		} else if (name.equals("jFLYCAM_Up" + id)) {
			rotateCamera(-value * (invertY ? -1 : 1), cam.getLeft());
		} else if (name.equals("jFLYCAM_Down" + id)) {
			if (value > CUTOFF) { // SCS
				rotateCamera(value * (invertY ? -1 : 1), cam.getLeft());
			}
		} else if (name.equals("jFLYCAM_Forward" + id)) {
			fwd = value > CUTOFF;
			//if (fwd) 
			Settings.p("fwd:" + value);
			//moveCamera(value, false);
		} else if (name.equals("jFLYCAM_Backward" + id)) {
			backward = value > CUTOFF;
			if (backward) Settings.p("backward:" + value);
			//moveCamera(-value, false);
		} else if (name.equals("jFLYCAM_StrafeLeft" + id)) {
			strafeLeft = value > CUTOFF;
			//moveCamera(value, true);
		} else if (name.equals("jFLYCAM_StrafeRight" + id)) {
			strafeRight = value > CUTOFF;
			//moveCamera(-value, true);
		}

		/*else if (name.equals("FLYCAM_Rise")){
			riseCamera(value);
		}else if (name.equals("FLYCAM_Lower")){
			riseCamera(-value);
		}else if (name.equals("FLYCAM_ZoomIn")){
			zoomCamera(value);
		}else if (name.equals("FLYCAM_ZoomOut")){
			zoomCamera(-value);
		}*/
	}


	@Override
	public void resetFlags() {
		strafeLeft = false;
		strafeRight = false;
		fwd = false;
		backward = false;

	}        


	// Raw Input Listener ------------------------

	@Override
	public void onJoyAxisEvent(JoyAxisEvent evt) {
		/*Joystick stick = evt.getAxis().getJoystick();
		if (stick == joystick) {
			if (evt.getValue() == 0) {
				JoystickAxis axis = evt.getAxis();
				if( axis == axis.getJoystick().getXAxis() ) {
					Settings.p("Xaxis=" + evt.getValue());
				} else if( axis == axis.getJoystick().getYAxis() ) {
					//Settings.p("Yaxis=" + evt.getValue()); 
				}
			}
		}*/
	}

	/*
	 * (non-Javadoc)
	 * @see com.jme3.input.RawInputListener#onJoyButtonEvent(com.jme3.input.event.JoyButtonEvent)
	 * 1 = X
	 * 2 = O
	 * 5 = R1
	 * 7 = R2
	 */
	@Override
	public void onJoyButtonEvent(JoyButtonEvent evt) {
		Joystick stick = evt.getButton().getJoystick();
		if (stick == joystick) {
			//setButtonValue( evt.getButton(), evt.isPressed() );
			JoystickButton button = evt.getButton();
			Settings.p("button.getButtonId()=" + button.getButtonId());
			if (button.getButtonId() == 1) {
				this.jump = evt.isPressed();
			} else if (button.getButtonId() == 5 || button.getButtonId() == 7) {
				this.shoot = evt.isPressed();
			} else if (button.getButtonId() == 2) {
				this.ability1 = evt.isPressed();
			}

		}
	}

	public void beginInput() {}
	public void endInput() {}
	public void onMouseMotionEvent(MouseMotionEvent evt) {}
	public void onMouseButtonEvent(MouseButtonEvent evt) {}
	public void onKeyEvent(KeyInputEvent evt) {}
	public void onTouchEvent(TouchEvent evt) {}


	// End of Raw Input Listener

}
