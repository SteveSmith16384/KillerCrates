package com.scs.overwatch.input;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.renderer.Camera;
import com.scs.overwatch.MyFlyByCamera;
import com.scs.overwatch.Settings;

public class MouseAndKeyboardCamera extends MyFlyByCamera implements ActionListener, IInputDevice { 

	private boolean left = false, right = false, up = false, down = false, jump = false, shoot = false, ability1 = false;

	public MouseAndKeyboardCamera(Camera cam, InputManager _inputManager) {
		super(cam);

		this.inputManager = _inputManager;

		this.setUpKeys();
	}


	/** We over-write some navigational key mappings here, so we can
	 * add physics-controlled walking and jumping: */
	private void setUpKeys() {
		//inputManager.clearMappings();

		inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addListener(this, "Left");
		inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addListener(this, "Right");
		inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addListener(this, "Up");
		inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addListener(this, "Down");
		inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addListener(this, "Jump");
		inputManager.addMapping("Ability1", new KeyTrigger(KeyInput.KEY_C),  new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
		inputManager.addListener(this, "Ability1");
		inputManager.addMapping("Shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addListener(this, "Shoot");

		// both mouse and button - rotation of cam
		inputManager.addMapping("FLYCAM_Left", new MouseAxisTrigger(MouseInput.AXIS_X, true),
				new KeyTrigger(KeyInput.KEY_LEFT));
		inputManager.addListener(this, "FLYCAM_Left");

		inputManager.addMapping("FLYCAM_Right", new MouseAxisTrigger(MouseInput.AXIS_X, false),
				new KeyTrigger(KeyInput.KEY_RIGHT));
		inputManager.addListener(this, "FLYCAM_Right");

		inputManager.addMapping("FLYCAM_Up", new MouseAxisTrigger(MouseInput.AXIS_Y, false),
				new KeyTrigger(KeyInput.KEY_UP));
		inputManager.addListener(this, "FLYCAM_Up");

		inputManager.addMapping("FLYCAM_Down", new MouseAxisTrigger(MouseInput.AXIS_Y, true),
				new KeyTrigger(KeyInput.KEY_DOWN));
		inputManager.addListener(this, "FLYCAM_Down");

		// mouse only - zoom in/out with wheel, and rotate drag
		/*inputManager.addMapping("FLYCAM_ZoomIn", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
		inputManager.addMapping("FLYCAM_ZoomOut", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
		inputManager.addMapping("FLYCAM_RotateDrag", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));*/

		// keyboard only WASD for movement and WZ for rise/lower height
		/*inputManager.addMapping("FLYCAM_StrafeLeft", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("FLYCAM_StrafeRight", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("FLYCAM_Forward", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("FLYCAM_Backward", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("FLYCAM_Rise", new KeyTrigger(KeyInput.KEY_Q));
		inputManager.addMapping("FLYCAM_Lower", new KeyTrigger(KeyInput.KEY_Z));*/

		//inputManager.addListener(this, mappings);  scs!
		inputManager.setCursorVisible(dragToRotate || !isEnabled());

		/*Joystick[] joysticks = inputManager.getJoysticks();
        if (joysticks != null && joysticks.length > 0){
            for (Joystick j : joysticks) {
                mapJoystick(j);
            }
        }*/
	}


	@Override
	public void onAnalog(String name, float value, float tpf) {
		if (!enabled)
			return;

		//Settings.p("name=" + name);
		//Settings.p("CAM=" +this.cam.getName());

		if (name.equals("FLYCAM_Left")){
			//Settings.p("name=" + name);
			rotateCamera(value, initialUpVec);
		}else if (name.equals("FLYCAM_Right")){
			rotateCamera(-value, initialUpVec);
		}else if (name.equals("FLYCAM_Up")){
			rotateCamera(-value * (invertY ? -1 : 1), cam.getLeft());
		}else if (name.equals("FLYCAM_Down")){
			rotateCamera(value * (invertY ? -1 : 1), cam.getLeft());
		}/*else if (name.equals("FLYCAM_Forward")){
			moveCamera(value, false);
		}else if (name.equals("FLYCAM_Backward")){
			moveCamera(-value, false);
		}else if (name.equals("FLYCAM_StrafeLeft")){
			moveCamera(value, true);
		}else if (name.equals("FLYCAM_StrafeRight")){
			moveCamera(-value, true);
		}else if (name.equals("FLYCAM_Rise")){
			riseCamera(value);
		}else if (name.equals("FLYCAM_Lower")){
			riseCamera(-value);
		}else if (name.equals("FLYCAM_ZoomIn")){
			zoomCamera(value);
		}else if (name.equals("FLYCAM_ZoomOut")){
			zoomCamera(-value);
		}*/
	}


	public void onAction(String binding, boolean isPressed, float tpf) {
		if (binding.equals("Left")) {
			left = isPressed;
		} else if (binding.equals("Right")) {
			right = isPressed;
		} else if (binding.equals("Up")) {
			up = isPressed;
		} else if (binding.equals("Down")) {
			down = isPressed;
		} else if (binding.equals("Jump")) {
			jump = isPressed;
		} else if (binding.equals("Shoot")) {
			shoot = isPressed;
		} else if (binding.equals("Ability1")) {
			ability1 = isPressed;
		}		
	}


	@Override
	public boolean isFwdPressed() {
		return up;
	}


	@Override
	public boolean isBackPressed() {
		return down;
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
	public boolean isStrafeLeftPressed() {
		return left;
	}


	@Override
	public boolean isStrafeRightPressed() {
		return right;
	}


	@Override
	public boolean isAbility1Pressed() {
		return ability1;
	}


	@Override
	public void resetFlags() {
		/*left = false;
		right = false;
		up = false;
		down = false;*/
		
	}        


}
