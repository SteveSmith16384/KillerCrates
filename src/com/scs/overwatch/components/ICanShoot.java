package com.scs.overwatch.components;

import com.jme3.math.Vector3f;

public interface ICanShoot {

	Vector3f getLocation();

	Vector3f getShootDir();
	
	void hasSuccessfullyHit(IEntity e);
	
}
