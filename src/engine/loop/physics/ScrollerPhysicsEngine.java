package engine.loop.physics;

import structures.run.RunObject;
import utils.Vector;

public class ScrollerPhysicsEngine implements IPhysicsEngine {
	
	private double precision = .5;

	@Override
	public void step(RunObject obj) {
		
		// Gravity
		obj.velocity = Vector.add(obj.velocity, obj.gravity);
		
		// Friction
		if (obj.velocity.length() >= obj.friction) {
			obj.velocity = obj.velocity.addLength(-1 * obj.friction);
		} else {
			obj.velocity = Vector.ZERO;
		}
		
		// Solid objects are special
		if (obj.solid) {
			double desiredX = obj.x + obj.velocity.x;
			double desiredY = obj.y + obj.velocity.y;
			if (Math.abs(obj.velocity.x) > Math.abs(obj.velocity.y)) {
				stepX(obj, obj.x, obj.x + obj.velocity.x);
				stepY(obj, obj.y, obj.y + obj.velocity.y);
			} else {
				stepY(obj, obj.y, obj.y + obj.velocity.y);
				stepX(obj, obj.x, obj.x + obj.velocity.x);
			}
			if (Math.abs(obj.x - desiredX) > precision) {
				obj.velocity = obj.velocity.setX(0.0);
			}
			if (Math.abs(obj.y - desiredY) > .5) {
				obj.velocity = obj.velocity.setY(0.0);
			}
		} else {
			obj.move_to(obj.velocity.x, obj.velocity.y, true);
		}
		
	}
	
	public void stepX(RunObject obj, double currentX, double desiredX) {
		if (desiredX > currentX) {
			for (double i = currentX; i <= desiredX; i += .5) {
				if (obj.collision_at(i, obj.y)) {
					break;
				}
				obj.x = i;
			}
		} else {
			for (double i = currentX; i >= desiredX; i -= .5) {
				if (obj.collision_at(i, obj.y)) {
					break;
				}
				obj.x = i;
			}			
		}
	}

	public void stepY(RunObject obj, double currentY, double desiredY) {
		if (desiredY > currentY) {
			for (double i = currentY; i <= desiredY; i += .5) {
				if (obj.collision_at(obj.x, i)) {
					break;
				}
				obj.y = i;
			}
		} else {
			for (double i = currentY; i >= desiredY; i -= .5) {
				if (obj.collision_at(obj.x, i)) {
					break;
				}
				obj.y = i;
			}			
		}
	}

}
