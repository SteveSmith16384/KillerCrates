package com.scs.overwatch.input;

public interface IInputDevice {

	boolean isFwdPressed();

	boolean isBackPressed();

	boolean isStrafeLeftPressed();

	boolean isStrafeRightPressed();

	boolean isJumpPressed();

	boolean isShootPressed();

	boolean isAbility1Pressed();

	void resetFlags();
}
