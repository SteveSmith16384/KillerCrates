package com.scs.overwatch.abilities;

public class NoAbility implements IAbility {

	public NoAbility() {
	}

	@Override
	public boolean process(float interpol) {
		return false;
	}

	
	@Override
	public void activate(float interpol) {
		// Do nothing
		
	}

	
	@Override
	public String getHudText() {
		return "[no ability]";
	}

}
