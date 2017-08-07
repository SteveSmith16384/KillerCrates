package com.scs.killercrates.input;

public interface IInputDevice {

	//boolean isFwdPressed();
	float getFwdValue();

	boolean isBackPressed();

	boolean isStrafeLeftPressed();

	boolean isStrafeRightPressed();

	boolean isJumpPressed();

	boolean isShootPressed();

	boolean isAbility1Pressed();

	void resetFlags();
}
