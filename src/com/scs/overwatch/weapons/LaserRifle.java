package com.scs.overwatch.weapons;

import com.scs.overwatch.Overwatch;
import com.scs.overwatch.components.ICanShoot;
import com.scs.overwatch.entities.LaserBullet;
import com.scs.overwatch.modules.GameModule;

public class LaserRifle extends AbstractGun implements IMainWeapon {

	public LaserRifle(Overwatch _game, GameModule _module, ICanShoot shooter) {
		super(_game, _module, 300, shooter);
	}
	

	@Override
	public boolean shoot() {
		if (shotInterval.hitInterval()) {
			LaserBullet b = new LaserBullet(game, module, shooter);
			module.addEntity(b);
			return true;
		}
		return false;
	}

}
