package com.scs.killercrates.weapons;

import ssmith.util.RealtimeInterval;

import com.scs.killercrates.KillerCrates;
import com.scs.killercrates.components.ICanShoot;
import com.scs.killercrates.modules.GameModule;

public abstract class AbstractGun {

	protected KillerCrates game;
	protected GameModule module;
	protected ICanShoot shooter;
	protected RealtimeInterval shotInterval;// = new RealtimeInterval(1000);

	public AbstractGun(KillerCrates _game, GameModule _module, long reloadms, ICanShoot _shooter) {
		game = _game;
		module = _module;
		shooter = _shooter;
		shotInterval = new RealtimeInterval(reloadms);
	}

}
