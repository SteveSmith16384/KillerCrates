package com.scs.killercrates.weapons;

import com.scs.killercrates.KillerCrates;
import com.scs.killercrates.components.ICanShoot;
import com.scs.killercrates.entities.KillerCrateBullet;
import com.scs.killercrates.modules.GameModule;

public class KillerCrateGun extends AbstractGun implements IMainWeapon {

	public KillerCrateGun(KillerCrates _game, GameModule _module, ICanShoot shooter) {
		super(_game, _module, 1000, shooter);
	}
	

	@Override
	public boolean shoot() {
		if (shotInterval.hitInterval()) {
			KillerCrateBullet b = new KillerCrateBullet(game, module, shooter);
			module.addEntity(b);
			return true;
		}
		return false;
	}

}
