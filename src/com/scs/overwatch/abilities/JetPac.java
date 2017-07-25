package com.scs.overwatch.abilities;

import com.jme3.math.Vector3f;
import com.scs.overwatch.entities.PlayersAvatar;

public class JetPac extends AbstractAbility {

	private static final Vector3f FORCE = new Vector3f(0, 1f, 0);
	private static final float MAX_FUEL = 5;

	private float fuel;
	private PlayersAvatar player;

	public JetPac(PlayersAvatar _player) {
		super();

		player = _player;
	}

	@Override
	public boolean process(float interpol) {
		fuel += interpol;
		fuel = Math.min(fuel, MAX_FUEL);
		return fuel < MAX_FUEL;
	}


	@Override
	public void activate(float interpol) {
		fuel -= (interpol*3);
		fuel = Math.max(fuel, 0);
		if (fuel > 1) {
			//Settings.p("Jetpac-ing!");
			//player.playerControl.getPhysicsRigidBody().applyImpulse(FORCE, Vector3f.ZERO);
			player.walkDirection.addLocal(FORCE);//, Vector3f.ZERO);
		}
	}

	@Override
	public String getHudText() {
		return "JetPac Fuel:" + ((int)fuel);
	}

}
