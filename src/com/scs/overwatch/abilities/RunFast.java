package com.scs.overwatch.abilities;

import com.scs.overwatch.Settings;
import com.scs.overwatch.entities.PlayersAvatar;

public class RunFast extends AbstractAbility {

	private static final float MAX_POWER = 10;
	
	private float power;
	private PlayersAvatar player;
	private boolean isRunningFast;
	
	public RunFast(PlayersAvatar _player) {
		super();
		
		player = _player;
	}

	
	@Override
	public boolean process(float interpol) {
		isRunningFast = false;
		power += interpol;
		power = Math.min(power, MAX_POWER);
		this.player.moveSpeed = Settings.DEFAULT_MOVE_SPEED;
		return true;
	}

	
	@Override
	public void activate(float interpol) {
		power -= interpol;
		power = Math.max(power, 0);
		if (power > 0) {
			this.player.moveSpeed = Settings.DEFAULT_MOVE_SPEED * 1.5f;
			isRunningFast = true;
		}
		
	}

	
	@Override
	public String getHudText() {
		return isRunningFast ? "RUNNING FAST!" : "[running normally]";
	}

}
