package com.scs.overwatch.entities;

import java.util.Iterator;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.collision.UnsupportedCollisionException;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.components.IEntity;
import com.scs.overwatch.components.IProcessable;
import com.scs.overwatch.modules.GameModule;

public abstract class PhysicalEntity extends Entity implements IProcessable {

	protected Node main_node;//, left_node, right_node;
	public String name;

	public PhysicalEntity(Overwatch _game, GameModule _module, String _name) {
		super(_game, _module);

		name = _name;

		main_node = new Node(name + "_MainNode");

		/*left_node = new Node("left_node");
		main_node.attachChild(left_node);
		left_node.setLocalTranslation(-3, 0, 0);

		right_node = new Node("right_node");
		main_node.attachChild(right_node);
		right_node.setLocalTranslation(3, 0, 0);*/
	}


	@Override
	public void remove() {
		super.remove();
		if (this.main_node.getParent() == null) {
			//todo throw new RuntimeException("No parent!");
		} else {
		this.main_node.removeFromParent(); // Don't need to remove left/right nodes as they are attached to the main node
	}
	}
	

	/*public void turnLeft(float tpf) {
		this.getMainNode().rotate(new Quaternion().fromAngleAxis(-1 * TURN_SPEED * tpf, Vector3f.UNIT_Y));
	}


	public void turnRight(float tpf) {
		this.getMainNode().rotate(new Quaternion().fromAngleAxis(1 * TURN_SPEED * tpf, Vector3f.UNIT_Y));
	}*/


	public Node getMainNode() {
		return main_node;
	}


	public float distance(PhysicalEntity o) {
		return distance(o.getMainNode().getWorldTranslation());
	}


	public float distance(Vector3f pos) {
		float dist = this.getMainNode().getWorldTranslation().distance(pos);
		return dist;
	}


	public boolean canSee(PhysicalEntity cansee) {
		Ray r = new Ray(this.getMainNode().getWorldTranslation(), cansee.getMainNode().getWorldTranslation().subtract(this.getMainNode().getWorldTranslation()).normalizeLocal());
		//synchronized (module.objects) {
		//if (go.collides) {
		CollisionResults results = new CollisionResults();
		Iterator<IEntity> it = module.entities.iterator();
		while (it.hasNext()) {
			IEntity o = it.next();
			if (o instanceof PhysicalEntity && o != this) {
				PhysicalEntity go = (PhysicalEntity)o;
				// if (go.collides) {
				if (go.getMainNode().getWorldBound() != null) {
					results.clear();
					try {
						go.getMainNode().collideWith(r, results);
					} catch (UnsupportedCollisionException ex) {
						System.out.println("Spatial: " + go.getMainNode());
						ex.printStackTrace();
					}
					if (results.size() > 0) {
						float go_dist = this.distance(cansee)-1;
						/*Iterator<CollisionResult> it = results.iterator();
							while (it.hasNext()) {*/
						CollisionResult cr = results.getClosestCollision();
						if (cr.getDistance() < go_dist) {
							return false;
						}
					}
				}
				//}
			}
		}
		return true;
	}


	@Override
	public String toString() {
		return "Entity:" + name;
	}


	public Vector3f getLocation() {
		return this.main_node.getWorldTranslation();
	}


}
