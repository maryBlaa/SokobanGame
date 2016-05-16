package com.sokoban.maryblaa.sokoban.collision;


import com.sokoban.maryblaa.sokoban.math.Vector2;
import com.sokoban.maryblaa.sokoban.collision.Point;

public class AABB implements Shape2D {

	private Vector2 min;
	private Vector2 max;
	private Object relatedObj;
	
	public AABB() {
		this.min = new Vector2();
		this.max = new Vector2();
	}
	
	public AABB(Vector2 min, Vector2 max) {
		this.min = new Vector2(Math.min(min.v[0], max.v[0]), Math.min(min.v[1], max.v[1]));
		this.max = new Vector2(Math.max(min.v[0], max.v[0]), Math.max(min.v[1], max.v[1]));
	}
	
	public AABB(Vector2 position, float width, float height) {
		this.min = new Vector2(position.v[0] - 0.5f * width, position.v[1] - 0.5f * height);
		this.max = new Vector2(position.v[0] + 0.5f * width, position.v[1] + 0.5f * height);
	}

	public AABB(float x, float y, float width, float height, Object relatedObj) {
		this(x, y, width, height);
		this.relatedObj = relatedObj;
	}

	public Object getRelatedObj() {
		return relatedObj;
	}

	public void setRelatedObj(Object relatedObj) {
		this.relatedObj = relatedObj;
	}

	public AABB(float x, float y, float width, float height) {
		this.min = new Vector2(x, y);
		this.max = new Vector2(x + width, y + height);
	}
	
	public boolean intersects(Shape2D shape) {
		return shape.intersects(this);
	}

	public boolean intersects(Point point) {
		Vector2 position = point.getPosition();
		if (position.getX() < min.getX() || position.getX() > max.getX()) return false;
		if (position.getY() < min.getY() || position.getY() > max.getY()) return false;
		return true;
	}

	public boolean intersects(Circle circle) {
		Vector2 center = circle.getCenter();
		
		if (center.getX() >= min.getX() && center.getX() <= max.getX()) return true;
		if (center.getY() >= min.getY() && center.getY() <= max.getY()) return true;
		
		Vector2 nearestPosition = new Vector2(
				MathHelper.clamp(center.getX(), min.getX(), max.getX()),
				MathHelper.clamp(center.getY(), min.getY(), max.getY()));
		
		float radius = circle.getRadius();
		return nearestPosition.getLengthSqr() < radius * radius;
	}

	public boolean intersects(AABB box) {
		if (this.min.getX() >= box.getMax().getX() || this.max.getX() <= box.getMin().getX()) return false;
		if (this.min.getY() >= box.getMax().getY() || this.max.getY() <= box.getMin().getY()) return false;		
		return true;
	}

	public Vector2 getPosition() {
		return new Vector2(
				0.5f * (this.min.v[0] + this.max.v[0]),
				0.5f * (this.min.v[1] + this.max.v[1]));
	}

	public void setPosition(Vector2 position) {
		Vector2 size = getSize();
		this.min.v[0] = position.v[0] - 0.5f * size.v[0];
		this.min.v[1] = position.v[1] - 0.5f * size.v[1];
		this.max.v[0] = position.v[0] + 0.5f * size.v[0];
		this.max.v[1] = position.v[1] + 0.5f * size.v[1];
	}

	public Vector2 getMin() {
		return min;
	}

	public void setMin(Vector2 min) {
		this.min.v[0] = Math.min(min.v[0], this.max.v[0]);
		this.min.v[1] = Math.min(min.v[1], this.max.v[1]);
		this.max.v[0] = Math.max(min.v[0], this.max.v[0]);
		this.max.v[1] = Math.max(min.v[1], this.max.v[1]);
	}

	public Vector2 getMax() {
		return max;
	}

	public void setMax(Vector2 max) {
		this.max.v[0] = Math.max(max.v[0], this.min.v[0]);
		this.max.v[1] = Math.max(max.v[1], this.min.v[1]);
		this.min.v[0] = Math.min(max.v[0], this.min.v[0]);
		this.min.v[1] = Math.min(max.v[1], this.min.v[1]);
	}

	public Vector2 getSize() {
		return Vector2.subtract(this.max, this.min);
	}
	
	public void setSize(Vector2 size) {
		Vector2 position = getPosition();
		this.min.v[0] = position.v[0] - 0.5f * size.v[0];
		this.min.v[1] = position.v[1] - 0.5f * size.v[1];
		this.max.v[0] = position.v[0] + 0.5f * size.v[0];
		this.max.v[1] = position.v[1] + 0.5f * size.v[1];
	}
	
}
