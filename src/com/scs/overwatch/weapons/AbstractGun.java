package com.scs.overwatch.weapons;

import ssmith.util.RealtimeInterval;

import com.scs.overwatch.Overwatch;
import com.scs.overwatch.components.ICanShoot;
import com.scs.overwatch.modules.GameModule;

public abstract class AbstractGun {

	protected Overwatch game;
	protected GameModule module;
	protected ICanShoot shooter;
	protected RealtimeInterval shotInterval;// = new RealtimeInterval(1000);

	public AbstractGun(Overwatch _game, GameModule _module, long reloadms, ICanShoot _shooter) {
		game = _game;
		module = _module;
		shooter = _shooter;
		shotInterval = new RealtimeInterval(reloadms);
	}

}
