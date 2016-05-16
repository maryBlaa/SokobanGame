package com.sokoban.maryblaa.sokoban.collision;

import com.sokoban.maryblaa.sokoban.math.Vector2;

public class Circle implements Shape2D {

	private Vector2 center;
	private float radius;
	
	public Circle() {
		this.center = new Vector2();
		this.radius = 0.0f;
	}
	
	public Circle(Vector2 center, float radius) {
		this.center = new Vector2(center.v[0], center.v[1]);
		this.radius = radius;
	}
	
	public Circle(float x, float y, float radius) {
		this.center = new Vector2(x, y);
		this.radius = radius;
	}
	
	public boolean intersects(Shape2D shape) {
		return shape.intersects(this);
	}

	public boolean intersects(Point point) {
		float distSqr = Vector2.subtract(point.getPosition(), this.getCenter()).getLengthSqr(); 
		return distSqr <= radius * radius;
	}

	public boolean intersects(Circle circle) {
		float distSqr = Vector2.subtract(circle.center, this.center).getLengthSqr();
		return distSqr <= (this.radius + circle.radius) * (this.radius + circle.radius); 
	}

	public boolean intersects(AABB box) {
		Vector2 min = box.getMin();
		Vector2 max = box.getMax();
		
		if (center.getX() >= min.getX() && center.getX() <= max.getX()) return true;
		if (center.getY() >= min.getY() && center.getY() <= max.getY()) return true;
		
		Vector2 nearestPosition = new Vector2(
				MathHelper.clamp(center.getX(), min.getX(), max.getX()),
				MathHelper.clamp(center.getY(), min.getY(), max.getY()));
		
		return nearestPosition.getLengthSqr() < radius * radius;
	}
	
	public Vector2 getPosition() {
		return center;
	}

	public void setPosition(Vector2 position) {
		this.center.v[0] = position.v[0];
		this.center.v[1] = position.v[1];
	}

	public Vector2 getCenter() {
		return center;
	}

	public void setCenter(Vector2 center) {
		this.center = center;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

}
