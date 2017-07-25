package com.scs.overwatch;

/*
public class CollisionLogic {

	public static void collision(GameModule game, PhysicalEntity a, PhysicalEntity b) {
		//Settings.p(a + " has collided with " + b);

		if (a instanceof PlayersAvatar && b instanceof IBullet) {
			Player_Bullet(game, (PlayersAvatar)a, (IBullet)b);
		}
		if (a instanceof IBullet && b instanceof PlayersAvatar) {
			Player_Bullet(game, (PlayersAvatar)b, (IBullet)a);
		}

		if (a instanceof PlayersAvatar && b instanceof Collectable) {
			Player_Collectable(game, (PlayersAvatar)a, (Collectable)b);
		}
		if (a instanceof Collectable && b instanceof PlayersAvatar) {
			Player_Collectable(game, (PlayersAvatar)b, (Collectable)a);
		}
	}


	private static void Player_Collectable(GameModule module, PlayersAvatar player, Collectable col) {
		col.remove();
		player.incScore(10);
		player.hud.showCollectBox();
		
		// Drop new collectable
		Point p = module.mapData.getRandomCollectablePos();
		Collectable c = new Collectable(Overwatch.instance, module, p.x, p.y);
		Overwatch.instance.getRootNode().attachChild(c.getMainNode());
	}


	private static void Player_Bullet(GameModule module, PlayersAvatar playerHit, IBullet bullet) {
		if (bullet.getShooter() != playerHit) {
			playerHit.hitByBullet();
			bullet.getShooter().hasSuccessfullyHit(playerHit);
		}
	}


}
*/
