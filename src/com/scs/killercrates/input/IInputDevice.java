package com.scs.killercrates.input;

public interface IInputDevice {

	float getFwdValue();

	float getBackValue();

	float getStrafeLeftValue();

	float getStrafeRightValue();

	boolean isJumpPressed();

	boolean isShootPressed();

	boolean isAbility1Pressed();

	void resetFlags();
}
