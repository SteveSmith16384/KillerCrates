package com.scs.overwatch.abilities;

import com.scs.overwatch.entities.PlayersAvatar;

import ssmith.lang.NumberFunctions;

public abstract class AbstractAbility implements IAbility {
	
	
	public static IAbility GetRandomAbility(PlayersAvatar _player) {
		int i = NumberFunctions.rnd(1, 3);
		switch (i) {
		case 1:
			return new JetPac(_player);
		case 2:
			return new Invisibility(_player);
		case 3:
			return new RunFast(_player);
		default:
			throw new RuntimeException("Unknown ability: " + i);
		}
		
	}
	
	
	public AbstractAbility() {
	}

}
